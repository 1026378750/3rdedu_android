package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.view.CalendarView;
import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.MyRecyclerView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ScheduleCalendarView extends BaseView implements MonthPager.OnPageListener{
    private String defaultDate = "";
    private CalendarDate selectDate;
    private String timeList="";
    private RadioButton selectBtn;
    private Calendar data;
    private String canTime = "1,2,3";
    private Map<String,Integer> statusMap = new HashMap<>();
    private Map<String,Integer> canTimeMap = new HashMap<>();
    private TeachingMethod teachingMethod;
    private Bundle bundle;
    private SimpleAdapter adapt;

    public ScheduleCalendarView(Context context) {
        super(context);
    }

    private IScheduleCalendarView iView;
    public void setIScheduleCalendarView(IScheduleCalendarView iView){
        this.iView = iView;
    }

    public void initDatas(Intent extras,final FragmentManager supportFragmentManager) {
        statusMap.clear();
        teachingMethod = extras.getParcelableExtra(StringUtils.teachingMethod);
        bundle = extras.getExtras();
        data = bundle.getParcelable(StringUtils.calendar);
        canTime = bundle.getString(StringUtils.canTime);

        defaultDate = TextUtils.isEmpty(data.data)? ClassScheduleUtil.getDateForWeek(data.week):data.getDate();
        selectDate = new CalendarDate(defaultDate);

        iView.getDateView().setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月");

        initMonthPager();
        if (!TextUtils.isEmpty(data.time))
            timeList = data.getDate()+" "+data.time.split("-")[0];
        iView.getTimeView().setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月"+selectDate.getDay()+"日"+" "+data.time);
        canTimeMap.putAll(ClassScheduleUtil.getCanNotTime(canTime));
        //把LayoutManager设置给RecyclerView
        iView.getContentView().setLayoutManager(UiUtils.getGridLayoutManager(6));
        adapt = new SimpleAdapter<String>(mContext, ContentUtil.selectDate(), R.layout.item_select_time) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final String data) {
                final RadioButton item = holder.getView(R.id.rb_select_item);
                item.setText(data);
                item.setChecked(false);
                item.setClickable(false);
                iView.getConfirmView().setChecked(false);
                iView.getConfirmView().setClickable(false);
                holder.setVisible(R.id.iv_teacher_busy,false);
                item.setBackgroundResource(R.drawable.search_item_bg_selector);
                item.setTextColor(UiUtils.getColorStateList(R.drawable.selector_textcolor_white666666));

                if(ClassScheduleUtil.isPastDue(selectDate,data)){
                    //不安排时间(不开课)
                    item.setBackgroundResource(R.drawable.btn_nomal4);
                    item.setTextColor(UiUtils.getColor(R.color.color_666666));
                    return;
                }

                if(timeList.equals(selectDate.toString()+" "+data)) {
                    selectBtn = item;
                    item.setChecked(true);
                    item.setClickable(true);
                }
                if(statusMap.get(data)!=null&&!statusMap.isEmpty()){
                    if(statusMap.get(data)==1){
                        //忙
                        holder.setVisible(R.id.iv_teacher_busy,statusMap.get(data)==1);
                        item.setBackgroundResource(R.drawable.search_item_bg_selector2);
                        item.setTextColor(UiUtils.getColorStateList(R.drawable.selector_textcolor_666666));
                        item.setClickable(true);
                    }else if(statusMap.get(data)==2){
                        //不安排时间
                        item.setChecked(false);
                        item.setClickable(false);
                        item.setBackgroundResource(R.drawable.btn_nomal4);
                        item.setTextColor(UiUtils.getColor(R.color.color_666666));
                    }
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if(v!=selectBtn){
                            if(statusMap.get(data)==null){
                                if(!ClassScheduleUtil.isValidTime(data,teachingMethod.classTime,canTimeMap) || Integer.parseInt(data.substring(0,2))+teachingMethod.classTime>23){
                                    ((RadioButton) v).setChecked(false);
                                    ConfirmDialog.newInstance("", "您选择的开始时间与课时时长不符，重新选择", "", "确定")
                                            .setMargin(60)
                                            .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                                            .setOutCancel(false)
                                            .show(supportFragmentManager);
                                    return;
                                }
                                if(selectBtn!=null)
                                    selectBtn.setChecked(false);
                                selectBtn = (RadioButton) v;
                                setSelectStatus(data);
                            }else if(statusMap.get(data)==1){
                                final ConfirmDialog dialog = ConfirmDialog.newInstance("", "老师该时段可能忙碌，您是否愿意接受和老师协调具体时间？", "重新再选", "接受");
                                dialog.setMargin(60)
                                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                                        .setOutCancel(false)
                                        .show(supportFragmentManager);
                                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

                                    @Override
                                    public void dialogStatus(int id) {
                                        switch (id){
                                            case R.id.tv_dialog_cancel:
                                                //确定
                                                item.setChecked(false);
                                                item.setClickable(false);
                                                break;
                                            case R.id.tv_dialog_ok:
                                                //确定
                                                if(selectBtn!=null)
                                                    selectBtn.setChecked(false);
                                                selectBtn = (RadioButton) v;
                                                setSelectStatus(data);
                                                break;
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        };
        iView.getContentView().setAdapter(adapt);
    }

    private void setSelectStatus(String data){
        iView.getTimeView().setText(selectDate.getYear() + "年" + (selectDate.getMonth() < 10 ? "0" + selectDate.getMonth() : selectDate.getMonth()) + "月" + (selectDate.getDay() < 10 ? "0" + selectDate.getDay() : selectDate.getDay()) + "日" + " " + data + "-" + ClassScheduleUtil.getTimeLag(data, String.valueOf(teachingMethod.classTime)));
        timeList = "";
        selectBtn.setChecked(true);
        iView.getConfirmView().setChecked(true);
        iView.getConfirmView().setClickable(true);
    }

    private void initMonthPager() {
        iView.getCalendarView().setOnPageListener(this);
        iView.getCalendarView().setDefaultData(defaultDate);
        iView.getCalendarView().setDisable(true);
        iView.getCalendarView().setWeekAble(data.week,null);
        iView.getCalendarView().notifyMarkData(null);
        iView.getCalendarView().setPagerAdapter();
        iView.getCalendarView().setCurrentPage(selectDate);
    }

    @Override
    public void onCurrentPage(ArrayList<CalendarView> calendarList, CalendarDate date) {
        iView.getDateView().setText(date.getYear() + "年"+date.getMonth() + "月");
    }

    @Override
    public void onCheckedDate(CalendarDate date) {
        selectDate = date;
        selectBtn = null;
        iView.getTimeView().setText(date.getYear() + "年"+(date.getMonth()<10?"0"+date.getMonth():date.getMonth()) + "月"+(date.getDay()<10?"0"+date.getDay():date.getDay())+"日");
        iView.getConfirmView().setChecked(false);
        iView.getConfirmView().setClickable(false);
    }

    public void clearDatas() {
        if (statusMap!=null)
            statusMap.clear();
        if (canTimeMap!=null)
            canTimeMap.clear();
    }

    public long getTimes() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(selectDate.getYear()+"-"+selectDate.getMonth()+"-"+selectDate.getDay()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setResultDatas(List<Integer> dataList) {
        statusMap.clear();
        statusMap.putAll(ClassScheduleUtil.getBusyTime(dataList));
        statusMap.putAll(canTimeMap);
        adapt.notifyDataSetChanged();
    }

    public interface IScheduleCalendarView{
        TextView getDateView();
        MonthPager getCalendarView();
        MyRecyclerView getContentView();
        TextView getTimeView();
        RadioButton getConfirmView();
    }

}
