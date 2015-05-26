package com.limxing.safe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

/**
 * 
 * 版权：李利锋个人版权所有
 * 
 * 版本：1.0
 * 
 * 作者：李利锋
 * 
 * 创建日期：2015-5-25 下午6:20:01
 * 
 * 描述：短信备份还原的工具类
 * 
 * 
 * 修订历史：
 * 
 */
public class SmsUtils {

	public static boolean BackUp(final Context context, BackState backState)
			throws IllegalArgumentException, IllegalStateException, IOException {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "address", "body",
				"type", "date" }, null, null, null);
		XmlSerializer serializer = Xml.newSerializer();
		File sdDir = Environment.getExternalStorageDirectory();
		long free = sdDir.getFreeSpace();
		// 判断是否SD卡存在以及空间是否足够
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& free > 1024l * 1024l) {
			// 创建保存位置
			File file = new File(sdDir, "smsBackup.xml");
			FileOutputStream os = new FileOutputStream(file);
			// 初始化xml文件的序列化器
			serializer.setOutput(os, "utf-8");
			// 写xml文件的开头
			serializer.startDocument("utf-8", true);
			// 写根节点
			int size = cursor.getCount();
			backState.beforeBackup(size);
			serializer.startTag(null, "smss");
			serializer.attribute(null, "size", String.valueOf(size));
			// 初始化进度条
			int process = 0;
			while (cursor.moveToNext()) {
				serializer.startTag(null, "sms");
				serializer.startTag(null, "body");
				try {
					// 加密处理
					serializer.text(Crypto.encrypt("123", cursor.getString(1)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				serializer.endTag(null, "body");
				serializer.startTag(null, "address");
				serializer.text(cursor.getString(0));
				serializer.endTag(null, "address");
				serializer.startTag(null, "type");
				serializer.text(cursor.getString(2));
				serializer.endTag(null, "type");
				serializer.startTag(null, "date");
				serializer.text(cursor.getString(3));
				serializer.endTag(null, "date");
				serializer.endTag(null, "sms");
				process++;
				backState.process(process);
				SystemClock.sleep(500);

			}
			cursor.close();
			serializer.endTag(null, "smss");
			serializer.endDocument();
			os.flush();
			os.close();
			return true;
		} else {
			throw new IllegalStateException("SD卡不存在或者空间不足");
		}

	}

	public interface BackState {
		// 备份时
		public void process(int process);

		// 备份前
		public void beforeBackup(int size);
	}

	// 还原短信
	public static boolean Restor(Context context, ResortState resortState)
			throws XmlPullParserException, IOException {
		// 解析xml文件
		File file = new File(Environment.getExternalStorageDirectory(),
				"smsBackup.xml");
		if (file.exists()) {
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			// resolver.insert(uri, values);
			XmlPullParser parser = Xml.newPullParser();
			FileInputStream fis = new FileInputStream(file);
			parser.setInput(fis, "utf-8");
			int type = parser.getEventType();
			ContentValues values = new ContentValues();
			int process = 0;
			while (type != XmlPullParser.END_DOCUMENT) {
				if (type == XmlPullParser.START_TAG) {

					if ("smss".equals(parser.getName())) {
						resortState.beforeBackup(Integer.valueOf(parser
								.getAttributeValue(null, "size")));
					}
					if ("body".equals(parser.getName())) {
						try {
							values.put("body",
									Crypto.decrypt("123", parser.nextText()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if ("address".equals(parser.getName())) {
						values.put("address", parser.nextText());
					}
					if ("type".equals(parser.getName())) {
						values.put("type", parser.nextText());
					}
					if ("date".equals(parser.getName())) {
						values.put("date", parser.nextText());
					}
				} else if (type == XmlPullParser.END_TAG
						&& !"smss".equals(parser.getName())) {
					resolver.insert(uri, values);
					process++;
					resortState.process(process);
					SystemClock.sleep(500);
				}
				type = parser.next();
			}
			fis.close();
			return true;
		} else {
			throw new FileNotFoundException("没有可还原的数据");
		}
	}

	public interface ResortState {
		// 备份时
		public void process(int process);

		// 备份前
		public void beforeBackup(int size);
	}

}
