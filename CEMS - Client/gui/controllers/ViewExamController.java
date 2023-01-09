package controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.ExamPreview;

/**
 * @author edena
 * Class for selecting a question for updating or deleting.
 */
public class ViewExamController {

    @FXML
    private TableView<ExamPreview> questionTblView;

    @FXML
    private TableColumn<ExamPreview, String> QIdCol;

    @FXML
    private TableColumn<ExamPreview, String> DetailsCol;

    @FXML
    private TableColumn<ExamPreview, String> PointsCol;

    @FXML
    private Button btnExit;

    @FXML
    private Button Updatebtn;

    @FXML
    private Button Deletebtn;

    @FXML
    private TextField Points;
    
    CreateExamController econtroller;
    /**
     * @param event
     * Delete question
     */
    @FXML
    void DeleteClick(ActionEvent event) {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
    	if (questionTblView.getSelectionModel().getSelectedItem() != null) {
    		ExamPreview ex = questionTblView.getSelectionModel().getSelectedItem();
    		econtroller.exprev.remove(ex);
    		int index=econtroller.Id.indexOf(ex.getID());
    		econtroller.Id.remove(index);
    		econtroller.points.remove(index);
        	econtroller.refresh(expre);
    		loadDetails(econtroller, expre);
    	}else {
			alert.setTitle("No selection");
			alert.setContentText("Select a row from the table");
			alert.showAndWait();
    	}
    }
    /**
     * @param event
     * Close screen.
     */
    @FXML
    void ExitBtn(ActionEvent event) {
    	econtroller.refresh(expre);
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
    }
    /**
     * @param event
     * Question update.
     */
    @FXML
    void UpdateClick(ActionEvent event) {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
    	if (questionTblView.getSelectionModel().getSelectedItem() != null) {
    		if(!Points.getText().isEmpty())
    		{
    		ExamPreview ex = questionTblView.getSelectionModel().getSelectedItem();
    		econtroller.points.set(econtroller.Id.indexOf(ex.getID()), Points.getText());
    		ex.setPoints(Points.getText());
    		expre.set(econtroller.Id.indexOf(ex.getID()), ex);
    		loadDetails(econtroller, expre);
    		}
    		else {
    			alert.setTitle("Empty points");
    			alert.setContentText("Empty points field");
    			alert.showAndWait();
    		}
    	}else {
			alert.setTitle("No selection");
			alert.setContentText("Select a row from the table");
			alert.showAndWait();
    	}
    }
	ObservableList<ExamPreview> Exprev;
	ArrayList<ExamPreview> expre;
	
	/**
	 * @param createExamController
	 * @param exprev
	 * Loading question data.
	 */
	public void loadDetails(CreateExamController createExamController, ArrayList<ExamPreview> exprev) {
		econtroller=createExamController;
		expre=exprev;
		QIdCol.setCellValueFactory(new PropertyValueFactory<ExamPreview, String>("ID"));
		DetailsCol.setCellValueFactory(new PropertyValueFactory<ExamPreview, String>("Instructions"));
		PointsCol.setCellValueFactory(new PropertyValueFactory<ExamPreview, String>("points"));
		Exprev = FXCollections.observableArrayList(exprev);
		questionTblView.getItems().setAll(Exprev);	
	}

}
