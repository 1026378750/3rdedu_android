package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.banner.MyBanner;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineClazzActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MineOrderActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.StartClassActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherPagerActivity;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.HomePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.IHomeView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import butterknife.BindView;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements IHomeView ,HomePresenter.OnjudgeClickPresenter{
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;
    private TextView mNotify,mNiceName,mMessage,mOrderNum,mMineClass,mTimes;
    private ImageView mHeadImage;
    private LinearLayout mLayout;
    private MyBanner mbHomeBanner;
    private HomePresenter presenter;

    @Override
    public void initData() {
        refreshCommonView.addScrollViewChild(R.layout.fragment_home_child);
        mbHomeBanner = (MyBanner) refreshCommonView.findViewById(R.id.mb_home_banner);
        mLayout = (LinearLayout) refreshCommonView.findViewById(R.id.rl_home_notify_layout);
        mNotify = (TextView) refreshCommonView.findViewById(R.id.tv_home_notify);
        mHeadImage = (ImageView) refreshCommonView.findViewById(R.id.iv_headimage);
        mNiceName = (TextView) refreshCommonView.findViewById(R.id.tv_nicename);
        mMessage = (TextView) refreshCommonView.findViewById(R.id.tv_message);
        mOrderNum = (TextView) refreshCommonView.findViewById(R.id.tv_main_ordernum);
        mMineClass = (TextView) refreshCommonView.findViewById(R.id.tv_main_mineclass);
        mTimes = (TextView) refreshCommonView.findViewById(R.id.tv_main_times);

        mNotify.setOnClickListener(this);
        refreshCommonView.findViewById(R.id.iv_home_notify_close).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.btn_toclass_confirm).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ll_main_order).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ll_main_setting).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ll_main_mine).setOnClickListener(this);

        presenter = new HomePresenter(mContext, this);
        presenter.initDate();
        presenter.setOnpresenterjudgeClick(this);
    }


    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }



    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_home_notify:
                //点击消息
                presenter.judgeStatus();
                break;

            case R.id.iv_home_notify_close:
                //消息关闭
                presenter.setNotifyFloat();
                break;

            case R.id.btn_toclass_confirm:
                //我要开课
                presenter.dialogCreateCourse();
                break;

            case R.id.ll_main_order:
                //我的订单
                startActivity(new Intent(mContext, MineOrderActivity.class));
                break;

            case R.id.ll_main_setting:
                //课程管理
                ConstantUrl.IS_EDIT=true;
                startActivity(new Intent(mContext, MineClazzActivity.class));
                break;

            case R.id.ll_main_mine:
                //我的主页；
                ConstantUrl.CLIEN_Center=true;
                ConstantUrl.IS_EDIT = false;
                if(!presenter.judgeTeacherStatus()) return;
                //startActivity(new Intent(mContext, TeacherNewPagerActivity.class));
                startActivity(new Intent(mContext, TeacherPagerActivity.class));
                break;
        }
    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11002:
            case IntegerUtil.EVENT_ID_11038:
                //实名认证返回刷新
                presenter.postHomeData();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    @Override
    public TextView getNotify() {
        return mNotify;
    }

    @Override
    public TextView getNiceName() {
        return mNiceName;
    }

    @Override
    public TextView getMessage() {
        return mMessage;
    }

    @Override
    public TextView getOrderNum() {
        return mOrderNum;
    }

    @Override
    public TextView getMineClass() {
        return mMineClass;
    }

    @Override
    public TextView getTimes() {
        return mTimes;
    }

    @Override
    public ImageView getHeadImage() {
        return mHeadImage;
    }

    @Override
    public LinearLayout getLayout() {
        return mLayout;
    }

    @Override
    public MyBanner getHomeBanner() {
        return mbHomeBanner;
    }

    @Override
    public FragmentManager getFragmentManagers() {
        return getChildFragmentManager();
    }

    private ConfirmDialog niceDialog;

    @Override
    public void presenterjudgeClick(Object obj) {

        //开课设置
        if ((SharedPreferencesManager.getUserInfo() != null) && (SharedPreferencesManager.getOpenCity() <= 1)) {

            niceDialog = ConfirmDialog.newInstance("", "您当前所选的城市未开通，不能填写开课设置您可以联系客服修改城市", "取消", "联系客服");
            niceDialog.setMargin(60)
                    .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                    .setOutCancel(false)
                    .show(getChildFragmentManager());

            niceDialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void dialogStatus(int id) {
                    switch (id) {
                        case R.id.tv_dialog_ok:
                            SystemInfoUtil.callDialing("400005666");
                            break;
                    }
                }
            });
            return;
        }
       mContext.startActivity(new Intent(mContext, StartClassActivity.class));


    }
}
