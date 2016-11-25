package com.aip.targascan.vo;


public class Cachedjob {
	//private variables
	private int id;
	private String _signatureName;
	private String _companyid;
	private String _chg1;
	private String _chg2;
	private String  _cartoon1;
	private String _redelivery;
	private String _to;
	private String _from;
	private String encodeSign;
	private String cachedDate;
	private String system;
	private String servId;

	// getting ID
	public String getSignatureName(){
		return this._signatureName;
	}
	public int getID(){
		return this.id;
	}

	public void setID(int id){
		this.id = id;
	}

	public String getCompanyID(){
		return this._companyid;
	}

	public void setCompanyID(String id){
		this._companyid = id;
	}

	public String getChg1(){
		return this._chg1;
	}

	public void setChg1(String chg1){
		this._chg1 = chg1;
	}
	
	public String getChg2(){
		return this._chg2;
	}

	public void setChg2(String chg2){
		this._chg2 = chg2;
	}
	
	public String getcartoon1(){
		return this._cartoon1;
	}
	
	public void setCartoon1(String cartoon1 ){
		this._cartoon1 =cartoon1 ;
	}

	public String getRedelivery(){
		return  this._redelivery;
	}
	
	public void setRedelivery(String redelivery){
		this._redelivery = redelivery;
	}
	
	public String getTo(){
		return  this._to;
	}
	
	public void setTo(String to){
		this._to = to;
	}
	
	public String getFrom(){
		return  this._from;
	}
	
	public void setFrom(String from){
		this._from = from;
	}
	
	public void setSignatureName(String signature) {
		this._signatureName = signature;
	}
	public String getEncodeSign() {
		return encodeSign;
	}
	public void setEncodeSign(String encodeSign) {
		this.encodeSign = encodeSign;
	}
	public String getCachedDate() {
		return cachedDate;
	}
	public void setCachedDate(String cachedDate) {
		this.cachedDate = cachedDate;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getServId() {
		return servId;
	}
	public void setServId(String servId) {
		this.servId = servId;
	}
	
	@Override
	public String toString() {
		return "Cachedjob [id=" + id + ", _signatureName=" + _signatureName + ", _companyid=" + _companyid + ", _chg1=" + _chg1
				+ ", _chg2=" + _chg2 + ", _cartoon1=" + _cartoon1 + ", _redelivery=" + _redelivery + ", _to=" + _to + ", _from=" + _from
				+ ", encodeSign=" + encodeSign + ", cachedDate=" + cachedDate + ", system=" + system + ", servId=" + servId + "]";
	}
	
}


