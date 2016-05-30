package com.zrodo.fsclz.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zrodo.fsclz.model.BaseSample;
import com.zrodo.fsclz.model.GenreModel;
import com.zrodo.fsclz.model.LoginModel;
import com.zrodo.fsclz.model.RoleModel;
import com.zrodo.fsclz.model.sampleModel;
import com.zrodo.fsclz.pilingdatas.PilingData;
import com.zrodo.fsclz.service.CacheData;
import com.zrodo.fsclz.service.Constant;
import com.zrodo.fsclz.service.HttpClient;
import com.zrodo.fsclz.service.LocationService;
import com.zrodo.fsclz.service.OnResponseListener;
import com.zrodo.fsclz.service.Provider;
import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.service.ZrodoProviderImp;
import com.zrodo.fsclz.sqlite.RoleDAOImpl;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.BitmapUtil;
import com.zrodo.fsclz.utils.ZRDUtils;
import com.zrodo.fsclz.widget.ClearEditText;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2016-04-23.
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" }) 
public class BDLocationActivity extends BaseActivity implements OnClickListener{
    
	private TextView locInfo,longilati;
    private LinearLayout photoMenu;
    private LinearLayout videoMenu;
    private BaseSample sample=new BaseSample();
    private ImageView photoImg;
    private ImageView videoImg;
    private FinalHttp finalHttp;
    private String filPath = "";
    private TextView pic_sample_name,pic_sample_source,sample_time,sample_tempr,submitBtn;
    private TextView txtAuthority,txtDetectionObject,txtSampleObject;
    private RelativeLayout relAuthority ,relDetectionObject,relSampleObject;
    private static final int REQUEST_TACKPIC_CODE=1;
    private static final int REQUEST_TACKVIDEO_CODE=2;
    private String tmpFilename=Environment.getExternalStorageDirectory()+"/123456789.jpg";
    private int mPhotoSizeInKb = 200;
    private Provider mProvider;
    private String authorityID,detectionID,sampleID;
    private LocationService locationService;
    private ArrayList<GenreModel> genreDetectionObjectList;
    private ArrayList<GenreModel> genreSampleObjectList;
    private int windowsWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(getString(R.string.sample_collection),R.layout.location_info);
        initView();
        sample = new BaseSample();
        windowsWidth = getWindowManager().getDefaultDisplay().getWidth() - 20;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        finalHttp = new FinalHttp();
        finalHttp.configTimeout(10 * 1000);
    }
    
    /**
	 * 启动百度定位服务，在生命周期onStart时
	 */
    @Override
	protected void onStart() {		
		super.onStart();
		// ---location config ---
		locationService = ((ZRDApplication) getApplication()).locationService; 
		//获取locationservice实例
		locationService.registerListener(mListener);
		//注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		} else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}
		// 定位SDK
		loadDatas();
		locationService.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Constant.clearAuthorityList();
	}

	@Override
	protected void onStop() {
		//注销掉监听
		locationService.unregisterListener(mListener); 
		//停止定位服务
		locationService.stop();
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		
		Constant.DataList(getLocalDatas());
		super.onResume();
	}

	private void loadDatas() {
		
		ZRDUtils.showProgressDialog(this, "数据加载中....");
		mProvider = ZrodoProviderImp.getmProvider();
		try {
			String BaseDateURl = mProvider.getAllBasedata().toString();
			getJSONVolley(BaseDateURl);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	private void getJSONVolley(String baseDateURl) {
		
		HttpClient.getRequest(this, baseDateURl, new OnResponseListener() {
			
			ArrayList<GenreModel> genreList = null;
			@Override
			public void success(JSONObject result) {
	
				try {
					ZRDUtils.dismissProgressDialog();
					String genreObj = result.getString("objects");
					genreList = new Gson().fromJson(genreObj,new TypeToken<List<GenreModel>>() {}.getType());
					RoleDAOImpl roleDaoImpl = RoleDAOImpl.getmRoleDaoImpl(BDLocationActivity.this);
					if(roleDaoImpl.genreInsert(genreList) == 0){
						ZRDUtils.alert(BDLocationActivity.this, MsgDuration.SHORT, "样品入库失败，请联系管理员");
					}
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			@Override
			public void failure(String errorMessage) {
				ZRDUtils.dismissProgressDialog();
				ZRDUtils.alert(BDLocationActivity.this,ActivityUtil.MsgDuration.LONG, "数据加载失败");
			}
		});
		
	}

	private ArrayList<String> getLocalDatas(){
		RoleDAOImpl roleDaoImpl = new RoleDAOImpl(this);
		ArrayList<RoleModel> roleList = roleDaoImpl.getRoleList("F");
		ArrayList<String> roleSubIdList = new ArrayList<String>();
		for(int i =0 ;i< roleList.size() ;i++){
			String roleSubId = roleList.get(i).getSubId();
			roleSubIdList.add(roleSubId);			
		}
		return roleSubIdList;
	}

	protected void initView() {
        locInfo = (TextView) findViewById(R.id.location_addr);
        longilati=(TextView) findViewById(R.id.location_logilati);
        photoImg = (ImageView) findViewById(R.id.phontoImg);
        videoImg = (ImageView) findViewById(R.id.videoImg);
        photoMenu = (LinearLayout) findViewById(R.id.photo_menu);
        videoMenu = (LinearLayout) findViewById(R.id.video_menu);   
        pic_sample_name=(TextView) findViewById(R.id.pic_sample_name);
        pic_sample_source=(TextView) findViewById(R.id.pic_sample_source);
        sample_time=(TextView) findViewById(R.id.sample_time);
        sample_tempr=(TextView) findViewById(R.id.sample_temperature);
        submitBtn = (TextView) findViewById(R.id.add_menu);
        txtAuthority = (TextView) findViewById(R.id.txt_authority);
        txtDetectionObject = (TextView) findViewById(R.id.txt_detection_object);
        txtSampleObject = (TextView) findViewById(R.id.txt_sample_object);       		
        relAuthority = (RelativeLayout) findViewById(R.id.rel_authority);
        relAuthority.setOnClickListener(this);
        relDetectionObject = (RelativeLayout) findViewById(R.id.rel_detection_object);
        relSampleObject = (RelativeLayout) findViewById(R.id.rel_sample_object);
        relDetectionObject.setOnClickListener(this);
        relSampleObject.setOnClickListener(this);
        submitBtn.setText(getString(R.string.bdlocation_submit));      
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setOnClickListener(this);
        photoMenu.setOnClickListener(this);
        videoMenu.setOnClickListener(this);
        notifyAutoGenerate();
    }
	
    /**
	 * 定位结果回调，重写onReceiveLocation方法
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@SuppressLint("DefaultLocale") @Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();
			
			if (null != location && code != BDLocation.TypeServerError) {
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
		        sample.setAddr(addr);
		        sample.setAddrLati(lati);
		        sample.setAddrLongi(longi);
			}
		}
	};
	
	/**
	 * 视频图片回调
	 */
	@SuppressLint("NewApi") @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_TACKPIC_CODE){
            String photoPath=tmpFilename;
            File file=new File(photoPath);
            if(!file.exists()){
                return;
            }
            final Bitmap bm=ZRDUtils.convert2SmallBitmap(photoPath,mPhotoSizeInKb);
            final Dialog dialog=new Dialog(this,R.style.Dialog_FullScreen);
            View contentView = LayoutInflater.from(this).inflate(R.layout.activity_camera_preview, null);
            ImageView iv = (ImageView) contentView.findViewById(R.id.photo);
            iv.setImageBitmap(bm);
            Button btnOK = (Button) contentView.findViewById(R.id.btn_ok);
            Button btnCancel = (Button) contentView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    photoImg.setVisibility(View.VISIBLE);
                    sample.setPhontoBase64(BitmapUtil.convertToBase64(bm));
                  //继续缩小图片尺寸，显示在缩略图上
                    Bitmap bitmap=BitmapUtil.scaleBitmapSmall(bm,0.2f,0.2f);
                    photoImg.setImageBitmap(ZRDUtils.getRoundedCornerBitmap(bitmap, 8.0f));                    
                }
            });
            
            dialog.setContentView(contentView);
            dialog.show();
            file.delete();
            photoMenu.setVisibility(View.GONE);
        } else {
        	if (null != data) {
        		filPath = data.getExtras().getString("VIDEOFILE");
    			if (filPath.equals("") || filPath == null) {
    				return;
    			} else {				
    				Bitmap bmp = ZRDUtils.getRoundedCornerBitmap(BitmapUtil.getVideoThumbnail(filPath), 15.0f);
    				videoImg.setVisibility(View.VISIBLE);
    				videoMenu.setVisibility(View.GONE);
    				BitmapDrawable bd = new BitmapDrawable(getResources(), bmp);
    				videoImg.setBackground(bd);
    				Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.video_player);
    				videoImg.setImageBitmap(bp);
    				//視頻文件壓縮
    				/*try {
						ZipUtils.zip(filPath, "/storage/emulated/0/myvideo.zip");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				File file = new File("/storage/emulated/0/myvideo.zip");*/
    				File file = new File(filPath);
    				sample.setVideofile(file);			
    				videoImg.setOnClickListener(new OnClickListener() {   						
    						@Override
    				public void onClick(View arg0) {    							
    							startActivity(new Intent(BDLocationActivity.this, VideoPlayerActivity.class).putExtra("VIDEOPATH", filPath));
    						}
    				});   				
    			}
    		}
        }

    }

    /**
     * 图片显示时候通知自动生成样品部分信息
     * new Date()获取当前时间
     * simpleDateFormat 把获取的时间按照yyyy-MM-dd HH:mm样式的格式转换成字符串
     */
    private void notifyAutoGenerate(){    	
        String sTime=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());       
        sample_time.setText(sTime);      
        sample.setTime(sTime);
    }

    /**
     * 上传数据
     * 网络请求:Volley--修改为--afinal（测试阶段，无问题全部更新） 
     */
     private void finalUpload(String JSONDateUrl){
    	 //上传前先检查数据是否全
         LinearLayout layout=(LinearLayout) 
         findViewById(R.id.sample_info_parent);
         int count=layout.getChildCount();
         for(int i=0;i<count;i++){
             RelativeLayout rel=(RelativeLayout) layout.getChildAt(i);
             View editText = rel.getChildAt(2);
             if(editText instanceof ClearEditText){
                 if(TextUtils.isEmpty(((ClearEditText) editText).getText().toString())){
                     ((ClearEditText) editText).shakeAimation(5);
                     String tip=((TextView) rel.getChildAt(0)).getText().toString();
                     showToast(MsgDuration.LONG, tip+getString(R.string.bdlocation_not_null));
                     return;
                 }
             }
         }
         if(authorityID == null){
        	 showToast(MsgDuration.LONG, "种类不能为空");
        	 return;
         } 
         if(detectionID == null){
        	 showToast(MsgDuration.LONG, "检测对象不能为空");
        	 return;
         }
         if(sampleID == null){
        	 showToast(MsgDuration.LONG, "检测样品不能为空");
        	 return;
         }
         sample.setSource(pic_sample_source.getText().toString());
         sample.setName(pic_sample_name.getText().toString());
         sample.setTemperature(sample_tempr.getText().toString());
         sample.setType(authorityID);
         sample.setObjectid(detectionID);
         sample.setSampleid(sampleID);
       //finalHttp请求方式
         AjaxParams params = constructAjaxParamsObj();
         finalHttp.post(JSONDateUrl, params, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
					 dismissProgressDialog();
					 showToast(ActivityUtil.MsgDuration.LONG, R.string.bdlocation_serverConection_failed);
                     refresh();
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
     
     protected void showAlertDialog(final int flag) {
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
 			txtBody.setText("上传失败");
 			btnBody.setText("重新上传");
 		  } else {
  			txtBody.setText("上传成功");
  			btnBody.setText("确定");  
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
 				refresh();
 			}
 		});
	}

	//上传成功或失败后，刷新当前页面
     private void refresh() {  
         onCreate(null);
     }
   
    /**
     * 构建jsonobject(上传数据，暂时不用)
     */
          
     private AjaxParams constructAjaxParamsObj(){
         
    	     AjaxParams params = new AjaxParams();
    	     try {
    	    	 params.put("userid",CacheData.loginInfo.getUserid());
        	     params.put("sampleName",sample.getName());
        	     params.put("sampleSource",sample.getSource());
        	     params.put("location",sample.getAddr());
        	     params.put("latitude",sample.getAddrLati());
        	     params.put("longitude",sample.getAddrLongi());
        	     params.put("sampleTemp",sample.getTemperature());
        	     params.put("photo",sample.getPhontoBase64());
        	     params.put("video",sample.getVideofile());
        	     params.put("type",sample.getType());
        	     params.put("objectid",sample.getObjectid());
        	     params.put("sampleid",sample.getSampleid());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    	     
    	    return  params;
             
     } 
     
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_menu:
			//上传采集文本
			mProvider= ZrodoProviderImp.getmProvider();		        
	        try {
				String postURl = mProvider.postSamplePic().toString();			
				finalUpload(postURl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case R.id.photo_menu:
			//照片采集
			File tmpFile=new File(tmpFilename);
		    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
		    intent.putExtra("aspectX", 4);
		    intent.putExtra("aspectY", 3);
		    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 120 * 1024);
		    intent.putExtra("return-data", true);
		    startActivityForResult(intent, REQUEST_TACKPIC_CODE);  
		    break;
		
		case R.id.video_menu:
			//视频采集
			Intent intent1 = new Intent(this,VideoRecord.class);
			startActivityForResult(intent1, REQUEST_TACKVIDEO_CODE);
			break;
		case R.id.rel_authority:
			
			ArrayList<GenreModel> authorityList = (ArrayList<GenreModel>) Constant.getAuthorityList();
			showAlertDialog(txtAuthority, authorityList, "权限",0);
			break;
		case R.id.rel_detection_object:
			if(authorityID != null){
				showAlertDialog(txtDetectionObject, genreDetectionObjectList, "检测对象",1);
			} else {
				ZRDUtils.alert(this, MsgDuration.SHORT, "请选择类别");
			}
			
			break;
		case R.id.rel_sample_object:
			if(detectionID != null){
				showAlertDialog(txtSampleObject, genreSampleObjectList, "检测样本",2);
			} else {
				ZRDUtils.alert(this, MsgDuration.SHORT, "请选择检测对象");
			}		
			break;
		default:
			break;
		}
	}
	
	
	private void showAlertDialog(final TextView textView, ArrayList<GenreModel> authorityList,String dialogTitle,final int flag){
			
		final String[] Name = new String[authorityList.size()];
		final String[] Code = new String[authorityList.size()];
		for(int i = 0; i< authorityList.size(); i++){
			Name[i] = authorityList.get(i).getObjectname();
			Code[i] = authorityList.get(i).getObjectid();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(BDLocationActivity.this);
		builder.setTitle(dialogTitle);
		builder.setItems(Name, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				textView.setText(Name[item]);
				getCode(Code[item],flag);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();	
	}

	protected void getCode(String checkCode,int flag) {
		// TODO Auto-generated method stub
		switch (flag) {
		case 0:
			authorityID = checkCode;
			if(authorityID != null && !authorityID.equals("")){
				getGenreDatas(authorityID,flag);
			}
			break;
		case 1:
			detectionID = checkCode;
			if(detectionID != null && !detectionID.equals("")){
				getGenreDatas(detectionID,flag);
			}
			break;		
		case 2:
			sampleID = checkCode;
			break;
		default:
			break;
		}
	}
	
	private void getGenreDatas(String genreID,int flag) {
		RoleDAOImpl roleDaoImpl = RoleDAOImpl.getmRoleDaoImpl(this);
		if(flag == 0){
			genreDetectionObjectList = roleDaoImpl.getGenreList(genreID);
		} else {
			genreSampleObjectList = roleDaoImpl.getGenreList(genreID);
		}
		
		
	}
	 
}
