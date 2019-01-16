package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2017/11/30.
 */

public class MineClass implements Parcelable{
    /**
     * 线下1对1
     */
    public  String  directTypeName="";
    /**
     * · 学生上门
     */
    public  String  teachingMethodName="";//
    /**
     * · 已开课
     */
    public  String  courseType="";//已开课
    /**
     * · 高中语文-高中语文现代文阅读与理解第三阶段
     */
    public  String  sellerName="";
    /**
     * · 老师名
     */
    public  String  teacherName ="";
    /**
     * · 老师地止
     */
    public  String  teachingAddress="";
    /**
     * · 开课时间
     */
    public  String  openTime="";
    /**
     * · 已上课次
     */
    public  String  afterClass="";
    /**
     * · 课次状态
     */
    public  String  classStatus="";

    /**
     * url
     */
     public String  url="";


    public MineClass() {
    }

    protected MineClass(Parcel in) {
        directTypeName = in.readString();
        teachingMethodName = in.readString();
        courseType = in.readString();
        sellerName = in.readString();
        teacherName = in.readString();
        teachingAddress = in.readString();
        openTime = in.readString();
        afterClass = in.readString();
        classStatus = in.readString();
        url = in.readString();
    }

    public static final Creator<MineClass> CREATOR = new Creator<MineClass>() {
        @Override
        public MineClass createFromParcel(Parcel in) {
            return new MineClass(in);
        }

        @Override
        public MineClass[] newArray(int size) {
            return new MineClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(directTypeName);
        dest.writeString(teachingMethodName);
        dest.writeString(courseType);
        dest.writeString(sellerName);
        dest.writeString(teacherName);
        dest.writeString(teachingAddress);
        dest.writeString(openTime);
        dest.writeString(afterClass);
        dest.writeString(classStatus);
        dest.writeString(url);
    }
}
