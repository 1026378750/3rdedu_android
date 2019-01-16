package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.CourseDate;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.activity.ScheduleCalendarActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/26.
 */

public class SelectSchooltimeView extends BaseView implements CompoundButton.OnCheckedChangeListener{
    private ArrayList<Calendar> calList = new ArrayList<>();
    private Map<Integer, Calendar> indexMap = new HashMap<>();
    private Set<String> modiftSet = new HashSet<>();
    private int selectNum = 0;
    private List<String> strList = new ArrayList<>();
    private Map<String,String> canTimeMap = new HashMap<>();
    private int motifyIndex = 0;
    private SimpleAdapter adapt;
    private ISelectSchooltimeView iView;

    private FragmentManager supportFragmentManager;

    public SelectSchooltimeView(Context mContext, FragmentManager supportFragmentManager) {
        super(mContext);
        this.supportFragmentManager = supportFragmentManager;
    }

    public void setISelectSchooltimeView(ISelectSchooltimeView iView){
        this.iView = iView;
    }

    public void initDatas(final int courseId,final Bundle bundle, int selectNum) {
        strList.clear();
        modiftSet.clear();
        canTimeMap.clear();
        this.selectNum = selectNum;
        calList.clear();
        indexMap.clear();
        strList.add("1");
        strList.add("2");
        strList.add("3");
        strList.add("4");
        strList.add("5");
        strList.add("6");
        strList.add("7");
        iView.getMondayView().setOnCheckedChangeListener(this);
        iView.getWednesdayView().setOnCheckedChangeListener(this);
        iView.getThursdayView().setOnCheckedChangeListener(this);
        iView.getTuesdayView().setOnCheckedChangeListener(this);
        iView.getFridayView().setOnCheckedChangeListener(this);
        iView.getSaturdayView().setOnCheckedChangeListener(this);
        iView.getSundayView().setOnCheckedChangeListener(this);

        iView.getContentView().setLayoutManager(new LinearLayoutManager(mContext));

        adapt = new SimpleAdapter<Calendar>(mContext, calList, R.layout.item_calendar) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Calendar data) {
                CommonCrosswiseBar textBar = holder.getView(R.id.item_calender);
                textBar.setLeftText("每" + data.week);
                textBar.setRightText(data.getStrTime());

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(canTimeMap.isEmpty()){
                            ToastUtil.showShort("请稍等。。。");
                            return;
                        }
                        if(calList==null||calList.isEmpty()){
                            ToastUtil.showShort("请选择星期");
                            return;
                        }
                        motifyIndex = calList.indexOf(data);
                        Intent intent = new Intent(mContext,ScheduleCalendarActivity.class);
                        bundle.putParcelable(StringUtils.calendar,data);
                        bundle.putString(StringUtils.canTime,canTimeMap.get(data.week));
                        intent.putExtras(bundle);
                        intent.putExtra(StringUtils.COURSE_ID,courseId);
                        mContext.startActivity(intent);
                    }
                });
            }
        };
        iView.getContentView().setAdapter(adapt);

        iView.getConfirmView().setClickable(false);
    }

    public void setResultDatas(List<CourseDate> dataList) {
        if(dataList==null||dataList.isEmpty()){
            setNormal();
            return;
        }
        for(CourseDate date : dataList){
            if(TextUtils.isEmpty(date.times)){
                continue;
            }
            strList.remove(String.valueOf(date.week));
            canTimeMap.put(ClassScheduleUtil.numToWeek2(date.week),date.times);
        }
        setNormal();
    }

    public void setNormal(){
        for(String normal : strList){
            switch (normal){

                case "1":
                    iView.getMondayView().setChecked(false);
                    iView.getMondayView().setHint("normal");
                    iView.getMondayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getMondayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;

                case "2":
                    iView.getTuesdayView().setChecked(false);
                    iView.getTuesdayView().setHint("normal");
                    iView.getTuesdayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getTuesdayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;

                case "3":
                    iView.getWednesdayView().setChecked(false);
                    iView.getWednesdayView().setHint("normal");
                    iView.getWednesdayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getWednesdayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;

                case "4":
                    iView.getThursdayView().setChecked(false);
                    iView.getThursdayView().setHint("normal");
                    iView.getThursdayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getThursdayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;

                case "5":
                    iView.getFridayView().setChecked(false);
                    iView.getFridayView().setHint("normal");
                    iView.getFridayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getFridayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;

                case "6":
                    iView.getSaturdayView().setChecked(false);
                    iView.getSaturdayView().setHint("normal");
                    iView.getSaturdayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getSaturdayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;

                case "7":
                    iView.getSundayView().setChecked(false);
                    iView.getSundayView().setHint("normal");
                    iView.getSundayView().setTextColor(UiUtils.getColor(R.color.color_666666));
                    iView.getSundayView().setBackgroundResource(R.drawable.btn_nomal);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getHint()!=null&&buttonView.getHint().toString().equals("normal")){
            final ConfirmDialog dialog = ConfirmDialog.newInstance("", "老师该时段不授课，您可选择其他时间段。", "", "我知道了");
            dialog.setMargin(60)
                    .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                    .setOutCancel(false)
                    .show(supportFragmentManager);
            return;
        }
        if(isChecked&&calList.size()>= selectNum){
            buttonView.setChecked(false);
            return;
        }
        int vierwId = buttonView.getId();
        Calendar calendar = null;
        String week="";
        switch (vierwId) {
            case R.id.cb_schooltime_monday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.MONDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 0;
                    calendar.week = week;
                }
                break;

            case R.id.cb_schooltime_tuesday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.TUESDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 1;
                    calendar.week = week;
                }
                break;

            case R.id.cb_schooltime_wednesday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.WEDNESDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 2;
                    calendar.week = week;
                }
                break;

            case R.id.cb_schooltime_thursday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.THURSDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 3;
                    calendar.week = week;
                }
                break;

            case R.id.cb_schooltime_friday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.FRIDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 4;
                    calendar.week = week;
                }
                break;

            case R.id.cb_schooltime_saturday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.SATURDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 5;
                    calendar.week = week;
                }
                break;

            case R.id.cb_schooltime_sunday:
                week = ClassScheduleUtil.numToWeek(java.util.Calendar.SUNDAY);
                if (isChecked) {
                    calendar = new Calendar();
                    calendar.id = 6;
                    calendar.week = week;
                }
                break;
        }

        if (isChecked) {
            calendar.canTime = canTimeMap.get(calendar.week);
            calList.add(calendar);
            indexMap.put(vierwId, calendar);
        } else {
            calList.remove(indexMap.get(vierwId));
            indexMap.remove(vierwId);
            modiftSet.remove(week);
        }
        if (calList.isEmpty()) {
            iView.getMumberView().setVisibility(View.GONE);
            iView.getContentView().setVisibility(View.GONE);
            iView.getMessageView().setVisibility(View.INVISIBLE);
            iView.getMumberView().setText("次数/周");
            iView.getConfirmView().setChecked(false);
            iView.getConfirmView().setClickable(false);
        } else {
            iView.getMumberView().setVisibility(View.VISIBLE);
            iView.getMessageView().setVisibility(View.VISIBLE);
            iView.getContentView().setVisibility(View.VISIBLE);
            iView.getMumberView().setText("一周" + calList.size() + "次");
            if(calList.size()==modiftSet.size()){
                iView.getConfirmView().setChecked(true);
                iView.getConfirmView().setClickable(true);
            }else{
                iView.getConfirmView().setChecked(false);
                iView.getConfirmView().setClickable(false);
            }
        }
        Calendar[] arr = ClassScheduleUtil.bubbleSort(calList.toArray(new Calendar[calList.size()]));
        calList.clear();
        Collections.addAll(calList, arr);
        adapt.notifyDataSetChanged();
    }

    public void selectSchooltime(String[] data) {
        calList.get(motifyIndex).data = data[0];
        calList.get(motifyIndex).time = data[1];
        adapt.notifyItemChanged(motifyIndex);
        modiftSet.add(calList.get(motifyIndex).week);
        if(calList.size()==modiftSet.size()){
            iView.getConfirmView().setChecked(true);
            iView.getConfirmView().setClickable(true);
        }else{
            iView.getConfirmView().setChecked(false);
            iView.getConfirmView().setClickable(false);
        }
    }

    public void clearDatas() {
        iView.getMondayView().setChecked(false);
        iView.getTuesdayView().setChecked(false);
        iView.getThursdayView().setChecked(false);
        iView.getWednesdayView().setChecked(false);
        iView.getFridayView().setChecked(false);
        iView.getSaturdayView().setChecked(false);
        iView.getSundayView().setChecked(false);
        iView.getConfirmView().setChecked(false);
        iView.getConfirmView().setClickable(false);
        calList.clear();
        indexMap.clear();
        modiftSet.clear();
        adapt.notifyDataSetChanged();
    }

    public ArrayList<Calendar> getCalList() {
        return calList;
    }

    public interface ISelectSchooltimeView{
        CheckBox getMondayView();
        CheckBox getTuesdayView();
        CheckBox getWednesdayView();
        CheckBox getThursdayView();
        CheckBox getFridayView();
        CheckBox getSaturdayView();
        CheckBox getSundayView();
        RecyclerView getContentView();
        TextView getMumberView();
        TextView getMessageView();
        RadioButton getConfirmView();
    }

}
