package com.limxing.safe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	/**
	 * ��һ�������������ת����һ���ַ���?
	 * 
	 * @param is
	 * @return
	 */

	public static String readStream(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			is.close();
			return new String(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
