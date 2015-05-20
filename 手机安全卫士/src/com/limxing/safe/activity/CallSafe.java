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
	//��ʼ��ȡ���ݵ�λ��
	private int startIndex;
	//ÿҳ������Ŀ
	private int maxCount = 10;
	//���ݿ������е�����
	private int totalCount;
	//��Ϣ������
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			callsafe_main_loading.setVisibility(View.INVISIBLE);
			// ��һ�ε��õ�ʱ��adapter�ʹ�����
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

	// ����UI��ִֻ��һ��
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
		//��Ӻ�����
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
											"��ѡ������ģʽ");
									return;
								}

								String number = callsafe_add_et.getText()
										.toString().trim();
								if (TextUtils.isEmpty(number)) {
									ToastUtils.showToast(CallSafe.this,
											"���벻��Ϊ��");
									return;
								}
								BlackNumber bn = new BlackNumber(number, mode);
								if (bnd.add(number, mode)) {
									ToastUtils.showToast(CallSafe.this, "��ӳɹ�");
									dialog.dismiss();
									// ��ʾȥ��ѯ���ݿ⣬��ִ��UI�ĸ���
									list.add(0, bn);
									// ֪ͨ����ˢ�¡�
									if (list.size() == 1) {
										callsafe_main_lv.setAdapter(adapter);
										callsafe_main_tv
												.setVisibility(TextView.INVISIBLE);
									}
									adapter.notifyDataSetChanged();

								} else {
									ToastUtils.showToast(CallSafe.this, "���ʧ��");
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
		// ����ListView�Ļ����¼�
		callsafe_main_lv.setOnScrollListener(new OnScrollListener() {
			// ״̬�ı�ʱִ��
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int lastPosition = callsafe_main_lv
							.getLastVisiblePosition();
					if (lastPosition == list.size() - 1) {
						startIndex += maxCount;
						if (startIndex >= totalCount) {
							ToastUtils.showToast(CallSafe.this, "û�и�������");
							return;
						}
						initData();
					}
					break;
				}
			}

			// ÿһ�λ�����ִ��
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		// ������Ŀ����¼�
		callsafe_main_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}
	/**
	 * �ص�ѡ����ϵ�˽��������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// �жϾ��Ƿ�Ϊ��
		if (data != null) {
			callsafe_add_et.setText(data.getStringExtra("phoneNum"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	// Adapter�ڲ���
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			// �����ͥ��ķ�����ʹ��ÿؼ��ķ���ִֻ��һ��
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
			// ����ɾ����ť�ĵ���¼�
			holder.button_icon_clear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					boolean b = bnd.delete(list.get(position).getNumber());
					if (b) {
						ToastUtils.showToast(CallSafe.this, "ɾ���ɹ�");
						// �Ƴ������е������Ŀ
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

	// mode�뺺���ת��
	public String modeName(String mode) {
		if ("1".equals(mode)) {
			return "ȫ������";
		} else if ("2".equals(mode)) {
			return "�绰����";
		} else if ("3".equals(mode)) {
			return "��������";
		} else {
			return "���ݴ���";
		}
	}

	// ���ݵ�ִ��
	public void initData() {
		callsafe_main_loading.setVisibility(LinearLayout.VISIBLE);
		new Thread() {
			public void run() {
				SystemClock.sleep(500);
				// �ж�list���ú��Ƿ��ǿգ�Ҳ�����ǲ��ǵ�һ��ִ�У����ǵĻ����ҵ�������ӵ�list��
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

	//����ͼƬ��ť
	public void back(View view){
		onBackPressed();
	}
}
