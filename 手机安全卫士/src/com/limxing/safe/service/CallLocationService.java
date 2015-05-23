package com.limxing.safe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.db.dao.NumberAddressDao;

public class CallLocationService extends Service {
	private TelephonyManager mTelephonyManager;
	private MyListener mMyListener;
	private WindowManager mWindowManager;
	private OutCallReceiver mOutCallReceiver;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		sp = getSharedPreferences("info", MODE_PRIVATE);
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		// 来电监听
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mMyListener = new MyListener();
		mTelephonyManager.listen(mMyListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		// 去电
		mOutCallReceiver = new OutCallReceiver();
		registerReceiver(mOutCallReceiver, new IntentFilter(
				Intent.ACTION_NEW_OUTGOING_CALL));

		super.onCreate();
	}

	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String location = NumberAddressDao.findNumber(number);
			showToast(location);
		}

	}

	// 电话状态监听
	private class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					mWindowManager.removeView(view);
					view = null;
				}

				break;

			case TelephonyManager.CALL_STATE_RINGING:
				String location = NumberAddressDao.findNumber(incomingNumber);
				showToast(location);

				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

	}

	// 自定义土司Toast
	private WindowManager.LayoutParams params;
	private View view;
	private int[] skins = { R.drawable.call_locate_white,
			R.drawable.call_locate_orange, R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green };

	public void showToast(String location) {
		view = View.inflate(this, R.layout.location_toast, null);
		TextView location_toast_tv = (TextView) view
				.findViewById(R.id.location_toast_tv);
		location_toast_tv.setText(location);
		view.findViewById(R.id.location_toast_ll).setBackgroundResource(skins[sp.getInt("skin", 0)]);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 自定义的土司需要用户触摸
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST; // 土司窗体天生不响应触摸事件
		// params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		mWindowManager.addView(view, params);

	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mOutCallReceiver);
		mOutCallReceiver = null;
		mTelephonyManager.listen(mMyListener, PhoneStateListener.LISTEN_NONE);
		mMyListener = null;
		super.onDestroy();
	}

}
