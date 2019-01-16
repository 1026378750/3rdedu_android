package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MinesSistantView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的助教
 * <p>
 * liukui 2017/11/23 11:56
 */

public class MinesSistantActivity extends BaseActivity implements MinesSistantView.IMinesSistantView {
    @BindView(R.id.iv_sisant_head)
    ImageView ivSisantHead;
    @BindView(R.id.iv_sisant_name)
    TextView ivSisantName;
    @BindView(R.id.tv_mine_mobile)
    TextView tvMineMobile;

    private UserPresenter presenter;

    @Override
    public void initData() {
        if(presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initMinesSistantUi(getSupportFragmentManager());
        presenter.setMinesSistantDatas();
        presenter.loadMinesSistant();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_minesistant;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.tv_mine_mobile})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_mine_mobile:
                //客服电话
                presenter.callMinesSistantPhone();
                break;
        }
    }

    @Override
    public ImageView getSisantHeadView() {
        return ivSisantHead;
    }

    @Override
    public TextView getSisantNameView() {
        return ivSisantName;
    }

    @Override
    public TextView getMineMobileView() {
        return tvMineMobile;
    }
}
