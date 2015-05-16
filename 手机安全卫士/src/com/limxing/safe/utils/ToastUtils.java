package com.limxing.safe.utils;

import android.app.Activity;
import android.widget.Toast;
/**
 * ��˿�Ĺ����࣬�ж��Ƿ������̣߳�������������߳��е��õ�Toast���½��߳�Toast
 * @author lenovo
 *
 */
public class ToastUtils {
	public static void showToast(final Activity context, final String msg) {
		if ("main".equals(Thread.currentThread().getName())) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		} else {
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}

			});

		}
	}

}
