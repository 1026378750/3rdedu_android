package com.ldf.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.utils.DateState;
import com.ldf.calendar.utils.DayUtil;

import java.util.Calendar;

/**
 * Created by ldf on 16/10/19.
 */

public abstract class DayView extends RelativeLayout implements IDayRenderer {
    public DayUtil dayUtil;
    protected Context context;
    protected int layoutResource;
    protected int cellHeight = 0;
    protected int cellWidth = 0;

    /**
     * 构造器 传入资源文件创建DayView
     *
     * @param layoutResource 资源文件
     * @param context        上下文
     */
    public DayView(Context context, int layoutResource) {
        super(context);
        this.context = context;
        this.layoutResource = layoutResource;
        cellWidth = getResources().getDisplayMetrics().widthPixels / 7;
        setupLayoutResource(layoutResource);
    }

    /**
     * 为自定义的DayView设置资源文件
     *
     * @param layoutResource 资源文件
     * @return CalendarDate 修改后的日期
     */
    private void setupLayoutResource(int layoutResource) {
        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflated.layout(0, 0, cellWidth, cellHeight);
    }

    @Override
    public void refreshContent() {
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        layout(0, 0, cellWidth, cellHeight);
    }

    @Override
    public void drawDay(Canvas canvas, DayUtil dayUtil) {
        this.dayUtil = dayUtil;
        refreshContent();
        int saveId = canvas.save();
        canvas.translate(getTranslateX(canvas, dayUtil), dayUtil.getPosRow() * cellHeight);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }

    private int getTranslateX(Canvas canvas, DayUtil day) {
        int dx;
        //获取画布区域宽度
        int canvasWidth = canvas.getWidth() / 7;
        //获取布局宽度
        int viewWidth = cellWidth;
        //计算横向边框间距
        int moveX = (canvasWidth - viewWidth) / 2;
        //计算偏移量
        dx = day.getPosCol() * canvasWidth + moveX;
        /*if (day.getDate().getMonth()==3&&day.getDate().getDay()==31)
            Log.d("calendar","==================================>位置：getTranslateX31："+ dx);
        if (day.getDate().getMonth()==4&&day.getDate().getDay()==1)
            Log.d("calendar","==================================>位置：getTranslateX1："+ dx);*/
        return dx;
    }

    public String getDateDay() {
        return String.valueOf(dayUtil.getDate().getDay());
    }

    public int getDateYear() {
        return dayUtil.getDate().getYear();
    }

    public int getDateMonth() {
        return dayUtil.getDate().getMonth();
    }

    public DateState getState() {
        return dayUtil.getDateState();
    }

    public String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

}