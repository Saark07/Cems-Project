package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *A class that saves questions of student after a test is performed.
 */
public class QuestionsForStud implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ID;
	private String Question;
	private String Answer1, Answer2, Answer3, Answer4;
	private String CorrectAnswer;
	private int Point;

	
	public QuestionsForStud(String iD, String question, String answer1, String answer2,
			String answer3, String answer4, String correctAnswer, int point) {
		super();
		ID = iD;

		Question = question;
		Answer1 = answer1;
		Answer2 = answer2;
		Answer3 = answer3;
		Answer4 = answer4;
		CorrectAnswer = correctAnswer;
		Point = point;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}



	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String question) {
		Question = question;
	}

	public String getCorrectAnswer() {
		return CorrectAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		CorrectAnswer = correctAnswer;
	}

	public int getPoint() {
		return Point;
	}

	public void setPoint(int point) {
		Point = point;
	}

	
	public String getAnswer1() {
		return Answer1;
	}

	public void setAnswer1(String answer1) {
		Answer1 = answer1;
	}

	public String getAnswer2() {
		return Answer2;
	}

	public void setAnswer2(String answer2) {
		Answer2 = answer2;
	}

	public String getAnswer3() {
		return Answer3;
	}

	public void setAnswer3(String answer3) {
		Answer3 = answer3;
	}

	public String getAnswer4() {
		return Answer4;
	}

	public void setAnswer4(String answer4) {
		Answer4 = answer4;
	}


}
