package com.aip.targascan.vo;

public class MultiScan  {
	//private variables
		String signatureName;
		String companyID;
		String chg1;
		String chg2;
		String carton1;
		String reDelivery;
		String toAddress;
		String fromAddress;
		String recordDate;
		String authKey;
		String driverId;
		
		// Empty constructor
		public MultiScan(){
			
		}
		// constructor
		public MultiScan(String signaturename,String companyID, String chg1,String chg2,String carton1,String redelivery,String toaddress,String fromaddress,String recorddate,String authkey,String driverId){
			this.signatureName = signaturename; 
			this.companyID= companyID;
			this.chg1 =chg1;
			this.chg2 =chg2;
			this.carton1 =carton1;
			this.reDelivery = redelivery;
			this.toAddress = toaddress;
			this.fromAddress=fromaddress;
			this.recordDate=recorddate;
			this.authKey=authkey;
			this.driverId=driverId;
			
		}
		
		// constructor
		
		
		// getting ID
		public String getSignatureName(){
			return this.signatureName;
		}
		
		// setting id
		public void setSignatureName(String signaturename){
			this.signatureName =signaturename;
		}
		public String getCompanyID(){
			return this.companyID;
		}
		
		// setting id
		public void setDriverID(String companyid){
			this.companyID =companyid;
		}
		
		// getting name
		public String getChg1(){
			return this.chg1;
		}
		
		// setting name
		public void setChg1(String chg1){
			this.chg1 =chg1;
		}
		
		// getting phone number
		public String getChg2(){
			return this.chg2;
		}
		public void setChg2(String  chg2){
			this.chg2 =chg2 ;
		}
		
		// setting phone number
		
		
		// setting phone number
		public String getCarton1(){
			return  this.carton1;
		}
		public void setCarton1(String Carton1){
			this.carton1 =carton1;
		}
		public String getRedelivery(){
			return  this.reDelivery;
		}
		public void setRedelivery(String redelivery){
			this.reDelivery =redelivery;
		}
		public String getToAdress(){
			return  this.toAddress;
		}
		public void setToAddress(String toaddress){
			this.toAddress =toaddress;
		}
		public String getFromAdress(){
			return  this.fromAddress;
		}
		public void setFromAddress(String fromaddress){
			this.fromAddress = fromaddress;
		}
		public String getCompanyId(){
			return  this.driverId;
		}
		public void setCompanyId(String driverid){
			this.driverId = driverid;
		}
		public String getAuthKey(){
			return  this.authKey;
		}
		public void setAuthKey(String authkey){
			this.authKey = authkey;
		}
	
}
