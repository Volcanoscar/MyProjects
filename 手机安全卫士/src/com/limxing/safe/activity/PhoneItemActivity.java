package com.limxing.safe.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.domain.ContactInfo;
import com.limxing.safe.utils.ContactInfoUtils;

public class PhoneItemActivity extends Activity {
	private ListView lv_lostfind_phoneitems;
	private List<ContactInfo> list;
	private TextView lostfind_phoneitems_tv_nobody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_phoneitems);
		lv_lostfind_phoneitems = (ListView) findViewById(R.id.lv_lostfind_phoneitems);
		lostfind_phoneitems_tv_nobody=(TextView) findViewById(R.id.lostfind_phoneitems_tv_nobody);
		list = ContactInfoUtils.getAllContactInfos(this);
		lv_lostfind_phoneitems.setAdapter(new MyAdapter());
		if (list.size() < 1) {
			lostfind_phoneitems_tv_nobody.setVisibility(TextView.VISIBLE);
		} else {
			lv_lostfind_phoneitems
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Intent intent = new Intent();
							intent.putExtra("phoneNum", list.get(arg2)
									.getPhone());
							setResult(0, intent);
							finish();

						}
					});
		}
	}

	protected class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(PhoneItemActivity.this,
						R.layout.lostfind_phoneitem, null);
			} else {
				view = convertView;
			}

			TextView tv_lostfind_phoneitem_name = (TextView) view
					.findViewById(R.id.tv_lostfind_phoneitem_name);
			TextView tv_lostfind_phoneitem_number = (TextView) view
					.findViewById(R.id.tv_lostfind_phoneitem_number);
			tv_lostfind_phoneitem_name.setText(list.get(position).getName());
			tv_lostfind_phoneitem_number.setText(list.get(position).getPhone());
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
