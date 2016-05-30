package com.zrodo.fsclz.activity;

import com.zrodo.fsclz.service.ConfigClass;
import com.zrodo.fsclz.service.ZRDApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机首部状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置窗体全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ZRDApplication.getInstance().addActivity(this);
		setContentView(R.layout.zrd_welcom);		
		//初始化网络URL
		ConfigClass.init();
		new Handler().postDelayed(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				// TODO Auto-generated method stub;
				Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				intent.addFlags(intent.FLAG_ACTIVITY_NO_USER_ACTION);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 2000);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//ZRDApplication.getInstance().exit();
        	android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
	    }
	}
		
}
