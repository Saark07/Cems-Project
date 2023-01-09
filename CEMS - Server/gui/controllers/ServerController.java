package controllers;

import java.io.IOException;

import Server.EchoServer;
import Server.ServerLog;
import Server.ServerUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;;
/**
 * @author Vladi
 * Server controller for making the connection with the data base
 *
 */
public class ServerController {
	public static ServerController instance;
	public static int port;
	private EchoServer echoServer;

	@FXML
	private TextArea txtTextArea;

	@FXML
	private Button btnStartServer;

	@FXML
	private Button btnExit;

	@FXML
	private TextField txtStatusColor;

	@FXML
	private Label txtStatus;

	@FXML
	void initialize() {
		ServerLog.setServerController(this);
	}
	/**
	 * @param event
	 * method for closing the connection with the database
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		try {
			echoServer.close();
		} catch (Exception e) {

			ServerLog.writeNewLine("Closing server failed.");
		}
		System.out.println("Bye Bye!!");
		System.exit(0);
	}

	/**
	 * @param event
	 * @throws IOException
	 * Method for starting the connection with the database
	 */
	@FXML
	void StartServerBtn(ActionEvent event) throws IOException {
		ServerUI.runServer(5555);//default connection to port 5555
		txtStatus.setText("ON");
		btnStartServer.setDisable(true);
		txtStatus.setStyle("-fx-text-fill: white;");
		txtStatusColor.setStyle("-fx-background-color:LIMEGREEN;\n" + "-fx-border-color: BLACK;\n" + "-fx-border-width:2");
	}
	/**
	 * @param primaryStage
	 * @throws Exception
	 * showing the screen when opening the server
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ServerWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Server");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * @param msg
	 * method for writing server messages to the log
	 */
	public void AddMsgToLog(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtTextArea.appendText(msg);
			}
		});

	}

}
