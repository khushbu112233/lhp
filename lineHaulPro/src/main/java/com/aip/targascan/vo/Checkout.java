package com.aip.targascan.vo;

public class Checkout {
	//private variables
	
		String _name;
		String _miles;
		String _driverid;
		String _authkey;
		String _checkout;
		// Empty constructor
		public Checkout(){
			
		}
		// constructor
		public Checkout(String name, String miles  ){
			this._name = name; 
			this._miles = miles; 
		}
		
		// constructor
		
		
		// getting ID
		public String getVehicleName(){
			return this._name;
		}
		
		// setting id
		public void setVehicleName(String  name){
			this._name = name;
		}
		public String getMiles(){
			return this._miles;
		}
		
		// setting id
		public void setMiles(String miles){
			this._miles = miles;
		}
		public String getDriverId(){
			return this._driverid;
		}
		
		// setting id
		public void setDriverId(String  id){
			this._driverid = id;
		}
		public String getAuthKey(){
			return this._authkey;
		}
		
		// setting id
		public void setAuthKey(String  authkey){
			this._authkey =authkey;
		}
		public String getCheckout(){
			return this._checkout;
		}
		
		// setting id
		public void setCheckout(String  checkout){
			this._checkout =checkout;
		}
		// getting name
		
		
		// getting phone number
		
	

}
