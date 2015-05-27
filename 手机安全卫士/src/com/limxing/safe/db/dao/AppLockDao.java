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
 * 作者：Limxing 时间： 2015-5-27 下午7:21:55
 * 
 * 描述：查询程序锁数据库中的信息，添加程序锁内容
 */
public class AppLockDao {
	private AppLockDBOpenHelper helper;
	private Context context;

	public AppLockDao(Context context) {
		this.context = context;
		helper = new AppLockDBOpenHelper(context);
	}

	/**
	 * 查询数据库中是否含有锁定的应用程序
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

	// 添加程序锁的信息并通知内容观察者刷新数据
	public void add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("info", null, values);
		db.close();
		// 通知内容观察者添加成功
		context.getContentResolver().notifyChange(
				Uri.parse("com.limxing.safe.applock"), null);
	}

	// 查询数据库中所有的条目
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

	// 删除应用锁内容
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("info", "packname=?", new String[] { packname });
		db.close();
		// 通知内容观察者添加成功
		context.getContentResolver().notifyChange(
				Uri.parse("com.limxing.safe.applock"), null);
	}
}
