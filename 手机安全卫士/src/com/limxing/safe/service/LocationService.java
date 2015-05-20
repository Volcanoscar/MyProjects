package com.limxing.safe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {
	private LocationManager locationManager;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	/**
	 * 服务一启动就要获取位置信息，并发送经纬度
	 */
	@Override
	public void onCreate() {
		// 1、获取系统位置管理者
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyListener();
		// 2、得到位置提供者，gps 网络定位，基站定位。。。
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 设置为获取精确的位置
		criteria.setCostAllowed(true);// 设置可以消耗流量费用
		// 获取最好的位置提供者
		String name = locationManager.getBestProvider(criteria, true);
		//
		locationManager.requestLocationUpdates(name, 0, 0, listener);
		super.onCreate();

	}

	class MyListener implements LocationListener {
		/**
		 * 位置发生改变时
		 */
		@Override
		public void onLocationChanged(Location location) {
			StringBuilder sb = new StringBuilder();
			sb.append("Accuracy" + location.getAccuracy());
			sb.append("speed" + location.getSpeed());
			sb.append("latitude" + location.getLatitude());
			sb.append("longitude" + location.getLongitude());
			sb.append("altitude" + location.getAltitude());
			String result = sb.toString();
			// 获取安全吗
			SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
			SmsManager.getDefault().sendTextMessage(
					sp.getString("safeNum", ""), null, result, null, null);
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	// 服务结束释放监听器资源
	public void onDestroy() {
		locationManager.removeUpdates(listener);
		listener = null;
		super.onDestroy();
	}

}
