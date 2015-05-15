package com.touzila.limxing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("info", 0);
		if (sp.getInt("welcome", 0) == 0) {
			System.out.println("更改了Welcome");
			changeFirst();
		}

	}

	// 更改第一次info信息
	private void changeFirst() {
		Editor editor = sp.edit();
		editor.putInt("welcome", 1);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
