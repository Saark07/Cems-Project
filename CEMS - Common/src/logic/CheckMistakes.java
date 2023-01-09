package logic;

import java.io.Serializable;
import java.util.List;
/**
 * @author Ronen
 * Class for saving the answers data for exam of all students for the purpose of checking for cheating.
 */
@SuppressWarnings("serial")
public class CheckMistakes implements Serializable {
	private String StudID;
	private List<QuestionsAfterTest> qList;
	
	@Override
	public String toString() {
		return "CheckMistakes [StudID=" + StudID + ", qList=" + qList + "]";
	}
	public CheckMistakes(String studID, List<QuestionsAfterTest> qList) {
		super();
		StudID = studID;
		this.qList = qList;
	}
	public String getStudID() {
		return StudID;
	}
	public void setStudID(String studID) {
		StudID = studID;
	}
	public List<QuestionsAfterTest> getqList() {
		return qList;
	}
	public void setqList(List<QuestionsAfterTest> qList) {
		this.qList = qList;
	}
	
}
