package com.limxing.beijing.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SettingPager extends BasePager {

	public SettingPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView view=new TextView(context);
		view.setText("shezhi");
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
