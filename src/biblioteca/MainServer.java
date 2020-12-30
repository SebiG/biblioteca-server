package biblioteca;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import biblioteca.service.CommandService;

public class MainServer {

	public static final int PORT = 9001;

	public static void main(String[] args) {

        // Thread-pool that creates new threads as needed
		ExecutorService exSrv = Executors.newCachedThreadPool();

		try {
			// Create the Runnable class and submit it to the executor service
			CommandService server = new CommandService(PORT);
			exSrv.submit(server);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create a new instance of the AutomaticHomeworkReminder which will
		// show an automated message every 30 seconds
		// new AutomaticReminder();
	}
}

