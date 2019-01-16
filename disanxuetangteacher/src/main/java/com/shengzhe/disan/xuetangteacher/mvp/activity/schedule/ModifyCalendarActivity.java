package com.shengzhe.disan.xuetangteacher.mvp.activity.schedule;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
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
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.MyRecyclerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 课程日期选择 on 2017/12/5.
 */

public class ModifyCalendarActivity extends BaseActivity implements MonthPager.OnPageListener{

    @BindView(R.id.tv_schedulecalendar_date)
    TextView mDate;
    @BindView(R.id.mp_schedulecalendar_calendar)
    MonthPager mCalendar;
    @BindView(R.id.mp_schedulecalendar_content)
    MyRecyclerView nContent;
    @BindView(R.id.tv_schedulecalendar_time)
    TextView mTime;
    @BindView(R.id.cb_schedulecalendar_confirm)
    CheckBox mConfirm;

    private CalendarDate selectDate;
    private ArrayList<CalendarView> currentCalendars = new ArrayList<>();
    private RadioButton selectBtn;
    private Set<String> selectSet = new HashSet<>();
    private List<String> current;
    private Bundle bundle=null;
    private Schedule newSchedule = null;
    private ArrayList<String> weekList = new ArrayList<>();
    private Map<String,List<String>> timeMap = new HashMap<>();
    private String selfDate = "";
    private int middHour = 0;
    private String currentTime = "",endTime = "";

    @Override
    public void initData() {
        selectSet.clear();
        weekList.clear();
        timeMap.clear();
        bundle = getIntent().getExtras();
        middHour = bundle.getInt(StringUtils.singleTime,0);
        weekList.addAll(bundle.getStringArrayList(StringUtils.WeekList));
        Schedule schedule =  bundle.getParcelable(StringUtils.schedule);
        selfDate = schedule.date;
        newSchedule = schedule;
        current = ClassScheduleUtil.getTimeQuantumTeacher(selfDate,schedule.time);
        currentTime = selfDate +" " + schedule.time.split("-")[0];
        endTime = selfDate+" " +  schedule.time.split("-")[1];
        selectDate = new CalendarDate(newSchedule.date);

        mDate.setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月");
        mTime.setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月"+selectDate.getDay()+"日");
        initMonthPager();

        //把LayoutManager设置给RecyclerView
        nContent.setLayoutManager(UiUtils.getGridLayoutManager(6));
        nContent.setAdapter(adapt);
        mConfirm.setChecked(true);
        mConfirm.setClickable(true);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_schedulecalendar;
    }


    private void initMonthPager() {
        List<String> markData = new ArrayList<>();
        ArrayList<Schedule> scheduleList = bundle.getParcelableArrayList(StringUtils.scheduleList);
        for(Schedule item : scheduleList){
            markData.add(item.date);
            List<String> child = ClassScheduleUtil.getTimeQuantumTeacher(item.date, item.time);
            if(child==null||child.isEmpty()){
                continue;
            }
            String one = child.get(0);
            int minIndex = child.size()-1<current.size()-1?child.size()-1:current.size()-1;
            String two = child.get(minIndex)==null?child.get(0):child.get(minIndex);
            if(one ==null||two==null||one.equals(current.get(0))||two.equals(current.get(minIndex))){
                //排除当前选中的日期
                continue;
            }
            List<String> timeList = new ArrayList<>();
            for (String childItem : child){
                selectSet.add(childItem);
                timeList.add(childItem.split(" ")[1]);
            }
            timeMap.put(item.date,timeList);
        }
        mCalendar.setOnPageListener(this);
        mCalendar.setDefaultData(selectDate.toString());
        mCalendar.notifyMarkData(markData);
        mCalendar.setDisable(true);
        mCalendar.setWeekAble("",null);
        mCalendar.setPagerAdapter();
        mCalendar.setCurrentPage(selectDate);
        mTime.setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月"+selectDate.getDay()+"日"+" "+ newSchedule.time);
    }

    private SimpleAdapter adapt = new SimpleAdapter<String>(this, ContentUtil.selectDate(), R.layout.item_select_time) {
        @Override
        protected void onBindViewHolder(TrdViewHolder holder, final String data) {
            final RadioButton item = holder.getView(R.id.rb_select_item);
            item.setText(data);
            final String dateStr = selectDate.toString() + " " + data;
            item.setBackgroundResource(R.drawable.search_item_bg_selector);
            item.setTextColor(UiUtils.getColorStateList(R.drawable.selector_textcolor_white666666));
            item.setChecked(true);
            item.setEnabled(true);
            if(ClassScheduleUtil.isPastDue(selectDate,data)){
                //不安排时间
                item.setBackgroundResource(R.drawable.btn_nomal4);
                item.setTextColor(UiUtils.getColor(R.color.color_666666));
                return;
            }

            if(selectSet.contains(dateStr)){
                //其他天选中的 不可选择
                item.setChecked(false);
                item.setEnabled(false);
                item.setBackgroundResource(R.drawable.btn_nomal4);
            }

            if(currentTime.equals(dateStr)) {
                selectBtn = item;
                selectBtn.setChecked(true);
            }else{
                item.setChecked(false);
            }

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(v!=selectBtn){
                        /*if(ClassScheduleUtil.isValidTime(data,bundle.getInt(StringUtils.singleTime,0),timeMap.get(selectDate.toString()))){
                            ((RadioButton) v).setChecked(false);
                            ConfirmDialog.newInstance("", "您选择的开始时间与课时时长不符，重新再选", "", "确定")
                                    .setMargin(60)
                                    .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                                    .setOutCancel(false)
                                    .show(getSupportFragmentManager());
                            return;
                        }*/
                        if(selectBtn!=null)
                            selectBtn.setChecked(false);
                        selectBtn = (RadioButton) v;
                        setSelectStatus(data);
                    }
                }
            });
        }
    };

    private void setSelectStatus(String data){
        selectBtn.setChecked(true);
        newSchedule.time = data+"-"+ClassScheduleUtil.getTimeLagTeacher(data,String.valueOf(middHour));
        mTime.setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月"+selectDate.getDay()+"日"+" "+ newSchedule.time);
        newSchedule.date = selectDate.getYear() + "-"+(selectDate.getMonth()<10?"0"+selectDate.getMonth():selectDate.getMonth()) + "-"+(selectDate.getDay()<10?"0"+selectDate.getDay():selectDate.getDay());
        newSchedule.week = ClassScheduleUtil.numToWeek(selectDate.getIntWeek());
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.cb_schedulecalendar_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                //刷新课表
                onBackPressed();
                break;

            case R.id.cb_schedulecalendar_confirm:
                //提交
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11004);
                bundle.putParcelable(StringUtils.EVENT_DATA,newSchedule);
                EventBus.getDefault().post(bundle);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (current != null)
            current.clear();
        if (currentCalendars != null)
            currentCalendars.clear();
        if (selectSet != null)
            selectSet.clear();
        if (weekList != null)
            weekList.clear();
        if (timeMap != null)
            timeMap.clear();
    }

    @Override
    public void onCurrentPage(ArrayList<CalendarView> currentCalendars,CalendarDate date) {
        this.currentCalendars = currentCalendars;
        mDate.setText(date.getYear() + "年"+date.getMonth() + "月");
    }

    @Override
    public void onCheckedDate(CalendarDate date) {
        selectDate = date;
        mTime.setText(date.getYear() + "年"+date.getMonth() + "月"+date.getDay()+"日");
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11039);
        EventBus.getDefault().post(bundle);
        finish();
    }
}
