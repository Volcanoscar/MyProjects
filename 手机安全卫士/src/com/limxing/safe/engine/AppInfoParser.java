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
 * ��Ȩ����������˰�Ȩ����
 * 
 * �汾��1.0
 * 
 * ���ߣ�������
 * 
 * �������ڣ�2015-5-26 ����9:11:26
 * 
 * ���������ǻ�ȡ�ֻ���������Ӧ�ó�����Ϣ�Ĺ����࣬������һ��javaBean��
 * 
 * 
 * �޶���ʷ��
 * 
 */

public class AppInfoParser {
	/**
	 * ��ȡ�ֻ���������е�Ӧ�ó���
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		// �õ�һ��java��֤�� ����������
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
			// Ӧ�ó���apk����·��
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.setApppath(apkpath);
			File file = new File(apkpath);
			long appSize = file.length();
			appinfo.setAppsize(appSize);
			// Ӧ�ó���װ��λ�á�
			int flags = packInfo.applicationInfo.flags; // ������ӳ�� ��bit-map
			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
				// �ⲿ�洢
				appinfo.setInphone(false);
			} else {
				// �ֻ��ڴ�
				appinfo.setInphone(true);
			}
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				// ϵͳӦ��
				appinfo.setIsuser(false);
			} else {
				// �û�Ӧ��
				appinfo.setIsuser(true);
			}
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
}
