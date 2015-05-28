package com.limxing.safe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DogService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("开启了程序锁服务");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		System.out.println("关闭的程序锁");
		super.onDestroy();
	}

}
