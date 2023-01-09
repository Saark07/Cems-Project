package logic;

import java.io.Serializable;

/**
 * @author Ronen
 *Manual Testing class
 */
public class ManualTest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String IdTest, file, Profession, Coures;
	private int Duration;
	
	
	
	public ManualTest(String idTest, String file, String profession, String coures, int duration) {
		super();
		IdTest = idTest;
		this.file = file;
		Profession = profession;
		Coures = coures;
		Duration = duration;
	}
	public String getIdTest() {
		return IdTest;
	}
	public void setIdTest(String idTest) {
		IdTest = idTest;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public int getDuration() {
		return Duration;
	}
	public void setDuration(int duration) {
		Duration = duration;
	}
	public String getProfession() {
		return Profession;
	}
	public void setProfession(String profession) {
		Profession = profession;
	}
	public String getCoures() {
		return Coures;
	}
	public void setCoures(String coures) {
		Coures = coures;
	}
}
