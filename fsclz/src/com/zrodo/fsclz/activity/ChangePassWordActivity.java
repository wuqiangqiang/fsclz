package com.zrodo.fsclz.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import com.zrodo.fsclz.service.CacheData;
import com.zrodo.fsclz.service.Provider;
import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.service.ZrodoProviderImp;
import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.ActivityUtil;
import com.zrodo.fsclz.utils.ZRDUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassWordActivity extends BaseActivity {

	private EditText editOriginalPwd = null;
	private EditText editNewPwd = null;
	private EditText editConfirmNewPwd = null;
	private String strLoginPassWord = "";
	private FinalHttp finalHttp;
	private String strNewPassWord = "";
	private String strConfirmPassWord = "";
	private Button btnSubmit;
	private Provider mProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(getString(R.string.str_change_password), R.layout.update_password);
		editOriginalPwd = (EditText) findViewById(R.id.edit_old_password);
		editOriginalPwd.setSelection(editOriginalPwd.getText().length());
		editNewPwd = (EditText) findViewById(R.id.edit_new_password);
		editNewPwd.setSelection(editNewPwd.getText().length());
		editConfirmNewPwd = (EditText) findViewById(R.id.edit_comfirm_new_password);
		editConfirmNewPwd.setSelection(editConfirmNewPwd.getText().length());
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				mProvider= ZrodoProviderImp.getmProvider();	
				String requestObjURL;
				try {
					requestObjURL = mProvider.changePWD().toString();
					updatePassword(requestObjURL);
				} catch (Exception e) {
					e.printStackTrace();
				}			
				
	        }
		});
		
		finalHttp = new FinalHttp();
        finalHttp.configTimeout(10 * 1000);

	}

	private void updatePassword(String requestObjURL) {
		strLoginPassWord = editOriginalPwd.getText().toString().trim();
		strNewPassWord = editNewPwd.getText().toString().trim();
		strConfirmPassWord = editConfirmNewPwd.getText().toString().trim();
		if(strLoginPassWord.equals(strNewPassWord)){
			showToast(MsgDuration.SHORT, R.string.str_old_identical_new_pwd);
			return;
		} else if(!(strNewPassWord.equals(strConfirmPassWord))){
			showToast(MsgDuration.SHORT, R.string.str_new_confirm_pwd_diff);
			return;
		} else if (!ZRDUtils.isPwd(strNewPassWord)) {
			showToast(MsgDuration.SHORT, R.string.new_pwd_Invalid);
			return;
		}
		
		AjaxParams params = constructAjaxParamsObj();
		
		finalHttp.post(requestObjURL, params,new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				 dismissProgressDialog();
                 showToast(ActivityUtil.MsgDuration.LONG, R.string.bdlocation_serverConection_failed);
			}

			@Override
			public void onStart() {
				super.onStart();
				showProgressDialog(R.string.change_pwd_loding);
			}
		
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if(!TextUtils.isEmpty(t)){
					try {
						JSONObject obj = new JSONObject(t);
						if("true".equals(obj.optString("message"))){
							dismissProgressDialog(); 
	 						showToast(ActivityUtil.MsgDuration.LONG, R.string.change_pwd_sucess);
	 						ZRDApplication.getInstance().delActivity();
	 						ZRDUtils.CommIntent(ChangePassWordActivity.this, null, LoginActivity.class);
						} else {
							dismissProgressDialog(); 
	 						showToast(ActivityUtil.MsgDuration.LONG, R.string.change_pwd_failed);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
			
		});
	}

	private AjaxParams constructAjaxParamsObj(){      
	     AjaxParams params = new AjaxParams();	    
	     params.put("userid",CacheData.loginInfo.getUserid());
   	     params.put("password",strLoginPassWord);
   	     params.put("newpassword",strNewPassWord);   	   	     
	     return  params;       
    } 
	
}
