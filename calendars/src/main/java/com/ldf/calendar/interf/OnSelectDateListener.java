package com.ldf.calendar.interf;

import com.ldf.calendar.utils.CalendarDate;

/**
 * Created by ldf on 17/6/2.
 */

public interface OnSelectDateListener {
    void onSelectDate(CalendarDate date,int row);

    void onSelectOtherMonth(int offset);//点击其它月份日期
}
