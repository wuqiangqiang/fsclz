package com.zrodo.fsclz.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;

public class DataWarningActivity extends BaseActivity implements OnClickListener{

	public HorizontalScrollView mTouchView;	
	//装入所有的HScrollView
	public List<AnalysisCHScrollView> mHScrollViews = new ArrayList<AnalysisCHScrollView>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		  setView("风险预警",R.layout.data_waring_main);   
	}

	@Override
	public void onClick(View view) {
			
	}

	@SuppressLint("NewApi")
	public void onScrollChanged(int l, int t, int oldl, int oldt){
		for(AnalysisCHScrollView scrollView : mHScrollViews) {
			//防止重复滑动
			if(mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}
	
}
