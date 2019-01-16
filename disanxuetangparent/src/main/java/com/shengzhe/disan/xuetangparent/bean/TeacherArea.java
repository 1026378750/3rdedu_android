package com.shengzhe.disan.xuetangparent.bean;

/**
 * Created by acer on 2017/12/5.
 */

public class TeacherArea {
    private int id;
    private Object version;
    private Object lastAccessTime;
    private String areaCode;
    private String areaName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public Object getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Object lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}

