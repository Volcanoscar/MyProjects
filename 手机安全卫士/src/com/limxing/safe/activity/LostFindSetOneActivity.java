package com.limxing.safe.activity;

import android.os.Bundle;

import com.limxing.safe.R;

public class LostFindSetOneActivity extends BaseSetActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_one);

	}

	@Override
	void showNext() {
		startActivityAndFinishSelf(LostFindSetTwoActivity.class);
	}

	@Override
	void showPre() {
		// TODO Auto-generated method stub

	}

}
