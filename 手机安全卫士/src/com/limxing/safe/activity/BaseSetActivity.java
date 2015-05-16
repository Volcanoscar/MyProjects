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

	// �����һ����ť
	public void next(View view) {
		showNext();

	}

	abstract void showNext();

	// �����һ��
	public void pre(View view) {
		showPre();
	}

	abstract void showPre();
	
	//���õ��������һ��ҳ�沢�رձ�ҳ��
	public void startActivityAndFinishSelf(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
		finish();
	}
	//���������ҳ�治�رձ�ҳ��
	public void startActivity(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
	}

}
