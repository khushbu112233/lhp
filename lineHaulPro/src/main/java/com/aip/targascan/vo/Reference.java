package com.aip.targascan.vo;

public class Reference {

	String _co_type;
	String _reference_number;
	String _comments;

	public String get_co_type() {
		return _co_type;
	}

	public void set_co_type(String _co_type) {
		this._co_type = _co_type;
	}

	public String get_reference_number() {
		return _reference_number;
	}

	public void set_reference_number(String _reference_number) {
		this._reference_number = _reference_number;
	}

	public String get_comments() {
		return _comments;
	}

	public void set_comments(String _comments) {
		this._comments = _comments;
	}

	@Override
	public String toString() {
		return "Reference [_co_type=" + _co_type + ", _reference_number=" + _reference_number + ", _comments=" + _comments + "]";
	}

}
