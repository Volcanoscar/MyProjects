package com.limxing.jiesuo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.limxing.jiesuo.LockPatternView.Cell;
import com.limxing.jiesuo.LockPatternView.DisplayMode;
import com.limxing.jiesuo.LockPatternView.OnPatternListener;

public class ScreenLock extends Activity implements OnClickListener {

	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private SharedPreferences sp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		sp = getSharedPreferences("locktime", 5);
		findViewById(R.id.tv_screen_lock).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ���������������������õ�dialog����Activity

					}
				});
		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				int result = lockPatternUtils.checkPattern(pattern);
				if (result == 0) {
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					// lockPatternView.clearPattern();
					Toast.makeText(ScreenLock.this, "�������", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(ScreenLock.this, "�����ɹ�,��ӭ����Ͷ����",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					// ������Ҫ������ҳ��
					startActivity(intent);
					finish();
				}

			}

			public void onPatternCleared() {

			}

			public void onPatternCellAdded(List<Cell> pattern) {

			}
		});
	}

	public void onClick(View v) {

	}

}
