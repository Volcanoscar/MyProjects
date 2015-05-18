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
						// ���������������������õ�dialog����Activity����½�ɹ����������
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
					tv_delete_screen_lock.setText("�������������");
					tv_delete_screen_lock.setTextColor(Color.RED);
					wrongKey();
				} else if (result == 1) {
					//�����ɹ�������������
					lockPatternUtils.clearLock();
					// Intent intent = new Intent();
					// ����֮ǰҳ��
					// setResult(0,intent);
					// finish();
				} else {
					Toast.makeText(DeleteScreenLock.this, "û����������", 0).show();
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
		Intent intent = new Intent(DeleteScreenLock.this, SetScreenLock.class);
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


}
