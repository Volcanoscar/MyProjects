package com.limxing.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.limxing.safe.R;
import com.limxing.safe.service.CallSafeService;
import com.limxing.safe.ui.SetItem;
import com.limxing.safe.utils.SystemInfoUtils;

public class SetActivity extends Activity {
	private com.limxing.safe.ui.SetItem set_main_si_update;
	private com.limxing.safe.ui.SetItem set_main_si_black;
	private SharedPreferences sp;
	private boolean isUpdate;
	private boolean isBlack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		init();

	}

	public void initUI() {
		setContentView(R.layout.set_main);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		set_main_si_update = (SetItem) findViewById(R.id.set_main_si_update);
		set_main_si_black = (SetItem) findViewById(R.id.set_main_si_black);
		isUpdate = sp.getBoolean("isUpdate", true);
		isBlack = sp.getBoolean("isBlack", true);

	}

	public void init() {
		checkUpdate();
		checkBlack();

	}

	// 判断是否开启了拦截黑名单的功能
	private void checkBlack() {
		set_main_si_black.changeCheckState(isBlack);

		set_main_si_black.setOnClickListener(new OnClickListener() {
			private Intent blackIntent = new Intent(SetActivity.this,
					CallSafeService.class);

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (isBlack) {
					isBlack = false;
					set_main_si_black.changeCheckState(false);
					editor.putBoolean("isBlack", false);
					stopService(blackIntent);

				} else {
					isBlack = true;
					set_main_si_black.changeCheckState(true);
					editor.putBoolean("isBlack", true);
					startService(blackIntent);

				}
				editor.commit();
			}
		});

	}

	@Override
	protected void onStart() {
		// 需要获取系统的状态是否开启了某个服务
		isBlack = SystemInfoUtils.isServiceRunning(this,
				"com.limxing.safe.service.CallSafeService");
		super.onStart();
	}

	// 判断是不是更新系统
	public void checkUpdate() {
		set_main_si_update.changeCheckState(isUpdate);
		set_main_si_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (isUpdate) {
					isUpdate = false;
					set_main_si_update.changeCheckState(false);
					editor.putBoolean("isUpdate", false);
				} else {
					isUpdate = true;
					set_main_si_update.changeCheckState(true);
					editor.putBoolean("isUpdate", true);

				}
				editor.commit();
			}
		});

	}
}
