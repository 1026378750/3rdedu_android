package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/30.
 */

public class CourseDetailLiveBean implements Parcelable {
    //课程编号
    public int courseId = -1;
    //课程名称
    public String courseName;
    //课程图片
    public String photoUrl;
    //科目名称
    public String subjectName;
    //年级
    public int gradeId;
    //年级名称
    public String gradeName;
    //直播类型 1，一对一授课 2，互动小班课 3，普通大班课
    public int directType;
    //直播类型名称
    public String directTypeName;
    //最大人数
    public int maxUser;
    //课次
    public int classTime;
    //课程简介
    public String courseRemark;
    //课程单价
    public long price;
    //教学目标
    public String target;
    //适应人群
    public String crowd;
    //课时
    public int duration;

    public CourseDetailLiveBean() {

    }


    protected CourseDetailLiveBean(Parcel in) {
        courseId = in.readInt();
        courseName = in.readString();
        photoUrl = in.readString();
        subjectName = in.readString();
        gradeId = in.readInt();
        gradeName = in.readString();
        directType = in.readInt();
        directTypeName = in.readString();
        maxUser = in.readInt();
        classTime = in.readInt();
        courseRemark = in.readString();
        price = in.readLong();
        target = in.readString();
        crowd = in.readString();
        duration = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseId);
        dest.writeString(courseName);
        dest.writeString(photoUrl);
        dest.writeString(subjectName);
        dest.writeInt(gradeId);
        dest.writeString(gradeName);
        dest.writeInt(directType);
        dest.writeString(directTypeName);
        dest.writeInt(maxUser);
        dest.writeInt(classTime);
        dest.writeString(courseRemark);
        dest.writeLong(price);
        dest.writeString(target);
        dest.writeString(crowd);
        dest.writeInt(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseDetailLiveBean> CREATOR = new Creator<CourseDetailLiveBean>() {
        @Override
        public CourseDetailLiveBean createFromParcel(Parcel in) {
            return new CourseDetailLiveBean(in);
        }

        @Override
        public CourseDetailLiveBean[] newArray(int size) {
            return new CourseDetailLiveBean[size];
        }
    };
}
