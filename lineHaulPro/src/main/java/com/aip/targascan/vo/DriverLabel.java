package com.aip.targascan.vo;

public class DriverLabel {

	String driver_id;
	String carton_num;
	String barcode;
	String cust_name;

	public DriverLabel() {
		super();
	}

	public DriverLabel(String driver_id, String carton_num, String barcode) {
		super();
		this.driver_id = driver_id;
		this.carton_num = carton_num;
		this.barcode = barcode;
	}

	public String getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	public String getCarton_num() {
		return carton_num;
	}

	public void setCarton_num(String carton_num) {
		this.carton_num = carton_num;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

}
