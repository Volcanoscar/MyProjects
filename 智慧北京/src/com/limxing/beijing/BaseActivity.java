package com.limxing.beijing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public abstract class BaseActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		init();
	}

	public abstract void initView();

	public abstract void init();

	@Override
	public void onBackPressed() {

	}
}
