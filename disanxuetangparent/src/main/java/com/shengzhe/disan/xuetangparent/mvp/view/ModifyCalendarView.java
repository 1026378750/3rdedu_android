package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.view.CalendarView;
import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Schedule;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.MyRecyclerView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ModifyCalendarView extends BasePresenter implements  MonthPager.OnPageListener{


    private CalendarDate selectDate;
    private ArrayList<CalendarView> currentCalendars = new ArrayList<>();
    private RadioButton selectBtn;
    private Set<String> selectSet = new HashSet<>();
    private List<String> current;
    private Schedule newSchedule = null;
    private Map<String, Integer> statusMap = new HashMap<>();
    private Map<String, Integer> canTimeMap = new HashMap<>();
    private ArrayList<String> weekList = new ArrayList<>();
    private Map<String, List<String>> timeMap = new HashMap<>();
    private String canTime = "1,2,3";
    private SimpleAdapter adapt;
    private Bundle bundle;

    public ModifyCalendarView(Context context,Bundle bundle) {
        super(context);
        this.bundle = bundle;
    }

    private IModifyCalendarView iView;
    public void setIModifyCalendarView(IModifyCalendarView iView){
        this.iView = iView;
    }

    @Override
    public void onCurrentPage(ArrayList<CalendarView> calendarList, CalendarDate date) {
        currentCalendars = calendarList;
        iView.getDateView().setText(date.getYear() + "年" + date.getMonth() + "月");
    }

    @Override
    public void onCheckedDate(CalendarDate date) {
        selectDate = date;
        iView.getTimeView().setText(date.getYear() + "年" + date.getMonth() + "月" + date.getDay() + "日");
    }

    public void initDatas(final FragmentManager supportFragmentManager) {
        selectSet.clear();
        canTimeMap.clear();
        weekList.clear();
        timeMap.clear();

        Schedule schedule = bundle.getParcelable(StringUtils.schedule);
        weekList.addAll(bundle.getStringArrayList(StringUtils.WeekList));
        current = ClassScheduleUtil.getTimeQuantum(schedule.date, schedule.time);
        canTime = bundle.getString(StringUtils.canTime);
        newSchedule = schedule;
        selectDate = new CalendarDate(newSchedule.date);

        iView.getDateView().setText(selectDate.getYear() + "年" + selectDate.getMonth() + "月");
        iView.getTimeView().setText(selectDate.getYear() + "年" + selectDate.getMonth() + "月" + selectDate.getDay() + "日");
        initMonthPager();

        //把LayoutManager设置给RecyclerView
        iView.getContentView().setLayoutManager(UiUtils.getGridLayoutManager(6));
        canTimeMap.putAll(ClassScheduleUtil.getCanNotTime(canTime));

        adapt = new SimpleAdapter<String>(mContext, ContentUtil.selectDate(), R.layout.item_select_time) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final String data) {
                final RadioButton item = holder.getView(R.id.rb_select_item);
                item.setText(data);
                final String date = selectDate.toString() + " " + data;
                item.setBackgroundResource(R.drawable.search_item_bg_selector);
                item.setTextColor(UiUtils.getColorStateList(R.drawable.selector_textcolor_white666666));
                holder.setVisible(R.id.iv_teacher_busy, false);
                item.setChecked(true);
                item.setEnabled(true);
                if (ClassScheduleUtil.isPastDue(selectDate, data)) {
                    //不安排时间
                    item.setBackgroundResource(R.drawable.btn_nomal4);
                    item.setTextColor(UiUtils.getColor(R.color.color_666666));
                    return;
                }

                if (selectSet.contains(date) || (statusMap.get(data) != null && statusMap.get(data) == 2)) {
                    //其他天选中的 不可选择
                    item.setChecked(false);
                    item.setEnabled(false);
                    item.setBackgroundResource(R.drawable.btn_nomal4);
                }

                if (statusMap.get(data) != null && statusMap.get(data) == 1) {
                    ImageView busy = holder.getView(R.id.iv_teacher_busy);
                    busy.setVisibility(View.VISIBLE);
                    item.setBackgroundResource(R.drawable.search_item_bg_selector2);
                    item.setTextColor(UiUtils.getColorStateList(R.drawable.selector_textcolor_666666));
                    if (selectSet.contains(date)) {
                        //其他时期已选中
                        busy.setImageResource(R.mipmap.icn_teacher_busynomal);
                    } else {
                        busy.setImageResource(R.mipmap.icn_teacher_busy);
                    }
                }

                if (current.get(0).equals(date)) {
                    selectBtn = item;
                    selectBtn.setChecked(true);
                } else {
                    item.setChecked(false);
                }

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (v != selectBtn) {
                            if (ClassScheduleUtil.isValidTime(data, bundle.getInt(StringUtils.duration, 0), timeMap.get(selectDate.toString()))) {
                                ((RadioButton) v).setChecked(false);
                                ConfirmDialog.newInstance("", "您选择的开始时间与课时时长不符，重新再选", "", "确定")
                                        .setMargin(60)
                                        .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                                        .setOutCancel(false)
                                        .show(supportFragmentManager);
                                return;
                            }
                            if (statusMap.get(data) != null && statusMap.get(data) == 1) {
                                final ConfirmDialog dialog = ConfirmDialog.newInstance("", "老师该时段可能忙碌，您是否愿意接受和老师协调具体时间？", "重新再选", "接受");
                                dialog.setMargin(60)
                                        .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                                        .setOutCancel(false)
                                        .show(supportFragmentManager);
                                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                                    @Override
                                    public void dialogStatus(int id) {
                                        switch (id) {
                                            case R.id.tv_dialog_cancel:
                                                //确定
                                                item.setChecked(false);
                                                break;
                                            case R.id.tv_dialog_ok:
                                                //确定
                                                if (selectBtn != null)
                                                    selectBtn.setChecked(false);
                                                selectBtn = (RadioButton) v;
                                                setSelectStatus(data);
                                                break;
                                        }
                                    }
                                });
                                return;
                            }
                            if (selectBtn != null)
                                selectBtn.setChecked(false);
                            selectBtn = (RadioButton) v;
                            setSelectStatus(data);
                        }
                    }
                });
            }
        };

        iView.getContentView().setAdapter(adapt);
        iView.getConfirmView().setChecked(true);
        iView.getConfirmView().setClickable(true);
    }

    private void initMonthPager() {
        List<String> markData = new ArrayList<>();
        ArrayList<Schedule> scheduleList = bundle.getParcelableArrayList(StringUtils.scheduleList);
        for (Schedule item : scheduleList) {
            markData.add(item.date);
            List<String> child = ClassScheduleUtil.getTimeQuantum(item.date, item.time);
            if (child == null || child.isEmpty()) {
                continue;
            }
            String one = child.get(0);
            String two = child.get(child.size() - 1) == null ? child.get(0) : child.get(child.size() - 1);
            if (one == null || two == null || one.equals(current.get(0)) || two.equals(current.get(child.size() - 1))) {
                //排除当前选中的日期
                continue;
            }
            List<String> timeList = new ArrayList<>();
            for (String childItem : child) {
                selectSet.add(childItem);
                timeList.add(childItem.split(" ")[1]);
            }
            timeMap.put(item.date, timeList);
        }
        iView.getCalendarView().setOnPageListener(this);
        iView.getCalendarView().setDefaultData(selectDate.toString());
        iView.getCalendarView().notifyMarkData(markData);
        iView.getCalendarView().setDisable(true);
        iView.getCalendarView().setWeekAble("", weekList);
        iView.getCalendarView().setPagerAdapter();
        iView.getCalendarView().setCurrentPage(selectDate);
        iView.getTimeView().setText(selectDate.getYear() + "年" + selectDate.getMonth() + "月" + selectDate.getDay() + "日" + " " + newSchedule.time);
    }

    private void setSelectStatus(String data) {
        selectBtn.setChecked(true);
        newSchedule.time = data + "-" + ClassScheduleUtil.getTimeLag(data, String.valueOf(bundle.getInt(StringUtils.duration, 1)));
        iView.getTimeView().setText(selectDate.getYear() + "年" + selectDate.getMonth() + "月" + selectDate.getDay() + "日" + " " + newSchedule.time);
        newSchedule.date = selectDate.getYear() + "-" + (selectDate.getMonth() < 10 ? "0" + selectDate.getMonth() : selectDate.getMonth()) + "-" + (selectDate.getDay() < 10 ? "0" + selectDate.getDay() : selectDate.getDay());
        newSchedule.week = ClassScheduleUtil.numToWeek(selectDate.getIntWeek());
    }

    public Schedule getSchedule(){
        return newSchedule;
    }

    public void clearDatas(){
        if (canTimeMap != null)
            canTimeMap.clear();
        if (current != null)
            current.clear();
        if (currentCalendars != null)
            currentCalendars.clear();
        if (statusMap != null)
            statusMap.clear();
        if (selectSet != null)
            selectSet.clear();
        if (weekList != null)
            weekList.clear();
        if (timeMap != null)
            timeMap.clear();
    }

    public long getTimes() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(selectDate.getYear() + "-" + selectDate.getMonth() + "-" + selectDate.getDay()).getTime();
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

    public interface IModifyCalendarView {
        TextView getDateView();
        MonthPager getCalendarView();
        MyRecyclerView getContentView();
        TextView getTimeView();
        RadioButton getConfirmView();
    }

}
