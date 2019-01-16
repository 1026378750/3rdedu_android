package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2017/12/9.
 */

public class OrderCourse implements Parcelable {

    //上课地址 —一对一课程独有
    private String address;
    //已上课程数
    private int orderNum;
    //课程类型
    private int courseType;
    private int orderCourseType;
    //课程id
    private int courseId;
    //视频课播放百家云id —视频课程独有
    private int videoBjyId;
    //视频课播放百家云token —视频课程独有
    private String videoBjyToken;
    //首次开课时间
    private long startTime;
    //课程名称
    private String courseName;
    //课程类型名称
    private String courseTypeName;
    //老师昵称
    private String teacherNickName;
    //购买数量
    private int buyNum;
    //赠送数量
    private int giveNum;
    //老师头像
    private String photoUrl;
    //订单Id
    private int id;
    //课程数量
    private int classNum;
    //课程状态 21待执行 22 正在执行 80上课完成
    private int subStatus;
    //授课方式/直播类型/分类名称
    private String teachingMethodName;

    protected OrderCourse(Parcel in) {
        address = in.readString();
        orderNum = in.readInt();
        courseType = in.readInt();
        orderCourseType = in.readInt();
        courseId = in.readInt();
        videoBjyId = in.readInt();
        videoBjyToken = in.readString();
        startTime = in.readLong();
        courseName = in.readString();
        courseTypeName = in.readString();
        teacherNickName = in.readString();
        buyNum = in.readInt();
        giveNum = in.readInt();
        photoUrl = in.readString();
        id = in.readInt();
        classNum = in.readInt();
        subStatus = in.readInt();
        teachingMethodName = in.readString();
        isJoin = in.readInt();
        videoCourseTypeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeInt(orderNum);
        dest.writeInt(courseType);
        dest.writeInt(orderCourseType);
        dest.writeInt(courseId);
        dest.writeInt(videoBjyId);
        dest.writeString(videoBjyToken);
        dest.writeLong(startTime);
        dest.writeString(courseName);
        dest.writeString(courseTypeName);
        dest.writeString(teacherNickName);
        dest.writeInt(buyNum);
        dest.writeInt(giveNum);
        dest.writeString(photoUrl);
        dest.writeInt(id);
        dest.writeInt(classNum);
        dest.writeInt(subStatus);
        dest.writeString(teachingMethodName);
        dest.writeInt(isJoin);
        dest.writeString(videoCourseTypeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderCourse> CREATOR = new Creator<OrderCourse>() {
        @Override
        public OrderCourse createFromParcel(Parcel in) {
            return new OrderCourse(in);
        }

        @Override
        public OrderCourse[] newArray(int size) {
            return new OrderCourse[size];
        }
    };

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    private int isJoin;

    public String getVideoCourseTypeName() {
        return videoCourseTypeName;
    }

    public void setVideoCourseTypeName(String videoCourseTypeName) {
        this.videoCourseTypeName = videoCourseTypeName;
    }

    private String videoCourseTypeName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }


    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }


    public int getOrderCourseType() {
        return orderCourseType;
    }

    public void setOrderCourseType(int orderCourseType) {
        this.orderCourseType = orderCourseType;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getVideoBjyToken() {
        return videoBjyToken;
    }

    public void setVideoBjyToken(String videoBjyToken) {
        this.videoBjyToken = videoBjyToken;
    }

    public int getVideoBjyId() {
        return videoBjyId;
    }

    public void setVideoBjyId(int videoBjyId) {
        this.videoBjyId = videoBjyId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getTeacherNickName() {
        return teacherNickName;
    }

    public void setTeacherNickName(String teacherNickName) {
        this.teacherNickName = teacherNickName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(int giveNum) {
        this.giveNum = giveNum;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public int getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(int subStatus) {
        this.subStatus = subStatus;
    }

    public String getTeachingMethodName() {
        return teachingMethodName;
    }

    public void setTeachingMethodName(String teachingMethodName) {
        this.teachingMethodName = teachingMethodName;
    }
}

