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
		// �Ŵ�
		case KeyEvent.KEYCODE_1:
			controller.zoomIn();
			break;
		// ��С
		case KeyEvent.KEYCODE_2:
			controller.zoomOut();
			break;
		// ��һ�����Ľ�����ת
		case KeyEvent.KEYCODE_3:
			// ��ȡ��ǰ�ĽǶȣ�-45~0��
			int rotation = mapview.getMapRotation();
			controller.setRotation(rotation + 30);

			break;
		// ��һ��ֱ��Ϊ�����ת��
		case KeyEvent.KEYCODE_4:
			int overlooking = mapview.getMapOverlooking();
			controller.setOverlooking(overlooking - 5);

			break;
		// �ƶ�
		case KeyEvent.KEYCODE_5:
			// �ƶ����趨�ľ�γ��λ��
			controller.animateTo(new GeoPoint(40065796, 115349868));
			break;

		}
		return super.onKeyDown(keyCode, event);
	}
}
