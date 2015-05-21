package com.touzila.limxing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.touzila.utils.StreamUtils;
import com.touzila.utils.ToastUtils;

public class Welcome extends Activity {
	protected static final int GO_MAINACTIVITY = 1;
	protected static final int SHOW_UPLOAD_DIALOG = 2;
	private SharedPreferences sp;
	private ImageView iv_welcome;
	private TextView tv_welcome_load;
	private ProgressBar pb_welcome_load;
	private Bitmap bitmap;
	private int clientCode;
	private String picPath;
	private String downloadPath;
	private String desc;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_MAINACTIVITY:
				goMainActivity();
				break;
			case SHOW_UPLOAD_DIALOG:
				Builder builder = new Builder(Welcome.this);
				builder.setTitle("发现新版本");
				builder.setMessage(desc);
				// 点击返回按钮后的操作
				builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						goMainActivity();
					}
				});
				builder.setPositiveButton("立即更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						goDownloadApp();
						
					}

				});
				builder.setNegativeButton("以后再说", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						goMainActivity();
					}

				});
				builder.show();
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		pb_welcome_load = (ProgressBar) findViewById(R.id.pb_welcome_load);

		tv_welcome_load = (TextView) findViewById(R.id.tv_welcome_load);
		// 判断应用缓存中是否含有Welcome图片，如果有则欢迎页使用这个，如果没有则使用app中assects中的照片
		File file = new File(this.getCacheDir(), "welcome.png");
		if (file.isFile()) {
			bitmap = BitmapFactory.decodeFile(file.toString());
			iv_welcome.setImageBitmap(bitmap);
		} else {
			iv_welcome.setImageResource(R.drawable.w520);
		}
		try {
			//获取版本信息
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			clientCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 如果是wifi联网则下载图片，和检查更新
		if (!isWifi(this.getApplicationContext())) {
			checkPicture();
			checkVersion();
		} else {
			goMainActivity();
		}

	}

	/**
	 * 检查图片是否要更新,原理是判断服务器上是否含有Welcome这个文件，只要存在就下载
	 */
	private void checkPicture() {
		
		new Thread() {
			public void run() {
				try {
					picPath = getResources().getString(R.string.welcomeurl);
					URL picUrl = new URL(picPath);
					HttpURLConnection conn = (HttpURLConnection) picUrl
							.openConnection();
					conn.setConnectTimeout(2000);
					int code = conn.getResponseCode();
					if (code == 200) {
						downloadPicture(picPath);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 检查版本更新
	 */
	private void checkVersion() {
		
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				InputStream is = null;
				try {
					// 获取版本信息网络的请求路径
					URL appUrl = new URL(getResources().getString(
							R.string.versionurl));
					HttpURLConnection conn = (HttpURLConnection) appUrl
							.openConnection();
					
					conn.setConnectTimeout(3000);
					
					int code = conn.getResponseCode();
					
					if (code == 200) {
						is = conn.getInputStream();
						String json = StreamUtils.readStream(is);
						
						if (TextUtils.isEmpty(json)) {
							ToastUtils.showToast(Welcome.this,
									"新版本下载失败，稍后重试(001)");
							msg.what = GO_MAINACTIVITY;
						} else {
							JSONObject jsonObj = new JSONObject(json);
							downloadPath = jsonObj.getString("downloadurl");
							
							desc = jsonObj.getString("desc");
							
							int serveiceVersionCode = jsonObj.getInt("version");
							
							if (serveiceVersionCode == clientCode) {
								// 版本相同
								msg.what = GO_MAINACTIVITY;
							} else {
								// 版本不同提示下载去下载
								msg.what = SHOW_UPLOAD_DIALOG;
							}
						}
					} else {
						ToastUtils.showToast(Welcome.this, "路径请求失败，稍后重试(002)");
						msg.what = GO_MAINACTIVITY;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					ToastUtils.showToast(Welcome.this, "更新有误，稍后重试(003)");
					e.printStackTrace();
					msg.what = GO_MAINACTIVITY;
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtils.showToast(Welcome.this, "更新有误，稍后重试(004)");
					msg.what = GO_MAINACTIVITY;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					ToastUtils.showToast(Welcome.this, "更新有误，稍后重试(005)");
					msg.what = GO_MAINACTIVITY;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtils.showToast(Welcome.this, "更新有误，稍后重试(006)");
					msg.what = GO_MAINACTIVITY;
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}
			}
		}.start();

	}

	/**
	 * 从服务器端下载欢迎页图片到缓存中的功能
	 */
	public void downloadPicture(String pictureUrl) {
		// 下载的工具类
		HttpUtils http = new HttpUtils();
		http.download(pictureUrl, Welcome.this.getCacheDir().toString()
				+ "/welcome.jpg", new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 自己没有用工具类写的下载代码
	 */
	// public void getPicture() {
	// new Thread() {
	// public void run() {
	// try {
	// // 获取资源中的图片路径
	// URL url = new URL(getResources().getString(
	// R.string.welcomeurl));
	// conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5000);
	// // 判断网络资源是否存在，否则不执行下载
	// if (conn.getResponseCode() == 200) {
	// InputStream input = conn.getInputStream();
	// Bitmap bitmap = BitmapFactory.decodeStream(input);
	// bitmap.compress(
	// Bitmap.CompressFormat.JPEG,
	// 50,
	// new FileOutputStream(new File(Welcome.this
	// .getCacheDir(), "welcome.jpg")));
	// input.close();
	// } else {
	// return;
	// }
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// }.start();
	// }
	/**
	 * 下载新版本
	 */
	public void goDownloadApp() {
		
		pb_welcome_load.setVisibility(ProgressBar.VISIBLE);
		tv_welcome_load.setText("正在下载新版本...");
		HttpUtils http = new HttpUtils();
		http.download(downloadPath, Environment.getExternalStorageDirectory()
				.toString() + "/touzila.apk", new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// 下载成功调用系统安装软件
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "touzila.apk")),
						"application/vnd.android.package-archive");
				// 开启安装的结果，如果没有安装则调用onActivityResult去启动主界面
				startActivityForResult(intent, 0);

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showToast(Welcome.this, "下载失败，请稍候再试！");
				goMainActivity();

			}

			public void onLoading(long total, long current, boolean isUploading) {
				int progress = (int) (current / total * 100);
				pb_welcome_load.setProgress(progress);
				super.onLoading(total, current, isUploading);
			}
		});

	}

	// 用户下载完毕不进行安装，直接返回，则调用这个方法
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		goMainActivity();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 进入软件主界面
	 */
	public void goMainActivity() {
		// 判断是否是第一次打开应用
		if (sp.getInt("welcome", 0) == 0) {
			System.out.println("第一次打开应用");
			Intent intent = new Intent(Welcome.this, MainActivity.class);
			startActivity(intent);
			Welcome.this.finish();
		} else {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent(Welcome.this, MainActivity.class);
					startActivity(intent);
					Welcome.this.finish();
				}
			}.start();

		}

	}

	/**
	 * 判断是否是wifi联网
	 * 
	 * @param mContext
	 * @return
	 */
	private static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {

	}
}
