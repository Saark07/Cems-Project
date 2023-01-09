package Server;

import java.io.IOException;
import java.net.BindException;

import controllers.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerUI extends Application {
	static ServerController sc = new ServerController();
	final public static int DEFAULT_PORT = 5555;
	static EchoServer sv;
	static boolean islistening = false;
	/**
	 * @author Vladi
	 *	user interface for server starting connection window
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerController server = new ServerController(); // create server frame
		server.start(primaryStage);

	}
	/**
	 * @param p
	 * @throws IOException
	 * method for listening for connections requests
	 */
	public static void runServer(int p) throws IOException {
		int port=p;
		try {
			sv = new EchoServer(port);
			ServerLog.writeNewLine("Server listening for connections on port " + port);
			sv.listen(); // Start listening for connections
			islistening = true;
		} catch (BindException ex) {
			ServerLog.writeNewLine("port " + port + " already in use");
		}
	}

	
	
}