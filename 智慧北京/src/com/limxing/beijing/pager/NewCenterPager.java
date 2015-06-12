package com.limxing.beijing.pager;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.beijing.MainActivity;
import com.limxing.beijing.R;
import com.limxing.beijing.bean.NewCenter;
import com.limxing.beijing.utils.BeiJingApi;
import com.limxing.beijing.utils.GsonUtil;
import com.limxing.beijing.utils.SharedPreferencesUtil;

public class NewCenterPager extends BasePager {

	@ViewInject(R.id.news_center_fl)
	public FrameLayout news_center_fl;
	private ArrayList<String> titleList;
	private ArrayList<BasePager> pagers;

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
		LogUtils.i("联网查询");
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
		getData(HttpMethod.GET, BeiJingApi.NEWS_CENTER_CATEGORIES, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.i("下载成功");
						// 数据获取成功，保存缓存数据到应用中
						SharedPreferencesUtil.saveStringData(context,
								BeiJingApi.NEWS_CENTER_CATEGORIES,
								responseInfo.result);
						// 解析数据
						processData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.i("下载失败");

					}
				});

	}

	private void processData(String result) {
		// 解析数据
		NewCenter newCenter = GsonUtil.jsonToBean(result, NewCenter.class);
		titleList = new ArrayList<String>();
		// 循环遍历对象中，侧滑菜单中的条目标题放进集合中
		for (int i = 0; i < newCenter.data.size(); i++) {
			titleList.add(newCenter.data.get(i).title);
		}
		// titleList中填充的是左侧侧拉菜单中的数据，需要传递给MeanuFragment，获取MenuFragment的对应对象
		// 使用主MainActivity掉用Fragment
		((MainActivity) context).switchMenuFragment().initMenu(titleList);

		pagers = new ArrayList<BasePager>();
		pagers.add(new NewPager(context, newCenter.data.get(0)));
		pagers.add(new TopicPager(context, newCenter.data.get(1)));
		pagers.add(new PicPager(context, newCenter.data.get(2)));
		pagers.add(new IntPager(context, newCenter.data.get(3)));
		//默认选中第一个新闻页面
		switchPager(0);

	}

	// 被菜单上的条目点击调用的方法，是从MainActivity中一层一层调过来的
	public void switchPager(int position) {
		// 设置标题的文字
		txt_title.setText(titleList.get(position));
		// 清除下面原有的帧布局中的东西
		news_center_fl.removeAllViews();
		// 获取将要填充到帧布局中的对象
		news_center_fl.addView(pagers.get(position).getRootView());
		// 得到对象并传送给了Pager页面，调用这个页面的加载数据的方法，完成pager的数据初始化
		pagers.get(position).initData();

	}
}
