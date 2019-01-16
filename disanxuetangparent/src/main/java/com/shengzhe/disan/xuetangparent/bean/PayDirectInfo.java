package com.shengzhe.disan.xuetangparent.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/11.
 */

public class PayDirectInfo implements Serializable {
   // 年级名称
    private String gradeName;
    //折扣值（100表示无折扣）
    private int courseDiscount;
    //折扣价
    private int discountPrice;
    //直播类型名称
    private String directTypeName;
    //优惠了多少钱
    private int favorablePrice;
    //课次数
    private int classTime;
    //课时
    private int duration;
    //课程名称
    private String courseName;
    //课程版本
    private int courseVersion;
    //老师昵称
    private String teacherNickName;
    //课程单价
    private int price;
    //课程总价
    private int coursePrice;
    //课程id
    private int courseId;
    //科目名称
    private String subjectName;
    private int courseDiscountId;
    private int courseDiscountVersion;
    //课次时间列表
    private List<CourseItemBean> courseItem;

    public int getCourseDiscountId() {
        return courseDiscountId;
    }

    public void setCourseDiscountId(int courseDiscountId) {
        this.courseDiscountId = courseDiscountId;
    }

    public int getCourseDiscountVersion() {
        return courseDiscountVersion;
    }

    public void setCourseDiscountVersion(int courseDiscountVersion) {
        this.courseDiscountVersion = courseDiscountVersion;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    private String courseTypeName;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getCourseDiscount() {
        return courseDiscount;
    }

    public void setCourseDiscount(int courseDiscount) {
        this.courseDiscount = courseDiscount;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDirectTypeName() {
        return directTypeName;
    }

    public void setDirectTypeName(String directTypeName) {
        this.directTypeName = directTypeName;
    }

    public int getFavorablePrice() {
        return favorablePrice;
    }

    public void setFavorablePrice(int favorablePrice) {
        this.favorablePrice = favorablePrice;
    }

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public String getTeacherNickName() {
        return teacherNickName;
    }

    public void setTeacherNickName(String teacherNickName) {
        this.teacherNickName = teacherNickName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
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

    public List<CourseItemBean> getCourseItem() {
        return courseItem==null?new ArrayList<CourseItemBean>():courseItem;
    }

    public void setCourseItem(List<CourseItemBean> courseItem) {
        this.courseItem = courseItem;
    }

}