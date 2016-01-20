package com.su.scott.inews.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @类名 AppConfig
 * @描述 配置数据(SharedPreferences)管理类
 * @作者 Su
 * @时间 2015年10月16日
 */
public class AppConfig {
    private static SharedPreferences mSharedPreferences;
    private static AppConfig mInstance = null;

    private AppConfig() {
    }

    public synchronized static AppConfig getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new AppConfig();
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mInstance;
    }

    public void save(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void save(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void save(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void save(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void save(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public float get(String key, float defaultValue) {
        return (mSharedPreferences.getFloat(key, defaultValue));
    }

    public boolean get(String key, boolean defaultValue) {
        return (mSharedPreferences.getBoolean(key, defaultValue));
    }

    public String get(String key, String defaultValue) {
        return (mSharedPreferences.getString(key, defaultValue));
    }

    public int get(String key, int defaultValue) {
        return (mSharedPreferences.getInt(key, defaultValue));
    }

    public long get(String key, long defaultValue) {
        return (mSharedPreferences.getLong(key, defaultValue));
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
