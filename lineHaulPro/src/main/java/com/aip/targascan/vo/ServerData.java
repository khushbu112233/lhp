package com.aip.targascan.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerData implements Parcelable {

	String date;
	String delivery_name;
	String delivery_address;
	String carton_num;
	String delivery_address2;
	String city;
	String zip;
	String driver_id;
	String app_ordering;

	public ServerData() {
		super();
	}

	public ServerData(String date, String delivery_name,
			String delivery_address, String carton_num,
			String delivery_address2, String city, String zip,
			String driver_id, String app_ordering) {
		super();
		this.date = date;
		this.delivery_name = delivery_name;
		this.delivery_address = delivery_address;
		this.carton_num = carton_num;
		this.delivery_address2 = delivery_address2;
		this.city = city;
		this.zip = zip;
		this.driver_id = driver_id;
		this.app_ordering = app_ordering;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDelivery_name() {
		return delivery_name;
	}

	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}

	public String getDelivery_address() {
		return delivery_address;
	}

	public void setDelivery_address(String delivery_address) {
		this.delivery_address = delivery_address;
	}

	public String getCarton_num() {
		return carton_num;
	}

	public void setCarton_num(String carton_num) {
		this.carton_num = carton_num;
	}

	public String getDelivery_address2() {
		return delivery_address2;
	}

	public void setDelivery_address2(String delivery_address2) {
		this.delivery_address2 = delivery_address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	public String getApp_ordering() {
		return app_ordering;
	}

	public void setApp_ordering(String app_ordering) {
		this.app_ordering = app_ordering;
	}

	@Override
	public String toString() {
		return "ServerData [date=" + date + ", delivery_name=" + delivery_name
				+ ", delivery_address=" + delivery_address + ", carton_num="
				+ carton_num + ", delivery_address2=" + delivery_address2
				+ ", city=" + city + ", zip=" + zip + ", driver_id="
				+ driver_id + ", app_ordering=" + app_ordering + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeString(delivery_name);
		dest.writeString(delivery_address);
		dest.writeString(carton_num);
		dest.writeString(delivery_address2);
		dest.writeString(city);
		dest.writeString(zip);
		dest.writeString(driver_id);
		dest.writeString(app_ordering);
	}

	public ServerData(Parcel in) {
		date = in.readString();
		delivery_name = in.readString();
		delivery_address = in.readString();
		carton_num = in.readString();
		delivery_address2 = in.readString();
		city = in.readString();
		zip = in.readString();
		driver_id = in.readString();
		app_ordering = in.readString();
	}

	public static final Parcelable.Creator<ServerData> CREATOR = new Parcelable.Creator<ServerData>() {
		public ServerData createFromParcel(Parcel in) {
			return new ServerData(in);
		}

		public ServerData[] newArray(int size) {
			return new ServerData[size];
		}
	};

}
