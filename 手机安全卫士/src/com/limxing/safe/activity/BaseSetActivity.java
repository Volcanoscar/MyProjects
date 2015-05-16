package com.limxing.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public abstract class BaseSetActivity extends Activity {
	protected SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("info", MODE_PRIVATE);
	}

	// 点击下一步按钮
	public void next(View view) {
		showNext();

	}

	abstract void showNext();

	// 点击上一步
	public void pre(View view) {
		showPre();
	}

	abstract void showPre();
	
	//设置点击开启下一个页面并关闭本页面
	public void startActivityAndFinishSelf(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
		finish();
	}
	//点击开启下页面不关闭本页面
	public void startActivity(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
	}

}
