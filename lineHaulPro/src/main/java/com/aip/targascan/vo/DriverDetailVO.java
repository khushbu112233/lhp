package com.aip.targascan.vo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DriverDetailVO {

	@SerializedName("data")
	private List<DataVO> dataVOs;
	
	public List<DataVO> getDataVOs() {
		return dataVOs;
	}

	public void setDataVOs(List<DataVO> dataVOs) {
		this.dataVOs = dataVOs;
	}

	public class DataVO{
		 String color;
         String count_all;
         String co_type;
         String work_order_num;
         String pickup;
         String delivery_address;
         String city;
         String zip;
         
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getCount_all() {
			return count_all;
		}
		public void setCount_all(String count_all) {
			this.count_all = count_all;
		}
		public String getCo_type() {
			return co_type;
		}
		public void setCo_type(String co_type) {
			this.co_type = co_type;
		}
		public String getWork_order_num() {
			return work_order_num;
		}
		public void setWork_order_num(String work_order_num) {
			this.work_order_num = work_order_num;
		}
		public String getPickup() {
			return pickup;
		}
		public void setPickup(String pickup) {
			this.pickup = pickup;
		}
		public String getDelivery_address() {
			return delivery_address;
		}
		public void setDelivery_address(String delivery_address) {
			this.delivery_address = delivery_address;
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

         
	}
}
