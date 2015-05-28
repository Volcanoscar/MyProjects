package com.limxing.safe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.domain.TaskInfo;
import com.limxing.safe.engine.TaskInfoParser;

/*
 * 作者：Limxing 时间： 2015-5-27 下午11:34:20
 * 
 * 描述：这是进程管理的操作类
 */
public class TaskManager extends Activity {
	private TextView task_manager_tv_number;
	private TextView task_manager_memory;
	private RelativeLayout task_manager_loading;
	private ListView task_manager_lv;
	private TextView task_manager_tv_float;
	private MyAdapter adapter;
	private List<TaskInfo> TaskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	private int userTaskSize;
	private int systemTaskSize;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			task_manager_loading.setVisibility(View.INVISIBLE);
			adapter = new MyAdapter();
			task_manager_lv.setAdapter(adapter);
			task_manager_lv.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					if (firstVisibleItem < userTaskSize + 1) {
						task_manager_tv_float.setText("用户进程" + userTaskSize
								+ "个");
					} else {
						task_manager_tv_float.setText("系统进程" + systemTaskSize
								+ "个");
					}

				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUi();
		init();
	}

	/*
	 * 作者：Limxing 时间： 2015-5-27 下午11:39:09
	 * 
	 * 描述：初始化数据
	 */
	private void init() {

		new Thread() {
			public void run() {
				TaskInfos = TaskInfoParser
						.getRunningTaskInfos(getApplicationContext());
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo info : TaskInfos) {
					if (info.isUsertask()) {
						userTaskInfos.add(info);
					} else {
						System.out.println(info.getIcon().toString());
						systemTaskInfos.add(info);
					}
				}
				userTaskSize = userTaskInfos.size();
				systemTaskSize = systemTaskInfos.size();
				handler.sendEmptyMessage(0);

			}
		}.start();

	}

	/*
	 * 作者：Limxing 时间： 2015-5-28 上午12:10:23
	 * 
	 * 描述：数据适配器的类
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			task_manager_tv_number.setText("正在运行程序："
					+ String.valueOf(TaskInfos.size()) + "个");
			return TaskInfos.size() + 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				TextView view = new TextView(TaskManager.this);
				view.setText("用户进程" + userTaskSize + "个");

				view.setBackgroundColor(Color.GRAY);
				return view;
			}
			if (position == userTaskSize + 1) {
				TextView view = new TextView(TaskManager.this);
				view.setText("系统进程" + systemTaskSize + "个");
				task_manager_tv_float.setText("系统进程" + systemTaskSize + "个");
				view.setBackgroundColor(Color.GRAY);
				return view;
			}
			View view;
			Holder holder = new Holder();
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (Holder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.task_manager_item, null);
				holder.task_manager_item_tv_icon = (ImageView) view
						.findViewById(R.id.task_manager_item_tv_icon);
				holder.task_manager_item_tv_name = (TextView) view
						.findViewById(R.id.task_manager_item_tv_name);
				holder.task_manager_item_tv_memory = (TextView) view
						.findViewById(R.id.task_manager_item_tv_memory);
				holder.task_manager_item_cb = (CheckBox) view
						.findViewById(R.id.task_manager_item_cb);
				holder.task_manager_item_cb
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									System.out.println("选中了");
								}

							}
						});
				view.setTag(holder);
			}
			if (position < userTaskSize + 1) {
				holder.task_manager_item_tv_icon.setImageDrawable(userTaskInfos
						.get(position - 1).getIcon());
				holder.task_manager_item_tv_name.setText(userTaskInfos.get(
						position - 1).getAppname());
				holder.task_manager_item_tv_memory.setText(String
						.valueOf(Formatter.formatFileSize(
								getApplicationContext(),
								userTaskInfos.get(position - 1).getMemsize())));
			} else {
				int i = position - userTaskSize - 2;
				holder.task_manager_item_tv_icon
						.setImageDrawable(systemTaskInfos.get(i).getIcon());
				holder.task_manager_item_tv_name.setText(systemTaskInfos.get(i)
						.getAppname());
				holder.task_manager_item_tv_memory.setText(String
						.valueOf(Formatter.formatFileSize(
								getApplicationContext(), systemTaskInfos.get(i)
										.getMemsize())));
			}
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
		ImageView task_manager_item_tv_icon;
		TextView task_manager_item_tv_name;
		TextView task_manager_item_tv_memory;
		CheckBox task_manager_item_cb;

	}

	/*
	 * 作者：Limxing 时间： 2015-5-27 下午11:39:21
	 * 
	 * 描述：初始化UI
	 */
	private void initUi() {
		setContentView(R.layout.task_manager);
		task_manager_tv_number = (TextView) findViewById(R.id.task_manager_tv_number);
		task_manager_memory = (TextView) findViewById(R.id.task_manager_memory);
		task_manager_loading = (RelativeLayout) findViewById(R.id.task_manager_loading);
		task_manager_lv = (ListView) findViewById(R.id.task_manager_lv);
		task_manager_tv_float = (TextView) findViewById(R.id.task_manager_tv_float);

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	// 返回按键的事件
	public void back(View view) {
		onBackPressed();
	}

}
