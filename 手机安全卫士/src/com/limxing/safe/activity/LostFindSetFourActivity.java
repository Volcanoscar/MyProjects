package com.limxing.safe.activity;

import com.limxing.safe.R;

import android.os.Bundle;

public class LostFindSetFourActivity extends BaseSetActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_four);
	}

	@Override
	void showNext() {
		startActivityAndFinishSelf(LostFindActivity.class);
	}

	@Override
	void showPre() {
		startActivityAndFinishSelf(LostFindSetThreeActivity.class);
	}

}
