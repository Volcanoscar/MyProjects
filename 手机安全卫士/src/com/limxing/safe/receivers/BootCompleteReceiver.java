package com.limxing.safe.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("info",
				Context.MODE_PRIVATE);
		if (sp.getBoolean("protectstate", false)) {
			// 开启了保护则判断SIm卡是不是更换了
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// 如果sim卡与之前保存的Sim卡号不相同则发送短信
			if (tm.getSimSerialNumber().equals(sp.getString("sim", ""))) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(sp.getString("safeNum", ""), null,
						"SIM Changed!", null, null);

			}
		}
	}

}
