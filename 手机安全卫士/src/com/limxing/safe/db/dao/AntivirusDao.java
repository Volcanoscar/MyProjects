package com.limxing.safe.db.dao;

import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	private static SQLiteDatabase db;

	public static String find(String packname) {
		db = SQLiteDatabase.openDatabase(
				"/data/data/com.limxing.safe/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);
		db.query("datable", new String[] { "desc" }, "md5=?", new String[] {},
				null, null, null);
		return null;
	}

}
