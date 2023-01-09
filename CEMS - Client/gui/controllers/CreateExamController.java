package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.ChatClient;
import Client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ExamPreview;
import logic.Person;
/**
 * @author doron
 * 
 * Class of exam creation by the teacher.
 *
 */
public class CreateExamController implements Initializable {

	@FXML
	private Button btnExit;

	@FXML
	private TextField Durationtxtfield;

	@FXML
	private Button Createbtn;

	@FXML
	private TextArea TeacherNtxtArea;

	@FXML
	private TextArea QtextArea;

	@FXML
	private TextField Pointstxt;

	@FXML
	private Button Addbtn;

	@FXML
	private Button ExamSumbtn;

	@FXML
	private Label Overallpts;

	@FXML
	private ComboBox<String> ComBoxProf;

	@FXML
	private ComboBox<String> ComBoxCourse;

	@FXML
	private ComboBox<String> ComBoxQ;

	@FXML
	private TextArea StudnetNTxtArea;

	ArrayList<String> points;
	ArrayList<String> Id;
	ArrayList<ExamPreview> exprev;
	/**
	 * @param event
	 * Adds  question to the exam.
	 */
	@FXML
	void AddClicked(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (!Pointstxt.getText().isEmpty() && ComBoxProf.getValue() != null && ComBoxCourse.getValue() != null
				&& ComBoxQ.getValue() != null) { 
			if (CheckPoints(points) + Integer.parseInt(Pointstxt.getText()) <= 100) {
				if (!Id.contains(ID)) {
					ExamPreview ex=new ExamPreview();
					Id.add(ID);
					points.add(Pointstxt.getText());
					ex.setID(ID);
					ex.setInstructions(QtextArea.getText());
					ex.setPoints(Pointstxt.getText());
					exprev.add(ex);
					Pointstxt.setText("");
					Overallpts.setVisible(true);
					Overallpts.setText("Overall Points: " + CheckPoints(points));
					QtextArea.setText("");
					alert.setTitle("Question Added");
					alert.setContentText("Question added successfully");
					alert.showAndWait();
				} else {
					alert.setTitle("Question exists");
					alert.setContentText("Question already exists in exam");
					alert.showAndWait();
				}
			} else {
				alert.setTitle("Points");
				alert.setContentText("To many points, You have " + (100 - CheckPoints(points)) + " left");
				alert.showAndWait();
			}
		} else {
			alert.setTitle("Points");
			alert.setContentText("empty points field");
			alert.showAndWait();
		}
	}

	/**
	 * @param points
	 * @return
	 * Calculate the sum of the points to be 100.
	 */
	public int CheckPoints(ArrayList<String> points) {
		int sum = 0;
		for (int i = 0; i < points.size(); i++)
			sum += Integer.parseInt(points.get(i));
		return sum;
	}
	/**
	 * @param event
	 * Produces the exam in the database.
	 */
	@FXML
	void CreateClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (ComBoxProf.getValue() != null && ComBoxCourse.getValue() != null && ComBoxQ.getValue() != null
				&& !Durationtxtfield.getText().isEmpty()) {
			if (CheckPoints(points) != 100) {
				alert.setTitle("Creation failed");
				alert.setContentText("Exam must contain 100 points");
				alert.showAndWait();
			} else {
				String details = per.getFirstName() + "_" + per.getLastName() + " ";
				details += ComBoxProf.getValue().replace(" ", "_") + " " + ComBoxCourse.getValue().replace(" ", "_")
						+ " ";
				details += Durationtxtfield.getText() + " "+Id.size()+" ";
				if (StudnetNTxtArea.getText().isEmpty())
					details += "emptystudnotes ";
				else
					details += StudnetNTxtArea.getText().replace(" ", "_")+" ";
				if (TeacherNtxtArea.getText().isEmpty())
					details += "emptyteacnotes ";
				else {
					details += TeacherNtxtArea.getText().replace(" ", "_")+" ";
				}
				details += splitStringArr(Id) + " ";
				details += splitStringArr(points) + " ";
				ClientUI.chat.accept("CreateExam " + details);
				alert.setTitle("Exam Created");
				alert.setContentText("The Exam created!");
				alert.showAndWait();
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
			}
		} else {
			alert.setTitle("Missing fields");
			alert.setContentText("Some of the fields are missing");
			alert.showAndWait();
		}
	}

	/**
	 * @param arr
	 * @return
	 * Split the string.
	 */
	String splitStringArr(ArrayList<String> arr) {
		String st = "";
		for (int i = 0; i < arr.size() - 1; i++)
			st += arr.get(i) + "_";
		st += arr.get(arr.size() - 1);
		return st;
	}
	/**
	 * @param event
	 * @throws IOException
	 * Shows a preview of an exam question and allows you to delete a selected question and change a score.
	 */
	@FXML
	void ExamSumClick(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if(points.isEmpty())
		{
			alert.setTitle("Empty questions");
			alert.setContentText("No questions added yet.");
			alert.showAndWait();
		}
		else {
			FXMLLoader loader = new FXMLLoader();
			URL location = getClass().getResource("/fxml/ViewExamScreen.fxml");
			loader.setLocation(location);
			Stage primaryStage = new Stage();
			Pane root = loader.load(location.openStream());
			ViewExamController controller = loader.getController();
			controller.loadDetails(this,exprev);
			Scene scene = new Scene(root);
			primaryStage.setTitle("Create Exam");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}
	/**
	 * @param event
	 * Close the screen.
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}
	/**
	 * @param event
	 * Shows the questions related to the teacher who wrote them.
	 */
	@FXML
	void ShowQuestion(ActionEvent event) {
		if (ComBoxQ.getValue() != null) {
			String qnum = ComBoxQ.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept("GetQInstructions " + Integer.parseInt(qnum));
			String qdetails = ChatClient.Qinstruction;
			String[] st = qdetails.split(" ");
			String inst = "";
			ID = st[0];
			for (int i = 1; i < st.length - 1; i++)
				inst += st[i] + " ";
			inst += st[st.length - 1];
			QtextArea.setText("");
			QtextArea.setText(inst);
		}
	}

	String ID;
	ObservableList<String> SelectedCourses;
	/**
	 * @param event
	 * Shows all the courses the teacher teaches.
	 */
	@FXML
	void ShowSelected(ActionEvent event) {
		if (ComBoxProf.getValue() != null) {
			String prof = ComBoxProf.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept("GetCourses " + prof);
			ArrayList<String> courses = ChatClient.CoursesArr;
			SelectedCourses = FXCollections.observableArrayList(courses);
			ComBoxCourse.setItems(SelectedCourses);
			ClearAll();
			ComBoxQ.setItems(null);
		}
	}

	ObservableList<String> SelectedQ;
	/**
	 * @param event
	 * Shows all questions by course.
	 * 
	 */
	@FXML
	void ShowSelectedQ(ActionEvent event) {
		if (ComBoxCourse.getValue() != null) {
			String course = ComBoxCourse.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept(
					"GetQIDByCourse " + course.replace(" ", "_") + " " + per.getFirstName() + " " + per.getLastName());
			ArrayList<String> qst = ChatClient.QuestionsArrByCourse;
			SelectedQ = FXCollections.observableArrayList(qst);
			ComBoxQ.setItems(SelectedQ);
			ClearAll();

		}
	}

	@FXML
	void ShowSelectionQu(MouseEvent event) {

	}
	/**
	 * Clear all the fields.
	 */

	void ClearAll() {
		Durationtxtfield.setText("");
		TeacherNtxtArea.setText("");
		QtextArea.setText("");
		Pointstxt.setText("");
		StudnetNTxtArea.setText("");
		Id.clear();
		points.clear();
		Overallpts.setVisible(false);
		exprev.clear();
	}

	Person per;
	/**
	 * @param per
	 * Load the details of the teacher.
	 * 
	 */
	public void loadDetails(Person per) {
		this.per = per;
		ClientUI.chat.accept("GetTeacherFaculties " + per.getID());
		setFacultiesComboBox();
	}

	ObservableList<String> FList;


	/**
	 *  creating list of Students.
	 */
	private void setFacultiesComboBox() {
		ArrayList<String> facultieslist = ChatClient.TeacherProfArr;
		FList = FXCollections.observableArrayList(facultieslist);
		ComBoxProf.setItems(FList);
	}
	/**
	 * initialize the class
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		points = new ArrayList<>();
		Id = new ArrayList<>();
		exprev=new ArrayList<>();
		Overallpts.setVisible(false);
	}
	/**
	 * @param expre
	 * refresh the data.
	 */
	public void refresh(ArrayList<ExamPreview> expre) {
		Pointstxt.setText("");
		Overallpts.setText("Overall Points: " + CheckPoints(points));
		exprev=expre;
	}

}
