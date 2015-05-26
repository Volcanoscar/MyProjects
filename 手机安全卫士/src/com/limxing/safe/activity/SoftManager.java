package com.limxing.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.utils.SystemInfoUtils;

/*
 * 作者：Limxing 时间： 2015-5-26 上午8:47:49
 * 
 * 描述：这是软件管家的Activity操作类
 */
public class SoftManager extends Activity {
	private TextView soft_manager_tv_memory;
	private TextView soft_manager_sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	// 初始化数据
	private void init() {
		getMemory();
		getSd();
	}

	// 初始化UI
	private void initUI() {
		setContentView(R.layout.soft_manager);
		soft_manager_tv_memory = (TextView) findViewById(R.id.soft_manager_tv_memory);
		soft_manager_sd = (TextView) findViewById(R.id.soft_manager_sd);
	}

	// 获取内存信息
	public void getMemory() {
		long total = SystemInfoUtils.getTotalMem();
		long free = SystemInfoUtils.getAvailMem(getApplicationContext());
		soft_manager_tv_memory.setText("内存信息："
				+ Formatter.formatShortFileSize(this, free));

	}

	// 获取sd卡信息
	public void getSd() {
		long total = Environment.getExternalStorageDirectory().getTotalSpace();
		long free = Environment.getExternalStorageDirectory().getFreeSpace();
		soft_manager_sd.setText("SD卡信息："
				+ Formatter.formatShortFileSize(this, free));
	}

	// 返回按键的事件
	public void back(View view) {
		onBackPressed();
	}

}
