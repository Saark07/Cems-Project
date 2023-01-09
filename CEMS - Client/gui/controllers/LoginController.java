package controllers;

import java.io.IOException;
import java.net.URL;

import Client.ChatClient;
import Client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * @author  Danit Doron
 * 
 * This class is assigned to enter the user to the system.
 *
 */
public class LoginController {

	@FXML
	private TextField UserNameField;

	@FXML
	private TextField PasswordFieldTxt;

	@FXML
	private Button LoginBtn;

	@FXML
	private Button btnExit;

	@FXML
	private ImageView HideIMG;

	@FXML
	private ImageView ShowIMG;

	@FXML
	private PasswordField PasswordField;

	boolean flag = false;
	/**
	 * @param event
	 * Encrypts the password
	 */
	@FXML
	void ClickedShowIMG(MouseEvent event) {
		flag = true;
		PasswordFieldTxt.setText(PasswordField.getText());
		PasswordFieldTxt.setVisible(true);
		PasswordField.setVisible(false);
		ShowIMG.setVisible(false);
		HideIMG.setVisible(true);

	}
	/**
	 * @param event
	 * Encrypts the password
	 */
	@FXML
	void HideClickeIMG(MouseEvent event) {
		flag = false;
		PasswordField.setText(PasswordFieldTxt.getText());
		PasswordField.setVisible(true);
		PasswordFieldTxt.setVisible(false);
		ShowIMG.setVisible(true);
		HideIMG.setVisible(false);
	}
	/**
	 * initialize the class
	 */
	@FXML
	void initialize() {
		PasswordFieldTxt.setVisible(false);
		HideIMG.setVisible(false);
	}
	/**
	 * @param event
	 * @throws IOException
	 * The login window, in this method the system verifies that the user is no longer logged in,
	 *  and opens the appropriate screen according to the type of user.
	 * 
	 */
	@FXML
	void ClickLogin(ActionEvent event) throws IOException {
		if (!UserNameField.getText().isEmpty() && !PasswordField.getText().isEmpty()) {
			if (!flag)
				ClientUI.chat.accept("Login " + UserNameField.getText() + " " + PasswordField.getText());
			else
				ClientUI.chat.accept("Login " + UserNameField.getText() + " " + PasswordFieldTxt.getText());
			FXMLLoader loader = new FXMLLoader();
			String Permission = ChatClient.Permission;
			if (Permission.equals("Principal")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				URL location = getClass().getResource("/fxml/PrincipalScreen.fxml");
				loader.setLocation(location);
				Stage primaryStage = new Stage();
				Pane root = loader.load(location.openStream());
				PrincipalController principalController = loader.getController();
				principalController.loadDetails(ChatClient.person,"(Principal)");
				String id = ChatClient.person.getID();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Principal");
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we) {
						ClientUI.chat.accept("Logout " + id);
					}
				});
			} else if (Permission.equals("Teacher")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				URL location = getClass().getResource("/fxml/TeacherScreen.fxml");
				loader.setLocation(location);
				Stage primaryStage = new Stage();
				Pane root = loader.load(location.openStream());
				TeacherController teacherController = loader.getController();
				teacherController.loadDetails(ChatClient.person,"(Teacher)");
				String id = ChatClient.person.getID();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Teacher");
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we) {
						ClientUI.chat.accept("Logout " + id);
					}
				});

			} else if (Permission.equals("Student")) {
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				URL location = getClass().getResource("/fxml/StudentScreen.fxml");
				loader.setLocation(location);
				Stage primaryStage = new Stage();
				Pane root = loader.load(location.openStream());
				StudentController studentController = loader.getController();
				studentController.loadDetails(ChatClient.person,"(Student)");
				String id = ChatClient.person.getID();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Student");
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we) {
						ClientUI.chat.accept("Logout " + id);
					}
				});

			} else if (Permission.equals("not found")) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Validation Error");
						alert.setHeaderText(null);
						alert.setContentText("Wrong Username of Password.");
						alert.showAndWait();
					}
				});
			} else if (Permission.equals("alreadylogged"))
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection Error");
						alert.setHeaderText(null);
						alert.setContentText("User Allready Connected.");
						alert.showAndWait();
					}
				});
		} else
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Empty Fields");
					alert.setHeaderText(null);
					alert.setContentText("Please insert User name and Password.");
					alert.showAndWait();
				}
			});

	}
	/**
	 * @param event
	 * Close window.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		System.out.println("Bye Bye!!");
		System.exit(0);
	}
	/**
	 * @param primaryStage
	 * @throws Exception
	 * Loading  the gui screen of LoginScreen.

	 */
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS - Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
