package com.su.scott.inews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @类名 SearchFilmResultBean
 * @描述 影片信息详情bean
 * @作者 Su
 * @时间
 */
public class SearchFilmResultBean implements Serializable {
    private String title; //闪电侠第一季
    private String tag;//科幻/动作
    private String act;//格兰特·古斯汀 埃涅·赫德森 汤姆·卡瓦纳夫
    private String year;//2014
    private String rating;  //7.8
    private String area;//美国
    private String dir;//大卫·努特尔
    private String desc;//《闪电侠》精彩看点：二次元超级英雄再登电视荧屏...
    private String cover;//http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/0/0l01jm9yobh4xo4.jpg
    private String vdo_status;//play
    //    private Playlinks playlinks; //data error
    private List<Video_rec> video_rec;
    private List<Act_s> act_s;
    private String error_code;

  /*  public static class Playlinks {
        private String youku;
        private String qq;
        private String leshi;
        private String pptv;
        private String sohu;

        public String getYouku() {
            return youku;
        }

        public void setYouku(String youku) {
            this.youku = youku;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getLeshi() {
            return leshi;
        }

        public void setLeshi(String leshi) {
            this.leshi = leshi;
        }

        public String getPptv() {
            return pptv;
        }

        public void setPptv(String pptv) {
            this.pptv = pptv;
        }

        public String getSohu() {
            return sohu;
        }

        public void setSohu(String sohu) {
            this.sohu = sohu;
        }

        @Override
        public String toString() {
            return "Playlinks{" +
                    "youku='" + youku + '\'' +
                    ", qq='" + qq + '\'' +
                    ", leshi='" + leshi + '\'' +
                    ", pptv='" + pptv + '\'' +
                    ", sohu='" + sohu + '\'' +
                    '}';
        }
    }*/

    public static class Video_rec implements Serializable {
        private String detail_url;
        private String cover;
        private String title;

        public String getDetail_url() {
            return detail_url;
        }

        public void setDetail_url(String detail_url) {
            this.detail_url = detail_url;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "Video_rec{" +
                    "detail_url='" + detail_url + '\'' +
                    ", cover='" + cover + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }


    public static class Act_s implements Serializable {
        private String name;
        private String url;
        private String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "Act_s{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<Act_s> getAct_s() {
        return act_s;
    }

    public void setAct_s(List<Act_s> act_s) {
        this.act_s = act_s;
    }

    public List<Video_rec> getVideo_rec() {
        return video_rec;
    }

    public void setVideo_rec(List<Video_rec> video_rec) {
        this.video_rec = video_rec;
    }

   /* public Playlinks getPlaylinks() {
        return playlinks;
    }

    public void setPlaylinks(Playlinks playlinks) {
        this.playlinks = playlinks;
    }*/

    public String getVdo_status() {
        return vdo_status;
    }

    public void setVdo_status(String vdo_status) {
        this.vdo_status = vdo_status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "SearchFilmResultBean{" +
                "title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", act='" + act + '\'' +
                ", year='" + year + '\'' +
                ", rating='" + rating + '\'' +
                ", area='" + area + '\'' +
                ", dir='" + dir + '\'' +
                ", desc='" + desc + '\'' +
                ", cover='" + cover + '\'' +
                ", vdo_status='" + vdo_status + '\'' +
/*
                ", playlinks=" + playlinks +
*/
                ", video_rec=" + video_rec +
                ", act_s=" + act_s +
                ", error_code='" + error_code + '\'' +
                '}';
    }
}
