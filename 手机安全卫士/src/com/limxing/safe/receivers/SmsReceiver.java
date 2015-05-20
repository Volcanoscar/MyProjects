package com.limxing.safe.receivers;

import com.limxing.safe.R;
import com.limxing.safe.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private ComponentName componentName;

	@Override
	public void onReceive(Context context, Intent intent) {

		// 获取超级管理员
		final DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(context, MyDeviceAdmin.class);

		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body = smsMessage.getMessageBody();
			if ("#*location*#".equals(body)) {
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
				abortBroadcast();

			} else if ("#*alarm*#".equals(body)) {
				MediaPlayer mediaPlayer = MediaPlayer.create(context,
						R.raw.ylzs);
				mediaPlayer.setVolume(1.0f, 1.0f);
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				abortBroadcast();
			} else if ("#*wipedata*#".equals(body)) {
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				abortBroadcast();

			} else if ("#*lockscreen*#".equals(body)) {
				dpm.resetPassword("123", 0);// 重置锁屏密码
				dpm.lockNow();// 现在锁定
				abortBroadcast();
			}

		}

	}

	private void startActivityForResult(Intent data, int i) {
		// TODO Auto-generated method stub

	}
}
