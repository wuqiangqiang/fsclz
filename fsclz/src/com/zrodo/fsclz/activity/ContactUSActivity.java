package com.zrodo.fsclz.activity;

import android.os.Bundle;

public class ContactUSActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(getString(R.string.call_me), R.layout.contact_us);	
	}
}
