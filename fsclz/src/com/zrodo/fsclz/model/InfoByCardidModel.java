package com.zrodo.fsclz.model;

import java.io.Serializable;

public class InfoByCardidModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String result;
	private String detectdate;
	private String province;
	private String city;
	private String country;
	private String deptname;
	private String companyname;
	private String cardid;
	private String detecter;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDetectdate() {
		return detectdate;
	}
	public void setDetectdate(String detectdate) {
		this.detectdate = detectdate;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getDetecter() {
		return detecter;
	}
	public void setDetecter(String detecter) {
		this.detecter = detecter;
	}
	
}
