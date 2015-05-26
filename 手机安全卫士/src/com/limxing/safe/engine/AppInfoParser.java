package com.limxing.safe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.limxing.safe.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
/**
 * 
 * 版权：李利锋个人版权所有
 * 
 * 版本：1.0
 * 
 * 作者：李利锋
 * 
 * 创建日期：2015-5-26 下午9:11:26
 * 
 * 描述：这是获取手机里面所有应用程序信息的工具类，他依赖一个javaBean类
 * 
 * 
 * 修订历史：
 * 
 */

public class AppInfoParser {
	/**
	 * 获取手机里面的所有的应用程序
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		// 得到一个java保证的 包管理器。
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			AppInfo appinfo = new AppInfo();
			String packname = packInfo.packageName;
			appinfo.setApppack(packname);
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			appinfo.setAppico(icon);
			String appname = packInfo.applicationInfo.loadLabel(pm).toString();
			appinfo.setAppname(appname);
			// 应用程序apk包的路径
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.setApppath(apkpath);
			File file = new File(apkpath);
			long appSize = file.length();
			appinfo.setAppsize(appSize);
			// 应用程序安装的位置。
			int flags = packInfo.applicationInfo.flags; // 二进制映射 大bit-map
			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
				// 外部存储
				appinfo.setInphone(false);
			} else {
				// 手机内存
				appinfo.setInphone(true);
			}
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				// 系统应用
				appinfo.setIsuser(false);
			} else {
				// 用户应用
				appinfo.setIsuser(true);
			}
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
}
