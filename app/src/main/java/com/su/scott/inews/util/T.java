package com.su.scott.inews.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @类名 T
 * @描述 Toast显示
 * @作者 Su
 * @时间 2015年12月9日 11:12:40
 */
public class T {

    private static final int GRAVITY_DEFAULT = Gravity.BOTTOM;

    private T() {

    }

    /**
     * 显示短时间Toast
     *
     * @param context
     * @param msg
     */
    public static void showShort(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_SHORT, GRAVITY_DEFAULT);
    }

    /**
     * 显示短时间Toast
     *
     * @param context
     * @param msg
     */
    public static void showShort(Context context, int msg) {
        showToast(context, msg, Toast.LENGTH_SHORT, GRAVITY_DEFAULT);
    }

    /**
     * 显示短时间Toast(中心显示)
     *
     * @param context
     * @param msg
     */
    public static void showShortCenter(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * 显示短时间Toast(中心显示)
     *
     * @param context
     * @param msg
     */
    public static void showShortCenter(Context context, int msg) {
        showToast(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * 显示长时间Toast
     *
     * @param context
     * @param msg
     */
    public static void showLong(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_LONG, GRAVITY_DEFAULT);
    }

    /**
     * 显示长时间Toast
     *
     * @param context
     * @param msg
     */
    public static void showLong(Context context, int msg) {
        showToast(context, msg, Toast.LENGTH_LONG, GRAVITY_DEFAULT);
    }

    /**
     * 显示长时间Toast(中心显示)
     *
     * @param context
     * @param msg
     */
    public static void showLongCenter(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    /**
     * 显示长时间Toast(中心显示)
     *
     * @param context
     * @param msg
     */
    public static void showLongCenter(Context context, int msg) {
        showToast(context, msg, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    private static void showToast(Context context, CharSequence msg, int duration, int gravity) {
        Toast toast = Toast.makeText(context, msg, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    private static void showToast(Context context, int msg, int duration, int gravity) {
        Toast toast = Toast.makeText(context, msg, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }
}
