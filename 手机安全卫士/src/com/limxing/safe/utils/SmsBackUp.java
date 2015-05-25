package com.limxing.safe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

/**
 * 
 * ��Ȩ����������˰�Ȩ����
 * 
 * �汾��1.0
 * 
 * ���ߣ�������
 * 
 * �������ڣ�2015-5-25 ����6:20:01
 * 
 * ���������ű��ݻ�ԭ�Ĺ�����
 * 
 * 
 * �޶���ʷ��
 * 
 */
public class SmsBackUp {

	public static boolean backUp(final Context context, BackState backState)
			throws IllegalArgumentException, IllegalStateException, IOException {

		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "address", "body",
				"type", "date" }, null, null, null);
		XmlSerializer serializer = Xml.newSerializer();
		File sdDir = Environment.getExternalStorageDirectory();
		long free = sdDir.getFreeSpace();
		// �ж��Ƿ�SD�������Լ��ռ��Ƿ��㹻
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& free > 1024l * 1024l) {
			// ��������λ��
			File file = new File(sdDir, "smsBackup.xml");
			FileOutputStream os = new FileOutputStream(file);
			// ��ʼ��xml�ļ������л���
			serializer.setOutput(os, "utf-8");
			// дxml�ļ��Ŀ�ͷ
			serializer.startDocument("utf-8", true);
			// д���ڵ�
			int size = cursor.getCount();
			backState.beforeBackup(size);
			serializer.startTag(null, "smss");
			serializer.attribute(null, "size", String.valueOf(size));
			// ��ʼ��������
			int process = 0;
			while (cursor.moveToNext()) {
				serializer.startTag(null, "sms");
				serializer.startTag(null, "body");
				try {
					// ���ܴ���
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
			throw new IllegalStateException("SD�������ڻ��߿ռ䲻��");
		}

	}

	public interface BackState {
		// ����ʱ
		public void process(int process);

		// ����ǰ
		public void beforeBackup(int size);
	}

}
