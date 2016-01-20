package com.su.scott.inews.util;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/12/30.
 */
public class DateUtil {

    /**
     * 获取当天日期字符串（年-月-日）
     *
     * @return
     */
    public static String getDateToday() {
        StringBuilder sb = new StringBuilder();
        sb.append(Calendar.getInstance().get(Calendar.YEAR) + "-");
        sb.append((Calendar.getInstance().get(Calendar.MONTH) + 1) + "-");
        sb.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        return sb.toString();
    }


}
