package com.zrodo.fsclz.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.zrodo.fsclz.model.VersionModel;
import com.zrodo.fsclz.service.Constant;
import com.zrodo.fsclz.service.HttpClient;
import com.zrodo.fsclz.service.OnResponseListener;
import com.zrodo.fsclz.service.Provider;
import com.zrodo.fsclz.service.ZrodoProviderImp;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.FileUtil;
import com.zrodo.fsclz.utils.ZRDUtils;
import com.zrodo.fsclz.widget.UpdateManager;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutSystemActivity extends BaseActivity implements OnClickListener{

	private TextView  txtVerSionCode;
	private LinearLayout linAboutSoft;
	private LinearLayout linUpdate;
	private Provider mProvider;
	private int windowsWidth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView("关于菜篮子", R.layout.about_system);
		txtVerSionCode = (TextView) findViewById(R.id.txt_version_code);
		linAboutSoft = (LinearLayout) findViewById(R.id.lin_about_Soft);
		linUpdate = (LinearLayout) findViewById(R.id.lin_update);
		linAboutSoft.setOnClickListener(this);
		linUpdate.setOnClickListener(this);
		windowsWidth = getWindowManager().getDefaultDisplay().getWidth() - 20;
		try {
        	
        	txtVerSionCode.setText(getString(R.string.version) + getVersionName().toString()+"for Android");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getVersionName() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0.0.0";
		}
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.lin_about_Soft:
			ZRDUtils.CommIntent(this, null, AboutUSActivity.class);
			break;
        case R.id.lin_update:
        	ZRDUtils.showProgressDialog(this, "正在检查版本 ，请稍候...");
        	mProvider = ZrodoProviderImp.getmProvider();
    		try {
    			String loginURl = mProvider.getVersionInfo("SYS_CODE_VERSION_MOBILE").toString();
    			getVersion(loginURl);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
			break;

		default:
			break;
		}
	}

	private void getVersion(String postUrl) {
		HttpClient.getRequest(this, postUrl, new OnResponseListener() {
			
			@Override
			public void success(JSONObject response) {
				ZRDUtils.dismissProgressDialog();
				try {
					String result = response.getString("result");
					if(result != null){
						VersionModel versionInfo = new Gson().fromJson(result, VersionModel.class);
						HashMap<String, String> mHashMap = new HashMap<String, String>();
						mHashMap.put("url", versionInfo.getUrl());
						mHashMap.put("version", versionInfo.getVersion());
						mHashMap.put("content",versionInfo.getContent());
						
						UpdateManager manager = new UpdateManager(AboutSystemActivity.this,mHashMap);
						
						if(!manager.isUpdate()){
							showAlertDialog();
							return ;
						}
						
						manager.checkUpdate();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void failure(String errorMessage) {
				
				ZRDUtils.dismissProgressDialog();
				ZRDUtils.alert(AboutSystemActivity.this,ActivityUtil.MsgDuration.LONG, "检查更新失败");
			}
		});
	}

	protected void showAlertDialog() {	
	   
		 final AlertDialog dlg = new AlertDialog.Builder(this).create();
	 		dlg.show();
	 		WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
	 		params.width = windowsWidth;
	 		dlg.getWindow().setAttributes(params);
	 		Window window = dlg.getWindow();
	 		
	 		window.setContentView(R.layout.alert_dialog);
	 		TextView  txtBody = (TextView) window.findViewById(R.id.txt_body);
	 		Button btnBody = (Button) window.findViewById(R.id.btn_body);
	 		txtBody.setText("已经是最新版本");
	 		btnBody.setText("确定");

	 		btnBody.setOnClickListener(new OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View view) {
	 				// TODO Auto-generated method stub
	 					dlg.cancel();

	 			}
	 		});
		
	}
	
}
