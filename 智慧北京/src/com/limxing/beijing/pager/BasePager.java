package com.limxing.beijing.pager;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.limxing.beijing.MainActivity;
import com.limxing.beijing.R;

public abstract class BasePager {
	public Context context;
	public View view;
	private TextView txt_title;
	private ImageButton imgbtn_text;
	private ImageButton imgbtn_right;
	private ImageButton btn_right;

	public BasePager(Context context) {
		this.context = context;

		view = initView();
		initData();

	}

	public View getRootView() {
		return view;
	}

	public abstract View initView();

	public abstract void initData();

	protected void initTitleBar() {
		Button bt_left = (Button) view.findViewById(R.id.btn_left);
		bt_left.setVisibility(View.GONE);
		ImageButton imgbtn_left = (ImageButton) view
				.findViewById(R.id.imgbtn_left);
		// 前景显示当前控件内容
		imgbtn_left.setImageResource(R.drawable.img_menu);
		imgbtn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 导航栏显示与隐藏
				((MainActivity) context).getSlidingMenu().toggle();

			}
		});

		txt_title = (TextView) view.findViewById(R.id.txt_title);
		imgbtn_text = (ImageButton) view.findViewById(R.id.imgbtn_text);
		imgbtn_right = (ImageButton) view.findViewById(R.id.imgbtn_right);
		btn_right = (ImageButton) view.findViewById(R.id.btn_right);

		imgbtn_text.setVisibility(View.GONE);
		imgbtn_right.setVisibility(View.GONE);
		btn_right.setVisibility(View.GONE);
	}

	// 向服务器获取数据
	public void getData(HttpMethod httpMethod, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(httpMethod, url, params, callBack);

	}
}
