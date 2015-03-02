package edu.arizona.biosemantics.oto.oto.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NameContextData {
	private List<NameSentencePair> data;
	private LoginData loginData;
	
	public NameContextData(){
		data = new ArrayList<NameSentencePair>();
	}
	
	public LoginData getLoginData(){
		return loginData;
	}
	
	public void setLoginData(LoginData data){
		loginData = data;
	}
	
	public List<NameSentencePair> getData(){
		return data;
	}
	
	public void setData(List<NameSentencePair> data){
		this.data = data;
	}
	
	public void addEntry(String termName, String contextSentence){
		data.add(new NameSentencePair(termName, contextSentence));
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append("PopulateDatasetRequest {");
		for (int i = 0; i < data.size(); i++){
			NameSentencePair pair = data.get(i);
			buff.append(pair.toString());
			if (i != data.size()-1)
				buff.append(", ");
		}
		buff.append("}\n");
		return buff.toString();
	}
	
	public ArrayList<String> getTermList(){
		ArrayList<String> termList = new ArrayList<String>(data.size());
		for (NameSentencePair pair: data){
			termList.add(pair.getTermName());
		}
		return termList;
	}
	
	public ArrayList<String> getSentences(){
		ArrayList<String> termList = new ArrayList<String>(data.size());
		for (NameSentencePair pair: data){
			termList.add(pair.getContextSentence());
		}
		return termList;
	}
}
class NameSentencePair{
	String termName;
	String contextSentence;
	
	public NameSentencePair(){
		
	}
	
	public NameSentencePair(String termName, String contextSentence){
		this.termName = termName;
		this.contextSentence = contextSentence;
	}
	
	public String getTermName(){
		return termName;
	}
	
	public void setTermName(String termName){
		this.termName = termName;
	}
	
	public String getContextSentence(){
		return contextSentence;
	}
	
	public void setContextSentence(String contextSentence){
		this.contextSentence = contextSentence;
	}
	
	public String toString(){
		return termName + ": " + contextSentence;
	}
}
