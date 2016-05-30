package com.zrodo.fsclz.model;

import java.io.Serializable;

public class RoleModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String fkSubId;
	private String subId;
	
	public RoleModel() {}
	
	public RoleModel(String fkSubId, String subId) {
		this.fkSubId = fkSubId;
		this.subId = subId;
	}
	
	public String getFkSubId() {
		return fkSubId;
	}
	public void setFkSubId(String fkSubId) {
		this.fkSubId = fkSubId;
	}
	public String getSubId() {
		return subId;
	}
	public void setSubId(String subId) {
		this.subId = subId;
	}
	
}
