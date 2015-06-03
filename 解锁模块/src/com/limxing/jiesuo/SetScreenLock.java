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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.limxing.jiesuo.LockPatternView.Cell;
import com.limxing.jiesuo.LockPatternView.DisplayMode;
import com.limxing.jiesuo.LockPatternView.OnPatternListener;

public class SetScreenLock extends Activity implements OnClickListener {
	private Animation aa;
	private LockPatternView lockPatternView;
	private TextView tv_set_screen_lock;
	private String pattern_first;
	private String pattern_second;
	private LockPatternUtils lockPatternUtils;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			lockPatternView.clearPattern();

		}
	};
	private boolean isFirst;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_screen_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		tv_set_screen_lock = (TextView) findViewById(R.id.tv_set_screen_lock);
		isFirst = true;
		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new MyOnPatternListener());

	}

	// ʵ�ֽӿ�
	class MyOnPatternListener implements OnPatternListener {

		public void onPatternStart() {

		}

		// Animation aa=AnimationUtils.loadAnimation(AppLockPwd.this,
		// R.anim.shake);
		// et_task_password.startAnimation(aa);
		public void onPatternDetected(List<Cell> pattern) {
			if (isFirst) {
				if (pattern.size() < 4) {
					aa = AnimationUtils.loadAnimation(SetScreenLock.this,
							R.anim.shake);
					tv_set_screen_lock.setText("���ٻ���4�����ӵ�,������");
					tv_set_screen_lock.startAnimation(aa);
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					tv_set_screen_lock.setTextColor(Color.RED);
					clear();
					return;
				} else {
					pattern_first = pattern.toString();
					isFirst = false;
					tv_set_screen_lock.setText("�ٴλ���ͼ������ȷ��");
					tv_set_screen_lock.setTextColor(Color.GREEN);
					clear();

				}
			} else {
				pattern_second = pattern.toString();
				if (!pattern_first.equals(pattern_second)) {
					tv_set_screen_lock.setText("����ͼ����ͬ�������»���");
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					tv_set_screen_lock.setTextColor(Color.RED);
					aa = AnimationUtils.loadAnimation(SetScreenLock.this,
							R.anim.shake);
					tv_set_screen_lock.startAnimation(aa);
					isFirst = true;
					clear();
					return;
				} else {
					lockPatternUtils.saveLockPattern(pattern);
					Toast.makeText(SetScreenLock.this, "�������óɹ�",
							Toast.LENGTH_SHORT).show();
					// �������õ�״̬Ϊtrue,����������״̬Ϊtrue���ڽ�������������info
					Intent data = new Intent();
					data.putExtra("setlockstate", true);// ��ǰ���ҳ�淢�����óɹ�����Ϣ
					setResult(0, data);
					finish();
				}
			}

		}

		@Override
		public void onPatternCleared() {

		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {

		}

	}

	@Override
	public void onClick(View v) {

	}

	public void clear() {

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
}
