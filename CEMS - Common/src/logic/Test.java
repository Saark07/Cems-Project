package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *Test Information Class
 */
@SuppressWarnings("serial")
public class Test implements Serializable {
	private String ID;
	private String Profession;
	private String Course;
	private int Duration;
	private int number;
	private String Author;
	private String textForStudent;
	private String TextForTeacher;
	private String QArray;
	private String Points;
	private String Code;
	
	public Test(String iD, String profession, String course, int duration, int number, String author,
			String textForStudent, String textForTeacher, String qArray, String points) {
		super();
		ID = iD;
		Profession = profession;
		Course = course;
		Duration = duration;
		this.number = number;
		Author = author;
		this.textForStudent = textForStudent;
		TextForTeacher = textForTeacher;
		QArray = qArray;
		Points = points;
	}
	public Test(String id,String proff,String course) {
		ID=id;
		Profession=proff;
		Course=course;
	}
	public Test(String id,String proff,String course,int duration) {
		ID=id;
		Profession=proff;
		Course=course;
		Duration=duration;
	}
	public Test(String id,String proff,String course,int duration,String Code) {
		ID=id;
		Profession=proff;
		Course=course;
		Duration=duration;
		this.Code=Code;
	}
	
	public Test() {
		
	}
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getProfession() {
		return Profession;
	}
	public void setProfession(String profession) {
		Profession = profession;
	}
	public String getCourse() {
		return Course;
	}
	public void setCourse(String course) {
		Course = course;
	}
	public int getDuration() {
		return Duration;
	}
	public void setDuration(int duration) {
		Duration = duration;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public String getTextForStudent() {
		return textForStudent;
	}
	public void setTextForStudent(String textForStudent) {
		this.textForStudent = textForStudent;
	}
	public String getTextForTeacher() {
		return TextForTeacher;
	}
	public void setTextForTeacher(String textForTeacher) {
		TextForTeacher = textForTeacher;
	}
	public String getQArray() {
		return QArray;
	}
	public void setQArray(String qArray) {
		QArray = qArray;
	}
	public String getPoints() {
		return Points;
	}
	public void setPoints(String points) {
		Points = points;
	}

	@Override
	public String toString() {
		return "Test [ID=" + ID + ", Profession=" + Profession + ", Course=" + Course + "]";
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	

	
}
