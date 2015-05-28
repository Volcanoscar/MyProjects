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
 * 任务信息 & 进程信息的解析器
 * 
 * @author Administrator
 * 
 */
public class TaskInfoParser {

	/**
	 * 获取正在运行的所有的进程的信息。
	 * 
	 * @param context
	 *            上下文
	 * @return 进程信息的集合
	 */
	public static List<TaskInfo> getRunningTaskInfos(Context context) {
		//获取活动的进程管理器
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		//通过进程管理器获取包管理器
		PackageManager pm = context.getPackageManager();
		//得到正在运行的应用进程
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		//创建我们自定义对象的集合
		 List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		 //遍历正在运行的进程的集合
		for (RunningAppProcessInfo processInfo : processInfos) {
			//创建自定义的线程对象
			TaskInfo taskInfo = new TaskInfo();
			//得到进程的包名
			String packname = processInfo.processName;
			taskInfo.setPackname(packname);
			//通过进程的pid得到进程内存占用信息
			MemoryInfo[]  memroyinfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
			//得到内存占用值
			long memsize = memroyinfos[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemsize(memsize);
			try {
				//通过包管理器，根据包名获得程序的所有信息
				PackageInfo packInfo = pm.getPackageInfo(packname, 0);
				//得到程序的图标
				Drawable icon = packInfo.applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);
				//得到程序的名称
				String appname = packInfo.applicationInfo.loadLabel(pm).toString();
				taskInfo.setAppname(appname);
				//根据包名的记号与系统的几号相与，相同即等于0则是系统进程
				if((ApplicationInfo.FLAG_SYSTEM&packInfo.applicationInfo.flags)!=0){
					//系统进程
					taskInfo.setUsertask(false);
				}else{
					//用户进程
					taskInfo.setUsertask(true);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				//如果不能找到名称信息，则使用包名，图标设置为系统默认的图标
				taskInfo.setAppname(packname);
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
			}
			//最后把所有的对象添加到集合中，并返回
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
