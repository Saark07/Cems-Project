package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import logic.DurationRequests;
import logic.Person;
/**
*
* @author Danit Doron
* In this class the principal can reach all the sub-screens and perform actions.
*
*/
public class PrincipalController {

	@FXML
	private Label lblWelcome;

	@FXML
	private Button btnTeacherStatistic;

	@FXML
	private Button btnStudentStatistics;

	@FXML
	private Button btnCourseStatistics;

	@FXML
	private TableView<DurationRequests> RequestsTableView;

	@FXML
	private TableColumn<DurationRequests, String> TestIDCol;

	@FXML
	private TableColumn<DurationRequests, String> TeacherNCol;

	@FXML
	private TableColumn<DurationRequests, String> CourseCol;

	@FXML
	private TableColumn<DurationRequests, String> OldDurCol;

	@FXML
	private TableColumn<DurationRequests, String> NewDurCol;

	@FXML
	private TableColumn<DurationRequests, String> ReasonCol;

	@FXML
	private Button Approvebtn;

	@FXML
	private Button Declinebtn;

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
	private Button btnUpdate;

	@FXML
	private Label lblNewMsg;

	@FXML
	private Circle popUpCircle;

	@FXML
	private Button btnExit;

	@FXML
	private Tab RequestsTab;
	/**
	 * @param event
	 * Selection of requests tab.
	 */
	@FXML
	void SelectRequestsTab(Event event) {
		if (RequestsTab.isSelected()) {
			RequestsTableView.getItems().clear();
			loadRequests();
		}
	}

	ObservableList<DurationRequests> ReqList;
	/**
	 * Shows all requests received from the teacher.
	 * 
	 */
	private void loadRequests() {
		ClientUI.chat.accept("GetDurationRequests");
		durReq2 = (ArrayList<DurationRequests>) ChatClient.msgFromServer;
		if (durReq2 != null) {
			for (int i = 0; i < durReq2.size(); i++) {
				DurationRequests dur = durReq2.get(i);
				ClientUI.chat.accept("GetFullNameByID " + dur.getTeacherID());
				String name = ChatClient.fullname;
				dur.setTeacherName(name.replace("_", " "));
				durReq2.set(i, dur);
			}
		} 
		TestIDCol.setCellValueFactory(new PropertyValueFactory<DurationRequests, String>("testID"));
		TeacherNCol.setCellValueFactory(new PropertyValueFactory<DurationRequests, String>("teacherName"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<DurationRequests, String>("Course"));
		OldDurCol.setCellValueFactory(new PropertyValueFactory<DurationRequests, String>("OldDuration"));
		NewDurCol.setCellValueFactory(new PropertyValueFactory<DurationRequests, String>("NewDuration"));
		ReasonCol.setCellValueFactory(new PropertyValueFactory<DurationRequests, String>("reason"));
		ReqList = FXCollections.observableArrayList(durReq2);
		RequestsTableView.getItems().setAll(ReqList);
	}
	/**
	 * @param event
	 * By clicking on this button the principal approves the selected request.
	 */
	@FXML
	void ApproveClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (RequestsTableView.getSelectionModel().getSelectedItem() != null) {
			DurationRequests dur = RequestsTableView.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept("ApproveRequest " + dur.getTestID());
			ClientUI.chat.accept("GetDurationRequests");
			durReq2.remove(dur);

			alert.setTitle("Approved");
			alert.setContentText("The request has been Approved");
			alert.showAndWait();
			ReqList.remove(dur);
			RequestsTableView.getItems().clear();
			RequestsTableView.getItems().setAll(ReqList);
		} else {
			alert.setTitle("Nothing selected");
			alert.setContentText("Must select row from the list");
			alert.showAndWait();
		}
	}
	/**
	 * @param event
	 * By clicking on this button the principal rejects the selected request.
	 */
	@FXML
	void Declinebtn(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (RequestsTableView.getSelectionModel().getSelectedItem() != null) {
			DurationRequests dur = RequestsTableView.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept("DeclineRequest " + dur.getTestID());
			ClientUI.chat.accept("GetDurationRequests");
			durReq2.remove(dur);
			alert.setTitle("Declined");
			alert.setContentText("The request has been declined");
			alert.showAndWait();
			ReqList.remove(dur);
			RequestsTableView.getItems().clear();
			RequestsTableView.getItems().setAll(ReqList);
		} else {
			alert.setTitle("Nothing selected");
			alert.setContentText("Must select row from the list");
			alert.showAndWait();
		} 
	}
	/**
	 * @param event
	 * @throws IOException
	 * hiding primary principal screen, and log out from the system.
	 */
	@FXML
	void LogOutClick(MouseEvent event) throws IOException {
		ClientUI.chat.accept("Logout " + per.getID());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setTitle("CEMS - Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 *  Exit and disconnect from the system.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		ClientUI.chat.accept("Logout " + per.getID());
		System.out.println("Bye Bye!!");
		System.exit(0);
	}

	private Person per;
	/**
	 * @param event
	 * @throws IOException
	 * A window will open where the principal can see all the static information about a particular course.
	 */
	@FXML
	void CourseStatisticsClick(ActionEvent event) throws IOException {
		ClientUI.chat.accept("GetDetails");
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/CourseStatisticsScreen.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Course Statistics");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 * @throws IOException
	 * A window will open where the principal can see all the static information about a particular student.
	 */
	@FXML
	void StudentStatisticsClick(ActionEvent event) throws IOException {
		ClientUI.chat.accept("GetDetails");
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/StudentStatisticsScreen.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Student Statistics");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param event
	 * @throws IOException
	 * A window will open where the principal can see all the static information about a particular teacher.
	 */
	@FXML
	void TeacherStatisticsClick(ActionEvent event) throws IOException {
		ClientUI.chat.accept("GetDetails");
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TeacherStatisticsScreen.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Teacher Statistics");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * @param event
	 * Load the personal details.
	 */
	@FXML
	void SelectMyProfile(Event event) {
		if (myProfileTab.isSelected())
			loadDetails(per, status);
	}
	/**
	 * initialize the class
	 */
	@FXML
	void initialize() {
		CheckNewReq();
		txtFirstName.setDisable(true);
		txtLastName.setDisable(true);
		lblNewMsg.setVisible(false);
		popUpCircle.setVisible(false);
	}

	ArrayList<DurationRequests> durReq2;
	/**
	 * Checking a new request received from the teacher
	 */
	private void CheckNewReq() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				ClientUI.chat.accept("GetDurationRequests");
				durReq2 = (ArrayList<DurationRequests>) ChatClient.msgFromServer;
				if (!durReq2.isEmpty()) {
					lblNewMsg.setVisible(true);
					popUpCircle.setVisible(true);
				} else {
					lblNewMsg.setVisible(false);
					popUpCircle.setVisible(false);
				}

			}
		};
		timer.schedule(timerTask, 0, 3000);
	}
	/**
	 * @param event
	 *  Update personal details.
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

	String status;
	/**
	 * @param person
	 * @param Status
	 * Load personal details.
	 */
	public void loadDetails(Person person, String Status) {
		per = person;
		status = Status;
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
	 * Verifies legality phone number.
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
