package com.zrodo.fsclz.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

@SuppressLint("NewApi")
public class AnalysisCHScrollView extends HorizontalScrollView{

     DataAnalysisActivity activity;
	
	public AnalysisCHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (DataAnalysisActivity) context;
	}

	
	public AnalysisCHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (DataAnalysisActivity) context;
	}

	public AnalysisCHScrollView(Context context) {
		super(context);
		activity = (DataAnalysisActivity) context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//进行触摸赋值ֵ
		activity.mTouchView = this;
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//当当前的CHSCrollView被触摸时，滑动其它
		if(activity.mTouchView == this) {
			activity.onScrollChanged(l, t, oldl, oldt);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
