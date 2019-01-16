package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*****
 * 银行卡列表
 */
public class BankCardActivity extends BaseActivity {
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
    private String Mobile = "400005666";
    private MyBankCardBean myBankCard = new MyBankCardBean();
    private  String bankNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvCcbAddBankReminder.setText(StringUtils.textFormatHtml("3. 如有任何疑问，请拨打客服电话 <font color='#3a7bd5'>400-000-5666</font>"));
         bankNum = getIntent().getStringExtra("bankNum");
        if (!TextUtils.isEmpty(bankNum)&& !(bankNum.equals("0"))) {
            postMyBankCard();
            ccbDollarTitle.setRightText("更换银行卡");
            ccbDollarTitle.setTitleText("我的银行卡");
        }else {
            ccbDollarTitle.setRightText("");
            ccbDollarTitle.setTitleText("添加银行卡");
        }


    }


    /**
     * 我的银行卡
     */
    private void postMyBankCard() {

        HttpService httpService = Http.getHttpService();
        httpService.myBankCard()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBankCardBean>(mContext, true) {
                    @Override
                    protected void onDone(MyBankCardBean _myBankCardBean) {
                        myBankCard = _myBankCardBean;
                        if (!TextUtils.isEmpty(myBankCard.getBankTypeName())) {
                            tvAddBankType.setText(myBankCard.getBankTypeName());
                            String bankNum = String.valueOf(myBankCard.getBankNum());
                            ccbDollarTitle.setRightText("更换银行卡");
                            ccbDollarTitle.setTitleText("我的银行卡");
                            tvAddBankNumber.setText("**** **** **** " + bankNum.substring(bankNum.length() - 4, bankNum.length()));
                            tvAddBankName.setText(TextUtils.isEmpty(myBankCard.getBankMan())?"":myBankCard.getBankMan().substring(0,1)+"**");
                            switchBank(myBankCard.getBankType());
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }

    /**
     * 选择银行类型
     *
     * @param bankType
     */
    private void switchBank(int bankType) {
        switch (bankType) {
            case 1:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_icbc);
                break;
            case 2:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_blue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_construction);
                break;
            case 3:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepblue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_agriculture);
                break;
            case 4:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_china);
                break;
            case 5:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_attract);
                break;
            case 6:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_blue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_traffic);
                break;
            case 7:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_canton_card);
                break;
            case 8:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_citic);
                break;
            case 9:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_brighten);
                break;
            case 10:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepblue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_society);
                break;
            case 11:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_safeness);
                break;
            case 12:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_huaxia);
                break;
            case 13:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_blue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_xye);
            case 14:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_blue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_spdb);
                break;
            case 15:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepblue);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_dawk);
                break;
            case 16:
                rlAddBankCard.setBackgroundResource(R.drawable.bank_bgcolor_deepred);
                ivBankImage.setBackgroundResource(R.mipmap.bank_type_asia);
                break;
        }

    }

    @Override
    public void initData() {

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
                ConfirmDialog dialogs=null;
                //打电话
                if (dialogs == null) {
                    dialogs = ConfirmDialog.newInstance("", "您确定要拨打客服电话<br/><font color='#3a7bd5'>400-000-5666</font>？", "取消", "立即拨打");
                }
                dialogs.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialogs.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                    @Override
                    public void dialogStatus(int id) {
                        switch (id) {
                            case R.id.tv_dialog_ok:
                                SystemInfoUtil.callDialing(Mobile);
                                break;
                        }
                    }
                });
                break;
            case R.id.common_bar_leftBtn:
                if(myBankCard!=null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.TEA__BINDING_32014);
                    EventBus.getDefault().post(bundle);
                    onBackPressed();
                }
                break;
            case R.id.common_bar_rightBtn:
                if (!TextUtils.isEmpty(myBankCard.getBankTypeName())) {
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
                if(!judgeStatus()) return;
                //有没有绑定银行卡
                if (TextUtils.isEmpty(myBankCard.getBankTypeName())) {
                    AddBankCard();
                }
                break;
        }
    }


    /**
     *  判断状态
     */
    public boolean judgeStatus(){
        //消息关闭
         HomeBean homebean=new HomeBean();
        homebean =SharedPreferencesManager.getHomeBean();

       /* if(homebean.getHomeStatus()==0){
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,StringUtils.btn_is_next);
           startActivity(intent);
            return false;
        }
*/
        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 0:
            case 1:
            case 3:
                //实名认证
                startActivity(new Intent(mContext,IdentityCardActivity.class));
                return false;
        }


        return true;
    }

    private void AddBankCard() {
        Intent intent = new Intent();
        intent.setClass(this, AddBankCardActivity.class);
        intent.putExtra("myBankCardBean", myBankCard);
        startActivity(intent);

    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {

        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.TEA__BINDING_32012:
                postMyBankCard();
                break;
        }
        return false;
    }
}
