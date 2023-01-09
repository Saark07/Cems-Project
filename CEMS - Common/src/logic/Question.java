package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *A class that saves questions of student after a test is performed.
 */
@SuppressWarnings("serial")
public class Question implements Serializable {
	private String profession;
	private String Course;
	private String instructions;
	private String ID;
	private int number;
	private String Author;
	private String[] Answers = new String[4];
	private String CorrectAnswer;

	public Question(String profession, String course, String instructions, String iD, int number, String author,
			String[] answers, String correctAnswer) {
		super();
		this.profession = profession;
		Course = course;
		this.instructions = instructions;
		ID = iD;
		this.number = number;
		Author = author;
		Answers = answers;
		CorrectAnswer = correctAnswer;
	}

	public Question() {
		// TODO Auto-generated constructor stub
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCourse() {
		return Course;
	}

	public void setCourse(String courses) {
		Course = courses;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

	public String[] getAnswers() {
		return Answers;
	}

	public void setAnswers(String[] answers) {
		Answers = answers;
	}

	public String getCorrectAnswer() {
		return CorrectAnswer;
	}

	public void setCorrectAnswer(String string) {
		CorrectAnswer = string;
	}

	public String toString() {

		String str = profession + "_" + Course + "_" + Author + "_" + instructions + "_" + CorrectAnswer + "_"
				+ Answers[0] + "_" + Answers[1] + "_" + Answers[2] + "_" + Answers[3];
		return str;
	}

}
