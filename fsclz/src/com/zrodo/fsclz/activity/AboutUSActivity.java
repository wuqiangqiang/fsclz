package com.zrodo.fsclz.activity;

import com.zrodo.fsclz.utils.ZRDUtils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;


public class AboutUSActivity extends BaseActivity {

	private TextView  txtVerSionCode,systemInfo;
    private String text = "\u0020\u0020\u0020\u0020\u0020\u0020佛山菜篮子为佛山农业局食品安全检测客户端，提供食品样本数据上传，历史数据查询，各站点监控等功能，安全快捷高效，\n\u0020\u0020\u0020\u0020\u0020\u0020目前支持android4.0以上版本";
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView("关于本软件", R.layout.about_us);
		txtVerSionCode = (TextView) findViewById(R.id.txt_version_code);
		systemInfo = (TextView) findViewById(R.id.system_info);
		
		try {
        	String dbcText = ZRDUtils.ToDBC(text);
        	systemInfo.setText(dbcText);
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

}
