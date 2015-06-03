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
						// ���������������������õ�dialog����Activity����½�ɹ����������
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
						tv_screen_lock.setText("�������������" + locktime + "�λ���");
						tv_screen_lock.setTextColor(Color.RED);
						aa = AnimationUtils.loadAnimation(ScreenLock.this,
								R.anim.shake);
						tv_screen_lock.startAnimation(aa);
						wrongKey();
					} else if (result == 1) {
						Toast.makeText(ScreenLock.this, "�����ɹ�,��ӭ����Ͷ����",
								Toast.LENGTH_SHORT).show();
						// Intent intent = new Intent();
						// ������Ҫ���÷���֮ǰҳ��
						// setResult(0,intent);
						// finish();
					} else {
						Toast.makeText(ScreenLock.this, "û����������", 0).show();
					}

				} else {
					// ��locktime����Ϊ0��ʹ���´ν���ֱ�ӽ����û���¼����
					Editor editor = sp.edit();
					editor.putInt("locktime", 0);
					editor.commit();
					// �����û���¼���棬�������������
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

	// ���������Լ��������û�У����õ�½���沢����������룬����״̬Ϊfalse�������������������ĵط��ָ�
	public void forgetKey() {
		Intent intent = new Intent(ScreenLock.this, SetScreenLock.class);
		startActivity(intent);
		finish();
	}

	public void wrongKey() {
		lockPatternView.setDisplayMode(DisplayMode.Wrong);
		// �����̷߳������ع켣��ui����
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
