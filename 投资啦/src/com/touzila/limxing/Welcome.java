package com.touzila.limxing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Welcome extends Activity {
	private SharedPreferences sp;
	private HttpURLConnection conn;
	private ImageView iv_welcome;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		// 判断应用缓存中是否含有Welcome图片，如果有则欢迎页使用这个，如果没有则使用app中assects中的照片
		try {
			File file = new File(this.getCacheDir(), "welcome.jpg");
			if (file.isFile()) {
				bitmap = BitmapFactory.decodeFile(file.toString());
			} else {
				bitmap = BitmapFactory.decodeStream(this.getAssets().open(
						"psb.jpg"));
			}
			iv_welcome.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 如果是wifi联网则下载图片
		if (!isWifi(this.getApplicationContext())) {
			getPicture();
		}
		
		// 判断是否是第一次打开应用
		if (sp.getInt("welcome", 0) == 0) {
			// 3秒后开启主页面
			System.out.println("第一次打开应用");
			goMainActivity();
		} else {
			System.out.println("不是第一次打开应用");
			goMainActivity();
		}
	}

	/**
	 * 从服务器端下载欢迎页图片到缓存中的功能
	 */
	public void getPicture() {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(
							"http://192.168.31.216:8080/psb/welcome.jpg");
					conn = (HttpURLConnection) url.openConnection();
					// 判断网络资源是否存在，否则不执行下载
					if (conn.getResponseCode() == 200) {
						InputStream input = conn.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(input);
						bitmap.compress(
								Bitmap.CompressFormat.JPEG,
								50,
								new FileOutputStream(new File(Welcome.this
										.getCacheDir(), "welcome.jpg")));
						input.close();
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	/**
	 * 延时3秒钟进入软件主界面
	 */
	public void goMainActivity() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(Welcome.this, MainActivity.class);
				startActivity(intent);
				Welcome.this.finish();
			}
		}, 3000);
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
