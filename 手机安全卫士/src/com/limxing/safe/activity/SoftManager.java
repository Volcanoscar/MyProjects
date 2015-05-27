package com.limxing.safe.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.domain.AppInfo;
import com.limxing.safe.engine.AppInfoParser;
import com.limxing.safe.utils.DensityUtil;
import com.limxing.safe.utils.SystemInfoUtils;
import com.limxing.safe.utils.ToastUtils;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

/*
 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����8:47:49
 * 
 * ��������������ܼҵ�Activity������
 */
public class SoftManager extends Activity implements OnClickListener {
	private UninstallReceiver receiver;
	private PopupWindow popupwindow;
	private LinearLayout ll_start;
	private LinearLayout ll_share;
	private LinearLayout ll_uninstall;
	private LinearLayout ll_setting;
	private AppInfo clickedAppInfo;
	private int userSoftSize;
	private int systemSoftSize;
	private TextView soft_manager_tv_float;
	private TextView soft_manager_tv_memory;
	private TextView soft_manager_sd;
	private ListView soft_manager_lv;
	private RelativeLayout soft_manager_loading;
	private List<AppInfo> appinfos;
	private List<AppInfo> userSofts;
	private List<AppInfo> systemSofts;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			soft_manager_loading.setVisibility(View.INVISIBLE);
			soft_manager_lv.setAdapter(new MyAppAdapter());

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	// ��ʼ������
	private void init() {
		getMemory();
		getSd();
		findSoft();
	}

	// ��ʼ��UI
	private void initUI() {
		setContentView(R.layout.soft_manager);
		soft_manager_tv_memory = (TextView) findViewById(R.id.soft_manager_tv_memory);
		soft_manager_sd = (TextView) findViewById(R.id.soft_manager_sd);
		soft_manager_loading = (RelativeLayout) findViewById(R.id.soft_manager_loading);
		soft_manager_lv = (ListView) findViewById(R.id.soft_manager_lv);
		soft_manager_tv_float = (TextView) findViewById(R.id.soft_manager_tv_float);
		/*
		 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����8:02:47
		 * 
		 * ������ListView�����¼���ʵ��Ư��
		 */
		soft_manager_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// ������ʱ��ر�Ư���Ĵ�
				dismissPopupWindow();
				if (firstVisibleItem <= userSoftSize) {
					soft_manager_tv_float.setText("�û�Ӧ�ã�" + userSoftSize + "��");
				} else {
					soft_manager_tv_float.setText("ϵͳӦ�ã�" + systemSoftSize
							+ "��");
				}

			}
		});
		/*
		 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����8:16:42
		 * 
		 * ����������Ŀ��ӵ���¼�����������
		 */
		soft_manager_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = soft_manager_lv.getItemAtPosition(position);
				if (obj != null && obj instanceof AppInfo) {
					// Ϊ����¼����ݶ���
					clickedAppInfo = (AppInfo) obj;
					//��ʼ����popupwindowView
					View popuView = View.inflate(getApplicationContext(),
							R.layout.soft_manager_popup_item, null);
					ll_uninstall = (LinearLayout) popuView
							.findViewById(R.id.ll_uninstall);
					ll_start = (LinearLayout) popuView
							.findViewById(R.id.ll_start);
					ll_share = (LinearLayout) popuView
							.findViewById(R.id.ll_share);
					ll_setting = (LinearLayout) popuView
							.findViewById(R.id.ll_setting);
					ll_uninstall.setOnClickListener(SoftManager.this);
					ll_start.setOnClickListener(SoftManager.this);
					ll_share.setOnClickListener(SoftManager.this);
					ll_setting.setOnClickListener(SoftManager.this);
					// �ر�������Ŀ������(�����ݴ���ʱ)
					dismissPopupWindow();
					popupwindow = new PopupWindow(popuView, -2, -2);
					// ����������һ��ǰ�������� �������Ҫ�б�����Դ�� �������û�б����������Ͳ��Ų�������
					popupwindow.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));
					int[] location = new int[2];
					view.getLocationInWindow(location);
					// ��ʾ��������Ч����
					// �ڴ����������еĳ��ȵ�λ���� ���ء�
					int dip = 60;
					int px = DensityUtil.dip2px(getApplicationContext(), dip);
					// System.out.println(px);
					// ��dipת���� ����
					popupwindow.showAtLocation(parent, Gravity.LEFT
							+ Gravity.TOP, px, location[1]);
					ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f,
							1.0f, Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0.5f);
					sa.setDuration(200);
					AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
					aa.setDuration(200);
					AnimationSet set = new AnimationSet(false);
					set.addAnimation(aa);
					set.addAnimation(sa);
					popuView.startAnimation(set);
				}

			}
		});
		/**
		 * ����¼�������ע��һ��ж�ع㲥�����յ�ж����ɵĹ㲥��ˢ��ҳ��
		 */
		receiver = new UninstallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);
	}

	/*
	 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����10:26:15
	 * 
	 * �������ر�������Ư����
	 */
	private void dismissPopupWindow() {
		if (popupwindow != null && popupwindow.isShowing()) {
			popupwindow.dismiss();
			popupwindow = null;
		}
	}

	// ��ѯ�Ѱ�װ��app�����뼯����
	public void findSoft() {
		new Thread() {
			public void run() {
				appinfos = AppInfoParser.getAppInfos(SoftManager.this);
				userSofts = new ArrayList<AppInfo>();
				systemSofts = new ArrayList<AppInfo>();
				for (AppInfo info : appinfos) {
					if (info.isIsuser()) {
						userSofts.add(info);
					} else {
						systemSofts.add(info);
					}
				}
				userSoftSize = userSofts.size();
				systemSoftSize = systemSofts.size();
				handler.sendEmptyMessage(0);

			};
		}.start();
	}

	// MyAppAdapter
	private class MyAppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userSofts.size() + systemSofts.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/*
			 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����7:46:04
			 * 
			 * ���������ӷֱ���ʾ�û���ϵͳ��Ӧ��
			 */
			if (position == 0) {
				TextView view = new TextView(SoftManager.this);
				view.setText("�û�Ӧ�ã�" + userSoftSize + "��");
				view.setBackgroundColor(Color.GRAY);
				return view;
			}
			if (position == userSofts.size() + 1) {
				TextView view = new TextView(SoftManager.this);
				view.setText("ϵͳӦ�ã�" + systemSoftSize + "��");
				view.setBackgroundColor(Color.GRAY);
				return view;
			}

			AppInfo appinfo;
			if (position < userSoftSize + 1) {
				appinfo = userSofts.get(position - 1);
			} else {
				appinfo = systemSofts.get(position - userSoftSize - 1 - 1);
			}
			View view;
			ViewHolder holder = new ViewHolder();
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(SoftManager.this,
						R.layout.soft_manager_item, null);
				holder.soft_manager_item_tv_icon = (ImageView) view
						.findViewById(R.id.soft_manager_item_tv_icon);
				holder.soft_manager_item_tv_location = (TextView) view
						.findViewById(R.id.soft_manager_item_tv_location);
				holder.soft_manager_item_tv_memory = (TextView) view
						.findViewById(R.id.soft_manager_item_tv_memory);
				holder.soft_manager_item_tv_name = (TextView) view
						.findViewById(R.id.soft_manager_item_tv_name);
				view.setTag(holder);
			}

			holder.soft_manager_item_tv_icon.setImageDrawable(appinfo
					.getAppico());
			if (appinfo.isInphone()) {
				holder.soft_manager_item_tv_location.setText("�ֻ��ڴ�");
			} else {
				holder.soft_manager_item_tv_location.setText("SD��");
			}
			holder.soft_manager_item_tv_memory.setText(Formatter
					.formatFileSize(getApplicationContext(),
							appinfo.getAppsize()));
			holder.soft_manager_item_tv_name.setText(appinfo.getAppname());
			return view;
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				// ��0��λ����ʾ��Ӧ���� �û�����ĸ����ı�ǩ��
				return null;
			} else if (position == (userSoftSize + 1)) {
				return null;
			}
			AppInfo appInfo;
			if (position < (userSoftSize + 1)) {
				// �û�����
				appInfo = userSofts.get(position - 1);// ����һ��textview�ı�ǩ ��
														// λ����Ҫ-1
			} else {
				// ϵͳ����
				int location = position - 1 - userSoftSize - 1;
				appInfo = systemSofts.get(location);
			}
			return appInfo;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	/**
	 * ��ͥ��
	 */
	static class ViewHolder {
		ImageView soft_manager_item_tv_icon;
		TextView soft_manager_item_tv_name;
		TextView soft_manager_item_tv_location;
		TextView soft_manager_item_tv_memory;
	}

	// ��ȡ�ڴ���Ϣ
	public void getMemory() {
		long total = SystemInfoUtils.getTotalMem();
		long free = SystemInfoUtils.getAvailMem(getApplicationContext());
		soft_manager_tv_memory.setText("�ڴ���Ϣ��"
				+ Formatter.formatShortFileSize(this, free));

	}

	// ��ȡsd����Ϣ
	public void getSd() {
		long total = Environment.getExternalStorageDirectory().getTotalSpace();
		long free = Environment.getExternalStorageDirectory().getFreeSpace();
		soft_manager_sd.setText("SD����Ϣ��"
				+ Formatter.formatShortFileSize(this, free));
	}

	// ���ذ������¼�
	public void back(View view) {
		onBackPressed();
	}

	/*
	 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����8:33:00
	 * 
	 * ���������� �ĵ���¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_start:
			// ������ť
			PackageManager pm = getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(clickedAppInfo
					.getApppack());
			if (intent != null) {
				startActivity(intent);
			} else {
				ToastUtils.showToast(SoftManager.this, "����û�н���");
			}
			break;

		case R.id.ll_setting:
			// ��������ҳ��
			Intent intent1 = new Intent();
			intent1.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent1.addCategory(Intent.CATEGORY_DEFAULT);
			intent1.setData(Uri.parse("package:" + clickedAppInfo.getApppack()));
			startActivity(intent1);

			break;
		case R.id.ll_share:
			// ����ҳ��
			Intent intent3 = new Intent();
			intent3.setAction("android.intent.action.SEND");
			intent3.addCategory("android.intent.category.DEFAULT");
			intent3.setType("text/plain");
			intent3.putExtra(
					Intent.EXTRA_TEXT,
					"�Ƽ�ʹ�ã�"
							+ clickedAppInfo.getAppname()
							+ "����·����https://play.google.com/store/apps/details?id="
							+ clickedAppInfo.getApppack());
			startActivity(intent3);

			break;
		case R.id.ll_uninstall:
			// ж��ҳ��
			if (clickedAppInfo.isIsuser()) {
				// ������û�Ӧ��ֱ�ӷ���ж��
				Intent intent2 = new Intent();
				intent2.setAction(Intent.ACTION_DELETE);
				intent2.setData(Uri.parse("package:"
						+ clickedAppInfo.getApppack()));
				startActivity(intent2);
			} else {
				if (RootTools.isRootAvailable()) {
					ToastUtils.showToast(this, "û�л�ȡRootȨ��");
					return;
				}
				// ��������Ȩ�ޣ���Ҫͨ�������е���ʽж��
				try {
					RootTools.sendShell("mount -o remount ,rw /system", 3000);
					RootTools.sendShell("rm -r" + clickedAppInfo.getApppack(),
							3000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (RootToolsException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	/*
	 * ���ߣ�Limxing ʱ�䣺 2015-5-26 ����11:10:53
	 * 
	 * �����������˳�ʱ���ر���صĴ��ڵ�
	 */
	@Override
	protected void onDestroy() {
		dismissPopupWindow();
		super.onDestroy();
	}

	/*
	 * ���ߣ�Limxing ʱ�䣺 2015-5-27 ����12:02:52
	 * 
	 * ����������һ��ж��Ӧ�õĹ㲥������
	 */
	private class UninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String info = intent.getData().toString();
			findSoft();

		}
	}

}
