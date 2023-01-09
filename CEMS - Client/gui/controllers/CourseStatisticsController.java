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
 * @author doron
 * This class is responsible for presenting the statistics of a particular course.
 *
 */
public class CourseStatisticsController implements Initializable {

	@FXML
	private Button btnShowStatistics;

	@FXML
	private Button btnClose;

	@FXML
	private BarChart<String, Integer> DistributChart;

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;

	@FXML
	private Label lblMedian;

	@FXML
	private Label lblAverage;

	@FXML
	private ComboBox<String> CoursesCmb;

	@FXML
	private ComboBox<String> ProfCmb;

	@FXML
	private Label lblExamsCnt;

	@FXML
	private Label noDtExist;

	ObservableList<String> ProfList;
	ObservableList<String> CourseList;
	/**
	 * set course profession
	 */
	private void setProffComboBox() {
		ArrayList<String> profflist = ChatClient.ProfArr;
		ProfList = FXCollections.observableArrayList(profflist);
		ProfCmb.setItems(ProfList);
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
	 * display all courses
	 */
	@FXML
	void ShowSelected(ActionEvent event) {
		String prof = ProfCmb.getSelectionModel().getSelectedItem();
		ClientUI.chat.accept("GetCourses " + prof);
		ArrayList<String> courses = ChatClient.CoursesArr;
		CourseList = FXCollections.observableArrayList(courses);
		CoursesCmb.setItems(CourseList);
	}
	/**
	 * @param event
	 * Displays the histogram of the statistical information
	 */
	@FXML
	void ShowStatisticsClick(ActionEvent event) {
		if (CoursesCmb.getValue() != null) {
			ClientUI.chat.accept("getCoursesExamsID " + CoursesCmb.getValue().toString().replace(" ", "_"));
			ArrayList<String> ExamsID = ChatClient.CoursesExamsIDArr;
			ArrayList<Integer> sumOfGrades = new ArrayList<>();
			DistributChart.getData().clear();
			if (!ExamsID.isEmpty()) {
				for(int i=0;i<ExamsID.size();i++)
				{
					ClientUI.chat.accept("GetExamStatsByExamID " + ExamsID.get(i));
					ArrayList<Integer> grades = ChatClient.ExamsGradeByExamIDArr;
					if (!grades.isEmpty()) {
						sumOfGrades.addAll(grades);
						XYChart.Series<String, Integer> set = new XYChart.Series<>();
						set.setName(ExamsID.get(i)+" ");
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
				lblAverage.setText(new DecimalFormat("##.##").format(GetAverage(sumOfGrades))+"");
				lblMedian.setText(new DecimalFormat("##.##").format(GetMedian(sumOfGrades))+"");
				lblExamsCnt.setText(sumOfGrades.size() + "");
			} else 
				clearAll();
		}
	}
	/**
	 * Clears the histogram
	 */
	private void clearAll() {
		lblAverage.setText("");
		lblMedian.setText("");
		lblExamsCnt.setText("");
		noDtExist.setVisible(true);
		DistributChart.getData().clear();
	}
	/**
	 *initialize the class
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setProffComboBox();
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
