package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import logic.CheckMistakes;
import logic.DurationRequests;
import logic.FinishedTest;
import logic.ManualTest;
import logic.Person;
import logic.Question;
import logic.QuestionsAfterTest;
import logic.QuestionsForStud;
import logic.Test;
import logic.TestCopy;
import logic.TestResult;
import logic.TestToPerform;
import logic.User;

/**
 * @author Saar
 *this class used to contains all of the queries to use in DB
 */
public class Queries {
	private Connection conn;
	boolean flag = false; // flag that used to check if user already connected
	Person person;
	ArrayList<String> StudentsArr;
	ArrayList<String> TeachersArr;
	ArrayList<String> ProfArr;
	ArrayList<String> TeacherProfArr;
	ArrayList<String> TeacherCourseArr;
	ArrayList<String> CoursesArr;
	ArrayList<String> QuestionsArr;
	ArrayList<String> QuestionsArrByCourse;
	ArrayList<String> CoursesExamsIDArr;
	ArrayList<Test> TeacherTestArr;
	ArrayList<Integer> ExamsGradeByExamIDArr;

	public Queries(Connection conn) {
		super();
		this.conn = conn;
	}

	/*------------------------------------------------------------------------------------------------------------------------
	 *													 Queries
	--------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * @param proffCourse used in SQL
	 *  This method used to get Courses names from DB
	 */
	public void GetCoursesNames(String proffCourse) {

		ArrayList<String> courses = new ArrayList<>();
		courses.add("GetCourse");
		String sql = ("SELECT Course FROM courses WHERE Faculty='" + proffCourse + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String course = rs.getString("Course");
				courses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CoursesArr = courses;
	}

	/**
	 * @param StudId id of student
	 *  This method returns a ArrayList of grades for specific Student
	 * @return ArrayList of student exams result
	 */
	public ArrayList<Integer> GetStudExamRes(String StudId) {
		ArrayList<Integer> exams = new ArrayList<>();
		exams.add(1);// to know that student sent
		String sql = ("SELECT Grade FROM finishedexam WHERE Status=true AND StudentID='" + StudId + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				exams.add(rs.getInt("Grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exams;
	}

	/**
	 * @param CourseName 
	 *  This method returns a ArrayList of grades for specific Course
	 * @return Arraylist of courses exams results
	 */
	public ArrayList<Integer> GetCourseExamRes(String CourseName) {
		ArrayList<Integer> exams = new ArrayList<>();
		exams.add(3);// to know that courses sent
		String sql = ("SELECT Grade FROM finishedexam WHERE Status=true AND Course='" + CourseName + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				exams.add(rs.getInt("Grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exams;
	}

	/**
	 * @param TeacherId
	 *  This method returns a ArrayList of grades for specific Teacher
	 * @return ArrayList of teachers exams result
	 */
	public ArrayList<Integer> GetTeacherExamRes(String TeacherId) {
		ArrayList<Integer> exams = new ArrayList<>();
		exams.add(2);// to know that teacher sent
		String sql = ("SELECT Grade FROM finishedexam WHERE Status=true AND TeacherID='" + TeacherId + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				exams.add(rs.getInt("Grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exams;
	}

	/**
	 * @param id of person
	 * This method return the full name by checking the ID
	 * @return String of full name
	 * @throws SQLException
	 */
	public String GetFullName(String id) throws SQLException {
		String sql = ("SELECT FirstName, LastName FROM person WHERE ID='" + id + "';");
		Statement st = conn.createStatement();
		ResultSet rs;
		String fullName = null;
		rs = st.executeQuery(sql);
		if (rs.next()) {
			String Firstname = rs.getString("FirstName");
			String Lastname = rs.getString("LastName");
			fullName = Firstname + "_" + Lastname;
		}
		return fullName;

	}

	/**
	 * @param Username
	 * @param password
	 *  check existence of a user name in DB
	 * @return The user
	 */
	public User ExistenceCheck(String Username, String password) {
		String sql = ("SELECT * FROM USERS WHERE UserName='" + Username + "' AND Password='" + password + "';");
		User user = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String UserName = rs.getString("UserName");
				String Password = rs.getString("Password");
				String Permssion = rs.getString("Permssion");
				boolean status = rs.getBoolean("Status");
				if (!status) { // If the user is Disconnected than get the user details and update the status
					user = new User(UserName, Password, Permssion);
					ChangeStatusOfConnection(Username, true);
					getPersonData(Username);
				}
				flag = status;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * @param username
	 * @param status
	 *  Change the status of connection in DB
	 * @throws SQLException
	 */
	public void ChangeStatusOfConnection(String username, boolean status) throws SQLException {
		Statement st = conn.createStatement();
		String sql = ("UPDATE users SET Status=" + status + " WHERE UserName='" + username + "';");
		st.executeUpdate(sql);
		flag = status;
	}

	/**
	 * @param username
	 *  This method used to get all the personal data of a person by checking his User name
	 */
	public void getPersonData(String username) {
		Person person = null;
		String sql = ("SELECT * FROM person WHERE UserName='" + username + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String Id = rs.getString("ID");
				String Firstname = rs.getString("FirstName");
				String Lastname = rs.getString("LastName");
				String Phone = rs.getString("Phone");
				String Email = rs.getString("Email");
				String Address = rs.getString("Address");
				person = new Person(Id, Firstname, Lastname, Phone, Email, Address);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.person = person;
	}

	/**
	 * @param userName
	 * @param newPhone
	 * @param newEmail
	 * @param newAddress
	 *  Update personal details in DB
	 * @throws SQLException
	 */
	public void UpdateDetails(String userName, String newPhone, String newEmail, String newAddress)
			throws SQLException {
		Statement st = conn.createStatement();
		String address = newAddress.replace("_", " ");
		String sql = ("UPDATE person SET Phone='" + newPhone + "', Email='" + newEmail + "', Address='" + address
				+ "' WHERE UserName='" + userName + "';");
		st.executeUpdate(sql);
	}

	/**
	 * @param id
	 *  This method return the User Name by checking the ID
	 * @return String of USERNAME
	 * @throws SQLException
	 */
	public String GetUserName(String id) throws SQLException {
		String sql = ("SELECT UserName FROM person WHERE ID='" + id + "';");
		String User = null;
		Statement st = conn.createStatement();
		ResultSet rs;
		rs = st.executeQuery(sql);
		if (rs.next()) {
			User = rs.getString("UserName");
		}
		return User;
	}

	/**
	 * This method gets the data base detail of students or teachers names
	 */
	public void GetDataBaseDetails() {
		ArrayList<String> students = new ArrayList<>();
		ArrayList<String> teachers = new ArrayList<>();
		students.add("students");
		teachers.add("teachers");
		String sql = ("SELECT * FROM users;");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String permission = rs.getString("Permssion");
				if (permission.equals("Teacher")) {
					teachers.add(getID(rs.getString("UserName")));
				} else if (permission.equals("Student")) {
					students.add(getID(rs.getString("UserName")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		StudentsArr = students;
		TeachersArr = teachers;
	}

	/**
	 * This method used to get Profession names from DB
	 */
	public void GetProffNames() {
		ArrayList<String> proff = new ArrayList<>();
		proff.add("faculty");
		String sql = ("SELECT Faculty FROM faculties;");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				proff.add(rs.getString("Faculty"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ProfArr = proff;
	}

	/**
	 * @param username
	 *  this method gets the full name of specific User Name
	 * @return String of the ID
	 */
	public String getID(String username) {
		String sql = ("SELECT ID FROM person WHERE UserName='" + username + "';");
		String Id = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				Id = rs.getString("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Id;
	}

	/**
	 * @param str contains the details for the query
	 *  this method adding question to the database
	 */
	public void AddQuestionQuery(String str) {
		String[] s = str.split("_");
		int Qnum = MaxQNum();
		String id = CreateIdForQuestion(s[0], Qnum);
		String sql = ("INSERT INTO questions (ID,Profession,Course,Author,QuestionNum,Question,CorrectAnswer,Answer1,Answer2,Answer3,Answer4)"
				+ " VALUES('" + id + "','" + s[0] + "','" + s[1] + "','" + s[2] + "','" + Qnum + "','" + s[3] + "','"
				+ s[4] + "','" + s[5] + "','" + s[6] + "','" + s[7] + "','" + s[8] + "');");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return return the next question number that need to be saved
	 */
	private int MaxQNum() {
		String sql = ("SELECT MAX(QuestionNum) FROM questions;");
		int max = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				max = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max + 1;
	}

	/**
	 * @return return the next question number that need to be saved
	 */
	private int MaxManuallyExamNum() {
		String sql = ("SELECT MAX(TestNum) FROM manualtest;");
		int max = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				max = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max + 1;
	}

	/** 
	 * @return return the next exam number that need to be saved
	 */
	private int MaxExamNum() {
		String sql = ("SELECT MAX(TestNum) FROM tests;");
		int max = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				max = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max + 1;
	}

	/**
	 * @param profname
	 * @param qnum question num
	 * this method creates Id for question buy combining the profession name and the question number
	 * @return the string of the full ID for question
	 */
	private String CreateIdForQuestion(String profname, int qnum) {
		String ID = null;
		String sql = ("SELECT FacultyID FROM faculties WHERE Faculty='" + profname + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ID = rs.getString("FacultyID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ID + qnum;
	}

	/**
	 * @param profname
	 * @param coursename
	 * @param exnum
	 * this method creates Id for question buy combining the profession name and the question number
	 * @return the string of the full ID for question
	 */
	private String CreateIdForManualExam(String profname, String coursename, String exnum) {
		String ID = null;
		String ID2 = null;
		String sql = ("SELECT FacultyID FROM faculties WHERE Faculty='" + profname + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ID = rs.getString("FacultyID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql1 = ("SELECT CourseID FROM courses WHERE Course='" + coursename + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql1);
			while (rs.next()) {
				ID2 = rs.getString("CourseID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ID + ID2 + exnum;
	}

	
	/**
	 * @param s contains the details for query
	 * this method make array list with all the Questions Id that related to specific Profession to apply the list for the combo box
	 */
	public void GetQuestionID(String s) {
		String[] str = s.split(" ");
		String fullname = str[1] + " " + str[2];
		ArrayList<String> questions = new ArrayList<>();
		questions.add("GetQID");
		String sql = ("SELECT QuestionNum FROM questions WHERE Author='" + fullname + "' AND Profession='"
				+ str[0].replace("_", " ") + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String qnum = rs.getString("QuestionNum");
				questions.add(qnum);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		QuestionsArr = questions;

	}

	/**
	 * @param qnum
	 * this method get the question details by the question num
	 * @return Question details
	 */
	public Question GetQuestionDetails(String qnum) {
		Question qst = new Question();
		String sql = ("SELECT * FROM questions WHERE QuestionNum='" + qnum + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				qst.setInstructions(rs.getString("Question"));
				String[] answers = new String[4];
				answers[0] = rs.getString("Answer1");
				answers[1] = rs.getString("Answer2");
				answers[2] = rs.getString("Answer3");
				answers[3] = rs.getString("Answer4");
				qst.setAnswers(answers);
				qst.setCorrectAnswer(rs.getString("CorrectAnswer"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return qst;
	}

	/**
	 * @param qnum
	 * this method delete a specific question from the data base by the question number
	 */
	public void DeleteQuestion(String qnum) {
		String sql = ("DELETE FROM questions WHERE QuestionNum='" + qnum + "';");
		try {
			Statement st = conn.createStatement();
			st.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param id
	 * this method delete a specific exam from the data base by the exam id
	 */
	public void DeleteExam(String id) {
		String sql = ("DELETE FROM tests WHERE ID='" + id + "';");
		try {
			Statement st = conn.createStatement();
			st.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param details
	 * this method update question in data base by question number
	 */
	public void UpdateQuestion(String details) {
		String[] s = details.split(" ");
		String sql = ("UPDATE questions SET Question='" + s[0].replace("_", " ") + "', Answer1='" + s[1].replace("_", " ") + "', Answer2='" + s[2].replace("_", " ")
				+ "', Answer3='" + s[3].replace("_", " ") + "', Answer4='" + s[4].replace("_", " ") + "', CorrectAnswer='" + s[5] + "' WHERE QuestionNum='"
				+ s[6] + "';");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param author
	 *  this method gets the Teacher exams by the teacher name
	 */
	public void GetTeacherTests(String author) {
		ArrayList<Test> tests = new ArrayList<>();
		String sql = ("SELECT ID, Profession, Course FROM tests WHERE Author='" + author + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("ID");
				String proff = rs.getString("Profession");
				String course = rs.getString("Course");
				Test test = new Test(id, proff, course);
				tests.add(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TeacherTestArr = tests;
	}


	/**
	 * @param examID
	 * @param TeacherId
	 * 	 this method receive exam id and teacher id to return the exam grade of the given teacher id
	 * @return ArrayList of examstats for Teacher
	 */
	public ArrayList<Integer> GetExamStats(String examID, String TeacherId) {
		ArrayList<Integer> exams = new ArrayList<>();
		exams.add(4);// to know that teacher statistics exams sent
		String sql = ("SELECT Grade FROM finishedexam WHERE ExamID='" + examID + "' AND Status=true AND TeacherID='"
				+ TeacherId + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				exams.add(rs.getInt("Grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exams;
	}

	/**
	 * @param id
	 *  this method gets the profession of a specific teacher
	 */
	public void GetTeachersProfNames(String id) {

		ArrayList<String> proff = new ArrayList<>();
		proff.add("teacherproff");
		String prof = null;
		String sql = ("SELECT Professions FROM teacher WHERE ID='" + id + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				prof = rs.getString("Professions");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] s = prof.split(", ");
		for (int i = 0; i < s.length; i++)
			proff.add(s[i]);
		TeacherProfArr = proff;
	}

	/**
	 * @param id
	 *  this method gets the courses of a specific teacher
	 */
	public void GetTeachersCourses(String id) {
		ArrayList<String> courses = new ArrayList<>();
		courses.add("teachercourses");
		String course = null;
		String sql = ("SELECT DISTINCT Course FROM finishedexam WHERE Status=1 AND TeacherID='" + id + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				course = rs.getString("Course");
				courses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TeacherCourseArr = courses;
	}

	/**
	 * @param CourseName
	 * @param TeacherId
	 * this method gets the course name and teacher id and return the stats by the course name and id
	 * @return arraylist of exams stats
	 */
	public ArrayList<Integer> GetExamStatsByCourseName(String CourseName, String TeacherId) {
		ArrayList<Integer> exams = new ArrayList<>();
		exams.add(5);// to know that teacher statistics exams sent
		String sql = ("SELECT Grade FROM finishedexam WHERE Course='" + CourseName.replace("_", " ")
				+ "' AND Status=true AND TeacherID='" + TeacherId + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				exams.add(rs.getInt("Grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exams;
	}

	/**
	 * @param CourseName
	 * this method gets all the courses exams id
	 */
	public void GetCourseseExamsID(String CourseName) {
		ArrayList<String> ExamsID = new ArrayList<>();
		ExamsID.add("CoursesExamsID");
		String sql = ("SELECT DISTINCT ExamID FROM finishedexam WHERE Status=1 AND Course='" + CourseName + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ExamsID.add(rs.getString("ExamID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CoursesExamsIDArr = ExamsID;

	}

	/**
	 * @param ExamID
	 * this method get the stats by the exams id
	 */
	public void GetStatsByExamsID(String ExamID) {
		ArrayList<Integer> ExamsStats = new ArrayList<>();
		ExamsStats.add(6);
		String sql = ("SELECT Grade FROM finishedexam WHERE Status=1 AND ExamID='" + ExamID + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ExamsStats.add(rs.getInt("Grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ExamsGradeByExamIDArr = ExamsStats;

	}

	/**
	 * @param details contains the details for the query
	 * this method uploading the exam to the DB
	 */
	public void UploadExam(String details) {
		String[] s = details.split(" ");
		int Exnum = MaxManuallyExamNum();
		String zeroadd = "0";
		String id = null;
		if (Exnum < 10)
			id = CreateIdForManualExam(s[1].replace("_", " "), s[2].replace("_", " "), zeroadd + Exnum);
		else
			id = CreateIdForManualExam(s[1].replace("_", " "), s[2].replace("_", " "), "" + Exnum);
		String sql = ("INSERT INTO manualtest (ID,Author,Profession,Course,Duration,TestNum)" + " VALUES('" + id + "','"
				+ s[0].replace("_", " ") + "','" + s[1].replace("_", " ") + "','" + s[2].replace("_", " ") + "','"
				+ Integer.parseInt(s[3]) + "','" + Exnum + "');");
		try {
			Statement sta = conn.createStatement();
			sta.executeUpdate(sql);
			String sql2 = "UPDATE manualtest SET Test=? WHERE ID='" + id + "';";
			PreparedStatement ps = conn.prepareStatement(sql2);
			FileInputStream input = new FileInputStream(s[4].replace("_", " "));
			ps.setBlob(1, input);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param s - contains the data for the query
	 * this method returns the question ID by getting course id
	 */
	public void GetQuestionIDByCourse(String s) {
		String[] str = s.split(" ");
		String fullname = str[1] + " " + str[2];
		ArrayList<String> questions = new ArrayList<>();
		questions.add("GetQIDByCourse");
		String sql = ("SELECT QuestionNum FROM questions WHERE Course='" + str[0].replace("_", " ") + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String qnum = rs.getString("QuestionNum");
				questions.add(qnum);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		QuestionsArrByCourse = questions;

	}

	/**
	 * @param qnum
	 * @return Arraylist of the question instruction
	 */
	public ArrayList<String> GetQuestionInstruction(String qnum) {
		String inst = null;
		String ID = null;
		String sql = ("SELECT ID,Question FROM questions WHERE QuestionNum='" + qnum + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ID = rs.getString("ID");
				inst = rs.getString("Question");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<String> instlist = new ArrayList<>();
		instlist.add("QInstruction");
		instlist.add(ID);
		String[] st = inst.split(" ");
		for (int i = 0; i < st.length; i++)
			instlist.add(st[i]);
		return instlist;
	}

	/**
	 * @param details - contains the details for the query
	 * this method create a exam in the database
	 */
	public void CreateExam(String details) {
		String[] s = details.split(" ");
		int Exnum = MaxExamNum();
		String zeroadd = "0";
		String id = null;
		if (Exnum < 10) {
			id = CreateIdForManualExam(s[1].replace("_", " "), s[2].replace("_", " "), zeroadd + Exnum);
		} else
			id = CreateIdForManualExam(s[1].replace("_", " "), s[2].replace("_", " "), "" + Exnum);
		String teacherNotes, studentNotes;
		if (s[5].equals("emptystudnotes"))
			studentNotes = "";
		else
			studentNotes = s[5];
		if (s[6].equals("emptyteacnotes"))
			teacherNotes = "";
		else
			teacherNotes = s[6];
		String sql = ("INSERT INTO tests (ID,Author,Profession,Course,Duration,NumOfQuestions,TextForStudent,TextForTeacher,qArray,Points,TestNum)"
				+ " VALUES('" + id + "','" + s[0].replace("_", " ") + "','" + s[1].replace("_", " ") + "','"
				+ s[2].replace("_", " ") + "','" + Integer.parseInt(s[3]) + "','" + Integer.parseInt(s[4]) + "','"
				+ studentNotes.replace("_", " ") + "','" + teacherNotes.replace("_", " ") + "','"
				+ s[7].replace("_", " ") + "','" + s[8].replace("_", " ") + "','" + Exnum + "');");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param s contains the details for the query
	 * @return ArrayList of Exams by TeacherID
	 */
	public ArrayList<String> GetExamsIDByTeacher(String s) {
		ArrayList<String> list = new ArrayList<>();
		String[] str = s.split(" ");
		list.add("GetEIdByTeacher");
		String sql = ("SELECT ID FROM tests WHERE Author='" + str[1].replace("_", " ") + "' AND Course='"
				+ str[0].replace("_", " ") + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String qnum = rs.getString("ID");
				list.add(qnum);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * @param ExID exam id
	 * @return The specific exam by the ID
	 */
	public Test GetExamsByID(String ExID) {
		Test test = new Test();
		String sql = ("SELECT * FROM tests WHERE ID='" + ExID + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				test.setAuthor(rs.getString("Author"));
				test.setProfession(rs.getString("Profession"));
				test.setCourse(rs.getString("Course"));
				test.setDuration(rs.getInt("Duration"));
				test.setTextForStudent(rs.getString("TextForStudent"));
				test.setTextForTeacher(rs.getString("TextForTeacher"));
				test.setQArray(rs.getString("qArray"));
				test.setPoints(rs.getString("Points"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return test;
	}

	/**
	 * @param details contains the details for the query
	 * this method update the exam in DB
	 */
	public void UpdateExam(String details) {
		String[] s = details.split(" ");
		String teacherNotes, studentNotes;
		if (s[4].equals("emptystudnotes"))
			studentNotes = "";
		else
			studentNotes = s[4];
		if (s[5].equals("emptyteacnotes"))
			teacherNotes = "";
		else
			teacherNotes = s[5];
		String sql = ("UPDATE tests SET Duration='" + s[2] + "', NumOfQuestions='" + s[3] + "', TextForStudent='"
				+ studentNotes.replace("_", " ") + "', TextForTeacher='" + teacherNotes.replace("_", " ")
				+ "', qArray='" + s[6].replace("_", " ") + "', Points='" + s[7].replace("_", " ") + "' WHERE ID='"
				+ s[0] + "';");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	/**
	 * @param idStud
	 * @return List of the test results by student ID
	 * @throws SQLException
	 */
	public List<TestResult> GetExamDetailsByStudent(String idStud) throws SQLException {// details test
		List<TestResult> list = new ArrayList<>();
		String status = "1";
		Statement st = conn.createStatement();
		String sql = ("SELECT ExamID, Grade, Profession, Course FROM finishedexam WHERE Status='" + status + "' "
				+ "AND StudentID ='" + idStud + "';");
		ResultSet rs;
		rs = st.executeQuery(sql);
		while (rs.next()) {
			String IdTest = rs.getString("ExamID");
			String Profession = rs.getString("Profession");
			String Course = rs.getString("Course");
			String Grade = rs.getString("Grade");
			TestResult res = new TestResult(Profession, Course, IdTest, Grade);// NewGrade
			list.add(res);
		}
		if (list.size() > 0) {
			return list;
		}
		return null;
	}

	/**
	 * @param idTest
	 * @param idStud
	 * @return return test result by test and stud ID
	 */
	public TestResult GetExamDetailsById(String idTest, String idStud) {// details test
		TestResult res = null;
		String status = "1";
		String sql = ("SELECT ExamID, Grade, Profession, Course FROM finishedexam WHERE ExamID='" + idTest + "' "
				+ "AND StudentID='" + idStud + "' " + "AND status='" + status + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String IdTest = rs.getString("ExamID");
				String Profession = rs.getString("Profession");
				String Course = rs.getString("Course");
				String Grade = rs.getString("Grade");
				res = new TestResult(Profession, Course, IdTest, Grade); // NewGrade
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * @param idTest
	 * @param idStud
	 * @param status
	 * @return the test copy of Specific exam
	 */
	public TestCopy GetCopyExam(String idTest, String idStud, String status) {
		TestCopy testCopy = null;

		List<QuestionsAfterTest> questions = new ArrayList<>();

		String sql = ("SELECT* FROM finishedexam WHERE ExamID='" + idTest + "' " + "AND StudentID='" + idStud + "' "
				+ "AND status='" + status + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String IdStud = rs.getString("StudentID");
				String IdTest = rs.getString("ExamID");
				String Profession = rs.getString("Profession");
				String Course = rs.getString("Course");
				String IdAuthor = rs.getString("TeacherID");
				String Grade = rs.getString("Grade");
				String Explanation = rs.getString("Explanation");
				String Notes = rs.getString("Notes");
				String QArray = rs.getString("qArray");
				String PArray = rs.getString("Points");
				String[] s = QArray.split(" ");
				String[] p = PArray.split(" ");
				questions = GetQuestions(s, p, IdStud, IdTest);
				testCopy = new TestCopy(Profession, Course, IdTest, Grade, IdAuthor, Notes, Explanation, questions);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return testCopy;
	}

	/**
	 * @param s
	 * @param p
	 * @param idStud
	 * @param idTest
	 * @return list of questions after test to save the data
	 */
	private List<QuestionsAfterTest> GetQuestions(String[] s, String[] p, String idStud, String idTest) {
		List<QuestionsAfterTest> questions = new ArrayList<>();

		for (int i = 0; i < s.length; i++) {
			String sql = ("SELECT * FROM questionsaftertest WHERE idTest='" + idTest + "' " + "AND idStudent='" + idStud
					+ "' " + "AND idQuestion='" + s[i] + "';");
			try {
				Statement st = conn.createStatement();
				ResultSet rs;
				rs = st.executeQuery(sql);
				if (rs.next()) {
					String IdQues = rs.getString("idQuestion");
					String Question = rs.getString("Question");
					String AnswerStud = rs.getString("answerStud");
					String CorrectAnswer = rs.getString("CorrectAnswer");
					String Answer1 = rs.getString("Answer1");
					String Answer2 = rs.getString("Answer2");
					String Answer3 = rs.getString("Answer3");
					String Answer4 = rs.getString("Answer4");
					QuestionsAfterTest ques = new QuestionsAfterTest(IdQues, Question, p[i], CorrectAnswer, AnswerStud,
							Answer1, Answer2, Answer3, Answer4);
					questions.add(ques);
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return questions;
	}

	/**
	 * @param code
	 * @return the Test to perform by code
	 */
	public TestToPerform TakeExamByCode(String code) {// Brings the exam to the student
		TestToPerform testTo = null;
		List<QuestionsForStud> questions = new ArrayList<>();
		String sql1 = ("SELECT * FROM tests WHERE Code='" + code + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql1);
			if (rs.next()) {
				String IdTest = rs.getString("ID");
				String Profession = rs.getString("Profession");
				String Course = rs.getString("Course");
				String Duration = rs.getString("Duration");
				String NumOfQuestion = rs.getString("NumOfQuestions");
				String TextForStudent = rs.getString("TextForStudent");
				String TextForTeacher = rs.getString("TextForTeacher");
				String QArray = rs.getString("qArray");
				String Points = rs.getString("Points");
				String Author = rs.getString("Author");
				String[] q = QArray.split(" ");
				String[] p = Points.split(" ");
				int TimeOfTest = Integer.parseInt(Duration);
				int NumOfQues = Integer.parseInt(NumOfQuestion);
				questions = getQuestionsForStuds(q, p);
				testTo = new TestToPerform(IdTest, Profession, Course, TextForStudent, TextForTeacher, Author,
						TimeOfTest, NumOfQues, questions);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return testTo;
	}

	/**
	 * @param q
	 * @param p
	 * @return List of question for student 
	 */
	private List<QuestionsForStud> getQuestionsForStuds(String[] q, String[] p) {
		List<QuestionsForStud> questions = new ArrayList<>();

		for (int i = 0; i < q.length; i++) {
			String sql = ("SELECT ID,  Question, CorrectAnswer, Answer1, Answer2, Answer3, Answer4 FROM questions WHERE id='"
					+ q[i] + "';");
			try {
				Statement st = conn.createStatement();
				ResultSet rs;
				rs = st.executeQuery(sql);
				if (rs.next()) {
					String IdQues = rs.getString("ID");
					String Question = rs.getString("Question");
					String CorrectAnswer = rs.getString("CorrectAnswer");
					String Answer1 = rs.getString("Answer1");
					String Answer2 = rs.getString("Answer2");
					String Answer3 = rs.getString("Answer3");
					String Answer4 = rs.getString("Answer4");
					int Point = Integer.parseInt(p[i]);
					QuestionsForStud ques = new QuestionsForStud(IdQues, Question, Answer1, Answer2, Answer3, Answer4,
							CorrectAnswer, Point);
					questions.add(ques);
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return questions;
	}

	/**
	 * @param code
	 * @return the manual test
	 */
	public ManualTest TakeManualExamByCode(String code) {
		ManualTest testM = null;
		String sql = ("SELECT ID,Profession,Course, Duration, Test FROM manualtest WHERE Code='" + code + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String IdTest = rs.getString("ID");
				String Profession = rs.getString("Profession");
				String Course = rs.getString("Course");
				String Duration = rs.getString("Duration");
				String FileTest = rs.getString("Test");
				int TimeOfTest = Integer.parseInt(Duration);
				testM = new ManualTest(IdTest, FileTest, Profession, Course, TimeOfTest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return testM;
	}

	/**
	 * @param details contains the details for the query
	 * submit the question in DB
	 */
	public void submitQuestion(String details) {
		Statement st;
		String[] list = details.split(" ");
		String[] listofQuestionsID = list[2].split("_");// questionID
		String[] listofQuestions = list[3].split("@");// testid
		String[] listofCorrectAns = list[4].split("_");
		String[] listofStudAns = list[5].split("_");
		String[] listofAns1 = list[6].split("@");
		String[] listofAns2 = list[7].split("@");
		String[] listofAns3 = list[8].split("@");
		String[] listofAns4 = list[9].split("@");
		for (int i = 0; i < listofQuestionsID.length; i++) {
			String sql = ("INSERT INTO questionsaftertest (idStudent,idTest,idQuestion,question,CorrectAnswer,answerStud,answer1,answer2,answer3,answer4 )"
					+ " VALUES('" + list[0] + "','" + list[1] + "','" + listofQuestionsID[i] + "','"
					+ listofQuestions[i].replace("_", " ") + "','" + listofCorrectAns[i] + "','" + listofStudAns[i]
					+ "','" + listofAns1[i].replace("_", " ") + "','" + listofAns2[i].replace("_", " ") + "','"
					+ listofAns3[i].replace("_", " ") + "','" + listofAns4[i].replace("_", " ") + "');");
			try {
				st = conn.createStatement();
				st.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param details contains the details for the query
	 * submit the test in DB
	 */
	public void submitTest(String details) {
		String[] str = details.split(" ");
		String sql = ("INSERT INTO finishedexam (TeacherID,StudentID,ExamID,Grade,Profession,Course,Duration,qArray,Points,TextForStudent,TextForTeacher,Status)"
				+ " VALUES('" + str[0] + "','" + str[1] + "','" + str[2] + "','" + Integer.parseInt(str[3]) + "','"
				+ str[4].replace("_", " ") + "','" + str[5].replace("_", " ") + "','" + str[6].replace("_", " ") + "','"
				+ str[7].replace("_", " ") + "','" + str[8].replace("_", " ") + "','" + str[9].replace("_", " ") + "','"
				+ str[10].replace("_", " ") + "','" + str[11] + "');");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param name
	 * @return the teacher ID by name
	 */
	public String GetTeacherID(String name) {
		String[] fname = name.split(" ");
		String id = null;
		String sql = ("SELECT ID  FROM person WHERE LastName='" + fname[1] + "' AND FirstName='" + fname[0] + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				id = rs.getString("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * @param testID
	 * @return the duration by test id
	 */
	public int getDuration(String testID) {
		String sql = ("SELECT NewDuration  FROM durationrequests WHERE TestId='" + testID + "' AND Status='1';");
		int duration = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				duration = rs.getInt("NewDuration");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return duration;
	}

	/**
	 * @param details contains the details for the query
	 * @return ArrayList of exams to apply
	 */
	public ArrayList<Test> getExamsToApply(String details) {
		ArrayList<Test> tests = new ArrayList<>();
		String sql = ("SELECT ID, Profession, Course,Duration FROM tests WHERE Author='" + details.replace("_", " ")
				+ "'AND " + "Code IS NULL;");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("ID");
				String proff = rs.getString("Profession");
				String course = rs.getString("Course");
				int dur = rs.getInt("Duration");
				Test test = new Test(id, proff, course, dur);
				tests.add(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql2 = ("SELECT ID, Profession, Course,Duration FROM manualtest WHERE Author='"
				+ details.replace("_", " ") + "'AND " + "Code IS NULL;");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql2);
			while (rs.next()) {
				String id = rs.getString("ID");
				String proff = rs.getString("Profession");
				String course = rs.getString("Course");
				int dur = rs.getInt("Duration");
				Test test = new Test(id, proff, course, dur);
				tests.add(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tests;
	}

	/**
	 * @param details contains the details for the query
	 * this method set the code for the exam
	 */
	public void SetCode(String details) {
		String[] s = details.split(" ");
		String sql = ("UPDATE tests SET Code='" + s[0] + "' WHERE ID='" + s[1] + "';");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql2 = ("UPDATE manualtest SET Code='" + s[0] + "' WHERE ID='" + s[1] + "';");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param details the author name
	 * @return arrayList of live exams that are available now
	 */
	public ArrayList<Test> GetLiveExams(String details) {
		ArrayList<Test> tests = new ArrayList<>();
		String sql = ("SELECT ID, Profession, Course,Duration,Code FROM tests WHERE Author='"
				+ details.replace("_", " ") + "'AND " + "Code IS NOT NULL AND Code!='-1';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("ID");
				String proff = rs.getString("Profession");
				String course = rs.getString("Course");
				int dur = rs.getInt("Duration");
				String code = rs.getString("Code");
				Test test = new Test(id, proff, course, dur, code);
				tests.add(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql2 = ("SELECT ID, Profession, Course,Duration,Code FROM manualtest WHERE Author='"
				+ details.replace("_", " ") + "'AND " + "Code IS NOT NULL AND Code!='-1';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql2);
			while (rs.next()) {
				String id = rs.getString("ID");
				String proff = rs.getString("Profession");
				String course = rs.getString("Course");
				int dur = rs.getInt("Duration");
				String code = rs.getString("Code");
				Test test = new Test(id, proff, course, dur, code);
				tests.add(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tests;
	}

	/**
	 * @param examId
	 * this method lock the exam by the ID
	 */
	public void LockExamByID(String examId) {
		String sql = ("UPDATE tests SET Code='-1' WHERE ID='" + examId + "';");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}String sql2 = ("UPDATE manualtest SET Code='-1' WHERE ID='" + examId + "';");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param testID
	 * @return this method gets the code of the exam by test ID
	 */
	public String GetCodeExam(String testID) {
		String sql = ("SELECT Code  FROM tests WHERE ID='" + testID + "';");
		String code = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				code = rs.getString("Code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * @param details contains the details for the query
	 * insert a request to the DB
	 */
	public void SendDurRequest(String details) {
		String[] str = details.split(" ");
		String sql = ("INSERT INTO durationrequests (TestId,TeacherID,Course,OldDuration,NewDuration,Reason,Status)"
				+ " VALUES('" + str[0] + "','" + str[1] + "','" + str[2].replace("_", " ") + "','"
				+ Integer.parseInt(str[3]) + "','" + Integer.parseInt(str[4]) + "','" + str[5].replace("_", " ")
				+ "','0');");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {

		}

	}

	/**
	 * @param exID
	 * this method remove the request from DB
	 */
	public void RemoveReq(String exID) {
		String sql = ("DELETE FROM durationrequests WHERE TestId='" + exID + "';");
		try {
			Statement st = conn.createStatement();
			st.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return ArrayList of duration requests so the principal can approve\decline
	 */
	public ArrayList<DurationRequests> GetDurationRequests() {
		ArrayList<DurationRequests> arr = new ArrayList<>();
		String sql = ("SELECT TestId, TeacherID, Course,OldDuration,NewDuration,Reason FROM durationrequests WHERE Status='0';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("TestId");
				String teacherid = rs.getString("TeacherID");
				String course = rs.getString("Course");
				int olddur = rs.getInt("OldDuration");
				int newdur = rs.getInt("NewDuration");
				String reason = rs.getString("Reason");
				DurationRequests durReq = new DurationRequests(id, teacherid, course, olddur, newdur, reason);
				arr.add(durReq);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * @param testID
	 * this method delete the request by the test ID
	 */
	public void DeleteRequest(String testID) {
		String sql = ("DELETE FROM durationrequests WHERE TestId='" + testID + "';");
		try {
			Statement st = conn.createStatement();
			st.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param testID
	 * this method approve the request
	 * @throws SQLException
	 */
	public void ApproveRequest(String testID) throws SQLException {
		Statement st = conn.createStatement();
		String sql = ("UPDATE durationrequests SET Status = '1' WHERE TestId = '" + testID + "';");
		st.executeUpdate(sql);

	}

	/**
	 * @param TeacherID
	 * this method gets the finished exam by specific teacher ID
	 * @return ArrayList of finished exams
	 */
	public ArrayList<FinishedTest> GetFinishedExams(String TeacherID) {
		ArrayList<FinishedTest> arr = new ArrayList<>();
		String sql = ("SELECT StudentID, ExamID, Profession, Course, Grade FROM finishedexam WHERE Status='0' AND TeacherID='"
				+ TeacherID + "' ;");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String StudentID = rs.getString("StudentID");
				String ExamID = rs.getString("ExamID");
				String Profession = rs.getString("Profession");
				String Course = rs.getString("Course");
				int Grade = rs.getInt("Grade");
				FinishedTest ftest = new FinishedTest(StudentID, ExamID, Profession, Course, Grade);
				arr.add(ftest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * @param details contains the details for the query
	 * this method update the new grade 
	 */
	public void UpdateNewGrade(String details) {
		String[] str = details.split(" ");
		try {
			Statement st = conn.createStatement();
			String sql = ("UPDATE finishedexam SET Grade='" + Integer.parseInt(str[3]) + "', Explanation='"
					+ str[4].replace("_", " ") + "' WHERE (TeacherID='" + str[0] + "') and (StudentID='" + str[1]
					+ "') and (ExamID='" + str[2] + "');");
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param details contains the details for the query
	 * this method approve the grade for the student
	 */
	public void ApproveGrade(String details) {
		String[] str = details.split(" ");
		String note = str[3].replace("_", " ");

		if (note.equals("J")) {
			note = " ";
		}

		try {
			Statement st = conn.createStatement();
			String sql = ("UPDATE finishedexam SET Status='1', Notes='" + note + "' WHERE (TeacherID='" + str[0]
					+ "') and (StudentID='" + str[1] + "') and (ExamID='" + str[2] + "');");
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param ExamID
	 * This method used to get Courses names from DB
	 * @return ArrayList of notes
	 */
	public ArrayList<String> GetNotes(String ExamID) {

		ArrayList<String> notes = new ArrayList<>();
		notes.add("notes");
		String sql = ("SELECT TextForTeacher,TextForStudent FROM finishedexam WHERE ExamID='" + ExamID + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String TeacherNotes = rs.getString("TextForTeacher");
				String StudentNotes = rs.getString("TextForStudent");
				notes.add(TeacherNotes);
				notes.add(StudentNotes);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notes;
	}

	/**
	 * @param exid
	 * this method return arraylist of check mistakes by exam id
	 * @return
	 */
	public ArrayList<CheckMistakes> GetCheckMistakesArr(String exid) {
		ArrayList<CheckMistakes> arr = new ArrayList<>();
		ArrayList<String> StudIDarr = new ArrayList<>();
		String sql = ("SELECT DISTINCT idStudent FROM questionsaftertest WHERE idTest='" + exid + "';");
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				StudIDarr.add(rs.getString("idStudent"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < StudIDarr.size(); i++) {
			String sql2 = ("SELECT idQuestion,CorrectAnswer,answerStud FROM questionsaftertest " + "WHERE idStudent='"
					+ StudIDarr.get(i) + "'AND idTest='" + exid + "';");
			try {
				List<QuestionsAfterTest> qatArr = new ArrayList<>();
				Statement st = conn.createStatement();
				ResultSet rs;
				rs = st.executeQuery(sql2);
				while (rs.next()) {
					String id = (rs.getString("idQuestion"));
					String correctAns = (rs.getString("CorrectAnswer"));
					String studAns = (rs.getString("answerStud"));
					QuestionsAfterTest q = new QuestionsAfterTest(id, correctAns, studAns);
					qatArr.add(q);
				}
				CheckMistakes checkMArr = new CheckMistakes(StudIDarr.get(i), qatArr);
				arr.add(checkMArr);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arr;
	}

	/**
	 * @param details
	 *  If the date and time are updated in the table?
	 * @return 0 or 1
	 */
	public String GetManualflag(String details) {
		String[] str = details.split(" ");
		String sql = ("SELECT ExamID FROM manualexamdata WHERE ExamID='" + str[0] + "';");
		String up = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				up = rs.getString("ExamID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (up != null)
			return "1";
		return "0";
	}

	/**
	 * @param details contains the details for the query
	 * insert the data for manual exam
	 */
	public void ExamManualData(String details) {
		String[] str = details.split(" ");
		String sql = ("INSERT INTO manualexamdata (ExamID,ExamDate,ExecutionTime,ExamOrgDuration,ExamActualTime,updating)"
				+ " VALUES('" + str[0] + "','" + str[1] + "','" + str[2].replace("_", " ") + "','" + str[3] + "','"
				+ str[4] + "','" + str[5] + "');");
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param exID
	 * remove manual exam code from DB by examID
	 */
	public void RemoveManualCode(String exID) {
		String sql = ("DELETE FROM manualtest WHERE ID='" + exID + "';");
		try {
			Statement st = conn.createStatement();
			st.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param details contains the details for the query
	 * this method update the date for manual exam
	 */
	public void ExamManualDateUp(String details) {
		String[] str = details.split(" ");
		int numOf = 0;
		String sql = ("SELECT NumOfStudent FROM manualexamdata WHERE ExamID='" + str[0] + "';");
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				numOf = rs.getInt("NumOfStudent");
				numOf++;
				String sql00 = ("UPDATE manualexamdata SET NumOfStudent='" + numOf + "' WHERE ExamID='" + str[0]
						+ "';");
				st.executeUpdate(sql00);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (str[2].equals("1")) {
			int numOf1 = 0;
			String sql1 = ("SELECT NumOfSubmit FROM manualexamdata WHERE ExamID='" + str[0] + "';");
			Statement st1;
			try {
				st1 = conn.createStatement();
				ResultSet rs;
				rs = st1.executeQuery(sql1);
				if (rs.next()) {
					numOf1 = rs.getInt("NumOfSubmit");
					numOf1++;
					String sql11 = ("UPDATE manualexamdata SET NumOfSubmit='" + numOf1 + "' WHERE ExamID='" + str[0]
							+ "';");
					st1.executeUpdate(sql11);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			int numOf2 = 0;
			String sql2 = ("SELECT NumOfUnSubmit FROM manualexamdata WHERE ExamID='" + str[0] + "';");
			Statement st2;
			try {
				st2 = conn.createStatement();
				ResultSet rs;
				rs = st2.executeQuery(sql2);
				if (rs.next()) {
					numOf2 = rs.getInt("NumOfUnSubmit");
					numOf2++;
					String sql22 = ("UPDATE manualexamdata SET NumOfUnSubmit='" + numOf2 + "' WHERE ExamID='" + str[0]
							+ "';");
					st2.executeUpdate(sql22);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param details contains the details for the query
	 * this method submit the manual exam
	 */
	public void SubmitManualExam(String details) {
		String[] s = details.split(" ");
		String sql = ("INSERT INTO finishedmanualexam (StudentID,ExamID,Profession,Course,Duration,Test)" + " VALUES('" + s[0] + "','"
				+ s[1] + "','" +s[2].replace("_", " ") + "','" + s[3].replace("_", " ") + "','"+s[4]+"',?);");
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			FileInputStream input = new FileInputStream(s[5].replace("_", " "));
			ps.setBlob(1, input);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	/**
	 * @param details contains the details for the query
	 * this method update the actual time of manual exam
	 */
	public void UpdataManualTimeActual(String details) {
		String[] str = details.split(" ");
		String sql = ("UPDATE manualexamdata SET ExamActualTime='"+str[1] + "' WHERE ExamID='"+str[0]+ "';");
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
     }
	
	/**
	 * @param testID
	 * @return the code of manual exam
	 */
	public String GetManualCodeExam(String testID) {
		String sql = ("SELECT Code  FROM manualtest WHERE ID='" + testID + "';");
		String code = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				code = rs.getString("Code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * @param ID
	 * @param path
	 * this method download the exam for the student 
	 * @throws IOException
	 */
	public void DownloadExam(String ID, String path) throws IOException {
		Statement myStmt = null;
		ResultSet rs = null;

		InputStream input = null;
		FileOutputStream output = null;

		String str = "\\ExamDoc.docx";
		path = path + str;

		File file = new File(path);

		try {
			myStmt = conn.createStatement();
			String sql = ("SELECT Test FROM manualtest WHERE Code='" + ID + "';");
			rs = myStmt.executeQuery(sql);

			output = new FileOutputStream(file);

			if (rs.next()) {

				input = rs.getBinaryStream("Test");


				byte buffer[] = new byte[1024];
				while (input.read(buffer) > 0)
					output.write(buffer);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (input != null)
				input.close();

			if (output != null)
				output.close();

		}

	}
	
	/**
	 * @param details contains the details for the query
	 * If the date and time are updated in the table?
	 * @return the flag 0 or 1
	 */
	public String Getflag(String details ) {
		String[] str = details.split(" ");
		String sql=("SELECT ExamID FROM examdata WHERE ExamID='" + str[0]+"';");
		String up = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				up = rs.getString("ExamID");
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(up!=null)
				return "1";
			return "0";
	}

	/**
	 * @param details contains the details for the query
	 * this method insert the exam data to DB
	 */
	public void ExamData(String details) {
		String[] str = details.split(" ");
		String sql = ("INSERT INTO examdata (ExamID,ExamDate,ExecutionTime,ExamOrgDuration,ExamActualTime,updating)"
				+ " VALUES('" + str[0] + "','" + str[1] + "','" + str[2].replace("_", " ") + "','"
				+ str[3] + "','" + str[4] +"','"+str[5]+ "');");
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param details contains the details for the query
	 * this method update the exam data in DB
	 */
	public void ExamDateUp(String details) {
		String[] str = details.split(" ");
		int numOf=0;
		String sql = ("SELECT NumOfStudent FROM examdata WHERE ExamID='" + str[0] + "';");
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				numOf = rs.getInt("NumOfStudent");
				numOf++;
				String sql00 = ("UPDATE examdata SET NumOfStudent='"+numOf + "' WHERE ExamID='"+str[0]+ "';");
				st.executeUpdate(sql00);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(str[2].equals("1")) {
			int numOf1=0;
			String sql1 = ("SELECT NumOfSubmit FROM examdata WHERE ExamID='" + str[0] + "';");
			Statement st1;
			try {
				st1 = conn.createStatement();
				ResultSet rs;
				rs = st1.executeQuery(sql1);
				if (rs.next()) {
					numOf1 = rs.getInt("NumOfSubmit");
					numOf1++;
					String sql11 = ("UPDATE examdata SET NumOfSubmit='"+numOf1 + "' WHERE ExamID='"+str[0]+ "';");
					st1.executeUpdate(sql11);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			int numOf2=0;
			String sql2 = ("SELECT NumOfUnSubmit FROM examdata WHERE ExamID='" + str[0] + "';");
			Statement st2;
			try {
				st2 = conn.createStatement();
				ResultSet rs;
				rs = st2.executeQuery(sql2);
				if (rs.next()) {
					numOf2 = rs.getInt("NumOfUnSubmit");
					numOf2++;
					String sql22 = ("UPDATE examdata SET NumOfUnSubmit='"+numOf2 + "' WHERE ExamID='"+str[0]+ "';");
					st2.executeUpdate(sql22);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param details contains the details for the query
	 * this method update the actual time of test
	 */
	public void UpdataTimeActual(String details) {
		String[] str = details.split(" ");
		String sql = ("UPDATE examdata SET ExamActualTime='"+str[1] + "' WHERE ExamID='"+str[0]+ "';");
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
     }
	
	
}
