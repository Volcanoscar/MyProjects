package com.limxing.safe.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.db.dao.NumberAddressDao;
import com.limxing.safe.utils.ToastUtils;

/*
 * 作者：Limxing 时间： 2015-5-22 下午11:05:28
 * 
 * 描述：这是归属地查询的Activity类
 */
public class ToolLocationActivity extends Activity {
	private EditText tool_location_et;
	private Button tool_location_bt;
	private TextView tool_location_tv;
	private String mNumber;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	private void init() {
		// 文本框的实时查询
		tool_location_et.addTextChangedListener(new TextWatcher() {

			private String location;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String number = s.toString();
				location = NumberAddressDao.findNumber(number);
				tool_location_tv.setText("号码归属地为：" + location);
			}
		});
		// 按钮的点击事件
		tool_location_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNumber = tool_location_et.getText().toString().trim();
				if (TextUtils.isEmpty(mNumber)) {
					//增加控件抖动效果
					Animation shake = AnimationUtils.loadAnimation(
							ToolLocationActivity.this, R.anim.shake);
					tool_location_et.startAnimation(shake);
					//增加手机震动效果
					vibrator = (Vibrator) getApplication().getSystemService(
							Service.VIBRATOR_SERVICE);
					vibrator.vibrate(200);
					ToastUtils.showToast(ToolLocationActivity.this, "请输入号码");
					return;
				}
				String location = NumberAddressDao.findNumber(mNumber);
				tool_location_tv.setText("号码归属地为：" + location);

			}
		});

	}

	private void initUI() {
		setContentView(R.layout.tools_location);
		tool_location_et = (EditText) findViewById(R.id.tools_location_et);
		tool_location_bt = (Button) findViewById(R.id.tool_location_bt);
		tool_location_tv = (TextView) findViewById(R.id.tool_location_tv);

	}

	// 返回键
	public void back(View view) {
		onBackPressed();
	}

}
