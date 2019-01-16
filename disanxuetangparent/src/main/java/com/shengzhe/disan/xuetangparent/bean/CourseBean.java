package com.shengzhe.disan.xuetangparent.bean;

/**
 * Created by acer on 2017/12/20.
 */

public class CourseBean {
    private int classNum;
    private String courseName;
    private String courseTime;
    private int courseType;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private int courseId;
    private String courseTypeName;
    private int id;

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    private String orderItemCode;
    private String parCode;
    private String photoUrl;
    private long roomId;
    private String sellerName;
    private int status;
    private String teachingMethodName;
    //时间段
    private String timeDay;
    //是否是每个类别第一个
    private boolean firstItem;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    private long startTime;

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    private String courseTitle;//大纲名称


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    private int orderNum;

    public String getTeachingAddress() {
        return teachingAddress;
    }

    public void setTeachingAddress(String teachingAddress) {
        this.teachingAddress = teachingAddress;
    }

    private String teachingAddress;


    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classSum) {
        this.classNum = classNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getParCode() {
        return parCode;
    }

    public void setParCode(String parCode) {
        this.parCode = parCode;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTeachingMethodName() {
        return teachingMethodName;
    }

    public void setTeachingMethodName(String teachingMethodName) {
        this.teachingMethodName = teachingMethodName;
    }

    public String getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(String timeDay) {
        this.timeDay = timeDay;
    }


    public boolean isFirstItem() {
        return firstItem;
    }

    public void setFirstItem(boolean firstItem) {
        this.firstItem = firstItem;
    }

}
