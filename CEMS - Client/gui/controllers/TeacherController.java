package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Client.ChatClient;
import Client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Person;
import logic.Test;
/**
 * @author Danit Doron
 * In this class the teacher can reach all the sub-screens and perform actions.
 *
 */
public class TeacherController {

	@FXML
	private Button CreateExambtn;

	@FXML
	private Button EditExambtn;

	@FXML
	private Button UploadExambtn;

	@FXML
	private Label lblWelcome;

	@FXML
	private Button CrtQstBtn;

	@FXML
	private Button EditQstBtn;

	@FXML
	private TableView<Test> tblViewExams;

	@FXML
	private TableColumn<Test, String> TestIdCol;

	@FXML
	private Tab StatisticsTab;

	@FXML
	private TableColumn<Test, String> ProfCol;

	@FXML
	private TableColumn<Test, String> CourseCol;

	@FXML
	private Button btnShowStats;

	@FXML
	private Tab myProfileTab;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtAddress;
	
    @FXML
    private Button LiveExambtn;

    @FXML
    private Button CheckExambtn;


    @FXML
    private Button ApplyCodebtn;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnExit;
	/**
	 * @param event
	 * @throws IOException
	 * Open the create exam window.
	 */
	@FXML
	void CreateExamClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/CreateExamScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		CreateExamController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root); 
		primaryStage.setTitle("Create Exam");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 * @throws IOException
	 * Open the edit exam window.
	 */
	@FXML
	void EditExamClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/EditExamScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		EditExamController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Edit Exam");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 * Tab selection statistic data.
	 */
	@FXML
	void SelectedStatisticsTab(Event event) {
		if (StatisticsTab.isSelected()) {
			tblViewExams.getItems().clear();
			loadTests();
		}
	}
	/**
	 * @param event
	 * @throws IOException
	 * Shows all the exams that the teacher wrote,
	 * The teacher can select a specific test and see his statistics data.
	 */
	@FXML
	void ShowStatisticClick(ActionEvent event) throws IOException {
		if (tblViewExams.getSelectionModel().getSelectedItem() != null) {
			Test test = tblViewExams.getSelectionModel().getSelectedItem();
			FXMLLoader loader = new FXMLLoader();
			URL location = getClass().getResource("/fxml/TeacherStatisticsExamScreen.fxml");
			loader.setLocation(location);
			Stage primaryStage = new Stage();
			Pane root = loader.load(location.openStream());
			TeacherStatisticsExamController controller = loader.getController();
			controller.loadStatistics(test, per.getID());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Exam Statistics");
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Empty selection");
			alert.setHeaderText(null);
			alert.setContentText("Please select row from the table");
		}
	}
	/**
	 * Load all the exams that the teacher wrote and approved.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void loadTests() {
		ClientUI.chat.accept("GetTeacherTests " + per.getFirstName() + " " + per.getLastName());
		TestIdCol.setCellValueFactory(new PropertyValueFactory<Test, String>("ID"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Course"));
		ProfCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Profession"));
		if (ChatClient.msgFromServer != null) {
			ArrayList<Test> arr=(ArrayList<Test>) ChatClient.msgFromServer;
			TestsList = FXCollections.observableArrayList(arr);
			tblViewExams.getItems().setAll(TestsList);
		}
	}

	ObservableList<Test> TestsList;
	/**
	 * @param event
	 * @throws IOException
	 * Open the edit question screen.
	 */
	@FXML
	void EditQuestionClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/EditQuestion.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		EditQuestionController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Edit Question");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * @param event
	 * @throws IOException
	 * Open the create new question screen.
	 */
	@FXML
	void CreateQuestion(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/CreateQuestionScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		CreateQuestionController controller = loader.getController();
		controller.loadDetails(per); 
		Scene scene = new Scene(root);
		primaryStage.setTitle("Create Question");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 * @throws IOException
	 * Upload the manual exam(file). 
	 */
	@FXML
	void UploadExamClick(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/UploadExamScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		UploadExamController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Upload Exam");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 * Close and disconnect from the system.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		ClientUI.chat.accept("Logout " + per.getID());
		System.out.println("Bye Bye!!");
		System.exit(0);
	}
	/**
	 * @param event
	 * Update personal details.
	 */
	@FXML
	void UpdateClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (ValidatePhoneNumber() && ValidateEmail()) {
			if (!txtAddress.getText().isEmpty()) {
				String address = txtAddress.getText().replace(" ", "_");
				ClientUI.chat.accept(
						"Update " + per.getID() + " " + txtPhone.getText() + " " + txtEmail.getText() + " " + address);
				loadDetails(ChatClient.person, status);
				alert.setTitle("Updated");
				alert.setContentText("Details Updated Succeed.");
				alert.showAndWait();
			} else {
				alert.setTitle("Empty Address");
				alert.setContentText("Address cant be empty.");
				alert.showAndWait();
			}
		}
	}
	/**
	 * Preventing a change of name and last name.
	 */
	@FXML
	void initialize() {
		txtFirstName.setDisable(true);
		txtLastName.setDisable(true);
	}

	private Person per;
	/**
	 * @param event
	 *  Load personal details
	 * 
	 */
	@FXML
	void SelectMyProfile(Event event) {
		if (myProfileTab.isSelected())
			loadDetails(per, status);
	}
	/**
	 * @param event
	 * @throws IOException
	 * hiding primary teacher screen, and log out from the system.
	 */
	@FXML
	void LogOutClick(MouseEvent event) throws IOException {
		ClientUI.chat.accept("Logout " + per.getID());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setTitle("CEMS - Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	 /**
     * @param event
     * @throws IOException
     * Opens a window to view all live exams.
     */
    @FXML
    void LiveExamClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/LiveExamsScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		LiveExamsController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Live Exam");
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    /**
     * @param event
     * @throws IOException
     *Opens a screen where all the teacher's exams appear, 
     *allows enter a new code for a specific exam.
     */
    @FXML
    void ApplyCodeClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/ApplyCodeScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		ApplyCodeController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Apply Code");
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    /**
     * @param event
     * @throws IOException
     * Opens a screen that shows all the exams that need to be approved for grading.
     */
    @FXML
    void CheckExamClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("/fxml/CheckExamsScreen.fxml");
		loader.setLocation(location);
		Stage primaryStage = new Stage();
		Pane root = loader.load(location.openStream());
		CheckExamController controller = loader.getController();
		controller.loadDetails(per);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Check Exam");
		primaryStage.setScene(scene);
		primaryStage.show();
    }


	String status;

	/**
	 * @param person
	 * @param Status
	 * Load the personal details.
	 */
	public void loadDetails(Person person, String Status) {
		status = Status;
		per = person;
		txtAddress.setText(person.getAddress());
		txtEmail.setText(person.getEmail());
		txtPhone.setText(person.getPhone());
		txtFirstName.setText(person.getFirstName());
		txtLastName.setText(person.getLastName());
		lblWelcome.setText("Welcome " + person.getFirstName() + " " + person.getLastName() + " " + Status);
	}

	/*--------------------------------------------------------------------
	 *                   		Validation
	----------------------------------------------------------------------*/
	/**
	 * @return
	 * Verifies legality of an email address.
	 */
	private boolean ValidatePhoneNumber() {
		Pattern p = Pattern.compile("[0][5][0-9]{8}");
		Matcher m = p.matcher(txtPhone.getText());
		if (m.find() && m.group().equals(txtPhone.getText()))
			return true;
		else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Validation Error");
					alert.setHeaderText(null);
					alert.setContentText(
							"Invalid Phone Number.\nPhone must have:\n*10 numbers\n*No letters\n*Start with 05");
					alert.showAndWait();
				}
			});
			return false;
		}

	}
	/**
	 * @return
	 * Verifies legality of an email address.
	 */
	private boolean ValidateEmail() {
		Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-z0-9]+([.][a-zA-Z]+)+");
		Matcher m = p.matcher(txtEmail.getText());
		if (m.find() && m.group().equals(txtEmail.getText()))
			return true;
		else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Validation Error");
					alert.setHeaderText(null);
					alert.setContentText("Invalid Email!!.");
					alert.showAndWait();
				}
			});
			return false;
		}
	}
	

}
