package logic;

import java.io.Serializable;

/**
 * @author Ronen
 *Class for saving all time extension requests in exams.
 */
@SuppressWarnings("serial")
public class DurationRequests implements Serializable {
	private String testID;
	private String teacherID;
	private String teacherName;
	private String Course;
	private int OldDuration;
	private int NewDuration;
	private String reason;

	public DurationRequests(String testID, String teacherID, String course, int oldDuration, int newDuration,
			String reason) {
		super();
		this.testID = testID;
		this.teacherID = teacherID;
		Course = course;
		OldDuration = oldDuration;
		NewDuration = newDuration;
		this.reason = reason;
	}

	public String getTestID() {
		return testID;
	}

	public void setTestID(String testID) {
		this.testID = testID;
	}

	public String getTeacherID() {
		return teacherID;
	}

	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	public String getCourse() {
		return Course;
	}

	public void setCourse(String course) {
		Course = course;
	}

	public int getOldDuration() {
		return OldDuration;
	}

	public void setOldDuration(int oldDuration) {
		OldDuration = oldDuration;
	}

	public int getNewDuration() {
		return NewDuration;
	}

	public void setNewDuration(int newDuration) {
		NewDuration = newDuration;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
