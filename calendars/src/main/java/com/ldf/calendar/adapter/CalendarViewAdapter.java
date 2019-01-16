package com.ldf.calendar.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.interf.OnAdapterSelectListener;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.utils.CalendarUtils;
import com.ldf.calendar.view.MonthPager;
import com.ldf.calendar.view.CalendarView;
import java.util.ArrayList;
import java.util.List;

/*****
 * 日历适配器
 */
public class CalendarViewAdapter extends PagerAdapter {
    //周排列方式 1：代表周日显示为本周的第一天
    //0:代表周一显示为本周的第一天
    public static int weekArrayType = 0;
    private ArrayList<CalendarView> calendars = new ArrayList<>();
    private int currentPosition;
    private CalendarAttr.CalendayType calendarType = CalendarAttr.CalendayType.MONTH;
    private int rowCount = 0;
    private CalendarDate seedDate;
    private IDayRenderer dayView;
    private float mmpHeight = 0;

    public CalendarViewAdapter(Context context, OnSelectDateListener onSelectDateListener, CalendarAttr.CalendayType calendarType, IDayRenderer dayView,float mmp_height) {
        super();
        this.calendarType = calendarType;
        this.dayView = dayView;
        this.mmpHeight = mmp_height;
        setDefaultData("");
        init(context,onSelectDateListener);
        setCustomDayRenderer(dayView);
        setDisable(false);
        setIsMineSchedule(false);
        setWeekAble("",null);
    }

    private void init(Context context, OnSelectDateListener onSelectDateListener) {
        //初始化的种子日期为今天
        seedDate = new CalendarDate().modifyDay(1);
        if (seedDate==null)
            return;
        for (int i = 0; i < 3; i++) {
            CalendarView calendar = new CalendarView(context, onSelectDateListener,mmpHeight);
            calendar.setOnAdapterSelectListener(new OnAdapterSelectListener() {
                @Override
                public void cancelSelectState() {
                    cancelOtherSelectState();
                }

                @Override
                public void updateSelectState() {
                    invalidateCurrentCalendar();
                }
            });
            calendars.add(calendar);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.currentPosition = position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position < 2) {
            return null;
        }
        if (seedDate==null)
            return null;
        CalendarView calendar = calendars.get(position % calendars.size());
        if (calendarType == CalendarAttr.CalendayType.MONTH) {
            CalendarDate current = seedDate.modifyMonth(position - MonthPager.CURRENT_DAY_INDEX);
            current.setDay(1);//每月的种子日期都是1号
            calendar.showDate(current);
        } else {
            CalendarDate current = seedDate.modifyWeek(position - MonthPager.CURRENT_DAY_INDEX);
            if (weekArrayType == 1) {
                calendar.showDate(CalendarUtils.getInstance().getSaturday(current));
            } else {
                calendar.showDate(CalendarUtils.getInstance().getSunday(current));
            }//每周的种子日期为这一周的最后一天
            calendar.updateWeek(rowCount);
        }
        if (container.getChildCount() == calendars.size()) {
            container.removeView(calendars.get(position % 3));
        }
        if (container.getChildCount() < calendars.size()) {
            container.addView(calendar, 0);
        } else {
            container.addView(calendar, position % 3);
        }
        return calendar;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container);
    }

    public ArrayList<CalendarView> getPagers() {
        return calendars;
    }

    public void cancelOtherSelectState() {
        for (int i = 0; i < calendars.size(); i++) {
            CalendarView calendar = calendars.get(i);
            calendar.cancelSelectState();
        }
    }

    public void invalidateCurrentCalendar() {
        for (int i = 0; i < calendars.size(); i++) {
            CalendarView calendar = calendars.get(i);
            calendar.update();
            if (calendar.getCalendarType() == CalendarAttr.CalendayType.WEEK) {
                calendar.updateWeek(rowCount);
            }
        }
    }

    /****
     * 唤醒mark标识
     * @param markData
     */
    private boolean markTag = false;
    public void notifyMarkDataAsyn(List<String> markData) {
        CalendarUtils.getInstance().setMarkData(markData==null?new ArrayList<String>():markData);
        if(!markTag){
            notifyDataChanged();
            markTag = true;
        }else{
            dayView.refreshContent();
        }
    }

    public void notifyMarkData(List<String> markData) {
        CalendarUtils.getInstance().setMarkData(markData==null?new ArrayList<String>():markData);
    }

    /****
     * 设置默认时间
     * @param defaultData yyyy-MM-dd
     */
    public void setDefaultData(@Nullable String defaultData) {
        CalendarUtils.getInstance().setSelectDate(defaultData);
    }

    public void setDisable(boolean isDisable) {
        CalendarUtils.getInstance().setDisable(isDisable);
    }

    public void setIsMineSchedule(boolean isMineSchedule) {
        CalendarUtils.getInstance().setIsMineSchedule(isMineSchedule);
    }

    /****
     * 设置选择
     * @param week
     */
    public void setWeekAble(@Nullable String week ,@Nullable ArrayList<String> weekList) {
        week = TextUtils.isEmpty(week)?"":week;
        weekList = weekList==null ? getDefaultWeek():weekList;
        CalendarUtils.getInstance().setWeek(week,weekList);
    }

    private ArrayList<String> getDefaultWeek(){
        ArrayList<String> weekList = new ArrayList<>();
        weekList.add("周一");
        weekList.add("周二");
        weekList.add("周三");
        weekList.add("周四");
        weekList.add("周五");
        weekList.add("周六");
        weekList.add("周日");
        return weekList;
    }

    public void notifyDataChanged() {
        seedDate = CalendarUtils.getInstance().getSelectDate();
        if (seedDate==null)
            return;
        if (calendarType == CalendarAttr.CalendayType.WEEK) {
            MonthPager.CURRENT_DAY_INDEX = currentPosition;
            CalendarView v1 = calendars.get(currentPosition % 3);
            v1.showDate(seedDate);
            v1.updateWeek(rowCount);

            if (currentPosition>0){
                CalendarView v2 = calendars.get((currentPosition - 1) % 3);
                CalendarDate last = seedDate.modifyWeek(-1);
                if (weekArrayType == 1) {
                    v2.showDate(CalendarUtils.getInstance().getSaturday(last));
                } else {
                    v2.showDate(CalendarUtils.getInstance().getSunday(last));
                }//每周的种子日期为这一周的最后一天
                v2.updateWeek(rowCount);
            }

            CalendarView v3 = calendars.get((currentPosition + 1) % 3);
            CalendarDate next = seedDate.modifyWeek(1);
            if (weekArrayType == 1) {
                v3.showDate(CalendarUtils.getInstance().getSaturday(next));
            } else {
                v3.showDate(CalendarUtils.getInstance().getSunday(next));
            }//每周的种子日期为这一周的最后一天
            v3.updateWeek(rowCount);
        } else {
            //MonthPager.CURRENT_DAY_INDEX = currentPosition;

            CalendarView v1 = calendars.get(currentPosition % 3);//0
            v1.showDate(seedDate);
            if (currentPosition>0) {
                CalendarView v2 = calendars.get((currentPosition - 1) % 3);//2
                CalendarDate last = seedDate.modifyMonth(-1);
                last.setDay(1);
                v2.showDate(last);
            }
            CalendarView v3 = calendars.get((currentPosition + 1) % 3);//1
            CalendarDate next = seedDate.modifyMonth(1);
            next.setDay(1);
            v3.showDate(next);
        }
    }

    /**
     * 为每一个Calendar实例设置renderer对象
     *
     * @return void
     */
    public void setCustomDayRenderer(IDayRenderer dayRenderer) {
        CalendarView c0 = calendars.get(0);
        c0.setDayRenderer(dayRenderer);

        CalendarView c1 = calendars.get(1);
        c1.setDayRenderer(dayRenderer.copy());

        CalendarView c2 = calendars.get(2);
        c2.setDayRenderer(dayRenderer.copy());
    }

    public void switchToMonth() {
        if (calendars != null && calendars.size() > 0 && calendarType != CalendarAttr.CalendayType.MONTH) {
            calendarType = CalendarAttr.CalendayType.MONTH;
            MonthPager.CURRENT_DAY_INDEX = currentPosition;
            CalendarView v = calendars.get(currentPosition % 3);//0
            seedDate = v.getSeedDate();
            if (seedDate==null)
                return;
            CalendarView v1 = calendars.get(currentPosition % 3);//0
            v1.switchCalendarType(CalendarAttr.CalendayType.MONTH);
            v1.showDate(seedDate);
            if (currentPosition>0) {
                CalendarView v2 = calendars.get((currentPosition - 1) % 3);//2
                v2.switchCalendarType(CalendarAttr.CalendayType.MONTH);
                CalendarDate last = seedDate.modifyMonth(-1);
                last.setDay(1);
                v2.showDate(last);
            }

            CalendarView v3 = calendars.get((currentPosition + 1) % 3);//1
            v3.switchCalendarType(CalendarAttr.CalendayType.MONTH);
            CalendarDate next = seedDate.modifyMonth(1);
            next.setDay(1);
            v3.showDate(next);
        }
    }

    public void switchToWeek(int rowIndex) {
        rowCount = rowIndex;
        if (calendars != null && calendars.size() > 0 && calendarType != CalendarAttr.CalendayType.WEEK) {
            calendarType = CalendarAttr.CalendayType.WEEK;
            MonthPager.CURRENT_DAY_INDEX = currentPosition;
            CalendarView v = calendars.get(currentPosition % 3);
            seedDate = v.getSeedDate();
            if (seedDate==null)
                return;

            rowCount = v.getSelectedRowIndex();

            CalendarView v1 = calendars.get(currentPosition % 3);
            v1.switchCalendarType(CalendarAttr.CalendayType.WEEK);
            v1.showDate(seedDate);
            v1.updateWeek(rowIndex);
            if (currentPosition>0) {
                CalendarView v2 = calendars.get((currentPosition - 1) % 3);
                v2.switchCalendarType(CalendarAttr.CalendayType.WEEK);
                CalendarDate last = seedDate.modifyWeek(-1);
                if (weekArrayType == 1) {
                    v2.showDate(CalendarUtils.getInstance().getSaturday(last));
                } else {
                    v2.showDate(CalendarUtils.getInstance().getSunday(last));
                }//每周的种子日期为这一周的最后一天
                v2.updateWeek(rowIndex);
            }

            CalendarView v3 = calendars.get((currentPosition + 1) % 3);
            v3.switchCalendarType(CalendarAttr.CalendayType.WEEK);
            CalendarDate next = seedDate.modifyWeek(1);
            if (weekArrayType == 1) {
                v3.showDate(CalendarUtils.getInstance().getSaturday(next));
            } else {
                v3.showDate(CalendarUtils.getInstance().getSunday(next));
            }//每周的种子日期为这一周的最后一天
            v3.updateWeek(rowIndex);
        }
    }

}