package com.aip.targascan.vo;

public class ReferenceNo {
	private String co_type;

	private String reference_num;

	public String getCo_type() {
		return co_type;
	}

	public void setCo_type(String co_type) {
		this.co_type = co_type;
	}

	public String getReference_num() {
		return reference_num;
	}

	public void setReference_num(String reference_num) {
		this.reference_num = reference_num;
	}

	@Override
	public String toString() {
		return "[co_type = " + co_type + ", reference_num = " + reference_num + "]";
	}
}
