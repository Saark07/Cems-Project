package controllers;

import java.util.ArrayList;
import java.util.Optional;

import Client.ChatClient;
import Client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.Person;
import logic.Test;

/**
 * @author Saar
 *this class shows the exams that are available to be taken right now
 */
public class LiveExamsController {

	@FXML
	private TextArea txtReason;

	@FXML
	private Button btnExit;

	@FXML
	private TableView<Test> tblViewExams;

	@FXML
	private TableColumn<Test, String> TestIDCol;

	@FXML
	private TableColumn<Test, String> ProfCol;

	@FXML
	private TableColumn<Test, String> CourseCol;

	@FXML
	private TableColumn<Test, String> DurCol;

	@FXML
	private TableColumn<Test, String> CodeCol;

	@FXML
	private Button LockExambtn;

	@FXML
	private TextField txtDuration;

	@FXML
	private Button SendReqbtn;
	/**
	 * @param event
	 * Close window.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * @param event
	 * this method lock the exam of specific exam
	 */
	@FXML
	void LockExamClick(ActionEvent event) {
		Alert a2 = new Alert(AlertType.INFORMATION);
		a2.setHeaderText(null);
		if (tblViewExams.getSelectionModel().getSelectedItem() != null) {
			Alert a1 = new Alert(AlertType.WARNING, " ", ButtonType.NO, ButtonType.YES);
			a1.setHeaderText(null);
			a1.setContentText("If you click yes, all existing tests in progress will be locked\nAre You Sure?");
			Optional<ButtonType> res = a1.showAndWait();
			if (res.get() == ButtonType.YES) {

				Test t = tblViewExams.getSelectionModel().getSelectedItem(); 
				ClientUI.chat.accept("LockExam " + t.getID());
				TestsList.remove(t);
				tblViewExams.setItems(null);
				tblViewExams.setItems(TestsList);
			}
		} else {
			a2.setTitle("Empty selection");
			a2.setContentText("Must select a row from the table");
			a2.showAndWait();
		}

	}

	/**
	 * @param event
	 * this method send request to the principal for duration extend
	 */
	@FXML
	void SendReqClick(ActionEvent event) {
		Alert a2 = new Alert(AlertType.INFORMATION);
		a2.setHeaderText(null);
		if (tblViewExams.getSelectionModel().getSelectedItem() != null) {
			Test t = tblViewExams.getSelectionModel().getSelectedItem();
			if (!txtDuration.getText().isEmpty() && !txtReason.getText().isEmpty()) {
				if (Integer.parseInt(txtDuration.getText()) > t.getDuration()) {
					ClientUI.chat.accept("SendRequest " + t.getID() + " " + per.getID() + " "
							+ t.getCourse().replace(" ", "_") + " " + t.getDuration() + " "
							+ Integer.parseInt(txtDuration.getText()) + " " + txtReason.getText().replace(" ", "_"));
					txtDuration.setText("");
					txtReason.setText("");
					a2.setTitle("Request has been sent");
					a2.setContentText("The request sent to the principal for approval or decline");
					a2.showAndWait();
				} else
					{
					a2.setTitle("Wrong Duration");
					a2.setContentText("Duration must be higher than the old duration");
					a2.showAndWait();
					}
			} else
				{
				a2.setTitle("Empty Duration");
				a2.setContentText("Must enter duration and reason");
				a2.showAndWait();
				}
			

		} else
			{			a2.setTitle("Empty selection");
			a2.setContentText("Must select a row from the table");
			a2.showAndWait();}
		
	}

	Person per;

	/**
	 * @param per
	 * this method load details for the live exams screen
	 */
	@SuppressWarnings("unchecked")
	public void loadDetails(Person per) {
		this.per = per;
		ClientUI.chat.accept("GetLiveExams " + per.getFirstName() + "_" + per.getLastName());
		ArrayList<Test> tests = (ArrayList<Test>) ChatClient.msgFromServer;
		TestsList = FXCollections.observableArrayList(tests);
		TestIDCol.setCellValueFactory(new PropertyValueFactory<Test, String>("ID"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Course"));
		ProfCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Profession"));
		DurCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Duration"));
		CodeCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Code"));
		tblViewExams.setItems(TestsList);
	}

	ObservableList<Test> TestsList;

}
