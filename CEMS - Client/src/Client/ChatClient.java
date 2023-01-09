// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package Client;

import java.io.IOException;
import java.util.ArrayList;

import common.ChatIF;
import controllers.LoginController;
import logic.CheckMistakes;
import logic.DurationRequests;
import logic.Person;
import logic.Question;
import logic.Test;
import logic.User;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */

public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;

	public static boolean awaitResponse = false;

	@SuppressWarnings("unused")
	private LoginController loginController;
	private static ChatClient client = null;
	public static User user;
	public static String Permission;
	public static Person person;
	public static String fullname;
	public static String Qinstruction;
	public static Question qst;
	public static Test test;
	public static int Value = 0;
	public static String TeacherID;
	public static String Code;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	public ChatClient(String host, int port) throws Exception {
		super(host, port); // Call the superclass constructor
		openConnection();
		if (isConnected() == false) {
			throw new IOException("Could not connect to server.");
		}
		System.out.println("Connected");
		loginController = null;
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public static Object msgFromServer;

	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		msgFromServer = msg;
		awaitResponse = false;
		if (msgFromServer instanceof User) {
			user = (User) msg;
			if (user != null) {
				Permission = user.getPermssion();
			}
		} else if (msgFromServer instanceof Person) {
			person = (Person) msg;
		} else if (msgFromServer instanceof Test) {
			test = (Test) msg;
		} else if (msgFromServer instanceof Question) {
			qst = (Question) msg;
		} else if (msgFromServer instanceof Integer) {
			Value = (int) msg;
		} else if (msgFromServer instanceof String) {
			if (msgFromServer.equals("not found"))
				Permission = "not found";
			else if (msgFromServer.equals("nothing"))
				System.out.println("");
			else if (msgFromServer.equals("0"))
				Code = "0";
			else if (msgFromServer.equals("1"))
				Code = "1";
			else if (msgFromServer.equals("alreadylogged"))
				Permission = "alreadylogged";
			else if (((String) msgFromServer).contains("_"))
				fullname = (String) msg;
			else if (((String) msgFromServer).length() == 9)
				TeacherID = (String) msg;
		} else if (msgFromServer instanceof ArrayList) {
			if (((ArrayList<String>) msgFromServer).contains("teachers")) {
				TeachersArr = (ArrayList<String>) msg;
				TeachersArr.remove(0);

			} else if (((ArrayList<String>) msgFromServer).contains("students")) {
				StudentsArr = (ArrayList<String>) msg;
				StudentsArr.remove(0);

			} else if (((ArrayList<String>) msgFromServer).contains("notes")) {
				NotesArr = (ArrayList<String>) msg;
				NotesArr.remove(0);
			} else if (((ArrayList<String>) msgFromServer).contains("faculty")) {
				ProfArr = (ArrayList<String>) msg;
				ProfArr.remove(0);
		
			} else if (((ArrayList<String>) msgFromServer).contains("teacherproff")) {
				TeacherProfArr = (ArrayList<String>) msg;
				TeacherProfArr.remove(0);
	
			} else if (((ArrayList<String>) msgFromServer).contains("teachercourses")) {
				TeacherCoursesArr = (ArrayList<String>) msg;
				TeacherCoursesArr.remove(0);
	
			} else if (((ArrayList<String>) msgFromServer).contains("GetCourse")) {
				CoursesArr = (ArrayList<String>) msg;
				CoursesArr.remove(0);
			} else if (((ArrayList<String>) msgFromServer).contains("GetEIdByTeacher")) {
				ExamIDArrByCourse = (ArrayList<String>) msg;
				ExamIDArrByCourse.remove(0);
	
			} else if (((ArrayList<String>) msgFromServer).contains("GetQIDByCourse")) {
				QuestionsArrByCourse = (ArrayList<String>) msg;
				QuestionsArrByCourse.remove(0);
		
			} else if (((ArrayList<String>) msgFromServer).contains("CoursesExamsID")) {
				CoursesExamsIDArr = (ArrayList<String>) msg;
				CoursesExamsIDArr.remove(0);
	
			} else if (((ArrayList<String>) msgFromServer).contains("QInstruction")) {
				ArrayList<String> list = (ArrayList<String>) msg;
				Qinstruction = "";
				list.remove(0);
				for (int i = 0; i < list.size() - 1; i++) {
					Qinstruction += list.get(i) + " ";
				}
				Qinstruction += list.get(list.size() - 1);
		
			} else if (((ArrayList<Integer>) msgFromServer).contains(1)) {
				ExamsGradeArr = (ArrayList<Integer>) msg;
				ExamsGradeArr.remove(0);

			} else if (((ArrayList<Integer>) msgFromServer).contains(2)) {
				TeacherExamsGradeArr = (ArrayList<Integer>) msg;
				TeacherExamsGradeArr.remove(0);
		
			} else if (((ArrayList<Integer>) msgFromServer).contains(3)) {
				CoursesExamsGradeArr = (ArrayList<Integer>) msg;
				CoursesExamsGradeArr.remove(0);
	
			} else if (((ArrayList<Integer>) msgFromServer).contains(4)) {
				ExamsGradeByIDArr = (ArrayList<Integer>) msg;
				ExamsGradeByIDArr.remove(0);
		
			} else if (((ArrayList<Integer>) msgFromServer).contains(5)) {
				ExamsGradeByCourseArr = (ArrayList<Integer>) msg;
				ExamsGradeByCourseArr.remove(0);
			
			} else if (((ArrayList<Integer>) msgFromServer).contains(6)) {
				ExamsGradeByExamIDArr = (ArrayList<Integer>) msg;
				ExamsGradeByExamIDArr.remove(0);
	
			} else if (((ArrayList<String>) msgFromServer).contains("GetQID")) {
				QuestionsArr = (ArrayList<String>) msg;
				QuestionsArr.remove(0);
			
			} else if (!((ArrayList<Test>) msgFromServer).isEmpty()) {
				TeacherTestsArr = (ArrayList<Test>) msg;
		
			} else if (((ArrayList<Test>) msgFromServer).isEmpty()) {
				TeacherTestsArr = null;
			}
		}

	}
	/*-------------------------------------------------------------------
	 * 						 Statics Variables
	--------------------------------------------------------------------- */

	public static ArrayList<String> NotesArr;
	public static ArrayList<String> TeachersArr;
	public static ArrayList<String> StudentsArr;
	public static ArrayList<String> CoursesArr;
	public static ArrayList<String> ProfArr;
	public static ArrayList<String> QuestionsArr;
	public static ArrayList<String> QuestionsArrByCourse;
	public static ArrayList<String> TeacherProfArr;
	public static ArrayList<String> CoursesExamsIDArr;
	public static ArrayList<Integer> ExamsGradeArr;
	public static ArrayList<Integer> TeacherExamsGradeArr;
	public static ArrayList<Integer> CoursesExamsGradeArr;
	public static ArrayList<Integer> ExamsGradeByIDArr;
	public static ArrayList<Integer> ExamsGradeByCourseArr;
	public static ArrayList<Integer> ExamsGradeByExamIDArr;
	public static ArrayList<Test> TeacherTestsArr;
	public static ArrayList<String> TeacherCoursesArr;
	public static ArrayList<String> ExamIDArrByCourse;
	public static ArrayList<DurationRequests> DurRequests;
	public static ArrayList<CheckMistakes> CheckMistakesArr;

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	public static ChatClient GetClient() {
		if (client != null) {
			return client;
		}
		return null;
	}

	public static ChatClient GetClient(String host, int port) throws Exception {
		if (client == null) {
			client = new ChatClient(host, port);
		}
		return client;
	}
}
//End of ChatClient class
