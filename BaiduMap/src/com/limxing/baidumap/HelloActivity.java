package com.limxing.baidumap;

import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class HelloActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		// 放大
		case KeyEvent.KEYCODE_1:
			controller.zoomIn();
			break;
		// 放小
		case KeyEvent.KEYCODE_2:
			controller.zoomOut();
			break;
		// 以一个中心进行旋转
		case KeyEvent.KEYCODE_3:
			// 获取当前的角度（-45~0）
			int rotation = mapview.getMapRotation();
			controller.setRotation(rotation + 30);

			break;
		// 以一个直线为轴进行转动
		case KeyEvent.KEYCODE_4:
			int overlooking = mapview.getMapOverlooking();
			controller.setOverlooking(overlooking - 5);

			break;
		// 移动
		case KeyEvent.KEYCODE_5:
			// 移动到设定的经纬度位置
			controller.animateTo(new GeoPoint(40065796, 115349868));
			break;

		}
		return super.onKeyDown(keyCode, event);
	}
}
