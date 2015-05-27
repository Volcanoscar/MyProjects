package com.limxing.safe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.limxing.safe.db.AppLockDBOpenHelper;

/*
 * ���ߣ�Limxing ʱ�䣺 2015-5-27 ����7:21:55
 * 
 * ��������ѯ���������ݿ��е���Ϣ����ӳ���������
 */
public class AppLockDao {
	private AppLockDBOpenHelper helper;
	private Context context;

	public AppLockDao(Context context) {
		this.context = context;
		helper = new AppLockDBOpenHelper(context);
	}

	/**
	 * ��ѯ���ݿ����Ƿ���������Ӧ�ó���
	 * 
	 * @param packname
	 * @return
	 */
	public boolean exist(String packname) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("info", null, "packname=?",
				new String[] { packname }, null, null, null);
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	// ��ӳ���������Ϣ��֪ͨ���ݹ۲���ˢ������
	public void add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("info", null, values);
		db.close();
		// ֪ͨ���ݹ۲�����ӳɹ�
		context.getContentResolver().notifyChange(
				Uri.parse("com.limxing.safe.applock"), null);
	}

	// ��ѯ���ݿ������е���Ŀ
	public List<String> findAll() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("info", new String[] { "packname" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;
	}

	// ɾ��Ӧ��������
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("info", "packname=?", new String[] { packname });
		db.close();
		// ֪ͨ���ݹ۲�����ӳɹ�
		context.getContentResolver().notifyChange(
				Uri.parse("com.limxing.safe.applock"), null);
	}
}
