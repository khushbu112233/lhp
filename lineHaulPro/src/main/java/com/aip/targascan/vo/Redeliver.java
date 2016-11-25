package com.aip.targascan.vo;

public class Redeliver {
	String _text;
	String _data;
	
	// Empty constructor
	public Redeliver(){
		
	}
	// constructor
	public Redeliver(String text,String _data){
		this._text = text; 
		this._data= _data;
	}
	// getting ID
	public String getredeliverText(){
		return this._text;
	}
	
	// setting id
	public void setredeliverText(String text){
		this._text = text;
	}
	public String getredeliverData(){
		return this._data;
	}
	
	// setting id
	public void setredeliverData(String data){
		this._data = data;
	}
	
}
