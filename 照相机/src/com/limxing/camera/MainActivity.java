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
			// 照相机不存在
			finish();
		}
		
		mCamera=getCameraInstance();
		if(mCamera==null){
			System.out.println(222);
			finish();
		}
		
		//使用默认的照相机设置，不对他进行set
		mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        //拍照按钮
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
						//拍完照片，重新开始拍照
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
	 * 检查照相机是否存在
	 */
	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	// 访问照相机，获取照相机对象
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(1);//API9以上才可以open(int i),设置打开前置还是后置摄像头
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return c;

	}
}
