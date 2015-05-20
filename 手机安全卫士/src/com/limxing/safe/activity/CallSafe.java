package com.limxing.safe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.db.dao.BlackNumberDao;
import com.limxing.safe.db.domain.BlackNumber;
import com.limxing.safe.utils.ToastUtils;

public class CallSafe extends Activity {
	private BlackNumberDao bnd;
	private List<BlackNumber> list;
	private TextView callsafe_main_tv;
	private ListView callsafe_main_lv;
	private ImageView callsafe_main_iv;
	private LinearLayout callsafe_main_loading;
	private MyAdapter adapter;
	private Dialog dialog;
	private View view;
	private ImageView callsafe_add_iv;
	private EditText callsafe_add_et;
	//开始获取数据的位置
	private int startIndex;
	//每页多少条目
	private int maxCount = 10;
	//数据库中所有的总数
	private int totalCount;
	//消息处理器
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			callsafe_main_loading.setVisibility(View.INVISIBLE);
			// 第一次调用的时候adapter就创建了
			if (adapter == null) {
				adapter = new MyAdapter();
				if (list.size() < 1) {
					callsafe_main_tv.setVisibility(TextView.VISIBLE);
				} else {
					callsafe_main_tv.setVisibility(TextView.INVISIBLE);
					callsafe_main_lv.setAdapter(adapter);
				}
			} else {
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	// 更新UI，只执行一次
	/**
	 * 
	 */
	private void initUI() {
		setContentView(R.layout.callsafe_main);
		bnd = new BlackNumberDao(this);
		totalCount = bnd.getCount();

		callsafe_main_tv = (TextView) findViewById(R.id.callsafe_main_tv);
		callsafe_main_lv = (ListView) findViewById(R.id.callsafe_main_lv);
		callsafe_main_iv = (ImageView) findViewById(R.id.callsafe_main_iv);
		callsafe_main_loading = (LinearLayout) findViewById(R.id.callsafe_main_loading);
		//添加黑名单
		callsafe_main_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder builder = new Builder(CallSafe.this);
				view = View.inflate(CallSafe.this,
						R.layout.callsafe_addblacknumber, null);
				builder.setView(view);
				callsafe_add_et = (EditText) view
						.findViewById(R.id.callsafe_add_et);
				view.findViewById(R.id.bt_confirm).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								CheckBox callsafe_cb_phone = (CheckBox) view
										.findViewById(R.id.callsafe_cb_phone);
								CheckBox callsafe_cb_sms = (CheckBox) view
										.findViewById(R.id.callsafe_cb_sms);
								
								boolean b1 = callsafe_cb_sms.isChecked();
								boolean b2 = callsafe_cb_phone.isChecked();
								String mode = "0";
								if (b1 && b2) {
									mode = "1";
								} else if (b1 && !b2) {
									mode = "3";
								} else if (!b1 && b2) {
									mode = "2";
								} else {
									ToastUtils.showToast(CallSafe.this,
											"请选择拦截模式");
									return;
								}

								String number = callsafe_add_et.getText()
										.toString().trim();
								if (TextUtils.isEmpty(number)) {
									ToastUtils.showToast(CallSafe.this,
											"号码不能为空");
									return;
								}
								BlackNumber bn = new BlackNumber(number, mode);
								if (bnd.add(number, mode)) {
									ToastUtils.showToast(CallSafe.this, "添加成功");
									dialog.dismiss();
									// 提示去查询数据库，并执行UI的更新
									list.add(0, bn);
									// 通知界面刷新。
									if (list.size() == 1) {
										callsafe_main_lv.setAdapter(adapter);
										callsafe_main_tv
												.setVisibility(TextView.INVISIBLE);
									}
									adapter.notifyDataSetChanged();

								} else {
									ToastUtils.showToast(CallSafe.this, "添加失败");
								}
							}
						});
				view.findViewById(R.id.bt_cancel).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
				callsafe_add_iv=(ImageView) view.findViewById(R.id.callsafe_add_iv);
				callsafe_add_iv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(CallSafe.this,PhoneItemActivity.class);
						startActivityForResult(intent, 0);
					}
				});
				dialog = builder.show();

			}
		});
		// 设置ListView的滑动事件
		callsafe_main_lv.setOnScrollListener(new OnScrollListener() {
			// 状态改变时执行
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int lastPosition = callsafe_main_lv
							.getLastVisiblePosition();
					if (lastPosition == list.size() - 1) {
						startIndex += maxCount;
						if (startIndex >= totalCount) {
							ToastUtils.showToast(CallSafe.this, "没有更多数据");
							return;
						}
						initData();
					}
					break;
				}
			}

			// 每一次滑动都执行
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		// 设置条目点击事件
		callsafe_main_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}
	/**
	 * 回调选择联系人界面的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 判断句是否为空
		if (data != null) {
			callsafe_add_et.setText(data.getStringExtra("phoneNum"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	// Adapter内部类
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			// 引入家庭组的方法，使获得控件的方法只执行一次
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				view = View
						.inflate(CallSafe.this, R.layout.callsafe_item, null);
				holder.callsafe_item_tv_number = (TextView) view
						.findViewById(R.id.callsafe_item_tv_number);
				holder.callsafe_item_tv_mode = (TextView) view
						.findViewById(R.id.callsafe_item_tv_mode);
				holder.button_icon_clear = (ImageView) view
						.findViewById(R.id.callsafe_item_delete);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			// 设置删除按钮的点击事件
			holder.button_icon_clear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					boolean b = bnd.delete(list.get(position).getNumber());
					if (b) {
						ToastUtils.showToast(CallSafe.this, "删除成功");
						// 移除集合中的这个条目
						list.remove(position);
						if (list.size() == 0) {
							adapter.notifyDataSetChanged();
							callsafe_main_tv.setVisibility(TextView.VISIBLE);
						} else {
							adapter.notifyDataSetChanged();
						}
					}

				}
			});

			holder.callsafe_item_tv_number.setText(list.get(position)
					.getNumber());
			holder.callsafe_item_tv_mode.setText(modeName(list.get(position)
					.getMode()));
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

	class ViewHolder {
		TextView callsafe_item_tv_number;
		TextView callsafe_item_tv_mode;
		ImageView button_icon_clear;
	}

	// mode与汉语的转换
	public String modeName(String mode) {
		if ("1".equals(mode)) {
			return "全部拦截";
		} else if ("2".equals(mode)) {
			return "电话拦截";
		} else if ("3".equals(mode)) {
			return "短信拦截";
		} else {
			return "数据错误";
		}
	}

	// 数据的执行
	public void initData() {
		callsafe_main_loading.setVisibility(LinearLayout.VISIBLE);
		new Thread() {
			public void run() {
				SystemClock.sleep(500);
				// 判断list经济和是否是空，也就是是不是第一次执行，不是的话查找的数据添加到list中
				if (list == null) {
					list = bnd.findPart(0, maxCount);
				} else {
					for (BlackNumber l : bnd.findPart(startIndex, maxCount)) {
						list.add(l);
					}
				}
				// list = bnd.findAll();
				handler.sendMessage(new Message());
			}

		}.start();
	}

	//返回图片按钮
	public void back(View view){
		onBackPressed();
	}
}
