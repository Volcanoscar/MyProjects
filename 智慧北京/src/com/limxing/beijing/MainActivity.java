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
		// ��ȡ�����˵�����
		slidingMenu = getSlidingMenu();
		// ���ô���λ�ã��������ܲ��ܲ໬���˵�
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// ��������Ұ��Ӧ��dp��С
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// ���ò����˵�����ʾλ��
		slidingMenu.setMode(SlidingMenu.LEFT);
		// ��������Ŀ���������ҳ�ֿ����ӷָ���
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		// �����ߵĿ��
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		
		
		// ��ʼ��д����
		MenuFragment fragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.menu, fragment, "MENU").commit();
		
		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.content_frame, homeFragment, "HOME").commit();

	}

}
