package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangteacher.bean.WalletDetailsBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.BankCardActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.MyBalance;
import com.shengzhe.disan.xuetangteacher.bean.WalletBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 我的课酬 on 2018/1/16.
 */

public class MineDollarsActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.ll_minedollars_head)
    LinearLayout mHeadView;
    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.tv_dollars_account)
    TextView mAccount;
    @BindView(R.id.tv_dollars_radix)
    TextView mRadix;
    @BindView(R.id.rcv_dollars)
    RefreshCommonView refreshCommonView;
    @BindView(R.id.tv_withdrawal_already_withdraw)
    TextView tvWithdrawalAlreadyWithdraw;
    @BindView(R.id.tv_amount_freezing_money)
    TextView tvAmountFreezingMoney;
    @BindView(R.id.ll_dollars_amount)
    LinearLayout llDollarsAmount;
    @BindView(R.id.tv_mine_month)
    TextView tvMineMonth;
    @BindView(R.id.tv_mine_dollars)
    TextView tvMineDollars;
    @BindView(R.id.rl_mine_month)
    LinearLayout rlMineMonth;
    @BindView(R.id.tv_dollars_can)
    TextView tvDollarsCan;
    @BindView(R.id.tv_dollars_all_withdraw)
    TextView tvDollarsAllWithdraw;

    private SimpleAdapter adapter;
    private NiceDialog niceDialog;
    private List<WalletBean> walletList = new ArrayList<>();
    private int pageNum = 1;
    private MyBalance myBalance = new MyBalance();

    @Override
    public void initData() {
        /*CollapsingToolbarLayout.LayoutParams params = new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,mTitle.getTitleHeight(),0,0);
        mHeadView.setLayoutParams(params);*/

        Calendar calendar = Calendar.getInstance();
        selectData = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
        tvMineMonth.setText((calendar.get(Calendar.MONTH) + 1) + "月");
        startDate = (calendar.get(Calendar.YEAR) - 1) + "-" + (calendar.get(Calendar.MONTH) + 1);
        endDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);

        llDollarsAmount.setVisibility(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 0 ?  View.VISIBLE:View.GONE);
        tvDollarsCan.setText(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 0 ? "可提现金额(元)":"已完成金额(元)" );
        tvDollarsAllWithdraw.setVisibility(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 0 ? View.VISIBLE:View.GONE);
        mTitle.setRightText(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 0 ? "银行卡":"");
        rlMineMonth.setVisibility(View.VISIBLE);

        adapter = new SimpleAdapter<WalletBean>(mContext, walletList, R.layout.item_dollars) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final WalletBean data) {
                holder.setText(R.id.tv_order_number, data.courseName)
                        .setText(R.id.bt_mine_type, data.teachingMethodName)
                        .setText(R.id.tv_deduction, "+¥" + ArithUtils.round(data.settlementPrice))
                        .setText(R.id.tv_mine_time, DateUtil.timeStampDate(data.lastAccessTime, "yyyy-MM-dd HH:mm"))
                        .setText(R.id.tv_mine_class_name, data.userName);
                if (data.courseType == 1) {
                    switch (data.teachingMethod) {
                        case 1:
                            holder.setBackgroundRes(R.id.bt_mine_type, R.mipmap.mine_teacher);
                            holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_one_tow_one);
                            holder.setText(R.id.bt_mine_type, "学生上门");
                            //holder.setTextColor(R.id.bt_mine_type,R.color.color_ffffff);
                            break;
                        case 2:
                            holder.setBackgroundRes(R.id.bt_mine_type, R.mipmap.mine_studnet);
                            holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_one_tow_one);
                            holder.setText(R.id.bt_mine_type, "老师上门");
                         //   holder.setTextColor(R.id.bt_mine_type,R.color.color_ffffff);
                            break;
                        case 3:
                            holder.setBackgroundRes(R.id.bt_mine_type, R.mipmap.mine_school);
                            holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_one_tow_one);
                            holder.setText(R.id.bt_mine_type, "校区上课");
                         //   holder.setTextColor(R.id.bt_mine_type,R.color.color_ffffff);
                            break;
                    }
                }
                if (data.courseType == 4){
                    holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_offline_class);
                    holder.setVisible(R.id.bt_mine_type,false);
                }

                if (data.courseType == 2) {
                    holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_live);
                    switch (data.directType) {
                        case 1:
                            holder.setBackgroundRes(R.id.bt_mine_type, R.mipmap.mine_offline);
                            holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_live);
                            holder.setText(R.id.bt_mine_type, "一对一授课");
                         //   holder.setTextColor(R.id.bt_mine_type,R.color.color_ffffff);
                            break;
                        case 2:
                            holder.setBackgroundRes(R.id.bt_mine_type, R.mipmap.mine_interact);
                            holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_live);
                            holder.setText(R.id.bt_mine_type, "互动小班课");
                         //   holder.setTextColor(R.id.bt_mine_type,R.color.color_ffffff);
                            break;
                        case 3:
                            holder.setBackgroundRes(R.id.bt_mine_type, R.mipmap.mine_general);
                            holder.setBackgroundRes(R.id.iv_minewall, R.mipmap.mine_live);
                            holder.setText(R.id.bt_mine_type, "普通大班课");
                         //   holder.setTextColor(R.id.bt_mine_type,R.color.color_ffffff);
                            break;
                    }
                }
            }
        };
        refreshCommonView.setRecyclerViewAdapter(adapter);
        refreshCommonView.setRefreshLoadMoreListener(this);
    }


    /**
     * 我的钱包账户记录信息
     */
    private void loadDatas() {
        try {
            HttpService httpService = Http.getHttpService();
            Map<String, Object> map = new HashMap<>();
            map.put("pageNum", pageNum);
            map.put("pageSize", 15);
            map.put("billTime", new SimpleDateFormat("yyyy-MM").parse(selectData).getTime());
            ConstantUrl.CLIEN_Info=2;
            httpService.myWallet(RequestBodyUtils.setRequestBody(map))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AbsAPICallback<BasePageBean<WalletDetailsBean>>(mContext, true) {
                        @Override
                        protected void onDone(BasePageBean<WalletDetailsBean> myWallet) {
                            ConstantUrl.CLIEN_Info=1;
                            if( myWallet.getList()!=null &&  myWallet.getList().size()>0){
                                List<WalletBean> walletBeanList = myWallet.getList().get(0).getListMap();
                                walletList.addAll(myWallet.getList().get(0).getListMap());
                            }
                            refreshCommonView.finishRefresh();
                            refreshCommonView.finishLoadMore();
                            if (myWallet.getList().isEmpty()) {
                                myWallet.getList().clear();
                                refreshCommonView.setIsEmpty(true);
                                tvMineDollars.setText("0.00");
                            } else {
                                tvMineDollars.setText("¥" + ArithUtils.round(myWallet.getList().get(0).getSumsettlementAmount()));
                                refreshCommonView.setIsEmpty(false);
                                refreshCommonView.setIsLoadMore(myWallet.isHasNextPage());
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onResultError(ResultException ex) {
                            refreshCommonView.finishRefresh();
                            refreshCommonView.finishLoadMore();
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //账户余额信息
    private void postMyBalance() {
        HttpService httpService = Http.getHttpService();
        httpService.myBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBalance>(mContext, true) {
                    @Override
                    protected void onDone(MyBalance _myBalance) {
                        myBalance = _myBalance;
                        tvWithdrawalAlreadyWithdraw.setText(ArithUtils.roundLong((_myBalance.getTotalAmount())));
                        tvAmountFreezingMoney.setText(ArithUtils.roundLong((_myBalance.getLockAmount())));
                        if(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 1){
                            String money = ArithUtils.roundDouble(_myBalance.getSumsettlementAmount());
                            money = "158.05241";
                            mAccount.setText(ArithUtils.getIntegerNum(money));
                            mRadix.setText(ArithUtils.getPointNum(money));
                        }else {
                            String money = ArithUtils.roundDouble(_myBalance.getAvailAmount());
                            money = "158.05241";
                            mAccount.setText(ArithUtils.getIntegerNum(money));
                            mRadix.setText(ArithUtils.getPointNum(money));
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }


    @Override
    public int setLayout() {
        return R.layout.activity_mine_dollars;
    }

    private String startDate, endDate;
    private String selectData = "";

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn, R.id.rl_mine_month, R.id.tv_dollars_all_withdraw, R.id.ll_dollars_withdrawal, R.id.ll_dollars_freezing})
    @Override
    public void onClick(View v) {
        ConfirmDialog dialog;
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.tv_dollars_all_withdraw:
                //全部提现
                if (TextUtils.isEmpty(myBalance.getBankNum())) {
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

             if (myBalance.getAvailAmount() == 0) {
                    dialog = ConfirmDialog.newInstance("", "金额为0.00不可提现", "", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                } else if(myBalance.getIsWithdrawAppy() >= 1){
                    dialog = ConfirmDialog.newInstance("", "每天只能提现一次", "", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                } else {
                    dialog = ConfirmDialog.newInstance("", "您确定要全额提现到尾号为[" + myBalance.getBankNum().substring(myBalance.getBankNum().length() - 4, myBalance.getBankNum().length()) + "]的银行卡中？", "取消", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    //确定 提现
                                    postUserWithdrawals();

                                    break;
                            }
                        }
                    });


                }

                break;
            case R.id.ll_dollars_withdrawal:

                Intent intent = new Intent(this, WithdrawalRecordActivity.class);
                //在意图中传递数据
                intent.putExtra("totalAmount", myBalance.getTotalAmount());
                startActivity(intent);
                //已提现
                break;

            case R.id.ll_dollars_freezing:
                //冻结中
                if(myBalance.getLockAmount()==0){
                    return;
                }
                if (myBalance.getLockAmount() != 0) {
                    dialog = ConfirmDialog.newInstance("", "金额冻结中，工作人员会在3-5个工作 日内会将金额打入尾号为[" + myBalance.getBankNum().substring(myBalance.getBankNum().length() - 4, myBalance.getBankNum().length()) + "]的银行 卡中，请您耐心等待！", "", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                }

                break;

            case R.id.common_bar_rightBtn:
                if(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 0 ){
                    AddBank();
                }
                break;
            case R.id.rl_mine_month:
                //选择月份
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.common_popup_yymm)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                pickerView.setDateRange(startDate, endDate).setShowDatePicker(selectData);
                                holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        colseDialog();
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        //如果是当年的不显示年份
                                        selectData = String.valueOf(obj);
                                        if (TextUtils.isEmpty(selectData) || !selectData.contains("-")) {
                                            colseDialog();
                                            return;
                                        }
                                        String[] select = selectData.split("-");
                                        //tvMineMonth.setText((select[0].equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))) ? "" : select[0] + "年") + select[1] + "月");
                                        tvMineMonth.setText(select[1] + "月");
                                        refreshCommonView.notifyData();
                                        colseDialog();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
        }
    }

    /**
     * 全额提现
     */
    private void onClickAll() {
        ConfirmDialog dialog;
        dialog = ConfirmDialog.newInstance("", "申请成功，工作人员会在3-5工作日 内会将金额打入尾号为[" + myBalance.getBankNum().substring(myBalance.getBankNum().length() - 4, myBalance.getBankNum().length()) + "]卡中，请您耐心等待！", "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        //确定 提现
                       // postUserWithdrawals();
                        //allMoney=2;
                        break;
                }
            }
        });


    }
    /**
     * 添加银行卡
     */
    private void AddBank() {
        Intent intent = new Intent();
        intent.setClass(this, BankCardActivity.class);
        intent.putExtra("bankNum", myBalance.getBankNum());
        startActivity(intent);
    }
    //申请提现
    private void postUserWithdrawals() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        map.put("withdrawalsAmount", myBalance.getAvailAmount());
        httpService.userWithdrawals(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String strTeacher) {
                       // ToastUtil.showToast(strTeacher);
                        ToastUtil.showShort("更新成功");
                        mAccount.setText("0");
                        mRadix.setText(".00");
                        onClickAll();
                        tvAmountFreezingMoney.setText(ArithUtils.roundLong((myBalance.getLockAmount()+myBalance.getAvailAmount())));
                        //tvAmountFreezingMoney.setText("0.00");
                        //refreshCommonView.notifyData();
                      /*  if(allMoney==2){
                        }*/

                    }

                    @Override
                    public void onResultError(ResultException ex) {

                        ToastUtil.showToast(ex.getMessage());
                    }
                });


    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    @Override
    public void startRefresh() {
        walletList.clear();
        pageNum = 1;
        loadDatas();
        postMyBalance();

    }


    @Override
    public void startLoadMore() {
        pageNum++;
        loadDatas();
    }
    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {

        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.TEA__BINDING_32014:
               postMyBalance();
        }
        return  false;
    }
}
