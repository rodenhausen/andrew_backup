package edu.arizona.biosemantics.oto.oto.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginData {
	private String userEmail;
	private String userPassword;
	
	public LoginData(){}
	
	public LoginData(String userEmail, String userPassword){
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}
	
	public String getUserEmail(){
		return userEmail;
	}
	
	public String getUserPassword(){
		return userPassword;
	}
	
	public void setUserEmail(String email){
		this.userEmail = email;
	}
	
	public void setUserPassword(String password){
		this.userPassword = password;
	}
}