package com.limxing.safe.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.limxing.safe.R;
import com.limxing.safe.utils.ToastUtils;

public class LostFindSetThreeActivity extends BaseSetActivity {
	private String safeNum;
	private EditText et_lostfindthree_safenum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_three);
		safeNum = sp.getString("safeNum", "");
		et_lostfindthree_safenum = (EditText) findViewById(R.id.et_lostfindthree_safenum);
		et_lostfindthree_safenum.setText(safeNum);
	}

	/**
	 * ѡ����ϵ�˵İ�ť����¼�
	 * 
	 * @param view
	 */
	public void selectNum(View view) {
		Intent intent = new Intent(this, PhoneItemActivity.class);
		startActivityForResult(intent, 0);

	}

	/**
	 * �ص�ѡ����ϵ�˽��������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// �жϾ��Ƿ�Ϊ��
		if (data != null) {
			et_lostfindthree_safenum.setText(data.getStringExtra("phoneNum"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �����һ��
	 */
	@Override
	void showNext() {
		safeNum=et_lostfindthree_safenum.getText().toString();
		if (TextUtils.isEmpty(safeNum)) {
			ToastUtils.showToast(this, "�������ѡ��һ����ȫ����");
			return;
		}
		Editor editor = sp.edit();
		editor.putString("safeNum", safeNum);
		editor.commit();
		startActivityAndFinishSelf(LostFindSetFourActivity.class);
	}

	/**
	 * �����һ��
	 */
	@Override
	void showPre() {
		startActivityAndFinishSelf(LostFindSetTwoActivity.class);
	}

}
