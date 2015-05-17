package com.limxing.jiesuo;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.limxing.jiesuo.LockPatternView.Cell;
import com.limxing.jiesuo.LockPatternView.DisplayMode;
import com.limxing.jiesuo.LockPatternView.OnPatternListener;

public class DeleteScreenLock extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;

	private Button btn_set_pwd;

	private Button btn_reset_pwd;

	private Button btn_check_pwd;
	
	private boolean opFLag = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_lock);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
//		btn_reset_pwd = (Button) findViewById(R.id.btn_reset_pwd);
//		btn_set_pwd = (Button) findViewById(R.id.btn_set_pwd);
//		btn_check_pwd = (Button) findViewById(R.id.btn_check_pwd);
//		btn_reset_pwd.setOnClickListener(this);
//		btn_set_pwd.setOnClickListener(this);
//		btn_check_pwd.setOnClickListener(this);

		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				if(opFLag){
					int result = lockPatternUtils.checkPattern(pattern);
					if (result!= 1) {
						if(result==0){
							lockPatternView.setDisplayMode(DisplayMode.Wrong);
//							lockPatternView.clearPattern();
							Toast.makeText(DeleteScreenLock.this, "密码错误", Toast.LENGTH_LONG)
							.show();
						}else{
							lockPatternView.clearPattern();
							Toast.makeText(DeleteScreenLock.this, "请设置密码", Toast.LENGTH_LONG)
							.show();
						}
						
					} else {
						Toast.makeText(DeleteScreenLock.this, "密码正确", Toast.LENGTH_LONG)
								.show();
					}
				}else{
					
					lockPatternUtils.saveLockPattern(pattern);
					Toast.makeText(DeleteScreenLock.this, "密码已经设置", Toast.LENGTH_LONG)
					.show();
					lockPatternView.clearPattern();
				}
			
			}

			public void onPatternCleared() {

			}

			public void onPatternCellAdded(List<Cell> pattern) {

			}
		});
	} 
//点击按钮的操作
	public void onClick(View v) {
		if (v == btn_reset_pwd) {
			lockPatternView.clearPattern();
			lockPatternUtils.clearLock();
		} else if (v == btn_check_pwd) {
			opFLag = true;
		} else {
			opFLag = false;
		}
	}

}
