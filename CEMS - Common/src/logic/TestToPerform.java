package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ronen
 *Class for the detail of a test in progress
 */
public class TestToPerform implements Serializable {


	
	private static final long serialVersionUID = 1L;
	private	String IdTest,Profession, Coures;
    private String TextForStudent, TextForTeacher;
    private String Author;
    private int TimeOfTest, NumOfQuestion;
    List<QuestionsForStud> question=new ArrayList<>();
    
    
	


	public TestToPerform(String idTest, String profession, String coures, String textForStudent,
			String textForTeacher, String author, int timeOfTest, int numOfQuestion, List<QuestionsForStud> question) {
		super();
		IdTest = idTest;
		Profession=profession;
		Coures = coures;
		TextForStudent = textForStudent;
		TextForTeacher = textForTeacher;
		Author = author;
		TimeOfTest = timeOfTest;
		NumOfQuestion = numOfQuestion;
		this.question = question;
	}

	public String getIdTest() {
		return IdTest;
	}

	public void setIdTest(String idTest) {
		IdTest = idTest;
	}

	public String getCoures() {
		return Coures;
	}

	public void setCoures(String coures) {
		Coures = coures;
	}

	public String getProfession() {
		return Profession;
	}

	public void setProfession(String profession) {
		Profession = profession;
	}

	public String getTextForStudent() {
		return TextForStudent;
	}

	public void setTextForStudent(String textForStudent) {
		TextForStudent = textForStudent;
	}

	public String getTextForTeacher() {
		return TextForTeacher;
	}

	public void setTextForTeacher(String textForTeacher) {
		TextForTeacher = textForTeacher;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public int getTimeOfTest() {
		return TimeOfTest;
	}

	public void setTimeOfTest(int timeOfTest) {
		TimeOfTest = timeOfTest;
	}

	public int getNumOfQuestion() {
		return NumOfQuestion;
	}

	public void setNumOfQuestion(int numOfQuestion) {
		NumOfQuestion = numOfQuestion;
	}

	public List<QuestionsForStud> getQuestion() {
		return question;
	}

	public void setQuestion(List<QuestionsForStud> question) {
		this.question = question;
	}


	@Override
	public String toString() {
		return "TestToPerform [question=" + question + "]";
	}
}
