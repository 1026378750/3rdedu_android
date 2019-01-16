package com.main.disanxuelib.bean;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CanTimer {
    //具体哪一天
    private long dayTime;
    //一天中哪几个时间忙
    private int[] houreTimes;

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public int[] getHoureTimes() {
        return houreTimes;
    }

    public void setHoureTimes(int[] houreTimes) {
        this.houreTimes = houreTimes;
    }

}
