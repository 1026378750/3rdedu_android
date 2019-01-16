package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 直播课
 * Created by acer on 2017/12/5.
 */

public class LiveBean implements Parcelable {

    //课程名称
    private String courseName;
    //阶段名称
    private String gradeName;
    //上课人数
    private int salesVolume;
    //图片地址
    private String pictureUrl;
    //折扣价格
    public  long getDiscountPrice() {
        return discountPrice;
    }public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }private long discountPrice;
    //直播状态 1：直播中 2：未开课 3：已结束
    private int courseStatus;
    //直播类型
    private String directTypeName;
    //课程开始时间
    private long startTime;
    //课程总价
    public  long getCourseTotalPrice() {
        return courseTotalPrice;
    }public void setCourseTotalPrice(long courseTotalPrice) {
        this.courseTotalPrice = courseTotalPrice;
    }private long courseTotalPrice;
    //课程id
    private int courseId;
    //科目名称
    private String subjectName;
    //老师id
    private int teacherId;
    //老师昵称
    private String nickName;
    //老师头像
    private String photoUrl;
    //教师身份 0自由老师，1平台教师
    private int identity;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }



    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(int courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getDirectTypeName() {
        return directTypeName;
    }

    public void setDirectTypeName(String directTypeName) {
        this.directTypeName = directTypeName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public void setCourseTotalPrice(int courseTotalPrice) {
        this.courseTotalPrice = courseTotalPrice;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    protected LiveBean(Parcel in) {
        courseName = in.readString();
        gradeName = in.readString();
        salesVolume = in.readInt();
        pictureUrl = in.readString();
        discountPrice = in.readLong();
        courseStatus = in.readInt();
        directTypeName = in.readString();
        startTime = in.readLong();
        courseTotalPrice = in.readInt();
        courseId = in.readInt();
        subjectName = in.readString();
        teacherId = in.readInt();
        nickName = in.readString();
        photoUrl = in.readString();
        identity = in.readInt();
        courseTotalPrice=in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(gradeName);
        dest.writeInt(salesVolume);
        dest.writeString(pictureUrl);
        dest.writeLong(discountPrice);
        dest.writeInt(courseStatus);
        dest.writeString(directTypeName);
        dest.writeLong(startTime);
        dest.writeLong(courseTotalPrice);
        dest.writeInt(courseId);
        dest.writeString(subjectName);
        dest.writeInt(teacherId);
        dest.writeString(nickName);
        dest.writeString(photoUrl);
        dest.writeInt(identity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveBean> CREATOR = new Creator<LiveBean>() {
        @Override
        public LiveBean createFromParcel(Parcel in) {
            return new LiveBean(in);
        }

        @Override
        public LiveBean[] newArray(int size) {
            return new LiveBean[size];
        }
    };
}
