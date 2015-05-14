package com.touzila.limxing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Welcome extends Activity {
	private ImageView iv_welcome;
	private SharedPreferences sp;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		sp = getSharedPreferences("welcome", MODE_PRIVATE);
		getPicture();
		goMainActivity();
		try {
			File file = new File(this.getCacheDir(), "welcome.jpg");
			if (file.isFile()) {
				bitmap = BitmapFactory.decodeFile(file.toString());
			} else {
				bitmap = BitmapFactory.decodeStream(this.getAssets().open(
						"psb.jpg"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		iv_welcome.setImageBitmap(bitmap);
	}

	// 从服务器端下载欢迎页图片到缓存中的功能
	public void getPicture() {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(
							"http://192.168.17.30:8080/psb/welcome.jpg");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					if (conn.getResponseCode() == 200) {
						InputStream input = conn.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(input);
						bitmap.compress(
								Bitmap.CompressFormat.JPEG,
								50,
								new FileOutputStream(new File(Welcome.this
										.getCacheDir(), "welcome.jpg")));
						input.close();
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
}
