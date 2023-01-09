package controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import logic.Person;
/**
 * @author doron
 * 
 * Exam Upload class by the teacher.
 *
 */
public class UploadExamController implements Initializable {

	@FXML
	private TextField DurationTxt;

	@FXML

	private Button btnExit;

	@FXML
	private ComboBox<String> ComBoxProf;

	@FXML
	private ComboBox<String> ComBoxCourse;

	@FXML
	private Button Browsebtn;

	@FXML
	private TextField FilePathtxt;

	@FXML
	private Button UploadExambtn;

	@FXML
	void ActiononClick(MouseEvent event) {

	}
	/**
	 * @param event
	 * Uploading an exam to the database.
	 */
	@FXML
	void UploadExamClick(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (ComBoxCourse.getValue() != null && ComBoxProf.getValue() != null && !DurationTxt.getText().isEmpty()) {
			String details;
			details = per.getFirstName() + "_" + per.getLastName() + " " + ComBoxProf.getValue().replace(" ", "_") + " "
					+ ComBoxCourse.getValue().replace(" ", "_") + " " + DurationTxt.getText() + " "
					+ filePath.replace(" ", "_")+" "+filename.replace(" ", "_");
			ClientUI.chat.accept("UploadExam " + details);
			alert.setTitle("Upload Success");
			alert.setContentText("Exam upload successfully");
			alert.showAndWait();
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
		} else {
			alert.setTitle("Empty");
			alert.setContentText("Must select Profession and Course\nAnd enter duration");
			alert.showAndWait(); 
		}
	}

	ObservableList<String> FList;

	/**
	 * set the profession to combobox
	 */
	private void setFacultiesComboBox() {
		ArrayList<String> facultieslist = ChatClient.TeacherProfArr;
		FList = FXCollections.observableArrayList(facultieslist);
		ComBoxProf.setItems(FList);
	}
	/**
	 * @param event
	 * Upload a manual exam file.
	 */
	@FXML
	void BrowseClick(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Word Files", lstFile));
		File f = fc.showOpenDialog(null);
		if (f != null)
		{
			FilePathtxt.setText(f.getAbsolutePath());
			
			filePath = f.getAbsolutePath();
			filename=f.getName();
		}
	}
String filename;
	String filePath;
	/**
	 * @param event
	 * Close screen.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();

	}

	ObservableList<String> CList;

	/**
	 * @param event
	 * load the courses by the profession
	 */
	@FXML
	void ShowSelected(ActionEvent event) {
		String prof = ComBoxProf.getSelectionModel().getSelectedItem();
		ClientUI.chat.accept("GetCourses " + prof);
		ArrayList<String> courses = ChatClient.CoursesArr;
		CList = FXCollections.observableArrayList(courses);
		ComBoxCourse.setItems(CList);
	}

	Person per;
	/**
	 * @param per
	 * loads person data to the screen
	 */
	public void loadDetails(Person per) {
		this.per = per;
		ClientUI.chat.accept("GetTeacherFaculties " + per.getID());
		setFacultiesComboBox();
	}

	List<String> lstFile;

	/**
	 *Allows you to upload only a docx file.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lstFile = new ArrayList<>();
		lstFile.add("*.doc");
		lstFile.add("*.docx");
		lstFile.add("*.DOC");
		lstFile.add("*.DOCX");
	}
}