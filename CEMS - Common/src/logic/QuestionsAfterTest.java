package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *A class that saves questions after a test is performed.
 */
public class QuestionsAfterTest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID, Question, CorrectAnswer, AnswerStud;
	private String Answer1, Answer2, Answer3, Answer4;
	private String Point;
	
	public QuestionsAfterTest(String id, String question,String point, String correctAnswer,String answerStud, String answer1, String answer2,
			String answer3, String answer4) {
		super();
		
		ID=id;
		Question = question;
		CorrectAnswer = correctAnswer;
		AnswerStud=answerStud;
		Answer1 = answer1;
		Answer2 = answer2;
		Answer3 = answer3;
		Answer4 = answer4;
		Point=point;
	}
	
	public QuestionsAfterTest(String iD, String correctAnswer, String answerStud) {
		super();
		ID = iD;
		CorrectAnswer = correctAnswer;
		AnswerStud = answerStud;
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

	public String getAnswerStud() {
		return AnswerStud;
	}


	public void setAnswerStud(String answerStud) {
		AnswerStud = answerStud;
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

	@Override
	public String toString() {
		return "QuestionsAfterTest [ID=" + ID + ", Question=" + Question + ", CorrectAnswer=" + CorrectAnswer
				+ ", AnswerStud=" + AnswerStud + ", Answer1=" + Answer1 + ", Answer2=" + Answer2 + ", Answer3="
				+ Answer3 + ", Answer4=" + Answer4 + "]";
	}

	public String getPoint() {
		return Point;
	}

	public void setPoint(String point) {
		Point = point;
	}
	
}
