package com.ldf.calendar.utils;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarDate implements Serializable {
    private static final long serialVersionUID = 1L;
    public int year;
    public int month;  //1~12
    public int day;
    public int week=0;

    /*****
     *
     * @param date yyyy-MM-dd
     */
    public CalendarDate(String date) {
        String[] dateArr =date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]);
        int day = Integer.parseInt(dateArr[2]);
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CalendarDate(int year, int month, int day,int week) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
    }

    public CalendarDate() {
        this.year = CalendarUtils.getInstance().getYear();
        this.month = CalendarUtils.getInstance().getMonth();
        this.day = CalendarUtils.getInstance().getDay();
        this.week = CalendarUtils.getInstance().getWeek();
    }

    /**
     * 通过修改当前Date对象的天数返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyDay(int day) {
        int lastMonthDays = CalendarUtils.getInstance().getMonthDays(this.year, this.month - 1);
        int currentMonthDays = CalendarUtils.getInstance().getMonthDays(this.year, this.month);
        Calendar cal = Calendar.getInstance();
        CalendarDate modifyDate = null;
        try {
        if (day > currentMonthDays) {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(this.year+"-"+this.month+"-"+this.day));
            modifyDate = new CalendarDate(this.year, this.month, this.day,cal.get(Calendar.DAY_OF_WEEK));
            Log.e("ldf", "移动天数过大");
        } else if (day > 0) {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(this.year+"-"+this.month+"-"+day));
            modifyDate = new CalendarDate(this.year, this.month, day,cal.get(Calendar.DAY_OF_WEEK));
        } else if (day > 0 - lastMonthDays) {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(this.year+"-"+(this.month - 1)+"-"+(lastMonthDays + day)));
            modifyDate = new CalendarDate(this.year, this.month - 1, lastMonthDays + day,cal.get(Calendar.DAY_OF_WEEK));
        } else {
            cal.set(this.year, this.month, this.day);
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(this.year+"-"+this.month+"-"+this.day));
            modifyDate = new CalendarDate(this.year, this.month, this.day,cal.get(Calendar.DAY_OF_WEEK));
            Log.e("ldf", "移动天数过大");
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modifyDate;
    }

    /**
     * 通过修改当前Date对象的所在周返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyWeek(int offset) {
        CalendarDate result = new CalendarDate();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.DAY_OF_WEEK, week);
        c.add(Calendar.DATE, offset * 7);
        result.setYear(c.get(Calendar.YEAR));
        result.setMonth(c.get(Calendar.MONTH) + 1);
        result.setDay(c.get(Calendar.DATE));
        result.setWeek(c.get(Calendar.DAY_OF_WEEK));
        return result;
    }

    /**
     * 通过修改当前Date对象的所在月返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyMonth(int offset) {
        CalendarDate result = new CalendarDate();
        int addToMonth = this.month + offset;
        if (offset > 0) {
            if (addToMonth > 12) {
                result.setYear(this.year + (addToMonth - 1) / 12);
                result.setMonth(addToMonth % 12 == 0 ? 12 : addToMonth % 12);
            } else {
                result.setYear(this.year);
                result.setMonth(addToMonth);
            }
        } else {
            if (addToMonth == 0) {
                result.setYear(this.year - 1);
                result.setMonth(12);
            } else if (addToMonth < 0) {
                result.setYear(this.year + addToMonth / 12 - 1);
                int month = 12 - Math.abs(addToMonth) % 12;
                result.setMonth(month == 0 ? 12 : month);
            } else {
                result.setYear(this.year);
                result.setMonth(addToMonth == 0 ? 12 : addToMonth);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return year + "-" + (month<10?"0"+month:month) + "-" + (day<10?"0"+day:day);
    }

    public int getIntWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public boolean equals(CalendarDate date) {
        if (date == null) {
            return false;
        }
        if (this.getYear() == date.getYear()
                && this.getMonth() == date.getMonth()
                && this.getDay() == date.getDay()) {
            return true;
        }
        return false;
    }

    public CalendarDate cloneSelf() {
        return new CalendarDate(year, month, day,this.week);
    }

}