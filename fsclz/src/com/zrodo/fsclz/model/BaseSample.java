package com.zrodo.fsclz.model;

import java.io.File;

/**
 * Created by lenovo on 2016-02-24.
 */
public class BaseSample {
    private String userId;
    private String addr;
    private String addrLati;
    private String addrLongi;
    private String name;
    private String time;
    private String temperature;
    private String source;
    private String phontoBase64;
    private File videofile;
    private String type;
    private String objectid;
    private String sampleid;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String date) {
        this.time = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPhontoBase64() {
        return phontoBase64;
    }

    public void setPhontoBase64(String phontoBase64) {
        this.phontoBase64 = phontoBase64;
    }

	public File getVideofile() {
		return videofile;
	}

	public void setVideofile(File videofile) {
		this.videofile = videofile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public String getSampleid() {
		return sampleid;
	}

	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}
}
