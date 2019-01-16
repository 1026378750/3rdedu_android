package com.shengzhe.disan.xuetangteacher.bean;

/**
 * 参数名	类型	示例/默认值	说明
 courseName	string		课程名称
 coursePrice	int		课程价格
 courseType	int		课程类型
 courseId	int		课程id
 * Created by acer on 2017/12/7.
 */

public class OneCourseArrayBean {
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
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

    private String courseName;
    private int courseType;
    private int coursePrice;
    private int courseId;


}


