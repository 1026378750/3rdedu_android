package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2017/12/7.
 */

public class TeachingExperienceBean implements Parcelable{
    private int id=-1;
    private String version="";
    private String lastAccessTime="";
    private String school="";
    private long startTime=0;
    private long endTime=0;
    private String remark="";
    private String teacherId="";
    private int expeId=0;

    public TeachingExperienceBean() {

    }

    protected TeachingExperienceBean(Parcel in) {
        id = in.readInt();
        version = in.readString();
        lastAccessTime = in.readString();
        school = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        remark = in.readString();
        teacherId = in.readString();
        expeId=in.readInt();
    }

    public static final Creator<TeachingExperienceBean> CREATOR = new Creator<TeachingExperienceBean>() {
        @Override
        public TeachingExperienceBean createFromParcel(Parcel in) {
            return new TeachingExperienceBean(in);
        }

        @Override
        public TeachingExperienceBean[] newArray(int size) {
            return new TeachingExperienceBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(version);
        dest.writeString(lastAccessTime);
        dest.writeString(school);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(remark);
        dest.writeString(teacherId);
        dest.writeInt(expeId);
    }
}
