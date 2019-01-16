package com.shengzhe.disan.xuetangparent.mvp.fragment.teacher;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OneToOnePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineItemView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by 我的课程 线下1对1 on 2018/1/17.
 */

public class OfflineItemFragment extends BaseFragment implements RefreshCommonView.RefreshLoadMoreListener, BaseFragment.LazyLoadingListener,OfflineItemView.IOfflineItemView {
    @BindView(R.id.rcv_mineclass_commonlayout)
    RefreshCommonView refreshCommonView;

    private OneToOnePresenter presenter;
    private int teacherId = 0;

    public static OfflineItemFragment newInstance(int teacherId) {
        OfflineItemFragment fragment = new OfflineItemFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.TEACHER_ID, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new OneToOnePresenter(mContext,this);
        presenter.initOfflineItemUi();
        presenter.initOfflineItemDatas(this);

        teacherId = getArguments().getInt(StringUtils.TEACHER_ID);
        setLazyLoadingListener(this);
    }

    @Override
    public void startRefresh() {

    }

    private int pageNum = 1;
    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadOfflineTeacher(teacherId,pageNum,(getActivity() instanceof OfflineTeacherActivity));
    }


    @Override
    public int setLayout() {
        return R.layout.fragment_mineclass;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.loadOfflineTeacher(teacherId,pageNum,(getActivity() instanceof OfflineTeacherActivity));
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
