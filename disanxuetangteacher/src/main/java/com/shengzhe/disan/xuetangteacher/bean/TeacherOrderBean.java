package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/31.
 */

public class TeacherOrderBean implements Parcelable {
    public String courseTypeName;
    public int orderId;
    public int giveSum;
    public int buySum;
    public int teachingMethod;
    public int courseType;
    public String userName;
    public String courseName;
    public long buyerTime;
    public String userMobile;
    public int classSum;
    public String teachingAddress;
    public long receivablePrice;
    public int status;
    public String teachingMethodName;
    public String directName;
    public String photoUrl;
    public int isJoin;//isJoin字段，是否可以插班 0否 1是

    protected TeacherOrderBean(Parcel in) {
        courseTypeName = in.readString();
        orderId = in.readInt();
        courseType = in.readInt();
        giveSum = in.readInt();
        buySum = in.readInt();
        teachingMethod = in.readInt();
        userName = in.readString();
        courseName = in.readString();
        buyerTime = in.readLong();
        userMobile = in.readString();
        classSum = in.readInt();
        teachingAddress = in.readString();
        receivablePrice = in.readLong();
        status = in.readInt();
        teachingMethodName = in.readString();
        directName = in.readString();
        photoUrl = in.readString();
        isJoin = in.readInt();
    }

    public static final Creator<TeacherOrderBean> CREATOR = new Creator<TeacherOrderBean>() {
        @Override
        public TeacherOrderBean createFromParcel(Parcel in) {
            return new TeacherOrderBean(in);
        }

        @Override
        public TeacherOrderBean[] newArray(int size) {
            return new TeacherOrderBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseTypeName);
        dest.writeInt(orderId);
        dest.writeInt(giveSum);
        dest.writeInt(buySum);
        dest.writeInt(courseType);
        dest.writeInt(teachingMethod);
        dest.writeString(userName);
        dest.writeString(courseName);
        dest.writeLong(buyerTime);
        dest.writeString(userMobile);
        dest.writeInt(classSum);
        dest.writeString(teachingAddress);
        dest.writeLong(receivablePrice);
        dest.writeInt(status);
        dest.writeString(teachingMethodName);
        dest.writeString(directName);
        dest.writeString(photoUrl);
        dest.writeInt(isJoin);
    }
}
