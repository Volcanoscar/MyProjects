package com.limxing.jiesuo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

public class SetScreenLock extends Activity implements OnClickListener {

	private LockPatternView lockPatternView;
	private TextView tv_set_screen_lock;
	private List<Cell> pattern_first;
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
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				if (isFirst) {
					if (pattern.size() < 4) {
						tv_set_screen_lock.setText("���ٻ���4�����ӵ�,������");
						wrongKey();
					}
					pattern_first = pattern;
					isFirst = false;
					tv_set_screen_lock.setText("�ٴλ���ͼ������ȷ��");
				} else {
					if (!pattern_first.toString().equals(pattern.toString())) {
						tv_set_screen_lock.setText("����ͼ����ͬ�������»���");
						isFirst = true;
						wrongKey();
						return;
					} else {
						lockPatternUtils.saveLockPattern(pattern);
						Toast.makeText(SetScreenLock.this, "�������óɹ�",
								Toast.LENGTH_SHORT).show();
						// �������õ�״̬Ϊtrue,����������״̬Ϊtrue���ڽ�������������info
						Intent data = new Intent();
						data.putExtra("setlockstate", true);
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
		});

	}

	@Override
	public void onClick(View v) {

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
}
