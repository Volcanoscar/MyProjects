package com.limxing.safe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.limxing.safe.service.CallSafeService;
import com.limxing.safe.utils.StreamUtils;
import com.limxing.safe.utils.ToastUtils;

/**
 * 
 * ��Ȩ����������˰�Ȩ����
 * 
 * �汾��1.0
 * 
 * ���ߣ�������
 * 
 * �������ڣ�2015-5-20 ����10:04:44
 * 
 * ������
 * 
 * 
 * �޶���ʷ��
 * 
 */
public class WelcomeActivity extends Activity {
	protected static final int OPEN_MAINACTIVITY = 1;
	protected static final int DOWNLOAD_APP = 2;
	private int clientCode;
	private String clientName;
	private String desc;
	private String downloadPath;
	private ProgressBar pb_splash_download;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OPEN_MAINACTIVITY:
				openMainActivity();
				break;

			case DOWNLOAD_APP:
				Builder builder = new Builder(WelcomeActivity.this);
				builder.setTitle("�����°汾");
				builder.setMessage(desc);
				// ���ֶԻ�����ȡ�����¼�
				builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						openMainActivity();
					}
				});
				builder.setNegativeButton("��������", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloadAPP();
					}
				});
				builder.setNegativeButton("�´���˵", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						openMainActivity();

					}

				});
				builder.show();
				break;

			}

		}

	};

	/**
	 * ���߳�������Դ
	 */

	public void downloadAPP() {
		pb_splash_download.setVisibility(ProgressBar.VISIBLE);
		HttpUtils http = new HttpUtils();
		http.download(downloadPath, WelcomeActivity.this.getCacheDir()
				.toString() + "/safe.apk", new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				// ͬʱ�������ݺ�����
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "temp.apk")),
						"application/vnd.android.package-archive");
				startActivityForResult(intent, 0);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showToast(WelcomeActivity.this, "����ʧ�����Ժ�����");
				openMainActivity();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				int progress = (int) (current / total * 100);
				pb_splash_download.setProgress(progress);
				super.onLoading(total, current, isUploading);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		openMainActivity();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		init();

	}

	/**
	 * ���汾�ķ���
	 */
	private void checkVersion() {
		new Thread() {
			public void run() {

				InputStream is = null;
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {

					URL checkUrl = new URL(getResources().getString(
							R.string.versioncheckurl));

					HttpURLConnection conn = (HttpURLConnection) checkUrl
							.openConnection();
					conn.setConnectTimeout(3000);
					if (conn.getResponseCode() == 200) {
						// ��ȡ�����ϵ�json
						is = conn.getInputStream();
						String json = StreamUtils.readStream(is);
						if (TextUtils.isEmpty(json)) {
							JSONObject jsonObj = new JSONObject(json);
							int serviceVersionCode = jsonObj.getInt("version");
							desc = jsonObj.getString("desc");
							downloadPath = jsonObj.getString("downloadPath");
							if (serviceVersionCode == clientCode) {
								// �汾��ͬ��֪ͨ������ҳ
								msg.what = OPEN_MAINACTIVITY;

							} else {
								// �汾��ͬ
								msg.what = DOWNLOAD_APP;
							}

						} else {
							// ��Դ�ļ���û������
							ToastUtils.showToast(WelcomeActivity.this,
									"��Դ��û������");
							msg.what = OPEN_MAINACTIVITY;
						}

					} else {
						// ������Դ������ԭ���
						msg.what = OPEN_MAINACTIVITY;
						ToastUtils.showToast(WelcomeActivity.this, "�����ļ�������");
						msg.what = OPEN_MAINACTIVITY;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "����·��������");
					msg.what = OPEN_MAINACTIVITY;
				} catch (NotFoundException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "û�з��������Դ");
					msg.what = OPEN_MAINACTIVITY;
				} catch (IOException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "IOException");

					msg.what = OPEN_MAINACTIVITY;
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "json����ʧ��");
					msg.what = OPEN_MAINACTIVITY;
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// ���߳�˯��2����
					long endTime = System.currentTimeMillis();
					long time = endTime - startTime;
					if (time < 2000) {
						SystemClock.sleep(2000 - time);
						handler.sendMessage(msg);
					} else {
						handler.sendMessage(msg);
					}
				}
			}
		}.start();
	}

	/**
	 * ����ҳ�ķ���
	 */
	public void openMainActivity() {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(intent);
		WelcomeActivity.this.finish();
	}

	@Override
	public void onBackPressed() {
	}

	// ��ʼ������
	public void init() {
		copyDB("address.db");
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			clientName = pi.packageName;
			clientCode = pi.versionCode;
			if (getSharedPreferences("info", MODE_PRIVATE).getBoolean(
					"isUpdate", true)) {
				checkVersion();
			} else {
				new Thread() {
					public void run() {
						SystemClock.sleep(2000);
						openMainActivity();
					}
				}.start();

			}
			if (getSharedPreferences("info", MODE_PRIVATE).getBoolean(
					"isBlack", true)) {
				startBlackService();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	// ��������������
	public void startBlackService() {
		Intent intent = new Intent(this, CallSafeService.class);
		startService(intent);
	}
	//�������ݿ�
	public void copyDB(final String dbName){
		//�ж��Ƿ���ڻ������ݿ�
		new Thread(){
			public void run(){
				File file=new File(getFilesDir(),dbName);
				if(file.exists()&&file.length()>0){
					//���ݿ����
					return;
				}
				try {
					InputStream is=getAssets().open(dbName);
					FileOutputStream fos=openFileOutput(dbName, MODE_PRIVATE);
					byte[] buffer=new byte[1024];
					int len=0;
					while((len=is.read(buffer))!=-1){
						fos.write(buffer,0,len);
					}
					is.close();
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		
	}

}
