package com.limxing.beijing;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends BaseActivity {

	private ViewPager view_pager;
	private Button button;

	@Override
	public void initView() {
		setContentView(R.layout.guide);
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		button = (Button) findViewById(R.id.button);
	}

	private List<ImageView> list;

	@Override
	public void init() {
		list = new ArrayList<ImageView>();
		ImageView view1 = new ImageView(getApplicationContext());
		view1.setBackgroundResource(R.drawable.guide_1);
		ImageView view2 = new ImageView(getApplicationContext());
		view2.setBackgroundResource(R.drawable.guide_2);
		ImageView view3 = new ImageView(getApplicationContext());
		view3.setBackgroundResource(R.drawable.guide_3);
		list.add(view1);
		list.add(view2);
		list.add(view3);
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				if (list.size() - 1 == index) {
					button.setVisibility(View.VISIBLE);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(intent);
							finish();

						}
					});
				} else {
					button.setVisibility(View.GONE);
				}

			}

			// view滚动完成
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			// ViewPager滚动时
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		view_pager.setAdapter(new PagerAdapter() {
			// 销毁一个View
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				((ViewPager) container).removeView((View) object);
			}

			// 在当前的viewpager中添加view
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(list.get(position));
				return list.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return list.size();
			}
		});

	}

}
