package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/29.
 */

public class CourseDetailsBean implements Parcelable {
    /**
     * courseName : 12
     * subjectName : 2
     * gradeId : 1
     * gradeName : 一年级
     * courseRemark : 2222
     * isListenApply : 1
     * studenPrice : 200
     * teacherPrice : 100
     * campusPrice : 0
     * duration : 2
     */

    public String courseName;
    public String subjectName;
    public int gradeId;
    public String gradeName;
    public String courseRemark;
    public int isListenApply;
    public long studentPrice;
    public long teacherPrice;
    public long campusPrice;
    public int duration;

    protected CourseDetailsBean(Parcel in) {
        courseName = in.readString();
        subjectName = in.readString();
        gradeId = in.readInt();
        gradeName = in.readString();
        courseRemark = in.readString();
        isListenApply = in.readInt();
        studentPrice = in.readLong();
        teacherPrice = in.readLong();
        campusPrice = in.readLong();
        duration = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(subjectName);
        dest.writeInt(gradeId);
        dest.writeString(gradeName);
        dest.writeString(courseRemark);
        dest.writeInt(isListenApply);
        dest.writeLong(studentPrice);
        dest.writeLong(teacherPrice);
        dest.writeLong(campusPrice);
        dest.writeInt(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseDetailsBean> CREATOR = new Creator<CourseDetailsBean>() {
        @Override
        public CourseDetailsBean createFromParcel(Parcel in) {
            return new CourseDetailsBean(in);
        }

        @Override
        public CourseDetailsBean[] newArray(int size) {
            return new CourseDetailsBean[size];
        }
    };
}
