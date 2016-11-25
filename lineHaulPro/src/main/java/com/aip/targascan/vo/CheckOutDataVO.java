package com.aip.targascan.vo;

import java.io.Serializable;
import java.util.List;

public class CheckOutDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String showForm;
	private String show_application;
	private List<ScrollToBottom> scrollToBottom;
	public class ScrollToBottom implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String date;
		String carton_num;
		String co_type;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getCarton_num() {
			return carton_num;
		}
		public void setCarton_num(String carton_num) {
			this.carton_num = carton_num;
		}
		public String getCo_type() {
			return co_type;
		}
		public void setCo_type(String co_type) {
			this.co_type = co_type;
		}
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getShowForm() {
		return showForm;
	}
	public void setShowForm(String showForm) {
		this.showForm = showForm;
	}
	public String getShow_application() {
		return show_application;
	}
	public void setShow_application(String show_application) {
		this.show_application = show_application;
	}
	public List<ScrollToBottom> getScrollToBottom() {
		return scrollToBottom;
	}
	public void setScrollToBottom(List<ScrollToBottom> scrollToBottom) {
		this.scrollToBottom = scrollToBottom;
	}
	
}