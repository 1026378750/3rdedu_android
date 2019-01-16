package com.shengzhe.disan.xuetangparent.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/18.
 */

public class MyCourseInfo {
    private long firstTime;
    private String courseName;
    private String courseTypeName;
    private String teacherNickName;
    private int buyNum;
    private int giveNum;
    private int orderNum;
    private String assistantPhone;
    /*****一对一*****/
    private String teachingMethodName;
    private String photoUrl;
    private String address;

    public String getAssistantPhone() {
        return assistantPhone;
    }

    public void setAssistantPhone(String assistantPhone) {
        this.assistantPhone = assistantPhone;
    }

    public String getTeachingMethodName() {
        return teachingMethodName;
    }

    public void setTeachingMethodName(String teachingMethodName) {
        this.teachingMethodName = teachingMethodName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDirectTypeName() {
        return directTypeName;
    }

    public void setDirectTypeName(String directTypeName) {
        this.directTypeName = directTypeName;
    }

    public List<CourseImtemBean> getCourseItem() {
        return courseItem==null?new ArrayList<CourseImtemBean>():courseItem;
    }

    public void setCourseItem(List<CourseImtemBean> courseItem) {
        this.courseItem = courseItem;
    }

    private List<CourseImtemBean> courseItem;

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    private int classNum;
    /*****在线直播课*****/
    private String directTypeName;
    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
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

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getTeacherNickName() {
        return teacherNickName;
    }

    public void setTeacherNickName(String teacherNickName) {
        this.teacherNickName = teacherNickName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(int giveNum) {
        this.giveNum = giveNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
    public static class CourseImtemBean {
        /**
         * courseStatus : 3
         * startTime : 1512358200000
         * endTime : 1512365400000
         */

        private int courseStatus;
        private long startTime;
        private long endTime;

        public int getCourseStatus() {
            return courseStatus;
        }

        public void setCourseStatus(int courseStatus) {
            this.courseStatus = courseStatus;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }
}
