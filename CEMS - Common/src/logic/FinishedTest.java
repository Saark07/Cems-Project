package logic;

import java.io.Serializable;
/**
* @author Ronen
*Class for Examining Completed Exams.
*/
@SuppressWarnings("serial")
public class FinishedTest implements Serializable {
	private String teacherID;
	private String studentID;
	private String examID;
	private int grade;
	private String course;
	private String proffession;

	public FinishedTest(String teacherID, String studentID, String examID, int grade, String course,
			String proffession) {
		super();
		this.teacherID = teacherID;
		this.studentID = studentID;
		this.examID = examID;
		this.grade = grade;
		this.course = course;
		this.proffession = proffession;
	}

	public FinishedTest(String studentID, String examID, String proffession, String course, int grade) {
		super();
		this.studentID = studentID;
		this.examID = examID;
		this.grade = grade;
		this.course = course;
		this.proffession = proffession;
	}

	public FinishedTest() {
	}

	public String getTeacherID() {
		return teacherID;
	}

	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getExamID() {
		return examID;
	}

	public void setExamID(String examID) {
		this.examID = examID;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getProffession() {
		return proffession;
	}

	public void setProffession(String proffession) {
		this.proffession = proffession;
	}

}
