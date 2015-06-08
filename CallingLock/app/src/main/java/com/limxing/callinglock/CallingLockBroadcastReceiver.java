package com.limxing.callinglock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by limxing on 2015/6/6.
 */
public class CallingLockBroadcastReceiver extends BroadcastReceiver {
//    private TelephonyManager mTelephonyManager;
//    private MyListener mMyListener;
//    private WindowManager mWindowManager;
//    private Context context;
//    private SharedPreferences sp;
//    private int width;

    @Override
    public void onReceive(Context context, Intent intent) {
//        this.context = context;
        //如果是去电
//            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//
//            } else
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (context.getSharedPreferences("info", context.MODE_PRIVATE).getInt("time", 1) != 4) {
                Intent service = new Intent(context, CallingLockService.class);
                context.startService(service);
            }
        } else {
            if (!SystemInfoUtils.isServiceRunning(context, "com.limxing.callinglock.CallingLockService")) {
                if (context.getSharedPreferences("info", context.MODE_PRIVATE).getInt("time", 1) != 4) {
                    Intent service = new Intent(context, CallingLockService.class);
                    context.startService(service);
                }
            }
//            System.out.println("检测到信号");
//            if (view == null) {
//                sp = context.getSharedPreferences("info", 0x0000);
//                mWindowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
//                width = mWindowManager.getDefaultDisplay().getWidth();
//                System.out.println(width);
//                mTelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
//                mMyListener = new MyListener();
//
//            }
//            mTelephonyManager.listen(mMyListener,
//                    PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

//    private class MyListener extends PhoneStateListener {
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
//
//            switch (state) {
//                case TelephonyManager.CALL_STATE_IDLE:
//                    System.out.println("挂断");
//
//                    if (view != null) {
//                        System.out.println("删除一次");
////                        mWindowManager.removeView(view);
////                        view = null;
////                        mTelephonyManager.listen(mMyListener, PhoneStateListener.LISTEN_NONE);
////                        mMyListener = null;
////                        SharedPreferences.Editor editor = sp.edit();
////                        editor.putInt("count", count);
////                        editor.commit();
//                    }
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    System.out.println("接听");
//                    if (view == null) {
//                        showToast();
//                    }
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING:
//
//                    break;
//            }
//            super.onCallStateChanged(state, incomingNumber);
//        }
//    }


    // 自定义土司Toast
//    private WindowManager.LayoutParams params;
//    private View view;
//    private int count;
//    private Long[] times = new Long[]{Long.valueOf(0), Long.valueOf(3000), Long.valueOf(5000), Long.valueOf(10000)};
//    private android.os.Handler handler = new android.os.Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            mWindowManager.addView(view, params);
//        }
//    };


//    public void showToast() {
//        System.out.println("创建一次");
//        final long time = times[sp.getInt("time", 1)];
//        count = sp.getInt("count", 0);
//
//
//        view = View.inflate(context, R.layout.service_callinglock, null);
//
//        //点击其他位置时的事件
//        view.findViewById(R.id.rl_callinglockservice).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count++;
//            }
//        });
//        //找到控件的位置
//        SlideView slideView = (SlideView) view.findViewById(R.id.slider);
//
//        slideView.setSlideListener(new SlideView.SlideListener() {
//            @Override
//            public void onDone() {
//
//                mWindowManager.removeView(view);
//                view = null;
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putInt("count", count);
//                editor.commit();
//            }
//        });
//        slideView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count++;
//            }
//        });
//
//
//        params = new WindowManager.LayoutParams();
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.gravity = Gravity.LEFT + Gravity.TOP;
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//        params.format = PixelFormat.TRANSLUCENT;
//        params.type = WindowManager.LayoutParams.TYPE_TOAST; // 土司窗体天生不响应触摸事件
//
//        new Thread() {
//            public void run() {
//                try {
//                    Thread.sleep(time);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                handler.sendEmptyMessage(0);
//            }
//        }.start();
//
//
//    }

//    @Override
//    public void onDestroy() {
//        mTelephonyManager.listen(mMyListener, PhoneStateListener.LISTEN_NONE);
//        mMyListener = null;
//        super.onDestroy();
//    }
}


