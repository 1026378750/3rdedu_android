package com.shengzhe.disan.xuetangparent.mvp.fragment.mine;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.ClassPresenter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.ClassItemView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by 线下班课 on 2018/4/2.
 */

public class ClassItemFragment extends BaseFragment implements RefreshCommonView.RefreshLoadMoreListener, BaseFragment.LazyLoadingListener,ClassItemView.IClassItemView {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private int teacherId = 0;
    private ClassPresenter presenter;

    public static ClassItemFragment newInstance(int teacherId) {
        ClassItemFragment fragment = new ClassItemFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.TEACHER_ID, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        teacherId = getArguments().getInt(StringUtils.TEACHER_ID);
        if (presenter==null)
            presenter = new ClassPresenter(mContext,this);
        presenter.initClassItemUi();
        presenter.setClassItemDatas(this);
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle;
    }

    @Override
    public void onClick(View v) {

    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        presenter.getClassItemCourseList().clear();
        pageNum = 1;
        presenter.loadClassItemCourseList(teacherId,pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadClassItemCourseList(teacherId,pageNum);
    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.getClassItemCourseList().clear();
        pageNum = 1;
        presenter.loadClassItemCourseList(teacherId,pageNum);
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
