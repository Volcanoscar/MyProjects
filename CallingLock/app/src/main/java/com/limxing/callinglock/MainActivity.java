package com.limxing.callinglock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.limxing.callinglock.utils.ShareUtil;


public class MainActivity extends ActionBarActivity {
    private boolean running;
    private TextView tv_time;
    private TextView tv_main_count;
    private String[] times;
    private ImageView iv_menu;
    private ListView main_start_drawer;
    private DrawerLayout dl_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        times = getResources().getStringArray(R.array.lockset_item);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_main_count = (TextView) findViewById(R.id.tv_main_count);
        main_start_drawer= (ListView) findViewById(R.id.main_start_drawer);
        iv_menu= (ImageView) findViewById(R.id.iv_menu);
        dl_main= (DrawerLayout) findViewById(R.id.dl_main);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_main.openDrawer(Gravity.LEFT);
            }
        });

        //使用说明的点击事件
        findViewById(R.id.tv_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatementActivity.class);
                startActivity(intent);
            }
        });
        tv_main_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CountActivity.class);
                startActivity(intent);
            }
        });


        //时间设定的点击事件
        findViewById(R.id.rl_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改锁定时间
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.timedelay));
                builder.setSingleChoiceItems(times, ShareUtil.getIntData(MainActivity.this, "time", 1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShareUtil.saveIntData(MainActivity.this, "time", which);
                        if (which == 4) {
                            tv_time.setText(getResources().getString(R.string.close));
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
        //给左侧菜单栏填充数据
        main_start_drawer.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView view = new TextView(MainActivity.this);
                view.setText("欢迎使用" + position);
                return view;
            }
        });
        //给菜单栏上的条目设置点击事件
        main_start_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dl_main.closeDrawers();
                Intent intent = new Intent(MainActivity.this,StatementActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        running = SystemInfoUtils.isServiceRunning(MainActivity.this, "com.limxing.callinglock.CallingLockService");
        if (!running) {
            tv_time.setText(getResources().getString(R.string.close));
        }
        tv_time.setText(times[ShareUtil.getIntData(MainActivity.this, "time", 1)]);
        tv_main_count.setText(String.valueOf(getResources().getString(R.string.lanjie) + ShareUtil.getIntData(MainActivity.this, "count", 0)) + getResources().getString(R.string.time));
        super.onStart();
    }


    //设置再按一次退出程序
    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            Toast.makeText(this, getResources().getString(R.string.quite), Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            finish();
        }
    }
}
