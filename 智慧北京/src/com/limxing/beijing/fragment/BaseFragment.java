package com.limxing.beijing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {

	private View view;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		view=initView();
		return view;
	}
	public abstract View initView();
	public abstract void initData();

}
