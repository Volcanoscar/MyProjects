package com.limxing.beijing;

import com.limxing.beijing.R;
import com.limxing.beijing.fragment.HomeFragment;
import com.limxing.beijing.fragment.MenuFragment;
import com.limxing.beijing.utils.ShareUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;

public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareUtil.saveBooleanData(getApplicationContext(), "isfirst", false);
		setContentView(R.layout.content);
		setBehindContentView(R.layout.menu_frame);
		// 获取滑动菜单对象
		slidingMenu = getSlidingMenu();
		// 设置触摸位置：决定了能不能侧滑出菜单
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置内容野对应的dp大小
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置侧拉菜单的显示位置
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 给侧拉栏目和左侧内容页分开，加分割线
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		// 设置线的宽度
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		
		
		// 开始填写内容
		MenuFragment fragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.menu, fragment, "MENU").commit();
		
		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.content_frame, homeFragment, "HOME").commit();

	}

}
