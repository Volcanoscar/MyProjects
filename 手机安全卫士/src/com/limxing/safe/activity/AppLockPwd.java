package com.limxing.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.utils.ToastUtils;

public class AppLockPwd extends Activity {
	private ImageView iv_task_password;
	private TextView tv_task_password;
	private EditText et_task_password;
	private Button bt_task_password;
	private String packname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_manager_app_password);
		packname = getIntent().getStringExtra("packname");
		iv_task_password = (ImageView) findViewById(R.id.iv_task_password);
		tv_task_password = (TextView) findViewById(R.id.tv_task_password);
		PackageManager pm = getPackageManager();
		try {
			//获取图标和程序名称
			iv_task_password.setImageDrawable(pm
					.getApplicationInfo(packname, 0).loadIcon(pm));
			tv_task_password.setText(pm.getApplicationInfo(packname, 0)
					.loadLabel(pm).toString());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		et_task_password = (EditText) findViewById(R.id.et_task_password);
		bt_task_password = (Button) findViewById(R.id.bt_task_password);
		bt_task_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String s = et_task_password.getText().toString().trim();
				if ("123".equals(s)) {
					// 密码正确，发送通知不让服务监控这个程序
					Intent intent=new Intent();
					intent.setAction("com.limxing.safe.pass");
					intent.putExtra("packname", packname);
					sendBroadcast(intent);
					finish();
				} else {
					Animation aa=AnimationUtils.loadAnimation(AppLockPwd.this, R.anim.shake);
					et_task_password.startAnimation(aa);
					ToastUtils.showToast(AppLockPwd.this, "密码错误");
				}

			}
		});

	}

	public void back(View view) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// 去左面
		// <action android:name="android.intent.action.MAIN" />
		// <category android:name="android.intent.category.HOME" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <category android:name="android.intent.category.MONKEY"/>
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		finish();
		super.onBackPressed();
	}

}
