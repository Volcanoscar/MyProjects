package com.limxing.safe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.limxing.safe.R;

public class TaskManagerSet extends Activity {
	private CheckBox task_manager_set_cb_system;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_manager_set);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		task_manager_set_cb_system = (CheckBox) findViewById(R.id.task_manager_set_cb_system);
		task_manager_set_cb_system.setChecked(sp.getBoolean("showSystemTask",
				false));
		task_manager_set_cb_system
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						if (isChecked) {
							editor.putBoolean("showSystemTask", true);
							editor.commit();
						}else{
							editor.putBoolean("showSystemTask", false);
							editor.commit();
						}
						onBackPressed();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 返回按键的事件
	public void back(View view) {
		onBackPressed();
	}

}
