package com.shengzhe.disan.xuetangparent.bean;

import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/15.
 */

public class OrderOnfflineInfo {
    private String address;
    private int buyNum;
    private int classSum;
    private String courseName;
    private String courseTypeName;
    private long discountAmount;
    private int giveNum;
    private String gradeName;
    private int onePrice;
    private String orderCode;
    private int orderId;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    private int orderStatus;

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    private long payTime;
    private int signPrice;
    private String subjectName;
    private String teacherNick;
    private String teachingMethodName;
    private int teachingPeriod;
    private int totalPrice;
    private List<Long> startTimeArray;
    private String directTypeName;//直播类型名称
    private int upperFrame;
    private int discountPercent;//课程折扣值
    private int courseDiscountVersion;//课程折扣版本
    private int courseDiscountId;//	课程折扣id
    private int campusDiscountPercent;
    private int campusDiscountVersion;
    private int campusDiscountId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private int courseId;
    private List<OrderDiscount> orderDiscountList;
    private List<CampusDiscountGive> campusDiscountGive;//	校区满赠信息

    public long getBuyerTime() {
        return buyerTime;
    }

    public void setBuyerTime(long buyerTime) {
        this.buyerTime = buyerTime;
    }

    private long buyerTime;
    private String courseVideoName;//分类名称


    public List<OrderDiscount> getOrderDiscountList() {
        return orderDiscountList;
    }

    public void setOrderDiscountList(List<OrderDiscount> orderDiscountList) {
        this.orderDiscountList = orderDiscountList;
    }

    public List<CampusDiscountGive> getCampusDiscountGive() {
        return campusDiscountGive;
    }

    public void setCampusDiscountGive(List<CampusDiscountGive> campusDiscountGive) {
        this.campusDiscountGive = campusDiscountGive;
    }



    public String getCourseVideoName() {
        return courseVideoName;
    }

    public void setCourseVideoName(String courseVideoName) {
        this.courseVideoName = courseVideoName;
    }

    public String getDirectTypeName() {
        return directTypeName;
    }

    public void setDirectTypeName(String directTypeName) {
        this.directTypeName = directTypeName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getCourseDiscountVersion() {
        return courseDiscountVersion;
    }

    public void setCourseDiscountVersion(int courseDiscountVersion) {
        this.courseDiscountVersion = courseDiscountVersion;
    }

    public int getCourseDiscountId() {
        return courseDiscountId;
    }

    public void setCourseDiscountId(int courseDiscountId) {
        this.courseDiscountId = courseDiscountId;
    }

    public int getUpperFrame() {
        return upperFrame;
    }

    public void setUpperFrame(int upperFrame) {
        this.upperFrame = upperFrame;
    }

    public int getCampusDiscountPercent() {
        return campusDiscountPercent;
    }

    public void setCampusDiscountPercent(int campusDiscountPercent) {
        this.campusDiscountPercent = campusDiscountPercent;
    }

    public int getCampusDiscountVersion() {
        return campusDiscountVersion;
    }

    public void setCampusDiscountVersion(int campusDiscountVersion) {
        this.campusDiscountVersion = campusDiscountVersion;
    }

    public int getCampusDiscountId() {
        return campusDiscountId;
    }

    public void setCampusDiscountId(int campusDiscountId) {
        this.campusDiscountId = campusDiscountId;
    }

    public String getAddress() {
        return StringUtils.textIsEmpty(address) ? "暂无" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getClassSum() {
        return classSum;
    }

    public void setClassSum(int classSum) {
        this.classSum = classSum;
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

    public long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(int giveNum) {
        this.giveNum = giveNum;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(int onePrice) {
        this.onePrice = onePrice;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSignPrice() {
        return signPrice;
    }

    public void setSignPrice(int signPrice) {
        this.signPrice = signPrice;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherNick() {
        return teacherNick;
    }

    public void setTeacherNick(String teacherNick) {
        this.teacherNick = teacherNick;
    }

    public String getTeachingMethodName() {
        return teachingMethodName;
    }

    public void setTeachingMethodName(String teachingMethodName) {
        this.teachingMethodName = teachingMethodName;
    }

    public int getTeachingPeriod() {
        return teachingPeriod;
    }

    public void setTeachingPeriod(int teachingPeriod) {
        this.teachingPeriod = teachingPeriod;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Long> getStartTimeArray() {
        return startTimeArray == null ? new ArrayList<Long>() : startTimeArray;
    }

    public void setStartTimeArray(List<Long> startTimeArray) {
        this.startTimeArray = startTimeArray;
    }
}
