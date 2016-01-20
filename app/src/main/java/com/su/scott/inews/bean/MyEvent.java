package com.su.scott.inews.bean;

import android.os.Bundle;

/**
 * Created by Administrator on 2015/12/18.
 */
public class MyEvent {
    private String tag;
    private String flag;
    private Bundle data;


    public MyEvent() {
    }

    public MyEvent(String tag) {
        this.tag = tag;
    }

    public MyEvent(String tag, String flag) {
        this.tag = tag;
        this.flag = flag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFlag() {
        return flag;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
