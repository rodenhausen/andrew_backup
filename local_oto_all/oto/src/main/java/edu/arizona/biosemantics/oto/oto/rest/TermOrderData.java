package edu.arizona.biosemantics.oto.oto.rest;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.arizona.biosemantics.oto.oto.beans.SimpleOrderBean;

@XmlRootElement
public class TermOrderData {
	private ArrayList<SimpleOrderBean> orderData;
	private LoginData loginData;
	
	public TermOrderData(){
		orderData = new ArrayList<SimpleOrderBean>();
	}
	
	public LoginData getLoginData(){
		return loginData;
	}
	
	public void setLoginData(LoginData data){
		loginData = data;
	}
	
	public ArrayList<SimpleOrderBean> getOrderData(){
		return orderData;
	}
	
	public void setOrderData(ArrayList<SimpleOrderBean> data){
		this.orderData = data;
	}
	
	public void addEntry(String orderName, ArrayList<String> includedTerms){
		orderData.add(new SimpleOrderBean(orderName, includedTerms));
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append("TermOrderData {\n");
		for (SimpleOrderBean pair: orderData){
			buff.append("\t[" + pair + "]\n");
		}
		buff.append("}\n");
		return buff.toString();
	}
}
