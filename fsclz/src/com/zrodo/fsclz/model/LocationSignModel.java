package com.zrodo.fsclz.model;

public class LocationSignModel {

	private String sId;
	private String addr;
	private String addrLati;
	private String addrLongi;
	private String time;
	private String locImgTitle;
	private String locImgPath;
	private String mapViewBase64;
	
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAddrLati() {
		return addrLati;
	}
	public void setAddrLati(String addrLati) {
		this.addrLati = addrLati;
	}
	public String getAddrLongi() {
		return addrLongi;
	}
	public void setAddrLongi(String addrLongi) {
		this.addrLongi = addrLongi;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getLocImgTitle() {
		return locImgTitle;
	}
	
	public void setLocImgTitle(String locImgTitle) {
		this.locImgTitle = locImgTitle;
	}
	public String getLocImgPath() {
		return locImgPath;
	}
	public void setLocImgPath(String locImgPath) {
		this.locImgPath = locImgPath;
	}
	public String getMapViewBase64() {
		return mapViewBase64;
	}
	public void setMapViewBase64(String mapViewBase64) {
		this.mapViewBase64 = mapViewBase64;
	}
}
