package com.shengzhe.disan.xuetangparent.mvp.fragment.mine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.ldf.calendar.view.MonthPager;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.CourseBean;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.BaseView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineScheduleView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;

import butterknife.BindView;

/**
 * Created by 我的课表 on 2017/11/23.
 */
public class MineScheduleFragment extends BaseFragment implements MineScheduleView.IMineScheduleView,BaseView.WebApiListener {

    @BindView(R.id.rv_mine_class)
    RecyclerView rvMineClass;
    @BindView(R.id.tv_mine_course_date)
    TextView tvMineCourseDate;
    @BindView(R.id.calendar_view)
    MonthPager calendarView;

    private CoursePresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CoursePresenter(mContext,this);
        presenter.initMineScheduleUi(this);
        presenter.initMineScheduleDatas(getChildFragmentManager());
        presenter.getMonthStatus();
        presenter.getMySchedule();
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_mineschedule;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.clearMineScheduleDatas();
    }



    @Override
    public RecyclerView getMineClassView() {
        return rvMineClass;
    }

    @Override
    public TextView getMineCourseDateView() {
        return tvMineCourseDate;
    }

    @Override
    public MonthPager getCalendarView() {
        return calendarView;
    }

    @Override
    public void loadWebApi(int tag, Object obj) {
        switch (tag){

            case IntegerUtil.WEB_API_MySchedule:
                presenter.getMySchedule();
                break;

            case IntegerUtil.WEB_API_MonthStatus:
                presenter.getMonthStatus();
                break;

            case IntegerUtil.WEB_API_ScheduleSignStatus:
                presenter.saveScheduleSignStatus((CourseBean)obj);
                break;

        }
    }
}
