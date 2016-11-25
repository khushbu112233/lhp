package com.aip.targascan.vo;

public class Company {
	//private variables
		int _companyid;
		String _companyname;
		
		// Empty constructor
		public Company(){
			
		}
		// constructor
		public Company(int id,String _companyname){
			this._companyid = id; 
			this._companyname= _companyname;
			
			
		}
   	// getting ID
		public int companyID(){
			return this._companyid;
		}
		
		// setting id
		public void setcompanyID(int companyid){
			this._companyid = companyid;
		}
		public String getcompanyName(){
			return this._companyname;
		}
		public int getcompanyID(){
			return this._companyid;
		}
		// setting id
		public void setcompanyName(String companyname){
			this._companyname = companyname;
		}
		
		
		

}
