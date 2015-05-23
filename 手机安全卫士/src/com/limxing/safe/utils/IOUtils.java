package com.limxing.safe.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by limxing on 2015-5-21.
 */
public class IOUtils {
	/** 鍏抽棴娴� */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
