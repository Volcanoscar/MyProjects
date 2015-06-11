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
	// �ҵ��˵��е�ListViewȻ���������������
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

	// ͨ��MianActivity�����ҵķ��������ݹ���һ������������˵���
	public void initMenu(List<String> list) {
		this.titleList = list;
		adapter = new MyAdapter(context, list);
		lv_menu_news_center.setAdapter(adapter);
		// ��Ŀ��������¼�
		lv_menu_news_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPosition = position;
				// ֪ͨ���������������Ĳ˵�����ʽ
				adapter.notifyDataSetChanged();
				// ����MainActivity���µ�newCenterPage�ն����е�switchPage��i���ķ���������ҳ������
				// 1����ȡNewCenterPager����
				// 2��NewCenterPager��������HomeFragment��list��
				// 3����ȡHomneFragment����
				// 4������NewCenterPager�еķ���
				((MainActivity) context).switchHomeFragment()
						.switchNewCenterPager().switchPager(position);
				slidingMenu.toggle();
			}
		});

	}

	// ������������
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
			// ��View �е��ı��������
			holder.tv_menu_item.setText(list.get(position));
			//
			if (position == currentPosition) {
				holder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_select);
				holder.tv_menu_item.setTextColor(context.getResources()
						.getColor(R.color.red));
				convertView
						.setBackgroundResource(R.drawable.menu_item_bg_select);
				// ���û�б�ѡ���򷵻�֮ǰ�����״̬
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
