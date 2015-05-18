package com.limxing.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.limxing.safe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_lostfind_main_safenum;
	private Switch s_lostfind_main_protectstate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("info", MODE_PRIVATE);

	}

	public void openSetGuide(View view) {
		setGuide();
	}

	/**
	 * �����򵼿�������
	 */
	private void setGuide() {
		Intent intent = new Intent(LostFindActivity.this,
				LostFindSetOneActivity.class);
		startActivity(intent);
		// finish();
	}

	@Override
	protected void onStart() {
		if (sp.getBoolean("isSet", false)) {
			setContentView(R.layout.lostfind_main);
			tv_lostfind_main_safenum = (TextView) findViewById(R.id.tv_lostfind_main_safenum);
			s_lostfind_main_protectstate = (Switch) findViewById(R.id.s_lostfind_main_protectstate);
			s_lostfind_main_protectstate.setChecked(sp.getBoolean(
					"protectstate", false));
			tv_lostfind_main_safenum.setText(sp.getString("safeNum", ""));
			super.onStart();
		} else {
			// ���û�н������ã������������ҳ��
			setGuide();
		}
		super.onStart();
	}

}
