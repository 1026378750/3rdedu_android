package com.shengzhe.disan.xuetangteacher.mvp.activity.schedule;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.MyRecyclerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 课程日期选择 on 2017/12/5.
 */

public class ScheduleCalendarActivity extends BaseActivity implements MonthPager.OnPageListener{
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
    private String defaultDate = "";

    private CalendarDate selectDate;
    private ArrayList<CalendarView> currentCalendars = new ArrayList<>();
    private String timeList="";
    private RadioButton selectBtn;
    private Calendar data;

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        data = bundle.getParcelable(StringUtils.calendar);

        defaultDate = TextUtils.isEmpty(data.data)?ClassScheduleUtil.getDateForWeek(data.week):data.getDate();
        selectDate = new CalendarDate(defaultDate);

        mDate.setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月");

        initMonthPager();
        if (!TextUtils.isEmpty(data.time))
            timeList = data.getDate()+" "+data.time.split("-")[0];
        mTime.setText(selectDate.getYear() + "年"+selectDate.getMonth() + "月"+selectDate.getDay()+"日"+" "+data.time);
        // 实例化一个GridLayoutManager，列数为2
        //把LayoutManager设置给RecyclerView
        nContent.setLayoutManager(UiUtils.getGridLayoutManager(6));
        nContent.setAdapter(adapt);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_schedulecalendar;
    }

    private void initMonthPager() {
        mCalendar.setOnPageListener(this);
        mCalendar.setDefaultData(defaultDate);
        mCalendar.setDisable(true);
        mCalendar.setWeekAble(data.week,null);
        mCalendar.notifyMarkData(null);
        mCalendar.setPagerAdapter();
        mCalendar.setCurrentPage(selectDate);
    }

    private SimpleAdapter adapt = new SimpleAdapter<String>(this, ContentUtil.selectDate(), R.layout.item_select_time) {
        @Override
        protected void onBindViewHolder(TrdViewHolder holder, final String data) {
           final RadioButton item = holder.getView(R.id.rb_select_item);
            item.setText(data);
            item.setChecked(false);
            mConfirm.setChecked(false);
            mConfirm.setClickable(false);
            item.setBackgroundResource(R.drawable.search_item_bg_selector);
            item.setTextColor(UiUtils.getColorStateList(R.drawable.selector_textcolor_white666666));

            if(ClassScheduleUtil.isPastDue(selectDate,data)){
                //不安排时间
                item.setBackgroundResource(R.drawable.btn_nomal4);
                item.setTextColor(UiUtils.getColor(R.color.color_666666));
                return;
            }

            if(timeList.equals(selectDate.toString()+" "+data)) {
                selectBtn = item;
                item.setChecked(true);
            }
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(v!=selectBtn){
                        /*if(Integer.parseInt(data.split(":")[0])+getIntent().getIntExtra(StringUtils.singleTime,0)>23){
                            ((RadioButton) v).setChecked(false);
                            ConfirmDialog.newInstance("", "您选择的开始时间与课时时长不符，重新选择", "", "确定")
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
        mTime.setText(selectDate.getYear() + "年" + (selectDate.getMonth() < 10 ? "0" + selectDate.getMonth() : selectDate.getMonth()) + "月" + (selectDate.getDay() < 10 ? "0" + selectDate.getDay() : selectDate.getDay()) + "日" + " " + data + "-" + ClassScheduleUtil.getTimeLagTeacher(data, String.valueOf(getIntent().getIntExtra(StringUtils.singleTime,0))));
        timeList = "";
        selectBtn.setChecked(true);
        mConfirm.setChecked(true);
        mConfirm.setClickable(true);
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.cb_schedulecalendar_confirm})
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                //刷新课表
                onBackPressed();
                break;

            case R.id.cb_schedulecalendar_confirm:
                //提交
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11001);
                bundle.putString(StringUtils.EVENT_DATA,mTime.getText().toString().trim());
                EventBus.getDefault().post(bundle);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentCalendars!=null)
            currentCalendars.clear();
    }

    @Override
    public void onCurrentPage(ArrayList<CalendarView> calendarList, CalendarDate date) {
        currentCalendars = calendarList;
        mDate.setText(date.getYear() + "年"+date.getMonth() + "月");
    }

    @Override
    public void onCheckedDate(CalendarDate date) {
        selectDate = date;
        selectBtn = null;
        mTime.setText(date.getYear() + "年"+(date.getMonth()<10?"0"+date.getMonth():date.getMonth()) + "月"+(date.getDay()<10?"0"+date.getDay():date.getDay())+"日");
        mConfirm.setChecked(false);
        mConfirm.setClickable(false);
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11039);
        EventBus.getDefault().post(bundle);
        finish();
    }
}
