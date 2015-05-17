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
		if (sp.getBoolean("isSet", false)) {
			setContentView(R.layout.lostfind_main);
			tv_lostfind_main_safenum = (TextView) findViewById(R.id.tv_lostfind_main_safenum);
			tv_lostfind_main_safenum.setText(sp.getString("safeNum", "110"));
			s_lostfind_main_protectstate = (Switch) findViewById(R.id.s_lostfind_main_protectstate);
			s_lostfind_main_protectstate.setChecked(sp.getBoolean(
					"protectstate", false));
		} else {
			// 如果没有进行设置，则进入设置向导页面
			setGuide();
		}
	}

	public void openSetGuide(View view) {
		setGuide();
	}

	/**
	 * 设置向导开启方法
	 */
	private void setGuide() {
		Intent intent = new Intent(LostFindActivity.this,
				LostFindSetOneActivity.class);
		startActivity(intent);
		finish();
	}

}
