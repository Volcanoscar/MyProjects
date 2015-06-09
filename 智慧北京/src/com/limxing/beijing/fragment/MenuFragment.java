package com.limxing.beijing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MenuFragment extends BaseFragment {

	@Override
	public View initView(LayoutInflater inflater) {
		TextView view = new TextView(getActivity());
		view.setText("Œ“ «≤‡ª¨≤Àµ•");
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

}
