package com.aip.targascan.vo;

public class CompanyRejection {

	String master_company_id;
	String editing;

	public CompanyRejection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompanyRejection(String master_company_id, String editing) {
		super();
		this.master_company_id = master_company_id;
		this.editing = editing;
	}

	public String getMaster_company_id() {
		return master_company_id;
	}

	public void setMaster_company_id(String master_company_id) {
		this.master_company_id = master_company_id;
	}

	public String getEditing() {
		return editing;
	}

	public void setEditing(String editing) {
		this.editing = editing;
	}

}
