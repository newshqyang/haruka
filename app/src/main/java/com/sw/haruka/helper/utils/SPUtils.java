package com.sw.haruka.helper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SPUtils {

    private static Context context;
    public static void setContext(Context context) {
        SPUtils.context = context;
    }

    /*
        获取int
         */
    public static Integer getIntDefaultZero(String key) {
        return getPreferences(context).getInt(key, 0);
    }

    /*
    保存int
     */
    public static void saveInt(String key, int value) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /*
    保存String
     */
    public static void saveString(String key, String string) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(key, string);
        editor.apply();
    }

    /*
    判断是否登录
     */
    public static boolean getBooleanValueDefaultFalse(String key) {
        return getPreferences(context).getBoolean(key, false);
    }

    /*
    保存Boolean
     */
    public static void saveBoolean(String key, Boolean value) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("PREES", Context.MODE_PRIVATE);
    }

    public static String getStringDefaultNull(String key) {
        return getPreferences(context).getString(key, null);
    }

    /*
        获取set
         */
    public static Set<String> getSetDefault0(String key) {
        return new HashSet<>(getPreferences(context).getStringSet(key, new HashSet<String>()));
    }

    /*
    保存set
     */
    public static void saveSet(String key, Set<String> value) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putStringSet(key, new HashSet<>(value));
        editor.apply();
    }

    /*
    保存到本地
     */
    public static void commit() {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.commit();
    }
}
