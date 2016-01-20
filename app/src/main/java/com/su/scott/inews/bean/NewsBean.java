package com.su.scott.inews.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @类名 NewsBean
 * @描述 新闻bean
 * @作者 Su
 * @时间
 */

@Table(name = "NewsBean")
public class NewsBean implements Parcelable {
    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    @Id
    private String url;

    public NewsBean() {
    }

    public NewsBean(String ctime, String title, String description, String picUrl, String url) {
        this.ctime = ctime;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.url = url;
    }


    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "ctime='" + ctime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ctime);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.picUrl);
        dest.writeString(this.url);
    }

    protected NewsBean(Parcel in) {
        this.ctime = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.picUrl = in.readString();
        this.url = in.readString();
    }

    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        public NewsBean createFromParcel(Parcel source) {
            return new NewsBean(source);
        }

        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };
}
