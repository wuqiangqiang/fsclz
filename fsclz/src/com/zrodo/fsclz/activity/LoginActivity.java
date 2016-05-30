package com.zrodo.fsclz.activity;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zrodo.fsclz.model.LoginModel;
import com.zrodo.fsclz.model.RoleModel;
import com.zrodo.fsclz.model.sampleModel;
import com.zrodo.fsclz.service.CacheData;
import com.zrodo.fsclz.service.HttpClient;
import com.zrodo.fsclz.service.OnResponseListener;
import com.zrodo.fsclz.service.Provider;
import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.service.ZrodoProviderImp;
import com.zrodo.fsclz.sqlite.RoleDAOImpl;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.ZRDUtils;
import com.zrodo.fsclz.widget.ClearEditText;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LoginActivity extends Activity {

	private ClearEditText editTextUser;
	private ClearEditText editTextPassword;

	private Provider mProvider;
	private SharedPreferences preferences;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉手机首部状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置窗体全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ZRDApplication.getInstance().addActivity(this);
		setContentView(R.layout.login);
		initview();
	}

	private void initview() {
		editTextUser = (ClearEditText) findViewById(R.id.user_info);
		editTextPassword = (ClearEditText) findViewById(R.id.pass_info);
		preferences = getSharedPreferences("user_info", MODE_PRIVATE);
		userName = preferences.getString("username", "");
		if (!TextUtils.isEmpty(userName)) {
			editTextUser.setText(userName);
		}
		
	}

	/**
	 * 登录按钮click事件
	 * @param view
	 */
	public void onClick(View view) {
		final String username = editTextUser.getText().toString();
		final String password = editTextPassword.getText().toString();
		if (TextUtils.isEmpty(username)) {
			editTextUser.shakeAimation(5);
			ZRDUtils.alert(this, ActivityUtil.MsgDuration.SHORT, R.string.login_name_empty);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			editTextPassword.shakeAimation(5);
			ZRDUtils.alert(this, ActivityUtil.MsgDuration.SHORT, getString(R.string.login_pwd_empay));
			return;
		}
        ZRDUtils.showProgressDialog(LoginActivity.this, R.string.str_login_loading);
		mProvider = ZrodoProviderImp.getmProvider();

		try {
			String loginURl = mProvider.login(username, password).toString();
			getJSONVolley(loginURl, username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//网络请求操作:谷歌网络请求框架volley
	public void getJSONVolley(String JSONDateUrl, final String username){
		HttpClient.getRequest(this, JSONDateUrl,new OnResponseListener() {
			@Override
			public void success(JSONObject response) {
				//数据请求方式
				ArrayList<RoleModel> roleList = null;
				try {
					ZRDUtils.dismissProgressDialog();
					String result = response.getString("result");
					if(result != null){
						LoginModel loginInfo = new Gson().fromJson(result, LoginModel.class);
						if(loginInfo != null){
							CacheData.loginInfo = loginInfo;
						}
					}
					
					if(!userName.equals(username)){
						String subRole = response.getString("sublist");
						roleList = new Gson().fromJson(subRole,new TypeToken<List<RoleModel>>() {}.getType());
						RoleDAOImpl roleDAOImpl = new RoleDAOImpl(LoginActivity.this);
						if(roleDAOImpl.roleInsert(roleList) == 0){
							ZRDUtils.alert(LoginActivity.this, MsgDuration.SHORT, "入库失败，请联系管理员");
						}
						
						// 缓存当前用户名
						saveAccount(username);
										
					}
					
					// 跳转到主页面
					ZRDUtils.CommIntent(LoginActivity.this, null, ModeGridActivity.class);
					finish();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void failure(String errorMessage) {
				ZRDUtils.dismissProgressDialog();
				//ZRDUtils.alert(LoginActivity.this, MsgDuration.LONG, errorMessage);
				ZRDUtils.alert(LoginActivity.this,ActivityUtil.MsgDuration.LONG, R.string.str_login_failed);
			}
		});
	}

	protected void saveAccount(String username) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("username", username);
		editor.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitGameAlert();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void showExitGameAlert() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.exit_dialog);
		Button cofirm = (Button) window.findViewById(R.id.btn_cofirm);
		cofirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				shutdonw();
			}
		});

		Button cancel = (Button) window.findViewById(R.id.btn_cancle);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}

	public void shutdonw() {
		ZRDUtils.setLaunched(this, false);
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
