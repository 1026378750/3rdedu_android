package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 线下一对订单详情
 * Created by acer on 2018/1/31.
 */

public class OrderOfflineDetailsBean implements Parcelable {

    /**
     * courseTypeName : 1对1课程
     * courseType : 1
     * onePrice : 1500000
     * orderItem : []
     * giveSum : 0
     * buySum : 12
     * teachingMethod : 1
     * remark : 左手定则用于判断安培力：伸开左手，使拇指与其余四个手指垂直且与手掌在同一平面内；
     * userName : null
     * duration : 2
     * courseName : 李老师教你搭思维积木
     * discountInfo : 线下优惠600元
     * buyerTime : 1514448832779
     * classSum : 12
     * userMobile : 15821629469
     * studentName : joky
     * teachingAddress : 虹口区祥德路989号
     * orderCode : 488327972781397
     * receivablePrice : 30000000
     * studentSex : 0
     * courseId : 4
     * status : 2
     * teachingMethodName : 学生上门
     */

    public String courseTypeName;
    public int courseType;
    public int onePrice;
    public int giveSum;
    public int buySum;
    public int teachingMethod;
    public String remark;
    public String userName;
    public int duration;
    public String courseName;
    public String discountInfo;
    public long buyerTime;
    public int classSum;
    public String userMobile;
    public String studentName;
    public String teachingAddress;
    public String orderCode;
    public long receivablePrice;
    public int studentSex;
    public int courseId;
    public int status;
    public String teachingMethodName;
    public int isJoin;//是否插班 0否 1是
    public String gradeName;//阶段名称
    public String subjectName;//科目名称
    public int classTime;//	课次
    public long totalPrice;//课程总价
    public int maxUser;//最大人数
    public int salesVolume;//已报名人数
    public ArrayList<OrderItemBean> orderItem;


    /**
     * directTypeName : 普通大班课
     * userName : 李麻麻
     * photoUrl : http://203.110.165.138:6889/static/images/6602a0ea07cc4530843b161e307af2ca1514449099699.jpg
     * discountInfo : null
     * price : 3600000
     */

    public String directTypeName;
    public String photoUrl;
    public long price;
    public long payTime;


    protected OrderOfflineDetailsBean(Parcel in) {
        courseTypeName = in.readString();
        courseType = in.readInt();
        onePrice = in.readInt();
        giveSum = in.readInt();
        buySum = in.readInt();
        teachingMethod = in.readInt();
        remark = in.readString();
        userName = in.readString();
        duration = in.readInt();
        courseName = in.readString();
        discountInfo = in.readString();
        buyerTime = in.readLong();
        classSum = in.readInt();
        userMobile = in.readString();
        studentName = in.readString();
        teachingAddress = in.readString();
        orderCode = in.readString();
        receivablePrice = in.readLong();
        studentSex = in.readInt();
        courseId = in.readInt();
        status = in.readInt();
        teachingMethodName = in.readString();
        isJoin = in.readInt();
        gradeName = in.readString();
        subjectName = in.readString();
        classTime = in.readInt();
        totalPrice = in.readLong();
        maxUser = in.readInt();
        salesVolume = in.readInt();
        orderItem = in.createTypedArrayList(OrderItemBean.CREATOR);
        directTypeName = in.readString();
        photoUrl = in.readString();
        price = in.readLong();
        payTime=in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseTypeName);
        dest.writeInt(courseType);
        dest.writeInt(onePrice);
        dest.writeInt(giveSum);
        dest.writeInt(buySum);
        dest.writeInt(teachingMethod);
        dest.writeString(remark);
        dest.writeString(userName);
        dest.writeInt(duration);
        dest.writeString(courseName);
        dest.writeString(discountInfo);
        dest.writeLong(buyerTime);
        dest.writeInt(classSum);
        dest.writeString(userMobile);
        dest.writeString(studentName);
        dest.writeString(teachingAddress);
        dest.writeString(orderCode);
        dest.writeLong(receivablePrice);
        dest.writeInt(studentSex);
        dest.writeInt(courseId);
        dest.writeInt(status);
        dest.writeString(teachingMethodName);
        dest.writeInt(isJoin);
        dest.writeString(gradeName);
        dest.writeString(subjectName);
        dest.writeInt(classTime);
        dest.writeLong(totalPrice);
        dest.writeInt(maxUser);
        dest.writeInt(salesVolume);
        dest.writeTypedList(orderItem);
        dest.writeString(directTypeName);
        dest.writeString(photoUrl);
        dest.writeLong(price);
        dest.writeLong(payTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderOfflineDetailsBean> CREATOR = new Creator<OrderOfflineDetailsBean>() {
        @Override
        public OrderOfflineDetailsBean createFromParcel(Parcel in) {
            return new OrderOfflineDetailsBean(in);
        }

        @Override
        public OrderOfflineDetailsBean[] newArray(int size) {
            return new OrderOfflineDetailsBean[size];
        }
    };
}
