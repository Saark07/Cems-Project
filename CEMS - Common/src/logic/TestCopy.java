package logic;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Ronen
 *A department that keeps a copy of a test that finished
 */

@SuppressWarnings("unused")
public class TestCopy extends TestResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String IdAuthor;
	private String Notes, Explanation;
	private List<QuestionsAfterTest> qArray;
	
	public TestCopy(String profession, String course, String idTest, String grade, String idAuthor,
			 String notes, String explanation, List<QuestionsAfterTest> qArray) {
		super(profession, course, idTest, grade);//newGarade
		IdAuthor = idAuthor;
		Notes = notes;
		Explanation = explanation;
		this.qArray = qArray;
	}
	/*setqArray(qArray);
		setExplanation(explanation);
		setNotes(notes);*/
	
	public void setNotes(String notes) {
		Notes=notes;
	}
	public String getNotes() {
		return Notes;
	}
	public String getExplanation() {
		return Explanation;
	}
	public void setExplanation(String explanation) {
		Explanation = explanation;
	}
	public List<QuestionsAfterTest> getqArray() {
		return qArray;
	}
	public void setqArray(List<QuestionsAfterTest> qArray) {
		this.qArray = qArray;
	}

	public String getIdAuthor() {
		return IdAuthor;
	}

	public void setIdAuthor(String idAuthor) {
		IdAuthor = idAuthor;
	}

	@Override
	public String toString() {
		return "TestCopy [IdAuthor=" + IdAuthor + ", Notes=" + Notes + ", Explanation="
				+ Explanation + ", qArray=" + qArray + "]";
	}
	
}
