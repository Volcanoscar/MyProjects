package com.limxing.baidumap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaseActivity extends ActionBarActivity {
	protected BMapManager manager;
	protected MapView mapview;
	protected MapController controller;
	protected int latitude = (int) (40.051 * 1E6);
	protected int longitude = (int) (116.303 * 1E6);
	protected GeoPoint heima = new GeoPoint(latitude, longitude);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// key��У��
		initManager();

		setContentView(R.layout.common);
		// ��ͼ�ĳ�ʼ����һ�򿪵�����
		init();
	}

	private void init() {
		mapview = (MapView) findViewById(R.id.mapview);
		controller = mapview.getController();
		// ���õ�ͼ�����ż���3~19��
		controller.setZoom(12);
		// �ڵ�ͼ��������ŵİ�ť
		mapview.setBuiltInZoomControls(true);
		// ���õ�ͼ�ĳ�ʼ��λ��
		controller.setCenter(heima);
		// ���õ�ͼ�ϵĿɵ�λ�ÿɵ�
		controller.enableClick(true);
	}

	private void initManager() {
		manager = new BMapManager(getApplicationContext());
		manager.init(getResources().getString(R.string.mapkey),
				new MKGeneralListener() {

					@Override
					public void onGetPermissionState(int iError) {
						// ��Ȩ��֤
						if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
							Toast.makeText(getApplicationContext(), "��֤ʧ��", 0)
									.show();
						}
					}

					@Override
					public void onGetNetworkState(int iError) {
						// ����״̬
						if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
							Toast.makeText(getApplicationContext(), "�����쳣", 0)
									.show();
						}
					}
				});

	}

	@Override
	protected void onResume() {
		mapview.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mapview.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mapview.destroy();
		super.onDestroy();
	}

}
