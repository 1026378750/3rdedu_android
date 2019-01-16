package com.main.disanxuelib.bean;

/**
 * Created by acer on 2017/12/4.
 */

public class VideoType {

    private String typeName;//分类名称
    private String appUrl;//图标地址
    private int id;//	分类id

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
