package com.zrodo.fsclz.service;

import java.util.LinkedList;
import java.util.List;
import com.baidu.mapapi.SDKInitializer;
import com.zrodo.fsclz.utils.ZRDUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

@SuppressLint("DefaultLocale") 
public class ZRDApplication extends Application {

	private static Context mContext;
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ZRDApplication instance;
	private CacheData mCache;
    private String requestUrl = "";
    
    //百度定位相关
    public LocationService locationService;
    public Vibrator mVibrator;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		mCache = new CacheData();
		ZRDUtils.setLaunched(this, false);
		
		/***
         * 初始化定位sdk
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());  
	}
		
	public static Context getContext() {
		return mContext;
	}

	public CacheData getCacheData(){
		return mCache;	
	}
	
	//POST传输方式
	/*public String getRequestUrl(String mothd) {
		if(!mothd.startsWith("/"))
			mothd = "/" + mothd;
		
		if(requestUrl.toLowerCase().startsWith("http")){
			return requestUrl + mothd;
		} else {
			return "http://" +requestUrl + mothd;
		}
	}*/
	
	//GET传输方式
	public String getRequestUrl() {		
		if(requestUrl.toLowerCase().startsWith("http")){
			return requestUrl;
		} else {
			return "http://" +requestUrl;
		}
	}

	public void setRequestUrl(String requestUrl) {
		/*if(requestUrl.endsWith("/")){
			requestUrl = requestUrl.substring(0, requestUrl.length()-1);
		}*/
		this.requestUrl = requestUrl;
	}

	public static ZRDApplication getInstance(){
		if(instance == null){
			instance = new ZRDApplication();
		}	
		return instance;
	}
	
	public void addActivity(Activity act){
		activityList.add(act);
	}
	
	public void delActivity(){
		for(Activity act:activityList){
			act.finish();
		}
	}
	
	public void exit(){		
		for(Activity act:activityList){
			act.finish();
		}		
		System.exit(0);
	}
}
