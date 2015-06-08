package com.limxing.beijing;

import android.content.Intent;
import android.view.WindowManager;

import com.limxing.beijing.utils.ShareUtil;

public class WelcomeActivity extends BaseActivity {

	private Intent intent;

	@Override
	public void initView() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

	}

	@Override
	public void init() {
		if (ShareUtil.getBooleanData(getApplicationContext(), "isfirst", true)) {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(2000);
						intent = new Intent(WelcomeActivity.this,
								GuideActivity.class);
						startActivity(intent);
						finish();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}.start();

		} else {
			intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}

}
