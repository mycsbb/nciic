package com.csrc.nciic.model;

public class Authinfo {
	private String JSESSIONID;
	private String fAvfGpbE26;
	private String Auth_Content;
	public Authinfo() {
		super();
		JSESSIONID = "";
		fAvfGpbE26 = "";
		Auth_Content = "";
	}
	public Authinfo(String jSESSIONID, String fAvfGpbE26, String auth_Content) {
		super();
		JSESSIONID = jSESSIONID;
		this.fAvfGpbE26 = fAvfGpbE26;
		Auth_Content = auth_Content;
	}
	public String getJSESSIONID() {
		return JSESSIONID;
	}
	public void setJSESSIONID(String jSESSIONID) {
		JSESSIONID = jSESSIONID;
	}
	public String getfAvfGpbE26() {
		return fAvfGpbE26;
	}
	public void setfAvfGpbE26(String fAvfGpbE26) {
		this.fAvfGpbE26 = fAvfGpbE26;
	}
	public String getAuth_Content() {
		return Auth_Content;
	}
	public void setAuth_Content(String auth_Content) {
		Auth_Content = auth_Content;
	}
	
}
