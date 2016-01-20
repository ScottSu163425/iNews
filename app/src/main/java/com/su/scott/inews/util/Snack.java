package com.su.scott.inews.util;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @类名 Snack
 * @描述 SnackBar显示类
 * @作者 Su
 * @时间 2015年12月21日
 */
public class Snack {
    public static void showShort(View parent, CharSequence text) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showShort(View parent, CharSequence text, CharSequence action, View.OnClickListener listener) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).setAction(action, listener).show();
    }

    public static void showLong(View parent, CharSequence text) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_LONG).show();
    }


    public static void showLong(View parent, CharSequence text, CharSequence action, View.OnClickListener listener) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_LONG).setAction(action, listener).show();
    }

    public static Snackbar makeShort(View parent, CharSequence text) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar makeShort(View parent, CharSequence text, CharSequence action, View.OnClickListener listener) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).setAction(action, listener);
    }

    public static Snackbar makeLong(View parent, CharSequence text) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_LONG);
    }

    public static Snackbar makeLong(View parent, CharSequence text, CharSequence action, View.OnClickListener listener) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_LONG).setAction(action, listener);
    }


}
