package com.shengzhe.disan.xuetangparent.mvp.fragment.mine;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MineCourseView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/******
 * 我的课程
 */
public class MineCourseItemFragment extends BaseFragment implements BaseFragment.LazyLoadingListener, RefreshCommonView.RefreshLoadMoreListener, MineCourseView.IMineCourseView {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private CoursePresenter presenter;
    private int type = 0;

    public static MineCourseItemFragment newInstance(String from) {
        MineCourseItemFragment fragment = new MineCourseItemFragment();
        Bundle args = new Bundle();
        args.putString(StringUtils.FRAGMENT_DATA, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        String status = getArguments().getString(StringUtils.FRAGMENT_DATA);
        if (status.equals("未开课")) {
            type =  21;
        } else if (status.equals("已开课")) {
            type =  22;
        } else if (status.equals("已完成")) {
            type =  80;
        }
        if (presenter == null)
            presenter = new CoursePresenter(mContext, this);
        presenter.initMineCourseItemUi(this);
        presenter.initMineCourseItemDatas();
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
        refreshCommonView.notifyData();
    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        presenter.getMineCourseArrayList().clear();
        pageNum = 1;
        presenter.loadMineCourse(type,pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadMineCourse(type,pageNum);
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
