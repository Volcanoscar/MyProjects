package com.limxing.beijing.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.limxing.beijing.MainActivity;

public abstract class BaseFragment extends Fragment {

	public View view;
	public Context context;
	public SlidingMenu slidingMenu;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		initData(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		slidingMenu = ((MainActivity) context).getSlidingMenu();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

	public abstract View initView(LayoutInflater inflater);
	public abstract void initData(Bundle savedInstanceState);


}
