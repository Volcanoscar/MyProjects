package com.limxing.safe.activity;

import com.limxing.safe.R;

import android.os.Bundle;

public class LostFindSetThreeActivity extends BaseSetActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_three);
	}

	@Override
	void showNext() {
		startActivityAndFinishSelf(LostFindSetFourActivity.class);
	}

	@Override
	void showPre() {
		startActivityAndFinishSelf(LostFindSetTwoActivity.class);
	}

}
