package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MineTeacherView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的老师
 * <p>
 * liukui 2017/11/23 12:14
 */

public class MineTeacherActivity extends BaseActivity implements  RefreshCommonView.RefreshLoadMoreListener ,MineTeacherView.IMineTeacherView{
    @BindView(R.id.rv_mine_teacher)
    RefreshCommonView refreshCommonView;

    private UserPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initMineTeacherUi();
        presenter.initMineTeacherDatas(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mineteacher;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void startRefresh() {
        presenter.loadMineTeacher();
    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
