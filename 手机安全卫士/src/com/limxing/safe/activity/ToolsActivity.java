package com.limxing.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limxing.safe.R;

public class ToolsActivity extends Activity {
	private TextView tools_tv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	private void initUI() {
		setContentView(R.layout.tools_main);
		tools_tv_location = (TextView) findViewById(R.id.tools_tv_location);

	}

	private void init() {
		tools_tv_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ToolsActivity.this,
						ToolLocationActivity.class);
				startActivity(intent);
			}
		});
	}

	// ·µ»Ø°´Å¥
	public void back(View view) {
		onBackPressed();

	}
}
