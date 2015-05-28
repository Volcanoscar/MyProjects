package com.limxing.safe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.limxing.safe.domain.TaskInfo;
import com.limxing.safe.engine.TaskInfoParser;

public class ClearService extends Service {
	private ClearBroadcast receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("清理服务已启动");
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		receiver = new ClearBroadcast();
		registerReceiver(receiver, filter);
		super.onCreate();
	}

	private class ClearBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("开启清理内存");
			List<TaskInfo> TaskInfos = TaskInfoParser
					.getRunningTaskInfos(getApplicationContext());
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for (TaskInfo info : TaskInfos) {
				am.killBackgroundProcesses(info.getPackname());
			}
			
		}

	}

	@Override
	public void onDestroy() {
		System.out.println("清理服务已关闭");
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

		super.onDestroy();
	}

}
