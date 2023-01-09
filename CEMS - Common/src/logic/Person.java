package logic;

import java.io.Serializable;
/**
 * @author Ronen
 *Class of Personal Information
 */
@SuppressWarnings("serial")
public class Person implements Serializable {
	private String ID;
	private String FirstName;
	private String LastName;
	private String Phone;
	private String Email;
	private String Address;
	
	public Person(String iD, String firstName, String lastName, String phone, String email, String address) {
		super();
		ID = iD;
		FirstName = firstName;
		LastName = lastName;
		Phone = phone;
		Email = email;
		Address = address;
	}
	
	@Override
	public String toString() {
		return "Person [ID=" + ID + ", FirstName=" + FirstName + ", LastName=" + LastName + ", Phone=" + Phone
				+ ", Email=" + Email + ", Address=" + Address + "]";
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	
}
