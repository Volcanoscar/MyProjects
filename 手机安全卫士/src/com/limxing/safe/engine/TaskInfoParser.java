package com.limxing.safe.engine;

import java.util.ArrayList;
import java.util.List;

import com.limxing.safe.R;
import com.limxing.safe.domain.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

/**
 * ������Ϣ & ������Ϣ�Ľ�����
 * 
 * @author Administrator
 * 
 */
public class TaskInfoParser {

	/**
	 * ��ȡ�������е����еĽ��̵���Ϣ��
	 * 
	 * @param context
	 *            ������
	 * @return ������Ϣ�ļ���
	 */
	public static List<TaskInfo> getRunningTaskInfos(Context context) {
		//��ȡ��Ľ��̹�����
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		//ͨ�����̹�������ȡ��������
		PackageManager pm = context.getPackageManager();
		//�õ��������е�Ӧ�ý���
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		//���������Զ������ļ���
		 List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		 //�����������еĽ��̵ļ���
		for (RunningAppProcessInfo processInfo : processInfos) {
			//�����Զ�����̶߳���
			TaskInfo taskInfo = new TaskInfo();
			//�õ����̵İ���
			String packname = processInfo.processName;
			taskInfo.setPackname(packname);
			//ͨ�����̵�pid�õ������ڴ�ռ����Ϣ
			MemoryInfo[]  memroyinfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
			//�õ��ڴ�ռ��ֵ
			long memsize = memroyinfos[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemsize(memsize);
			try {
				//ͨ���������������ݰ�����ó����������Ϣ
				PackageInfo packInfo = pm.getPackageInfo(packname, 0);
				//�õ������ͼ��
				Drawable icon = packInfo.applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);
				//�õ����������
				String appname = packInfo.applicationInfo.loadLabel(pm).toString();
				taskInfo.setAppname(appname);
				//���ݰ����ļǺ���ϵͳ�ļ������룬��ͬ������0����ϵͳ����
				if((ApplicationInfo.FLAG_SYSTEM&packInfo.applicationInfo.flags)!=0){
					//ϵͳ����
					taskInfo.setUsertask(false);
				}else{
					//�û�����
					taskInfo.setUsertask(true);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				//��������ҵ�������Ϣ����ʹ�ð�����ͼ������ΪϵͳĬ�ϵ�ͼ��
				taskInfo.setAppname(packname);
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
			}
			//�������еĶ�����ӵ������У�������
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
