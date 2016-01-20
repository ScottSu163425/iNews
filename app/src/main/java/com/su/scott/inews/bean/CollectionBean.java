package com.su.scott.inews.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @类名 CollectionBean
 * @描述 新闻收藏bean
 * @作者 Su
 * @时间
 */

@Table(name = "CollectionBean")
public class CollectionBean implements Parcelable {
    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    @Id
    private String url;

    public CollectionBean() {
    }



    public CollectionBean(String ctime, String title, String description, String picUrl, String url) {
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
        return "CollectionBean{" +
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

    public CollectionBean(Parcel source) {
        ctime = source.readString();
        title = source.readString();
        description = source.readString();
        picUrl = source.readString();
        url = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ctime);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(picUrl);
        dest.writeString(url);
    }

    // 实例化静态内部对象CREATOR实现接口Parcelable.Creator
    public static final Creator<CollectionBean> CREATOR = new Creator<CollectionBean>() {

        @Override
        public CollectionBean[] newArray(int size) {
            return new CollectionBean[size];
        }

        // 将Parcel对象反序列化为ParcelableDate
        @Override
        public CollectionBean createFromParcel(Parcel source) {
            return new CollectionBean(source);
        }
    };

}
