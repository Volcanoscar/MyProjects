package com.limxing.beijing.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.beijing.R;
import com.limxing.beijing.bean.NewBean;
import com.limxing.beijing.bean.NewBean.News;
import com.limxing.beijing.fragment.BeijingAdapter;
import com.limxing.beijing.utils.BeiJingApi;
import com.limxing.beijing.utils.CommonUtil;
import com.limxing.beijing.utils.GsonUtil;
import com.limxing.beijing.utils.SharedPreferencesUtil;
import com.limxing.beijing.view.RollViewPager;
import com.limxing.beijing.view.pullrefreshview.PullToRefreshListView;

public class NewItemPager extends BasePager {

	private String url;
	private View layout_roll_view;
	// 轮播图片的位置
	@ViewInject(R.id.top_news_viewpager)
	public LinearLayout top_news_viewpager;
	// 轮播图的标题
	@ViewInject(R.id.top_news_title)
	public TextView top_news_title;
	// 轮播图放置红点的位置
	@ViewInject(R.id.dots_ll)
	public LinearLayout dots_ll;

	@ViewInject(R.id.lv_item_news)
	public PullToRefreshListView lv_item_news;
	// 图片关联的标题
	private List<String> titleList = new ArrayList<String>();
	// 图片对应的url集合
	private List<String> urlImageList = new ArrayList<String>();
	// 红色放置点的集合
	private List<View> viewList = new ArrayList<View>();
	private MyBaseAdapter myBaseAdapter;

	public NewItemPager(Context context, String url) {
		super(context);
		this.url = url;
	}

	@Override
	public View initView() {
		// 获取轮播图的布局
		layout_roll_view = View.inflate(context, R.layout.layout_roll_view,
				null);
		// dots_ll=(LinearLayout) layout_roll_view.findViewById(R.id.dots_ll);
		// top_news_viewpager=(LinearLayout) layout_roll_view.findViewById(R.id.top_news_viewpager);
		ViewUtils.inject(context, layout_roll_view);
		// 获取正文的布局，这是个自定义的ListView然后把上面的轮播图放进去
		view = View.inflate(context, R.layout.frag_item_news, null);
		ViewUtils.inject(context, view);
		return view;
	}

	@Override
	public void initData() {
		// 获取本地的缓存数据
		String result = SharedPreferencesUtil.getStringData(context,
				BeiJingApi.BASE_URL + url, "");
		if (!TextUtils.isEmpty(result)) {
			// 解析本地数据展示在页面上
			processData(result);
			return;
		}
		// 联网下载到本地，并解析数据展现在页面上
		getNewItemPager();

	}

	private void getNewItemPager() {
		// 获取当前页面的数据操作
		getData(HttpMethod.GET, BeiJingApi.BASE_URL + url, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 把数据存在本地
						SharedPreferencesUtil.saveStringData(context,
								BeiJingApi.BASE_URL + url, responseInfo.result);
						processData(responseInfo.result);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.i("获取新闻失败");

					}
				});

	}

	/**
	 * 解析数据展示在页面上json
	 * 
	 * @param result
	 */
	private void processData(String result) {
		// NewBean是这个页面所有新闻的对象
		NewBean bean = GsonUtil.jsonToBean(result, NewBean.class);
		if (bean.data.topnews.size() > 0) {
			for (int i = 0; i < bean.data.topnews.size(); i++) {
				// 获取轮播图的标题
				titleList.add(bean.data.topnews.get(i).title);
				// 获取轮播图的图片url
				urlImageList.add(bean.data.topnews.get(i).url);
			}
			// 初始化红色的标记
			initDot();

			// 以下都是调用的自定义控件的方法，用来组装（将对应的View剖析出来，以后按照传递参数的方式直接去使用）
			RollViewPager rollViewPager = new RollViewPager(context, viewList);
			rollViewPager.initTitleList(top_news_title, titleList);
			rollViewPager.initImgUrlList(urlImageList);
			rollViewPager.startRoll();
			// 去除之前所有的轮播图的信息，然后再把自定义的界面添加进来
			top_news_viewpager.removeAllViews();
			top_news_viewpager.addView(rollViewPager);

			// 需要添加到listView上面去
			// -----------------------不知道什么意思
			if (lv_item_news.getRefreshableView().getHeaderViewsCount() < 1) {
				lv_item_news.getRefreshableView().addHeaderView(lv_item_news);
			}
		}
		// 填充ListView后 头部轮播图才会显示
		if (bean.data.news.size() > 0) {
			// 填充ListView
			if (myBaseAdapter == null) {
				myBaseAdapter = new MyBaseAdapter(context, bean.data.news);
				lv_item_news.getRefreshableView().setAdapter(myBaseAdapter);
			} else {
				myBaseAdapter.notifyDataSetChanged();
			}
		}

	}

	// 填充轮播图后面的新闻数据
	class MyBaseAdapter extends BeijingAdapter<News> {

		public MyBaseAdapter(Context context, List<News> list) {
			super(context, list);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(context, R.layout.layout_news_item, null);
			ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			TextView tv_pub_date = (TextView) view
					.findViewById(R.id.tv_pub_date);

			return view;
		}

	}

	// 初始化红色的标记
	private void initDot() {
		dots_ll.removeAllViews();
		viewList.clear();
		// 遍历图片集合
		for (int i = 0; i < urlImageList.size(); i++) {
			View view = new View(context);
			if (i == 0) {
				view.setBackgroundResource(R.drawable.dot_focus);
			} else {
				view.setBackgroundResource(R.drawable.dot_normal);
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					CommonUtil.dip2px(context, 6),
					CommonUtil.dip2px(context, 6));
			view.setLayoutParams(layoutParams);
			// 设置红点之间的距离，为左右阁5个px
			layoutParams.setMargins(5, 0, 5, 0);
			dots_ll.addView(view);
			viewList.add(view);
		}

	}

}
