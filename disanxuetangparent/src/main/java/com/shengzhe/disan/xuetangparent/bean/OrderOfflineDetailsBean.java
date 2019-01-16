package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 线下一对订单详情
 * Created by acer on 2018/1/31.
 */

public class OrderOfflineDetailsBean implements Parcelable {

    /**
     * buyNum : 22
     * classSum : 22
     * classTime : 22
     * courseDiscountId : 16
     * courseId : 264
     * courseName : mei you hui
     * courseTypeName : 线下班课
     * discountAmount : 435600l
     * discountPercent : 1
     * giveNum : 0
     * gradeName : 小学
     * isJoin : 0
     * maxUser : 20
     * onePrice : 6666l
     * orderCode : 696365358289670
     * orderId : 707
     * orderStatus : 1
     * orderTime : 1523269636532
     * payTime : 0
     * photoUrl : http://10.8.1.133/static/images/f15488bf46cc47f384f153472340d8821523258775135.jpg
     * salesVolume : 3
     * subjectName : 数学
     * teacherId : 25
     * teacherNick : 张数学
     * teacherPhotoUrl : http://10.8.1.133/static/images/59ea0c8d9e074a96910256a5134fa6fe1520318963940.jpg
     * teachingPeriod : 3
     * totalPrice : 440000l
     * upperFrame : 1
     */

    public int buyNum;
    public int classSum;
    public int classTime;
    public int courseDiscountId;
    public int courseId;
    public String courseName;
    public String courseTypeName;
    public long discountAmount;
    public long discountPercent;
    public int giveNum;
    public String gradeName;
    public int isJoin;
    public int maxUser;
    public long onePrice;
    public String orderCode;
    public int orderId;
    public int orderStatus;
    public long orderTime;
    public long payTime;
    public String photoUrl;
    public int salesVolume;
    public String subjectName;
    public int teacherId;
    public String teacherNick;
    public String teacherPhotoUrl;
    public int teachingPeriod;
    public long totalPrice;
    public int upperFrame;
    public long buyerTime;

    protected OrderOfflineDetailsBean(Parcel in) {
        buyNum = in.readInt();
        classSum = in.readInt();
        classTime = in.readInt();
        courseDiscountId = in.readInt();
        courseId = in.readInt();
        courseName = in.readString();
        courseTypeName = in.readString();
        discountAmount = in.readLong();
        discountPercent = in.readLong();
        giveNum = in.readInt();
        gradeName = in.readString();
        isJoin = in.readInt();
        maxUser = in.readInt();
        onePrice = in.readLong();
        orderCode = in.readString();
        orderId = in.readInt();
        orderStatus = in.readInt();
        orderTime = in.readLong();
        payTime = in.readLong();
        photoUrl = in.readString();
        salesVolume = in.readInt();
        subjectName = in.readString();
        teacherId = in.readInt();
        teacherNick = in.readString();
        teacherPhotoUrl = in.readString();
        teachingPeriod = in.readInt();
        totalPrice = in.readLong();
        upperFrame = in.readInt();
        buyerTime=in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(buyNum);
        dest.writeInt(classSum);
        dest.writeInt(classTime);
        dest.writeInt(courseDiscountId);
        dest.writeInt(courseId);
        dest.writeString(courseName);
        dest.writeString(courseTypeName);
        dest.writeLong(discountAmount);
        dest.writeLong(discountPercent);
        dest.writeInt(giveNum);
        dest.writeString(gradeName);
        dest.writeInt(isJoin);
        dest.writeInt(maxUser);
        dest.writeLong(onePrice);
        dest.writeString(orderCode);
        dest.writeInt(orderId);
        dest.writeInt(orderStatus);
        dest.writeLong(orderTime);
        dest.writeLong(payTime);
        dest.writeString(photoUrl);
        dest.writeInt(salesVolume);
        dest.writeString(subjectName);
        dest.writeInt(teacherId);
        dest.writeString(teacherNick);
        dest.writeString(teacherPhotoUrl);
        dest.writeInt(teachingPeriod);
        dest.writeLong(totalPrice);
        dest.writeInt(upperFrame);
        dest.writeLong(buyerTime);
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

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
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

    public long getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(long discountPercent) {
        this.discountPercent = discountPercent;
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

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(int maxUser) {
        this.maxUser = maxUser;
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
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

    public String getTeacherNick() {
        return teacherNick;
    }

    public void setTeacherNick(String teacherNick) {
        this.teacherNick = teacherNick;
    }

    public String getTeacherPhotoUrl() {
        return teacherPhotoUrl;
    }

    public void setTeacherPhotoUrl(String teacherPhotoUrl) {
        this.teacherPhotoUrl = teacherPhotoUrl;
    }

    public int getTeachingPeriod() {
        return teachingPeriod;
    }

    public void setTeachingPeriod(int teachingPeriod) {
        this.teachingPeriod = teachingPeriod;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUpperFrame() {
        return upperFrame;
    }

    public void setUpperFrame(int upperFrame) {
        this.upperFrame = upperFrame;
    }
}
