package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Client.ChatClient;
import Client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import logic.CheckMistakes;
import logic.Person;
import logic.QuestionsAfterTest;
import logic.TestCopy;

/**
 * @author edena
 * Through this department the teacher can approve / view the exam
 *
 */
public class TestExamApproveController implements Initializable {

	@FXML
	private RadioButton Choice1;

	@FXML
	private ToggleGroup g1;

	@FXML
	private TextArea txtQuestions;

	@FXML
	private TextField txtAnswer1;

	@FXML
	private TextField txtAnswer2;

	@FXML
	private TextField txtAnswer3;

	@FXML
	private TextField txtAnswer4;

	@FXML
	private RadioButton Choice2;

	@FXML
	private RadioButton Choice3;

	@FXML
	private RadioButton Choice4;

	@FXML
	private TextArea TestNotesTxt;

	@FXML
	private Label TestNotes;

	@FXML
	private TextField txtaFewQ;

	@FXML
	private Label txtNunOfQ;

	@FXML
	private Button btnPrevQ;

	@FXML
	private Button btnNextQ;

	@FXML
	private Button btnFirstQ;

	@FXML
	private TextArea txtNotes;

	@FXML
	private Label labrlExplanation;

	@FXML
	private Label labelNotes;

	@FXML
	private Label lblnoanswer;

	@FXML
	private Label lblPoints;

	@FXML
	private Button btnExit;

	@FXML
	private Label labelName;

	@FXML
	private Label labelCoures;

	@FXML
	private Label labelGrade;

	@FXML
	private Label titleScreen;

	@FXML
	private Label txtStudId;

	@FXML
	private Label txtGrade;

	@FXML
	private Label txtCoures;

	@FXML
	private TextArea txtExplanation;

	@FXML
	private TextArea TeacherNotes;

	@FXML
	private TextField txtChangeGrade;

	@FXML
	private Button AprroveBtn;

	@FXML
	private Label TeacherNoteslbl;

	@FXML
	private Button SubmitNewGradeBtn;
	@FXML
	private Label Cheatedlbl;
	/**
	 * @param event
	 * Test confirmation button
	 */
	@FXML
	void handelAprroveBtn(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		String notes = "J";
		alert.setHeaderText(null);
		if (!TestNotesTxt.getText().isEmpty()) {
			notes = TestNotesTxt.getText();
		}
		notes.replace(" ", "_");
		
		ClientUI.chat.accept("ApproveGrade " + per.getID() + " " + txtStudId.getText() + " " + testID + " "
				+ notes);

		alert.setTitle("Test Approved");
		alert.setContentText("The test approved ");
		alert.showAndWait();
		((Node) event.getSource()).getScene().getWindow().hide();

	}
	/**
	 * @param event
	 * Change of score and update
	 */

	@FXML
	void handelSubmitNewGrade(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (!txtExplanation.getText().isEmpty() && !txtChangeGrade.getText().isEmpty()) {
			ClientUI.chat.accept("UpdateNewGrade " + per.getID() + " " + txtStudId.getText() + " " + testID + " "
					+ txtChangeGrade.getText() + " " + txtExplanation.getText().replace(" ", "_"));
			txtGrade.setText(txtChangeGrade.getText());
			alert.setTitle("Update Grade");
			alert.setContentText("Update grade receive");
			alert.showAndWait();
		} else {
			alert.setTitle("Empty selection");
			alert.setContentText("Please set new grade and nice explanation");
			alert.showAndWait();
		}
	}
	/**
     * @param e
     * Close the screen and return to the main screen.
     */
	@FXML
	void handelBtnExit(ActionEvent event) {
		if (event.getSource() == btnExit) {
			JOptionPane.showMessageDialog(null, "Bye Bye!!  You return to the main screen");
			System.out.println("Bye Bye!!");
			((Node) event.getSource()).getScene().getWindow().hide();
		}
	}
	/**
	 * @param event
	 * Transfer directly to the first question
	 */
	@FXML
	void handelBtnFirstQuestions(ActionEvent event) {

		if (event.getSource() == btnFirstQ) {
			resetColor();
			btnPrevQ.setDisable(true);
			btnNextQ.setDisable(false);
			q = (List<QuestionsAfterTest>) copy.getqArray();
			txtQuestions.setText(q.get(0).getQuestion());
			txtNunOfQ.setText("1)");
			txtAnswer1.setText(q.get(0).getAnswer1());
			txtAnswer2.setText(q.get(0).getAnswer2());
			txtAnswer3.setText(q.get(0).getAnswer3());
			txtAnswer4.setText(q.get(0).getAnswer4());
			lblPoints.setText("Points: " + q.get(0).getPoint());
			txtaFewQ.setText("1/" + q.size());
			String choiceStud = q.get(0).getAnswerStud();
			String currecet = q.get(0).getCorrectAnswer();
			loadQ(choiceStud, currecet);
			i = 0;
		}
	}

	private int i = 0;
	private boolean firstQ = false, lastQ = false;

	/**
	 * @param event
	 * Transfer directly to the next question
	 */
	@FXML
	void handelBtnNext(ActionEvent event) {
		if (event.getSource() == btnNextQ) {
			q = (List<QuestionsAfterTest>) copy.getqArray();
			if (i == q.size() - 1) {
				if (!lastQ) {

					JOptionPane.showMessageDialog(null, "No more questions");
					lastQ = !lastQ;
				}
			} else {
				i++;
				if (i == q.size() - 1) {
					btnNextQ.setDisable(true);
					btnPrevQ.setDisable(false);
				} else
					btnPrevQ.setDisable(false);

				txtNunOfQ.setText((i + 1) + ")");
				txtQuestions.setText(q.get(i).getQuestion());
				txtaFewQ.setText((i + 1) + "/" + q.size());
				lblPoints.setText("Points: " + q.get(i).getPoint());
				txtAnswer1.setText(q.get(i).getAnswer1());
				txtAnswer2.setText(q.get(i).getAnswer2());
				txtAnswer3.setText(q.get(i).getAnswer3());
				txtAnswer4.setText(q.get(i).getAnswer4());
				String choiceStud = q.get(i).getAnswerStud();
				String currecet = q.get(i).getCorrectAnswer();
				resetColor();
				loadQ(choiceStud, currecet);
			}
		}
	}

	/**
	 * @param event
	 * Transfer to a previous question
	 */
	@FXML
	void handelBtnPrev(ActionEvent event) {
		if (event.getSource() == btnPrevQ) {
			q = (List<QuestionsAfterTest>) copy.getqArray();
			if (i == 0) {
				if (!firstQ) {
					JOptionPane.showMessageDialog(null, "This is a first question");
					firstQ = !firstQ;
				}
			} else {
				i--;
				if (i == 0) {
					btnPrevQ.setDisable(true);
					btnNextQ.setDisable(false);
				} else
					btnNextQ.setDisable(false);
				txtNunOfQ.setText((i + 1) + ")");
				txtQuestions.setText(q.get(i).getQuestion());
				txtaFewQ.setText((i + 1) + "/" + q.size());
				txtAnswer1.setText(q.get(i).getAnswer1());
				txtAnswer2.setText(q.get(i).getAnswer2());
				txtAnswer3.setText(q.get(i).getAnswer3());
				txtAnswer4.setText(q.get(i).getAnswer4());
				lblPoints.setText("Points: " + q.get(i).getPoint());
				String choiceStud = q.get(i).getAnswerStud();
				String currecet = q.get(i).getCorrectAnswer();
				resetColor();
				loadQ(choiceStud, currecet);
			}
		}
	}
	/**
	 *//**
     * Reset the correct and incorrect answer markers.
     */
	public void resetColor() {

		txtAnswer1.setStyle("-fx-background-color: white;");
		txtAnswer2.setStyle("-fx-background-color: white;");
		txtAnswer3.setStyle("-fx-background-color: white;");
		txtAnswer4.setStyle("-fx-background-color: white;");

	}

	private TestCopy copy;
	List<QuestionsAfterTest> q = new ArrayList<>();
	String testID;
	Person per;
	String StudID;
	CheckExamController checkExamController;

	
	/**
	 * @param checkExamController
	 * @param person
	 * @param testCopy
	 * @param studID
	 *  loads personal details
	 */
	public void loadDetails(CheckExamController checkExamController, Person person, TestCopy testCopy, String studID) {
		this.per = person;
		copy = testCopy;
		StudID = studID;
		btnPrevQ.setDisable(true);
		this.checkExamController = checkExamController;
		q = (List<QuestionsAfterTest>) testCopy.getqArray();
		testID = testCopy.getIdTest();
		titleScreen.setText("Test Approval\n");
		txtStudId.setText(studID);
		txtCoures.setText(testCopy.getCourse());
		txtGrade.setText(testCopy.getGrade());
		txtNunOfQ.setText("1)");
		txtQuestions.setText(q.get(0).getQuestion());
		txtaFewQ.setText("1/" + q.size());
		txtAnswer1.setText(q.get(0).getAnswer1());
		txtAnswer2.setText(q.get(0).getAnswer2());
		txtAnswer3.setText(q.get(0).getAnswer3());
		txtAnswer4.setText(q.get(0).getAnswer4());
		lblPoints.setText("Points: " + q.get(0).getPoint());
		txtNotes.setText(testCopy.getNotes());
		txtExplanation.setText(testCopy.getExplanation());
		String choiceStud = q.get(0).getAnswerStud();
		String currecet = q.get(0).getCorrectAnswer();
		ClientUI.chat.accept("GetNotes " + testID);
		ArrayList<String> arr = ChatClient.NotesArr;
		TeacherNotes.setText(arr.get(0));
		txtNotes.setText(arr.get(1));
		loadQ(choiceStud, currecet);
		btnPrevQ.setDisable(true);
		Cheatedlbl.setVisible(CheckMistakes());
	}

	/**
	 * @return
	 * Checks for copies and cheats on tests
	 */
	private boolean CheckMistakes() {
		ClientUI.chat.accept("CheckMistakes " + testID);
		ArrayList<CheckMistakes> arr = (ArrayList<CheckMistakes>) ChatClient.msgFromServer;
		int index = 0;
		int index2 = 0;
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getStudID().equals(StudID)) {
				index = i;
				index2 = i;
				break;
			}
		}
		ArrayList<String> correctAns = new ArrayList<>();
		ArrayList<String> StudAns = new ArrayList<>();
		int cnt = 0;
		int numOfQ = 0;
		for (int i = 0; i < arr.size(); i++) {
			CheckMistakes checkarr = arr.get(i);
			List<QuestionsAfterTest> list = checkarr.getqList();
			for (int j = 0; j < list.size(); j++) {
				correctAns.add(list.get(j).getCorrectAnswer());
				StudAns.add(list.get(j).getAnswerStud());
			}
			numOfQ = list.size();
		}
		index *= numOfQ;
		index2 *= numOfQ;
		int numOfStudMistake = 0;
		for (int i = 0; i < numOfQ * arr.size();) {
			if (!correctAns.get(index).equals(StudAns.get(index))) {
				numOfStudMistake++;
				if (i != index) {
					if (StudAns.get(index).equals(StudAns.get(i))) {
						cnt++;
					}
				} else {
					i += numOfQ;
					index--;
					numOfStudMistake--;
					i--;
				}
			}
			index++;
			i++;
			if (index % numOfQ == 0 && index != index2 && numOfStudMistake != 0) {
				if (cnt == numOfStudMistake) {
					return true;
				} else {
					index = index2;
					numOfStudMistake = 0;
					cnt = 0;
				}
			}
		}
		return false;
	}

	String[] str = { "1", "2", "3", "4" };

	/**
	 * @param choiceStud
	 * @param currecet
	 * loads the questions
	 */
	public void loadQ(String choiceStud, String currecet) {

		for (int j = 0; j < 4; j++) {
			if (!choiceStud.equals("-1")) {
				if (choiceStud.equals(str[j]) && choiceStud.equals(currecet)) {
					lblnoanswer.setVisible(false);
					if (j == 0) {
						txtAnswer1.setStyle("-fx-background-color: green;");
						Choice1.setSelected(true);
					} else if (j == 1) {
						txtAnswer2.setStyle("-fx-background-color: green;");
						Choice2.setSelected(true);
					} else if (j == 2) {
						txtAnswer3.setStyle("-fx-background-color: green;");
						Choice3.setSelected(true);
					} else {
						txtAnswer4.setStyle("-fx-background-color: green;");
						Choice4.setSelected(true);
					}
				} else if (choiceStud.equals(str[j]) && !(choiceStud.equals(currecet))) {
					lblnoanswer.setVisible(false);
					if (j == 0) {
						txtAnswer1.setStyle("-fx-background-color: red;");
						Choice1.setSelected(true);
					} else if (j == 1) {
						txtAnswer2.setStyle("-fx-background-color: red;");
						Choice2.setSelected(true);
					} else if (j == 2) {
						txtAnswer3.setStyle("-fx-background-color: red;");
						Choice3.setSelected(true);
					} else {
						txtAnswer4.setStyle("-fx-background-color: red;");
						Choice4.setSelected(true);
					}
				}

				if (!(choiceStud.equals(currecet))) {
					lblnoanswer.setVisible(false);
					if (currecet.equals(str[j])) {
						if (j == 0) {
							txtAnswer1.setStyle("-fx-background-color: green;");
							Choice1.setSelected(false);
						} else if (j == 1) {
							txtAnswer2.setStyle("-fx-background-color: green;");
							Choice2.setSelected(false);
						} else if (j == 2) {
							txtAnswer3.setStyle("-fx-background-color: green;");
							Choice3.setSelected(false);
						} else {
							txtAnswer4.setStyle("-fx-background-color: green;");
							Choice4.setSelected(false);
						}
					}
				}
			} else {
				lblnoanswer.setVisible(true);
				Choice1.setSelected(false);
				Choice2.setSelected(false);
				Choice3.setSelected(false);
				Choice4.setSelected(false);
				if (currecet.equals("1"))
					txtAnswer1.setStyle("-fx-background-color: green;");
				else if (currecet.equals("2"))
					txtAnswer2.setStyle("-fx-background-color: green;");
				else if (currecet.equals("3"))
					txtAnswer3.setStyle("-fx-background-color: green;");
				else if (currecet.equals("4"))
					txtAnswer4.setStyle("-fx-background-color: green;");

			}
		}
	}
	/**
	 *initialize the class variables
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Choice1.setDisable(true);
		Choice2.setDisable(true);
		Choice3.setDisable(true);
		Choice4.setDisable(true);
	}
}
