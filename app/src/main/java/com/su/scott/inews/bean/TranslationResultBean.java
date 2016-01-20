package com.su.scott.inews.bean;

import java.util.List;

/**
 * @类名 TranslationResultBean
 * @描述 翻译结果bean
 * @作者 Su
 * @时间
 */
public class TranslationResultBean {
    private String src;
    private String dst;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "TransResult{" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                '}';
    }
}
