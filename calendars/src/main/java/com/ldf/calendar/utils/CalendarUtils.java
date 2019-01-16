/*
 * Copyright (c) 2016.
 * wb-lijinwei.a@alibaba-inc.com
 */

package com.ldf.calendar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import com.ldf.calendar.view.MonthPager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/****
 * 日历工具处理
 */
public final class CalendarUtils {
    private static CalendarUtils calendarUtils;
    private static List<String> markData = new ArrayList<>();
    private static List<String> weekList = new ArrayList<>();
    private static boolean isDisable = false;
    private static String week,defaultDate;
    private static String format12 = "yyyy-M-d",format24 = "yyyy-MM-dd";

    public static CalendarUtils getInstance() {
        if (calendarUtils==null) {
            calendarUtils = new CalendarUtils();
            markData.clear();
            weekList.clear();
            isDisable = false;
        }
        return calendarUtils;
    }

    /**
     * 得到某一个月的具体天数
     *(1)年份能被4整除，但不能被100整除；(2)能被400整除。
     * @param year  参数月所在年
     * @param month 参数月
     * @return int 参数月所包含的天数
     */
    public int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year --;
        }
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        // 闰年2月29天
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            monthDays[1] = 29;
        }
        return monthDays[month - 1];
    }

    /****
     * 计算有效行数
     * @param year
     * @param month
     * @return
     */
    public int getRowCell(int year, int month){
       //int countDay = getFirstDayWeekPosition(year,month,0)+getMonthDays(year,month);
       // return countDay%7==0?countDay/7:(countDay/7)+1;
        return CalendarConst.TOTAL_ROW;
    }

    public int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public int getWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 得到当前月第一天在其周的位置
     *
     * @param year  当前年
     * @param month 当前月
     * @param type  周排列方式 0代表周一作为本周的第一天， 2代表周日作为本周的第一天
     * @return int 本月第一天在其周的位置
     */
    public int getFirstDayWeekPosition(int year, int month, int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (type == 1) {
            return week_index;
        } else {
            week_index = cal.get(Calendar.DAY_OF_WEEK) + 5;
            if (week_index >= 7) {
                week_index -= 7;
            }
        }
        return week_index;
    }

    /**
     * 将yyyy-MM-dd类型的字符串转化为对应的Date对象
     *
     * @param year  当前年
     * @param month 当前月
     * @return Date  对应的Date对象
     */
    @SuppressLint("SimpleDateFormat")
    private Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-01";
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    /**
     * 得到标记日期数据，可以通过该数据得到标记日期的信息，开发者可自定义格式
     * 目前HashMap<String, String>的组成仅仅是为了DEMO效果
     *
     * @return HashMap<String, String> 标记日期数据
     */
    public List<String> loadMarkData() {
        return markData;
    }

    /**
     * 设置标记日期数据
     *
     * @param data 标记日期数据
     * @return void
     */
    public void setMarkData(List<String> data) {
        markData = data;
    }

    /****
     * 是否显示无效图层
     * @param disable
     */
    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public void setWeek(String mWeek,ArrayList<String> mWeekList) {
        week = mWeek;
        weekList = mWeekList;
    }

    /****
     * 获取当前日期小的无效日期
     * @param date
     */
    public boolean getDayDisable(CalendarDate date) {
        try {
            Calendar current = Calendar.getInstance();
            String time = current.get(Calendar.YEAR) + "-" + (current.get(Calendar.MONTH) + 1) + "-" + current.get(Calendar.DAY_OF_MONTH);
            if(new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime() > new SimpleDateFormat("yyyy-MM-dd").parse(date.getYear()+"-"+date.getMonth()+"-"+date.getDay()).getTime()){
                return  isDisable && !isMineSchedule;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  false;
    }

    private boolean isMineSchedule = false;
    public void setIsMineSchedule(boolean mIsMineSchedule){
        isMineSchedule = mIsMineSchedule;
    }

    /****
     * 获取星期对应的无效日期
     * @param mMeek
     * @return
     */
    public boolean getWeekDisable(int mMeek) {
        switch (week){
            case "周一":
                return Calendar.MONDAY == mMeek;
            case "周二":
                return Calendar.TUESDAY == mMeek;
            case "周三":
                return Calendar.WEDNESDAY == mMeek;
            case "周四":
                return Calendar.THURSDAY == mMeek;
            case "周五":
                return Calendar.FRIDAY == mMeek;
            case "周六":
                return Calendar.SATURDAY == mMeek;
            case "周日":
                return Calendar.SUNDAY == mMeek;
        }

        String weekStr = "";
        switch (mMeek){
            case Calendar.MONDAY:
                weekStr = "周一";
                break;
            case Calendar.TUESDAY:
                weekStr = "周二";
                break;
            case Calendar.WEDNESDAY:
                weekStr = "周三";
                break;
            case Calendar.THURSDAY:
                weekStr = "周四";
                break;
            case Calendar.FRIDAY:
                weekStr = "周五";
                break;
            case Calendar.SATURDAY:
                weekStr = "周六";
                break;
            case Calendar.SUNDAY:
                weekStr = "周日";
                break;
        }
        return weekList.contains(weekStr);
    }

    /*****
     * 设置默认时间
     * @param date yyyy-MM-dd
     */
    public void setSelectDate(String date){
        defaultDate = TextUtils.isEmpty(date)?new SimpleDateFormat(format24).format(new Date()):date;
    }

    /*****
     * 获取默认日期
     * @return
     */
    public CalendarDate getSelectDate(){
        CalendarDate date =null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat(format24).parse(TextUtils.isEmpty(defaultDate)?new SimpleDateFormat(format24).format(new Date()):defaultDate));
            date = new CalendarDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算偏移距离
     *
     * @param offset 偏移值
     * @param min    最小偏移值
     * @param max    最大偏移值
     * @return int offset
     */
    private int calcOffset(int offset, int min, int max) {
        if (offset > max) {
            return max;
        } else if (offset < min) {
            return min;
        } else {
            return offset;
        }
    }

    /**
     * 删除方法, 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param child     需要移动的View
     * @param dy        实际偏移量
     * @param minOffset 最小偏移量
     * @param maxOffset 最大偏移量
     * @return void
     */
    public int scroll(View child, int dy, int minOffset, int maxOffset) {
        final int initOffset = child.getTop();
        int offset = calcOffset(initOffset - dy, minOffset, maxOffset) - initOffset;
        child.offsetTopAndBottom(offset);
        return -offset;
    }

    /**
     * 得到TouchSlop
     *
     * @param context 上下文
     * @return int touchSlop的具体值
     */
    public int getTouchSlop(Context context) {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 得到种子日期所在周的周日
     *
     * @param seedDate 种子日期
     * @return CalendarDate 所在周周日
     */
    public CalendarDate getSunday(CalendarDate seedDate) {// TODO: 16/12/12 得到一个CustomDate对象
        Calendar c = Calendar.getInstance();
        String dateString = seedDate.toString();
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format12);
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        c.setTime(date);
        if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            c.add(Calendar.DAY_OF_MONTH, 7 - c.get(Calendar.DAY_OF_WEEK) + 1);
        }
        return new CalendarDate(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * 得到种子日期所在周的周六
     *
     * @param seedDate 种子日期
     * @return CalendarDate 所在周周六
     */
    public CalendarDate getSaturday(CalendarDate seedDate) {// TODO: 16/12/12 得到一个CustomDate对象
        Calendar c = Calendar.getInstance();
        String dateString = seedDate.toString();
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format12);
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 7 - c.get(Calendar.DAY_OF_WEEK));
        return new CalendarDate(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.DAY_OF_WEEK));
    }

    private int top;
    private boolean customScrollToBottom = false;

    /**
     * 判断上一次滑动改变周月日历是向下滑还是向上滑 向下滑表示切换为月日历模式 向上滑表示切换为周日历模式
     *
     * @return boolean 是否是在向下滑动
     */
    public boolean isScrollToBottom() {
        return customScrollToBottom;
    }

    /**
     * 设置上一次滑动改变周月日历是向下滑还是向上滑 向下滑表示切换为月日历模式 向上滑表示切换为周日历模式
     *
     * @return void
     */
    public void setScrollToBottom(boolean customScrollToBottom) {
        this.customScrollToBottom = customScrollToBottom;
    }

    private boolean isTop = true;
    /**
     * 是否已经到达顶部
     *
     * @return boolean
     */
    public boolean isToTop() {
        return isTop;
    }

    /**
     * 设置是否已经到达顶部
     *
     * @return void
     */
    public void setToTop(boolean isTop) {
        if (isTop&&this.isTop)
            return;
        this.isTop = isTop;
    }

    private boolean isBottom = false;
    /**
     * 是否已经到达底部
     *
     * @return boolean
     */
    public boolean isToBottom() {
        return isBottom;
    }

    /**
     * 设置是否已经到达底部
     *
     * @return void
     */
    public void setToBottom(boolean isBottom) {
        if (isBottom&&this.isBottom)
            return;
        this.isBottom = isTop;
    }

    private int isMoveY = 0;
    /**
     * 是否已经到达底部
     *
     * @return boolean
     */
    public int getMoveY() {
        return isMoveY;
    }

    /**
     * 设置是否已经到达底部
     *
     * @return void
     */
    public void setMoveY(int isMoveY) {
        this.isMoveY = isMoveY;
    }

    public void scrollTo(final CoordinatorLayout parent, final RecyclerView child, final int y, int duration) {
        final Scroller scroller = new Scroller(parent.getContext());
        scroller.startScroll(0, top, 0, y - top, duration);   //设置scroller的滚动偏移量
        ViewCompat.postOnAnimation(child, new Runnable() {
            @Override
            public void run() {
                //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。
                // 这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
                if (scroller.computeScrollOffset()) {
                    int delta = scroller.getCurrY() - child.getTop();
                    child.offsetTopAndBottom(delta);
                    saveTop(child.getTop());
                    parent.dispatchDependentViewsChanged(child);
                    ViewCompat.postOnAnimation(child, this);
                } else {
                    MonthPager monthPager = (MonthPager)parent.getChildAt(0);
                    if (monthPager.getTop() < 0) {
                        if (monthPager.getTop() + monthPager.getTopMovableDistance() >= 0) {
                            monthPager.offsetTopAndBottom(-monthPager.getTop() - monthPager.getTopMovableDistance());
                        } else {
                            monthPager.offsetTopAndBottom(-monthPager.getTop());
                        }
                        parent.dispatchDependentViewsChanged(child);
                    }
                }
            }
        });
    }

    public void saveTop(int y) {
        this.top = y;
    }

    public int loadTop() {
        return this.top;
    }

    /****
     * 清空所有数据
     */
    public void clearDatas(){
        calendarUtils = null;
        calendarUtils = new CalendarUtils();
    }

}
