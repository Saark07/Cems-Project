package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *A class that saves the results of a specific student's test
 */
public class TestResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Profession, Course;
	private String IdTest;
	private String Grade;
	
	
	public TestResult(String profession, String course, String idTest, String grade) {
		super();
		Profession = profession;
		Course = course;
		IdTest = idTest;
		Grade = grade;
		//NewGrade=newGarade//, String newGarade;
	}
	
	public String getIdTest() {
		return IdTest;
	}
	public String getProfession() {
		return Profession;
	}
	public String getCourse() {
		return Course;
	}
	public String getGrade() {
		return Grade;
	}
	public void setGarde(String grade) {
		Grade=grade;
	}
	/*public String getNewGrade() {
		return NewGrade;
	}

	public void setNewGrade(String newGrade) {
		NewGrade = newGrade;
	}*/
	@Override
	public String toString() {
		return "TestResult [Profession=" + Profession + ", Course=" + Course + ", IdTest=" + IdTest + ", Grade=" + Grade
				+ "]";
	}

	
	
	
}
