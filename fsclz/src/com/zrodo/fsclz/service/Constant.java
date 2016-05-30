package com.zrodo.fsclz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zrodo.fsclz.activity.BDLocationActivity;
import com.zrodo.fsclz.activity.DataAnalysisActivity;
import com.zrodo.fsclz.activity.DataStatisticActivity;
import com.zrodo.fsclz.activity.InfoQueryActivity;
import com.zrodo.fsclz.activity.LocationSignInActivity;
import com.zrodo.fsclz.activity.PersonalCenterActivity;
import com.zrodo.fsclz.activity.R;
import com.zrodo.fsclz.model.GenreModel;

import android.os.Environment;

public class Constant {

	// 常量。。
	private static String SDCARD_ABS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static String SDCARD_PATH = Environment.getExternalStorageDirectory()+"/";
	public static final String down_path = SDCARD_PATH +"download";
	public static final String SIGN_IMG_DIR = SDCARD_ABS_PATH+ "/loc_sign_pic/";
	private static ArrayList<GenreModel> authorityList = new ArrayList<GenreModel>();
	
	//首页九宫格菜单选择
	private static List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	public static Map<Object,Class<?>> classmap=new HashMap<Object,Class<?>>();//图标与activity的对应map
	
    static {
	        classmap.put(R.drawable.sample, BDLocationActivity.class);
	        classmap.put(R.drawable.statistic, DataStatisticActivity.class);
	        classmap.put(R.drawable.analysis, DataAnalysisActivity.class);
	        classmap.put(R.drawable.source, InfoQueryActivity.class);
	        classmap.put(R.drawable.person, PersonalCenterActivity.class);
	        classmap.put(R.drawable.location, LocationSignInActivity.class);
	}

    //样本采集-权限
	public static void DataList(ArrayList<String> roleList) {

		String[] genreName = {"农产品","水产品","畜产品"};
		String[] roleName= {"F01","F02","F03"};
		
		for(int i =1 ;i<=genreName.length; i++){
			GenreModel authorihModel = new GenreModel();
			if(roleList.contains(roleName[i-1])){
				authorihModel.setObjectname(genreName[i-1]);
				authorihModel.setObjectid(i+"");
				authorityList.add(authorihModel);
			}
		}
	}
	
	
	    
	 /**
	  * 初始化九宫图数据，硬编码方式，后期可以采用服务端配置的方式灵活获取图片
	  */
	 public static List<Map<String,Object>> handMap(ArrayList<String> roleSubIdList){
	        if(list.size()>0){
	            list.clear();
	        }
	        Map<String,Object> sampleMap=new HashMap<String,Object>();
	        sampleMap.put("itemimag", R.drawable.sample);
	        sampleMap.put("itemtext", "采样管理");
	        sampleMap.put("role", roleSubIdList.contains("F"));
	        list.add(sampleMap);

	        Map<String,Object> statisticMap=new HashMap<String,Object>();
	        statisticMap.put("itemimag", R.drawable.statistic);
	        statisticMap.put("itemtext", "数据统计");
	        statisticMap.put("role", roleSubIdList.contains("4"));
	        list.add(statisticMap);


	        Map<String,Object> analyMap=new HashMap<String,Object>();
	        analyMap.put("itemimag", R.drawable.analysis);
	        analyMap.put("itemtext", "数据分析");
	        analyMap.put("role", roleSubIdList.contains("5"));
	        list.add(analyMap);

	        Map<String,Object> warningMap=new HashMap<String,Object>();
	        warningMap.put("itemimag", R.drawable.warning);
	        warningMap.put("itemtext", "风险预警");
	        warningMap.put("role", roleSubIdList.contains("7"));
	        list.add(warningMap);

	        Map<String,Object> sourceMap=new HashMap<String,Object>();
	        sourceMap.put("itemimag", R.drawable.source);
	        sourceMap.put("itemtext", "溯源查询");
	        sourceMap.put("role", true);
	        list.add(sourceMap);

	        Map<String,Object> videoMap=new HashMap<String,Object>();
	        videoMap.put("itemimag", R.drawable.video);
	        videoMap.put("itemtext", "视频监控");
	        videoMap.put("role", false);
	        list.add(videoMap);

	        Map<String,Object> locationMap=new HashMap<String,Object>();
	        locationMap.put("itemimag", R.drawable.location);
	        locationMap.put("itemtext", "定位签到");
	        locationMap.put("role", true);
	        list.add(locationMap);

	        Map<String,Object> messageMap=new HashMap<String,Object>();
	        messageMap.put("itemimag", R.drawable.message);
	        messageMap.put("itemtext", "信息中心");
	        messageMap.put("role", roleSubIdList.contains("D"));
	        list.add(messageMap);


	        Map<String,Object> personMap=new HashMap<String,Object>();
	        personMap.put("itemimag", R.drawable.person);
	        personMap.put("itemtext", "个人中心");
	        personMap.put("role", true);
	        list.add(personMap);

	        return list;
	    }

	public static List<GenreModel> getAuthorityList() {
		return authorityList;
	}

	public static void clearAuthorityList() {
		authorityList.clear();
	}
	
}
