package com.limxing.safe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.db.dao.AppLockDao;
import com.limxing.safe.domain.AppInfo;
import com.limxing.safe.engine.AppInfoParser;

public class UnlockFragment extends Fragment {
	private MyAdapter adapter;
	private AppLockDao dao;
	private List<AppInfo> unlockAppInfos;
	private TextView fragment_unlock_tv;
	private ListView fragment_unlock_lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_unlock, null);
		fragment_unlock_tv = (TextView) view
				.findViewById(R.id.fragment_unlock_tv);
		fragment_unlock_lv = (ListView) view
				.findViewById(R.id.fragment_unlock_lv);
		return view;
	}

	@Override
	public void onStart() {
		dao = new AppLockDao(getActivity());
		unlockAppInfos = new ArrayList<AppInfo>();
		List<AppInfo> appInfos = AppInfoParser.getAppInfos(getActivity());
		for (AppInfo info : appInfos) {
			if (dao.exist(info.getApppack())) {

			} else {
				unlockAppInfos.add(info);
			}
		}
		adapter = new MyAdapter();
		fragment_unlock_lv.setAdapter(adapter);

		super.onStart();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			fragment_unlock_tv.setText("未加锁(" + unlockAppInfos.size() + ")个");
			return unlockAppInfos.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final View view;
			Holder holder;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				holder = (Holder) view.getTag();

			} else {
				view = View.inflate(getActivity(), R.layout.item_unlock, null);
				holder = new Holder();
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.iv_app_lock = (ImageView) view
						.findViewById(R.id.iv_app_lock);
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				view.setTag(holder);
			}
			holder.iv_app_icon.setImageDrawable(unlockAppInfos.get(position)
					.getAppico());
			holder.tv_app_name.setText(unlockAppInfos.get(position)
					.getAppname());

			// 对锁进行点击事件的处理
			holder.iv_app_lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranslateAnimation ta = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 1.0f,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					ta.setDuration(300);
					view.startAnimation(ta);
					new Thread() {
						public void run() {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// 解决连续双击，崩溃的问题
									try {
										dao.add(unlockAppInfos.get(position)
												.getApppack());
										unlockAppInfos.remove(position);
										adapter.notifyDataSetChanged();
									} catch (IndexOutOfBoundsException e) {

									}
								}
							});

						}
					}.start();

				}

			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	private class Holder {
		ImageView iv_app_icon;
		TextView tv_app_name;
		ImageView iv_app_lock;
	}
}
