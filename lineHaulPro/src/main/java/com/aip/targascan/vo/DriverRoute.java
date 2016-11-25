package com.aip.targascan.vo;

public class DriverRoute {

	String id;
	String driver_route_name;

	public DriverRoute() {
		super();
	}

	public DriverRoute(String id, String driver_route_name) {
		super();
		this.id = id;
		this.driver_route_name = driver_route_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDriver_route_name() {
		return driver_route_name;
	}

	public void setDriver_route_name(String driver_route_name) {
		this.driver_route_name = driver_route_name;
	}

}
