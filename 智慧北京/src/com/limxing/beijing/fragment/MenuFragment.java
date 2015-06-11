package com.limxing.beijing.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.beijing.MainActivity;
import com.limxing.beijing.R;

public class MenuFragment extends BaseFragment {
	private int currentPosition = 0;
	private List<String> titleList;
	// 找到菜单中的ListView然后向里面填充数据
	@ViewInject(R.id.lv_menu_news_center)
	public ListView lv_menu_news_center;
	private MyAdapter adapter;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {

	}

	// 通过MianActivity调用我的方法，传递过来一个集合填充左侧菜单栏
	public void initMenu(List<String> list) {
		this.titleList = list;
		adapter = new MyAdapter(context, list);
		lv_menu_news_center.setAdapter(adapter);
		// 条目被点击的事件
		lv_menu_news_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPosition = position;
				// 通知数据是适配器更改菜单的样式
				adapter.notifyDataSetChanged();
				// 调用MainActivity中下的newCenterPage日对象中的switchPage（i）的方法，更改页面数据
				// 1、获取NewCenterPager对象
				// 2、NewCenterPager对象来自HomeFragment的list中
				// 3、获取HomneFragment对象
				// 4、调用NewCenterPager中的方法
				((MainActivity) context).switchHomeFragment()
						.switchNewCenterPager().switchPager(position);
				slidingMenu.toggle();
			}
		});

	}

	// 数据适配器，
	public class MyAdapter extends BeijingAdapter<String> {

		public MyAdapter(Context context, List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.layout_item_menu,
						null);
				holder.iv_menu_item = (ImageView) convertView
						.findViewById(R.id.iv_menu_item);
				holder.tv_menu_item = (TextView) convertView
						.findViewById(R.id.tv_menu_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 向View 中的文本添加文字
			holder.tv_menu_item.setText(list.get(position));
			//
			if (position == currentPosition) {
				holder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_select);
				holder.tv_menu_item.setTextColor(context.getResources()
						.getColor(R.color.red));
				convertView
						.setBackgroundResource(R.drawable.menu_item_bg_select);
				// 如果没有被选中则返回之前爱你的状态
			} else {
				holder.tv_menu_item.setTextColor(context.getResources()
						.getColor(R.color.white));
				holder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_normal);
				convertView.setBackgroundResource(R.drawable.transparent);
			}
			return convertView;
		}

	}

	private class ViewHolder {
		ImageView iv_menu_item;
		TextView tv_menu_item;
	}

}
