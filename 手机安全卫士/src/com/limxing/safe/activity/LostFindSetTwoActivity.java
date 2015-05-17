package com.limxing.safe.activity;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;

import com.limxing.safe.R;
import com.limxing.safe.utils.ToastUtils;

public class LostFindSetTwoActivity extends BaseSetActivity {
	private boolean lockState;
	private ImageView iv_lostfind_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_two);
		lockState = sp.getBoolean("lock", false);
		iv_lostfind_lock = (ImageView) findViewById(R.id.iv_lostfind_lock);
		if (!lockState) {
			iv_lostfind_lock.setImageResource(R.drawable.unlock);
		}

	}

	public void bind(View view) {
		Editor editor = sp.edit();
		lockState = sp.getBoolean("lock", false);
		// ture表示锁上了
		if (lockState) {
			editor.putBoolean("lock", false);
			lockState=false;
			editor.putString("sim", null);
			editor.commit();
			iv_lostfind_lock.setImageResource(R.drawable.unlock);
			ToastUtils.showToast(this, "已经解绑");
		} else {
			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			editor.putBoolean("lock", true);
			lockState=true;
			editor.putString("sim", tm.getSimSerialNumber());
			editor.commit();
			iv_lostfind_lock.setImageResource(R.drawable.lock);
			ToastUtils.showToast(this, "已经绑定");
		}
	}

	@Override
	void showNext() {
		if (lockState) {
			startActivityAndFinishSelf(LostFindSetThreeActivity.class);
		}else{
			ToastUtils.showToast(LostFindSetTwoActivity.this, "请先绑定SIM卡号");
		}
	}

	@Override
	void showPre() {
		startActivityAndFinishSelf(LostFindSetOneActivity.class);

	}

}
