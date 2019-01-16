package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

/**
 * Created by Administrator on 2018/4/20.
 */

public class BankCardView extends BaseView {
    private MyBankCardBean myBankCard = new MyBankCardBean();
    private String bankNum = "";
    private FragmentManager fragmentManager;
    public BankCardView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    private IBankCardView iView;
    public void setIBankCardView(IBankCardView iView){
        this.iView = iView;
    }

    public void setDatas(String bankNum) {
        iView.getAddBankReminderView().setText(StringUtils.textFormatHtml("3. 如有任何疑问，请拨打客服电话 <font color='#ffae12'>400-000-5666</font>"));
        if (!TextUtils.isEmpty(bankNum)) {
            iView.getDollarTitleView().setRightText("更换银行卡");
            iView.getDollarTitleView().setTitleText("我的银行卡");
        }else {
            iView.getDollarTitleView().setRightText("");
            iView.getDollarTitleView().setTitleText("添加银行卡");
        }
    }

    public void setMyBankMesg(MyBankCardBean myBankCard) {
        this.myBankCard = myBankCard;
        if (!TextUtils.isEmpty(myBankCard.getBankTypeName())) {
            iView.getAddBankTypeView().setText(myBankCard.getBankTypeName());
            String bankNum = String.valueOf(myBankCard.getBankNum());
            iView.getDollarTitleView().setRightText("更换银行卡");
            iView.getDollarTitleView().setTitleText("我的银行卡");
            iView.getAddBankNumberView().setText("**** **** **** " + bankNum.substring(bankNum.length() - 4, bankNum.length()));
            iView.getAddBankNameView().setText(TextUtils.isEmpty(myBankCard.getBankMan())?"":myBankCard.getBankMan().substring(0,1)+"**");
            switchBank(myBankCard.getBankType());
        }
    }

    /**
     * 选择银行类型
     *
     * @param bankType
     */
    private void switchBank(int bankType) {

        switch (bankType) {
            case 1:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_icbc);
                break;

            case 2:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_blue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_construction);
                break;

            case 3:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepblue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_agriculture);
                break;

            case 4:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_china);
                break;

            case 5:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_attract);
                break;

            case 6:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_blue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_traffic);
                break;

            case 7:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_canton_card);
                break;

            case 8:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_citic);
                break;

            case 9:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_brighten);
                break;

            case 10:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepblue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_society);
                break;

            case 11:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_safeness);
                break;

            case 12:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_huaxia);
                break;
            case 13:

                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_blue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_xye);
            case 14:

                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_blue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_spdb);
                break;

            case 15:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepblue);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_dawk);
                break;

            case 16:
                iView.getAddBankCardView().setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                iView.getBankImageView().setBackgroundResource(R.mipmap.bank_type_asia);
                break;
        }

    }

    public void setBankAcResult(MyBankCardBean bankAcResult) {
        iView.getDollarTitleView().setRightText("更换银行卡");
        iView.getDollarTitleView().setTitleText("我的银行卡");
        myBankCard.setBankType(myBankCard.getBankType());
        myBankCard.setBankTypeName(bankAcResult.getBankTypeName());
        myBankCard.setBankNum(Long.parseLong(bankNum));
        myBankCard.setBankCity(bankAcResult.getBankCity());
        myBankCard.setBankProvince(bankAcResult.getBankProvince());
        myBankCard.setBankSub(bankAcResult.getBankSub());
        myBankCard.setBankMan(myBankCard.getOpenMan());
    }

    public MyBankCardBean getBankCard() {
        return myBankCard;
    }

    public void callPhone(final String mobile) {
        ConfirmDialog dialogs = ConfirmDialog.newInstance("", "您确定要拨打客服电话<br/><font color='#3a7bd5'>400-000-5666</font>？", "取消", "立即拨打");
        dialogs.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(fragmentManager);
        dialogs.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        SystemInfoUtil.callDialing(mobile);
                        break;
                }
            }
        });
    }

    public interface IBankCardView{
        TextView getAddBankReminderView();
        TextView getAddBankNameView();
        TextView getAddBankTypeView();
        TextView getAddBankNumberView();
        RelativeLayout getAddBankCardView();
        TextView getTextView();
        ImageView getBankImageView();
        CommonCrosswiseBar getDollarTitleView();
    }
}
