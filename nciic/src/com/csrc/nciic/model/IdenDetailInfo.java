package com.csrc.nciic.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdenDetailInfo {
	private String identify_no;
	private String zh_name;
	private String used_name;
	private String sex;
	private String nation;
	private Date birth;
	private String belonged_unit;
	private String belonged_county;
	private String origin_place;
	private String address;
	private String service_place;
	private String marry_status;
	private String education;
	private String img;
	
	public IdenDetailInfo() {
		super();
		this.identify_no = "";
		this.zh_name = "";
		this.used_name = "";
		this.sex = "";
		this.nation = "";
		this.birth = new Date();
		this.belonged_unit = "";
		this.belonged_county = "";
		this.origin_place = "";
		this.address = "";
		this.service_place = "";
		this.marry_status = "";
		this.education = "";
		this.img = "";
	}
	
	public IdenDetailInfo(String identify_no, String zh_name, String used_name,
			String sex, String nation, Date birth, String belonged_unit,
			String belonged_county, String origin_place, String address,
			String service_place, String marry_status, String education,
			String img) {
		super();
		this.identify_no = identify_no;
		this.zh_name = zh_name;
		this.used_name = used_name;
		this.sex = sex;
		this.nation = nation;
		this.birth = birth;
		this.belonged_unit = belonged_unit;
		this.belonged_county = belonged_county;
		this.origin_place = origin_place;
		this.address = address;
		this.service_place = service_place;
		this.marry_status = marry_status;
		this.education = education;
		this.img = img;
	}
	
	public boolean isEmpty() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return this.identify_no.trim().equals("") 
				|| this.zh_name.trim().equals("")
				|| this.used_name.trim().equals("")
				|| this.sex.trim().equals("")
				|| this.nation.trim().equals("")
				|| sdf.format(this.birth).trim().equals("1970-00-00")
				|| this.belonged_unit.trim().equals("")
				|| this.belonged_county.trim().equals("")
				|| this.origin_place.trim().equals("")
				|| this.address.trim().equals("")
				|| this.service_place.trim().equals("")
				|| this.marry_status.trim().equals("")
				|| this.education.trim().equals("");
	}

	public String getService_place() {
		return service_place;
	}

	public void setService_place(String service_place) {
		this.service_place = service_place;
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
	
	public String getUsed_name() {
		return used_name;
	}

	public void setUsed_name(String used_name) {
		this.used_name = used_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getBelonged_unit() {
		return belonged_unit;
	}

	public void setBelonged_unit(String belonged_unit) {
		this.belonged_unit = belonged_unit;
	}

	public String getBelonged_county() {
		return belonged_county;
	}

	public void setBelonged_county(String belonged_county) {
		this.belonged_county = belonged_county;
	}

	public String getOrigin_place() {
		return origin_place;
	}

	public void setOrigin_place(String origin_place) {
		this.origin_place = origin_place;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}


	public String getMarry_status() {
		return marry_status;
	}


	public void setMarry_status(String marry_status) {
		this.marry_status = marry_status;
	}


	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Override
	public String toString() {
		return "IdenDetailInfo [identify_no=" + identify_no + ", zh_name="
				+ zh_name + ", used_name=" + used_name + ", sex=" + sex
				+ ", nation=" + nation + ", birth=" + birth
				+ ", belonged_unit=" + belonged_unit + ", belonged_county="
				+ belonged_county + ", origin_place=" + origin_place
				+ ", address=" + address + ", service_place=" + service_place
				+ ", marry_status=" + marry_status + ", education=" + education
				+ ", img=" + img + "]";
	}
	
}
