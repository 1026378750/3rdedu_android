package com.ldf.calendar.utils;

/**
 * Created by ldf on 17/6/27.
 */

public class WeekUtil {
    public int row;
    public DayUtil[] days = new DayUtil[CalendarConst.TOTAL_COL];

    public WeekUtil(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public DayUtil[] getDayUtil() {
        return days;
    }

    public void setDayUtil(DayUtil[] days) {
        this.days = days;
    }
}
