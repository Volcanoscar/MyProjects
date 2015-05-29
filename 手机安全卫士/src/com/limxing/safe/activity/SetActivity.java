package com.limxing.safe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.service.CallLocationService;
import com.limxing.safe.service.CallSafeService;
import com.limxing.safe.service.DogService;
import com.limxing.safe.ui.SetItem;
import com.limxing.safe.utils.SystemInfoUtils;

public class SetActivity extends Activity {
	private com.limxing.safe.ui.SetItem set_main_si_update;
	private com.limxing.safe.ui.SetItem set_main_si_black;
	private com.limxing.safe.ui.SetItem set_main_si_address;
	private com.limxing.safe.ui.SetItem set_main_si_dog;
	private TextView set_main_location_skin;
	private RelativeLayout set_main_location_rl;
	private SharedPreferences sp;
	private boolean isUpdate;
	private boolean isBlack;
	private boolean isDog;
	private boolean isAddress;
	private String[] styles = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		

	}

	public void initUI() {
		setContentView(R.layout.set_main);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		set_main_si_update = (SetItem) findViewById(R.id.set_main_si_update);
		set_main_si_black = (SetItem) findViewById(R.id.set_main_si_black);
		set_main_si_address = (SetItem) findViewById(R.id.set_main_si_address);
		set_main_si_dog = (SetItem) findViewById(R.id.set_main_si_dog);
		set_main_location_skin = (TextView) findViewById(R.id.set_main_location_skin);
		set_main_location_rl = (RelativeLayout) findViewById(R.id.set_main_location_rl);
		isUpdate = sp.getBoolean("isUpdate", true);
		// isBlack = sp.getBoolean("isBlack", true);
		// isAddress = sp.getBoolean("isAddress", true);
		// isDog = sp.getBoolean("isDog", false);
		set_main_location_skin.setText(styles[sp.getInt("skin", 0)]);
		set_main_location_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeSkin();
			}
		});
	}

	public void init() {
		checkUpdate();
		checkBlack();
		checkAddress();
		checkDog();

	}

	// 判断是否开启了程序锁服务
	private void checkDog() {
		set_main_si_dog.changeCheckState(isDog);
		set_main_si_dog.setOnClickListener(new OnClickListener() {
			private Intent dogIntent = new Intent(SetActivity.this,
					DogService.class);

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (isDog) {
					isDog = false;
					set_main_si_dog.changeCheckState(false);
					editor.putBoolean("isDog", false);
					stopService(dogIntent);

				} else {
					isDog = true;
					set_main_si_dog.changeCheckState(true);
					editor.putBoolean("isDog", true);
					startService(dogIntent);

				}
				editor.commit();
			}
		});

	}

	// 判断是否开启了归属地显示
	private void checkAddress() {
		set_main_si_address.changeCheckState(isAddress);
		set_main_si_address.setOnClickListener(new OnClickListener() {
			private Intent addrssIntent = new Intent(SetActivity.this,
					CallLocationService.class);

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (isAddress) {
					isAddress = false;
					set_main_si_address.changeCheckState(false);
					editor.putBoolean("isAddress", false);
					stopService(addrssIntent);

				} else {
					isAddress = true;
					set_main_si_address.changeCheckState(true);
					editor.putBoolean("isAddress", true);
					startService(addrssIntent);

				}
				editor.commit();
			}
		});

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
		isAddress = SystemInfoUtils.isServiceRunning(this,
				"com.limxing.safe.service.CallLocationService");
		isDog = SystemInfoUtils.isServiceRunning(this,
				"com.limxing.safe.service.DogService");
		init();
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

	// 点击条目
	public void changeSkin() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.settings);
		builder.setSingleChoiceItems(styles, sp.getInt("skin", 0),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = sp.edit();
						editor.putInt("skin", which);
						editor.commit();
						set_main_location_skin.setText(styles[which]);
						dialog.dismiss();
					}
				});
		builder.show();

	}

	// 返回按键的事件
	public void back(View view) {
		onBackPressed();
	}

}
