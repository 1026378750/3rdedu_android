package com.shengzhe.disan.xuetangparent.mvp.fragment.offline;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.ClassPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineClassView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by 线下班课 on 2018/4/3.
 */

public class OfflineClassFragment extends BaseFragment implements OfflineClassView.IOfflineClassView, DropDownMenu.DropMenuFragmentManage, RefreshCommonView.RefreshLoadMoreListener,BaseFragment.LazyLoadingListener {
    @BindView(R.id.ddm_common_dropmenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private ClassPresenter presenter;
    private int teacherId = -1;


    public static OfflineClassFragment newInstance(int teacherId) {
        OfflineClassFragment fragment = new OfflineClassFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.TEACHER_ID, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        teacherId = getArguments() == null ? -1 : getArguments().getInt(StringUtils.TEACHER_ID);
        if (presenter==null)
            presenter = new ClassPresenter(mContext,this);
        presenter.initClassListUi(teacherId);
        presenter.initClassListDatas(getActivity(),this,this);
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.common_drop_refresh_layout;
    }

    @Override
    public void onClick(View v) {

    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11005:
                refreshCommonView.notifyData();
                break;
        }
    }
    private int pageNum = 1;

    @Override
    public void startRefresh() {
        presenter.clearOfflineClassListDatas();
        pageNum = 1;
        presenter.loadOfflineClassList(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadOfflineClassList(pageNum);
    }

    @Override
    public FragmentTransaction requestFragmentManage() {
        return getChildFragmentManager().beginTransaction();
    }

    @Override
    public void loadLazyDatas(boolean bool) {
        refreshCommonView.notifyData();
    }

    @Override
    public DropDownMenu getDropDownMenuView() {
        return mDropDownMenu;
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
