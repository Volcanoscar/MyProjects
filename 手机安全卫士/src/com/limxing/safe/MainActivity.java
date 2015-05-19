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
	// 享元模式
	private AlertDialog dialog;
	private String[] names = { "手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };
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
					// 判断是否设置了密码
					password = sp.getString("password", "");
					if (TextUtils.isEmpty(password)) {
						// 进入设置密码的dialog
						setPasswordDialog();

					} else {
						// 设置了密码，弹出密码输入框
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
	 * 进入输入密码
	 */
	public void putPasswordDialog() {
		Builder builder = new Builder(this);
		view = View.inflate(this, R.layout.main_putpassword, null);
		builder.setView(view);
		// 点击了确认按钮
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
							ToastUtils.showToast(MainActivity.this, "密码错误");
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
	 * 进入设置密码
	 */
	public void setPasswordDialog() {
		Builder builder = new Builder(MainActivity.this);
		view = View.inflate(MainActivity.this, R.layout.main_setpassword, null);
		builder.setView(view);
		// 点击了确认按钮
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
							ToastUtils.showToast(MainActivity.this, "密码不能为空");
							return;
						}
						if (!_password.equals(et_repassword.getText()
								.toString())) {
							ToastUtils.showToast(MainActivity.this, "两次密码不相同");
							return;
						}
						// 密码你不为空，保存密码到sp中
						Editor editor = sp.edit();
						editor.putString("password", Md5Utils.encode(_password));
						editor.commit();
						ToastUtils.showToast(MainActivity.this, "密码保存成功");
						dialog.dismiss();
					}

				});
		// 点击了取消按钮
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
	 * 界面显示的Adapter
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
