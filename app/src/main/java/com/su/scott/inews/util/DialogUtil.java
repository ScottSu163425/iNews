package com.su.scott.inews.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


/**
 * @类名 DialogUtil
 * @描述 基于V7包下的AlertDialog工具类
 * @作者 Su
 * @时间 2015年12月
 */
public class DialogUtil {

    public static void showSimple(@NonNull Activity context, CharSequence title, CharSequence msg, CharSequence posText) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton(posText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    public static void showComplex(@NonNull Activity context, CharSequence title, CharSequence msg, CharSequence posText, @NonNull DialogInterface.OnClickListener posListener, CharSequence negText, @NonNull DialogInterface.OnClickListener negListener) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton(posText, posListener).setNegativeButton(negText, negListener).create().show();
    }

    public static AlertDialog make(@NonNull Activity context, CharSequence title, CharSequence msg, CharSequence posText, @NonNull DialogInterface.OnClickListener posListener, CharSequence negText, @NonNull DialogInterface.OnClickListener negListener) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton(posText, posListener).setNegativeButton(negText, negListener).create();
    }


}
