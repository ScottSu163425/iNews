package com.su.scott.inews.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;

/**
 * @类名 AppManager
 * @描述 Activity管理类
 * @作者 Su
 * @时间 2015年10月16日
 */
public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mInstance = null;

    private static final int FLAG_EMPTY = -1;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (null == mInstance) {
            mInstance = new AppManager();
            mActivityStack = new Stack<Activity>();
        }

        return mInstance;
    }


    public static int getActivityCount() {
        return mActivityStack.size();
    }

    /**
     * 将指定Activity压入栈顶
     *
     * @param activity
     * @return 该Activity在栈中的index
     */
    public synchronized int pushActivity(Activity activity) {
        mActivityStack.push(activity);
        // Toast.makeText(activity, "push:" + activity.getClass() + "",
        // 0).show();
        return mActivityStack.indexOf(activity);
    }

    /**
     * 弹出栈顶Activity
     *
     * @return
     */
    public synchronized Activity popActivity() {
        if (mActivityStack.empty()) {
            return null;
        }

        return mActivityStack.pop();
    }

    /**
     * 弹出栈顶Activity
     *
     * @return
     */
    public synchronized boolean popActivity(Activity actiivty) {
        if (mActivityStack.empty()) {
            return false;
        }

        return mActivityStack.remove(actiivty);
    }

    /**
     * 从Activity栈中弹出指定Activity,并销毁
     *
     * @param c
     * @return
     */
    public synchronized int finishActivity(Class c) {
        if (!isExistActivity(c)) {
            return FLAG_EMPTY;
        }

        int location = getActivityIndex(c);
        Activity a = mActivityStack.get(location);
        mActivityStack.remove(location);
        a.finish();
        // Toast.makeText(a, "finish:" + a.getClass() + "", 0).show();
        return location;
    }

    /**
     * 关闭栈中所有Activity
     */
    public synchronized void finishAll() {
        if (null != mActivityStack) {
            // while (0 != mActivityStack.size()) {
            // Activity a = mActivityStack.pop();
            // a.finish();
            // a = null;
            // }
            for (Activity a : mActivityStack) {
                a.finish();
            }
            mActivityStack.clear();
            // mActivityStack = null;
        }
    }

    /**
     * 在栈中只保留一个指定Activity
     *
     * @param c
     */
    public synchronized void finishAllExcept(Class c) {
        if (!isExistActivity(c)) {
            return;
        }

        if (null != mActivityStack) {
            // int location = getActivityIndex(c);
            // if (location == FLAG_EMPTY) {
            // return;
            // }
            // int n = mActivityStack.size();
            // for (int i = 0; i < n; i++) {
            // if (i == location) {
            // continue;
            // }
            // Activity a = mActivityStack.get(i);
            // mActivityStack.remove(i);
            // a.finish();
            // a = null;
            // }
            //

            List<Activity> listToRemove = new ArrayList<Activity>();
            for (Activity a : mActivityStack) {
                if (a.getClass().equals(c)) {
                    continue;
                }
                a.finish();
                listToRemove.add(a);
            }
            mActivityStack.removeAll(listToRemove);
        }
    }

    /**
     * 指定Activity在栈中是否存在
     *
     * @param c
     * @return
     */
    public boolean isExistActivity(Class c) {
        if (mActivityStack.empty()) {
            return false;
        }

        for (Activity a : mActivityStack) {
            if ((a.getClass()).equals(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定Activity在栈中的index
     *
     * @param c
     * @return
     */
    public int getActivityIndex(Class c) {
        int result = FLAG_EMPTY;

        if (!isExistActivity(c)) {
            return FLAG_EMPTY;
        }

        int n = mActivityStack.size();
        for (int i = 0; i < n; i++) {
            Activity a = mActivityStack.get(i);
            if ((a.getClass().equals(c))) {
                result = i;
                break;
            }
        }

        return result;
    }

}
