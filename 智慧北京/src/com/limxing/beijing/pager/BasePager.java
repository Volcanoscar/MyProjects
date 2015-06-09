package com.limxing.beijing.pager;

import android.content.Context;
import android.view.View;

public abstract class BasePager {
	public Context context;
	public View view;

	public BasePager(Context context) {
		this.context = context;
		view = initView();

	}

	public View getRootView() {
		return view;
	}

	public abstract View initView();

	public abstract void initData();

}
