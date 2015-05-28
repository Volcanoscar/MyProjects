package com.limxing.safe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.limxing.safe.utils.SystemInfoUtils;
import com.limxing.safe.utils.ToastUtils;

/*
 * 作者：Limxing 时间： 2015-5-27 下午11:34:20
 * 
 * 描述：这是进程管理的操作类
 */
public class TaskManager extends Activity {
	private long free;
	private SharedPreferences sp;
	private TextView task_manager_tv_number;
	private TextView task_manager_memory;
	private RelativeLayout task_manager_loading;
	private ListView task_manager_lv;
	private TextView task_manager_tv_float;
	private MyAdapter adapter;
	private List<TaskInfo> TaskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	// private int userTaskInfos.size();
	// private int systemTaskInfos.size();
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
					if (firstVisibleItem < userTaskInfos.size() + 1) {
						task_manager_tv_float.setText("用户进程"
								+ userTaskInfos.size() + "个");
					} else {
						task_manager_tv_float.setText("系统进程"
								+ systemTaskInfos.size() + "个");
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
		// 得到内存信息
		 free = SystemInfoUtils.getAvailMem(this);
		// long total = SystemInfoUtils.getTotalMem();
		task_manager_memory.setText("可用内存："
				+ Formatter.formatFileSize(this, free));

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
						systemTaskInfos.add(info);
					}
				}
				// userTaskInfos.size() = userTaskInfos.size();
				// systemTaskInfos.size() = systemTaskInfos.size();
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
			int count = userTaskInfos.size() + systemTaskInfos.size() + 2;
			if (sp.getBoolean("showSystemTask", false)) {
				int num = userTaskInfos.size() + systemTaskInfos.size();
				task_manager_tv_number.setText("正在运行程序：" + num + "个");
			} else {
				count = userTaskInfos.size() + 1;
				task_manager_tv_number.setText("正在运行程序：" + userTaskInfos.size()
						+ "个");
			}

			return count;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (position == 0) {
				TextView view = new TextView(TaskManager.this);
				view.setText("用户进程" + userTaskInfos.size() + "个");
				view.setBackgroundColor(Color.GRAY);
				return view;
			}
			if (position == userTaskInfos.size() + 1) {
				TextView view = new TextView(TaskManager.this);
				view.setText("系统进程" + systemTaskInfos.size() + "个");
				// task_manager_tv_float.setText("系统进程" + systemTaskInfos.size()
				// + "个");
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
				view.setTag(holder);
			}
			// CheckBox单独拿出来，不让他们复用
			holder.task_manager_item_cb = (CheckBox) view
					.findViewById(R.id.task_manager_item_cb);
			// // 设置CheckBox的选择事件
			// holder.task_manager_item_cb
			// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView,
			// boolean isChecked) {
			// if (isChecked) {
			// if (position < userTaskInfos.size() + 1) {
			// userTaskInfos.get(position - 1).setChecked(
			// true);
			// } else {
			// systemTaskInfos.get(
			// position - userTaskInfos.size() - 2)
			// .setChecked(true);
			// }
			// }
			//
			// }
			// });
			// 初始化名称占用内存以及是否被选中的状态
			if (position < userTaskInfos.size() + 1) {
				holder.task_manager_item_tv_icon.setImageDrawable(userTaskInfos
						.get(position - 1).getIcon());
				holder.task_manager_item_tv_name.setText(userTaskInfos.get(
						position - 1).getAppname());
				holder.task_manager_item_tv_memory.setText(String
						.valueOf(Formatter.formatFileSize(
								getApplicationContext(),
								userTaskInfos.get(position - 1).getMemsize())));
				holder.task_manager_item_cb.setChecked(userTaskInfos.get(
						position - 1).isChecked());
			} else {
				int i = position - userTaskInfos.size() - 2;
				holder.task_manager_item_tv_icon
						.setImageDrawable(systemTaskInfos.get(i).getIcon());
				holder.task_manager_item_tv_name.setText(systemTaskInfos.get(i)
						.getAppname());
				holder.task_manager_item_tv_memory.setText(String
						.valueOf(Formatter.formatFileSize(
								getApplicationContext(), systemTaskInfos.get(i)
										.getMemsize())));
				holder.task_manager_item_cb.setChecked(systemTaskInfos.get(i)
						.isChecked());
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return null;
			}
			if (position == userTaskInfos.size() + 1) {
				return null;
			}
			if (position < userTaskInfos.size() + 1) {
				return userTaskInfos.get(position - 1);
			} else {
				return systemTaskInfos.get(position - userTaskInfos.size() - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
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
		sp = getSharedPreferences("info", MODE_PRIVATE);
		// 条目的点击事件
		task_manager_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = task_manager_lv.getItemAtPosition(position);
				if (obj != null && obj instanceof TaskInfo) {
					TaskInfo info = (TaskInfo) obj;
					if (info.getPackname().equals(getPackageName())) {
						return;
					}
					Holder holder = (Holder) view.getTag();
					if (info.isChecked()) {
						holder.task_manager_item_cb.setChecked(false);
						info.setChecked(false);
					} else {
						holder.task_manager_item_cb.setChecked(true);
						info.setChecked(true);
					}
				}

			}
		});

	}

	@Override
	protected void onStart() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		super.onStart();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	// 返回按键的事件
	public void back(View view) {
		onBackPressed();
	}

	// 设置按钮
	public void set(View view) {
		Intent intent = new Intent(this, TaskManagerSet.class);
		startActivity(intent);
	}

	// 清理按钮
	public void clear(View view) {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		int count = 0;
		long savemem = 0;
		for (TaskInfo info : TaskInfos) {
			// list.add(info);
			if (info.isChecked()) {
				count++;
				savemem += info.getMemsize();
				am.killBackgroundProcesses(info.getPackname());
				if (info.isUsertask()) {
					userTaskInfos.remove(info);
				} else {
					systemTaskInfos.remove(info);
				}
			}
		}
		// for (TaskInfo info : list) {
		// if (info.isChecked()) {
		// count++;
		// savemem += info.getMemsize();
		// am.killBackgroundProcesses(info.getPackname());
		// TaskInfos.remove(info);
		// }
		// }
		ToastUtils.showToast(TaskManager.this, "清理了" + count + "个进程，释放了"
				+ Formatter.formatFileSize(TaskManager.this, savemem) + "内存");
		int num = userTaskInfos.size() + systemTaskInfos.size();
		task_manager_tv_number.setText("正在运行程序：" + num + "个");
		task_manager_memory.setText("可用内存："
				+ Formatter.formatFileSize(this, savemem+free));
		adapter.notifyDataSetChanged();
		// init();

	}

	// 全选按钮
	public void allClick(View view) {
		if (sp.getBoolean("showSystemTask", true)) {

			for (TaskInfo info : TaskInfos) {
				if (info.getPackname().equals(getPackageName())) {
					continue;
				}
				info.setChecked(true);
			}
		} else {
			for (TaskInfo info : userTaskInfos) {
				if (info.getPackname().equals(getPackageName())) {
					continue;
				}
				info.setChecked(true);

			}
		}

		adapter.notifyDataSetChanged();

	}

	// 反选按钮，反选和全选都要判断用户是否是自己
	public void reClick(View view) {
		if (sp.getBoolean("showSystemTask", true)) {
			for (TaskInfo info : TaskInfos) {
				if (info.getPackname().equals(getPackageName())) {
					continue;
				}
				if (info.isChecked()) {
					info.setChecked(false);
				} else {
					info.setChecked(true);
				}

			}
		} else {
			for (TaskInfo info : userTaskInfos) {
				if (info.getPackname().equals(getPackageName())) {
					continue;
				}
				if (info.isChecked()) {
					info.setChecked(false);
				} else {
					info.setChecked(true);
				}
			}
		}

		adapter.notifyDataSetChanged();

	}

}
