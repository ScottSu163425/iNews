package com.su.scott.inews.util;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;


/**
 * @类名 UIUtil
 * @描述 界面工具类
 * @作者 Su
 * @时间 2015年12月22日
 */
public class UIUtil {

    public static void setText(TextView textView, CharSequence msg) {
        if ((null != msg) && (null != textView)) {
            textView.setText(msg);
        }
    }

    public static void setText(TextView textView, CharSequence msg, CharSequence textDefault) {
        if (null != textView) {
            if (null != msg) {
                textView.setText(msg);
            } else {
                if (null != textDefault) {
                    textView.setText(textDefault);
                }
            }

        }
    }

    public static void startSwipeRefresh(final SwipeRefreshLayout refreshLayout) {
        if ((null != refreshLayout) && (!(refreshLayout.isRefreshing()))) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });
        }
    }

    public static void stopSwipeRefresh(SwipeRefreshLayout refreshLayout) {
        if ((null != refreshLayout) && (refreshLayout.isRefreshing())) {
            refreshLayout.setRefreshing(false);
        }
    }


    public static void setViewVisiable(View view) {
        if (null != view) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setViewInVisiable(View view) {
        if (null != view) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setViewGone(View view) {
        if (null != view) {
            view.setVisibility(View.GONE);
        }
    }

    public static void showAndHide(View viewToShow, View viewToHide) {
        if (null != viewToShow) {
            setViewVisiable(viewToShow);
        }

        if (null != viewToHide) {
            setViewInVisiable(viewToHide);
        }
    }

    public static void showAndGone(View viewToShow, View viewToGone) {
        if (null != viewToShow) {
            setViewVisiable(viewToShow);
        }

        if (null != viewToGone) {
            setViewGone(viewToGone);
        }
    }

    private static long lastClickTime;

    /**
     * 判断是否快速连续点击
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();

        //1s后再次点击，才能生效
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
