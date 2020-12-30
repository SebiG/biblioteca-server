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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import biblioteca.model.User;


@FunctionalInterface
interface InputDataHandlerInterface<T> {

	// abstract method
	String executeTask(T inputData);
}

public class CommandService implements Runnable {
	private final ServerSocket serverSocket;
	// store one lambda for each command
	private static final HashMap<String, InputDataHandlerInterface<JsonObject>> map = new HashMap<String, InputDataHandlerInterface<JsonObject>>() {

		private static final long serialVersionUID = 1L;

		{
			put("login", (login) -> {
				JsonObject obj = new JsonObject();
				String name = login.get("userName").getAsString();
				String password = login.get("password").getAsString();
				UserService userService = new UserService();
				User user = null;
				try {
					user = userService.findUser(name, password);
				} catch (Exception e) {
					// e.printStackTrace();
					obj.addProperty("message", "Cant find user! Try again. (cititor sau bibliotecar)");
					return obj.toString();
				}
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
					if (!result.isEmpty()) {
						System.out.println(result);
					} else {
						System.out.println("Unexpected command");
					}
				} catch (Exception e){
					System.out.println("Error: " + e.toString());
					JsonObject obj = new JsonObject();
					obj.addProperty("message", "Login bug, contact dev.");
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
