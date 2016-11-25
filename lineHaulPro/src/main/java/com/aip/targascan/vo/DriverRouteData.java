package com.aip.targascan.vo;

public class DriverRouteData {

	String driver_route_id = "";
	String driver_route_timestatus_id = "";
	String driver_id = "";
	String timestamp = "";
	String gps_lat = "";
	String gps_log = "";

	public DriverRouteData() {
		super();
	}

	public DriverRouteData(String driver_route_id, String driver_route_timestatus_id, String driver_id, String timestamp, String gps_lat,
			String gps_log) {
		super();
		this.driver_route_id = driver_route_id;
		this.driver_route_timestatus_id = driver_route_timestatus_id;
		this.driver_id = driver_id;
		this.timestamp = timestamp;
		this.gps_lat = gps_lat;
		this.gps_log = gps_log;
	}

	public String getDriver_route_id() {
		return driver_route_id;
	}

	public void setDriver_route_id(String driver_route_id) {
		this.driver_route_id = driver_route_id;
	}

	public String getDriver_route_timestatus_id() {
		return driver_route_timestatus_id;
	}

	public void setDriver_route_timestatus_id(String driver_route_timestatus_id) {
		this.driver_route_timestatus_id = driver_route_timestatus_id;
	}

	public String getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getGps_lat() {
		return gps_lat;
	}

	public void setGps_lat(String gps_lat) {
		this.gps_lat = gps_lat;
	}

	public String getGps_log() {
		return gps_log;
	}

	public void setGps_log(String gps_log) {
		this.gps_log = gps_log;
	}

	@Override
	public String toString() {
		return "DriverRouteData [driver_route_id=" + driver_route_id + ", driver_route_timestatus_id=" + driver_route_timestatus_id
				+ ", driver_id=" + driver_id + ", timestamp=" + timestamp + ", gps_lat=" + gps_lat + ", gps_log=" + gps_log + "]";
	}

}
