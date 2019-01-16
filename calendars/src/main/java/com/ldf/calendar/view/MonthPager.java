package com.ldf.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.ldf.calendar.adapter.CalendarViewAdapter;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.utils.CalendarConst;
import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.utils.CalendarUtils;
import com.ldf.mi.calendar.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*****
 * 月份页工具
 */
public class MonthPager extends ViewPager {
    private int mmp_background;
    private int mmp_normal_textColor;
    private int checked_textColor;
    private int mmp_textColor;
    private int mmp_selectColor;
    private boolean mmp_isShwPoint;
    private float mmp_width;
    private float mmp_height;
    private float mmp_textsize;
    private int mmp_firstDay;
    private int mmp_parentType;

    private int mmp_type;
    private int cellHeight;

    public static int CURRENT_DAY_INDEX = 1000;
    private int currentPosition = CURRENT_DAY_INDEX;
    private int rowIndex = -1;
    private OnPageListener pageListener;
    private boolean scrollable = true;
    private int pageScrollState = ViewPager.SCROLL_STATE_IDLE;
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;

    public MonthPager(Context context) {
        this(context, null);
    }

    public MonthPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**获取属性值*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineMonthPager);
        mmp_background = typedArray.getColor(R.styleable.MineMonthPager_mmp_backGround, Color.WHITE);
        mmp_normal_textColor = typedArray.getColor(R.styleable.MineMonthPager_mmp_normal_textColor, Color.WHITE);
        checked_textColor = typedArray.getColor(R.styleable.MineMonthPager_mmp_checked_textColor, Color.WHITE);
        mmp_textColor = typedArray.getColor(R.styleable.MineMonthPager_mmp_textColor, Color.WHITE);
        mmp_selectColor = typedArray.getColor(R.styleable.MineMonthPager_mmp_selectColor, Color.WHITE);
        mmp_isShwPoint = typedArray.getBoolean(R.styleable.MineMonthPager_mmp_isShwPoint, false);
        mmp_textsize = typedArray.getDimension(R.styleable.MineMonthPager_mmp_textSize, 12);
        mmp_firstDay = typedArray.getInt(R.styleable.MineMonthPager_mmp_firstDay, 0);
        mmp_type = typedArray.getInt(R.styleable.MineMonthPager_mmp_type, 0);
        mmp_parentType = typedArray.getInt(R.styleable.MineMonthPager_mmp_parentType, 0);

        typedArray.recycle();
        setParameter();
        initDatas();
    }

    private void setParameter() {
        removeAllViews();
        CalendarUtils.getInstance().clearDatas();
        setBackgroundColor(mmp_background);
    }

    private void initDatas(){
        mmp_height = getResources().getDimension(R.dimen.common_calendar_height);
        mmp_width = getResources().getDisplayMetrics().widthPixels;
        cellHeight = (int)(mmp_height/ CalendarConst.TOTAL_ROW);
        initListener();
        initCalendarView();
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     *
     * @return void
     */
    private void initCalendarView() {
        CustomDayView dayView = new CustomDayView(getContext());
        dayView.setMarkerResource(mmp_type == 0 ? R.drawable.selected_calendar_parent : R.drawable.selected_calendar_teacher);
        dayView.setPointResource(mmp_type == 0 ? R.drawable.selected_calendar_parent : R.drawable.selected_calendar_teacher);
        dayView.setNormalColor(mmp_normal_textColor);
        dayView.setCheckedColor(checked_textColor);
        dayView.setTextColor(mmp_textColor);
        dayView.setShwPoint(mmp_isShwPoint);
        dayView.setTextSize(mmp_textsize);
        dayView.setSelectColor(mmp_selectColor);
        dayView.setParentHeight(mmp_height);
        calendarAdapter = new CalendarViewAdapter(
                getContext(),
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                dayView
                ,mmp_height
        );
    }

    /**
     * 设置监听
     */
    private void initListener() {
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ArrayList<CalendarView> currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof CalendarView) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    if (pageListener == null || date == null)
                        return;
                    pageListener.onCurrentPage(currentCalendars,date);
                    rowIndex = -1;
                    //cellHeight = (int)(mmp_height/CalendarUtils.getInstance().getRowCell(date.getYear(),date.getMonth()));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                pageScrollState = state;
            }
        });

        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date,int row) {
                if (pageListener == null || date == null)
                    return;
                pageListener.onCheckedDate(date);
                rowIndex = row;
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                setCurrentItem(currentPosition + offset);
                calendarAdapter.notifyDataChanged();
            }
        };
    }

    public void setPagerAdapter() {
        setAdapter(calendarAdapter);
        setCurrentItem(CURRENT_DAY_INDEX);
        setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
    }

    /****
     * 设置选中年份
     * @param selectDate
     */
    public void setCurrentPage(CalendarDate selectDate) {
        Calendar times = Calendar.getInstance();
        setCurrentItem(CURRENT_DAY_INDEX + (selectDate.getYear() - times.get(java.util.Calendar.YEAR)) * 12 + selectDate.getMonth() - times.get(java.util.Calendar.MONTH) - 1);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (!scrollable)
            return false;
        else
            return super.onTouchEvent(me);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent me) {
        if (!scrollable)
            return false;
        else
            return super.onInterceptTouchEvent(me);
    }

    public int getPageScrollState() {
        return pageScrollState;
    }

    public void setOnPageListener(OnPageListener listener) {
        this.pageListener = listener;
    }

    public void setDefaultData(String defaultData) {
        calendarAdapter.setDefaultData(defaultData);
    }

    public void notifyMarkData(List<String> markData) {
        calendarAdapter.notifyMarkData(markData);
    }

    public void setDisable(boolean disable) {
        calendarAdapter.setDisable(disable);
    }

    public void setWeekAble(String week, ArrayList<String> weekList) {
        calendarAdapter.setWeekAble(week,weekList);
    }

    public void setIsMineSchedule(boolean bool) {
        calendarAdapter.setIsMineSchedule(bool);
    }

    public interface OnPageListener {
        void onCurrentPage(ArrayList<CalendarView> calendarList,CalendarDate date);

        void onCheckedDate(CalendarDate date);
    }

    public int getTopMovableDistance() {
        if (rowIndex==-1)
            rowIndex = calendarAdapter.getPagers().get(currentPosition % 3).getSelectedRowIndex();
        return getCellHeight() * rowIndex;
    }

    public void notifyMarkDataAsyn(List<String> timeMarkList) {
        calendarAdapter.notifyMarkDataAsyn(timeMarkList);
    }

    /****
     * 子元素高度
     * @return
     */
    public int getCellHeight() {
        return cellHeight;
    }

    /*****
     * 整体个高度
     * @return
     */
    public int getViewHeight() {
        return (int) mmp_height;
    }

    public int getRowIndex() {
        if (rowIndex==-1)
            rowIndex = calendarAdapter.getPagers().get(currentPosition % 3).getSelectedRowIndex();
        return rowIndex;
    }
}
