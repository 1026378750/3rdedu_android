package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by hy on 2017/12/9.
 */

public class TeacherLive {
    private String courseName;
    private int discountPrice;
    private int courseTotalPrice;
    private int courseId;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getCourseTotalPrice() {
        return courseTotalPrice;
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
}
