package com.limxing.callinglock;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private SharedPreferences sp;
    private boolean running;
    private TextView tv_time;
    private String[] times = new String[]{"立即锁定", "3秒后", "5秒后", "10秒后", "关闭通话锁屏"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("info", MODE_PRIVATE);
        tv_time = (TextView) findViewById(R.id.tv_time);
        //使用说明的点击事件
        findViewById(R.id.tv_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatementActivity.class);
                startActivity(intent);
            }
        });


        //时间设定的点击事件
        findViewById(R.id.rl_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改锁定时间
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请选择延迟时间");
                builder.setSingleChoiceItems(times, sp.getInt("time", 1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("time", which);
                        editor.commit();
                        if (which == 4) {
                            tv_time.setText("未开启通话锁屏");
                            if (running) {
                                Intent intent = new Intent(MainActivity.this, CallingLockService.class);
                                stopService(intent);
                            }
                        } else {
                            tv_time.setText(times[which]);
                            if (!running) {
                                Intent intent = new Intent(MainActivity.this, CallingLockService.class);
                                startService(intent);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        if (sp.getInt("time", 2) != 4) {
            Intent intent = new Intent(MainActivity.this, CallingLockService.class);
            startService(intent);
        }
        TextView tv_main_count = (TextView) findViewById(R.id.tv_main_count);
        tv_main_count.setText(String.valueOf("累计为您拦截" + sp.getInt("count", 0)) + "次误触");
    }

    @Override
    protected void onStart() {
        running = SystemInfoUtils.isServiceRunning(MainActivity.this, "com.limxing.callinglock.CallingLockService");
        if (!running) {
            tv_time.setText("未开启通话锁屏");
        }
        tv_time.setText(times[sp.getInt("time",1)]);
        super.onStart();
    }



    //设置再按一次退出程序
    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            finish();
        }
    }
}
