package com.shengzhe.disan.xuetangparent.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/7.
 */

public class CourseItem {
    private String beginTime;
    private String endTime;
    private int classTime;

    private List<CourseImtemBean> courseImtem;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
    }

    public List<CourseImtemBean> getCourseImtem() {
        return courseImtem==null?new ArrayList<CourseImtemBean>():courseImtem;
    }

    public void setCourseImtem(List<CourseImtemBean> courseImtem) {
        this.courseImtem = courseImtem;
    }

    public static class CourseImtemBean {
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
