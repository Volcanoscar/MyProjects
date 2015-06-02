package com.limxing.safe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.animation.AnimatorSet.Builder;
import android.app.Application;
import android.os.Environment;

public class SafeApplication extends Application {
	/**
	 * 应用程序第一个被执行的方法
	 */
	@Override
	public void onCreate() {
		Thread.currentThread().setUncaughtExceptionHandler(
				new MyExceptionHandler());
		super.onCreate();
	}

	// 没有捕获异常执行的适配器
	private class MyExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			System.out.println("出现的没有捕获的异常");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			// 反射获取成员变量
			Field[] fileds = Builder.class.getDeclaredFields();
			for (Field field : fileds) {
				try {
					sw.write(field.getName() + "--" + field.get(null) + "\n");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			ex.printStackTrace(pw);
			File file = new File(Environment.getExternalStorageDirectory(),
					"log.txt");
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sw.toString().getBytes());
				fos.close();
				pw.close();
				sw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 如果程序在某界面出现异常就关闭掉这个界面
			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}

	/**
	 * 模拟器调试
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

}
