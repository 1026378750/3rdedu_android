package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.banner.MyBanner;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface IScheduleView {
    RecyclerView getRvMineClass();
    TextView getTvMineCourseDate();
    MonthPager getCalendarView();
}
