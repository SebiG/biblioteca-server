package biblioteca.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import biblioteca.model.Book;
import biblioteca.model.Record;
import biblioteca.model.Review;
import biblioteca.model.User;


@FunctionalInterface
interface InputDataHandlerInterface<T> {

	// abstract method
	String executeTask(T inputData);
}

public class CommandService implements Runnable {
	private final ServerSocket serverSocket;
	private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static BookService bookService = new BookService();
	private static RecordService recordService = new RecordService();
	private static UserService userService = new UserService();
	
	// store one lambda for each command
	private static final HashMap<String, InputDataHandlerInterface<JsonObject>> map = new HashMap<String, InputDataHandlerInterface<JsonObject>>() {

		private static final long serialVersionUID = 1L;

		{
			put("login", (login) -> {
				//dummy login
				JsonObject obj = new JsonObject();
				String name = login.get("userName").getAsString();
				String password = login.get("password").getAsString();
				User user = null;
				try {
					user = userService.checkLogin(name, password);
				} catch (Exception e) {
					obj.addProperty("message", "Cant find user! Try again. (cititor sau bibliotecar)");
					return obj.toString();
				}
				
				obj.addProperty("userName", user.getUserName());
				obj.addProperty("userID", user.getUserID());
				if(user.getRole() == 1) {
					obj.addProperty("userRole", "user");
					obj.addProperty("message", "User login success!");
					return obj.toString();
				}
				if(user.getRole() == 0) {
					obj.addProperty("userRole", "admin");
					obj.addProperty("message", "Admin login success!");
					return obj.toString();
				}
				obj.addProperty("message", "Fail, check userName or password!");
				return obj.toString();
			});
			
			put("getBooks", (request) -> {
				String gsonList = null;
				if(request.has("filter")) {
					if(request.get("filter").getAsString().equals("all")) {
						gsonList = gson.toJson(bookService.getAllBooks());
					}
				}
				System.out.println(gsonList);
				return gsonList;
			});
			
			//rezerva cartea (pt. user)
			put("reserved", (request) -> {
				JsonObject obj = new JsonObject();

				try {
					Book b = bookService.findBook(request.get("bookID").getAsString());
					User u = userService.findUser(request.get("userID").getAsString());
					if(b.getStock() < 1) {
						//TODO create custom exception, inform client to update stock and to try again later
						throw new Exception("Book not in stock!");
					}
					b.decrementStock();
					bookService.updateBook(b);
					Record r = new Record(u, b, 1);
					recordService.addRecord(r);
					obj.addProperty("message", "ok");
				} catch (Exception e) {
					obj.addProperty("message", "error");
					e.printStackTrace();
				}
				return obj.toString();
			});
			
			put("getRecords", (request) -> {
				String gsonList = null;
				if(request.has("userID")) {
					if(request.get("userID").getAsString().equals("All")) {
						List<Record> rl = recordService.getAllRecords();
						gsonList = gson.toJson(rl);
					} else {
						User u;
						try {
							u = userService.findUser(request.get("userID").getAsString());
							gsonList = gson.toJson(u.getRecords());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println(gsonList);
				return gsonList;
			});
			
			put("getRecord", (request) -> {
				String json = null;
				if(request.has("recordID")) {
					Record r;
					try {
						r = recordService.findRecord(request.get("recordID").getAsString());
						json = gson.toJson(r);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return json;
			});
			
			put("putBook", (request) -> {
				Book book = new Book();
				book.setTitle(request.get("title").getAsString());
				book.setAuthor(request.get("authors").getAsString());
				book.setStock(request.get("stock").getAsInt());
				if(request.has("title")) {
					try {
						bookService.addBook(book);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				return gson.toJson(List.of(book));
			});
			
			put("setStateForRecord", (request) -> {
				JsonObject obj = new JsonObject();
				if(request.has("recordID")) {
					try {
						recordService.updateRecordState(
							request.get("recordID").getAsString(), 
							request.get("state").getAsInt()
						);
						obj.addProperty("message", "ok");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						obj.addProperty("message", "error");
					}
				}
				return obj.toString();
			});
			
			put("getReviewsForBook", (request) -> {
				String gsonList = null;
				Book book = null;
				if(request.has("bookID")) {
					try {
						book = bookService.findBook(request.get("bookID").getAsString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<Review> reviews = book.getReviews();
					gsonList = gson.toJson(reviews);
				}
				System.out.println(gsonList);
				return gsonList;
			});
			
			put("putReviewForBook", (request) -> {
				Review review = new Review();
				if(request.has("bookID")) {
					String userID = request.get("userID").getAsString();
					String bookID = request.get("bookID").getAsString();
					User u;
					try {
						u = userService.findUser(userID);
						review.setUser(u);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Book b;
					try {
						b = bookService.findBook(bookID);
						review.setBook(b);
						review.setReview(request.get("review").getAsString());
						b.addReview(review);
						bookService.updateBook(b);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return gson.toJson(List.of(review));
			});
		}
	};

	public CommandService(int port) throws IOException {
		// Create server socket and set the timeout for serverSocket.accept method
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(250);
	}

	@Override
	public void run() {
		try {
			accept();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void accept() throws IOException {
		System.out.println("Accepting connections on port " + serverSocket.getLocalPort());

		// Loop until the thread is interrupted
		while (!Thread.interrupted()) {
			// Use a try-with resources to instantiate the client socket and
			// buffers for reading and writing messages from and to the client
			try (Socket socket = serverSocket.accept();

					BufferedWriter bufferedOutputWriter = new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream()));
					BufferedReader bufferedInputReader = new BufferedReader(
							new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));) {

				System.out.println("Connection accepted");

				// Read the command and data from the client
				String receivedCommand = bufferedInputReader.readLine();
				String receivedData = bufferedInputReader.readLine();
				String result;
				try {
					JsonObject jsonObject = new JsonParser().parse(receivedData).getAsJsonObject();
					result = map.get(receivedCommand).executeTask(jsonObject);				
					if (result != null && !result.isEmpty()) {
						System.out.println(result);
					} else {
						System.out.println("Unexpected command");
					}
				} catch (Exception e){
					System.out.println("Error: " + e.toString());
					JsonObject obj = new JsonObject();
					obj.addProperty("message", "Login bug, contact dev."); //TODO: generalize message
					result = obj.toString();
				}
				System.out.println("Command received: " + receivedCommand);
				System.out.println("Data received: " + receivedData);

				outputToClient(bufferedOutputWriter, result, true);

			} catch (SocketTimeoutException ste) {
				// timeout every .25 seconds to see if interrupted
			}
		}

		System.out.println("server connection end");
	}

	// Helper methods to send data to client
	private void outputToClient(BufferedWriter bufferedOutputWriter, String message, boolean withNewLine)
			throws IOException {

		bufferedOutputWriter.write(message);

		if (withNewLine) {
			bufferedOutputWriter.newLine();
		}

		bufferedOutputWriter.flush();
	}
}
