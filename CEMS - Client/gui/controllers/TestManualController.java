package controllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.ManualTest;
import logic.Person;
/**
 * @author doron
 * Class downloads a manual exam file and uploads it for submission.
 *
 */
public class TestManualController {

	@FXML
	private Button btnUpload;

	@FXML
	private Button btnBrowse;

	@FXML
	private Button btnDownload;

	@FXML
	private TextField txtPathFile;

	@FXML
	private Label labelPathFile;

	@FXML
	private Label labelTitelScreen;

	@FXML
	private TextField txtTimeLeft;

	@FXML
	private Button btnExit;

	@FXML
	private Label labelStudName;

	@FXML
	private TextField txtStudName;

	@FXML
	private Label labelStudId;

	@FXML
	private TextField txtStudId;

	@FXML
	private TextField txtCouresName;

	@FXML
	private Label labelCouresName;

	private Person per;
	private ManualTest testManual;
	private List<String> lstFile;
	private String ExamID;
	private String Code;
	LocalTime localTime;
	LocalDate localDate;
	private int extraTime = 0;
	private String originalTimeTest = "";
	Date start = new Date();
	/**
     * @param person
     * @param test
     * Loading the details of the student being examined.
     */
	public void loadDetails(Person person, ManualTest test, String code) {
		per = person;
		testManual = test;
		ExamID = test.getIdTest();
		Code = code;
		TimerTest(test.getDuration(), test.getIdTest());
		txtStudName.setText(person.getFirstName() + " " + person.getLastName());
		txtStudId.setText(person.getID());
		start = new Date();
		txtCouresName.setText(test.getCoures());
		int timeTest = test.getDuration();
		lstFile = new ArrayList<>();
		lstFile.add("*.doc");
		lstFile.add("*.docx");
		lstFile.add("*.DOC");
		lstFile.add("*.DOCX");
		ClientUI.chat.accept("GetManualflag " + ExamID);
		String up = (String) ChatClient.Code;
		if (up.equals("0")) {
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("uuuu/MM/dd");
			localDate = LocalDate.now();
			
			DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");
			localTime = LocalTime.now();
			originalTimeTest = Integer.toString(testManual.getDuration());
	
			ClientUI.chat.accept("ExamManualData " + ExamID + " " + localDate + " " + localTime + " " + originalTimeTest
					+ " " + originalTimeTest + " " + "1");
		}

	}

	// Timer fields
	private long min, sec, hours, totalSec = 0;

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
	boolean submitted = false;

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
	 * Calculate the extra time for the test if necessary
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

	boolean clickExit, clickSubmit = false;
	private int started = 0;

	private String actualTimeTest;
	/**
	 * sets sql tabales
	 */
	public void dataForSql() {

		String ExecutionTime = "";
		if (clickExit) {
			ExecutionTime = "clickExit";
		} else {
			Date end = new Date();

			long diff = ((end.getTime() - start.getTime()) / 1000);
			double totalTime = (float) diff / 60;
			totalTime = totalTime * 100;
			int temp = (int) totalTime;
			totalTime = (temp / 100.0);
			ExecutionTime = "" + totalTime + "minutes";
		}

		if (clickSubmit) {

			ClientUI.chat.accept("ExamManualDateUp " + ExamID + " " + started + " " + "1");

		} else if (!clickSubmit) {

			ClientUI.chat.accept("ExamManualDateUp " + ExamID + " " + started + " " + "1");

		} else if (!clickSubmit && isLocked) {

			ClientUI.chat.accept("ExamManualDateUp " + ExamID + " " + started + " " + "1");

		} else if (!clickSubmit && isLocked) {

			ClientUI.chat.accept("ExamManualDateUp " + ExamID + " " + started + " " + "0");

		} else if (clickExit) {

			ClientUI.chat.accept("ExamManualDateUp " + ExamID + " " + started + " " + "0");

		} else if (!clickSubmit) {

			ClientUI.chat.accept("ExamManualDateUp " + ExamID + " " + started + " " + "0");

		}
		if (filePath != null)

			ClientUI.chat.accept("SubmitManualTest " + per.getID() + " " + ExamID + " "
					+ testManual.getProfession().replace(" ", "_") + " " + testManual.getCoures().replace(" ", "_")
					+ " " + ExecutionTime + " " + filePath.replace(" ", "_"));

		// studid examid prof course duration path
		if (DurationFlag) {
			int temp = testManual.getDuration() + extraTime;
			actualTimeTest = "" + temp;
			ClientUI.chat.accept("UpdataManualTimeActual " + ExamID + " " + actualTimeTest);
		}
	}

	String filePath;
	/**
	 * @param event
	 * @throws InterruptedException
	 * Closes the test screen as soon as the test is locked or the time runs out
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
	 * exam locked
	 */
	private void lockExam(String testID) {
		ClientUI.chat.accept("CheckManualLockExam " + testID);
		String code = (String) ChatClient.msgFromServer;
		if (code.equals("-1")) {
			isLocked = true;
			dataForSql();
		}
	}
	/**
     * @param e
     * Close the screen and return to the main screen.
     */
	@FXML
	public void handelBtnExit(ActionEvent e) {
		if (e.getSource() == btnExit) {
			JOptionPane.showMessageDialog(null, "Bye Bye!!  You return to the main screen");
			System.out.println("Bye Bye!!");
			clickExit = true;
			submitted = true;
			((Node) e.getSource()).getScene().getWindow().hide();
		}
	}
	/**
     * @param e
     * Upload the resolved exam file.
     */
	@FXML
	private void handelBtnBrowse(ActionEvent e) {// upload
		if (e.getSource() == btnBrowse) {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("Word Files", lstFile));
			File f = fc.showOpenDialog(null);
			if (f != null) {
				txtPathFile.setText(f.getAbsolutePath());
				filePath = f.getAbsolutePath();
			}
		}
	}
	/**
	 * @param e
	 * Allows the student to submit the test
	 */
	@FXML
	private void handelBtnUploadEaxm(ActionEvent e) {// submit test
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (filePath != null) {
			alert.setContentText("Exam uploaded successfully");
			alert.setTitle("Exam Upload");
			alert.showAndWait();
			submitted = true;
			clickSubmit = true;
			dataForSql();
			((Node) e.getSource()).getScene().getWindow().hide();
		} else {
			alert.setContentText("Must select file path");
			alert.setTitle("Error");
			alert.showAndWait();
		}
	}
	/**
	 * @param e
	 * /**
     * @param e
     * Download the exam file.
     */
	@FXML
	private void handelBtnDownload(ActionEvent e) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(null);
		ClientUI.chat.accept("DownloadExam " + ExamID + " " + selectedDirectory.getAbsolutePath());

	}

}