package com.su.scott.inews.bean;

/**
 * @类名 FilmRankBean
 * @描述 电影票房排行bean
 * @作者 Su
 * @时间
 */
public class FilmRankBean {
    private int id;
    private String rid; //排名
    private String name;
    private String wk;//榜单周数
    private String wboxoffice;//周末票房
    private String tboxoffice;//累计票房

    private String type;

    public FilmRankBean() {
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWk() {
        return wk;
    }

    public void setWk(String wk) {
        this.wk = wk;
    }

    public String getWboxoffice() {
        return wboxoffice;
    }

    public void setWboxoffice(String wboxoffice) {
        this.wboxoffice = wboxoffice;
    }

    public String getTboxoffice() {
        return tboxoffice;
    }

    public void setTboxoffice(String tboxoffice) {
        this.tboxoffice = tboxoffice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FilmRankBean{" +
                "rid='" + rid + '\'' +
                ", name='" + name + '\'' +
                ", wk='" + wk + '\'' +
                ", wboxoffice='" + wboxoffice + '\'' +
                ", tboxoffice='" + tboxoffice + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
