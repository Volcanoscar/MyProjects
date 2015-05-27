package com.limxing.safe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.fragment.LockedFragment;
import com.limxing.safe.fragment.UnlockFragment;

/*
 * 作者：Limxing 时间： 2015-5-27 下午6:23:38
 * 
 * 描述：程序锁的activity
 */
public class ToolAppLockActivity extends FragmentActivity implements
		OnClickListener {
	private TextView tools_applock_tv_locked;
	private TextView tools_applock_tv_unlock;
	private FragmentManager fm;
	private UnlockFragment uf;
	private LockedFragment lf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_applock);
		tools_applock_tv_locked = (TextView) findViewById(R.id.tools_applock_tv_locked);
		tools_applock_tv_unlock = (TextView) findViewById(R.id.tools_applock_tv_unlock);
		tools_applock_tv_locked.setOnClickListener(this);
		tools_applock_tv_unlock.setOnClickListener(this);
		// 初始化管理器
		fm = getSupportFragmentManager();
		uf = new UnlockFragment();
		lf = new LockedFragment();
		// 开启界面变化的事务
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.tools_applock_f, uf);
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction ft = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.tools_applock_tv_locked:
			tools_applock_tv_locked.setBackground(getResources().getDrawable(
					R.drawable.tab_right_pressed));
			tools_applock_tv_unlock.setBackground(getResources().getDrawable(
					R.drawable.tab_left_default));

			ft.replace(R.id.tools_applock_f, lf);
			ft.commit();

			break;

		case R.id.tools_applock_tv_unlock:
			tools_applock_tv_unlock.setBackground(getResources().getDrawable(
					R.drawable.tab_left_pressed));
			tools_applock_tv_locked.setBackground(getResources().getDrawable(
					R.drawable.tab_right_default));

			ft.replace(R.id.tools_applock_f, uf);
			ft.commit();
			break;
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	// 返回按钮
	public void back(View view) {
		onBackPressed();
	}

}
