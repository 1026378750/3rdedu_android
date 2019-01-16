package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.util.IDCardUtil;
import com.main.disanxuelib.util.ImageUtil;
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
import com.shengzhe.disan.xuetangteacher.bean.RealNameVerify;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MinesSistantActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 身份验证 on 2018/1/15.
 */

public class IdentityCardActivity extends BaseActivity implements IPhotoCall {
    @BindView(R.id.ccb_certificate_title)
    CommonCrosswiseBar ccbCertificateTitle;
    @BindView(R.id.et_identity_name)
    EditText etIdentityName;
    @BindView(R.id.et_identitycard_name)
    EditText etIdentitycardName;
    @BindView(R.id.iv_identity_front)
    ImageView ivIdentityFront;
    @BindView(R.id.tv_identity_front)
    TextView tvIdentityFront;
    @BindView(R.id.iv_identity_contrary)
    ImageView ivIdentityContrary;
    @BindView(R.id.tv_identity_contrary)
    TextView tvIdentityContrary;
    @BindView(R.id.iv_identity_take)
    ImageView ivIdentityTake;
    @BindView(R.id.tv_identity_take)
    TextView tvIdentityTake;
    @BindView(R.id.tv_certificate_confirm)
    CheckBox mConfirm;
    private int postion = 0;
    private HttpService httpService;

    @Override
    public void initData() {
        if (realNameAuthentication == null) {
            realNameAuthentication = new RealNameVerify();
        }
        httpService = Http.getHttpService();
        postCardApprData();
        mConfirm.setClickable(false);
        mConfirm.setChecked(false);
    }

    private RealNameVerify realNameAuthentication;

    //实名认证状态数据
    private void postCardApprData() {
        httpService.cardApprData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RealNameVerify>(mContext, false) {
                    @Override
                    protected void onDone(RealNameVerify verify) {
                        if (verify != null) {
                            realNameAuthentication = verify;
                            setAuthenticationCore();
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }

    private String CardFrontUrl = "",CardBackUrl="",CardUrl="";
    //设置实名认证状态数据
    private void setAuthenticationCore() {
        etIdentityName.setText(realNameAuthentication.getName());
        etIdentityName.requestFocus();
        etIdentityName.setSelection(realNameAuthentication.getName().length());
        etIdentitycardName.setText(realNameAuthentication.getCardNo());
        CardFrontUrl = realNameAuthentication.getCardFrontUrl();
        CardBackUrl = realNameAuthentication.getCardBackUrl();
        CardUrl = realNameAuthentication.getCardUrl();

        ImageUtil.loadImageViewLoding(mContext,CardFrontUrl , ivIdentityFront, R.mipmap.ic_certificate_front, R.mipmap.ic_certificate_front);
        int status = realNameAuthentication.getCardApprStatus();
        tvIdentityFront.setText(String.format("%s上传%s",status==1 || status ==2?"已":"请","身份证正面"));
        ImageUtil.loadImageViewLoding(mContext, CardBackUrl, ivIdentityContrary, R.mipmap.ic_certificate_front, R.mipmap.ic_certificate_front);
        tvIdentityContrary.setText(String.format("%s上传%s",status==1 || status ==2?"已":"请","身份证反面"));
        ImageUtil.loadImageViewLoding(mContext, CardUrl, ivIdentityTake, R.mipmap.ic_certificate_front, R.mipmap.ic_certificate_front);
        tvIdentityTake.setText(String.format("%s上传%s",status==1 || status ==2?"已":"请","手持身份证"));
        if (status == 1 || status == 2) {
            etIdentityName.setEnabled(false);
            etIdentitycardName.setEnabled(false);
        }else{
            mConfirm.setClickable(true);
            mConfirm.setChecked(true);
        }
        //0 未认证 1 审核中 2 已认证 3 已驳回
        mConfirm.setText(status==0?"提交认证":status==1?"审核中":status==2?"已认证":"重新认证");
    }

    @Override
    public int setLayout() {
        return R.layout.activity_identitycard;
    }

    private int width;
    private float ratios;
    @OnClick({R.id.common_bar_leftBtn, R.id.tv_certificate_confirm, R.id.iv_identity_front, R.id.iv_identity_contrary, R.id.iv_identity_take})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.iv_identity_front:
                postion = 1;
                if (realNameAuthentication.getCardApprStatus() == 1 || realNameAuthentication.getCardApprStatus() == 2)
                    return;
                width = SystemInfoUtil.getScreenWidth()<SystemInfoUtil.getScreenHeight()?SystemInfoUtil.getScreenWidth():SystemInfoUtil.getScreenHeight();
                ratios = (float) ivIdentityFront.getMeasuredWidth()/ivIdentityFront.getMeasuredHeight();
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE).setSuperRotate(true).setParam(width,(int)( width/ratios));
                break;

            case R.id.iv_identity_contrary:
                postion = 2;
                if (realNameAuthentication.getCardApprStatus() == 1 || realNameAuthentication.getCardApprStatus() == 2)
                    return;
                width = SystemInfoUtil.getScreenWidth()<SystemInfoUtil.getScreenHeight()?SystemInfoUtil.getScreenWidth():SystemInfoUtil.getScreenHeight();
                ratios = (float) ivIdentityContrary.getMeasuredWidth()/ivIdentityContrary.getMeasuredHeight();
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE).setSuperRotate(true).setParam(width,(int)( width/ratios));
                break;

            case R.id.iv_identity_take:
                postion = 3;
                if (realNameAuthentication.getCardApprStatus() == 1 || realNameAuthentication.getCardApprStatus() == 2)
                    return;
                width = SystemInfoUtil.getScreenWidth()<SystemInfoUtil.getScreenHeight()?SystemInfoUtil.getScreenWidth():SystemInfoUtil.getScreenHeight();
                ratios = (float) ivIdentityTake.getMeasuredWidth()/ ivIdentityTake.getMeasuredHeight();
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE).setSuperRotate(true).setParam(width,(int)( width/ratios));
                break;

            case R.id.tv_certificate_confirm:
                //提交认证
                if (TextUtils.isEmpty(etIdentityName.getText().toString())) {
                    nofifyShowMesg("请你填写真实姓名",etIdentityName);
                    return;
                }
                if (TextUtils.isEmpty(etIdentitycardName.getText().toString())) {
                    nofifyShowMesg("请你填写身份证号码",etIdentitycardName);
                    return;
                }

                if (!IDCardUtil.isIdcard(etIdentitycardName.getText().toString())) {
                    nofifyShowMesg("证件号码有误，请重新输入",etIdentitycardName);
                    return;
                }

                if (StringUtils.textIsEmpty(realNameAuthentication.getCardFrontUrl())) {
                    nofifyShowMesg("请上传身份证正面",null);
                    return;
                }

                if (StringUtils.textIsEmpty(realNameAuthentication.getCardBackUrl())) {
                    nofifyShowMesg("身份证反面",null);
                    return;
                }

                if (StringUtils.textIsEmpty(realNameAuthentication.getCardUrl())) {
                    nofifyShowMesg("手持身份证",null);
                    return;
                }
                postConfirm();
                break;
        }
    }

    private void postConfirm() {
        File file;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        byte[] data=new byte[0];
        if(StringUtils.textIsEmpty(realNameAuthentication.getCardFrontUrl())||realNameAuthentication.getCardFrontUrl().contains("http://")){
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("positiveFile", "positiveFile", imageBody);//imgfile 后台接收图片流的参数名
        }else {
             file = new File(realNameAuthentication.getCardFrontUrl());//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("positiveFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }

        if(StringUtils.textIsEmpty(realNameAuthentication.getCardBackUrl())||realNameAuthentication.getCardBackUrl().contains("http://")){
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("sideFile", "sideFile", imageBody);//imgfile 后台接收图片流的参数名
        }else{
            file = new File(realNameAuthentication.getCardBackUrl());//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("sideFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }

        if(StringUtils.textIsEmpty(realNameAuthentication.getCardUrl())||realNameAuthentication.getCardUrl().contains("http://")){
            RequestBody  imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("handFile", "handFile", imageBody);//imgfile 后台接收图片流的参数名
        }else{
            file = new File(realNameAuthentication.getCardUrl());//filePath 图片地址
            RequestBody  imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("handFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }



        Map<String, Object> map = new HashMap<>();
        map.put("name", etIdentityName.getText().toString());//真实姓名
        map.put("cardNo", etIdentitycardName.getText().toString());//身份证号码
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.realNameAuthentication(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String jsonObject) {
                        ToastUtil.showShort(jsonObject);
                        startPage();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                            ToastUtil.showShort(ex.getMessage());
                            startPage();
                        }
                    }
                });

    }
    private void startPage(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11038);
        EventBus.getDefault().post(bundle);

        HomeBean homeBean = SharedPreferencesManager.getHomeBean();
        homeBean.setRealNameAuthStatus(1);
        SharedPreferencesManager.setHomeBean(homeBean);
        BindingAssistant();
        finish();
    }
    private void BindingAssistant(){
        Intent  intent=new Intent();
        intent.setClass(this,MinesSistantActivity.class);
        intent.putExtra("haveSistant",1);
        startActivity(intent);
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        switch (postion) {
            case 1:
                realNameAuthentication.setCardFrontUrl(photoUrl);
                ImageUtil.loadImageViewLoding(mContext, photoUrl, ivIdentityFront, R.mipmap.default_error, R.mipmap.default_error);
                break;
            case 2:
                realNameAuthentication.setCardBackUrl(photoUrl);
                ImageUtil.loadImageViewLoding(mContext, photoUrl, ivIdentityContrary, R.mipmap.default_error, R.mipmap.default_error);
                break;
            case 3:
                realNameAuthentication.setCardUrl(photoUrl);
                ImageUtil.loadImageViewLoding(mContext, photoUrl, ivIdentityTake, R.mipmap.default_error, R.mipmap.default_error);
                break;
        }
        if (!StringUtils.textIsEmpty(this.realNameAuthentication.getCardFrontUrl())&&!StringUtils.textIsEmpty(this.realNameAuthentication.getCardBackUrl())&&!StringUtils.textIsEmpty(this.realNameAuthentication.getCardUrl())) {
            mConfirm.setClickable(true);
            mConfirm.setChecked(true);
        }
    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg ,final EditText currentEdit){
        ConfirmDialog dialog = ConfirmDialog.newInstance("", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        if(currentEdit==null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }

    private ConfirmDialog backDialog;
    @Override
    public void onBackPressed() {
        if(realNameAuthentication.getName().equals(etIdentityName.getText().toString())&&
                realNameAuthentication.getCardNo().equals(etIdentitycardName.getText().toString())&&
                CardFrontUrl.equals(realNameAuthentication.getCardFrontUrl())&&
                CardBackUrl.equals(realNameAuthentication.getCardBackUrl())&&
                CardUrl.equals(realNameAuthentication.getCardUrl())
                ){
            finish();
            return;
        }
        if(backDialog==null) {
            backDialog = ConfirmDialog.newInstance("", "您还没有保存，确定要退出？", "取消", "确定");
        }
        backDialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        backDialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

            @Override
            public void dialogStatus(int id) {
                switch (id){
                    case R.id.tv_dialog_ok:
                        colseDialog();
                        finish();
                        break;
                }
            }
        });
    }

    private void colseDialog() {
        if (backDialog != null && backDialog.isVisible()) {
            backDialog.dismiss();
        }
    }

}
