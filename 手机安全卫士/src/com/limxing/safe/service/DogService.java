package com.limxing.safe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.limxing.safe.activity.AppLockPwd;
import com.limxing.safe.db.dao.AppLockDao;

/*
 * 作者：Limxing 时间： 2015-5-29 下午12:42:34
 * 
 * 描述：程序锁监控服务
 */
public class DogService extends Service {
	// 数据库中锁定的程序
	private List<String> lockedApps;
	// 已经解锁白名单
	private List<String> unlockApps;
	// 当前执行的程序，是任务栈中的程序
	private List<RunningTaskInfo> gainApps;
	private String packname;
	private boolean flag;
	private ActivityManager am;
	private AppLockDao dao;
	private Intent intent;
	private DogReceiver receiver;
	private ContentObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new AppLockDao(this);
		lockedApps = dao.findAll();
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		unlockApps = new ArrayList<String>();
		intent = new Intent(this, AppLockPwd.class);
		// 创建内容观察者
		observer = new DogObserver(new Handler());
		getContentResolver()
				.registerContentObserver(
						Uri.parse("content://com.limxing.safe.applock"), true,
						observer);
		// 注册广播
		receiver = new DogReceiver();
		IntentFilter filter = new IntentFilter("com.limxing.safe.pass");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);

		startDog();
	}

	// 看门狗开启监控
	public void startDog() {
		flag = true;
		new Thread() {
			public void run() {
				while (flag) {
					// 获取最近运行的程序
					gainApps = am.getRunningTasks(1);
					packname = gainApps.get(0).topActivity.getPackageName();
					if (lockedApps.contains(packname)) {
						if (!unlockApps.contains(packname)) {
							// 是锁定的应用,打开密码输入界面
							intent.putExtra("packname", packname);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}.start();
	}

	// 广播接收者
	private class DogReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.limxing.safe.pass".equals(intent.getAction())) {
				String pack = intent.getStringExtra("packname");
				unlockApps.add(pack);
			}
			if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				unlockApps.clear();
				flag = false;
			}
			if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
				if (flag == false) {
					startDog();
				}
			}

		}

	}

	// 内容观察者，数据库
	private class DogObserver extends ContentObserver {

		public DogObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			lockedApps = dao.findAll();
		}

	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(receiver);
		receiver = null;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
		super.onDestroy();
	}

}
