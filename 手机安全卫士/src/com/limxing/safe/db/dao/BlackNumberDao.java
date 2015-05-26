package com.limxing.safe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.limxing.safe.db.MySQLiteOpenHelper;
import com.limxing.safe.db.domain.BlackNumber;

public class BlackNumberDao {
	private MySQLiteOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new MySQLiteOpenHelper(context);
	}

	// ��
	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		long rowid = db.insert("blackinfo", null, values);
		db.close();
		if (rowid == -1) {
			return false;
		} else {
			return true;
		}
	}

	// ɾ
	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rownumber = db.delete("blackinfo", "number=?",
				new String[] { number });
		db.close();
		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	// ɾ����������
	public boolean deleteAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rownumber = db.delete("blackinfo", null, null);
		db.close();
		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	// �ģ���mode
	public boolean changeMode(String number, String newMode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		int rownumber = db.update("blackinfo", values, "number=?",
				new String[] { number });
		db.close();
		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	// ��,һ�����ݵ�����ģʽ
	public String findMode(String number) {
		String mode = "0";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blackinfo", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	// ��ȫ��������
	public List<BlackNumber> findAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.query("blackinfo", new String[] { "number", "mode" }, null,
						null, null, null, null);
		List<BlackNumber> list = new ArrayList<BlackNumber>();
		while (cursor.moveToNext()) {
			BlackNumber bk = new BlackNumber();
			bk.setNumber(cursor.getString(0));
			bk.setMode(cursor.getString(1));
			list.add(bk);
		}
		cursor.close();
		db.close();
		return list;
	}

	// ������ѯ����
	public List<BlackNumber> findPart(int startIndex, int maxCount) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select number,mode from blackinfo order by _id desc limit ? offset ?",
						new String[] { String.valueOf(maxCount),
								String.valueOf(startIndex) });

		List<BlackNumber> list = new ArrayList<BlackNumber>();
		while (cursor.moveToNext()) {
			BlackNumber bk = new BlackNumber(cursor.getString(0),
					cursor.getString(1));
			list.add(bk);
		}
		cursor.close();
		db.close();
		return list;
	}

	// ��ѯ���ж�������¼
	public int getCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blackinfo",
				new String[] {});
		cursor.moveToNext();
		int count = Integer.valueOf(cursor.getString(0));
		cursor.close();
		db.close();
		return count;
	}

}
