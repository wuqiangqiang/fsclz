package com.zrodo.fsclz.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zrodo.fsclz.model.LocationSignModel;
import com.zrodo.fsclz.service.CacheData;
import com.zrodo.fsclz.service.Constant;
import com.zrodo.fsclz.service.LocationService;
import com.zrodo.fsclz.service.Provider;
import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.service.ZrodoProviderImp;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.BitmapUtil;
import com.zrodo.fsclz.utils.FileUtil;
import com.zrodo.fsclz.utils.ZRDUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationSignInActivity extends BaseActivity {

	private TextView sampleTime = null;
	private TextView locInfo = null;
	private TextView longilati = null;
	private MapView mMapView = null;
	private Button btnRefresh = null;
	private BaiduMap mBaiduMap;
	private Button btnSignIn = null;
	private Provider mProvider;
	private LocationSignModel  lsmodel = new LocationSignModel();
	private LocationService locationService;
	int windowsWidth;
	private FinalHttp finalHttp;
	private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setView("定位签到", R.layout.location_sign_in);
		WindowManager wm = this.getWindowManager();
		windowsWidth = wm.getDefaultDisplay().getWidth() - 20;
		finalHttp = new FinalHttp();
        finalHttp.configTimeout(10 * 1000);
        FileUtil.MakeDirs(Constant.SIGN_IMG_DIR);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		sampleTime = (TextView) findViewById(R.id.system_time);
		locInfo = (TextView) findViewById(R.id.loc_sign_address);
		longilati = (TextView) findViewById(R.id.loc_sign_longilati);
		mMapView = (MapView) findViewById(R.id.loc_sign_mapView);
		btnRefresh = (Button) findViewById(R.id.btn_refresh);
		btnSignIn = (Button) findViewById(R.id.btn_sign);
		showSampleTime();
		btnSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// 地图截屏
				mBaiduMap.snapshot(new SnapshotReadyCallback() {
					
					@Override
					public void onSnapshotReady(Bitmap snapt) {
						//地图保存到本地
						//lsmodel.setMapViewBase64(BitmapUtil.convertToBase64(snapt));
						//String strDate = String.valueOf(new Date().getTime());
						//lsmodel.setLocImgTitle(strDate);
						lsmodel.setLocImgPath(Constant.SIGN_IMG_DIR + String.valueOf(new Date().getTime()) + ".jpg");
						FileUtil.savePic(lsmodel.getLocImgPath(), snapt, Bitmap.CompressFormat.PNG);						
					}
					
				});
				
				//上传签到信息
				//uploadSignInfo();
				mProvider= ZrodoProviderImp.getmProvider();		        
		        try {
					String postURl = mProvider.postSignIn().toString();			
					uploadSignInfo(postURl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	 
	protected void uploadSignInfo(String JSONSignInUrl) {
		
		AjaxParams params = constructAjaxParamsObj();
		finalHttp.post(JSONSignInUrl, params, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					 dismissProgressDialog();
					 showToast(ActivityUtil.MsgDuration.LONG, R.string.bdlocation_serverConection_failed);
				}
				
				@Override
				public void onStart() {
					super.onStart();
					showProgressDialog(R.string.bdlocation_subminting);
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					if(!TextUtils.isEmpty(t)){
						try {
							JSONObject obj = new JSONObject(t);
							if("1".equals(obj.optString("code"))){
								dismissProgressDialog();
								showAlertDialog(0);		 						
							} else {
								dismissProgressDialog();
								showAlertDialog(1);
							}
						} catch (Exception e) {
							 throw new RuntimeException("json parser error");
						}
					}
					
				}
				
			});
	}

	private AjaxParams constructAjaxParamsObj() {
		 AjaxParams params = new AjaxParams();
	     try {
	    	 params.put("userid",CacheData.loginInfo.getUserid());
	    	 params.put("deptid",CacheData.loginInfo.getDeptid());
    	     params.put("address",lsmodel.getAddr());
    	     params.put("latitude",lsmodel.getAddrLati());
    	     params.put("longitude",lsmodel.getAddrLongi());
    	     params.put("date",lsmodel.getTime());
             params.put("img", new File(lsmodel.getLocImgPath()));	    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	     
	    return  params;
	}

	private void showAlertDialog(final int flag) {
		// TODO Auto-generated method stub
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
		params.width = windowsWidth;
		dlg.getWindow().setAttributes(params);
		Window window = dlg.getWindow();
		
		window.setContentView(R.layout.alert_dialog);
		ImageView imgHead = (ImageView) window.findViewById(R.id.img_head);
		TextView  txtHead = (TextView) window.findViewById(R.id.txt_head);
		TextView  txtBody = (TextView) window.findViewById(R.id.txt_body);
		Button btnBody = (Button) window.findViewById(R.id.btn_body);
		  if(flag == 1){
			imgHead.setBackgroundResource(R.drawable.failed);
			txtHead.setText("警告");
			txtBody.setText("签到失败");
			btnBody.setText("重新签到");
		  }
		btnBody.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(flag == 1){
					dlg.cancel();
				} else {
					dlg.cancel();
					finish();
				}
			}
		});
	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//-----baidu Map config----
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		// ---location config ---
		locationService = ((ZRDApplication) getApplication()).locationService; 
		
		//注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			LocationClientOption mOption = locationService.getDefaultLocationClientOption();
			mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving); 
			mOption.setCoorType("bd09ll");
			locationService.setLocationOption(mOption);
			//locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		} else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}
		//获取locationservice实例
		locationService.registerListener(mListener);
		// 定位SDK		
		locationService.start();
	}

	@Override
	protected void onStop() {
		//注销掉监听
		locationService.unregisterListener(mListener); 
		//停止定位服务
		locationService.stop();
		super.onStop();
	}
	
	/**
	 * 定位结果回调，重写onReceiveLocation方法
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@SuppressLint("DefaultLocale") @Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();
			
			if (location != null) {
				if(code != BDLocation.TypeServerError){
					StringBuffer sb = new StringBuffer(256);
					
					/**
					 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
					 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
					 */
					
					String addr="",lati="",longi="";
					if(code==BDLocation.TypeGpsLocation){
				            addr=location.getAddrStr();
				            lati = String.format("%.6f", location.getLatitude());
				            longi = String.format("%.6f",location.getLongitude());
				    }
				    if(code==BDLocation.TypeNetWorkLocation){
				            addr=location.getAddrStr();
				            lati = String.format("%.6f", location.getLatitude());
				            longi = String.format("%.6f",location.getLongitude());
				    }
					sb.append(getString(R.string.bdlocation_latitude_info));
					sb.append(location.getLatitude());
					sb.append(getString(R.string.bdlocation_longitude_info));
					sb.append(location.getLongitude());
					locInfo.setText(addr);
			        longilati.setText(sb.toString());
			        lsmodel.setAddr(addr);
			        lsmodel.setAddrLati(lati);
			        lsmodel.setAddrLongi(longi);
				} 
				if(location.getLocType() == 161 || location.getLocType() == 66){
					Message locMsg = locHander.obtainMessage();
					Bundle locData;
					locData = Algorithm(location);
					if (locData != null) {
						locData.putParcelable("loc", location);
						locMsg.setData(locData);
						locHander.sendMessage(locMsg);
					}
				}			
			}
		}
	};

	private void showSampleTime() {
		// TODO Auto-generated method stub
		//String sTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	    sampleTime.setText(ZRDUtils.getTimes("yyyy-MM-dd HH:mm:ss"));
	    lsmodel.setTime(ZRDUtils.getTimes("yyyy-MM-dd HH:mm:ss"));
	}
	
	/***
	 * @param BDLocation
	 * @return Bundle
	 * @author Baidu
	 */
	private Bundle Algorithm(BDLocation location) {
		Bundle locData = new Bundle();
		double curSpeed = 0;
		if (locationList.isEmpty() || locationList.size() < 2) {
			LocationEntity temp = new LocationEntity();
			temp.location = location;
			temp.time = System.currentTimeMillis();
			locData.putInt("iscalculate", 0);
			locationList.add(temp);
		} else {
			if (locationList.size() > 5)
				locationList.removeFirst();
			double score = 0;
			for (int i = 0; i < locationList.size(); ++i) {
				LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
						locationList.get(i).location.getLongitude());
				LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
				double distance = DistanceUtil.getDistance(lastPoint, curPoint);
				curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
				score += curSpeed * ZRDUtils.EARTH_WEIGHT[i];
			}
			if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
				location.setLongitude(
						(locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
								/ 2);
				location.setLatitude(
						(locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
								/ 2);
				locData.putInt("iscalculate", 1);
			} else {
				locData.putInt("iscalculate", 0);
			}
			LocationEntity newLocation = new LocationEntity();
			newLocation.location = location;
			newLocation.time = System.currentTimeMillis();
			locationList.add(newLocation);

		}
		return locData;
	}
	
	/***
	 * 接收定位结果消息，并显示在地图上
	 */
	private Handler locHander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				BDLocation location = msg.getData().getParcelable("loc");
				int iscal = msg.getData().getInt("iscalculate");
				if (location != null) {
					LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
					// 构建Marker图标
					BitmapDescriptor bitmap = null;
					if (iscal == 0) {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark); // 非推算结果
					} else {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark); // 推算结果
					}

					// 构建MarkerOption，用于在地图上添加Marker
					OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
					// 在地图上添加Marker，并显示
					mBaiduMap.addOverlay(option);
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	};
	
	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		btnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mBaiduMap != null)
					mBaiduMap.clear();
			}
		});
	}

	/**
	 * 封装定位结果和时间的实体类
	 * @author Baidu
	 */
	class LocationEntity {
		BDLocation location;
		long time;
	}
}
