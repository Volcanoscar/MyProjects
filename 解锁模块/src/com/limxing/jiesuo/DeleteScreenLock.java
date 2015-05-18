package com.limxing.jiesuo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.limxing.jiesuo.LockPatternView.Cell;
import com.limxing.jiesuo.LockPatternView.DisplayMode;
import com.limxing.jiesuo.LockPatternView.OnPatternListener;

public class DeleteScreenLock extends Activity implements OnClickListener {

	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private TextView tv_delete_screen_lock;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			lockPatternView.clearPattern();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		tv_delete_screen_lock = (TextView) findViewById(R.id.tv_delete_screen_lock);
		findViewById(R.id.tv_screen_lock_forget).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 点击忘记密码进入密码重置的dialog或者Activity，登陆成功则清除密码
						forgetKey();

					}
				});

		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				int result = lockPatternUtils.checkPattern(pattern);
				if (result == 0) {
					tv_delete_screen_lock.setText("密码错误，请重试");
					tv_delete_screen_lock.setTextColor(Color.RED);
					wrongKey();
				} else if (result == 1) {
					//解锁成功，调用清密码
					lockPatternUtils.clearLock();
					// Intent intent = new Intent();
					// 返回之前页面
					// setResult(0,intent);
					// finish();
				} else {
					Toast.makeText(DeleteScreenLock.this, "没有设置锁屏", 0).show();
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

	// 忘记密码以及密码次数没有，调用登陆界面并清除锁屏密码，锁屏状态为false，次数是在设置锁屏的地方恢复
	public void forgetKey() {
		Intent intent = new Intent(DeleteScreenLock.this, SetScreenLock.class);
		startActivity(intent);
		finish();
	}

	public void wrongKey() {
		lockPatternView.setDisplayMode(DisplayMode.Wrong);
		// 创建线程发送隐藏轨迹的ui操作
		new Thread() {
			public void run() {
				try {
					Message msg = Message.obtain();
					Thread.sleep(500);
					handler.sendMessage(msg);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}


}
