package com.touzila.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 文本滚动的控件
 * @author lenovo
 *
 */
public class FocusTextView extends TextView {

	public FocusTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}
