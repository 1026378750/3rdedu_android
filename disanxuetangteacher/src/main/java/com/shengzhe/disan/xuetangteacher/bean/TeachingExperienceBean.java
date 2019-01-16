package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by hy on 2017/12/7.
 */

public class TeachingExperienceBean {
    private int id;
    private String version;
    private String lastAccessTime;
    private String school;
    private long startTime;
    private long endTime;
    private String remark;
    private String teacherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(String lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
