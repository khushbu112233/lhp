package com.aip.targascan.vo;

public class Login {
	
	//private variables
	int _id;
	String _driverid;
	String _name;
	String _password;
	int _autologin;
	int _rememberme;
	// Empty constructor
	public Login(){
		
	}
	// constructor
	public Login(int id,String _driverid, String name,String _username ,int _autologin, int _rememberme){
		this._id = id; 
		this._driverid= _driverid;
		this._name = name;
		this._password = _password;
		this._autologin = _autologin;
		this._rememberme = _rememberme;		
	}
	
	// constructor
	public Login(int i, String name, String _password, String string, String string2, String string3, String string4){
		this._name = name;
		this._password = _password;
	}
	public Login(int id, String _driverid, String name ,String _username,
			String _password) {
		// TODO Auto-generated constructor stub
	
		this._id = id; 
		this._driverid= _driverid;
		this._name = name;
		this._password = _password;
		this._autologin = _autologin;
		this._rememberme = _rememberme;

	}
	
	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	public String getDriverID(){
		return this._driverid;
	}
	
	// setting id
	public void setDriverID(String id){
		this._driverid = id;
	}
	
	// getting name
	public String getuserName(){
		return this._name;
	}
	
	// setting name
	public void setuserName(String name){
		this._name = name;
	}
	
	// getting phone number
	public String getPassword(){
		return this._password;
	}
	public void setPassword(String  password){
		this._password =password ;
	}
	
	// setting phone number
	public void setAutologin(int i){
		this._autologin = i;
	}
	public int getAutologin(){
		return this._autologin;
	}
	
	// setting phone number
	public int getRememberme(){
		return  this._rememberme;
	}
	public void setRememberme(int rememberme){
		this._rememberme = rememberme;
	}
}
