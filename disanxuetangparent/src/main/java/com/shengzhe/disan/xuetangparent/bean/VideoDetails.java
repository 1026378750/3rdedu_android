package com.shengzhe.disan.xuetangparent.bean;

/**
 * Created by acer on 2017/12/6.
 */

public class VideoDetails {
    //课程id
    private int courseId;
    //折扣值
    private int courseDiscount;
    //课程名称
    private String courseName;
    //视频分类名称
    private String videoTypeName;
    //课程价格
    private int price;
    //课程图片地址
    private String pictureUrl;
    //折扣价格
    private int discountPrice;
    //讲师
    private String lecturer;
    //讲师简介
    private String lecturerInfo;
    //课程详情
    private String remark;
    //是否购买过课程 1未购买 2已购买
    private int isAreadyBuy;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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

    public String getLecturerInfo() {
        return lecturerInfo;
    }

    public void setLecturerInfo(String lecturerInfo) {
        this.lecturerInfo = lecturerInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsAreadyBuy() {
        return isAreadyBuy;
    }

    public void setIsAreadyBuy(int isAreadyBuy) {
        this.isAreadyBuy = isAreadyBuy;
    }
}
