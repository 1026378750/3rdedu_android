package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.ClassCourseBean;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ClassCreateTwoView extends BaseView implements CompoundButton.OnCheckedChangeListener{
    private  IClassCreateTwoView twoView;
    private ArrayList<Calendar> calList = new ArrayList<>();
    private Map<Integer, Calendar> indexMap = new HashMap<>();
    private Set<String> modiftSet = new HashSet<>();
    private ClassCourseBean onliveCourseBean;
    private SimpleAdapter adapt;
    private int motifyIndex = 0;

    public ClassCreateTwoView(Context mContext) {
        super(mContext);
    }

    public void setIClassCreateTwoView(IClassCreateTwoView twoView){
        this.twoView = twoView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked&&calList.size()>= onliveCourseBean.courseTimes){
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
            calList.add(calendar);
            indexMap.put(vierwId, calendar);
        } else {
            calList.remove(indexMap.get(vierwId));
            indexMap.remove(vierwId);
            modiftSet.remove(week);
        }

        twoView.getConfirmView().setClickable(false);
        twoView.getConfirmView().setChecked(false);
        if(!calList.isEmpty()&&calList.size()==modiftSet.size()){
            twoView.getConfirmView().setClickable(true);
            twoView.getConfirmView().setChecked(true);
        }
        Calendar[] arr = ClassScheduleUtil.bubbleSort(calList.toArray(new Calendar[calList.size()]));
        calList.clear();
        Collections.addAll(calList, arr);
        adapt.notifyDataSetChanged();
    }

    public void initDatas(ClassCourseBean onliveCourseBean) {
        modiftSet.clear();
        this.onliveCourseBean = onliveCourseBean;
        twoView.getMondayView().setOnCheckedChangeListener(this);
        twoView.getWednesdayView().setOnCheckedChangeListener(this);
        twoView.getThursdayView().setOnCheckedChangeListener(this);
        twoView.getTuesdayView().setOnCheckedChangeListener(this);
        twoView.getFridayView().setOnCheckedChangeListener(this);
        twoView.getSaturdayView().setOnCheckedChangeListener(this);
        twoView.getSundayView().setOnCheckedChangeListener(this);
    }

    public void setDatas() {
        twoView.getConfirmView().setClickable(false);
        twoView.getMondayView().setChecked(false);
        twoView.getTuesdayView().setChecked(false);
        twoView.getWednesdayView().setChecked(false);
        twoView.getThursdayView().setChecked(false);
        twoView.getFridayView().setChecked(false);
        twoView.getSaturdayView().setChecked(false);
        twoView.getSundayView().setChecked(false);
    }

    public void setConfirmStatus() {
        if(calList.size()==modiftSet.size()){
            twoView.getConfirmView().setChecked(true);
            twoView.getConfirmView().setClickable(true);
        }else{
            twoView.getConfirmView().setChecked(false);
            twoView.getConfirmView().setClickable(false);
        }
    }

    public void initAdapter(final FragmentManager manager) {
        adapt = new SimpleAdapter<Calendar>(mContext, calList, R.layout.item_calendar) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Calendar data) {
                holder.setText(R.id.tv_calender_week,data.week)
                        .setText(R.id.tv_calender_starttime,data.getStrTime());

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(calList==null||calList.isEmpty()){
                            ConfirmDialog.newInstance("", "请选择星期", "", "确定")
                                    .setMargin(60)
                                    .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                                    .setOutCancel(false)
                                    .show(manager);
                            return;
                        }
                        motifyIndex = calList.indexOf(data);
                        listener.viewClick(v,motifyIndex);
                    }
                });
            }
        };
        twoView.getContentView().setLayoutManager(new LinearLayoutManager(mContext));
        twoView.getContentView().setAdapter(adapt);
    }

    public ArrayList<Calendar> getCalList(){
        return calList;
    }

    public void notifyDatas(String[] data) {
        calList.get(motifyIndex).data = data[0];
        calList.get(motifyIndex).time = data[1];
        adapt.notifyItemChanged(motifyIndex);
        modiftSet.add(calList.get(motifyIndex).week);
    }

    public void clearDatas() {
        modiftSet.clear();
        calList.clear();
        adapt.notifyDataSetChanged();
        indexMap.clear();
        motifyIndex = 0;
    }

    public interface IClassCreateTwoView{
        CheckBox getMondayView();
        CheckBox getTuesdayView();
        CheckBox getWednesdayView();
        CheckBox getThursdayView();
        CheckBox getFridayView();
        CheckBox getSaturdayView();
        CheckBox getSundayView();
        RecyclerView getContentView();
        CheckBox getConfirmView();
    }
}
