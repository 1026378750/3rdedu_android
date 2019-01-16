package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 课程实体 on 2018/1/17.
 */

public class OfflineCourseBean implements Parcelable{

    //课程编号
    public int courseId = -1;
    //课程名称
    public String courseName="";
    //授课科目
    public String subjectName="";
    //阶段id
    public int gradeId;
    //授课阶段
    public String gradeName="";
    //单次课时
    public String singleTime="";
    //学生上门
    public long studentPrice =0;
    //老师上门
    public long teacherPrice =0;
    //校区上门
    public long campusPrice =0;
    //课程简介
    public String introduction ="";
    //家长试听
    public int isListenApply;

    public OfflineCourseBean() {

    }


    protected OfflineCourseBean(Parcel in) {
        courseId = in.readInt();
        courseName = in.readString();
        subjectName = in.readString();
        gradeName = in.readString();
        singleTime = in.readString();
        studentPrice = in.readLong();
        teacherPrice = in.readLong();
        campusPrice = in.readLong();
        introduction = in.readString();
        isListenApply=in.readInt();
        gradeId=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseId);
        dest.writeString(courseName);
        dest.writeString(subjectName);
        dest.writeString(gradeName);
        dest.writeString(singleTime);
        dest.writeLong(studentPrice);
        dest.writeLong(teacherPrice);
        dest.writeLong(campusPrice);
        dest.writeString(introduction);
        dest.writeInt(isListenApply);
        dest.writeInt(gradeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfflineCourseBean> CREATOR = new Creator<OfflineCourseBean>() {
        @Override
        public OfflineCourseBean createFromParcel(Parcel in) {
            return new OfflineCourseBean(in);
        }

        @Override
        public OfflineCourseBean[] newArray(int size) {
            return new OfflineCourseBean[size];
        }
    };
}
