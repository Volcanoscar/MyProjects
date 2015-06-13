package com.limxing.beijing.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.limxing.beijing.R;
import com.limxing.beijing.activity.NewsActivity;
import com.limxing.beijing.bean.NewBean;
import com.limxing.beijing.bean.NewBean.News;
import com.limxing.beijing.fragment.BeijingAdapter;
import com.limxing.beijing.utils.BeiJingApi;
import com.limxing.beijing.utils.CommonUtil;
import com.limxing.beijing.utils.GsonUtil;
import com.limxing.beijing.utils.SharedPreferencesUtil;
import com.limxing.beijing.view.RollViewPager;
import com.limxing.beijing.view.RollViewPager.onPageClick;
import com.limxing.beijing.view.pullrefreshview.PullToRefreshBase;
import com.limxing.beijing.view.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.limxing.beijing.view.pullrefreshview.PullToRefreshListView;

public class NewItemPager extends BasePager {

	private String IDS = "ids";
	private String url;
	private String moreUrl;
	private View layout_roll_view;
	// 轮播图片的位置
	// @ViewInject(R.id.top_news_viewpager)
	public LinearLayout top_news_viewpager;
	// 轮播图的标题
	// @ViewInject(R.id.top_news_title)
	public TextView top_news_title;
	// 轮播图放置红点的位置
	// @ViewInject(R.id.dots_ll)
	public LinearLayout dots_ll;

	// @ViewInject(R.id.lv_item_news)
	public PullToRefreshListView lv_item_news;
	// 图片关联的标题
	private List<String> titleList = new ArrayList<String>();
	// 图片对应的url集合
	private List<String> urlImageList = new ArrayList<String>();
	// 红色放置点的集合
	private List<View> viewList = new ArrayList<View>();
	private MyBaseAdapter myBaseAdapter;
	// 用于是刷新还是加载的清空操作
	private List<News> newList = new ArrayList<News>();
	private List<String> idList = new ArrayList<String>();

	public NewItemPager(Context context, String url) {
		super(context);
		this.url = url;
	}

	@Override
	public View initView() {
		// 获取轮播图的布局
		layout_roll_view = View.inflate(context, R.layout.layout_roll_view,
				null);
		top_news_title = (TextView) layout_roll_view
				.findViewById(R.id.top_news_title);
		top_news_viewpager = (LinearLayout) layout_roll_view
				.findViewById(R.id.top_news_viewpager);
		dots_ll = (LinearLayout) layout_roll_view.findViewById(R.id.dots_ll);
		// ViewUtils.inject(context, layout_roll_view);
		// 获取正文的布局，这是个自定义的ListView然后把上面的轮播图放进去
		view = View.inflate(context, R.layout.frag_item_news, null);
		lv_item_news = (PullToRefreshListView) view
				.findViewById(R.id.lv_item_news);
		// ViewUtils.inject(context, view);

		// 操作自定义的控件屏蔽下拉加载的错误
		// 下拉加载设为false
		lv_item_news.setPullLoadEnabled(false);
		lv_item_news.setScrollLoadEnabled(true);
		// 设置刷新监听
		lv_item_news.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新操作，请求服务器
				getNewItemPager(url, true);

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载更多
				getNewItemPager(moreUrl, false);

			}
		});
		// 设置已读变色的操作
		lv_item_news.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (!newList.get(position - 1).isRead) {
							// 如果发现是未读，点击后设置为已读
							newList.get(position - 1).isRead = true;
							// 把已读的信息的唯一标识IDs添加到sp中
							String ids = SharedPreferencesUtil.getStringData(
									context, IDS, "");
							SharedPreferencesUtil.saveStringData(context, IDS,
									ids + "#" + newList.get(position - 1).id);
						}
						// 通知更改ui
						myBaseAdapter.notifyDataSetChanged();
						// 转向新汶页面
						Intent intent = new Intent(context, NewsActivity.class);
						intent.putExtra("url", newList.get(position - 1).url);
						context.startActivity(intent);

					}
				});

		return view;
	}

	@Override
	public void initData() {
		// 初始化数据时，先将存储在本地的id分割成一个数组
		// 并且放置在集合中，然后然后跟请求服务端的数据进行数据匹配，如果匹配上则说明已读，则将该字段设置为已读
		idList.clear();
		String ids = SharedPreferencesUtil.getStringData(context, IDS, "");
		String[] s = ids.split("#");

		for (String ss : s) {
			idList.add(ss);
		}

		// 获取本地的缓存数据
		String result = SharedPreferencesUtil.getStringData(context,
				BeiJingApi.BASE_URL + url, "");
		if (!TextUtils.isEmpty(result)) {
			// 解析本地数据展示在页面上
			processData(result, true);
		}
		// 联网下载到本地，并解析数据展现在页面上
		getNewItemPager(url, true);

	}

	private void getNewItemPager(final String url, final boolean isRefresh) {
		// 判断url是否为空，意在表示加载没有更多数据了
		if (TextUtils.isEmpty(url)) {
			Toast.makeText(context, "没有更多数据", 0).show();
			// 加载完成隐藏刷新加载的条目
			lv_item_news.onPullDownRefreshComplete();
			lv_item_news.onPullUpRefreshComplete();
			return;

		}
		// 获取当前页面的数据操作
		getData(HttpMethod.GET, BeiJingApi.BASE_URL + url, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 把数据存在本地
						SharedPreferencesUtil.saveStringData(context,
								BeiJingApi.BASE_URL + url, responseInfo.result);
						processData(responseInfo.result, isRefresh);

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
	 * @param isRefresh
	 */
	private void processData(String result, boolean isRefresh) {
		// NewBean是这个页面所有新闻的对象
		NewBean bean = GsonUtil.jsonToBean(result, NewBean.class);
		// 解决红点变多的操作
		if (bean.retcode.equals("200")) {
			moreUrl = bean.data.more;
			if (bean.data.topnews.size() > 0) {
				if (isRefresh) {

					urlImageList.clear();
					titleList.clear();
					for (int i = 0; i < bean.data.topnews.size(); i++) {
						// 获取轮播图的标题
						titleList.add(bean.data.topnews.get(i).title);
						// 获取轮播图的图片url
						urlImageList.add(bean.data.topnews.get(i).topimage);
					}
					// 初始化红色的标记
					initDot();

					// 以下都是调用的自定义控件的方法，用来组装（将对应的View剖析出来，以后按照传递参数的方式直接去使用）
					RollViewPager rollViewPager = new RollViewPager(context,
							viewList, new onPageClick() {

								@Override
								public void onclick(int i) {
									Toast.makeText(context, "" + i, 0).show();

								}
							});
					rollViewPager.initTitleList(top_news_title, titleList);

					rollViewPager.initImgUrlList(urlImageList);
					// rollViewPager.startRoll();

					// 去除之前所有的轮播图的信息，然后再把自定义的界面添加进来
					top_news_viewpager.removeAllViews();
					top_news_viewpager.addView(rollViewPager);

					// 需要添加到listView上面去
					// -----------------------不知道什么意思
					if (lv_item_news.getRefreshableView().getHeaderViewsCount() < 1) {
						lv_item_news.getRefreshableView().addHeaderView(
								layout_roll_view);
					}
				}
			}

			// 填充ListView后 头部轮播图才会显示
			if (bean.data.news.size() > 0) {
				if (isRefresh) {
					// 如果刷新则将原有数据全部清空，然后进行添加操作
					newList.clear();
				}
				// 如果不是刷新则在原有的集合基础上添加对象
				newList.addAll(bean.data.news);

				// 这是在加载刷新下，对数据进行分析设置已读的操作
				for (int i = 0; i < newList.size(); i++) {
					if (idList.contains(newList.get(i).id)) {
						newList.get(i).isRead = true;
					} else {
						newList.get(i).isRead = false;
					}

				}

				// 填充ListView
				if (myBaseAdapter == null) {
					// myBaseAdapter = new MyBaseAdapter(context,
					// bean.data.news);
					// 更改为newList
					myBaseAdapter = new MyBaseAdapter(context, newList);
					lv_item_news.getRefreshableView().setAdapter(myBaseAdapter);
				} else {
					myBaseAdapter.notifyDataSetChanged();
				}
			}
			// 加载完成隐藏刷新加载的条目
			lv_item_news.onPullDownRefreshComplete();
			lv_item_news.onPullUpRefreshComplete();
		}
	}

	// 填充轮播图后面的新闻数据
	class MyBaseAdapter extends BeijingAdapter<News> {

		public MyBaseAdapter(Context context, List<News> list) {
			super(context, list);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.layout_news_item,
						null);
			}
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			ImageView iv_img = (ImageView) convertView
					.findViewById(R.id.iv_img);
			TextView tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			TextView tv_pub_date = (TextView) convertView
					.findViewById(R.id.tv_pub_date);
			bitmapUtils.display(iv_img,
					BeiJingApi.BASE_URL + list.get(position).listimage);
			tv_title.setText(list.get(position).title);
			tv_pub_date.setText(list.get(position).pubdate);
			// 设置字体的颜色
			if (list.get(position).isRead) {
				tv_title.setTextColor(context.getResources().getColor(
						R.color.news_item_has_read_textcolor));

			} else {
				tv_title.setTextColor(context.getResources().getColor(
						R.color.news_item_no_read_textcolor));
			}

			return convertView;
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
