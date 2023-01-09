package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Person;
import logic.Question;
/**
 * @author Vladi
 *	Controller for creating a new question
 */
public class CreateQuestionController implements Initializable {

	@FXML
	private Button OkbtnError;

	@FXML
	private TextArea FillQuestion;

	@FXML
	private Button CreateQst;

	@FXML
	private TextField AanswrFld;

	@FXML
	private TextField BanswrFld;

	@FXML
	private TextField CanswrFld;

	@FXML
	private TextField DanswrFld;

	@FXML
	private Button btnExit;

	@FXML
	private ComboBox<String> ComBoxProf;

	@FXML
	private ComboBox<String> ComBoxChoise;

	@FXML
	private ListView<String> ComBoxSelectCourse = new ListView<String>();
	/**
	 * @param event
	 * handling the event of exit the current window
	 */
	@FXML
	void ExitBtn(ActionEvent event) {

		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();

	}

	@FXML
	void ActiononClick(MouseEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setChoiseComboBox();
	}
	
	ObservableList<String> FList;
	/**
	 * bring from database all the faculties
	 */
	private void setFacultiesComboBox() {
		ArrayList<String> facultieslist = ChatClient.TeacherProfArr;
		FList = FXCollections.observableArrayList(facultieslist);
		ComBoxProf.setItems(FList);
	}
	/**
	 * setting the combo box with the wanted options
	 */
	private void setChoiseComboBox() {
		ObservableList<String> CList;
		ArrayList<String> ChoiseList = new ArrayList<>();
		ChoiseList.add("1");
		ChoiseList.add("2");
		ChoiseList.add("3");
		ChoiseList.add("4");
		CList = FXCollections.observableArrayList(ChoiseList);
		ComBoxChoise.setItems(CList);

	}

	ObservableList<String> SelectedCourses;
	/**
	 * @param event
	 * bring the courses list from database to the 'courses' combo box that match the selected profession
	 */
	@FXML
	void ShowSelected(ActionEvent event) {
		String prof = ComBoxProf.getSelectionModel().getSelectedItem();
		ClientUI.chat.accept("GetCourses " + prof);
		FillQuestion.clear();
		AanswrFld.clear();
		BanswrFld.clear();
		CanswrFld.clear();
		DanswrFld.clear();
		ComBoxChoise.getSelectionModel().clearSelection();
		ArrayList<String> courses = ChatClient.CoursesArr;
		SelectedCourses = FXCollections.observableArrayList(courses);
		ComBoxSelectCourse.setItems(SelectedCourses);
		ComBoxSelectCourse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ComBoxSelectCourse.setCellFactory(alv -> {
			ListCell<String> cell = new ListCell<>();
			cell.textProperty().bind(cell.itemProperty());
			cell.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
				ComBoxSelectCourse.requestFocus();
				if (!cell.isEmpty()) {
					int index = cell.getIndex();
					if (ComBoxSelectCourse.getSelectionModel().getSelectedIndices().contains(index)) {
						ComBoxSelectCourse.getSelectionModel().clearSelection(index);
					} else {
						ComBoxSelectCourse.getSelectionModel().select(index);
					}
					evt.consume();
				}
			});
			return cell;
		});

	}

	/**
	 * @param event
	 * @throws IOException
	 * adding the question to the database
	 */
	@FXML
	void AddQuestionToDB(ActionEvent event) throws IOException {

		int count = 0;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText("At least one of the fields is empty.\nPlease fill in all the fields");

		// missing Question description
		if (FillQuestion.getText().trim().length() == 0) {
			count++;
		}

		if (AanswrFld.getText().trim().isEmpty()) {
			count++;

		}

		if (BanswrFld.getText().trim().isEmpty()) {
			count++;

		}

		if (CanswrFld.getText().trim().isEmpty()) {
			count++;

		}

		if (DanswrFld.getText().trim().isEmpty()) {
			count++;

		}

		if (ComBoxProf.getSelectionModel().isEmpty()) {
			count++;
		}

		if (ComBoxChoise.getSelectionModel().isEmpty()) {
			count++;
		}

		if (ComBoxSelectCourse.getSelectionModel().isEmpty()) {
			count++;

		}

		if (count > 0)
			alert.showAndWait();
		else {
			ObservableList<String> courseAL = ComBoxSelectCourse.getSelectionModel().getSelectedItems();
			List<String> courses = courseAL.stream().collect(Collectors.toList());
 
			String[] ansArr = new String[4];
			ansArr[0] = AanswrFld.getText();
			ansArr[1] = BanswrFld.getText();
			ansArr[2] = CanswrFld.getText();
			ansArr[3] = DanswrFld.getText();

			Question qst = new Question();
			qst.setProfession(ComBoxProf.getSelectionModel().getSelectedItem());
			qst.setAuthor(per.getFirstName() + " " + per.getLastName());
			qst.setInstructions(FillQuestion.getText());
			qst.setAnswers(ansArr);
			qst.setCorrectAnswer(ComBoxChoise.getSelectionModel().getSelectedItem());
			for (int i = 0; i < courses.size(); i++) {
				qst.setCourse(courses.get(i));
				ClientUI.chat.accept("AddQuestion " + qst);

			}
			
			alert.setTitle("Add Message");
			alert.setContentText("The question creation succeeded!");
			alert.showAndWait();

			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary x window
		}

	}

	private Person per;
	/**
	 * @param person
	 * load details from other controllers
	 */
	public void loadDetails(Person person) {
		per = person;
		ClientUI.chat.accept("GetTeacherFaculties " + per.getID());
		setFacultiesComboBox();

	}

}
