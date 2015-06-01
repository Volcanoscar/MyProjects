package com.limxing.safe.db.dao;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * 作者：Limxing 时间： 2015-5-31 下午7:47:30
 * 
 * 描述：病毒数据库操作类
 */
public class AntivirusDao {
	private static SQLiteDatabase db;

	/**
	 * 数据库的查找功能
	 * 
	 * @param md5
	 * @return 描述或者null
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
	 * 判断数据库属否存在
	 * 
	 * @return true存在 false不存在
	 */
	public static boolean isExist() {
		File file = new File("/data/data/com.limxing.safe/files/antivirtus.db");
		return file.exists() && file.length() > 0;

	}

	/**
	 * 获取数据可以的版本号
	 * 
	 * @return 数字字符串
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
	 * 更新数据库版本的操作
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
	 * 更新数据库的操作
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
