package com.limxing.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.limxing.safe.R;

public class TrafficActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traffic_main);
		
	}

	public void back(View view) {
		onBackPressed();
	}

}
