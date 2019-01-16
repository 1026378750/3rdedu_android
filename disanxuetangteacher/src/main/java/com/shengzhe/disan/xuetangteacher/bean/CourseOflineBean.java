package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/29.
 */

public class CourseOflineBean implements Parcelable{

    public CourseOflineBean() {
    }

    /**
     * courseName : flhaskjfhaskjfh
     * courseType : 1
     * campusPrice : 888880000
     * singleClassTime : 5
     * courseId : 30
     * remark : Fskldjfkasjhfkljshafhaskfhkashfjahskfhakjhfdlkjahskjfhakjshfjkhasdlkjf
     */

    public String courseName;
    public int courseType;
    public int singleClassTime;
    public int courseId;
    public String courseRemark;
    public String remark;
    public long studentPrice;
    public long teacherPrice;
    public long campusPrice;


    protected CourseOflineBean(Parcel in) {
        courseName = in.readString();
        courseType = in.readInt();
        singleClassTime = in.readInt();
        courseId = in.readInt();
        courseRemark = in.readString();
        remark=in.readString();
        studentPrice = in.readLong();
        teacherPrice = in.readLong();
        campusPrice = in.readLong();
    }

    public static final Creator<CourseOflineBean> CREATOR = new Creator<CourseOflineBean>() {
        @Override
        public CourseOflineBean createFromParcel(Parcel in) {
            return new CourseOflineBean(in);
        }

        @Override
        public CourseOflineBean[] newArray(int size) {
            return new CourseOflineBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeInt(courseType);
        dest.writeInt(singleClassTime);
        dest.writeInt(courseId);
        dest.writeString(courseRemark);
        dest.writeLong(studentPrice);
        dest.writeLong(teacherPrice);
        dest.writeLong(campusPrice);
        dest.writeString(remark);
    }
}
