package com.limxing.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.limxing.safe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		if (sp.getBoolean("isSet", false)) {
			setContentView(R.layout.lostfind_main);

		} else {
			// ���û�н������ã������������ҳ��
			setGuide();
		}

	}

	/**
	 * �����򵼿�������
	 */
	private void setGuide() {
		Intent intent = new Intent(LostFindActivity.this,
				LostFindSetOneActivity.class);
		startActivity(intent);
		LostFindActivity.this.finish();
	}

}
