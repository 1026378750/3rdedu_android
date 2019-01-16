package com.shengzhe.disan.xuetangparent.bean;

/**
 * 线下班课课表
 * Created by acer on 2018/4/8.
 */

public class CourseSquadScheduleBean {


    /**
     * courseStatus : 2
     * startTime : 1522555200000
     * endTime : 1522562400000
     * title :
     */

    private int courseStatus;//	课程状态 1：直播中 2：未开始 3：已结束
    private long startTime;
    private long endTime;
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
