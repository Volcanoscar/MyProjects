package com.limxing.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context) {
		super(context, "callsafe.db", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blackinfo(_id integer primary key autoincrement,number varchar(20),mode varchar(2))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
