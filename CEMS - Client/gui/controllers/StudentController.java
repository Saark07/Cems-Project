package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import Client.ChatClient;
import Client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ManualTest;
import logic.Person;
import logic.TestCopy;
import logic.TestResult;
import logic.TestToPerform;
/**
 * @author Danit Doron
 * In this class the student can reach all the sub-screens and perform actions.
 *
 */
public class StudentController implements Initializable {

	List<TestResult> arrTest;
	List<String> arrIdTest;

	ObservableList<TestResult> arrList = FXCollections.observableArrayList();

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label lblWelcome;

	@FXML
	private Label testcode;

	@FXML
	private Button btnTakeExam = null;

	@FXML
	private TableView<TestResult> TableViewResult;

	@FXML
	private TableColumn<TestResult, String> TestIdCol;

	@FXML
	private TableColumn<TestResult, String> ProfessionCol;

	@FXML
	private TableColumn<TestResult, String> CourseCol;

	@FXML
	private TableColumn<TestResult, String> GradeCol;

	@FXML
	private Button btnExit = null;

	@FXML
	private ComboBox<String> btnAllNumTest;

	@FXML
	private TextField txtTestIdGet;

	@FXML
	private TextField txtTestCode;

	@FXML
	private TextField txtStudentId;

	@FXML
	private Tab ExamResTab;

	@FXML
	private Label or;

	@FXML
	private Label testidLabel;

	@FXML
	private TextField txtTestId2;

	@FXML
	private Label searchBy;

	@FXML
	private Button btnGetExam = null;

	@FXML
	private Button btnSearchByTest;

	@FXML
	private Button btnSearchByStud;

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
	private Tab myProfileTab;

	private Person per;
	private String[] codeTest = new String[1000];
	private int index = 0;
	/**
	 * @param event
	 * Exit and disconnect from the system.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		ClientUI.chat.accept("Logout " + per.getID());
		System.out.println("Bye Bye!!");
		System.exit(0);
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
	 * @param event
	 * Tab selection exam result.
	 */
	@FXML
	void SelectedExamResTab(Event event) {
		if (ExamResTab.isSelected())
			loadTests();
	}
	/**
	 * Shows all the tests results that were approved.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void loadTests() {
		TableViewResult.getItems().clear();
		ClientUI.chat.accept("SearchByStudent " + per.getID());
		arrTest = (List<TestResult>) ChatClient.msgFromServer;
		if (arrTest != null) {
			for (int i = 0; i < arrTest.size(); i++) {
				arrList.add(arrTest.get(i));
			}
			TableViewResult.setItems(arrList);
		}

	}
	/**
	 * initialize the class
	 */
	@FXML
	void initialize() {
		txtFirstName.setDisable(true);
		txtLastName.setDisable(true);
	}

	@FXML
	private Button btnUpdate;
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
	 * @param event
	 * @throws IOException
	 * hiding primary student screen, and log out from the system.
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

	String status;
	/**
	 * @param person
	 * @param status
	 * Load personal details.
	 */
	public void loadDetails(Person person, String status) {
		per = person;
		txtAddress.setText(person.getAddress());
		txtEmail.setText(person.getEmail());
		txtPhone.setText(person.getPhone());
		txtFirstName.setText(person.getFirstName());
		txtLastName.setText(person.getLastName());
		lblWelcome.setText("Welcome " + person.getFirstName() + " " + person.getLastName() + " " + status);
		for (int i = 0; i < codeTest.length; i++) {
			codeTest[i] = "-1";
		}
		this.status = status;
	}

	/**
	 * initialize the the {@link TestResult} list columns table
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TestIdCol.setCellValueFactory(new PropertyValueFactory<TestResult, String>("IdTest"));
		ProfessionCol.setCellValueFactory(new PropertyValueFactory<TestResult, String>("Profession"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<TestResult, String>("Course"));
		GradeCol.setCellValueFactory(new PropertyValueFactory<TestResult, String>("Grade"));

		List<String> tList = new ArrayList<String>();
		for (int i = 0; i < arrList.size(); i++) {
			String s = arrList.get(i).getIdTest();
			tList.add(s);
		}
	}

	/**
	 * @param e
	 * @throws IOException
	 * Starting an exam.
	 * 
	 */
	@FXML
	public void handelBtnTake(ActionEvent e) throws IOException {// Starting an exam
		String code = txtTestCode.getText();
		TestToPerform testTo = null;
		ManualTest testM = null;
		FXMLLoader loader = new FXMLLoader();
		boolean flagTest = false;

		for (int i = 0; i < codeTest.length; i++) {
			if (codeTest[i].equals(code)) {
				flagTest = true;
			}
		}
		if (!flagTest) {
			codeTest[index++] = code;
		}

		if (e.getSource() == btnTakeExam) {
			int lenCode = txtTestCode.getText().length();
			if (lenCode != 4) {
				JOptionPane.showMessageDialog(null, "Fill in the details according to the instructions");
			} else {
				ClientUI.chat.accept("TakeExam " + code);
				testTo = (TestToPerform) ChatClient.msgFromServer;

				if (testTo != null) {
					if (flagTest) {
						JOptionPane.showMessageDialog(null, "The test is already open");
					} else {
						// openTest=!openTest;
						URL location = getClass().getResource("/fxml/EnterIDnumScreen.fxml");
						loader.setLocation(location);
						Stage primaryStage = new Stage();
						Pane root = loader.load(location.openStream());
						EnterIDnumController enterId = loader.getController();
						enterId.loadDetails(ChatClient.person, testTo);
						Scene scene = new Scene(root);
						primaryStage.setTitle("Test");
						primaryStage.setScene(scene);
						primaryStage.show();
					}
				} else {
					if (flagTest) {
						JOptionPane.showMessageDialog(null, "The test is already open");
					} else {
						// openTest=!openTest;
						ClientUI.chat.accept("TakeManualExam " + code);
						testM = (ManualTest) ChatClient.msgFromServer;
						if (testM == null) {
							JOptionPane.showMessageDialog(null, "There is an error in the code");
						} else {
							URL location = getClass().getResource("/fxml/TestManualScreen.fxml");
							loader.setLocation(location);
							Stage primaryStage = new Stage();
							Pane root = loader.load(location.openStream());
							TestManualController testManual = loader.getController();
							testManual.loadDetails(ChatClient.person, testM,txtTestCode.getText());
							Scene scene = new Scene(root);
							primaryStage.setTitle("Test");
							primaryStage.setScene(scene);
							primaryStage.show();
						}
					}
				}
			}
		}
	}
	/**
	 * @param e
	 * @throws IOException
	 * Get test copy.
	 */
	@FXML
	public void handelBtnGet(ActionEvent e) throws IOException {// testCopy
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (TableViewResult.getSelectionModel().getSelectedItem() != null) {
			TestResult testres = (TestResult) TableViewResult.getSelectionModel().getSelectedItem();
			String testCopyId = testres.getIdTest();
			TestCopy testCopy = null;
			FXMLLoader loader = new FXMLLoader();
			if (e.getSource() == btnGetExam) {
				if (testCopyId.length() != 6) {
					JOptionPane.showMessageDialog(null, "Please enter 6 digits");
				} else {
					ClientUI.chat.accept("GetExam " + testCopyId + " " + per.getID() + " 1");
					testCopy = (TestCopy) ChatClient.msgFromServer;
					if (testCopy != null) {
						URL location = getClass().getResource("/fxml/TestCopy.fxml");
						loader.setLocation(location);
						Stage primaryStage = new Stage();
						Pane root = loader.load(location.openStream());
						TestCopyController copy = loader.getController();
						copy.loadDetails(ChatClient.person, testCopy);//
						Scene scene = new Scene(root);
						primaryStage.setTitle("TestCopy");
						primaryStage.setScene(scene);
						primaryStage.show();
					} else {
						JOptionPane.showMessageDialog(null, "The test does not exist");
					}
				}
			}
		}else {
			alert.setTitle("Empty selection");
			alert.setContentText("Select row from the table");
			alert.showAndWait();
		}
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
					Alert alert = new Alert(AlertType.ERROR);
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
					Alert alert = new Alert(AlertType.ERROR);
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
