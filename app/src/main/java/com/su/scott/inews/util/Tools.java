package com.su.scott.inews.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public class Tools {

    /**
     * 获取倒序List
     *
     * @param oriList
     * @param <T>
     * @return
     */
    public static <T> List<T> getReverseList(List<T> oriList) {
        List<T> resultList = new ArrayList<T>();

        int size = oriList.size();
        for (int i = size - 1; i >= 0; i--) {
            resultList.add(oriList.get(i));
        }

        return resultList;
    }

    private static long lastClickTime;

    /**
     * 判断是否快速连续点击
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();

        //0.5s后再次点击，才能生效
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
