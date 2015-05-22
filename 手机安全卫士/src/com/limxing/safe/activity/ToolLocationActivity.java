package com.limxing.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.limxing.safe.R;
import com.limxing.safe.db.dao.NumberAddressDao;
import com.limxing.safe.utils.ToastUtils;

public class ToolLocationActivity extends Activity {
	private EditText tool_location_et;
	private Button tool_location_bt;
	private TextView tool_location_tv;
	private String mNumber;
	private NumberAddressDao mNumberAddressDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		init();
	}

	private void init() {
		mNumberAddressDao=new NumberAddressDao();
		tool_location_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mNumber=tool_location_et.getText().toString().trim();
				if(TextUtils.isEmpty(mNumber)){
					ToastUtils.showToast(ToolLocationActivity.this, "«Î ‰»Î∫≈¬Î");
					return;
				}
				String location=mNumberAddressDao.findNumber(mNumber);
				tool_location_tv.setText("∫≈¬ÎπÈ ÙµÿŒ™£∫"+location);
				
			}
		});
		
		
	}

	private void initUI() {
		setContentView(R.layout.tools_location);
		tool_location_et = (EditText) findViewById(R.id.tools_location_et);
		tool_location_bt = (Button) findViewById(R.id.tool_location_bt);
		tool_location_tv=(TextView) findViewById(R.id.tool_location_tv);
	
	}

	// ∑µªÿº¸
	public void back(View view) {
		onBackPressed();
	}

}
