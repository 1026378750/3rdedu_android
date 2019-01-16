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
import com.shengzhe.disan.xuetangparent.mvp.view.ModifyCalendarView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 课程日期选择 on 2017/12/5.
 */

public class ModifyCalendarActivity extends BaseActivity implements ModifyCalendarView.IModifyCalendarView{
    @BindView(R.id.tv_schedulecalendar_date)
    TextView mDate;
    @BindView(R.id.mp_schedulecalendar_calendar)
    MonthPager mCalendar;
    @BindView(R.id.mp_schedulecalendar_content)
    MyRecyclerView nContent;
    @BindView(R.id.tv_schedulecalendar_time)
    TextView mTime;
    @BindView(R.id.tv_schedulecalendar_confirm)
    RadioButton mConfirm;

    private OneToOnePresenter presenter;
    private int courseId = 0;
    private Bundle bundle = null;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,0);
        bundle = getIntent().getExtras();
        if (presenter==null)
            presenter = new OneToOnePresenter(mContext,this);
        presenter.initModifyCalendarUi(bundle);
        presenter.initModifyCalendarDatas(getSupportFragmentManager());
        presenter.getMTeacherTimeType(courseId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_schedulecalendar;
    }



    @OnClick({R.id.common_bar_leftBtn, R.id.tv_schedulecalendar_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_schedulecalendar_confirm:
                //提交
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11004);
                bundle.putParcelable(StringUtils.EVENT_DATA, presenter.getSchedule());
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearModifyDatas();
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
        return nContent;
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
