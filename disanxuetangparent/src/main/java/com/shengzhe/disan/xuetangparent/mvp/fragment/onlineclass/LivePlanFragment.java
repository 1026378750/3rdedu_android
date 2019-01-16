package com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OnlivePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.LivePlanView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/29.
 */

public class LivePlanFragment extends BaseFragment implements LivePlanView.ILivePlanView,RefreshCommonView.RefreshLoadMoreListener,BaseFragment.LazyLoadingListener {
    @BindView(R.id.tv_liveplan_nofity)
    TextView mNotify;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private OnlivePresenter presenter;

    private int courseId = -1;

    public static Fragment getInstance(int courseId) {
        LivePlanFragment fragment = new LivePlanFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        courseId = getArguments().getInt(StringUtils.COURSE_ID);
        if (presenter==null)
            presenter = new OnlivePresenter(mContext,this);
        presenter.initLivePlanUi();
        presenter.initLivePlanDatas(this);
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_class_plan;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void startRefresh() {
        presenter.getOnliveCycleList(courseId);
    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        refreshCommonView.notifyData();
    }

    @Override
    public TextView getNotifyView() {
        return mNotify;
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
