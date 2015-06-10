package com.limxing.beijing.utils;

import com.google.gson.Gson;

public class GsonUtil {
	public static <T> T jsonToBean(String json, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}
}
