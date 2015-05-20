package com.limxing.safe.activity;

import com.limxing.safe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetActivity extends Activity {
	protected SharedPreferences sp;
	private GestureDetector gesture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		gesture = new GestureDetector(this, new SimpleOnGestureListener() {
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (Math.abs(velocityX) < 200) {
					return true;
				}
				if ((e2.getRawX() - e1.getRawX()) > 200) {
					showPre();
					overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
					return true;
				}
				if ((e1.getRawX() - e2.getRawX()) > 200) {
					showNext();
					overridePendingTransition(R.anim.next_in, R.anim.next_out);
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gesture.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	// �����һ����ť
	public void next(View view) {
		showNext();
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}

	abstract void showNext();

	// �����һ��
	public void pre(View view) {
		showPre();
		overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
	}

	abstract void showPre();

	// ���õ��������һ��ҳ�沢�رձ�ҳ��
	public void startActivityAndFinishSelf(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	}

	// ���������ҳ�治�رձ�ҳ��
	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

}
