package com.limxing.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.limxing.safe.R;
/*
 * 作者：Limxing 时间： 2015-5-30 下午7:24:23
 * 
 * 描述：病毒查杀的模块
 */
public class AntivirusActivity extends Activity {
	private ImageView antivirus_main_iv;
	private TextView antivirus_main_tv;
	private ProgressBar antivirus_main_pb;
	private LinearLayout antivirus_main_ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivirus_main);
		antivirus_main_iv=(ImageView) findViewById(R.id.antivirus_main_iv);
		antivirus_main_tv=(TextView) findViewById(R.id.antivirus_main_tv);
		antivirus_main_pb=(ProgressBar) findViewById(R.id.antivirus_main_pb);
		antivirus_main_ll=(LinearLayout) findViewById(R.id.antivirus_main_ll);
		RotateAnimation ra=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		antivirus_main_iv.startAnimation(ra);
		
		
		
		
		
	}

}
