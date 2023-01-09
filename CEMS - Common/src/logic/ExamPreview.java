package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *Class for saving selected questions for exam construction.
 */
@SuppressWarnings("serial")
public class ExamPreview implements Serializable {
	private String ID;
	private String Instructions;
	private String points;
	public String getID() {
		return ID;
	}
	public ExamPreview(String iD, String instructions, String points) {
		super();
		ID = iD;
		Instructions = instructions;
		this.points = points;
	}
	public ExamPreview() {
		
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getInstructions() {
		return Instructions;
	}
	public void setInstructions(String instructions) {
		Instructions = instructions;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
}
