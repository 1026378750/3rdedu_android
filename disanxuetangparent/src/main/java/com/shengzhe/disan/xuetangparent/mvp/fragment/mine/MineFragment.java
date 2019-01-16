package com.shengzhe.disan.xuetangparent.mvp.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineHelpActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineMessageActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineOrderActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineWalletActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MinesSistantActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.SettingActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.UserMessageActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MineFragmentView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/*****
 *
 *我的
 *
 */
public class MineFragment extends BaseFragment implements RefreshCommonView.RefreshLoadMoreListener,MineFragmentView.IMineView {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private UserPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initMineUi();
        presenter.setMineDatas(this,this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_headimage:
            case R.id.tv_usermesg:
                //用户信息
                startActivity(new Intent(mContext, UserMessageActivity.class));
                break;

            case R.id.tv_mineorder:
                //我的订单
                startActivity(new Intent(mContext, MineOrderActivity.class));
                break;

            case R.id.tv_minecourse:
                //我的课程
                Intent mineCourseIntent = new Intent(mContext, MineCourseActivity.class);
                mineCourseIntent.putExtra(StringUtils.FRAGMENT_INDEX,0);
                startActivity(mineCourseIntent);
                break;

            case R.id.ccb_mineteacher:
                //我的老师
                startActivity(new Intent(mContext, MineTeacherActivity.class));
                break;

            case R.id.ccb_mineteacher_ssistant:
                //我的助教
                startActivity(new Intent(mContext, MinesSistantActivity.class));
                break;

            case R.id.ccb_minemessage:
                //我的消息
                startActivity(new Intent(mContext, MineMessageActivity.class));
                break;

            case R.id.ccb_minewallet:
                //我的钱包
                startActivity(new Intent(mContext, MineWalletActivity.class));
                break;

            case R.id.ccb_help:
                //帮助中心
                startActivity(new Intent(mContext, MineHelpActivity.class));
                break;

            case R.id.ccb_setting:
                //设置
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }

    @Override
    public void startRefresh() {
        presenter.loadMineDatas();
    }

    @Override
    public void startLoadMore() {

    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11006:
                if (!bundle.getBoolean(StringUtils.EVENT_DATA))
                    break;
            case IntegerUtil.EVENT_ID_11013:
                getRefreshCommonView().notifyData();
                break;

        }
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    @Override
    public ImageView getHeadimageView() {
        return (ImageView) getRefreshCommonView().findViewById(R.id.iv_headimage);
    }

    @Override
    public TextView getNiceNameView() {
        return (TextView)getRefreshCommonView().findViewById(R.id.tv_nicename);
    }

    @Override
    public TextView getPhoneNumView() {
        return (TextView)getRefreshCommonView().findViewById(R.id.tv_phonenum);
    }

    @Override
    public TextView getUsermesgView() {
        return (TextView) getRefreshCommonView().findViewById(R.id.tv_usermesg);
    }

    @Override
    public CommonCrosswiseBar getMinemessageView() {
        return (CommonCrosswiseBar)getRefreshCommonView().findViewById(R.id.ccb_minemessage);
    }

    @Override
    public CommonCrosswiseBar getMineteacherView() {
        return (CommonCrosswiseBar)getRefreshCommonView().findViewById(R.id.ccb_mineteacher);
    }

    @Override
    public CommonCrosswiseBar getMineteacherSsistantView() {
        return (CommonCrosswiseBar)getRefreshCommonView().findViewById(R.id.ccb_mineteacher_ssistant);
    }

    @Override
    public CommonCrosswiseBar getMinewalletView() {
        return (CommonCrosswiseBar)getRefreshCommonView().findViewById(R.id.ccb_minewallet);
    }

    @Override
    public CommonCrosswiseBar getHelpView() {
        return (CommonCrosswiseBar)getRefreshCommonView().findViewById(R.id.ccb_help);
    }

    @Override
    public CommonCrosswiseBar getSettingView() {
        return (CommonCrosswiseBar)getRefreshCommonView().findViewById(R.id.ccb_setting);
    }

    @Override
    public ImageView getPointView() {
        return getMinemessageView().getRightImage();
    }

    @Override
    public TextView getMineOrderView() {
        return (TextView)getRefreshCommonView().findViewById(R.id.tv_mineorder);
    }

    @Override
    public TextView getMineCourseView() {
        return (TextView)getRefreshCommonView().findViewById(R.id.tv_minecourse);
    }
}


