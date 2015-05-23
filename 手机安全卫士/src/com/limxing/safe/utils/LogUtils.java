/*
 * File Name: LogUtils.java 
 * History:
 * Created by mwqi on 2014-4-4
 */
package com.limxing.safe.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * 鏃ュ織杈撳嚭鎺у埗绫� (Description)
 * @author limxing
 */
public class LogUtils {
	/** 鏃ュ織杈撳嚭绾у埆NONE */
	public static final int LEVEL_NONE = 0;
	/** 鏃ュ織杈撳嚭绾у埆V */
	public static final int LEVEL_VERBOSE = 1;
	/** 鏃ュ織杈撳嚭绾у埆D */
	public static final int LEVEL_DEBUG = 2;
	/** 鏃ュ織杈撳嚭绾у埆I */
	public static final int LEVEL_INFO = 3;
	/** 鏃ュ織杈撳嚭绾у埆W */
	public static final int LEVEL_WARN = 4;
	/** 鏃ュ織杈撳嚭绾у埆E */
	public static final int LEVEL_ERROR = 5;

	/** 鏃ュ織杈撳嚭鏃剁殑TAG */
	private static String mTag = "mwqi";
	/** 鏄惁鍏佽杈撳嚭log */
	private static int mDebuggable = LEVEL_ERROR;

	/** 鐢ㄤ簬璁版椂鐨勫彉閲� */
	private static long mTimestamp = 0;
	/** 鍐欐枃浠剁殑閿佸璞� */
	private static final Object mLogLock = new Object();

	/** 浠ョ骇鍒负 d 鐨勫舰寮忚緭鍑篖OG */
	public static void v(String msg) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(mTag, msg);
		}
	}

	/** 浠ョ骇鍒负 d 鐨勫舰寮忚緭鍑篖OG */
	public static void d(String msg) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(mTag, msg);
		}
	}

	/** 浠ョ骇鍒负 i 鐨勫舰寮忚緭鍑篖OG */
	public static void i(String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag, msg);
		}
	}

	/** 浠ョ骇鍒负 w 鐨勫舰寮忚緭鍑篖OG */
	public static void w(String msg) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, msg);
		}
	}

	/** 浠ョ骇鍒负 w 鐨勫舰寮忚緭鍑篢hrowable */
	public static void w(Throwable tr) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, "", tr);
		}
	}

	/** 浠ョ骇鍒负 w 鐨勫舰寮忚緭鍑篖OG淇℃伅鍜孴hrowable */
	public static void w(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_WARN && null != msg) {
			Log.w(mTag, msg, tr);
		}
	}

	/** 浠ョ骇鍒负 e 鐨勫舰寮忚緭鍑篖OG */
	public static void e(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, msg);
		}
	}

	/** 浠ョ骇鍒负 e 鐨勫舰寮忚緭鍑篢hrowable */
	public static void e(Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, "", tr);
		}
	}

	/** 浠ョ骇鍒负 e 鐨勫舰寮忚緭鍑篖OG淇℃伅鍜孴hrowable */
	public static void e(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR && null != msg) {
			Log.e(mTag, msg, tr);
		}
	}

	/**
	 * 鎶奓og瀛樺偍鍒版枃浠朵腑
	 * @param log  闇�瑕佸瓨鍌ㄧ殑鏃ュ織
	 * @param path 瀛樺偍璺緞
	 */
	public static void log2File(String log, String path) {
		log2File(log, path, true);
	}

	public static void log2File(String log, String path, boolean append) {
		synchronized (mLogLock) {
			FileUtils.writeFile(log + "\r\n", path, append);
		}
	}

	/**
	 * 浠ョ骇鍒负 e 鐨勫舰寮忚緭鍑簃sg淇℃伅,闄勫甫鏃堕棿鎴筹紝鐢ㄤ簬杈撳嚭涓�涓椂闂存璧峰鐐�
	 * @param msg 闇�瑕佽緭鍑虹殑msg
	 */
	public static void msgStartTime(String msg) {
		mTimestamp = System.currentTimeMillis();
		if (!TextUtils.isEmpty(msg)) {
			e("[Started锛�" + mTimestamp + "]" + msg);
		}
	}

	/** 浠ョ骇鍒负 e 鐨勫舰寮忚緭鍑簃sg淇℃伅,闄勫甫鏃堕棿鎴筹紝鐢ㄤ簬杈撳嚭涓�涓椂闂存缁撴潫鐐�* @param msg 闇�瑕佽緭鍑虹殑msg */
	public static void elapsed(String msg) {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - mTimestamp;
		mTimestamp = currentTime;
		e("[Elapsed锛�" + elapsedTime + "]" + msg);
	}

	public static <T> void printList(List<T> list) {
		if (list == null || list.size() < 1) {
			return;
		}
		int size = list.size();
		i("---begin---");
		for (int i = 0; i < size; i++) {
			i(i + ":" + list.get(i).toString());
		}
		i("---end---");
	}

	public static <T> void printArray(T[] array) {
		if (array == null || array.length < 1) {
			return;
		}
		int length = array.length;
		i("---begin---");
		for (int i = 0; i < length; i++) {
			i(i + ":" + array[i].toString());
		}
		i("---end---");
	}
}
