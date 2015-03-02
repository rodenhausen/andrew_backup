package edu.arizona.biosemantics.oto.oto.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GlossaryDictionaryEntryData {
	private LoginData loginData;
	private String termDescription;
	
	public GlossaryDictionaryEntryData(){}
	
	public GlossaryDictionaryEntryData(LoginData loginData, String termDescription){
		this.loginData = loginData;
		this.termDescription = termDescription;
	}
	
	public LoginData getLoginData(){
		return loginData;
	}
	
	public String getTermDescription(){
		return termDescription;
	}
	
	public void setLoginData(LoginData loginData){
		this.loginData = loginData;
	}
	
	public void setTermDescription(String termDescription){
		this.termDescription = termDescription;
	}
}
