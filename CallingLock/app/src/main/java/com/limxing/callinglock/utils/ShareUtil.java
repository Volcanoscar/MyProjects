package com.limxing.callinglock.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtil {
    public static String CONFIG = "info";
    private static SharedPreferences sharedPreferences;

    public static void saveBooleanData(Context context, String key, boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBooleanData(Context context, String key, boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void saveIntData(Context context, String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getIntData(Context context, String key, int defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, defValue);
    }
}
