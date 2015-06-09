package com.limxing.callinglock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.limxing.callinglock.utils.ShareUtil;

/**
 * Created by limxing on 2015/6/9.
 */
public class WelcomeActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (!SystemInfoUtils.isServiceRunning(WelcomeActivity.this, "com.limxing.callinglock.CallingLockService")) {
            if (ShareUtil.getIntData(WelcomeActivity.this, "time", 2) != 4) {
                Intent intent = new Intent(WelcomeActivity.this, CallingLockService.class);
                startService(intent);
            }
        }
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    @Override
    public void onBackPressed() {

    }

}
