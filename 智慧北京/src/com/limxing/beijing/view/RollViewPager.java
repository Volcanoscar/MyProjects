package com.limxing.beijing.view;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.limxing.beijing.R;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RollViewPager extends ViewPager {

	private Context context;
	private List<View> viewList;
	private BitmapUtils bitmapUtils;
	private RunnableTask runnableTask;
	public int currentPosition = 0;
	private Handler handler = new Handler() {
		private void handleMeaage() {
			RollViewPager.this.setCurrentItem(currentPosition);
			startRoll();

		}
	};
	private TextView top_news_title;
	private List<String> titleList;
	private List<String> urlImageList;
	private MyAdapter myAdapter;

	private class RunnableTask implements Runnable {

		@Override
		public void run() {
			// 滚动viewpager
			currentPosition = (currentPosition + 1) % viewList.size();
			handler.obtainMessage().sendToTarget();
		}
	}

	public RollViewPager(Context context, final List<View> viewList) {
		super(context);
		this.context = context;
		this.viewList = viewList;
		bitmapUtils = new BitmapUtils(context);
		runnableTask = new RunnableTask();
		// 设置图片主动或被动轮播时候的事件
		this.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				top_news_title.setText(titleList.get(arg0));
				for (int i = 0; i < urlImageList.size(); i++) {
					if (i == arg0) {
						viewList.get(arg0).setBackgroundResource(
								R.drawable.dot_focus);
					} else {
						viewList.get(arg0).setBackgroundResource(
								R.drawable.dot_normal);
					}
				}
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

	}

	// 先调用这个方法把，集合传进来
	public void initTitleList(TextView top_news_title, List<String> titleList) {
		if (null != top_news_title && null != titleList && titleList.size() > 0) {
			top_news_title.setText(titleList.get(0));
		}
		// 把传进来的控件和集合作为本对象的成员变量
		this.top_news_title = top_news_title;
		this.titleList = titleList;
	}

	// 第二个调用的方法 把图片资源传递进来
	public void initImgUrlList(List<String> urlImageList) {
		this.urlImageList = urlImageList;

	}

	public void startRoll() {
		// 设置滚动ViewPager的时间
		if (myAdapter == null) {
			myAdapter = new MyAdapter();
			this.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}
		// 延迟发送消息
		handler.postDelayed(runnableTask, 3000);

	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return urlImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((RollViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 获取 到图片的布局，找到布局中的控件，放进RollViewPager中
			View view = View.inflate(context, R.layout.viewpager_item, null);
			// 把图片放进布局控件中
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			bitmapUtils.display(imageView, urlImageList.get(position));
			((RollViewPager) container).addView(view);
			return view;
		}

	}

}
