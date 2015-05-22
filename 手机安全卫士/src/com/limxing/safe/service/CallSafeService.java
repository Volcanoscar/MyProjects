package com.limxing.safe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.limxing.safe.db.dao.BlackNumberDao;
import com.limxing.safe.receivers.SmsReceiver;

public class CallSafeService extends Service {
	private TelephonyManager mTelephonyManager;
	private BlackNumberDao mBlackNumberDao;
	private MyPhoneListener mListener;
	private MySmsReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mBlackNumberDao = new BlackNumberDao(this);
		//开启电话状态监听
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyPhoneListener();
		mTelephonyManager.listen(mListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		//开启短信广播接受者
		receiver = new MySmsReceiver();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		super.onCreate();
	}

	// 手机通话状态监听器
	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = mBlackNumberDao.findMode(incomingNumber);
				if ("1".equals(mode) || "2".equals(mode)) {
					// 查询出来这个号码是电话黑名单，再挂断电话之前开启一个内容（通话记录）观察者，判断里面的内容是否增加了
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new CallLogObserver(new Handler(), incomingNumber));
					endCall();

				}
				// 如果不存在这个号码会返回0，
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);

		}

	}

	private class CallLogObserver extends ContentObserver {
		private String incomingNumber;

		public CallLogObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		// 内容观察者，观察到电话通话记录数据变化了
		@Override
		public void onChange(boolean selfChange) {
			// 关闭观察者
			getContentResolver().unregisterContentObserver(this);
			// 删除通话记录，调用下面的方法
			deleteCallLog(incomingNumber);
			super.onChange(selfChange);
		}

	}

	// 删除通话记录
	public void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[] { incomingNumber });

	}

	// 挂断电话的操作
	public void endCall() {
		try {
			Class clazz = getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			iTelephony.endCall();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
//创建短信广播接收者
	private class MySmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String smsName = smsMessage.getOriginatingAddress();
				String mode = mBlackNumberDao.findMode(smsName);
				if ("1".equals(mode) || "3".equals(mode)) {
					abortBroadcast();
				}
			}

		}
	}
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
