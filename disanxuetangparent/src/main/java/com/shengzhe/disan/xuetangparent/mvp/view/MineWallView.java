package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Wallet;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.MyBalance;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class MineWallView extends BaseView {
    private List<Wallet> walletList = new ArrayList<>();
    private MyBalance myBalance;
    private SimpleAdapter adapter;
    private String startDate, endDate;
    private String selectData = "";
    private NiceDialog niceDialog;
    private FragmentManager fragmentManager;

    public MineWallView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    private IMineWallView iView;

    public void setIMineWallView(IMineWallView iView) {
        this.iView = iView;
    }

    public void setDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        iView.getApplyIthdrawView().setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        selectData = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
        iView.getMonthView().setRightText((calendar.get(Calendar.MONTH) + 1) + "月");
        startDate = (calendar.get(Calendar.YEAR) - 1) + "-" + (calendar.get(Calendar.MONTH) + 1);
        endDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);

        adapter = new SimpleAdapter<Wallet>(mContext, walletList, R.layout.item_mine_wallet) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Wallet data) {
                holder.setText(R.id.tv_order_number, "订单编号：" + data.getBillNo())
                        .setText(R.id.tv_mine_time, DateUtil.timeStampDate(data.getCreateTime(), "MM-dd HH:mm"))
                        .setVisible(R.id.tv_mine_line, walletList.indexOf(data) != walletList.size() - 1);
                holder.setText(R.id.tv_mine_class_name, data.getCourseName());
                long amount = data.getAmount();
                switch (data.getType()) {
                    case 1:
                        holder.setText(R.id.tv_mine_deduction, "退款: ");
                        holder.setText(R.id.tv_deduction, "+¥" + ArithUtils.roundLong(Math.abs(amount)));
                        holder.setTextColor(R.id.tv_deduction, UiUtils.getColor(R.color.color_ffae12));
                        holder.setImageResource(R.id.iv_minewall, R.mipmap.wallet_reimburse);
                        break;

                    case 3:
                        holder.setText(R.id.tv_mine_deduction, amount >= 0 ? "取消抵扣: " : "抵扣");
                        holder.setText(R.id.tv_deduction, (amount >= 0 ? "+¥" : "-¥") + ArithUtils.roundLong(Math.abs(amount)));
                        holder.setTextColor(R.id.tv_deduction, UiUtils.getColor(amount >= 0 ? R.color.color_ffae12 : R.color.color_d92b2b));
                        holder.setImageResource(R.id.iv_minewall, R.mipmap.wallet_deduction);
                        break;

                    case 5:
                        holder.setText(R.id.tv_mine_class_name, "提现完成");
                        holder.setText(R.id.tv_mine_deduction, "提现: ");
                        holder.setText(R.id.tv_deduction, "-¥" + ArithUtils.roundLong(Math.abs(amount)));
                        holder.setTextColor(R.id.tv_deduction, ContextCompat.getColor(mContext, R.color.color_d92b2b));
                        holder.setImageResource(R.id.iv_minewall, R.mipmap.wallet_deposit);
                        break;
                }
            }
        };
        iView.getRefreshCommonViewView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonViewView().setRefreshLoadMoreListener(listener);
        iView.getRefreshCommonViewView().setIsLoadMore(false);
    }

    public long getSelectData() throws ParseException {
        return new SimpleDateFormat("yyyy-MM").parse(selectData).getTime();
    }

    public void setWalletList(List<Wallet> subject) {
        walletList.clear();
        walletList.addAll(subject);
        if (walletList.isEmpty()) {
            iView.getRefreshCommonViewView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonViewView().setIsEmpty(false);
        }
        adapter.notifyDataSetChanged();
    }

    public void loadFinishDatas() {
        iView.getRefreshCommonViewView().finishRefresh();
        iView.getRefreshCommonViewView().finishLoadMore();
    }

    public void setBalance(MyBalance balance) {
        myBalance = balance;
        iView.getWithdrawalAmountView().setText("¥" + ArithUtils.roundDouble(myBalance.getAvailAmount()));
        iView.getAmountFreezingView().setText("¥" + ArithUtils.roundLong(myBalance.getLockAmount()));
        //可用余额提现
        iView.getApplyIthdrawView().setEnabled(myBalance.getAvailAmount() > 0);
        if (myBalance.getAvailAmount() <= 0) {
            iView.getApplyIthdrawView().setTextColor(UiUtils.getColor(R.color.color_dfdfdf));
        }
    }


    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    public MyBalance getMyBalance() {
        return myBalance;
    }

    public void notifyDatas() {
        iView.getRefreshCommonViewView().notifyData();
    }

    public void selectDate() {
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
                                iView.getMonthView().setRightText((select[0].equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))) ? "" : select[0] + "年") + select[1] + "月");
                                iView.getRefreshCommonViewView().notifyData();
                                colseDialog();
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
    }

    public interface IMineWallView {
        TextView getWithdrawalAmountView();

        TextView getWithdrawalView();

        TextView getAmountFreezingView();

        TextView getAmountView();

        TextView getApplyIthdrawView();

        CommonCrosswiseBar getMonthView();

        RefreshCommonView getRefreshCommonViewView();
    }

}
