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
 * desplay the statistics of teacher
 *
 */
public class TeacherStatisticsController implements Initializable {


	@FXML
	private Button btnShowStatistics;

	@FXML
	private Button btnClose;

	@FXML
	private ComboBox<String> TeacherCmbox;

	@FXML
	private Label lblExamsCnt;

	@FXML
	private Label lblMedian;

	@FXML
	private Label lblFullName;

	@FXML
	private Label lblAverage;

	@FXML
	private BarChart<String, Integer> DistributChart;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;

	@FXML
	private Label noDtExist;

	ObservableList<String> TeacherList;

	/**
	 * @param event
	 * close the screen 
	 */
	@FXML
	void CloseClick(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}
    /**
     * @param event
     * display the statistics
     */
	@FXML
	void ShowStatisticsClick(ActionEvent event) {
		if (TeacherCmbox.getValue() != null) {
			ClientUI.chat.accept("getTeacherCourses " + TeacherCmbox.getValue().toString());
			ArrayList<String> coursesnames = ChatClient.TeacherCoursesArr;
			ArrayList<Integer> sumOfGrades = new ArrayList<>();
			DistributChart.getData().clear();
			if (!coursesnames.isEmpty()) {
				for (int i = 0; i < coursesnames.size(); i++) {
					ClientUI.chat.accept("GetExamStatsByCourse " + coursesnames.get(i).replace(" ", "_") + " "
							+ TeacherCmbox.getValue().toString());
					ArrayList<Integer> grades = ChatClient.ExamsGradeByCourseArr;
					if (!grades.isEmpty()) {
						sumOfGrades.addAll(grades);
						lblFullName.setText(ChatClient.fullname.replace("_", " "));
						XYChart.Series<String, Integer> set = new XYChart.Series<>();
						set.setName(coursesnames.get(i)+ " ");
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
						DistributChart.getData().add(set);
					} else
					{
						clearAll();
						return;
					}
				}
				noDtExist.setVisible(false);
				lblAverage.setText(new DecimalFormat("##.##").format(GetAverage(sumOfGrades)) + "");
				lblMedian.setText(new DecimalFormat("##.##").format(GetMedian(sumOfGrades)) + "");
				lblExamsCnt.setText(sumOfGrades.size() + "");
			} else
				clearAll();

		}

	}

	/**
	 * clear all the data data
	 */
	private void clearAll() {
		lblFullName.setText("");
		lblAverage.setText("");
		lblMedian.setText("");
		lblExamsCnt.setText("");
		noDtExist.setVisible(true);
		DistributChart.getData().clear();
	}

	/**
	 * creating list of Teachers
	 */
	private void setTeachersComboBox() {
		ArrayList<String> teacherslist = ChatClient.TeachersArr;
		TeacherList = FXCollections.observableArrayList(teacherslist);
		TeacherCmbox.setItems(TeacherList);
	}
	/**
	 *initialize the class
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setTeachersComboBox();
		noDtExist.setVisible(false);
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
		return avg;
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
		return median;

	}

}
