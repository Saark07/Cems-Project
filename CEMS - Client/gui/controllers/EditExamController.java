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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ExamPreview;
import logic.Person;
import logic.Test;
/**
 * @author Vladi
 *	controller for editing a chosen exam
 */
public class EditExamController {

	@FXML
	private Button btnExit;

	@FXML
	private TextField Durationtxtfield;

	@FXML
	private Button DeleteExambtn;

	@FXML
	private TextArea TeacherNtxtArea;

	@FXML
	private Button UpdateExambtn;

	@FXML
	private ComboBox<String> ComBoxProf;

	@FXML
	private ComboBox<String> ComBoxCourse;

	@FXML
	private ComboBox<String> ComBoxExID;

	@FXML
	private TextArea StudnetNTxtArea;

	@FXML
	private Label Overallpts;

	@FXML
	private TableView<ExamPreview> QuestionsTblView;

	@FXML
	private TableColumn<ExamPreview, String> QIDCol;

	@FXML
	private TableColumn<ExamPreview, String> DetailsCol;

	@FXML
	private TableColumn<ExamPreview, String> PointsCol;

	@FXML
	private Button DeleteQbtn;

	@FXML
	private Button AddnewQbtn;

	@FXML
	private TextField Pointstxt;

	@FXML
	private Button updateQbtn;
	/**
	 * @param event
	 * deleting a chosen exam from database
	 */
	@FXML
	void DeleteExamClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (ComBoxExID.getValue() != null) {
			String Eid = ComBoxExID.getValue();
			ClientUI.chat.accept("DeleteExam " + Eid);
			alert.setTitle("Delete ");
			alert.setContentText("Exam Deleted successfully");
			alert.showAndWait();
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
		} else {
			alert.setTitle("No selection");
			alert.setContentText("Select ExamID");
			alert.showAndWait();
		}
	}
	/**
	 * @param event
	 * delete selected question from database
	 */
	@FXML
	void DeleteQClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (QuestionsTblView.getSelectionModel().getSelectedItem() != null) {
			ExamPreview ex = QuestionsTblView.getSelectionModel().getSelectedItem();
			int index = exprev.indexOf(ex);
			int sum = 0;
			exprev.remove(index);
			for (int i = 0; i < exprev.size(); i++) {
				ExamPreview exam = exprev.get(i);
				sum += Integer.parseInt(exam.getPoints());
			}
			Exprev = FXCollections.observableArrayList(exprev);
			QuestionsTblView.getItems().setAll(Exprev);
			Overallpts.setText("Overall Points: " + sum);
		} else {
			alert.setTitle("No selection");
			alert.setContentText("Select a row from the table");
			alert.showAndWait();

		}
	}

	/**
	 * @param event
	 * updating the changes applied to a specific question
	 */
	@FXML
	void updateQClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		int sum = 0;
		if (QuestionsTblView.getSelectionModel().getSelectedItem() != null) {
			if (!Pointstxt.getText().isEmpty()) {
				ExamPreview ex = QuestionsTblView.getSelectionModel().getSelectedItem();
				for (int i = 0; i < exprev.size(); i++) {
					ExamPreview exam = exprev.get(i);
					sum += Integer.parseInt(exam.getPoints());
				}
				sum -= Integer.parseInt(ex.getPoints());
				if (sum + Integer.parseInt(Pointstxt.getText()) <= 100) {
					sum += Integer.parseInt(Pointstxt.getText());
					int index = exprev.indexOf(ex);
					ex.setPoints(Pointstxt.getText());
					exprev.set(index, ex);
					Overallpts.setText("Overall Points: " + sum);
					Exprev = FXCollections.observableArrayList(exprev);
					QuestionsTblView.getItems().setAll(Exprev);
				} else {
					alert.setTitle("points");
					alert.setContentText("Too many points");
					alert.showAndWait();
				}
			} else {
				alert.setTitle("Empty points");
				alert.setContentText("Empty points field");
				alert.showAndWait();
			}
		} else {
			alert.setTitle("No selection");
			alert.setContentText("Select a row from the table");
			alert.showAndWait();
		}
	}
	/**
	 * @param event
	 * update selected exam in the database
	 */
	@FXML
	void UpdateExamClick(ActionEvent event) {
		String[] splittedtxt = Overallpts.getText().split("Overall Points: ");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		if (ComBoxExID.getValue() == null) {
			alert.setTitle("Empty ID");
			alert.setContentText("Please Select ExamID");
			alert.showAndWait();
		} else if (Integer.parseInt(splittedtxt[1]) < 100) {
			alert.setTitle("Update failed");
			alert.setContentText("Exam must contain 100 points");
			alert.showAndWait();
		} else {
			String qIdString = "";
			String pointsString = "";
			for (int i = 0; i < exprev.size() - 1; i++) {
				ExamPreview ex = exprev.get(i);
				qIdString += ex.getID() + " ";
				pointsString += ex.getPoints() + " ";
			}
			ExamPreview ex = exprev.get(exprev.size() - 1);
			qIdString += ex.getID();
			pointsString += ex.getPoints();
			String details = ComBoxExID.getValue() + " " + per.getFirstName() + "_" + per.getLastName() + " ";
			details += Durationtxtfield.getText() + " "+exprev.size()+" ";
			if (StudnetNTxtArea.getText().isEmpty())
				details += "emptystudnotes ";
			else
				details += StudnetNTxtArea.getText().replace(" ", "_") + " ";
			if (TeacherNtxtArea.getText().isEmpty())
				details += "emptyteacnotes ";
			else {
				details += TeacherNtxtArea.getText().replace(" ", "_") + " ";
			}
			details += qIdString.replace(" ", "_") + " " + pointsString.replace(" ", "_");
			ClientUI.chat.accept("UpdateExam " + details);
			alert.setTitle("Updated");
			alert.setContentText("Exam Updated successfulyl");
			alert.showAndWait();
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
		}
	}
	/**
	 * @param event
	 * handle exit action to close the current window
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}

	String[] points;
	/**
	 * @param event
	 * show the exam questions
	 */
	@FXML
	void ShowQuestions(ActionEvent event) {
		String exid = ComBoxExID.getSelectionModel().getSelectedItem();
		ArrayList<ExamPreview> exprev = new ArrayList<>();
		ClientUI.chat.accept("GetExamPrev " + exid);
		this.test = ChatClient.test;
		String qArr = test.getQArray();
		String[] st = qArr.split(" ");
		// 010202 030303
		String[] pts = test.getPoints().split(" ");
		points = pts;
		for (int i = 0; i < st.length; i++) {
			ClientUI.chat.accept("GetQInstructions " + Integer.parseInt(st[i].substring(2, 5)));
			ExamPreview ex = new ExamPreview();
			String qdetails = ChatClient.Qinstruction;
			String[] str = qdetails.split(" ");
			String inst = "";
			for (int j = 1; j < str.length - 1; j++)
				inst += str[j] + " ";
			inst += str[str.length - 1];
			ex.setID(st[i]);
			ex.setInstructions(inst);
			ex.setPoints(pts[i]);
			exprev.add(ex);
		}
		Durationtxtfield.setText(test.getDuration() + "");
		TeacherNtxtArea.setText(test.getTextForTeacher());
		StudnetNTxtArea.setText(test.getTextForStudent());
		Overallpts.setText("Overall Points: " + CheckPoints(pts));
		QIDCol.setCellValueFactory(new PropertyValueFactory<ExamPreview, String>("ID"));
		DetailsCol.setCellValueFactory(new PropertyValueFactory<ExamPreview, String>("Instructions"));
		PointsCol.setCellValueFactory(new PropertyValueFactory<ExamPreview, String>("points"));
		Exprev = FXCollections.observableArrayList(exprev);
		QuestionsTblView.getItems().setAll(Exprev);
		this.exprev = exprev;
	}
	/**
	 * clear the current screen
	 */
	void ClearAll() {
		Durationtxtfield.setText("");
		TeacherNtxtArea.setText("");
		Pointstxt.setText("");
		StudnetNTxtArea.setText("");
		QuestionsTblView.setItems(null);
	}

	ArrayList<ExamPreview> exprev;
	ObservableList<ExamPreview> Exprev;
	ObservableList<String> SelectedCourses;
	Test test;
	/**
	 * @param points
	 * @return int
	 * check that the sum of the questions is not higher than 100
	 */
	public int CheckPoints(String[] points) {
		int sum = 0;
		for (int i = 0; i < points.length; i++)
			sum += Integer.parseInt(points[i]);
		return sum;
	}

	/**
	 * @param event
	 * bring the courses list from database to the 'courses' combo box that match the selected profession
	 */
	
	@FXML
	void ShowSelected(ActionEvent event) {
		String prof = ComBoxProf.getSelectionModel().getSelectedItem();
		ClientUI.chat.accept("GetCourses " + prof);
		ArrayList<String> courses = ChatClient.CoursesArr;
		SelectedCourses = FXCollections.observableArrayList(courses);
		ComBoxCourse.setItems(SelectedCourses);
	}

	ObservableList<String> SelectedEid;
	/**
	 * @param event
	 * bring from the database the questions that match to the selected course
	 */
	@FXML
	void ShowSelectedQ(ActionEvent event) {
		if (ComBoxCourse.getValue() != null) {
			String course = ComBoxCourse.getSelectionModel().getSelectedItem();
			ClientUI.chat.accept("GetExamsIDByTeacher " + course.replace(" ", "_") + " " + per.getFirstName() + "_"
					+ per.getLastName());
			ArrayList<String> eid = ChatClient.ExamIDArrByCourse;
			SelectedEid = FXCollections.observableArrayList(eid);
			ComBoxExID.setItems(SelectedEid);
		}
	}
	/**
	 * @param event
	 * @throws IOException
	 * add a new question to the exam
	 */
	@FXML
	void addnewQClick(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		int sum = 0;
		if (ComBoxExID.getValue() != null) {
			for (int i = 0; i < exprev.size(); i++) {
				ExamPreview exam = exprev.get(i);
				sum += Integer.parseInt(exam.getPoints());
			}
			if (sum < 100) {
				FXMLLoader loader = new FXMLLoader();
				URL location = getClass().getResource("/fxml/AddNewQuestionToExamScreen.fxml");
				loader.setLocation(location);
				Stage primaryStage = new Stage();
				Pane root = loader.load(location.openStream());
				AddNewQuestionController controller = loader.getController();
				controller.loadDetails(this, per, sum, ComBoxExID.getValue(), ComBoxCourse.getValue());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Add Question to Exam");
				primaryStage.setScene(scene);
				primaryStage.show();
			} else {
				alert.setTitle("Exam Full");
				alert.setContentText("There is already 100 points for the exam.");
				alert.showAndWait();

			}
		} else {
			alert.setTitle("No Selection");
			alert.setContentText("Please select a row from the table");
			alert.showAndWait();
		}
	}
	/**
	 * bring the faculties list from the data base into combo box
	 */
	public int CheckPoints(ArrayList<String> points) {
		int sum = 0;
		for (int i = 0; i < points.size(); i++)
			sum += Integer.parseInt(points.get(i));
		return sum;
	}

	ObservableList<String> FList;
	/**
	 * bring the faculties list from the data base into combo box
	 */
	private void setFacultiesComboBox() {
		ArrayList<String> facultieslist = ChatClient.TeacherProfArr;
		FList = FXCollections.observableArrayList(facultieslist);
		ComBoxProf.setItems(FList);
	}

	Person per;
	/**
	 * @param per
	 * load details from other controllers
	 */
	public void loadDetails(Person per) {
		this.per = per;
		ClientUI.chat.accept("GetTeacherFaculties " + per.getID());
		setFacultiesComboBox();
	}
	/**
	 * @param sum
	 * refreshing the current screen
	 */
	public void refresh(int sum) {
		Exprev = FXCollections.observableArrayList(exprev);
		QuestionsTblView.getItems().setAll(Exprev);
		Overallpts.setText("Overall Points: " + sum);

	}

}
