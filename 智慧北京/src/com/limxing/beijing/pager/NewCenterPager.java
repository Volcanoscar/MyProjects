package com.limxing.beijing.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.beijing.R;
import com.limxing.beijing.bean.NewCenter;
import com.limxing.beijing.utils.BeiJingApi;
import com.limxing.beijing.utils.GsonUtil;
import com.limxing.beijing.utils.SharedPreferencesUtil;

public class NewCenterPager extends BasePager {

	@ViewInject(R.id.news_center_fl)
	public FrameLayout news_center_fl;

	public NewCenterPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.news_center_frame, null);
		ViewUtils.inject(this, view);
		// 初始化标题栏
		initTitleBar();
		return view;
	}

	@Override
	public void initData() {
		System.out.println("联网");
		// 判断本地是否含有缓存数据
		String result = SharedPreferencesUtil.getStringData(context,
				BeiJingApi.NEWS_CENTER_CATEGORIES, "");
		if (!TextUtils.isEmpty(result)) {
			processData(result);
		}

		// 联网解析数据
		getNewCenterData();
	}

	private void getNewCenterData() {
		getData(HttpMethod.GET, "http://192.168.11.1:8080/qbc/categories.json",
				null, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("下载成功");
						// 数据获取成功，保存缓存数据到应用中
						SharedPreferencesUtil.saveStringData(context,
								 "http://192.168.11.1:8080/qbc/categories.json",
								responseInfo.result);
						// 解析数据
						processData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("下载失败");

					}
				});

	}

	private void processData(String result) {
		GsonUtil.jsonToBean(result, NewCenter.class);
	}
}
