package com.limxing.safe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.domain.AppInfo;
import com.limxing.safe.engine.AppInfoParser;
import com.limxing.safe.utils.SystemInfoUtils;

/*
 * 作者：Limxing 时间： 2015-5-26 上午8:47:49
 * 
 * 描述：这是软件管家的Activity操作类
 */
public class SoftManager extends Activity {
	private TextView soft_manager_tv_memory;
	private TextView soft_manager_sd;
	private ListView soft_manager_lv;
	private RelativeLayout soft_manager_loading;
	private List<AppInfo> appinfos;
	private List<AppInfo> userSofts;
	private List<AppInfo> systemSofts;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			soft_manager_loading.setVisibility(View.INVISIBLE);
			soft_manager_lv.setAdapter(new MyAppAdapter());

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	// 初始化数据
	private void init() {
		getMemory();
		getSd();
		findSoft();
	}

	// 初始化UI
	private void initUI() {
		setContentView(R.layout.soft_manager);
		soft_manager_tv_memory = (TextView) findViewById(R.id.soft_manager_tv_memory);
		soft_manager_sd = (TextView) findViewById(R.id.soft_manager_sd);
		soft_manager_loading = (RelativeLayout) findViewById(R.id.soft_manager_loading);
		soft_manager_lv = (ListView) findViewById(R.id.soft_manager_lv);
	}

	// 查询已安装的app并放入集合中
	public void findSoft() {
		new Thread() {
			public void run() {
				appinfos = AppInfoParser.getAppInfos(SoftManager.this);
				userSofts = new ArrayList<AppInfo>();
				systemSofts = new ArrayList<AppInfo>();
				for (AppInfo info : appinfos) {
					if (info.isIsuser()) {
						userSofts.add(info);
					} else {
						systemSofts.add(info);
					}
				}
				handler.sendEmptyMessage(0);

			};
		}.start();
	}

	// MyAppAdapter
	private class MyAppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userSofts.size() + systemSofts.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo appinfo;
			if (position < userSofts.size()) {
				appinfo = userSofts.get(position);
			} else {
				appinfo = systemSofts.get(position - userSofts.size());
			}
			View view;
			ViewHolder holder = new ViewHolder();
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(SoftManager.this,
						R.layout.soft_manager_item, null);
				holder.soft_manager_item_tv_icon = (ImageView) view
						.findViewById(R.id.soft_manager_item_tv_icon);
				holder.soft_manager_item_tv_location = (TextView) view
						.findViewById(R.id.soft_manager_item_tv_location);
				holder.soft_manager_item_tv_memory = (TextView) view
						.findViewById(R.id.soft_manager_item_tv_memory);
				holder.soft_manager_item_tv_name = (TextView) view
						.findViewById(R.id.soft_manager_item_tv_name);
				view.setTag(holder);
			}

			holder.soft_manager_item_tv_icon.setImageDrawable(appinfo
					.getAppico());
			if (appinfo.isInphone()) {
				holder.soft_manager_item_tv_location.setText("手机内存");
			} else {
				holder.soft_manager_item_tv_location.setText("SD卡");
			}
			holder.soft_manager_item_tv_memory.setText(Formatter
					.formatFileSize(getApplicationContext(),
							appinfo.getAppsize()));
			holder.soft_manager_item_tv_name.setText(appinfo.getAppname());
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

	/**
	 * 家庭类
	 */
	static class ViewHolder {
		ImageView soft_manager_item_tv_icon;
		TextView soft_manager_item_tv_name;
		TextView soft_manager_item_tv_location;
		TextView soft_manager_item_tv_memory;
	}

	// 获取内存信息
	public void getMemory() {
		long total = SystemInfoUtils.getTotalMem();
		long free = SystemInfoUtils.getAvailMem(getApplicationContext());
		soft_manager_tv_memory.setText("内存信息："
				+ Formatter.formatShortFileSize(this, free));

	}

	// 获取sd卡信息
	public void getSd() {
		long total = Environment.getExternalStorageDirectory().getTotalSpace();
		long free = Environment.getExternalStorageDirectory().getFreeSpace();
		soft_manager_sd.setText("SD卡信息："
				+ Formatter.formatShortFileSize(this, free));
	}

	// 返回按键的事件
	public void back(View view) {
		onBackPressed();
	}

}
