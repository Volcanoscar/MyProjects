package com.limxing.beijing.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView view=new TextView(context);
		view.setText("zhihuifuwu");
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
