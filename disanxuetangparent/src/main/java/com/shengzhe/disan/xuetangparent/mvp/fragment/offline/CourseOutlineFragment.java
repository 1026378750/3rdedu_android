package com.shengzhe.disan.xuetangparent.mvp.fragment.offline;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.CourseOutlineView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by 大纲 on 2018/4/2.
 */

public class CourseOutlineFragment extends BaseFragment implements BaseFragment.LazyLoadingListener,CourseOutlineView.ICourseOutlineView {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private CoursePresenter presenter;
    private int courseId;

    public static CourseOutlineFragment getInstance(int courseId) {
        CourseOutlineFragment fragment = new CourseOutlineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.COURSE_ID,courseId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        courseId=getArguments().getInt(StringUtils.COURSE_ID);
        if (presenter==null)
            presenter = new CoursePresenter(mContext,this);
        presenter.initCourseOutlineUi();
        presenter.initCourseOutlineDatas();
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.getSquadScheduleList(courseId);
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
