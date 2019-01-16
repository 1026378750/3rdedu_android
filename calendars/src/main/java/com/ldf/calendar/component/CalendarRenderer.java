package com.ldf.calendar.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.ldf.calendar.adapter.CalendarViewAdapter;
import com.ldf.calendar.utils.CalendarConst;
import com.ldf.calendar.utils.CalendarUtils;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.utils.DateState;
import com.ldf.calendar.view.CalendarView;
import com.ldf.calendar.utils.DayUtil;
import com.ldf.calendar.utils.WeekUtil;

/**
 * Created by ldf on 17/6/26.
 */

public class CalendarRenderer {
    private WeekUtil[] weeks;    // 行数组，每个元素代表一行
    private CalendarView calendar;
    private CalendarAttr attr;
    private IDayRenderer dayRenderer;
    private Context context;
    private OnSelectDateListener onSelectDateListener;    // 单元格点击回调事件
    private CalendarDate seedDate; //种子日期
    private CalendarDate selectedDate; //被选中的日期
    private int selectedRowIndex = 0;

    public CalendarRenderer(CalendarView calendar, CalendarAttr attr, Context context) {
        this.calendar = calendar;
        this.attr = attr;
        this.context = context;
        weeks =  new WeekUtil[CalendarConst.TOTAL_ROW];
    }

    /**
     * 使用dayRenderer绘制每一天
     *
     * @return void
     */
    public void draw(Canvas canvas) {
        for (int row = 0; row < weeks.length; row++) {
            if (weeks[row] != null) {
                for (int col = 0; col < CalendarConst.TOTAL_COL; col++) {
                    if (weeks[row].days[col] != null) {
                        dayRenderer.drawDay(canvas, weeks[row].days[col]);
                    }
                }
            }
        }
    }

    /**
     * 点击某一天时刷新这一天的状态
     *
     * @return void
     */
    public void onClickDate(int col, int row) {
        if (col >= CalendarConst.TOTAL_COL || row >= CalendarConst.TOTAL_ROW)
            return;
        if (weeks[row] != null) {
            //去除无效点击事件处理
            if(CalendarUtils.getInstance().getDayDisable(weeks[row].days[col].getDate())||!CalendarUtils.getInstance().getWeekDisable(weeks[row].days[col].getDate().getWeek())){
                return;
            }
            if (attr.getCalendarType() == CalendarAttr.CalendayType.MONTH) {
                if (weeks[row].days[col].getDateState() == DateState.CURRENT_MONTH) {
                    weeks[row].days[col].setDateState(DateState.SELECT);
                    selectedDate = weeks[row].days[col].getDate();
                    CalendarUtils.getInstance().setSelectDate(selectedDate.toString());
                    onSelectDateListener.onSelectDate(selectedDate,row);
                    seedDate = selectedDate;
                } else if (weeks[row].days[col].getDateState() == DateState.PAST_MONTH) {
                    selectedDate = weeks[row].days[col].getDate();
                    CalendarUtils.getInstance().setSelectDate(selectedDate.toString());
                    onSelectDateListener.onSelectOtherMonth(-1);
                    onSelectDateListener.onSelectDate(selectedDate,row);
                } else if (weeks[row].days[col].getDateState() == DateState.NEXT_MONTH) {
                    weeks[row-1].days[col].setDateState(DateState.SELECT);
                    selectedDate = weeks[row-1].days[col].getDate();
                    CalendarUtils.getInstance().setSelectDate(selectedDate.toString());
                    onSelectDateListener.onSelectDate(selectedDate,row);
                    seedDate = selectedDate;
                }
            } else {
                weeks[row].days[col].setDateState(DateState.SELECT);
                if(CalendarUtils.getInstance().getDayDisable(weeks[row].days[col].getDate())||!CalendarUtils.getInstance().getWeekDisable(weeks[row].days[col].getDate().getWeek())){
                    return;
                }
                selectedDate = weeks[row].days[col].getDate();
                CalendarUtils.getInstance().setSelectDate(selectedDate.toString());
                onSelectDateListener.onSelectDate(selectedDate,row);
                seedDate = selectedDate;
            }
        }
    }

    /**
     * 刷新指定行的周数据
     *
     * @param rowIndex  参数月所在年
     * @return void
     */
    public void updateWeek(int rowIndex) {
        CalendarDate currentWeekLastDay;
        if (CalendarViewAdapter.weekArrayType == 1) {
            currentWeekLastDay = CalendarUtils.getInstance().getSaturday(seedDate);
        } else {
            currentWeekLastDay = CalendarUtils.getInstance().getSunday(seedDate);
        }
        int day = currentWeekLastDay.day;
        for (int i = CalendarConst.TOTAL_COL - 1; i >= 0; i--) {
            CalendarDate date = currentWeekLastDay.modifyDay(day);
            if (weeks[rowIndex] == null) {
                weeks[rowIndex] = new WeekUtil(rowIndex);
            }
            if (weeks[rowIndex].days[i] != null) {
                if (date.equals(CalendarUtils.getInstance().getSelectDate())) {
                    weeks[rowIndex].days[i].setDateState(DateState.SELECT);
                    weeks[rowIndex].days[i].setDate(date);
                } else {
                    weeks[rowIndex].days[i].setDateState(DateState.CURRENT_MONTH);
                    weeks[rowIndex].days[i].setDate(date);
                }
            } else {
                if (date.equals(CalendarUtils.getInstance().getSelectDate())) {
                    weeks[rowIndex].days[i] = new DayUtil(DateState.SELECT, date, rowIndex, i);
                } else {
                    weeks[rowIndex].days[i] = new DayUtil(DateState.CURRENT_MONTH, date, rowIndex, i);
                }
            }
            day--;
        }
    }

    /**
     * 填充月数据
     *
     * @return void
     */
    private void instantiateMonth() {
        int lastMonthDays = CalendarUtils.getInstance().getMonthDays(seedDate.year, seedDate.month - 1);    // 上个月的天数
        int currentMonthDays = CalendarUtils.getInstance().getMonthDays(seedDate.year, seedDate.month);    // 当前月的天数
        int firstDayPosition = CalendarUtils.getInstance().getFirstDayWeekPosition(seedDate.year, seedDate.month, CalendarViewAdapter.weekArrayType);
        int day = 0;
        for (int row = 0; row < CalendarConst.TOTAL_ROW; row++) {
            day = fillWeek(lastMonthDays, currentMonthDays, firstDayPosition, day, row);
        }
    }

    /**
     * 填充月中周数据
     *
     * @return void
     */
    private int fillWeek(int lastMonthDays, int currentMonthDays, int firstDayWeek, int day, int row) {
        for (int col = 0; col < CalendarConst.TOTAL_COL; col++) {
            int position = col + row * CalendarConst.TOTAL_COL;// 单元格位置
            if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                day++;
                fillCurrentMonthDate(day, row, col);
            } else if (position < firstDayWeek) {
                instantiateLastMonth(lastMonthDays, firstDayWeek, row, col, position);
            } else if (position >= firstDayWeek + currentMonthDays) {
                //填充下一个月份日期
                instantiateNextMonth(currentMonthDays, firstDayWeek, row, col, position);
            }
        }
        return day;
    }

    /****
     * 填充当前月份日期
     * @param day
     * @param row
     * @param col
     */
    private void fillCurrentMonthDate(int day, int row, int col) {
        CalendarDate date = seedDate.modifyDay(day);
        if (weeks[row] == null) {
            weeks[row] = new WeekUtil(row);
        }
        if (weeks[row].days[col] != null) {
            if (date.equals(CalendarUtils.getInstance().getSelectDate())) {
                weeks[row].days[col].setDate(date);
                weeks[row].days[col].setDateState(DateState.SELECT);
            } else {
                weeks[row].days[col].setDate(date);
                weeks[row].days[col].setDateState(DateState.CURRENT_MONTH);
            }
        } else {
            if (date.equals(CalendarUtils.getInstance().getSelectDate())) {
                weeks[row].days[col] = new DayUtil(DateState.SELECT, date, row, col);
            } else {
                weeks[row].days[col] = new DayUtil(DateState.CURRENT_MONTH, date, row, col);
            }
        }
        if (date.equals(seedDate)) {
            selectedRowIndex = row;
        }
    }

    /****
     * 获取下一个月数据
     * @param currentMonthDays
     * @param firstDayWeek
     * @param row
     * @param col
     * @param position
     */
    private void instantiateNextMonth(int currentMonthDays, int firstDayWeek, int row, int col, int position) {
        CalendarDate date = new CalendarDate(seedDate.year, seedDate.month + 1, position - firstDayWeek - currentMonthDays + 1,seedDate.week);
        if (weeks[row] == null) {
            weeks[row] = new WeekUtil(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setDateState(DateState.NEXT_MONTH);
        } else {
            weeks[row].days[col] = new DayUtil(DateState.NEXT_MONTH, date, row, col);
        }
    }

    /****
     * 初始化上一个月数据
     * @param lastMonthDays
     * @param firstDayWeek
     * @param row
     * @param col
     * @param position
     */
    private void instantiateLastMonth(int lastMonthDays, int firstDayWeek, int row, int col, int position) {
        CalendarDate date = new CalendarDate(seedDate.year, seedDate.month - 1, lastMonthDays - (firstDayWeek - position - 1),seedDate.week);
        if (weeks[row] == null) {
            weeks[row] = new WeekUtil(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setDateState(DateState.PAST_MONTH);
        } else {
            weeks[row].days[col] = new DayUtil(DateState.PAST_MONTH, date, row, col);
        }
    }

    /**
     * 根据种子日期孵化出本日历牌的数据
     *
     * @return void
     */
    public void showDate(CalendarDate seedDate) {
        if (seedDate != null) {
            this.seedDate = seedDate;
        } else {
            this.seedDate = new CalendarDate();
        }
        update();
    }

    public void update() {
        instantiateMonth();
        calendar.invalidate();
    }

    public CalendarDate getSeedDate() {
        return this.seedDate;
    }

    /****
     * 取消选中状态
     */
    public void cancelSelectState() {
        for (int i = 0; i < CalendarConst.TOTAL_ROW; i++) {
            if (weeks[i] != null) {
                for (int j = 0; j < CalendarConst.TOTAL_COL; j++) {
                    if (weeks[i].days[j].getDateState() == DateState.SELECT) {
                        weeks[i].days[j].setDateState(DateState.CURRENT_MONTH);
                        resetSelectedRowIndex();
                        break;
                    }
                }
            }
        }
    }

    public void resetSelectedRowIndex() {
        selectedRowIndex = 0;
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public CalendarView getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarView calendar) {
        this.calendar = calendar;
    }

    public CalendarAttr getAttr() {
        return attr;
    }

    public void setAttr(CalendarAttr attr) {
        this.attr = attr;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setOnSelectDateListener(OnSelectDateListener onSelectDateListener) {
        this.onSelectDateListener = onSelectDateListener;
    }

    public void setDayRenderer(IDayRenderer dayRenderer) {
        this.dayRenderer = dayRenderer;
    }

}
