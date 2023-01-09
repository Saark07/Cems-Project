package controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import Client.ChatClient;
import Client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @author Saar
 *show the statistics of student
 */
public class StudentStatisticsController implements Initializable {

	@FXML
	private Button btnShowStatistics;

	@FXML
	private Button btnClose;

	@FXML
	private ComboBox<String> StudentCmbox;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;
	
    @FXML
    private Label lblExamsCnt;


	@FXML
	private BarChart<String, Integer> DitributChart;

	@FXML
	private Label lblFullName;

	@FXML
	private Label lblMedian;

	@FXML
	private Label lblAverage;

	@FXML
	private Label noDtExist;

	ObservableList<String> StudentsList;

	/**
	 * creating list of Students
	 */
	private void setStudentsComboBox() {
		ArrayList<String> studentslist = ChatClient.StudentsArr;
		StudentsList = FXCollections.observableArrayList(studentslist);
		StudentCmbox.setItems(StudentsList);
	}
	/**
	 * @param event
	 * Close screen.
	 */
	@FXML
	void CloseClick(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}
	/**
	 * @param event
	 * Displays the histogram of the statistical information
	 */
	@FXML
	void ShowStatisticsClick(ActionEvent event) {
		if (StudentCmbox.getValue()!=null) {
			ClientUI.chat.accept("getStudentExam " + StudentCmbox.getValue().toString());
			ArrayList<Integer> grades = ChatClient.ExamsGradeArr;
			if (!grades.isEmpty()) {
				noDtExist.setVisible(false);
				lblFullName.setText(ChatClient.fullname.replace("_", " "));
				lblAverage.setText(new DecimalFormat("##.##").format(GetAverage(grades))+"");
				lblMedian.setText(new DecimalFormat("##.##").format(GetMedian(grades))+"");
				lblExamsCnt.setText(grades.size()+"");
				DitributChart.getData().clear();
				XYChart.Series<String, Integer> set = new XYChart.Series<>();
				set.getData().add(new Data<String, Integer>("0-54.9", getnumberOfGrades(grades, 0, 54)));
				set.getData().add(new Data<String, Integer>("55-59", getnumberOfGrades(grades, 55, 59)));
				set.getData().add(new Data<String, Integer>("60-64", getnumberOfGrades(grades, 60, 64)));
				set.getData().add(new Data<String, Integer>("65-69", getnumberOfGrades(grades, 65, 69)));
				set.getData().add(new Data<String, Integer>("70-74", getnumberOfGrades(grades, 70, 74)));
				set.getData().add(new Data<String, Integer>("75-79", getnumberOfGrades(grades, 75, 79)));
				set.getData().add(new Data<String, Integer>("80-84", getnumberOfGrades(grades, 80, 84)));
				set.getData().add(new Data<String, Integer>("85-89", getnumberOfGrades(grades, 85, 89)));
				set.getData().add(new Data<String, Integer>("90-94", getnumberOfGrades(grades, 90, 94)));
				set.getData().add(new Data<String, Integer>("95-100", getnumberOfGrades(grades, 95, 100)));
				DitributChart.getData().add(set);
			} else {
				lblFullName.setText("");
				lblAverage.setText("");
				lblMedian.setText("");
				lblExamsCnt.setText("");
				noDtExist.setVisible(true);
				DitributChart.getData().clear();
			}
		}

	}
	/**
	 *initialize the class
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setStudentsComboBox();
		noDtExist.setVisible(false);
		XYChart.Series<String, Integer> set = new XYChart.Series<>();
		set.getData().add(new Data<String, Integer>("0-54.9", 0));
		set.getData().add(new Data<String, Integer>("55-59", 0));
		set.getData().add(new Data<String, Integer>("60-64", 0));
		set.getData().add(new Data<String, Integer>("65-69", 0));
		set.getData().add(new Data<String, Integer>("70-74", 0));
		set.getData().add(new Data<String, Integer>("75-79", 0));
		set.getData().add(new Data<String, Integer>("80-84", 0));
		set.getData().add(new Data<String, Integer>("85-89", 0));
		set.getData().add(new Data<String, Integer>("90-94", 0));
		set.getData().add(new Data<String, Integer>("95-100", 0));
		DitributChart.getData().add(set);

	}
	/*------------------------------------------------------------------------------------------------------------------------------------
	 *                        									Statistics
	-------------------------------------------------------------------------------------------------------------------------------------- */
	/**
	 * @param grades
	 * @param lowestGrade
	 * @param highestGrade
	 * @return
	 * Calculates the amount of scores in the correct range.
	 */
	private int getnumberOfGrades(ArrayList<Integer> grades, int lowestGrade, int highestGrade) {
		int cnt = 0;
		for (int i = 0; i < grades.size(); i++) {
			if (grades.get(i) <= highestGrade && grades.get(i) >= lowestGrade)
				cnt++;
		}
		return cnt;

	}
	/**
	 * @param grades
	 * @return
	 * Calculation of average scores.
	 */
	private double GetAverage(ArrayList<Integer> grades) {
		int sum = 0;
		for (int i = 0; i < grades.size(); i++) {
			sum += grades.get(i);
		}
		double avg = (double) sum / grades.size();
		return avg ;
	}
	/**
	 * @param grades
	 * @return
	 * Median calculation.
	 */
	private double GetMedian(ArrayList<Integer> grades) {
		Collections.sort(grades);
		double median = 0;
		if (grades.size() % 2 == 0) {
			int medPlace1 = grades.size() / 2;
			int medPlace2 = (grades.size() + 2) / 2;
			median = ((double) grades.get(medPlace1 - 1) + grades.get(medPlace2 - 1)) / 2;
		} else {
			int medPlace = (grades.size() + 1) / 2;
			median = ((double) grades.get(medPlace - 1));
		}
		return median ;
	}
}