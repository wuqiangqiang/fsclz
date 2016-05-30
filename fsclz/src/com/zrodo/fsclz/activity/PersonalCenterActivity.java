package com.zrodo.fsclz.activity;

import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.ZRDUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class PersonalCenterActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout relPersonalInfo;
	private RelativeLayout relChangePassword;
	private RelativeLayout relContactUs;
	private RelativeLayout relAboutVersion;
	private Button btnCancellation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(getString(R.string.str_personal_center), R.layout.personal_center);
		initView();
	}

	private void initView() {
		relPersonalInfo = (RelativeLayout) findViewById(R.id.lin_personalinfo);
		relChangePassword = (RelativeLayout) findViewById(R.id.lin_change_password);
		relContactUs = (RelativeLayout) findViewById(R.id.lin_contact_us);
		relAboutVersion = (RelativeLayout) findViewById(R.id.lin_about_version);
		btnCancellation = (Button) findViewById(R.id.btnCancellation);		
		relPersonalInfo.setOnClickListener(this);
		relChangePassword.setOnClickListener(this);
		relContactUs.setOnClickListener(this);
		relAboutVersion.setOnClickListener(this);
		btnCancellation.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.lin_personalinfo:
			ZRDUtils.CommIntent(this, null, PersonInfoActivity.class);
			break;
		case R.id.lin_change_password:
			ZRDUtils.CommIntent(this, null, ChangePassWordActivity.class);
			break;
		case R.id.lin_contact_us:
            ZRDUtils.CommIntent(this, null, ContactUSActivity.class);
			break;
		case R.id.lin_about_version:
			ZRDUtils.CommIntent(this, null, AboutSystemActivity.class);
			break;
		case R.id.btnCancellation:
			ZRDApplication.getInstance().delActivity();
			ZRDUtils.CommIntent(this, null, LoginActivity.class);
			break;
		default:
			break;
		}
	}
}
