package com.limxing.safe.db.dao;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * ���ߣ�Limxing ʱ�䣺 2015-5-31 ����7:47:30
 * 
 * �������������ݿ������
 */
public class AntivirusDao {
	private static SQLiteDatabase db;

	/**
	 * ���ݿ�Ĳ��ҹ���
	 * 
	 * @param md5
	 * @return ��������null
	 */
	public static String find(String md5) {
		String desc = null;
		db = SQLiteDatabase.openDatabase(
				"/data/data/com.limxing.safe/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}

	/**
	 * �ж����ݿ��������
	 * 
	 * @return true���� false������
	 */
	public static boolean isExist() {
		File file = new File("/data/data/com.limxing.safe/files/antivirtus.db");
		return file.exists() && file.length() > 0;

	}

	/**
	 * ��ȡ���ݿ��Եİ汾��
	 * 
	 * @return �����ַ���
	 */
	public static String getDBVersion() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.limxing.safe/files/antivirtus.db", null,
				SQLiteDatabase.OPEN_READONLY);
		String version = "0";
		Cursor cursor = db.rawQuery("select subcnt from version", null);
		if (cursor.moveToNext()) {
			version = cursor.getString(0);
		}
		cursor.close();
		db.close();

		return version;
	}

	/**
	 * �������ݿ�汾�Ĳ���
	 * 
	 * @param newVersion
	 */
	public static void updataDBVersion(int newVersion) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.limxing.safe/files/antivirtus.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		ContentValues values = new ContentValues();
		values.put("subcnt", newVersion);
		db.update("version", values, null, null);
		db.close();
	}

	/**
	 * �������ݿ�Ĳ���
	 * 
	 * @param desc
	 * @param md5
	 */
	public static void add(String desc, String md5) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.limxing.safe/files/antivirtus.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		ContentValues values = new ContentValues();
		values.put("md5", md5);
		values.put("desc", desc);
		values.put("type", 6);
		values.put("name", "Android.Hack.i22hkt.a");
		db.insert("datable", null, values);
		db.close();

	}
}
