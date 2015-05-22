package com.limxing.safe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * 作者：Limxing 时间： 2015-5-22 下午10:44:20
 * 
 * 描述：操作归属地数据库的类，很烂，db都不关闭
 */
public class NumberAddressDao {
	private static String location;
	private static SQLiteDatabase db;

	public static String findNumber(String mNumber) {
		db = SQLiteDatabase.openDatabase(
				"/data/data/com.limxing.safe/files/address.db", null,
				SQLiteDatabase.OPEN_READONLY);
		switch (mNumber.length()) {
		case 3:
			return findLocation(mNumber);
		case 11:
			String number = mNumber.substring(0, 7);
			return findLocation(number);
		case 7:
			db.close();
			return "本地号码";
		case 8:
			db.close();
			return "本地号码";
		case 10:
			String number1 = mNumber.substring(0, 3);
			return findLocation(number1);

		}
		db.close();
		return "查不到的号码";

	}

	public static String findLocation(String number) {
		Cursor cursor = db
				.rawQuery(
						"select location from data2 where id=(select outkey from data1 where id=?)",
						new String[] { number });
		while (cursor.moveToNext()) {
			location = cursor.getString(0);
			if (location != null) {
				cursor.close();
				db.close();
				return location;
			}
		}
		cursor.close();
		db.close();
		return number;

	}

}
