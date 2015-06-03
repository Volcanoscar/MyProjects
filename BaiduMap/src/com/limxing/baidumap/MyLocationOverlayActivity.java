package com.limxing.baidumap;

import android.os.Bundle;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MyLocationOverlayActivity extends BaseActivity {
	private LocationClient mLocationClient;
	private BDLocationListener mLocationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		location();
	}

	private void location() {
		// ����λ����
		mLocationClient = new LocationClient(getApplicationContext());
		// �������ã���λģʽ�������������ͣ��Ƿ��gps
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		// ���صĶ�λ���������ַ��Ϣ
		option.setAddrType("all");
		// ���ض�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		option.setCoorType("bd0911");
		// ���÷���λ����ļ��ʱ��5000ms
		option.setScanSpan(5000);
		// ��ֹ���û��涨λ
		option.disableCache(true);
		// ��෵��POI����
		option.setPoiNumber(5);
		// poi��ѯ����
		option.setPoiDistance(1000);
		// �Ƿ���ҪPOI �ĵ绰�͵�ַ����ϸ��Ϣ
		option.setPoiExtraInfo(true);
		mLocationClient.setLocOption(option);
	}

}
