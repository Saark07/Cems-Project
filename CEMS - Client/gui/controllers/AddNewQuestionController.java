package controllers;

import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logic.ExamPreview;
import logic.Person;
/**
*
* @author Danit Doron
* In this class ,In the teacher screen, the teacher can create a new question.
*/
public class AddNewQuestionController {

	@FXML
	private Button btnExit;

	@FXML
	private TextArea QtextArea;

	@FXML
	private TextField Pointstxt;

	@FXML
	private Button Addbtn;

	@FXML
	private ComboBox<String> ComBoxQ;

	@FXML
	private Label Overallpts;
	EditExamController controller;
	Person per;
	int sum;
	String exID;

	/**
	 * @param event
	 * Add the points entered for a question
	 */
	@FXML
	void AddClicked(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (!Pointstxt.getText().isEmpty()) {
			if (Integer.parseInt(Pointstxt.getText()) + sum <= 100) {
				ExamPreview ex=new ExamPreview();
				ex.setID(QID);
				ex.setInstructions(QtextArea.getText());
				ex.setPoints(Pointstxt.getText());
				for(int i=0;i<controller.exprev.size();i++)
				{
					ExamPreview examcheck=controller.exprev.get(i);
					if(examcheck.getID().equals(QID))
					{
						alert.setTitle("Already exists");
						alert.setContentText("The question already exists in the exam");
						alert.showAndWait();
						return;
					}
				}
				controller.exprev.add(ex);
				sum+=Integer.parseInt(Pointstxt.getText());
				Overallpts.setText("Overall Points: "+sum);
				controller.refresh(sum);
				alert.setTitle("Question added");
				alert.setContentText("Question added successfully");
				alert.showAndWait();
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
			} else {
				alert.setTitle("Points");
				alert.setContentText("To many points, you have "+(100-sum)+" points left");
				alert.showAndWait();
			}
		} else {
			alert.setTitle("Points");
			alert.setContentText("No points entered.");
			alert.showAndWait();
		}
	}
	/**
	 * @param event
	 * Exit from the screen.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}

	String QID;
	/**
	 * @param event
	 * Allows the question to be displayed
	 */
	@FXML
	void ShowQuestion(ActionEvent event) {
		QtextArea.clear();
		if (ComBoxQ.getValue() != null) {
			String qnum = ComBoxQ.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept("GetQInstructions " + Integer.parseInt(qnum));
			String qdetails = ChatClient.Qinstruction;
			String[] st = qdetails.split(" ");
			String inst = "";
			QID = st[0];
			for (int i = 1; i < st.length - 1; i++)
				inst += st[i] + " ";
			inst += st[st.length - 1];
			QtextArea.setText("");
			QtextArea.setText(inst);
		}
	}

	ObservableList<String> SelectedCoursesID;
	/**
	 * @param editExamController
	 * @param per
	 * @param sum
	 * @param exid
	 * @param Course
	 * 
	 * Load all the question details 
	 */
	public void loadDetails(EditExamController editExamController, Person per, int sum, String exid, String Course) {
		controller = editExamController;
		this.per = per;
		this.sum = sum;
		this.exID = exid;
		Overallpts.setText("Overall Points :" + sum);
		ClientUI.chat.accept(
				"GetQIDByCourse " + Course.replace(" ", "_") + " " + per.getFirstName() + " " + per.getLastName());
		ArrayList<String> qst = ChatClient.QuestionsArrByCourse;
		SelectedCoursesID = FXCollections.observableArrayList(qst);
		ComBoxQ.setItems(SelectedCoursesID);

	}

}
