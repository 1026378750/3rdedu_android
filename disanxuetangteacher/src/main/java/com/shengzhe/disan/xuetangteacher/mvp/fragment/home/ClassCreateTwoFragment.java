package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.main.disanxuelib.bean.ClassCourseBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.schedule.ScheduleCalendarActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 在线直播了第二步 on 2018/1/17.
 */

public class ClassCreateTwoFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.cb_schooltime_monday)
    CheckBox mMonday;
    @BindView(R.id.cb_schooltime_tuesday)
    CheckBox mTuesday;
    @BindView(R.id.cb_schooltime_wednesday)
    CheckBox mWednesday;
    @BindView(R.id.cb_schooltime_thursday)
    CheckBox mThursday;
    @BindView(R.id.cb_schooltime_friday)
    CheckBox mFriday;
    @BindView(R.id.cb_schooltime_saturday)
    CheckBox mSaturday;
    @BindView(R.id.cb_schooltime_sunday)
    CheckBox mSunday;
    @BindView(R.id.rv_schooltime_content)
    RecyclerView mContent;
    @BindView(R.id.et_onliveoperator_next)
    CheckBox mConfirm;

    private ArrayList<Calendar> calList = new ArrayList<>();
    private Map<Integer, Calendar> indexMap = new HashMap<>();
    private Set<String> modiftSet = new HashSet<>();

    private ClassCourseBean onliveCourseBean;
    public static ClassCreateTwoFragment newInstance(ClassCourseBean data) {
        ClassCreateTwoFragment fragment = new ClassCreateTwoFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        if(onliveCourseBean==null)
            onliveCourseBean = new ClassCourseBean();
        modiftSet.clear();
        mMonday.setOnCheckedChangeListener(this);
        mWednesday.setOnCheckedChangeListener(this);
        mThursday.setOnCheckedChangeListener(this);
        mTuesday.setOnCheckedChangeListener(this);
        mFriday.setOnCheckedChangeListener(this);
        mSaturday.setOnCheckedChangeListener(this);
        mSunday.setOnCheckedChangeListener(this);

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
                                    .show(getFragmentManager());
                            return;
                        }
                        motifyIndex = calList.indexOf(data);
                        Intent intent = new Intent(mContext,ScheduleCalendarActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(StringUtils.calendar,data);
                        bundle.putInt(StringUtils.singleTime,onliveCourseBean.singleTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        mContent.setLayoutManager(new LinearLayoutManager(mContext));
        mContent.setAdapter(adapt);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_onlivetwo;
    }

    private int motifyIndex = 0;
    private SimpleAdapter adapt;

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

        mConfirm.setClickable(false);
        mConfirm.setChecked(false);
        if(!calList.isEmpty()&&calList.size()==modiftSet.size()){
            mConfirm.setClickable(true);
            mConfirm.setChecked(true);
        }
        Calendar[] arr = ClassScheduleUtil.bubbleSort(calList.toArray(new Calendar[calList.size()]));
        calList.clear();
        Collections.addAll(calList, arr);
        adapt.notifyDataSetChanged();
    }

    @OnClick({R.id.et_onliveoperator_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_onliveoperator_next:
                //下一步
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11040);
                onliveCourseBean.calList.clear();
                onliveCourseBean.calList.addAll(calList);
                bundle.putParcelable(StringUtils.EVENT_DATA,onliveCourseBean);
                bundle.putInt(StringUtils.EVENT_DATA2,2);
                EventBus.getDefault().post(bundle);
                break;
        }
    }

    public void setClassCourseBean(ClassCourseBean data){
        this.onliveCourseBean = data;
        modiftSet.clear();
        calList.clear();
        adapt.notifyDataSetChanged();
        indexMap.clear();
        mConfirm.setChecked(false);
        mConfirm.setClickable(false);
        motifyIndex = 0;
        mMonday.setChecked(false);
        mTuesday.setChecked(false);
        mWednesday.setChecked(false);
        mThursday.setChecked(false);
        mFriday.setChecked(false);
        mSaturday.setChecked(false);
        mSunday.setChecked(false);
    }

    public void setConfirmCheckable(){
        if(calList.size()==modiftSet.size()){
            mConfirm.setChecked(true);
            mConfirm.setClickable(true);
        }else{
            mConfirm.setChecked(false);
            mConfirm.setClickable(false);
        }
    }


    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11001:
                String[] data= bundle.getString(StringUtils.EVENT_DATA).split(" ");
                calList.get(motifyIndex).data = data[0];
                calList.get(motifyIndex).time = data[1];
                adapt.notifyItemChanged(motifyIndex);
                modiftSet.add(calList.get(motifyIndex).week);
                if(calList.size()==modiftSet.size()){
                    mConfirm.setChecked(true);
                    mConfirm.setClickable(true);
                }else{
                    mConfirm.setChecked(false);
                    mConfirm.setClickable(false);
                }
                break;

        }
    }

}
