package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.ChatClient;
import Client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Person;
import logic.Question;

/**
 * @author Saar
 *this class used to edit and existing controller
 */
public class EditQuestionController implements Initializable {

	@FXML
	private Button btnExit;

	@FXML
	private TextArea FillQuestion;

	@FXML
	private TextField AanswrFld;

	@FXML
	private TextField BanswrFld;

	@FXML
	private TextField CanswrFld;

	@FXML
	private TextField DanswrFld;

	@FXML
	private Button btnUpdateQuestion;

	@FXML
	private ComboBox<String> ComBoxProf;

	@FXML
	private ComboBox<String> ComBoxChoise;

	@FXML
	private ComboBox<String> ComBoxQID;

	@FXML
	private Button btnShowQuestion;

	@FXML
	private Button btnDeleteQuestion;

	/**
	 * @param event
	 * this method delete the selected question from the DB
	 */
	@FXML
	void DeleteClick(ActionEvent event) {
		if (ComBoxQID.getValue() != null) {
			ClientUI.chat.accept("DeleteQuestion " + ComBoxQID.getValue());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Deleted");
			alert.setContentText("The question Deleted succeeded!");
			alert.showAndWait();
			((Node) event.getSource()).getScene().getWindow().hide();
		}

	}

	/**
	 * @param event
	 * Close the window
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}

	String prevProf;
	String prevQID;

	/**
	 * @param event
	 * this method show the question detail
	 */
	@FXML
	void ShowQuestionClick(ActionEvent event) {
		if (ComBoxQID.getValue() != null) {
			ClientUI.chat.accept("GetQdetails " + ComBoxQID.getValue());
			int idx;
			Question qst = ChatClient.qst;
			String[] arr = qst.getAnswers();
			FillQuestion.setText(qst.getInstructions());
			AanswrFld.setText(arr[0]);
			BanswrFld.setText(arr[1]);
			CanswrFld.setText(arr[2]);
			DanswrFld.setText(arr[3]);

			if (qst.getCorrectAnswer().equals("1"))
				idx = 0;

			else if (qst.getCorrectAnswer().equals("2"))
				idx = 1;

			else if (qst.getCorrectAnswer().equals("3"))
				idx = 2;

			else
				idx = 3;

			prevProf = ComBoxProf.getValue();// save the previous prof value so it cant be changed
			prevQID = ComBoxQID.getValue();
			ComBoxChoise.getSelectionModel().select(idx);
		}

	}

	ObservableList<String> SelectedCoursesID;

	/**
	 * @param event
	 * this method shows the QID of specific course and load the details
	 */
	@FXML
	void ShowSelected(ActionEvent event) {
		String prof = ComBoxProf.getSelectionModel().getSelectedItem();
		ClientUI.chat.accept("GetQID " + prof.replace(" ", "_")+" "+per.getFirstName()+" "+per.getLastName());
		ArrayList<String> qst = ChatClient.QuestionsArr;
		SelectedCoursesID = FXCollections.observableArrayList(qst);
		ComBoxQID.setItems(SelectedCoursesID);
		FillQuestion.clear();
		AanswrFld.clear();
		BanswrFld.clear();
		CanswrFld.clear();
		DanswrFld.clear();
		ComBoxChoise.getSelectionModel().clearSelection();
	}

	/**
	 * @param event
	 * this method clear clear the selections
	 */
	@FXML
	void ShowSelectedID(ActionEvent event) {
		FillQuestion.clear();
		AanswrFld.clear();
		BanswrFld.clear();
		CanswrFld.clear();
		DanswrFld.clear();
		ComBoxChoise.getSelectionModel().clearSelection();
	}

	/**
	 * @param event
	 * this method update an existing question from the DB
	 */
	@FXML
	void UpdateClick(ActionEvent event) {
		int count = 0;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText("At least one of the fields is empty.\nPlease fill in all the fields");
		// missing Question description
		if (FillQuestion.getText().trim().length() == 0) {
			count++;
		}

		if (AanswrFld.getText().trim().isEmpty()) {
			count++;

		}

		if (BanswrFld.getText().trim().isEmpty()) {
			count++;

		}
		if (CanswrFld.getText().trim().isEmpty()) {
			count++;
		}

		if (DanswrFld.getText().trim().isEmpty()) {
			count++;
		}
		if (count > 0)
			alert.showAndWait();
		else {
			String updateString;
			updateString = FillQuestion.getText().replace(" ", "_") + " " + AanswrFld.getText().replace(" ", "_") + " " + BanswrFld.getText().replace(" ", "_")  + " "
					+ CanswrFld.getText().replace(" ", "_")  + " " + DanswrFld.getText().replace(" ", "_")  + " "
					+ ComBoxChoise.getSelectionModel().getSelectedItem() + " " + prevQID;
			ClientUI.chat.accept("UpdateQuestion " + updateString);
			alert.setTitle("Updated");
			alert.setContentText("The question Update succeeded!");
			alert.showAndWait();
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
		}
	}

	/**
	 * this method set the choice for the answers 1,2,3,4
	 */
	private void setChoiseComboBox() {
		ObservableList<String> CList;
		ArrayList<String> ChoiseList = new ArrayList<>();
		ChoiseList.add("1");
		ChoiseList.add("2");
		ChoiseList.add("3");
		ChoiseList.add("4");
		CList = FXCollections.observableArrayList(ChoiseList);
		ComBoxChoise.setItems(CList);
	}

	ObservableList<String> FList;

	/**
	 * this method set the combo box with the right faculties
	 */
	private void setFacultiesComboBox() {
		ArrayList<String> facultieslist = ChatClient.TeacherProfArr;
		FList = FXCollections.observableArrayList(facultieslist);
		ComBoxProf.setItems(FList);
	}

	Person per;
	/**
	 * @param per
	 * this method load the details of person
	 */
	public void loadDetails(Person per) {
		this.per = per;
		ClientUI.chat.accept("GetTeacherFaculties " + per.getID());
		setFacultiesComboBox();
	}

	@FXML
	void ActiononClick(MouseEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setChoiseComboBox();

	}

}
