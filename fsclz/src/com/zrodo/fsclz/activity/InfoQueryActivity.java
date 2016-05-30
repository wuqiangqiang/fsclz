package com.zrodo.fsclz.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.zrodo.fsclz.model.InfoByCardidModel;
import com.zrodo.fsclz.service.HttpClient;
import com.zrodo.fsclz.service.OnResponseListener;
import com.zrodo.fsclz.service.Provider;
import com.zrodo.fsclz.service.ZrodoProviderImp;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.ZRDUtils;

public class InfoQueryActivity extends BaseActivity implements Animation.AnimationListener {

    private LinearLayout queryContentLinear;
    private Provider mProvider;
    private TextView infoEdit,txt_compnayName,txt_deptName,txt_detectDate,txt_address,txt_cardId,txt_result,txt_inqId,txt_detecter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView("源溯查询",R.layout.info_query_layout);
        initView();
    }

    protected void initView() {
        queryContentLinear = (LinearLayout) findViewById(R.id.query_content_linear);
        infoEdit=(TextView) findViewById(R.id.query_edit);
        txt_compnayName = (TextView) findViewById(R.id.info_companyname);
        txt_deptName = (TextView) findViewById(R.id.info_deptname);
        txt_detectDate = (TextView) findViewById(R.id.info_detectdate);
        txt_address = (TextView) findViewById(R.id.info_address);
        txt_cardId = (TextView) findViewById(R.id.info_cardid);
        txt_result = (TextView) findViewById(R.id.info_result);
        txt_inqId = (TextView) findViewById(R.id.info_inqid);
        txt_detecter = (TextView) findViewById(R.id.info_detecter);
    }

    @SuppressLint("NewApi") 
    public void queryInfo(View view) {
        /**
         * 隐藏软键盘
         */
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        clearTxt();
        String strInfo = infoEdit.getText().toString();
        if(TextUtils.isEmpty(strInfo)){
            Toast.makeText(this,"请输入检疫证号",Toast.LENGTH_LONG).show();
            return;
        }
        mProvider = ZrodoProviderImp.getmProvider();
        ZRDUtils.showProgressDialog(InfoQueryActivity.this, "加载中...");
		try {
			String URL = mProvider.getInfoByCardid(strInfo).toString();
		    getInfoQuery(URL,strInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void getInfoQuery(String URL,final String strInfo) {
         HttpClient.getRequest(this, URL, new OnResponseListener() {
			
			@Override
			public void success(JSONObject response) {
				try {   
						ZRDUtils.dismissProgressDialog();
						queryContentLinear.setVisibility(View.VISIBLE);
			            int[] location = new int[2];
			            queryContentLinear.getLocationOnScreen(location);
			            Animation translationAnimation = new TranslateAnimation(0, 0, -location[1], 0);
			            translationAnimation.setDuration(300);
			            translationAnimation.setAnimationListener(InfoQueryActivity.this);
			            queryContentLinear.startAnimation(translationAnimation);
						String result = response.getString("result");
						if(result!=null){				  
							InfoByCardidModel infoQuery = new Gson().fromJson(result, InfoByCardidModel.class);
							txt_compnayName.setText(infoQuery.getCompanyname());
					        txt_deptName.setText(infoQuery.getDeptname());
					        txt_detectDate.setText(infoQuery.getDetectdate());
					        txt_address.setText(infoQuery.getProvince() + infoQuery.getCity() + infoQuery.getCountry());
					        txt_cardId.setText(infoQuery.getCardid());
					        txt_result.setText(infoQuery.getResult());
					        txt_inqId.setText(strInfo);
					        txt_detecter.setText(infoQuery.getDetecter());					  
						}					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			@Override
			public void failure(String errorMessage) {
				ZRDUtils.dismissProgressDialog();
				ZRDUtils.alert(InfoQueryActivity.this,ActivityUtil.MsgDuration.LONG, "网络错误，请求数据失败");
			}
		});
	}

	private void clearTxt(){
       int len=queryContentLinear.getChildCount();
       for(int i=0;i<len;i++){
           LinearLayout layout= (LinearLayout) queryContentLinear.getChildAt(i);
           TextView txtView= (TextView) layout.getChildAt(1);
           txtView.setText("");
       }
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        Animation popAnimation = new TranslateAnimation(0, 0, -10, 0);
        popAnimation.setInterpolator(new CycleInterpolator(3));
        popAnimation.setDuration(500);
        queryContentLinear.startAnimation(popAnimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}