package com.limxing.safe.activity;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.limxing.safe.R;
import com.limxing.safe.db.dao.AntivirusDao;
import com.limxing.safe.utils.Md5Utils;

/*
 * 作者：Limxing 时间： 2015-5-30 下午7:24:23
 * 
 * 描述：病毒查杀的模块
 */
public class AntivirusActivity extends Activity {
	protected static final int SCAN_START = 1;
	protected static final int SCANING = 2;
	protected static final int END = 3;
	private ImageView antivirus_main_iv;
	private TextView antivirus_main_tv;
	private ProgressBar antivirus_main_pb;
	private LinearLayout antivirus_main_ll;
	private PackageManager pm;
	private boolean flag;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_START:
				antivirus_main_tv.setText("初始化病毒扫描引擎");
				break;

			case SCANING:
				ScanInfo scanInfo = (ScanInfo) msg.obj;
				antivirus_main_tv.setText("正在扫描：" + scanInfo.appname);
				TextView view = new TextView(getApplicationContext());
				if (scanInfo.isvirtus) {
					view.setTextColor(Color.RED);
				} else {
					view.setTextColor(Color.BLACK);
				}
				view.setText(scanInfo.appname + ":" + scanInfo.desc);
				// 把新建的文本文件放进ListView控件中，0代表放置的位置
				antivirus_main_ll.addView(view, 0);

				break;
			case END:
				antivirus_main_tv.setText("扫描完成！");
				antivirus_main_iv.clearAnimation();
				Toast.makeText(AntivirusActivity.this, "扫描完成", 0).show();
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivirus_main);
		pm = getPackageManager();
		antivirus_main_iv = (ImageView) findViewById(R.id.antivirus_main_iv);
		antivirus_main_tv = (TextView) findViewById(R.id.antivirus_main_tv);
		antivirus_main_pb = (ProgressBar) findViewById(R.id.antivirus_main_pb);
		antivirus_main_ll = (LinearLayout) findViewById(R.id.antivirus_main_ll);
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		antivirus_main_iv.startAnimation(ra);
		scanVirus();

	}

	// 扫描病毒
	private void scanVirus() {
		flag = true;
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = SCAN_START;
				handler.sendMessage(msg);

				int process = 0;
				// 获取包集合
				List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
				int max = packageInfos.size();
				antivirus_main_pb.setMax(max);
				for (PackageInfo info : packageInfos) {
					if (!flag) {
						return;
					}
					String path = info.applicationInfo.sourceDir;
					// 工具类获取这个文件的特征码
					String md5Info = Md5Utils.getFileMd5(path);
					String result = AntivirusDao.find(md5Info);
					ScanInfo scanInfo = new ScanInfo();
					if (result == null) {
						scanInfo.isvirtus = false;
						scanInfo.desc = "扫描通过";
					} else {
						scanInfo.isvirtus = true;
						scanInfo.desc = result;
					}
					scanInfo.packname = info.packageName;
					scanInfo.appname = info.applicationInfo.loadLabel(pm)
							.toString();
					// 发送更新UI通知
					msg = Message.obtain();
					msg.obj = scanInfo;
					msg.what = SCANING;
					handler.sendMessage(msg);
					process++;
					antivirus_main_pb.setProgress(process);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				msg = Message.obtain();
				msg.what = END;
				handler.sendMessage(msg);
			}
		}.start();
	}

	public class ScanInfo {
		String appname;
		boolean isvirtus;
		String desc;
		String packname;
	}

	@Override
	protected void onDestroy() {
		flag = false;
		super.onDestroy();
	}

}
