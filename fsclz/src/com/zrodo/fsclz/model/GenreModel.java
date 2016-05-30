package com.zrodo.fsclz.model;

import java.io.Serializable;

public class GenreModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String objectname;
	private String typeid;
	private String objectid;

	public GenreModel() {
	}

	public GenreModel(String objectname, String typeid, String objectid) {
		this.objectname = objectname;
		this.typeid = typeid;
		this.objectid = objectid;
	}

	public String getObjectname() {
		return objectname;
	}

	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
}
