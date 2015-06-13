package com.limxing.beijing.pager;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.beijing.R;
import com.limxing.beijing.bean.NewCenter.NewCenterItem;
import com.limxing.beijing.view.pagerindicator.TabPageIndicator;

/*
 * 作者：Limxing 时间： 2015-6-11 下午2:45:11
 * 
 * 描述：展示新闻专题的的Pager：是由两部分组成，上面自定义的TabPageIndicator，和一个ViewPage
 * ViewPager中的内容是与TabPageIndicator上的条目对应的
 */
public class NewPager extends BasePager {

	private NewCenterItem newCenterItem;

	@ViewInject(R.id.indicator)
	public TabPageIndicator indicator;

	@ViewInject(R.id.pager)
	public ViewPager viewPager;

	private ArrayList<String> titleList;

	private ArrayList<BasePager> pagerList;
	private MyAdapter myAdapter;

	public NewPager(Context context, NewCenterItem newCenterItem) {
		super(context);
		this.newCenterItem = newCenterItem;
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.frag_news, null);
		ViewUtils.inject(this, view);
		titleList = new ArrayList<String>();
		pagerList = new ArrayList<BasePager>();
		return view;
	}

	/**
	 * 这个方法不是主动调用的，是在上一级点击事件的时候，调用的这个对象，直接调用的这个方法完成，数据的填充，而页面View是在对象以创建就能够填充的
	 */
	@Override
	public void initData() {
		// 底部的iewPager需要需要去显示一个界面，并且当前的界面继承自BasePager，先是具体的内容
		// 封装在centerIte对应的额children借点中url指向的网络请求
		// 获取TabPageIndicator的条目的内容，中国，国际。。。
		for (int i = 0; i < newCenterItem.children.size(); i++) {
			// 把每个标题装进集合中
			titleList.add(newCenterItem.children.get(i).title);
			// 把每个pagerView的路径封装成NewItemPager对象装进集合中
			pagerList.add(new NewItemPager(context, newCenterItem.children
					.get(i).url));
		}
		if (myAdapter == null) {
			myAdapter = new MyAdapter();
			viewPager.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}
		// 指针要和ViewPager绑定在一起，指针指向那一页（ViewPager对应的那一页）
		indicator.setViewPager(viewPager);
		// 初始化进去指针显示的页面为第一个（都是自定义控件中的方法）
		indicator.setCurrentItem(0);
		// 设置iewPager的改变监听事件
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				
				// 在此设置只有在第一页的时候才能拖动左侧菜单栏
				if (arg0 == 0) {
					slidingMenu
							.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
				indicator.setCurrentItem(arg0);
				BasePager basePager = pagerList.get(arg0);
				basePager.initData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 默认加载第一个页面的内容
		pagerList.get(0).initData();

	}

	// TabPageIndicator的数据适配器
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titleList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}

		// 消除前一个页面
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		// 获得pager的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);
		}

		// 加载viewpager的页面
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(pagerList.get(position)
					.getRootView());
			return pagerList.get(position).getRootView();

		}

	}

}
