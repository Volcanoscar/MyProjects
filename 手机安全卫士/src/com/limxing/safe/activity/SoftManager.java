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
 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����8:47:49
 * 
 * ��������������ܼҵ�Activity������
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

	// ��ʼ������
	private void init() {
		getMemory();
		getSd();
	}

	// ��ʼ��UI
	private void initUI() {
		setContentView(R.layout.soft_manager);
		soft_manager_tv_memory = (TextView) findViewById(R.id.soft_manager_tv_memory);
		soft_manager_sd = (TextView) findViewById(R.id.soft_manager_sd);
	}

	// ��ȡ�ڴ���Ϣ
	public void getMemory() {
		long total = SystemInfoUtils.getTotalMem();
		long free = SystemInfoUtils.getAvailMem(getApplicationContext());
		soft_manager_tv_memory.setText("�ڴ���Ϣ��"
				+ Formatter.formatShortFileSize(this, free));

	}

	// ��ȡsd����Ϣ
	public void getSd() {
		long total = Environment.getExternalStorageDirectory().getTotalSpace();
		long free = Environment.getExternalStorageDirectory().getFreeSpace();
		soft_manager_sd.setText("SD����Ϣ��"
				+ Formatter.formatShortFileSize(this, free));
	}

	// ���ذ������¼�
	public void back(View view) {
		onBackPressed();
	}

}
