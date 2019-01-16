package com.ldf.calendar.interf;

import android.graphics.Canvas;

import com.ldf.calendar.utils.DayUtil;

/**
 * Created by ldf on 17/6/26.
 */

public interface IDayRenderer {

    void refreshContent();

    void drawDay(Canvas canvas, DayUtil day);

    IDayRenderer copy();

}
