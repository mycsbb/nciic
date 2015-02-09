package com.csrc.nciic.model;

public class IdentifyInfo {
	private String identify_no;
	private String zh_name;
	public IdentifyInfo() {
		super();
		this.identify_no = "";
		this.zh_name = "";
	}
	public IdentifyInfo(String identify_no, String zh_name) {
		super();
		this.identify_no = identify_no;
		this.zh_name = zh_name;
	}
	public String getIdentify_no() {
		return identify_no;
	}
	public void setIdentify_no(String identify_no) {
		this.identify_no = identify_no;
	}
	public String getZh_name() {
		return zh_name;
	}
	public void setZh_name(String zh_name) {
		this.zh_name = zh_name;
	}
	@Override
	public String toString() {
		return "IdentifyInfo [identify_no=" + identify_no + ", zh_name="
				+ zh_name + "]";
	}
	
	public boolean isEmpty() {
		return this.identify_no.trim().equals("") 
				|| this.zh_name.trim().equals("");
	}
}
