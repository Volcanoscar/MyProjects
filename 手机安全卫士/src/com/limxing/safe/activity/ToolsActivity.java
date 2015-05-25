package com.limxing.safe.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.utils.SmsBackUp;
import com.limxing.safe.utils.ToastUtils;
import com.limxing.safe.utils.SmsBackUp.BackState;

public class ToolsActivity extends Activity {
	private TextView tools_tv_location;
	private TextView tools_tv_smsBackup;
	private TextView tools_tv_smsRestor;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	private void initUI() {
		setContentView(R.layout.tools_main);
		tools_tv_location = (TextView) findViewById(R.id.tools_tv_location);
		tools_tv_smsBackup = (TextView) findViewById(R.id.tools_tv_smsBackup);
		tools_tv_smsRestor = (TextView) findViewById(R.id.tools_tv_smsRestor);

	}

	private void init() {
		// ��ѯ�����ص���¼�
		tools_tv_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ToolsActivity.this,
						ToolLocationActivity.class);
				startActivity(intent);
			}
		});
		// ���ݶ��ŵ���¼�
		tools_tv_smsBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd = new ProgressDialog(ToolsActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setMessage("���ڱ���");
				pd.show();
				new Thread() {
					public void run() {

						try {
							SmsBackUp.backUp(getApplicationContext(),
									new BackState() {

										@Override
										public void process(int process) {
											pd.setProgress(process);

										}

										@Override
										public void beforeBackup(int size) {
											pd.setMax(size);
										}
									});
							ToastUtils.showToast(ToolsActivity.this, "�������");
						} catch (IllegalArgumentException e) {
							ToastUtils.showToast(ToolsActivity.this, "����ʧ��");
							e.printStackTrace();
						} catch (IllegalStateException e) {
							ToastUtils.showToast(ToolsActivity.this,
									e.getMessage());
						} catch (IOException e) {
							ToastUtils.showToast(ToolsActivity.this, "����ʧ��");
							e.printStackTrace();
						} finally {
							pd.dismiss();
						}

					}
				}.start();
			}
		});
		// ��ԭ���ŵ���¼�
		tools_tv_smsRestor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	// ���ذ�ť
	public void back(View view) {
		onBackPressed();

	}
}
