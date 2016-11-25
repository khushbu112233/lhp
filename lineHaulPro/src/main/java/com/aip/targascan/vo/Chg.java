package com.aip.targascan.vo;



public class Chg {
	//private variables
			String _text;
			String _data;
			
			// Empty constructor
			public Chg(){
				
			}
			// constructor
			public Chg(String text,String _data){
				this._text = text; 
				this._data= _data;
				
				
			}
	   	// getting ID
			public String getchgText(){
				return this._text;
			}
			
			// setting id
			public void setchgText(String text){
				this._text = text;
			}
			public String getchgData(){
				return this._data;
			}
			
			// setting id
			public void setchgData(String data){
				this._data = data;
			}
			
			
			
}
