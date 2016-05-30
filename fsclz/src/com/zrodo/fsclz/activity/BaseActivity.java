package com.zrodo.fsclz.activity;
import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.ZRDUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends Activity{

	//临时添加
	public int header_tabnum;
    public static final boolean TAB_VIEW_IS_SHOWED=true;
    
	private FrameLayout mContentLayout;
	private RelativeLayout back;
	private TextView mTitle;
	
	//ProgressDialog显示(R文件方式)
	public void showProgressDialog(int res){
		ZRDUtils.showProgressDialog(this, res);
	}
	
	//ProgressDialog显示(String方式)
	public void showProgressDialog(String msg){
		ZRDUtils.showProgressDialog(this, msg);
	}
	
	//注销ProgressDialog
	public void dismissProgressDialog(){
		ZRDUtils.dismissProgressDialog();
	}
	
	//Toast显示（R文件形式）
	public void showToast(ActivityUtil.MsgDuration msgDuration,int res){
		ZRDUtils.alert(this,msgDuration,res);
	}
	
	//Toast显示（String形式）
	public void showToast(ActivityUtil.MsgDuration msgDuration,String msg){
		ZRDUtils.alert(this,msgDuration,msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ZRDApplication.getInstance().addActivity(this);
		setContentView(R.layout.base_layout);
		back = (RelativeLayout) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mContentLayout = (FrameLayout) findViewById(R.id.contents_layout);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BaseActivity.this.finish();
			}
		});
	}
	
	public void setView (String title, int res){
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    mTitle.setText(title);
	    View view = inflater.inflate(res, null);
	    mContentLayout.addView(view);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
