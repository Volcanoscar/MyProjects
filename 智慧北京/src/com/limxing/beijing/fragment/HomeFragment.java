package com.limxing.beijing.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.limxing.beijing.view.LazyViewPager;
import com.limxing.beijing.view.LazyViewPager.OnPageChangeListener;
import com.limxing.beijing.view.MyViewPager;

public class HomeFragment extends BaseFragment {

	// 相当于findById(R.id.layout_content)
	@ViewInject(R.id.layout_content)
	private MyViewPager viewPagerlayout_content;

	@ViewInject(R.id.main_radio)
	private RadioGroup radio_group;
	protected ArrayList<BasePager> list;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_home, null);
		ViewUtils.inject(this, view);
		return view;

	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// 设置默认选中的导航首页
		radio_group.check(R.id.rb_function);
		// 把所有的导航页面对象添加到集合中
		list = new ArrayList<BasePager>();
		list.add(new FunctionPager(getActivity()));
		list.add(new NewCenterPager(getActivity()));
		list.add(new SmartServicePager(getActivity()));
		list.add(new GovAffairsPager(getActivity()));
		list.add(new SettingPager(getActivity()));
		// 向frag_home中的MyViewPager中添加数据
		viewPagerlayout_content.setAdapter(new MyAdapter());
		viewPagerlayout_content
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// 切换了按钮
						BasePager basePager = list.get(position);
						basePager.initData();

					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						// TODO Auto-generated method stub

					}
				});

		radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_function:
					viewPagerlayout_content.setCurrentItem(0);
					break;
				case R.id.rb_news_center:
					viewPagerlayout_content.setCurrentItem(1);
					break;
				case R.id.rb_smart_service:
					viewPagerlayout_content.setCurrentItem(2);
					break;
				case R.id.rb_gov_affairs:
					viewPagerlayout_content.setCurrentItem(3);
					break;
				case R.id.rb_setting:
					viewPagerlayout_content.setCurrentItem(4);
					break;
				}

			}
		});
		// 初始化是选定第一个group
		BasePager basePager = list.get(0);
		basePager.initData();

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
		public void destroyItem(ViewGroup container, int position, Object object) {
			((MyViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((MyViewPager) container).addView(list.get(position).getRootView());
			return list.get(position).getRootView();
		}

	}

	// 被菜单上的条目调用的方法
	public NewCenterPager switchNewCenterPager() {
		return (NewCenterPager) list.get(1);
	}

}
