package com.limxing.safe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.limxing.safe.db.dao.BlackNumberDao;

public class CallSafeService extends Service {
	private TelephonyManager mTelephonyManager;
	private BlackNumberDao mBlackNumberDao;
	private MyPhoneListener mListener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBlackNumberDao = new BlackNumberDao(this);
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyPhoneListener();
		mTelephonyManager.listen(mListener,
				PhoneStateListener.LISTEN_CALL_STATE);

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
			//ɾ��ͨ����¼����������ķ���
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
			Class clazz=getClassLoader().loadClass("android.os.ServiceManager");
			Method method=clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony iTelephony=ITelephony.Stub.asInterface(iBinder);
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

}
