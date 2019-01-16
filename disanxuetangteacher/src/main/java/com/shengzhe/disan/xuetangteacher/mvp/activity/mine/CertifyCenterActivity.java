package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.AuthenticationCoreBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.IdentityCardActivity;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/15.
 */

public class CertifyCenterActivity extends BaseActivity implements ConfirmDialog.ConfirmDialogListener{
    @BindView(R.id.ccb_certificate_name)
    CommonCrosswiseBar mName;
    @BindView(R.id.ccb_certificate_qualification)
    CommonCrosswiseBar mQualification;
    @BindView(R.id.ccb_certificate_education)
    CommonCrosswiseBar mEducation;
    @BindView(R.id.ccb_certificate_major)
    CommonCrosswiseBar mMajor;
    @BindView(R.id.ccb_certificate_reminder)
    TextView mReminder;
    private HttpService httpService;
    private AuthenticationCoreBean authenticahtionCore;
    private String Mobile ="400005666";
    @Override
    public void initData() {
        if (authenticahtionCore==null)
            authenticahtionCore = new AuthenticationCoreBean();
        httpService = Http.getHttpService();
        mReminder.setText(StringUtils.textFormatHtml("如果对认证进度有疑问，可拨打 <font color='#3a7bd5'>400-000-5666</font> 咨询"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        postAuthenticationCore();
    }

    private void postAuthenticationCore() {

        httpService.authenticationCore()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<AuthenticationCoreBean>(mContext, true) {
                    @Override
                    protected void onDone(AuthenticationCoreBean mAuthenticationCore) {
                        if (mAuthenticationCore==null)
                            return;
                        authenticahtionCore = mAuthenticationCore;
                        setAuthentication();
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });

    }

    /**
     * 设置状态
     * 0 未认证 1 审核中 2 已认证 3 已驳回
     */
    private void setAuthentication() {
        //实名认证状态
        switch (authenticahtionCore.getCardApprStatus()){
            case 0:
                mName.setRightText("未认证");
                mName.setRightTextColor(R.color.color_999999);
                break;
            case 1:
                mName.setRightText("审核中");
                mName.setRightTextColor(R.color.color_ffae12);
                break;
            case 2:
                mName.setRightText("已认证");
                mName.setRightTextColor(R.color.color_ff1d97ea);
                break;
            case 3:
                mName.setRightText("未通过");
                mName.setRightTextColor(R.color.color_d92b2b);
                break;
        }
        //教师资格认证状态
        switch (authenticahtionCore.getQtsStatus()){
            case 0:
                mQualification.setRightText("未认证");
                mQualification.setRightTextColor(R.color.color_999999);
                break;
            case 1:
                mQualification.setRightText("审核中");
                mQualification.setRightTextColor(R.color.color_ffae12);
                break;
            case 2:
                mQualification.setRightText("已认证");
                mQualification.setRightTextColor(R.color.color_ff1d97ea);
                break;
            case 3:
                mQualification.setRightText("未通过");
                mQualification.setRightTextColor(R.color.color_d92b2b);
                break;
        }
        //学历认证状态
        switch (authenticahtionCore.getQuaStatus()){
            case 0:
                mEducation.setRightText("未认证");
                mEducation.setRightTextColor(R.color.color_999999);
                break;
            case 1:
                mEducation.setRightText("审核中");
                mEducation.setRightTextColor(R.color.color_ffae12);
                break;
            case 2:
                mEducation.setRightText("已认证");
                mEducation.setRightTextColor(R.color.color_ff1d97ea);
                break;
            case 3:
                mEducation.setRightText("未通过");
                mEducation.setRightTextColor(R.color.color_d92b2b);
                break;
        }
        //专业资质状态认证
        switch (authenticahtionCore.getIpmpStatus()){
            case 0:
                mMajor.setRightText("未认证");
                mMajor.setRightTextColor(R.color.color_999999);
                break;
            case 1:
                mMajor.setRightText("审核中");
                mMajor.setRightTextColor(R.color.color_ffae12);
                break;
            case 2:
                mMajor.setRightText("已认证");
                mMajor.setRightTextColor(R.color.color_ff1d97ea);
                break;
            case 3:
                mMajor.setRightText("未通过");
                mMajor.setRightTextColor(R.color.color_d92b2b);
                break;
        }

    }
    private ConfirmDialog dialog;
    @Override
    public int setLayout() {
        return R.layout.activity_centifycenter;
    }
    @OnClick({R.id.common_bar_leftBtn,R.id.ccb_certificate_name,R.id.ccb_certificate_qualification,R.id.ccb_certificate_education,R.id.ccb_certificate_major,R.id.ccb_certificate_reminder})
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_certificate_reminder:
               //打电话
                if(dialog==null) {
                    dialog = ConfirmDialog.newInstance("", "您确定要拨打客服电话<br/><font color='#3a7bd5'>400-000-5666</font>？", "取消", "立即拨打");
                    dialog.setMessageGravity(Gravity.LEFT);
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(this);
                break;

            case R.id.ccb_certificate_name:
                //实名制认证
                startActivity(new Intent(mContext,IdentityCardActivity.class));
                break;

            case R.id.ccb_certificate_qualification:
                //教师资格证
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, CertificateActivity.class);
                intent.putExtra("title", "教师资格认证");
                intent.putExtra("CertifyUrl", authenticahtionCore.getTeachingCertifyUrl());
                intent.putExtra("Status", authenticahtionCore.getQtsStatus());
                startActivity(intent);
                break;

            case R.id.ccb_certificate_education:
                //学历
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, CertificateActivity.class);
                intent.putExtra("title", "学历认证");
                intent.putExtra("CertifyUrl", authenticahtionCore.getEduCertifyUrl());
                intent.putExtra("Status", authenticahtionCore.getQuaStatus());
                startActivity(intent);
                break;

            case R.id.ccb_certificate_major:
                //专业
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, CertificateActivity.class);
                intent.putExtra("title", "专业资质认证");
                intent.putExtra("CertifyUrl", authenticahtionCore.getProfessionaCertifyUrl());
                intent.putExtra("Status", authenticahtionCore.getIpmpStatus());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void dialogStatus(int id) {
        switch (id){
            case R.id.tv_dialog_ok:
                //确定
                SystemInfoUtil.callDialing(Mobile);
                break;
        }
    }
}
