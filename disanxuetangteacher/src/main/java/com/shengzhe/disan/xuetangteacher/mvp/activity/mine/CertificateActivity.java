package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.util.FileUtil;
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
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 认证中心 on 2018/1/15.
 */

public class CertificateActivity extends BaseActivity implements IPhotoCall {
    @BindView(R.id.ccb_certificate_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.iv_certificate_image)
    ImageView mImage;
    @BindView(R.id.tv_certificate_content)
    TextView mContent;
    @BindView(R.id.tv_certificate_confirm)
    CheckBox mConfirm;

    private String title="";
    @Override
    public void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        mTitle.setTitleText(title);
        photoUrl = intent.getStringExtra("CertifyUrl");
        ImageUtil.loadImageViewLoding(mContext,photoUrl , mImage, R.mipmap.ic_certificate_front, R.mipmap.ic_certificate_front);
        int status = intent.getIntExtra("Status",-1);//0 未认证 1 审核中 2 已认证 3 已驳回
        mContent.setText(String.format("%s上传%s",status==1 || status ==2?"已":"请",title));
        mConfirm.setClickable(false);
        mConfirm.setChecked(false);
        if(status==1 || status==2){
            mImage.setEnabled(false);
            mImage.setClickable(false);
        }
        if (status == 1){
            mConfirm.setText("审核中");
        }else if(status == 2){
            mConfirm.setText("已认证");
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_certificate;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.iv_certificate_image,R.id.tv_certificate_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_certificate_confirm:
                if(StringUtils.textIsEmpty(photoUrl)||photoUrl.contains("http://")){
                nofifyShowMesg("请选择"+title+"图片");
                return;
            }
                postConfirm();
                break;

            case R.id.iv_certificate_image:
                //图片选择
                int width = SystemInfoUtil.getScreenWidth()<SystemInfoUtil.getScreenHeight()?SystemInfoUtil.getScreenWidth():SystemInfoUtil.getScreenHeight();
                float ratios = (float) mImage.getMeasuredWidth()/mImage.getMeasuredHeight();
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE).setSuperRotate(true).setParam(width,(int)( width/ratios));
                break;
        }
    }

    private File getFile(){
        File file =FileUtil.getNewFile("");////filePath 图片地址
      return file;
    }
    private RequestBody getImageBody(){
        byte[] data= new byte[0];
        RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
        return imageBody;
    }
    /**
     * 提交认证
     */
    private void postConfirm() {
        HttpService httpService = Http.getHttpService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        File file = new File(photoUrl);//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
        switch (title){
            case "学历认证":
                builder.addFormDataPart("eduCertifyFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("teachingCertifyFile", "teachingCertifyFile", getImageBody());//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("professionaCertifyFile", "professionaCertifyFile", getImageBody());//imgfile 后台接收图片流的参数名

                break;
            case "教师资格认证":
                builder.addFormDataPart("teachingCertifyFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("eduCertifyFile", "eduCertifyFile", getImageBody());
                builder.addFormDataPart("professionaCertifyFile", "professionaCertifyFile", getImageBody());
                break;
            case "专业资质认证":
                builder.addFormDataPart("professionaCertifyFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("eduCertifyFile", "eduCertifyFile", getImageBody());
                builder.addFormDataPart("teachingCertifyFile", "teachingCertifyFile", getImageBody());
                break;
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.authenticationOther(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String jsonObject) {
                        finish();
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                            finish();
                        }

                    }
                });
    }

    private String photoUrl = "";
    @Override
    public void onPhotoResult(String photoUrl) {
        this.photoUrl = photoUrl;
        if (!StringUtils.textIsEmpty(this.photoUrl)) {
            mConfirm.setClickable(true);
            mConfirm.setChecked(true);
        }
        ImageUtil.loadImageViewLoding(mContext, photoUrl, mImage, R.mipmap.default_iamge, R.mipmap.default_iamge);
    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg){
       ConfirmDialog.newInstance("", mesg, "", "确定").setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private ConfirmDialog backDialog;
    @Override
    public void onBackPressed() {
        if(StringUtils.textIsEmpty(photoUrl)||photoUrl.contains("http://")){
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
