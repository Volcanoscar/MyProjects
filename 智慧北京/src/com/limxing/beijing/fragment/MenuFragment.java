package com.limxing.beijing.fragment;

import android.view.View;
import android.widget.TextView;

public class MenuFragment extends BaseFragment {

	@Override
	public View initView() {
		TextView view=new TextView(getActivity());
		view.setText("���ǲ໬�˵�");
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

}
