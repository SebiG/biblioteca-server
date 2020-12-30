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

//import com.google.gson.Gson;
//import biblioteca.model.User;

@FunctionalInterface
interface InputDataHandlerInterface<T> {

	// abstract method
	String executeTask(T inputData);
}

public class CommandService implements Runnable {
	private final ServerSocket serverSocket;
	// store one lambda for each command
	private static final HashMap<String, InputDataHandlerInterface<String>> map = new HashMap<String, InputDataHandlerInterface<String>>() {

		private static final long serialVersionUID = 1L;

		{
			put("login", (user) -> {
				System.out.println(user);
				return "test from server";
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

				System.out.println("Command received:  " + receivedCommand);
				System.out.println("Data received:  " + receivedData);

				String result = map.get(receivedCommand).executeTask(receivedData);

				if (!result.isEmpty()) {
					System.out.println(result);
				} else {
					System.out.println("Unexpected command");
				}
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
