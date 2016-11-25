package com.aip.targascan.vo;

public class Changepwd {
	
	//private variables
	
	String _curpwd;
	String _newpwd;
	String _confirmnewpwd;
	
	// Empty constructor
	public Changepwd(){
		
	}
	// constructor
	public Changepwd(String _curpwd, String _newpwd ,String _confirmnewpwd ){
		this._curpwd = _curpwd; 
		this._newpwd = _newpwd; 
		this._confirmnewpwd = _confirmnewpwd; 
	}
	
	// constructor
	
	
	// getting ID
	public String getCurrentPwd(){
		return this._curpwd;
	}
	
	// setting id
	public void setCurrentPwd(String  pwd){
		this._curpwd = pwd;
	}
	public String getNewpwd(){
		return this._newpwd;
	}
	
	// setting id
	public void setNewpwd(String newpwd){
		this._newpwd = newpwd;
	}
	
	// getting name
	public String getConfirmNewpwd(){
		return this._confirmnewpwd;
	}
	
	// setting name
	public void setConfirmNewpwd(String confirmnewpwd){
		this._confirmnewpwd = confirmnewpwd;
	}
	
	// getting phone number
	
}
