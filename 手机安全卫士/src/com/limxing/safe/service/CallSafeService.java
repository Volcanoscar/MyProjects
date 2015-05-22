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
		//�����绰״̬����
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyPhoneListener();
		mTelephonyManager.listen(mListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		//�������Ź㲥������
		receiver = new MySmsReceiver();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		super.onCreate();
	}

	// �ֻ�ͨ��״̬������
	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = mBlackNumberDao.findMode(incomingNumber);
				if ("1".equals(mode) || "2".equals(mode)) {
					// ��ѯ������������ǵ绰���������ٹҶϵ绰֮ǰ����һ�����ݣ�ͨ����¼���۲��ߣ��ж�����������Ƿ�������
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new CallLogObserver(new Handler(), incomingNumber));
					endCall();

				}
				// ����������������᷵��0��
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

		// ���ݹ۲��ߣ��۲쵽�绰ͨ����¼���ݱ仯��
		@Override
		public void onChange(boolean selfChange) {
			// �رչ۲���
			getContentResolver().unregisterContentObserver(this);
			// ɾ��ͨ����¼����������ķ���
			deleteCallLog(incomingNumber);
			super.onChange(selfChange);
		}

	}

	// ɾ��ͨ����¼
	public void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[] { incomingNumber });

	}

	// �Ҷϵ绰�Ĳ���
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
//�������Ź㲥������
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
