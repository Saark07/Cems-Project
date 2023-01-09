package controllers;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import Client.ChatClient;
import Client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Person;
import logic.QuestionsForStud;
import logic.TestToPerform;
/**
 * @author doron
 * 
 * Class of the exam itself.
 *
 */
public class TestController {

	@FXML
	private Button btnSubmit;

	@FXML
	private Button btnPrev;

	@FXML
	private TextArea txtAnswer1;

	@FXML
	private TextArea txtAnswer2;

	@FXML
	private TextArea txtAnswer3;

	@FXML
	private TextArea txtAnswer4;

	@FXML
	private RadioButton btnAnswer1;

	@FXML
	private ToggleGroup g1;

	@FXML
	private RadioButton btnAnswer2;

	@FXML
	private RadioButton btnAnswer3;

	@FXML
	private RadioButton btnAnswer4;

	@FXML
	private Label labelPoint;

	@FXML
	private Label labelQuestion;

	@FXML
	private TextArea txtQuestion;

	@FXML
	private Label txtNumOf;

	@FXML
	private TextField txtaFewQ;

	@FXML
	private Label LabelExamScreen;

	@FXML
	private Button btnFirstQuestion;

	@FXML
	private Button btnNext;

	@FXML
	private Label labelNameStud;

	@FXML
	private Label labelIdStud;

	@FXML
	private Label labelCourse;

	@FXML
	private Label goodLuck;

	@FXML
	private Button btnExit;

	@FXML
	private Label labelTimeLeft;

	@FXML
	private TextArea txtNotes;

	@FXML
	private Label labelNotes;

	@FXML
	private Label txtStudentName;

	@FXML
	private Label txtStudentId;

	@FXML
	private Label txtCourseName;

	@FXML
	private TextField txtTimeLeft;

	Stage stage;
	private int index = 0, started = 0, extraTime = 0;
	private Person per;
	private TestToPerform testP;
	List<QuestionsForStud> q = new ArrayList<>();
	String[] answerStud;
	boolean[] bool;
	private boolean flag = false, firstQ = false, lastQ = false, clickExit;
	boolean submitted = false, clickSubmit = false;
	String idOfQuestion = "", originalTimeTest = "", actualTimeTest = "";
	String question = "", pointsforQ = "", correctAnswer = "";
	String answer1 = "", answer2 = "", answer3 = "", answer4 = "";
	LocalTime localTime;
	LocalDate localDate;
	Date start = new Date();
	/**
	 * @param person
	 * @param test
	 * @param s
	 * Load test and student data on screen.
	 */
	public void loadDetails(Person person, TestToPerform test, Stage s) {

		testP = test;
		per = person;
		stage = s;
		answerStud = new String[test.getNumOfQuestion()];
		for (int i = 0; i < answerStud.length; i++) {
			answerStud[i] = "-1";
		}
		bool = new boolean[test.getNumOfQuestion()];
		for (int i = 0; i < bool.length; i++) {
			bool[i] = false;
		}
		ClientUI.chat.accept("Getflag " + testP.getIdTest());
		String up = (String) ChatClient.msgFromServer;

		if (up.equals("0")) { 
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("uuuu/MM/dd");
			localDate = LocalDate.now();
			DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");
			localTime = LocalTime.now();
			originalTimeTest = Integer.toString(testP.getTimeOfTest());

			ClientUI.chat.accept("ExamData " + testP.getIdTest() + " " + localDate + " " + localTime + " "
					+ originalTimeTest + " " + originalTimeTest + " " + "1");
		}

		q = (List<QuestionsForStud>) test.getQuestion();
		txtStudentName.setText(person.getFirstName() + " " + person.getLastName());
		txtStudentId.setText(person.getID());
		txtCourseName.setText(test.getCoures());
		LabelExamScreen.setText("Test screen");
		TimerTest(test.getTimeOfTest(), test.getIdTest());
		int p = q.get(0).getPoint();
		String point = Integer.toString(p);
		labelPoint.setText("Points: " + point);
		txtNumOf.setText("1)");
		txtaFewQ.setText("1/" + test.getNumOfQuestion());
		txtQuestion.setText(q.get(0).getQuestion());
		txtAnswer1.setText(q.get(0).getAnswer1());
		txtAnswer2.setText(q.get(0).getAnswer2());
		txtAnswer3.setText(q.get(0).getAnswer3());
		txtAnswer4.setText(q.get(0).getAnswer4());
		txtNotes.setText(test.getTextForStudent());
		btnPrev.setDisable(true);

	}

	// Timer fields
	private long min, sec, hours, totalSec = 0;
	/**
	 * @param value
	 * @return
	 * Helps display the clock correctly
	 */
	private String format(long value) {
		if (value < 10)
			return 0 + "" + value;
		return value + "";
	}
	/**
	 * Converts the time units
	 */
	public void convertTime() {
		min = TimeUnit.SECONDS.toMinutes(totalSec);
		sec = totalSec - (min * 60);
		hours = TimeUnit.MINUTES.toHours(min);
		min = min - (hours * 60);
		txtTimeLeft.setText("" + format(hours) + ":" + format(min) + ":" + format(sec) + "");
		totalSec--;

	}

	boolean isLocked = false;
	/**
	 * @param duration
	 * A timer ran for the duration of the exam.
	 */
	public void TimerTest(int duration, String TestID) {
		totalSec = duration * 60;
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				if (!isLocked) {
					convertTime();
					lockExam(TestID);
					extraTime = ExtraTime(TestID, duration);				
					totalSec += extraTime;
					if (totalSec <= 0) {
						ClientUI.chat.accept("LockExam "+TestID);
						timer.cancel();
						if (!submitted) {
							txtTimeLeft.setText("00:00:00");
							dataForSql();
							JOptionPane.showMessageDialog(null, "The time is up!");
						}
					}
				} else {
					timer.cancel();
					JOptionPane.showMessageDialog(null, "The exam is locked");
				}
			}

		};
		timer.schedule(timerTask, 0, 1000);
	}
	/**
	 * @param TestID
	 * @param duration
	 * @return
	 * Adds time to the exam
	 */
	public int ExtraTime(String TestID, int duration) {// JEN
		int newTime = 0;
		ClientUI.chat.accept("GetDuration " + TestID);
		if (ChatClient.Value >= duration && !DurationFlag) {
			DurationFlag = true;
			newTime = ChatClient.Value - duration;
			ClientUI.chat.accept("RemoveReq " + TestID);
		}
		return newTime * 60;
	}
	/**
	 * @param event
	 * @throws InterruptedException
	 * updates when the time is over
	 */
	@FXML
	void TimeIsOver(MouseEvent event) throws InterruptedException {

		if (txtTimeLeft.getText().equals("00:00:00") || isLocked) {
			if (!submitted) {

				Thread.sleep(1500);
				((Node) event.getSource()).getScene().getWindow().hide();
			}
		}
	}

	boolean DurationFlag = false;
	/**
	 * @param testID
	 * exam lock
	 */
	private void lockExam(String testID) {
		ClientUI.chat.accept("CheckLockExam " + testID);
		String code = (String) ChatClient.msgFromServer;
		if (code.equals("-1")) {
			isLocked = true;
			dataForSql();
		}
	}
	/**
	 * updates the data in database
	 */
	public void dataForSql() {
		q = (List<QuestionsForStud>) testP.getQuestion();
		for (int i = 0; i < q.size() - 1; i++) {
			idOfQuestion += q.get(i).getID() + " ";
			question += q.get(i).getQuestion() + "@";
			int p = q.get(i).getPoint();
			String point = Integer.toString(p);
			pointsforQ += point + " ";
			correctAnswer += q.get(i).getCorrectAnswer() + " ";
			answer1 += q.get(i).getAnswer1() + "@";
			answer2 += q.get(i).getAnswer2() + "@";
			answer3 += q.get(i).getAnswer3() + "@";
			answer4 += q.get(i).getAnswer4() + "@";
		}
		int p1 = q.get(q.size() - 1).getPoint();
		String point = Integer.toString(p1);
		pointsforQ += point;
		idOfQuestion += q.get(q.size() - 1).getID();
		question += q.get(q.size() - 1).getQuestion();
		correctAnswer += q.get(q.size() - 1).getCorrectAnswer();
		answer1 += q.get(q.size() - 1).getAnswer1();
		answer2 += q.get(q.size() - 1).getAnswer2();
		answer3 += q.get(q.size() - 1).getAnswer3();
		answer4 += q.get(q.size() - 1).getAnswer4();

		String ExecutionTime = "";
		int grade;
		if (clickExit) {
			grade = 0;
			ExecutionTime = "clickExit";
			for (int i = 0; i < answerStud.length; i++) {
				answerStud[i] = "-1";
			}
		} else {
			Date end = new Date();
			long diff = ((end.getTime() - start.getTime()) / 1000);
			double totalTime = (float) diff / 60;
			totalTime = totalTime * 100;
			int temp = (int) totalTime;
			totalTime = (temp / 100.0);
			ExecutionTime = "" + totalTime + "minutes";
			grade = checkTestBySystem(answerStud, q);

		}

		String ansStud = "";
		for (int i = 0; i < answerStud.length - 1; i++) {
			ansStud = ansStud + answerStud[i] + " ";
		}
		ansStud += answerStud[answerStud.length - 1];

		if (clickSubmit) {
			ClientUI.chat.accept("ExamDateUp " + testP.getIdTest() + " " + started + " " + "1");
		} else if (!clickSubmit && flag) {
			ClientUI.chat.accept("ExamDateUp " + testP.getIdTest() + " " + started + " " + "1");
		} else if (!clickSubmit && isLocked && flag) {
			ClientUI.chat.accept("ExamDateUp " + testP.getIdTest() + " " + started + " " + "1");
		} else if (!clickSubmit && isLocked && !flag) {
			ClientUI.chat.accept("ExamDateUp " + testP.getIdTest() + " " + started + " " + "0");
		} else if (clickExit) {
			ClientUI.chat.accept("ExamDateUp " + testP.getIdTest() + " " + started + " " + "0");
		} else if (!clickSubmit && !flag) {
			ClientUI.chat.accept("ExamDateUp " + testP.getIdTest() + " " + started + " " + "0");
		}

		ClientUI.chat.accept("GetTeacherID " + testP.getAuthor());// 315667854
		String idTeacher = (String) ChatClient.TeacherID;

		ClientUI.chat.accept("SubmitTest " + idTeacher + " " + per.getID() + " " + testP.getIdTest() + " " + grade + " "
				+ testP.getProfession().replace(" ", "_") + " " + testP.getCoures().replace(" ", "_") + " "
				+ ExecutionTime + " " + idOfQuestion.replace(" ", "_") + " " + pointsforQ.replace(" ", "_") + " "
				+ testP.getTextForStudent().replace(" ", "_") + " " + testP.getTextForTeacher().replace(" ", "_")
				+ " 0");

		ClientUI.chat.accept("SubmitQuestion " + per.getID() + " " + testP.getIdTest() + " "
				+ idOfQuestion.replace(" ", "_") + " " + question.replace(" ", "_") + " "
				+ correctAnswer.replace(" ", "_") + " " + ansStud.replace(" ", "_") + " " + answer1.replace(" ", "_")
				+ " " + answer2.replace(" ", "_") + " " + answer3.replace(" ", "_") + " " + answer4.replace(" ", "_"));
		if (DurationFlag) {
			int temp = testP.getTimeOfTest() + extraTime;
			actualTimeTest = "" + temp;
			ClientUI.chat.accept("UpdataTimeActual " + testP.getIdTest() + " " + actualTimeTest);
		}
	}
	/**
	 * @param e
	 * @throws IOException
	 * close screen
	 */
	@FXML
	public void ExitBtn(ActionEvent e) throws IOException {
		Alert a1 = new Alert(AlertType.WARNING, " ", ButtonType.NO, ButtonType.YES);
		a1.setHeaderText("You will get 0\nAre you sure?");
		Optional<ButtonType> res = a1.showAndWait();
		if (res.get() == ButtonType.YES) {
			JOptionPane.showMessageDialog(null, "Bye Bye!!! " + per.getFirstName() + " " + per.getLastName());
			submitted = true;
			clickExit = true;
			dataForSql();
			((Node) e.getSource()).getScene().getWindow().hide();
		}
	}
	/**
	 * @param e
	 * test submition
	 */
	@FXML
	public void handelBtnSubmit(ActionEvent e) {
		if (e.getSource() == btnSubmit) {
			if (flag) {// Answer all the questions;
				Alert a1 = new Alert(AlertType.WARNING, " ", ButtonType.NO, ButtonType.YES);
				a1.setHeaderText("Do you want to submit?");
				Optional<ButtonType> res = a1.showAndWait();
				if (res.get() == ButtonType.YES) {
					submitted = true;
					JOptionPane.showMessageDialog(null, "Bye Bye!!! " + per.getFirstName() + " " + per.getLastName()
							+ " Your test has been submitted");
					dataForSql();
					clickSubmit = true;
					((Node) e.getSource()).getScene().getWindow().hide();
				}
			} else {// Did not answer all questions
				Alert a1 = new Alert(AlertType.WARNING, " ", ButtonType.NO, ButtonType.YES);
				a1.setHeaderText("You did not answer all the questions, continue?");
				Optional<ButtonType> res1 = a1.showAndWait();
				if (res1.get() == ButtonType.YES) {
					Alert a2 = new Alert(AlertType.WARNING, " ", ButtonType.NO, ButtonType.YES);
					a2.setHeaderText("Do you want to submit?");
					Optional<ButtonType> res2 = a2.showAndWait(); 
					if (res2.get() == ButtonType.YES) {
						submitted = true;
						JOptionPane.showMessageDialog(null, "Bye Bye!!! " + per.getFirstName() + " " + per.getLastName()
								+ " Your test has been submitted");
						clickSubmit = true;
						dataForSql();
						((Node) e.getSource()).getScene().getWindow().hide();
					}
				}
			}
		}
	}
	/**
	 * @param answerStud
	 * @param q
	 * @return
	 * Automatic examination test
	 */
	private int checkTestBySystem(String[] answerStud, List<QuestionsForStud> q) {
		int sum = 0;
		for (int i = 0; i < answerStud.length; i++) {
			if (answerStud[i].equals(q.get(i).getCorrectAnswer())) {
				sum = sum + q.get(i).getPoint();
			}
		}
		return sum;
	}
	/**
	 * @param e
	 * * Go to the next question on
	 */
    
	@FXML
	public void handelBtnNext(ActionEvent e) {
		if (e.getSource() == btnNext) {
			resetColor();
			Cancellation();
			q = (List<QuestionsForStud>) testP.getQuestion();
			int p = q.get(index).getPoint();
			String point = Integer.toString(p);
			if (index == q.size() - 1) {
				if (!(lastQ)) {
					JOptionPane.showMessageDialog(null, "No more questions");
					lastQ = !lastQ;
				}
				if (bool[index] == true) {
					// lockChoise();
					resetColor();
					MarkAnswer(answerStud[index]);
					unCancellation(answerStud[index]);

				} else {
					resetChoise();

				}
			} else {
				index++;
				if (index == q.size() - 1) {
					btnNext.setDisable(true);
					btnPrev.setDisable(false);
				} else
					btnPrev.setDisable(false);
				labelPoint.setText("Points: " + point);
				txtNumOf.setText((index + 1) + ")");
				txtaFewQ.setText((index + 1) + "/" + testP.getNumOfQuestion());
				txtQuestion.setText(q.get(index).getQuestion());
				txtAnswer1.setText(q.get(index).getAnswer1());
				txtAnswer2.setText(q.get(index).getAnswer2());
				txtAnswer3.setText(q.get(index).getAnswer3());
				txtAnswer4.setText(q.get(index).getAnswer4());
				if (bool[index] == true) {
					// lockChoise();
					resetColor();
					MarkAnswer(answerStud[index]);
					unCancellation(answerStud[index]);

				} else {
					resetChoise();

				}
			}
		}
	}
	/**
     * @param e
     * Back to the previous question in the exam.
     */
	@FXML
	public void handelBtnPrev(ActionEvent e) {
		if (e.getSource() == btnPrev) {
			resetColor();
			Cancellation();
			q = (List<QuestionsForStud>) testP.getQuestion();
			int p = q.get(index).getPoint();
			String point = Integer.toString(p);
			if (index == 0) {
				if (!(firstQ)) {
					JOptionPane.showMessageDialog(null, "This is a first question");
					firstQ = !firstQ;
				}
				if (bool[index] == true) {
					// lockChoise();
					resetColor();
					MarkAnswer(answerStud[index]);
					unCancellation(answerStud[index]);

				} else {
					resetChoise();

				}
			} else {
				index--;
				if (index == 0) {
					btnPrev.setDisable(true);
					btnNext.setDisable(false);
				} else
					btnNext.setDisable(false);
				labelPoint.setText("Points: " + point);
				txtNumOf.setText((index + 1) + ")");
				txtaFewQ.setText((index + 1) + "/" + testP.getNumOfQuestion());
				txtQuestion.setText(q.get(index).getQuestion());
				txtAnswer1.setText(q.get(index).getAnswer1());
				txtAnswer2.setText(q.get(index).getAnswer2());
				txtAnswer3.setText(q.get(index).getAnswer3());
				txtAnswer4.setText(q.get(index).getAnswer4());
				if (bool[index] == true) {
					// lockChoise();
					resetColor();
					MarkAnswer(answerStud[index]);
					unCancellation(answerStud[index]);

				} else {
					resetChoise();

				}
			}
		}
	}
	/**
	 * @param e
	 *  * Go directly to the first question.
	 */
	@FXML
	public void handelBtnFirstQ(ActionEvent e) {
		if (e.getSource() == btnFirstQuestion) {
			q = (List<QuestionsForStud>) testP.getQuestion();
			btnPrev.setDisable(true);
			btnNext.setDisable(false);
			index = 0;
			if (bool[index] == true) {
				// lockChoise();
				resetColor();
				MarkAnswer(answerStud[index]);
				unCancellation(answerStud[index]);
			} else {
				resetChoise();
				Cancellation();

			}
			int p = q.get(0).getPoint();
			String point = Integer.toString(p);
			btnPrev.setDisable(true);
			labelPoint.setText("Points: " + point);
			txtNumOf.setText("1)");
			txtaFewQ.setText((index + 1) + "/" + testP.getNumOfQuestion());
			txtQuestion.setText(q.get(index).getQuestion());
			txtAnswer1.setText(q.get(0).getAnswer1());
			txtAnswer2.setText(q.get(0).getAnswer2());
			txtAnswer3.setText(q.get(0).getAnswer3());
			txtAnswer4.setText(q.get(0).getAnswer4());
		}
	}
	/**
	 * @param e
	 * answer selection
	 */
	@FXML
	public void handelBtnSelectAns1(MouseEvent e) {
		if (btnAnswer1.isSelected()) {
			txtAnswer1.setStyle("-fx-background-color: green;");
			txtAnswer2.setStyle("-fx-background-color: white;");
			txtAnswer3.setStyle("-fx-background-color: white;");
			txtAnswer4.setStyle("-fx-background-color: white;");
			btnAnswer2.setSelected(false);
			btnAnswer3.setSelected(false);
			btnAnswer4.setSelected(false);
			answerStud[index] = "1";

			bool[index] = true;
			allMarked();

		}
	}
	/**
	 * @param e
	* answer selection
	 */
	@FXML
	public void handelBtnSelectAns2(MouseEvent e) {
		if (btnAnswer2.isSelected()) {
			txtAnswer2.setStyle("-fx-background-color: green;");
			txtAnswer1.setStyle("-fx-background-color: white;");
			txtAnswer3.setStyle("-fx-background-color: white;");
			txtAnswer4.setStyle("-fx-background-color: white;");
			btnAnswer1.setSelected(false);
			btnAnswer3.setSelected(false);
			btnAnswer4.setSelected(false);
			answerStud[index] = "2";

			bool[index] = true;
			allMarked();

		}
	}
	/**
	 * @param e
	* answer selection
	 */
	@FXML
	public void handelBtnSelectAns3(MouseEvent e) {
		if (btnAnswer3.isSelected()) {
			txtAnswer3.setStyle("-fx-background-color: green;");
			txtAnswer2.setStyle("-fx-background-color: white;");
			txtAnswer1.setStyle("-fx-background-color: white;");
			txtAnswer4.setStyle("-fx-background-color: white;");
			btnAnswer2.setSelected(false);
			btnAnswer1.setSelected(false);
			btnAnswer4.setSelected(false);
			answerStud[index] = "3";
			bool[index] = true;
			allMarked();

		}
	}
	/**
	 * @param e
	* answer selection
	 */
	@FXML
	public void handelBtnSelectAns4(MouseEvent e) {
		if (btnAnswer4.isSelected()) {
			txtAnswer4.setStyle("-fx-background-color: green;");
			txtAnswer1.setStyle("-fx-background-color: white;");
			txtAnswer3.setStyle("-fx-background-color: white;");
			txtAnswer2.setStyle("-fx-background-color: white;");
			btnAnswer2.setSelected(false);
			btnAnswer3.setSelected(false);
			btnAnswer1.setSelected(false);
			answerStud[index] = "4";
			bool[index] = true;
			allMarked();

		}
	}

	public void resetColor() {
		txtAnswer1.setStyle("-fx-background-color: white;");
		txtAnswer2.setStyle("-fx-background-color: white;");
		txtAnswer3.setStyle("-fx-background-color: white;");
		txtAnswer4.setStyle("-fx-background-color: white;");
	}
	/**
	 * @param m
     * Reset the correct and incorrect answer markers.
     */
	public void MarkAnswer(String m) {
		if (m.equals("1")) {
			txtAnswer1.setStyle("-fx-background-color: green;");
		} else if (m.equals("2")) {
			txtAnswer2.setStyle("-fx-background-color: green;");
		} else if (m.equals("3")) {
			txtAnswer3.setStyle("-fx-background-color: green;");
		} else if (m.equals("4")) {
			txtAnswer4.setStyle("-fx-background-color: green;");
		}
	}
	/**
	 * reset the choice
	 */
	public void resetChoise() {
		btnAnswer1.setDisable(false);
		btnAnswer2.setDisable(false);
		btnAnswer3.setDisable(false);
		btnAnswer4.setDisable(false);
	}
	/**
	 * lock the right choice
	 */
	public void lockChoise() {
		btnAnswer1.setDisable(true);
		btnAnswer2.setDisable(true);
		btnAnswer3.setDisable(true);
		btnAnswer4.setDisable(true);
	}

	/**
	 * cancel the choices
	 */
	public void Cancellation() {
		btnAnswer1.setSelected(false);
		btnAnswer2.setSelected(false);
		btnAnswer3.setSelected(false);
		btnAnswer4.setSelected(false);
	}

	/**
	 * @param m
	 * 
	 */
	public void unCancellation(String m) {
		if (m.equals("1")) {
			btnAnswer1.setSelected(true);
			btnAnswer2.setSelected(false);
			btnAnswer3.setSelected(false);
			btnAnswer4.setSelected(false);
		} else if (m.equals("2")) {
			btnAnswer1.setSelected(false);
			btnAnswer2.setSelected(true);
			btnAnswer3.setSelected(false);
			btnAnswer4.setSelected(false);
		} else if (m.equals("3")) {
			btnAnswer1.setSelected(false);
			btnAnswer2.setSelected(false);
			btnAnswer3.setSelected(true);
			btnAnswer4.setSelected(false);
		} else if (m.equals("4")) {
			btnAnswer1.setSelected(false);
			btnAnswer2.setSelected(false);
			btnAnswer3.setSelected(false);
			btnAnswer4.setSelected(true);
		}
	}
	/**
	 * Checks if all questions are answered
	 */
	public void allMarked() {
		for (int i = 0; i < bool.length; i++) {
			if (bool[i] == false) {
				return;
			}
		}
		if (!flag) {
			flag = !flag;
		}
		return;
	}
}
