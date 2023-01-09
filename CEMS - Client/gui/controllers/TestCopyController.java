package controllers;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import logic.Person;
import logic.QuestionsAfterTest;
import logic.TestCopy;

/**
 * @author doron
 * Class of Copy Resolved Exam with Comments and Answer Markings
 *
 */

public class TestCopyController implements Initializable{

    @FXML
    private Button btnNextQ;

    @FXML
    private Button btnPrevQ;

    @FXML
    private RadioButton Choice1;

    @FXML
    private RadioButton Choice2;

    @FXML
    private RadioButton Choice3;

    @FXML
    private RadioButton Choice4;

	@FXML
	private Label lblPoints;

    @FXML
    private ToggleGroup g1;
    
    @FXML
    private TextArea txtExplanation;
    
    @FXML
    private TextArea txtQuestions;

    @FXML
    private TextField txtAnswer1;

    @FXML
    private TextField txtAnswer2;

    @FXML
    private TextField txtAnswer3;

    @FXML
    private TextField txtAnswer4;

    @FXML
    private Label txtNunOfQ;

    @FXML
    private TextField txtaFewQ;
    
    @FXML
    private TextArea txtNotes;

    @FXML
    private Label labrlExplanation;


    @FXML
    private Label lblnoanswer;
    @FXML
    private Button btnFirstQ;

    @FXML
    private Button btnExit;

    @FXML
    private Label labelName;

    @FXML
    private Label txtName;

    @FXML
    private Label labelCoures;

    @FXML
    private Label txtCoures;

    @FXML
    private Label labelGrade;

    @FXML
    private Label txtGrade;

    @FXML
    private Label titleScreen;

    private TestCopy copy;
    private int i=0;
    private boolean firstQ=false, lastQ=false;
    List<QuestionsAfterTest> q=new ArrayList<>();
    String [] str= {"1","2","3","4"};
    /**
  	 * @param person
  	 * @param testCopy
  	 * Loading exam and student information.
  	 */
    
	public void loadDetails(Person person, TestCopy testCopy) {//
    	copy=testCopy;
    	btnPrevQ.setDisable(true);
    	q=(List<QuestionsAfterTest>)testCopy.getqArray();
		titleScreen.setText("Test Copy\n"); 
		txtName.setText(person.getFirstName()+" "+person.getLastName());
		txtCoures.setText(testCopy.getCourse());
		txtGrade.setText(testCopy.getGrade());
		txtNunOfQ.setText("1)");
    	txtQuestions.setText(q.get(0).getQuestion());
    	txtaFewQ.setText("1/"+q.size());
    	lblPoints.setText("Points: "+q.get(0).getPoint());
    	txtNotes.setText(testCopy.getNotes());
    	txtExplanation.setText(testCopy.getExplanation());
    	txtAnswer1.setText(q.get(0).getAnswer1());
    	txtAnswer2.setText(q.get(0).getAnswer2());
    	txtAnswer3.setText(q.get(0).getAnswer3());
    	txtAnswer4.setText(q.get(0).getAnswer4());
    	String choiceStud= q.get(0).getAnswerStud();
    	String currecet= q.get(0).getCorrectAnswer();
    	loadQ(choiceStud, currecet);

    }
	/**
     * @param e
     * Close the window and return to the main window.
     */
    @FXML
    public void handelBtnExit(ActionEvent e) {
    	if(e.getSource()==btnExit) {
    		System.out.println("Bye Bye!!");
    		((Node) e.getSource()).getScene().getWindow().hide();
    	}
    }
    /**
     * @param e
     * Go to the next question on 
     */
    @FXML
    public void handelBtnNext(ActionEvent e) { 	
    	if(e.getSource()==btnNextQ) {
    		q=(List<QuestionsAfterTest>)copy.getqArray();		
    		if(i==q.size()-1) {
    			if(!lastQ) {
    				JOptionPane.showMessageDialog(null, "No more questions");
    				lastQ=!lastQ;
    			}
    		}
    		else {
    			i++;
    			if (i == q.size() - 1) {
					btnNextQ.setDisable(true);
					btnPrevQ.setDisable(false);
				} else
					btnPrevQ.setDisable(false);
    			txtNunOfQ.setText((i+1)+")");
	    		txtQuestions.setText(q.get(i).getQuestion());
	    		txtaFewQ.setText((i+1)+"/"+q.size());
	    	    txtAnswer1.setText(q.get(i).getAnswer1());
	    	    txtAnswer2.setText(q.get(i).getAnswer2());
	    	    txtAnswer3.setText(q.get(i).getAnswer3());
	    	    txtAnswer4.setText(q.get(i).getAnswer4());
	    	    lblPoints.setText("Points: "+q.get(i).getPoint());
	    	    String choiceStud= q.get(i).getAnswerStud();
	    	    String currecet= q.get(i).getCorrectAnswer();
	    	    resetColor();
	    	    loadQ(choiceStud, currecet);
			}
    	}
    	
    	
    	
    }
    /**
     * @param e
     * Back to the previous question in the exam.
     */
    @FXML
    void handelBtnPrev(ActionEvent e) {
    	if(e.getSource()==btnPrevQ) {
    		
    		q=(List<QuestionsAfterTest>)copy.getqArray();
    		if(i==0) {
    			if(!firstQ) {
    				JOptionPane.showMessageDialog(null, "This is a first question");
    				firstQ=!firstQ;
    			}
    		}
    		else {
    			i--;
    			if (i == 0) {
					btnPrevQ.setDisable(true);
					btnNextQ.setDisable(false);
				} else
					btnNextQ.setDisable(false);
    			txtNunOfQ.setText((i+1)+")");
	    		txtQuestions.setText(q.get(i).getQuestion());
	    		txtaFewQ.setText((i+1)+"/"+q.size());
	    	    txtAnswer1.setText(q.get(i).getAnswer1());
	    	    txtAnswer2.setText(q.get(i).getAnswer2());
	    	    txtAnswer3.setText(q.get(i).getAnswer3());
	    	    txtAnswer4.setText(q.get(i).getAnswer4());
	    	    lblPoints.setText("Points: "+q.get(i).getPoint());
	    	    String choiceStud= q.get(i).getAnswerStud();
	    	    String currecet= q.get(i).getCorrectAnswer();
	    	    resetColor();
	    	    loadQ(choiceStud, currecet);
			}
    	}
    }
    /**
     * @param e
     * Go directly to the first question.
     */
    @FXML
    public void handelBtnFirstQuestions(ActionEvent e) {
    	
    	if(e.getSource()==btnFirstQ) {
    		resetColor();
    		btnPrevQ.setDisable(true);
			btnNextQ.setDisable(false);
    		q=(List<QuestionsAfterTest>)copy.getqArray();
    		txtNunOfQ.setText("1)");
    		txtQuestions.setText(q.get(0).getQuestion());
    	    txtAnswer1.setText(q.get(0).getAnswer1());
    	    txtAnswer2.setText(q.get(0).getAnswer2());
    	    txtAnswer3.setText(q.get(0).getAnswer3());
    	    txtAnswer4.setText(q.get(0).getAnswer4());
			String choiceStud= q.get(0).getAnswerStud();
			lblPoints.setText("Points: "+q.get(0).getPoint());
			String currecet= q.get(0).getCorrectAnswer();
			loadQ(choiceStud, currecet);
		    i=0;	    
		    
		}
    }
    /**
     *  Reset the correct and incorrect answer markers.
     */
    public void resetColor() {
		
		txtAnswer1.setStyle("-fx-background-color: white;");
		txtAnswer2.setStyle("-fx-background-color: white;");
		txtAnswer3.setStyle("-fx-background-color: white;");
		txtAnswer4.setStyle("-fx-background-color: white;");
		
	}
    /**
  	 * @param choiceStud
  	 * @param currecet
  	 * Loading the questions on the screen.
  	 */
    public void loadQ(String choiceStud, String currecet) {

		for (int j = 0; j < 4; j++) {
			if (!choiceStud.equals("-1")) {
				if (choiceStud.equals(str[j]) && choiceStud.equals(currecet)) {
					lblnoanswer.setVisible(false);
					if (j == 0) {
						txtAnswer1.setStyle("-fx-background-color: green;");
						Choice1.setSelected(true);
					} else if (j == 1) {
						txtAnswer2.setStyle("-fx-background-color: green;");
						Choice2.setSelected(true);
					} else if (j == 2) {
						txtAnswer3.setStyle("-fx-background-color: green;");
						Choice3.setSelected(true);
					} else {
						txtAnswer4.setStyle("-fx-background-color: green;");
						Choice4.setSelected(true);
					}
				} else if (choiceStud.equals(str[j]) && !(choiceStud.equals(currecet))) {
					lblnoanswer.setVisible(false);
					if (j == 0) {
						txtAnswer1.setStyle("-fx-background-color: red;");
						Choice1.setSelected(true);
					} else if (j == 1) {
						txtAnswer2.setStyle("-fx-background-color: red;");
						Choice2.setSelected(true);
					} else if (j == 2) {
						txtAnswer3.setStyle("-fx-background-color: red;");
						Choice3.setSelected(true);
					} else {
						txtAnswer4.setStyle("-fx-background-color: red;");
						Choice4.setSelected(true);
					}
				}

				if (!(choiceStud.equals(currecet))) {
					lblnoanswer.setVisible(false);
					if (currecet.equals(str[j])) {
						if (j == 0) {
							txtAnswer1.setStyle("-fx-background-color: green;");
							Choice1.setSelected(false);
						} else if (j == 1) {
							txtAnswer2.setStyle("-fx-background-color: green;");
							Choice2.setSelected(false);
						} else if (j == 2) {
							txtAnswer3.setStyle("-fx-background-color: green;");
							Choice3.setSelected(false);
						} else {
							txtAnswer4.setStyle("-fx-background-color: green;");
							Choice4.setSelected(false);
						}
					}
				}
			} else {
				lblnoanswer.setVisible(true);
				Choice1.setSelected(false);
				Choice2.setSelected(false);
				Choice3.setSelected(false); 
				Choice4.setSelected(false);
				if (currecet.equals("1"))
					txtAnswer1.setStyle("-fx-background-color: green;");
				else if (currecet.equals("2"))
					txtAnswer2.setStyle("-fx-background-color: green;");
				else if (currecet.equals("3"))
					txtAnswer3.setStyle("-fx-background-color: green;");
				else if (currecet.equals("4"))
					txtAnswer4.setStyle("-fx-background-color: green;");

			}
		}
	}
	/**
	 * initialize the class 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Choice1.setDisable(true);
		Choice2.setDisable(true);
		Choice3.setDisable(true);
		Choice4.setDisable(true);
		
	}

}