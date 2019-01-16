package com.shengzhe.disan.xuetangparent.bean;

/**
 * Created by acer on 2017/12/6.
 */

public class PayVideo {
    //课程id
    private int courseId;
    //课程名称
    private String courseName;
    //课程分类名称
    private String videoTypeName;
    //课程原价
    private int price;
    //折扣版本号
    private int courseDiscountVersion;
    //折扣后的价格
    private int discountPrice;
    //讲师名称
    private String lecturer;
    //课程版本号
    private int courseVersion;
    //课程折扣id
    private int courseDiscountId;
    //折扣值，当值为100时表示满折即没有折扣
    private int courseDiscount;
    //优惠价格(优惠了多少钱)
    private int favorablePrice;
    //讲师简介
    private String lecturerInfo;

    public String getLecturerInfo() {
        return lecturerInfo;
    }

    public void setLecturerInfo(String lecturerInfo) {
        this.lecturerInfo = lecturerInfo;
    }

    public int getCourseDiscount() {
        return courseDiscount;
    }

    public void setCourseDiscount(int courseDiscount) {
        this.courseDiscount = courseDiscount;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseVersion() {
        return courseVersion;
    }

    public void setCourseVersion(int courseVersion) {
        this.courseVersion = courseVersion;
    }

    public String getVideoTypeName() {
        return videoTypeName;
    }

    public void setVideoTypeName(String videoTypeName) {
        this.videoTypeName = videoTypeName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCourseDiscountVersion() {
        return courseDiscountVersion;
    }

    public void setCourseDiscountVersion(int courseDiscountVersion) {
        this.courseDiscountVersion = courseDiscountVersion;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getCourseDiscountId() {
        return courseDiscountId;
    }

    public void setCourseDiscountId(int courseDiscountId) {
        this.courseDiscountId = courseDiscountId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getFavorablePrice() {
        return favorablePrice;
    }

    public void setFavorablePrice(int favorablePrice) {
        this.favorablePrice = favorablePrice;
    }
}
