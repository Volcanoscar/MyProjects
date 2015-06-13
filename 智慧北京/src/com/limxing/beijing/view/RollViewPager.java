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
			// ����viewpager
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
		// ����ͼƬ�����򱻶��ֲ�ʱ����¼�
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

	// ���ͼƬ�Ҳ���bug
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		handler.removeCallbacksAndMessages(null);
	}

	// �ȵ�����������ѣ����ϴ�����
	public void initTitleList(TextView top_news_title, List<String> titleList) {

		if (null != top_news_title && null != titleList && titleList.size() > 0) {
			top_news_title.setText(titleList.get(0));

		}
		// �Ѵ������Ŀؼ��ͼ�����Ϊ������ĳ�Ա����
		this.top_news_title = top_news_title;
		this.titleList = titleList;

	}

	// �ڶ������õķ��� ��ͼƬ��Դ���ݽ���
	public void initImgUrlList(List<String> urlImageList) {
		this.urlImageList = urlImageList;
		startRoll();
	}

	public interface onPageClick {
		public abstract void onclick(int i);

	}

	public void startRoll() {
		// ���ù���ViewPager��ʱ��
		if (myAdapter == null) {
			myAdapter = new MyAdapter();
			this.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}

		// �ӳٷ�����Ϣ
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
			// ��ȡ ��ͼƬ�Ĳ��֣��ҵ������еĿؼ����Ž�RollViewPager��
			View view = View.inflate(context, R.layout.viewpager_item, null);
			// ��ͼƬ�Ž����ֿؼ���
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			bitmapUtils.display(imageView,
					BeiJingApi.BASE_URL + urlImageList.get(position));
			((RollViewPager) container).addView(view);

			// ����ʵ����ָͷ���º����ֲ�ͼƬ
			view.setOnTouchListener(new OnTouchListener() {

				private long downTime;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						System.out.println(111);
						// ����ʱ
						handler.removeCallbacksAndMessages(null);
						// ��¼����ʱ��λ�ú�ʱ��
						downX = (int) event.getX();
						downTime = System.currentTimeMillis();

						break;

					case MotionEvent.ACTION_UP:
						System.out.println(222);
						if ((System.currentTimeMillis() - downTime) < 500
								&& downX == event.getX()) {
							// ��������¼���������������ҳ��
							if (pageClick != null) {
								pageClick.onclick(position);
							}
						}
						startRoll();
						break;
					case MotionEvent.ACTION_CANCEL:
						System.out.println(333);
						// ����ϵͳ�Զ��������¼�
						startRoll();
						break;
					}
					return false;
				}
			});
			return view;
		}

	}

	// �����ֲ�ͼ��ʱ��ַ��¼�
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��ȡ���಻���������¼��ķַ�����iewPager�е�view�����¼�
			getParent().requestDisallowInterceptTouchEvent(true);
			// �ж������Ļ�����
			downX = (int) ev.getX();
			downY = (int) ev.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
				// ��������»�����ˢ�²���,���ؼ�pagerview�ػ��¼��ķַ�
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				// �������һ����Ĳ�����Ҫ����
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
