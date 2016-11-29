package com.aip.targascan.vo;

public class DriverRouteTimeStatus {

	String id;
	String place_name;

	public DriverRouteTimeStatus() {
		super();
	}

	public DriverRouteTimeStatus(String id, String place_name) {
		super();
		this.id = id;
		this.place_name = place_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlace_name() {
		return place_name;
	}

	public void setPlace_name(String place_name) {
		this.place_name = place_name;
	}

}
