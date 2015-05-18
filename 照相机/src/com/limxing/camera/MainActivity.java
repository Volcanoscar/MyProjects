package com.limxing.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!checkCameraHardware(this)) {
			System.out.println(111);
			// �����������
			finish();
		}
		
		mCamera=getCameraInstance();
		if(mCamera==null){
			System.out.println(222);
			finish();
		}
		
		//ʹ��Ĭ�ϵ���������ã�����������set
		mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        //���հ�ť
        findViewById(R.id.button_capture).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCamera.takePicture(null, null, new PictureCallback() {
					
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						try {
							FileOutputStream fos=new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"123.jpg"));
							fos.write(data);
							fos.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//������Ƭ�����¿�ʼ����
						mCamera.startPreview();
						
					}
				});
				
			}
		});
	}
	public void onDestory() {
		mCamera.release();
		mCamera=null;
		super.onDestroy();

	}

	/**
	 * ���������Ƿ����
	 */
	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	// �������������ȡ���������
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(1);//API9���ϲſ���open(int i),���ô�ǰ�û��Ǻ�������ͷ
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return c;

	}
}
