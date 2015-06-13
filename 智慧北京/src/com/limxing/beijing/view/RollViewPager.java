package com.limxing.beijing.view;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.limxing.beijing.R;
import com.limxing.beijing.utils.BeiJingApi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
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
		public void handleMessage(Message msg) {
			RollViewPager.this.setCurrentItem(currentPosition);
			startRoll();
		};
	};
	private TextView top_news_title;
	private List<String> titleList;
	private List<String> urlImageList;
	private MyAdapter myAdapter;
	private int downX;
	private int downY;
	private onPageClick pageClick;

	private class RunnableTask implements Runnable {

		@Override
		public void run() {
			// 滚动viewpager
			currentPosition = (currentPosition + 1) % viewList.size();
			handler.obtainMessage().sendToTarget();
		}
	}

	public RollViewPager(Context context, final List<View> viewList,
			onPageClick pageClick) {
		super(context);
		this.context = context;
		this.viewList = viewList;
		this.pageClick = pageClick;

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
						viewList.get(i).setBackgroundResource(
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

	// 解决图片乱播的bug
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		handler.removeCallbacksAndMessages(null);
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
		startRoll();
	}

	public interface onPageClick {
		public abstract void onclick(int i);

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
		handler.postDelayed(runnableTask, 2000);

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
		public Object instantiateItem(ViewGroup container, final int position) {
			// 获取 到图片的布局，找到布局中的控件，放进RollViewPager中
			View view = View.inflate(context, R.layout.viewpager_item, null);
			// 把图片放进布局控件中
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			bitmapUtils.display(imageView,
					BeiJingApi.BASE_URL + urlImageList.get(position));
			((RollViewPager) container).addView(view);

			// 设置实现手指头按下后不再轮播图片
			view.setOnTouchListener(new OnTouchListener() {

				private long downTime;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						System.out.println(111);
						// 按下时
						handler.removeCallbacksAndMessages(null);
						// 记录按下时的位置和时间
						downX = (int) event.getX();
						downTime = System.currentTimeMillis();

						break;

					case MotionEvent.ACTION_UP:
						System.out.println(222);
						if ((System.currentTimeMillis() - downTime) < 500
								&& downX == event.getX()) {
							// 触发点击事件，进入新闻详情页面
							if (pageClick != null) {
								pageClick.onclick(position);
							}
						}
						startRoll();
						break;
					case MotionEvent.ACTION_CANCEL:
						System.out.println(333);
						// 这是系统自动产生的事件
						startRoll();
						break;
					}
					return false;
				}
			});
			return view;
		}

	}

	// 设置轮播图的时间分发事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获取父类不让其拦截事件的分发，由iewPager中的view处理事件
			getParent().requestDisallowInterceptTouchEvent(true);
			// 判断是往哪滑动的
			downX = (int) ev.getX();
			downY = (int) ev.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
				// 如果是向下滑动则刷新操作,父控件pagerview截获事件的分发
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				// 就是左右滑动的操作不要拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
