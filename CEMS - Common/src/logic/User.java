package logic;

import java.io.Serializable;

/**
 * @author Ronen
 *System Users Class
 */
@SuppressWarnings("serial")
public class User implements Serializable{
	private String UserName;
	private String Password;
	private String Permission;


	public User(String userName, String password, String permission) {
		super();
		UserName = userName;
		Password = password;
		Permission = permission;

	}

	@Override
	public String toString() {
		return UserName+" "+Password+" "+Permission;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPermssion() {
		return Permission;
	}

	public void setPermssion(String permission) {
		Permission = permission;
	}

}
