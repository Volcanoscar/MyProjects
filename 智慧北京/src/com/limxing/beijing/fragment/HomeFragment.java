package com.limxing.beijing.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.beijing.R;
import com.limxing.beijing.pager.BasePager;
import com.limxing.beijing.pager.FunctionPager;
import com.limxing.beijing.pager.GovAffairsPager;
import com.limxing.beijing.pager.NewCenterPager;
import com.limxing.beijing.pager.SettingPager;
import com.limxing.beijing.pager.SmartServicePager;
import com.limxing.beijing.view.MyViewPager;

public class HomeFragment extends BaseFragment {

	// 等同于findById(R.id.layout_content)
	@ViewInject(R.id.layout_content)
	private MyViewPager layout_content;// 已经有值不是null

	@ViewInject(R.id.main_radio)
	private RadioGroup main_radio;
	protected ArrayList<BasePager> list;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_home, null);
		// layout_content = (MyViewPager)
		// view.findViewById(R.id.layout_content);

		ViewUtils.inject(this, view);
		return view;

	}

	@Override
	public void initData(Bundle savedInstanceState) {
		main_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_function:

					layout_content.setCurrentItem(0);

					break;

				case R.id.rb_news_center:
					layout_content.setCurrentItem(1);

					break;
				case R.id.rb_smart_service:
					layout_content.setCurrentItem(2);
					break;
				case R.id.rb_gov_affairs:
					layout_content.setCurrentItem(3);
					break;
				case R.id.rb_setting:
					layout_content.setCurrentItem(4);
					break;
				}

			}
		});
		main_radio.check(R.id.rb_function);
		list = new ArrayList<BasePager>();
		list.add(new FunctionPager(getActivity()));
		list.add(new NewCenterPager(getActivity()));
		list.add(new SmartServicePager(getActivity()));
		list.add(new GovAffairsPager(getActivity()));
		list.add(new SettingPager(getActivity()));

		layout_content.setAdapter(new MyAdapter());
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((MyViewPager) container).removeView((View) object);

		}

		@Override
		public Object instantiateItem(View container, int position) {
			((MyViewPager) container).addView(list.get(position).getRootView());
			return list.get(position).getRootView();
		}

	}

}
