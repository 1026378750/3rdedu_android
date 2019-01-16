package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/29.
 */

public class CourseLiveBean implements Parcelable{
    public int duration;
    /**
     * gradeName : 一年级
     * salesVolume : 0
     * totalPrice : 3000000
     * singleClassTime : 2
     * subjectName : 化学
     * maxUser : 23
     */

    public String gradeName;
    public int salesVolume;
    public long totalPrice;
    public int singleClassTime;
    public String subjectName;
    public int maxUser;





    public String photoUrl;

    public CourseLiveBean() {
    }

    public String courseName;
    public int price;
    public String directTypeName;
    public String remark;
    public int courseId;
    public int courseType;
    public int classTime;
    public int isJoin;


    protected CourseLiveBean(Parcel in) {
        duration = in.readInt();
        gradeName = in.readString();
        salesVolume = in.readInt();
        totalPrice = in.readLong();
        singleClassTime = in.readInt();
        subjectName = in.readString();
        maxUser = in.readInt();
        photoUrl = in.readString();
        courseName = in.readString();
        price = in.readInt();
        directTypeName = in.readString();
        remark = in.readString();
        courseId = in.readInt();
        courseType = in.readInt();
        classTime = in.readInt();
        isJoin = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(duration);
        dest.writeString(gradeName);
        dest.writeInt(salesVolume);
        dest.writeLong(totalPrice);
        dest.writeInt(singleClassTime);
        dest.writeString(subjectName);
        dest.writeInt(maxUser);
        dest.writeString(photoUrl);
        dest.writeString(courseName);
        dest.writeInt(price);
        dest.writeString(directTypeName);
        dest.writeString(remark);
        dest.writeInt(courseId);
        dest.writeInt(courseType);
        dest.writeInt(classTime);
        dest.writeInt(isJoin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseLiveBean> CREATOR = new Creator<CourseLiveBean>() {
        @Override
        public CourseLiveBean createFromParcel(Parcel in) {
            return new CourseLiveBean(in);
        }

        @Override
        public CourseLiveBean[] newArray(int size) {
            return new CourseLiveBean[size];
        }
    };

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDirectTypeName() {
        return directTypeName;
    }

    public void setDirectTypeName(String directTypeName) {
        this.directTypeName = directTypeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getSingleClassTime() {
        return singleClassTime;
    }

    public void setSingleClassTime(int singleClassTime) {
        this.singleClassTime = singleClassTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(int maxUser) {
        this.maxUser = maxUser;
    }
}
