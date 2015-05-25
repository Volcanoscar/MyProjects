package com.limxing.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.utils.SystemInfoUtils;

public class SoftManager extends Activity {
	private TextView soft_manager_tv_memory;
	private TextView soft_manager_sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	private void init() {
		getMemory();
	}

	private void initUI() {
		setContentView(R.layout.soft_manager);
		soft_manager_tv_memory = (TextView) findViewById(R.id.soft_manager_tv_memory);
		soft_manager_sd = (TextView) findViewById(R.id.soft_manager_sd);
	}

	// 获取内存信息
	public void getMemory() {
		long total = SystemInfoUtils.getTotalMem()/1024;
		long free = SystemInfoUtils.getAvailMem(getApplicationContext())/1024;
		soft_manager_tv_memory.setText("内存信息："+free/1024+ "/" + total/1024 );

	}

	public void back(View view) {
		onBackPressed();
	}

}
