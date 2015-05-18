package com.limxing.safe.activity;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.utils.ToastUtils;

public class LostFindSetFourActivity extends BaseSetActivity {
	private Switch s_lostfind_four_lock;
	private TextView tv_lostfind_four_lockstate;
	private boolean protectstate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_four);
		s_lostfind_four_lock = (Switch) findViewById(R.id.s_lostfind_four_lock);
		tv_lostfind_four_lockstate = (TextView) findViewById(R.id.tv_lostfind_four_lockstate);
		protectstate = sp.getBoolean("protectstate", false);
		if (protectstate) {
			tv_lostfind_four_lockstate.setText("防盗保护已开启");
			s_lostfind_four_lock.setChecked(true);
		}
		// 控件的点击事件
		s_lostfind_four_lock
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (protectstate) {
							tv_lostfind_four_lockstate.setText("防盗保护未开启");
							protectstate = false;
							s_lostfind_four_lock.setChecked(false);

						} else {
							tv_lostfind_four_lockstate.setText("防盗保护已开启");
							protectstate = true;
							s_lostfind_four_lock.setChecked(true);
						}

					}
				});
	}

	/**
	 * 点击保存按钮，把已经设置和保护开启状态写入sp中
	 */
	@Override
	void showNext() {
		Editor editor = sp.edit();
		editor.putBoolean("protectstate", protectstate);
		editor.putBoolean("isSet", true);
		editor.commit();
		ToastUtils.showToast(LostFindSetFourActivity.this, "设置成功");
		finish();
	}

	@Override
	void showPre() {
		startActivityAndFinishSelf(LostFindSetThreeActivity.class);
	}

}
