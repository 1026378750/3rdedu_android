package com.ldf.calendar.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ldf.calendar.utils.CalendarUtils;
import com.ldf.calendar.utils.DateState;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.mi.calendar.R;

/**
 * Created by liukui on 17/6/26.
 */

public class CustomDayView extends DayView {
    private RelativeLayout mCustomDay;
    private TextView mDate;
    private ImageView point;
    private View mark;
    private int normalColor;
    private int checkedColor;
    private int textColor;
    private int selectColor;
    private boolean shwPoint;
    private int markerResource;
    private int pointResource;
    private float textSize;
    private float parentHeight;


    /**
     * 构造器
     *
     * @param context 上下文
     */
    public CustomDayView(Context context) {
        super(context, R.layout.custom_day);
        initViews();
    }

    private void  initViews(){
        mCustomDay = (RelativeLayout) findViewById(R.id.custom_day);
        mDate = (TextView) findViewById(R.id.custom_day_date);
        point = (ImageView) findViewById(R.id.custom_day_point);
        mark = findViewById(R.id.custom_day_mark);
    }

    @Override
    public void refreshContent() {
        if(dayUtil==null){
            return;
        }
        if(dayUtil.getDate() == null){
            return;
        }
        int row = CalendarUtils.getInstance().getRowCell(getDateYear(),getDateMonth());
        cellHeight = (int)(parentHeight/row);
        mCustomDay.setLayoutParams(new LayoutParams(cellWidth,cellHeight));

        mDate.setText(getDateDay());
        mDate.setTextSize(textSize);
        mDate.setTextColor(textColor);
        mark.setVisibility(INVISIBLE);
        renderSelect();
        super.refreshContent();
    }

    private void renderSelect() {
        point.setVisibility(shwPoint && dayUtil.isTagMark(CalendarUtils.getInstance().loadMarkData())?VISIBLE:INVISIBLE);
        point.setBackgroundResource(pointResource);

        if (getState() == DateState.SELECT) {
            mark.setVisibility(VISIBLE);
            mark.setBackgroundResource(markerResource);
            mDate.setTextColor(checkedColor);
            return;
        } else if (getState() == DateState.NEXT_MONTH || getState() == DateState.PAST_MONTH) {
            mDate.setTextColor(normalColor);
            return;
        }

        if(dayUtil.getDate().toString().trim().equals(getCurrentDay())){
            mDate.setTextColor(selectColor);
            return;
        }

        //排除之前无效日期、排除未选中的星期
        if(CalendarUtils.getInstance().getDayDisable(dayUtil.getDate())||!CalendarUtils.getInstance().getWeekDisable(dayUtil.getDate().getWeek())){
            mDate.setTextColor(normalColor);
        }
    }

    @Override
    public IDayRenderer copy() {
        CustomDayView dayView = new CustomDayView(getContext());
        dayView.setMarkerResource(markerResource);
        dayView.setPointResource(pointResource);
        dayView.setNormalColor(normalColor);
        dayView.setCheckedColor(checkedColor);
        dayView.setTextColor(textColor);
        dayView.setShwPoint(shwPoint);
        dayView.setTextSize(textSize);
        dayView.setSelectColor(selectColor);
        dayView.setParentHeight(parentHeight);
        return dayView;
    }

    /****
     * 无效字体颜色
     * @param normalColor
     */
    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    /****
     * 字体大小
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    /****
     * 选中字体颜色
     * @param checkedColor
     */
    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
    }

    /****
     * 字体颜色
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /****
     * 是否展示点
     * @param shwPoint
     */
    public void setShwPoint(boolean shwPoint) {
        this.shwPoint = shwPoint;
    }

    public void setMarkerResource(int resource){
        markerResource = resource;
    }

    public void setPointResource(int resource){
        pointResource = resource;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public void setParentHeight(float parentHeight) {
        this.parentHeight = parentHeight;
    }
}
