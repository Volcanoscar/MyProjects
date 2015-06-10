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
		slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		
		MenuFragment fragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.menu, fragment, "MENU").commit();
		
		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.content_frame, homeFragment, "HOME").commit();

	}

}
