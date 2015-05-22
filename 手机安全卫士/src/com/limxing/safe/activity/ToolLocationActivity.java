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
 * ���ߣ�Limxing ʱ�䣺 2015-5-22 ����11:05:28
 * 
 * ���������ǹ����ز�ѯ��Activity��
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
		// �ı����ʵʱ��ѯ
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
				tool_location_tv.setText("���������Ϊ��" + location);
			}
		});
		// ��ť�ĵ���¼�
		tool_location_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNumber = tool_location_et.getText().toString().trim();
				if (TextUtils.isEmpty(mNumber)) {
					//���ӿؼ�����Ч��
					Animation shake = AnimationUtils.loadAnimation(
							ToolLocationActivity.this, R.anim.shake);
					tool_location_et.startAnimation(shake);
					//�����ֻ���Ч��
					vibrator = (Vibrator) getApplication().getSystemService(
							Service.VIBRATOR_SERVICE);
					vibrator.vibrate(200);
					ToastUtils.showToast(ToolLocationActivity.this, "���������");
					return;
				}
				String location = NumberAddressDao.findNumber(mNumber);
				tool_location_tv.setText("���������Ϊ��" + location);

			}
		});

	}

	private void initUI() {
		setContentView(R.layout.tools_location);
		tool_location_et = (EditText) findViewById(R.id.tools_location_et);
		tool_location_bt = (Button) findViewById(R.id.tool_location_bt);
		tool_location_tv = (TextView) findViewById(R.id.tool_location_tv);

	}

	// ���ؼ�
	public void back(View view) {
		onBackPressed();
	}

}
