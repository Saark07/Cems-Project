package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Client.ChatClient;
import Client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.Person;
import logic.Test;
/**
 * @author Danit Doron
 * In this class the teacher enters a code for starting an exam.
 *
 */
public class ApplyCodeController implements Initializable{

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
    private Button SetCodebtn;

    @FXML
    private TextField txtCode;
    /**
     * @param event
     * hiding primary Apply Code screen.
     */
    @FXML
    void ExitBtn(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    }
    /**
     * @param event
     * Setting the code that was entered by the teacher.
     */
    @FXML
    void SetCodeClick(ActionEvent event) {
    	Alert alert =new Alert(AlertType.INFORMATION);
    	alert.setHeaderText(null);
    	if(ValidateCode())
    	{
    		if(tblViewExams.getSelectionModel().getSelectedItem()!=null) {
    		Test t=tblViewExams.getSelectionModel().getSelectedItem();
    		ClientUI.chat.accept("SetCodeForExam "+ txtCode.getText()+" "+t.getID());
    		TestsList.remove(t);
    		tblViewExams.setItems(null);
    		tblViewExams.setItems(TestsList);
    		alert.setTitle("Apply succeeded");
    		alert.setContentText("Exam now Available to be taken");
    		alert.showAndWait();
    		}
    	}
    	else {
    		alert.setTitle("Nothing selected");
    		alert.setContentText("Select test from the table");
    		alert.showAndWait();
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	ObservableList<Test> TestsList;
	Person per;
	/**
	 * @param per
	 * loads the exam details 
	 */
	@SuppressWarnings("unchecked")
	public void loadDetails(Person per) {
		this.per=per;
		ClientUI.chat.accept("GetExamsToApply "+ per.getFirstName()+"_"+per.getLastName());
		ArrayList<Test> tests=(ArrayList<Test>) ChatClient.msgFromServer;
		TestsList=FXCollections.observableArrayList(tests);
		TestIDCol.setCellValueFactory(new PropertyValueFactory<Test, String>("ID"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Course"));
		ProfCol.setCellValueFactory(new PropertyValueFactory<Test, String>("Profession"));
		DurCol.setCellValueFactory(new PropertyValueFactory<Test,String>("Duration"));
		tblViewExams.setItems(TestsList);
	}
	/**
	 * @return
	 * Verifies legality code number.
	 */
	private boolean ValidateCode() {
		Pattern p = Pattern.compile("[a-zA-Z0-9]{4}");
		Matcher m = p.matcher(txtCode.getText());
		if (m.find() && m.group().equals(txtCode.getText()))
			return true;
		else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Validation Error");
					alert.setHeaderText(null);
					alert.setContentText("Invalid Code!!.");
					alert.showAndWait();
				}
			});
			return false;
		}
	}
}
