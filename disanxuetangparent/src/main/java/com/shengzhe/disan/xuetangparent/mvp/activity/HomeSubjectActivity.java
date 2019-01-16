package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.Subject;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.HomeSubjectView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 在线课堂
 */
public class HomeSubjectActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener,DropDownMenu.DropMenuFragmentManage,HomeSubjectView.IHomeSubjectView {
    @BindView(R.id.rv_common_headview)
    CommonCrosswiseBar commonCrosswiseBar;
    @BindView(R.id.ddm_common_dropmenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private CoursePresenter presenter;

    @Override
    public void initData() {
        Subject subject = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        if (presenter ==null) {
            presenter = new CoursePresenter(mContext,this);
        }
        presenter.initHomeSubjectUi();
        presenter.setHomeSubjectDatas(this,subject,this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_home_subject;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                finish();
                break;
        }
    }

    private int pageNum = 1;
    @Override
    public void startRefresh() {
        pageNum = 1;
        presenter.clearSubject();
        presenter.loadHomeSubject(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadHomeSubject(pageNum);
    }

    @Override
    public CommonCrosswiseBar getCommonCrosswiseBarView() {
        return commonCrosswiseBar;
    }

    @Override
    public DropDownMenu getDropDownMenuView() {
        return mDropDownMenu;
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    @Override
    public FragmentTransaction requestFragmentManage() {
        return getSupportFragmentManager().beginTransaction();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

