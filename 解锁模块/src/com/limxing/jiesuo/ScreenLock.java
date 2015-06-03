package com.limxing.jiesuo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.limxing.jiesuo.LockPatternView.Cell;
import com.limxing.jiesuo.LockPatternView.DisplayMode;
import com.limxing.jiesuo.LockPatternView.OnPatternListener;

public class ScreenLock extends Activity implements OnClickListener {
	private Animation aa;
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private TextView tv_screen_lock;
	private SharedPreferences sp;
	private int locktime;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			lockPatternView.clearPattern();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		tv_screen_lock = (TextView) findViewById(R.id.tv_screen_lock);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		locktime = sp.getInt("locktime", 5);
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
				if (locktime > 0) {
					int result = lockPatternUtils.checkPattern(pattern);
					if (result == 0) {
						locktime = locktime - 1;
						tv_screen_lock.setText("密码错误，您还有" + locktime + "次机会");
						tv_screen_lock.setTextColor(Color.RED);
						aa = AnimationUtils.loadAnimation(ScreenLock.this,
								R.anim.shake);
						tv_screen_lock.startAnimation(aa);
						wrongKey();
					} else if (result == 1) {
						Toast.makeText(ScreenLock.this, "解锁成功,欢迎进入投资啦",
								Toast.LENGTH_SHORT).show();
						// Intent intent = new Intent();
						// 这里需要设置返回之前页面
						// setResult(0,intent);
						// finish();
					} else {
						Toast.makeText(ScreenLock.this, "没有设置锁屏", 0).show();
					}

				} else {
					// 把locktime设置为0，使其下次进入直接进入用户登录界面
					Editor editor = sp.edit();
					editor.putInt("locktime", 0);
					editor.commit();
					// 弹出用户登录界面，并清除锁屏密码
					forgetKey();
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
		Intent intent = new Intent(ScreenLock.this, SetScreenLock.class);
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

	@Override
	protected void onDestroy() {
		Editor editor = sp.edit();
		editor.putInt("locktime", locktime);
		editor.commit();
		super.onDestroy();
	}

}
