package com.limxing.safe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.safe.R;

public class SetItem extends RelativeLayout {
	private TextView set_item_tv_title;
	private TextView set_item_tv_desc;
	private String[] descs;
	private CheckBox set_item_cb;

	public SetItem(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr);
//		init(context);
	}

	public SetItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		init(context);

	}

	public SetItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.limxing.safe", "title");
		String desc = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.limxing.safe", "desc");
		descs = desc.split("#");
		setTitle(title);
	}

	public SetItem(Context context) {
		super(context);
//		init(context);
	}

	// init(Context context)�ǿؼ�������ʱ��ִ�У��ֶ��ڲ�����
	public void init(Context context) {
		View view = View.inflate(context, R.layout.set_item, null);
		set_item_tv_title = (TextView) view
				.findViewById(R.id.set_item_tv_title);
		set_item_tv_desc = (TextView) view.findViewById(R.id.set_item_tv_desc);
		set_item_cb = (CheckBox) view.findViewById(R.id.set_item_cb);
		this.addView(view);
	}

	// ���ڱ����ǹ̶��ģ���˳�ʼ�����ڲ����ü���
	public void setTitle(String title) {
		set_item_tv_title.setText(title);
	}

	// �ɵ����߿��ƿؼ��������Լ�CheckBox��״̬��
	public void changeCheckState(boolean b) {
		if (b) {
			set_item_tv_desc.setText(descs[0]);
			set_item_cb.setChecked(true);
		} else {
			set_item_tv_desc.setText(descs[1]);
			set_item_cb.setChecked(false);
		}
	}

}
