package com.shengzhe.disan.xuetangparent.mvp.fragment.teacher;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OnlivePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OnliveItemView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by 老师主页 在线直播课 on 2018/1/17.
 */

public class OnliveItemFragment extends BaseFragment implements RefreshCommonView.RefreshLoadMoreListener, BaseFragment.LazyLoadingListener,OnliveItemView.IOnliveItemView {
    @BindView(R.id.rcv_mineclass_commonlayout)
    RefreshCommonView refreshCommonView;
    private OnlivePresenter presenter;
    private int teacherId = 0;
    private int pageNum = 1;

    public static OnliveItemFragment newInstance(int teacherId) {
        OnliveItemFragment fragment = new OnliveItemFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.TEACHER_ID, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        teacherId = getArguments().getInt(StringUtils.TEACHER_ID);
        if (presenter==null)
            presenter = new OnlivePresenter(mContext,this);
        presenter.initOnliveItemUi();
        presenter.setOnliveItemDatas(this);
        setLazyLoadingListener(this);
    }

    @Override
    public void startRefresh() {

    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadOnliveDatas(teacherId,pageNum);
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
        presenter.loadOnliveDatas(teacherId,pageNum);
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
