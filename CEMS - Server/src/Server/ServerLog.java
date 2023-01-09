package Server;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import controllers.ServerController;
/**
 * @author Vladi
 *	this class is for handling messages for the server log
 */
public class ServerLog {
	static ServerController sc;

	/**
	 * @param controller
	 * setting up the server controller
	 */
	public static void setServerController(ServerController controller) {
		sc = controller;
	}
	/**
	 * @param msg
	 * method for writing a message to the server log
	 */
	public static void writeNewLine(String msg) {
		String timeStamp = new SimpleDateFormat("[HH:mm:ss]  ").format(Calendar.getInstance().getTime());
		sc.AddMsgToLog(timeStamp + msg + "\n");

	}
}
