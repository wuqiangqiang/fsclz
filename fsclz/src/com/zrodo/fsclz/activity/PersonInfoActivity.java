package com.zrodo.fsclz.activity;
import com.zrodo.fsclz.service.CacheData;
import android.os.Bundle;
import android.widget.TextView;

public class PersonInfoActivity extends BaseActivity {

	private TextView txtUserName;
	private TextView txtCompanyInfo;
	private TextView txtPermissionInfo;
	private TextView txtAccountInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(getString(R.string.str_personalinfo), R.layout.person_info);
		initView();
	}

	private void initView() {
		txtUserName = (TextView) findViewById(R.id.user_name);
		txtCompanyInfo =  (TextView) findViewById(R.id.txt_company_info);
		txtPermissionInfo = (TextView) findViewById(R.id.txt_permission_info);
		txtAccountInfo = (TextView) findViewById(R.id.txt_acoount_info);	
		txtUserName.setText(CacheData.loginInfo.getUsername());
		txtCompanyInfo.setText(CacheData.loginInfo.getDeptname());
		txtPermissionInfo.setText(CacheData.loginInfo.getRolename());
		txtAccountInfo.setText(CacheData.loginInfo.getTruename());
	}

	
}
