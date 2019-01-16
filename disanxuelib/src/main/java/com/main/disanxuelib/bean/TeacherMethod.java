package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liukui on 2017/11/30.
 * 授课方式
 */

public class TeacherMethod implements Parcelable {
    //自增id
    public int id;
    //课程id
    public int courseId;
    //授课方式名称
    public String teachingMethodName;
    public double preprice=0.00;
    //单次课小时价
    public long signPrice;
    //单次课总价
    public long price;
    //单次课折扣后的总价
    public String campusDiscountPrice;
    //单次课折扣后的小时价
    public long campusDiscountSignPrice;
    //授课方式值
    public int teachingMethod;
    public int campusDiscountId;
    public int campusDiscountVersion;
    //校区折扣值
    public int campusDiscountPercent;
    public String name="";
    public String type="";

    public String city="";
    public String address="";
    public String area="";

    protected TeacherMethod(Parcel in) {
        id = in.readInt();
        courseId = in.readInt();
        teachingMethodName = in.readString();
        preprice = in.readDouble();
        signPrice = in.readLong();
        price = in.readLong();
        campusDiscountPrice = in.readString();
        campusDiscountSignPrice = in.readLong();
        teachingMethod = in.readInt();
        campusDiscountId = in.readInt();
        campusDiscountVersion = in.readInt();
        campusDiscountPercent = in.readInt();
        name = in.readString();
        type = in.readString();
        city = in.readString();
        address = in.readString();
        area = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(courseId);
        dest.writeString(teachingMethodName);
        dest.writeDouble(preprice);
        dest.writeLong(signPrice);
        dest.writeLong(price);
        dest.writeString(campusDiscountPrice);
        dest.writeLong(campusDiscountSignPrice);
        dest.writeInt(teachingMethod);
        dest.writeInt(campusDiscountId);
        dest.writeInt(campusDiscountVersion);
        dest.writeInt(campusDiscountPercent);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeString(area);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherMethod> CREATOR = new Creator<TeacherMethod>() {
        @Override
        public TeacherMethod createFromParcel(Parcel in) {
            return new TeacherMethod(in);
        }

        @Override
        public TeacherMethod[] newArray(int size) {
            return new TeacherMethod[size];
        }
    };
}
