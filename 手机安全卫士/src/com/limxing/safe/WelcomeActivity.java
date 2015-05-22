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
 * 版权：李利锋个人版权所有
 * 
 * 版本：1.0
 * 
 * 作者：李利锋
 * 
 * 创建日期：2015-5-20 下午10:04:44
 * 
 * 描述：
 * 
 * 
 * 修订历史：
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
				builder.setTitle("发现新版本");
				builder.setMessage(desc);
				// 出现对话框点击取消的事件
				builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						openMainActivity();
					}
				});
				builder.setNegativeButton("立即更新", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloadAPP();
					}
				});
				builder.setNegativeButton("下次再说", new OnClickListener() {

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
	 * 多线程下载资源
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
				// 同时设置数据和类型
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "temp.apk")),
						"application/vnd.android.package-archive");
				startActivityForResult(intent, 0);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showToast(WelcomeActivity.this, "下载失败请稍后重试");
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
	 * 检测版本的方法
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
						// 获取网络上的json
						is = conn.getInputStream();
						String json = StreamUtils.readStream(is);
						if (TextUtils.isEmpty(json)) {
							JSONObject jsonObj = new JSONObject(json);
							int serviceVersionCode = jsonObj.getInt("version");
							desc = jsonObj.getString("desc");
							downloadPath = jsonObj.getString("downloadPath");
							if (serviceVersionCode == clientCode) {
								// 版本相同，通知开启主页
								msg.what = OPEN_MAINACTIVITY;

							} else {
								// 版本不同
								msg.what = DOWNLOAD_APP;
							}

						} else {
							// 资源文件内没有内容
							ToastUtils.showToast(WelcomeActivity.this,
									"资源中没有内容");
							msg.what = OPEN_MAINACTIVITY;
						}

					} else {
						// 网络资源不存在原因等
						msg.what = OPEN_MAINACTIVITY;
						ToastUtils.showToast(WelcomeActivity.this, "解析文件不存在");
						msg.what = OPEN_MAINACTIVITY;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "请求路径不存在");
					msg.what = OPEN_MAINACTIVITY;
				} catch (NotFoundException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "没有发现这个资源");
					msg.what = OPEN_MAINACTIVITY;
				} catch (IOException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "IOException");

					msg.what = OPEN_MAINACTIVITY;
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.showToast(WelcomeActivity.this, "json解析失败");
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
					// 让线程睡眠2秒钟
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
	 * 打开主页的方法
	 */
	public void openMainActivity() {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(intent);
		WelcomeActivity.this.finish();
	}

	@Override
	public void onBackPressed() {
	}

	// 初始化数据
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

	// 开启黑名单服务
	public void startBlackService() {
		Intent intent = new Intent(this, CallSafeService.class);
		startService(intent);
	}
	//更新数据库
	public void copyDB(final String dbName){
		//判断是否存在缓存数据库
		new Thread(){
			public void run(){
				File file=new File(getFilesDir(),dbName);
				if(file.exists()&&file.length()>0){
					//数据库存在
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
