package com.limxing.safe.utils;

import java.text.DecimalFormat;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

/**
 * Created by limxing on 2015-5-21.
 */
public class StringUtils {
	public final static String UTF_8 = "utf-8";

	/** 鍒ゆ柇瀛楃涓叉槸鍚︽湁鍊硷紝濡傛灉涓簄ull鎴栬�呮槸绌哄瓧绗︿覆鎴栬�呭彧鏈夌┖鏍兼垨鑰呬负"null"瀛楃涓诧紝鍒欒繑鍥瀟rue锛屽惁鍒欏垯杩斿洖false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** 鍒ゆ柇澶氫釜瀛楃涓叉槸鍚︾浉绛夛紝濡傛灉鍏朵腑鏈変竴涓负绌哄瓧绗︿覆鎴栬�卬ull锛屽垯杩斿洖false锛屽彧鏈夊叏鐩哥瓑鎵嶈繑鍥瀟rue */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 杩斿洖涓�涓珮浜畇pannable
	 * @param content 鏂囨湰鍐呭
	 * @param color   楂樹寒棰滆壊
	 * @param start   璧峰浣嶇疆
	 * @param end     缁撴潫浣嶇疆
	 * @return 楂樹寒spannable
	 */
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}


	/** 鏍煎紡鍖栨枃浠跺ぇ灏忥紝涓嶄繚鐣欐湯灏剧殑0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 鏍煎紡鍖栨枃浠跺ぇ灏忥紝淇濈暀鏈熬鐨�0锛岃揪鍒伴暱搴︿竴鑷� */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)锛屼繚鐣欎袱浣嶅皬鏁�
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)锛屼繚鐣欎竴浣嶅皬鏁�
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)锛屼釜浣嶅洓鑸嶄簲鍏�
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)锛屼繚鐣欎袱浣嶅皬鏁�
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)锛屼繚鐣欎竴浣嶅皬鏁�
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)锛屼釜浣嶅洓鑸嶄簲鍏�
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)锛屼繚鐣欎袱浣嶅皬鏁�
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}
}
