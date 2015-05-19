package com.limxing.safe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.limxing.safe.activity.LostFindActivity;
import com.limxing.safe.activity.SetActivity;
import com.limxing.safe.utils.Md5Utils;
import com.limxing.safe.utils.ToastUtils;

public class MainActivity extends Activity {
	private GridView gv_main_items;
	private SharedPreferences sp;
	private View view;
	private String password;
	// ��Ԫģʽ
	private AlertDialog dialog;
	private String[] names = { "�ֻ�����", "ͨѶ��ʿ", "����ܼ�", "���̹���", "����ͳ��", "�ֻ�ɱ��",
			"��������", "�߼�����", "��������" };
	private int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		gv_main_items = (GridView) findViewById(R.id.gv_main_items);
		gv_main_items.setAdapter(new MyAdapter());
		gv_main_items.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					// �ж��Ƿ�����������
					password = sp.getString("password", "");
					if (TextUtils.isEmpty(password)) {
						// �������������dialog
						setPasswordDialog();

					} else {
						// ���������룬�������������
						putPasswordDialog();
					}
					break;

				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 8:
					Intent intent=new Intent(MainActivity.this,SetActivity.class);
					startActivity(intent);
					break;
				}

			}
		});
	}

	/**
	 * ������������
	 */
	public void putPasswordDialog() {
		Builder builder = new Builder(this);
		view = View.inflate(this, R.layout.main_putpassword, null);
		builder.setView(view);
		// �����ȷ�ϰ�ť
		view.findViewById(R.id.bt_confirm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						EditText et_password = (EditText) view
								.findViewById(R.id.et_password);
						if (Md5Utils.encode(et_password.getText().toString())
								.equals(password)) {
							Intent intent = new Intent(MainActivity.this,
									LostFindActivity.class);
							startActivity(intent);
							dialog.dismiss();
						} else {
							ToastUtils.showToast(MainActivity.this, "�������");
							et_password.setText("");
							return;
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
		dialog = builder.show();

	}

	/**
	 * ������������
	 */
	public void setPasswordDialog() {
		Builder builder = new Builder(MainActivity.this);
		view = View.inflate(MainActivity.this, R.layout.main_setpassword, null);
		builder.setView(view);
		// �����ȷ�ϰ�ť
		view.findViewById(R.id.bt_confirm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						EditText et_password = (EditText) view
								.findViewById(R.id.et_password);
						EditText et_repassword = (EditText) view
								.findViewById(R.id.et_repassword);
						String _password = et_password.getText().toString();
						if (TextUtils.isEmpty(_password)
								&& TextUtils.isEmpty(et_repassword.getText()
										.toString())) {
							ToastUtils.showToast(MainActivity.this, "���벻��Ϊ��");
							return;
						}
						if (!_password.equals(et_repassword.getText()
								.toString())) {
							ToastUtils.showToast(MainActivity.this, "�������벻��ͬ");
							return;
						}
						// �����㲻Ϊ�գ��������뵽sp��
						Editor editor = sp.edit();
						editor.putString("password", Md5Utils.encode(_password));
						editor.commit();
						ToastUtils.showToast(MainActivity.this, "���뱣��ɹ�");
						dialog.dismiss();
					}

				});
		// �����ȡ����ť
		view.findViewById(R.id.bt_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog = builder.show();

	}

	/**
	 * ������ʾ��Adapter
	 * 
	 * @author lenovo
	 * 
	 */
	protected class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(MainActivity.this, R.layout.main_items,
					null);
			ImageView iv_main_items = (ImageView) view
					.findViewById(R.id.iv_main_items);
			TextView tv_main_items = (TextView) view
					.findViewById(R.id.tv_main_items);
			iv_main_items.setImageResource(icons[position]);
			tv_main_items.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
