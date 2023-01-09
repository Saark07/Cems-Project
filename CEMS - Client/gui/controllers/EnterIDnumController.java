package controllers;


import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import Client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Person;
import logic.TestToPerform;

/**
 * @author Saar
 *this class used for check if the student insert the correct id for the exam
 */
public class EnterIDnumController {

	
	
    @FXML
    private Button btnSendId;

    @FXML
    private TextField txtEnterId;

    @FXML
    private Label labelEnterId;

    @FXML
    private Label labelDearStud;

    private Person per;
    private TestToPerform testTo;
    
    /**
     * @param person
     * @param test
     * load personal details for the student and the test to perform
     */
    public void loadDetails(Person person, TestToPerform test) {
		per=person;
		testTo=test;
	}
    
    /**
     * @param e
     * @throws IOException
     * this method check if the id is equal and open the test controller if it does
     */
    @FXML
    public void handelBtnSend(ActionEvent e) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	
    	if(e.getSource()==btnSendId) {
    		String idNum=txtEnterId.getText();
    		if(idNum.equals(per.getID())) {
    			((Node) e.getSource()).getScene().getWindow().hide();
    			//StudentController idOk=loader.getController();
    			URL location = getClass().getResource("/fxml/TestControllerScreen.fxml");
				loader.setLocation(location);
				Stage primaryStage = new Stage();
				Pane root = loader.load(location.openStream());
				TestController testController = loader.getController();
				testController.loadDetails(ChatClient.person, testTo, primaryStage);
				Scene scene = new Scene(root);
				primaryStage.setTitle("Test");
				primaryStage.setScene(scene);
				primaryStage.show();
    		}
    		else {
    			JOptionPane.showMessageDialog(null, "There is an error in the ID, please re-enter");
    			txtEnterId.setText(" ");
			}
    	}
    }
}
