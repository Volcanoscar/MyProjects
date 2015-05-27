package com.limxing.safe.utils;

import java.util.ArrayList;
import java.util.List;

import com.limxing.safe.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
/**
 * 
 * 版权：李利锋个人版权所有
 * 
 * 版本：1.0
 * 
 * 作者：李利锋
 * 
 * 创建日期：忘记
 * 
 * 描述：获取手机里面所有联系人
 * 
 * 
 * 修订历史：
 * 
 */
public class ContactInfoUtils {

	public static List<ContactInfo> getAllContactInfos(Context context) {
		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();

		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			if (id != null) {
				ContactInfo info = new ContactInfo();

				Cursor datacursor = resolver.query(datauri, new String[] {
						"data1", "mimetype" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (datacursor.moveToNext()) {
					String data1 = datacursor.getString(0);
					String mimetype = datacursor.getString(1);
					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						info.setName(data1);
					} else if ("vnd.android.cursor.item/im".equals(mimetype)) {
						info.setQq(data1);
					} else if ("vnd.android.cursor.item/email_v2"
							.equals(mimetype)) {
						info.setEmail(data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						info.setPhone(data1);
					}
				}
				datacursor.close();

				infos.add(info);
			}
		}
		cursor.close();
		return infos;
	}
}
