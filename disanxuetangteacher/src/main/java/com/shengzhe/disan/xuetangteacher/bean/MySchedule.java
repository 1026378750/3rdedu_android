package com.shengzhe.disan.xuetangteacher.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/20.
 */

public class MySchedule {
    private long timeDay;
    private List<CourseBean> courseList;

    public long getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(long timeDay) {
        this.timeDay = timeDay;
    }

    public List<CourseBean> getCourseList() {
        return courseList==null?new ArrayList<CourseBean>():courseList;
    }

    public void setCourseList(List<CourseBean> courseList) {
        this.courseList = courseList;
    }


}
