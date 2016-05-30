package com.zrodo.fsclz.model;

import java.io.Serializable;

public class VersionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url;
	private String version;
	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
		
}
