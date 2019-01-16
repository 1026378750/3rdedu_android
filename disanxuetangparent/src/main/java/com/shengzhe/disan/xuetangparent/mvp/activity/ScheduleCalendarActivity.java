package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.view.MyRecyclerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OneToOnePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.ScheduleCalendarView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 课程日期选择 on 2017/12/5.
 */

public class ScheduleCalendarActivity extends BaseActivity implements ScheduleCalendarView.IScheduleCalendarView {
    @BindView(R.id.tv_schedulecalendar_date)
    TextView mDate;
    @BindView(R.id.mp_schedulecalendar_calendar)
    MonthPager mCalendar;
    @BindView(R.id.mp_schedulecalendar_content)
    MyRecyclerView mContent;
    @BindView(R.id.tv_schedulecalendar_time)
    TextView mTime;
    @BindView(R.id.tv_schedulecalendar_confirm)
    RadioButton mConfirm;

    private int courseId = -1;
    private OneToOnePresenter presenter;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,-1);
        if (presenter==null){
            presenter = new OneToOnePresenter(mContext,this);
        }
        presenter.initScheduleCalendarUi();
        presenter.initScheduleCalendarDatas(getIntent(),getSupportFragmentManager());
        presenter.getTeacherTimeType(courseId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_schedulecalendar;
    }


    @OnClick({R.id.common_bar_leftBtn,R.id.tv_schedulecalendar_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_schedulecalendar_confirm:
                //提交
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11001);
                bundle.putString(StringUtils.EVENT_DATA,mTime.getText().toString().trim());
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearScheduleCalendarDatas();
    }

    @Override
    public TextView getDateView() {
        return mDate;
    }

    @Override
    public MonthPager getCalendarView() {
        return mCalendar;
    }

    @Override
    public MyRecyclerView getContentView() {
        return mContent;
    }

    @Override
    public TextView getTimeView() {
        return mTime;
    }

    @Override
    public RadioButton getConfirmView() {
        return mConfirm;
    }
}
