package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MineWallView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/******
 * 我的钱包
 */
public class MineWalletActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener,MineWallView.IMineWallView{
    @BindView(R.id.tv_withdrawal_amount)
    TextView tvWithdrawalAmount;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    @BindView(R.id.tv_amount_freezing)
    TextView tvAmountFreezing;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_apply_ithdraw)
    TextView tvApplyIthdraw;
    @BindView(R.id.ccb_wallet_month)
    CommonCrosswiseBar mMonth;
    @BindView(R.id.rv_mine_wallet)
    RefreshCommonView refreshCommonView;

    private UserPresenter presenter;
    private ConfirmDialog dialog;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initMineWalletUi(getSupportFragmentManager());
        presenter.setMineWalletDatas(this);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_wallet;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.common_bar_rightBtn, R.id.tv_apply_ithdraw, R.id.ccb_wallet_month})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                AddBank();
                break;

            case R.id.tv_apply_ithdraw:
                //全部提现
                if(TextUtils.isEmpty(presenter.getMyBlance().getBankNum())){
                    dialog = ConfirmDialog.newInstance("", "您还没有绑定银行卡，请先绑定银行卡", "取消", "去绑定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    //绑定银行卡
                                    AddBank();
                                    break;
                            }
                        }
                    });
                    return;
                }
                if (presenter.getMyBlance().getAvailAmount() <= 0) {
                    return;
                }
                //提现提醒
                if (dialog == null) {
                    dialog = ConfirmDialog.newInstance("您确定要申请提现？", "<font color='#666666'>提交申请后，在审核期间</font><font color='#ffae12'>¥" + ArithUtils.roundLong(presenter.getMyBlance().getAvailAmount()) + "</font><font color='#666666'>将" +
                            "会被冻结，请谅解！</font>", "取消", "提现");
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                    @Override
                    public void dialogStatus(int id) {
                        switch (id) {
                            case R.id.tv_dialog_ok:
                                //确定 提现
                                presenter.applyUserWithdrawals();
                                break;
                        }
                    }
                });
                break;

            case R.id.ccb_wallet_month:
                //选择月份
                presenter.selectMyWalletDate();
                break;
        }
    }

    /**
     * 添加银行卡
     */
    private void AddBank() {
        Intent intent=new Intent();
        intent.setClass(this,BankCardActivity.class);
        intent.putExtra("bankNum",presenter.getMyBlance().getBankNum());
        startActivity(intent);
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {

        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.TEA__BINDING_32014:
                presenter.loadMyBalance();
        }
        return  false;
    }


    @Override
    public void startRefresh() {
        presenter.loadMineWalletDatas();
        presenter.loadMyBalance();
    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public TextView getWithdrawalAmountView() {
        return tvWithdrawalAmount;
    }

    @Override
    public TextView getWithdrawalView() {
        return tvWithdrawal;
    }

    @Override
    public TextView getAmountFreezingView() {
        return tvAmountFreezing;
    }

    @Override
    public TextView getAmountView() {
        return tvAmount;
    }

    @Override
    public TextView getApplyIthdrawView() {
        return tvApplyIthdraw;
    }

    @Override
    public CommonCrosswiseBar getMonthView() {
        return mMonth;
    }

    @Override
    public RefreshCommonView getRefreshCommonViewView() {
        return refreshCommonView;
    }
}
