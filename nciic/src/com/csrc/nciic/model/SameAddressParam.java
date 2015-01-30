package com.csrc.nciic.model;

public class SameAddressParam {
	@Override
	public String toString() {
		return "SameAddressParam [pk=" + pk + ", val=" + val + ", bm=" + bm
				+ ", id=" + id + ", sd=" + sd + "]";
	}

	private String pk;
	private String val;
	private String bm;
	private String id;
	private String sd;
	public SameAddressParam() {
		super();
		this.pk = "";
		this.val = "";
		this.bm = "";
		this.id = "";
		this.sd = "";
	}
	public SameAddressParam(String pk, String val, String bm, String id,
			String sd) {
		super();
		this.pk = pk;
		this.val = val;
		this.bm = bm;
		this.id = id;
		this.sd = sd;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSd() {
		return sd;
	}
	public void setSd(String sd) {
		this.sd = sd;
	}
	
	public boolean is_empty() {
		return this.pk == null || this.pk.trim().equals("") 
				|| this.val == null || this.val.trim().equals("")
				|| this.bm == null || this.bm.trim().equals("")
				|| this.id == null || this.id.trim().equals("")
				|| this.sd == null || this.sd.trim().equals("");
	}
}
