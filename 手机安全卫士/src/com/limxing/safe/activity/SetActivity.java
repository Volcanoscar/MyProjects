package com.limxing.safe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.limxing.safe.R;
import com.limxing.safe.ui.SetItem;

public class SetActivity extends Activity {
	private com.limxing.safe.ui.SetItem set_main_si_update;
	private SharedPreferences sp;
	private boolean isUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_main);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		set_main_si_update = (SetItem) findViewById(R.id.set_main_si_update);
		isUpdate = sp.getBoolean("isUpdate", true);
		if (isUpdate) {
			set_main_si_update.changeCheckState(true);
		} else {
			set_main_si_update.changeCheckState(false);
		}
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
