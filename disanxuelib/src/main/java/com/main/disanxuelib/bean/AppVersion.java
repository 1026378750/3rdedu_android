package com.main.disanxuelib.bean;

/**
 * 版本
 * Created by acer on 2018/3/21.
 */

public class AppVersion {


    /**
     * id : 1
     * url : static/files/fa982abf0ebf4bdc9e30e3d7065a38c01521536857590.apk.1
     * versionType : 1
     * forceUpdate : 1
     * remark : sssssssss
     */

    private long id;
    private String url;
    private int versionType;
    private int forceUpdate;
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionType() {
        return versionType;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
