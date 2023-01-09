package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.FinishedTest;
import logic.Person;
import logic.TestCopy;
/**
 * @author Vladi
 * controller for checking finished exams
 *
 */
public class CheckExamController {

	@FXML
	private Button btnExit;

	@FXML
	private TableView<FinishedTest> tblViewExams;

	@FXML
	private TableColumn<FinishedTest, String> StudIDCol;

	@FXML
	private TableColumn<FinishedTest, String> ExamIDCol;

	@FXML
	private TableColumn<FinishedTest, String> ProfCol;

	@FXML
	private TableColumn<FinishedTest, String> CourseCol;

	@FXML
	private TableColumn<FinishedTest, String> GradeCol;

	@FXML
	private Button OpenExambtn;
	/**
	 * @param event
	 * handling exit the current window action
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}
	/**
	 * @param event
	 * @throws IOException
	 * choosing the wanted exam and opening the screen for approving the grade
	 */
	@FXML
	void OpenExamClick(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (tblViewExams.getSelectionModel().getSelectedItem() != null) {
			FinishedTest testres = (FinishedTest) tblViewExams.getSelectionModel().getSelectedItem();
			String testCopyId = testres.getExamID();
			TestCopy testCopy = null;
			FXMLLoader loader = new FXMLLoader();
			if (event.getSource() == OpenExambtn) {
				ClientUI.chat.accept("GetExam " + testCopyId + " " + testres.getStudentID() + " 0");
				testCopy = (TestCopy) ChatClient.msgFromServer;
				if (testCopy != null) {
					URL location = getClass().getResource("/fxml/ApprovExamScreen.fxml");
					loader.setLocation(location);
					Stage primaryStage = new Stage();
					Pane root = loader.load(location.openStream());
					TestExamApproveController copy = loader.getController();
					copy.loadDetails(this,ChatClient.person, testCopy,testres.getStudentID());//
					Scene scene = new Scene(root);
					primaryStage.setTitle("Approve Exam Screen");
					primaryStage.setScene(scene);
					primaryStage.show();
				}
			}
		} else {
			alert.setTitle("Empty selection");
			alert.setContentText("Select row from the table");
			alert.showAndWait();
		}
	}

	Person per;
	ObservableList<FinishedTest> TestsList;

	/**
	 * @param per
	 * loading the details for another controller
	 */
	@SuppressWarnings("unchecked")
	public void loadDetails(Person per) {
		this.per = per;
		ClientUI.chat.accept("GetFinishedExams " + per.getID());
		StudIDCol.setCellValueFactory(new PropertyValueFactory<FinishedTest, String>("studentID"));
		ExamIDCol.setCellValueFactory(new PropertyValueFactory<FinishedTest, String>("examID"));
		ProfCol.setCellValueFactory(new PropertyValueFactory<FinishedTest, String>("proffession"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<FinishedTest, String>("course"));
		GradeCol.setCellValueFactory(new PropertyValueFactory<FinishedTest, String>("grade"));
		if (ChatClient.msgFromServer != null) {
			ArrayList<FinishedTest> arr = (ArrayList<FinishedTest>) ChatClient.msgFromServer;
			TestsList = FXCollections.observableArrayList(arr);
			tblViewExams.getItems().setAll(TestsList);
		}

	}
	/**
	 * refreshing the current screen
	 */
	public void refresh() {
		tblViewExams.getItems().clear();
		tblViewExams.getItems().setAll(TestsList);
		
	}

}
