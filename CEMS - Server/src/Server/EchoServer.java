package Server;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controllers.ServerController;
import logic.CheckMistakes;
import logic.DurationRequests;
import logic.FinishedTest;
import logic.ManualTest;
import logic.Question;
import logic.Test;
import logic.TestCopy;
import logic.TestResult;
import logic.TestToPerform;
import logic.User;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;res
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************
	static Connection conn;
	public ServerController sc;

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String str = (String) msg;
		User user = null;
		String[] s = str.split(" ");
		Queries query = new Queries(conn);
		if (s[0].equals("Login")) {
			user = query.ExistenceCheck(s[1], s[2]);
			if (user != null) {
				try {
					client.sendToClient(user);
					client.sendToClient(query.person);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (user == null && query.flag) { // if user not found it and the flag is on it mean the user already
														// connected
				try {
					client.sendToClient("alreadylogged");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				try {
					client.sendToClient("not found");
				} catch (IOException e) {
					e.printStackTrace();
				}
		} else if (s[0].equals("Logout")) {
			try {
				query.ChangeStatusOfConnection(query.GetUserName(s[1]), false);
				client.sendToClient("nothing");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("Update")) {
			try {
				query.UpdateDetails(query.GetUserName(s[1]), s[2], s[3], s[4]);
				query.getPersonData(query.GetUserName(s[1]));
				client.sendToClient(query.person);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetDetails")) {
			query.GetDataBaseDetails();
			query.GetProffNames();
			try {
				client.sendToClient(query.TeachersArr);
				client.sendToClient(query.StudentsArr);
				client.sendToClient(query.ProfArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("getStudentExam")) {
			ArrayList<Integer> examsRes = new ArrayList<>();
			examsRes = query.GetStudExamRes(s[1]);

			try {
				client.sendToClient(examsRes);
				String fullname = query.GetFullName(s[1]);
				client.sendToClient(fullname);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("getTeacherExam")) {
			ArrayList<Integer> examsRes = new ArrayList<>();
			examsRes = query.GetTeacherExamRes(s[1]);
			try {
				client.sendToClient(examsRes);
				String fullname = query.GetFullName(s[1]);
				client.sendToClient(fullname);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("getCoursesExam")) {
			ArrayList<Integer> examsRes = new ArrayList<>();
			examsRes = query.GetCourseExamRes(s[1].replace("_", " "));
			try {
				client.sendToClient(examsRes);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (s[0].equals("GetCourses")) {
			query.GetCoursesNames(str.replace("GetCourses ", ""));
			try {
				client.sendToClient(query.CoursesArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetFaculties")) {
			query.GetProffNames();
			try {
				client.sendToClient(query.ProfArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("AddQuestion")) {
			query.AddQuestionQuery(str.replace("AddQuestion ", ""));
			try {
				client.sendToClient(query.ProfArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetQID")) {
			query.GetQuestionID(str.replace("GetQID ", ""));
			try {
				client.sendToClient(query.QuestionsArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetQdetails")) {
			Question qst = query.GetQuestionDetails(str.replace("GetQdetails ", ""));
			try {
				client.sendToClient(qst);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("DeleteQuestion")) {
			query.DeleteQuestion(str.replace("DeleteQuestion ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("DeleteExam")) {
			query.DeleteExam(str.replace("DeleteExam ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("UpdateQuestion")) {
			String st = str.replace("UpdateQuestion ", "");
			query.UpdateQuestion(st);

			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetTeacherTests")) {
			query.GetTeacherTests(str.replace("GetTeacherTests ", ""));
			try {
				client.sendToClient(query.TeacherTestArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetExamStats")) {
			ArrayList<Integer> examsRes = new ArrayList<>();
			examsRes = query.GetExamStats(s[1], s[2]);
			try {
				client.sendToClient(examsRes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetTeacherFaculties")) {
			query.GetTeachersProfNames(s[1]);
			try {
				client.sendToClient(query.TeacherProfArr);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (s[0].equals("getTeacherCourses")) {
			query.GetTeachersCourses(s[1]);
			try {
				String fullname = query.GetFullName(s[1]);
				client.sendToClient(fullname);
				client.sendToClient(query.TeacherCourseArr);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (s[0].equals("GetExamStatsByCourse")) {
			ArrayList<Integer> examsRes = new ArrayList<>();
			examsRes = query.GetExamStatsByCourseName(s[1], s[2]);
			try {
				client.sendToClient(examsRes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("getCoursesExamsID")) {
			query.GetCourseseExamsID(s[1].replace("_", " "));
			try {
				client.sendToClient(query.CoursesExamsIDArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetExamStatsByExamID")) {
			query.GetStatsByExamsID(s[1]);
			try {
				client.sendToClient(query.ExamsGradeByExamIDArr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("UploadExam")) {
			query.UploadExam(str.replace("UploadExam ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetQIDByCourse")) {
			query.GetQuestionIDByCourse(str.replace("GetQIDByCourse ", ""));
			try {
				client.sendToClient(query.QuestionsArrByCourse);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetQInstructions")) {
			ArrayList<String> inst = new ArrayList<>();
			inst = query.GetQuestionInstruction(str.replace("GetQInstructions ", ""));
			try {
				client.sendToClient(inst);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("CreateExam")) {
			query.CreateExam(str.replace("CreateExam ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetExamsIDByTeacher")) {
			ArrayList<String> ids = new ArrayList<>();
			ids = query.GetExamsIDByTeacher(str.replace("GetExamsIDByTeacher ", ""));
			try {
				client.sendToClient(ids);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetExamPrev")) {
			Test test = new Test();
			test = query.GetExamsByID(str.replace("GetExamPrev ", ""));
			try {
				client.sendToClient(test);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("UpdateExam")) {
			query.UpdateExam(str.replace("UpdateExam ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("SearchByStudent")) {
			List<TestResult> list = new ArrayList<>();
			try {
				list = query.GetExamDetailsByStudent(s[1]);
				try {
					client.sendToClient(list);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("SearchByTest")) {
			TestResult testRes = null;
			try {
				testRes = query.GetExamDetailsById(s[1], s[2]);
				client.sendToClient(testRes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetExam")) {// copy test to see by student
			TestCopy testCopy = null;
			testCopy = query.GetCopyExam(s[1], s[2], s[3]);
			try {
				client.sendToClient(testCopy);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("TakeExam")) {// Test for solution
			TestToPerform test = null;
			test = query.TakeExamByCode(s[1]);
			try {
				client.sendToClient(test);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("TakeManualExam")) {// Test for solution
			ManualTest test = null;
			test = query.TakeManualExamByCode(s[1]);
			try {
				client.sendToClient(test);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("SubmitQuestion")) {
			query.submitQuestion(str.replace("SubmitQuestion ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("SubmitTest")) {
			query.submitTest(str.replace("SubmitTest ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetTeacherID")) {
			String id = query.GetTeacherID(str.replace("GetTeacherID ", ""));
			try {
				client.sendToClient(id);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetDuration")) {// JEN
			int duration;
			duration = query.getDuration(str.replace("GetDuration ", ""));
			try {
				client.sendToClient(duration);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetExamsToApply")) {
			ArrayList<Test> tests = new ArrayList<>();
			tests = query.getExamsToApply(str.replace("GetExamsToApply ", ""));
			try {
				client.sendToClient(tests);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("SetCodeForExam")) {
			query.SetCode(str.replace("SetCodeForExam ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetLiveExams")) {
			ArrayList<Test> tests = new ArrayList<>();
			tests = query.GetLiveExams(str.replace("GetLiveExams ", ""));
			try {
				client.sendToClient(tests);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("LockExam")) {

			query.LockExamByID(str.replace("LockExam ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("CheckLockExam")) {
			String Code = query.GetCodeExam(str.replace("CheckLockExam ", ""));
			try {
				client.sendToClient(Code);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("SendRequest")) {
			query.SendDurRequest(str.replace("SendRequest ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetFullNameByID")) {
			try {
				String fullname = query.GetFullName(s[1]);
				client.sendToClient(fullname);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetDurationRequests")) {
			ArrayList<DurationRequests> arr = new ArrayList<>();
			arr = query.GetDurationRequests();
			try {
				client.sendToClient(arr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("DeclineRequest")) {
			query.DeleteRequest(s[1]);
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("ApproveRequest")) {

			try {
				query.ApproveRequest(s[1]);
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("UpdateNewGrade")) {
			try {
				query.UpdateNewGrade(str.replace("UpdateNewGrade ", ""));
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("CheckMistakes")) {
			ArrayList<CheckMistakes> arr = new ArrayList<>();
			arr = query.GetCheckMistakesArr(str.replace("CheckMistakes ", ""));
			try {

				client.sendToClient(arr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetFinishedExams")) {
			ArrayList<FinishedTest> arr = new ArrayList<>();
			arr = query.GetFinishedExams(str.replace("GetFinishedExams ", ""));
			try {

				client.sendToClient(arr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("ApproveGrade")) {
			query.ApproveGrade(str.replace("ApproveGrade ", ""));
			try {

				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetNotes")) {
			ArrayList<String> arr = new ArrayList<>();
			arr = query.GetNotes(str.replace("GetNotes ", ""));
			try {

				client.sendToClient(arr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("RemoveReq")) {
			query.RemoveReq(str.replace("RemoveReq ", ""));
			try {

				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("GetManualflag")) {
			String flag = "";
			flag = query.GetManualflag(s[1]);
			try {
				client.sendToClient(flag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("ExamManualData")) {
			query.ExamManualData(str.replace("ExamManualData ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("RemoveManualCode")) {
			query.RemoveManualCode(str.replace("RemoveManualCode ", ""));
			try {

				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("ExamManualDateUp")) {
			query.ExamManualDateUp(str.replace("ExamManualDateUp ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("SubmitManualTest")) {
			query.SubmitManualExam(str.replace("SubmitManualTest ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("UpdataManualTimeActual")) {
			query.UpdataManualTimeActual(str.replace("UpdataManualTimeActual ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("CheckManualLockExam")) {
			String Code = query.GetManualCodeExam(str.replace("CheckManualLockExam ", ""));
			try {
				client.sendToClient(Code);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("DownloadExam")) {
			try {

				query.DownloadExam(s[1], s[2]);
				client.sendToClient("nothing");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (s[0].equals("Getflag")) {
			String flag = "";
			flag = query.Getflag(s[1]);
			try {
				client.sendToClient(flag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("ExamData")) {
			query.ExamData(str.replace("ExamData ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("ExamDateUp")) {
			query.ExamDateUp(str.replace("ExamDateUp ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (s[0].equals("UpdataTimeActual")) {
			query.UpdataTimeActual(str.replace("UpdataTimeActual ", ""));
			try {
				client.sendToClient("nothing");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*---------------------------------------------------------------------------------
	 *    				                SqlConnections
	--------------------------------------------------------------------------------- */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		ServerLog.writeNewLine("Client connected: " + client.toString());
	}

	@SuppressWarnings("deprecation")
	public void MySqlConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			ServerLog.writeNewLine("Driver definition succeed");
		} catch (Exception ex) {
			ServerLog.writeNewLine("Driver definition failed");
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/CEMS?serverTimezone=IST", "root", "sk159159");
			ServerLog.writeNewLine("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			ServerLog.writeNewLine("SQLException: " + ex.getMessage());
			ServerLog.writeNewLine("SQLState: " + ex.getSQLState());
			ServerLog.writeNewLine("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		// connect to DB
		MySqlConnection();

	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		ServerLog.writeNewLine("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		EchoServer sv = new EchoServer(port);
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			ServerLog.writeNewLine("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class
