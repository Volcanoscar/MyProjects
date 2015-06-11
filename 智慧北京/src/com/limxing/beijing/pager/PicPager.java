package com.limxing.beijing.pager;

import com.limxing.beijing.bean.NewCenter.NewCenterItem;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class PicPager extends BasePager {

	public PicPager(Context context, NewCenterItem newCenterItem) {
		super(context);
	}

	@Override
	public View initView() {
		TextView view = new TextView(context);
		view.setText("PicPager");
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
