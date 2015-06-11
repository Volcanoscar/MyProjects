package com.limxing.beijing.pager;

import com.limxing.beijing.bean.NewCenter.NewCenterItem;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class TopicPager extends BasePager {

	public TopicPager(Context context, NewCenterItem newCenterItem) {
		// TODO Auto-generated constructor stub
		super(context);
	}

	@Override
	public View initView() {
		TextView view = new TextView(context);
		view.setText("TopicPager");
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
