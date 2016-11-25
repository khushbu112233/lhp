package com.aip.targascan.vo;

public class DailyOrder {

	String _carton_num;
	String _co_type;

	// Empty constructor
	public DailyOrder() {

	}

	// constructor
	public DailyOrder(String _carton_num, String _co_type) {
		this._carton_num = _carton_num;
		this._co_type = _co_type;
	}

	// getting ID
	public String getCartonNumText() {
		return this._carton_num;
	}

	// setting id
	public void setCartonNumText(String _carton_num) {
		this._carton_num = _carton_num;
	}

	public String getCoTypeText() {
		return this._co_type;
	}

	// setting id
	public void setCoTypeText(String _co_type) {
		this._co_type = _co_type;
	}

}
