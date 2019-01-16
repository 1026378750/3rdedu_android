package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.BankCardView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 银行卡列表
 */
public class BankCardActivity extends BaseActivity implements BankCardView.IBankCardView {
    @BindView(R.id.tv_ccb_add_bank_reminder)
    TextView tvCcbAddBankReminder;
    @BindView(R.id.tv_add_bank_name)
    TextView tvAddBankName;
    @BindView(R.id.tv_add_bank_type)
    TextView tvAddBankType;
    @BindView(R.id.tv_add_bank_number)
    TextView tvAddBankNumber;
    @BindView(R.id.rl_add_bank_card)
    RelativeLayout rlAddBankCard;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.iv_bank_image)
    ImageView ivBankImage;
    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar ccbDollarTitle;

    private UserPresenter presenter;

    private String Mobile = "400005666";

    @Override
    public void initData() {
        String bankNum = getIntent().getStringExtra("bankNum");
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initBankUi(getSupportFragmentManager());
        presenter.setBankDatas(bankNum);
        if (!StringUtils.textIsEmpty(bankNum)){
            presenter.loadMyBankCard();
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_bank_card;
    }

    private ConfirmDialog dialog;

    @OnClick({R.id.tv_ccb_add_bank_reminder,R.id.common_bar_leftBtn, R.id.common_bar_rightBtn, R.id.rl_add_bank_card})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ccb_add_bank_reminder:
                presenter.callPhone(Mobile);
                break;
            case R.id.common_bar_leftBtn:
                if(presenter.getBankCard()!=null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.TEA__BINDING_32014);
                    EventBus.getDefault().post(bundle);
                    onBackPressed();
                }
                break;
            case R.id.common_bar_rightBtn:
                if (!TextUtils.isEmpty(presenter.getBankCard().getBankTypeName())) {
                    dialog = ConfirmDialog.newInstance("", "您确定要更换银行卡？", "取消", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    AddBankCard();
                                    break;
                            }
                        }
                    });
                }
                break;
            case R.id.rl_add_bank_card:
                //有没有绑定银行卡
                if (TextUtils.isEmpty(presenter.getBankCard().getBankTypeName())) {
                    AddBankCard();
                }
                break;
        }
    }

    private void AddBankCard() {
        Intent intent = new Intent();
        intent.setClass(this, AddBankCardActivity.class);
        intent.putExtra("myBankCardBean", presenter.getBankCard());
        startActivity(intent);
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {

        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.TEA__BINDING_32012:
                presenter.loadMyBankCard();
                break;
        }
        return false;
    }

    @Override
    public TextView getAddBankReminderView() {
        return tvCcbAddBankReminder;
    }

    @Override
    public TextView getAddBankNameView() {
        return tvAddBankName;
    }

    @Override
    public TextView getAddBankTypeView() {
        return tvAddBankType;
    }

    @Override
    public TextView getAddBankNumberView() {
        return tvAddBankNumber;
    }

    @Override
    public RelativeLayout getAddBankCardView() {
        return rlAddBankCard;
    }

    @Override
    public TextView getTextView() {
        return textView;
    }

    @Override
    public ImageView getBankImageView() {
        return ivBankImage;
    }

    @Override
    public CommonCrosswiseBar getDollarTitleView() {
        return ccbDollarTitle;
    }
}