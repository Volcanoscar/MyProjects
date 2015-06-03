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
		// 发起定位请求
		mLocationClient = new LocationClient(getApplicationContext());
		// 参数设置：定位模式，返回坐标类型，是否打开gps
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		// 返回的定位结果包含地址信息
		option.setAddrType("all");
		// 返回定位结果是百度经纬度，默认值gcj02
		option.setCoorType("bd0911");
		// 设置发起定位请求的间隔时间5000ms
		option.setScanSpan(5000);
		// 禁止启用缓存定位
		option.disableCache(true);
		// 最多返回POI个数
		option.setPoiNumber(5);
		// poi查询距离
		option.setPoiDistance(1000);
		// 是否需要POI 的电话和地址等详细信息
		option.setPoiExtraInfo(true);
		mLocationClient.setLocOption(option);
	}

}
