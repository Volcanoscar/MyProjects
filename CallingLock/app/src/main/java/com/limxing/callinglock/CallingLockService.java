package com.limxing.callinglock;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by limxing on 2015/6/5.
 */
public class CallingLockService extends Service {
    private TelephonyManager mTelephonyManager;
    private MyListener mMyListener;
    private WindowManager mWindowManager;
    private OutCallReceiver mOutCallReceiver;
    private SharedPreferences sp;
    private boolean flag;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Notification notification = new Notification(R.mipmap.ic_launcher, "limxing", System.currentTimeMillis());
        Intent intent = new Intent();
        intent.setAction("ooo.aaa.bbb");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PendingIntent contentIntnet = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, "limxing", "limxing", contentIntnet);
        startForeground(0, notification);

        sp = getSharedPreferences("info", MODE_PRIVATE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 来电监听
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mMyListener = new MyListener();
        mTelephonyManager.listen(mMyListener,
                PhoneStateListener.LISTEN_CALL_STATE);
        // 去电
        mOutCallReceiver = new OutCallReceiver();
        registerReceiver(mOutCallReceiver, new IntentFilter(
                Intent.ACTION_NEW_OUTGOING_CALL));
        super.onCreate();
    }

    private class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        }

    }

    // 电话状态监听
    private class MyListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (view != null) {
                        try {
                            flag=false;
                            mWindowManager.removeView(view);
                            view = null;
                        } catch (Exception e) {
                        }
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("count", count);
                        editor.commit();
                    }

                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    showToast();
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

    }

    // 自定义土司Toast
    private WindowManager.LayoutParams params;
    private View view;
    private int count;
    private Long[] times = new Long[]{Long.valueOf(0), Long.valueOf(3000), Long.valueOf(5000), Long.valueOf(10000)};
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (flag) {
                mWindowManager.addView(view, params);
            }
        }
    };


    public void showToast() {
        final long time = times[sp.getInt("time", 1)];

        count = sp.getInt("count", 0);
        view = View.inflate(getApplicationContext(), R.layout.service_callinglock, null);

        //点击其他位置时的事件
        view.findViewById(R.id.rl_callinglockservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
            }
        });
        //找到控件的位置
        SlideView slideView = (SlideView) view.findViewById(R.id.slider);
        slideView.setSlideListener(new SlideView.SlideListener() {
            @Override
            public void onDone() {
                mWindowManager.removeView(view);
                view = null;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("count", count);
                editor.commit();
            }
        });
        slideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
            }
        });

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST; // 土司窗体天生不响应触摸事件

        flag=true;
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(0);
            }

        }.start();


    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mOutCallReceiver);
        mOutCallReceiver = null;
        mTelephonyManager.listen(mMyListener, PhoneStateListener.LISTEN_NONE);
        mMyListener = null;
        super.onDestroy();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }

}
