package com.limxing.safe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class SystemInfoUtils {

	public static boolean isServiceRunning(Context context, String className) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取进程中的服务，200表示获取其中的200个存放在集合中
		List<RunningServiceInfo> infos = am.getRunningServices(200);
		for (RunningServiceInfo info : infos) {
			String serviceClassName = info.service.getClassName();
			if (className.equals(serviceClassName)) {
				return true;
			}
		}
		return false;

	}

}
