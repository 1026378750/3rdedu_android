package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MineMessageView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的消息
 * <p>
 * liukui 2017/11/23 11:02
 */

public class MineMessageActivity extends BaseActivity implements  RefreshCommonView.RefreshLoadMoreListener,MineMessageView.IMineMessageView {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private CommonPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.initMineMessageUi();
        presenter.initMineMessageDatas(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_minemssage;
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

    private int pageNum = 1;
    @Override
    public void startRefresh() {
        presenter.getMessageList().clear();
        pageNum = 1;
        presenter.loadMineMessage(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadMineMessage(pageNum);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11013);
        EventBus.getDefault().post(bundle);
        finish();
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
