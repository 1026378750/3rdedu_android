package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.UserAssistantMode;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的助教
 */
public class MinesSistantActivity extends BaseActivity implements ConfirmDialog.ConfirmDialogListener {

    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar ccbDollarTitle;
    @BindView(R.id.iv_sisant_head)
    ImageView ivSisantHead;
    @BindView(R.id.iv_sisant_name)
    TextView ivSisantName;
    @BindView(R.id.tv_mine_mobile)
    TextView tvMineMobile;
    @BindView(R.id.tv_sistant_content)
    TextView tvSistantContent;
    private ConfirmDialog dialog;
    private UserAssistantMode mUserAssistantMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

        if(mUserAssistantMode==null){
            mUserAssistantMode=new UserAssistantMode();
        }

        if((SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getAssistantId()==0)){
            tvSistantContent.setText(StringUtils.textFormatHtml("您必须绑定了助教方可正式开课，<br/> 您可以自行绑定认识的助教或者 <font color='#1d97ea'>申请分配助教。</font>"));
            tvMineMobile.setText("绑定助教");
            tvSistantContent.setEnabled(true);
            tvSistantContent.setClickable(true);
        }else {
            ivSisantName.setText("");
            tvMineMobile.setText("绑定助教");
            postMinesSitant();
            tvSistantContent.setEnabled(false);
            tvSistantContent.setClickable(false);
        }

    }

    @Override
    public int setLayout() {
        return R.layout.activity_mines_sistant;
    }
    @OnClick({R.id.common_bar_leftBtn, R.id.tv_mine_mobile, R.id.common_bar_rightBtn,R.id.tv_sistant_content})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_mine_mobile:
                //助教电话
                if (TextUtils.isEmpty(mUserAssistantMode.name)) {
                    startActivity(new Intent(this, BindingAssistantActivity.class));
                } else {
                    callPhone();
                }
                break;

            case R.id.tv_sistant_content:
                ConfirmDialog.newInstance("", "您的绑定助教申请提交成功！我们将尽快为您绑定助教。", "", "确定").setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                break;
        }

    }

    private void callPhone() {
        String mesg = "您确定要拨打";
        if (TextUtils.isEmpty(mUserAssistantMode.mobile)) {
            return;
        }
        mesg += "助教电话\n<font color='#1d97ea'>" + mUserAssistantMode.mobile + "</font>？";
        if (dialog == null) {
            dialog = ConfirmDialog.newInstance("", mesg, "取消", "立即拨打");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(this);
    }

    public void postMinesSitant() {
        HttpService httpService = Http.getHttpService();
        httpService.userAssistant()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<UserAssistantMode>(mContext, true) {
                    @Override
                    protected void onDone(UserAssistantMode userAssistantMode) {
                        mUserAssistantMode = userAssistantMode;
                        if (mUserAssistantMode != null) {
                            tvMineMobile.setText(TextUtils.isEmpty(mUserAssistantMode.mobile) == true ? "绑定助教" : "联系助教");
                            ivSisantName.setText(TextUtils.isEmpty(mUserAssistantMode.name) == true ? "绑定助教" : mUserAssistantMode.name);
                            ImageUtil.loadCircleImageView(mContext, mUserAssistantMode.photoUrl, ivSisantHead, R.mipmap.ic_personal_avatar);
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });
    }

    @Override
    public void dialogStatus(int id) {
        switch (id) {
            case R.id.tv_dialog_ok:
                //确定
                SystemInfoUtil.callDialing(mUserAssistantMode.mobile);
                break;
        }
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        if (mUserAssistantMode == null)
            mUserAssistantMode = new UserAssistantMode();
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.TEA__BINDING_32011:
                mUserAssistantMode = bundle.getParcelable(StringUtils.EVENT_DATA);
                tvMineMobile.setText(TextUtils.isEmpty(mUserAssistantMode.mobile) == true ? "绑定助教" : "联系助教");
                ivSisantName.setText(TextUtils.isEmpty(mUserAssistantMode.name) == true ? "绑定助教" : mUserAssistantMode.name);
                ImageUtil.loadCircleImageView(mContext, mUserAssistantMode.photoUrl, ivSisantHead, R.mipmap.ic_personal_avatar);
                break;
        }
        return false;
    }

}
