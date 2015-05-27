package com.limxing.safe.activity;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.utils.SmsUtils;
import com.limxing.safe.utils.SmsUtils.ResortState;
import com.limxing.safe.utils.ToastUtils;
import com.limxing.safe.utils.SmsUtils.BackState;

public class ToolsActivity extends Activity {
	private TextView tools_tv_location;
	private TextView tools_tv_smsBackup;
	private TextView tools_tv_smsRestor;
	private TextView tools_tv_applock;
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
		tools_tv_applock = (TextView) findViewById(R.id.tools_tv_applock);
		
	}

	private void init() {
		// 查询归属地点击事件
		tools_tv_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ToolsActivity.this,
						ToolLocationActivity.class);
				startActivity(intent);
			}
		});
		// 备份短信点击事件
		tools_tv_smsBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd = new ProgressDialog(ToolsActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setMessage("正在备份");
				pd.show();
				new Thread() {
					public void run() {

						try {
							SmsUtils.BackUp(getApplicationContext(),
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
							ToastUtils.showToast(ToolsActivity.this, "备份完成");
						} catch (IllegalArgumentException e) {
							ToastUtils.showToast(ToolsActivity.this, "备份失败");
							e.printStackTrace();
						} catch (IllegalStateException e) {
							ToastUtils.showToast(ToolsActivity.this,
									e.getMessage());
						} catch (IOException e) {
							ToastUtils.showToast(ToolsActivity.this, "备份失败");
							e.printStackTrace();
						} finally {
							pd.dismiss();
						}

					}
				}.start();
			}
		});
		// 还原短信点击事件
		tools_tv_smsRestor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd = new ProgressDialog(ToolsActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setMessage("正在还原");
				pd.show();
				new Thread() {
					public void run() {
						try {
							SmsUtils.Restor(getApplicationContext(),
									new ResortState() {

										@Override
										public void process(int process) {
											pd.setProgress(process);

										}

										@Override
										public void beforeBackup(int size) {
											pd.setMax(size);

										}
									});
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							pd.dismiss();
						}

					}
				}.start();
			}
		});
		tools_tv_applock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ToolsActivity.this,ToolAppLockActivity.class);
				startActivity(intent);
				
			}
		});
	}

	// 返回按钮
	public void back(View view) {
		onBackPressed();

	}
}

/*
*/
