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
	 * ����һ������Ҫ��ȡλ����Ϣ�������;�γ��
	 */
	@Override
	public void onCreate() {
		// 1����ȡϵͳλ�ù�����
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyListener();
		// 2���õ�λ���ṩ�ߣ�gps ���綨λ����վ��λ������
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// ����Ϊ��ȡ��ȷ��λ��
		criteria.setCostAllowed(true);// ���ÿ���������������
		// ��ȡ��õ�λ���ṩ��
		String name = locationManager.getBestProvider(criteria, true);
		//
		locationManager.requestLocationUpdates(name, 0, 0, listener);
		super.onCreate();

	}

	class MyListener implements LocationListener {
		/**
		 * λ�÷����ı�ʱ
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
			// ��ȡ��ȫ��
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

	// ��������ͷż�������Դ
	public void onDestroy() {
		locationManager.removeUpdates(listener);
		listener = null;
		super.onDestroy();
	}

}
