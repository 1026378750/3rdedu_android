package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import com.google.gson.Gson;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.IdentityCardActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.OpenCityActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.StartClassActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.SubjectSelectActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherExperienceActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.main.disanxuelib.bean.CityBean;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.bean.PersonalDataBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
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
 * Created by Administrator on 2018/1/15.
 */

public class MineCenterActivity extends BaseActivity {
    @BindView(R.id.ccb_center_subject)
    CommonCrosswiseBar mSubjectLayout;
    @BindView(R.id.ccb_center_userbase)
    CommonCrosswiseBar mCenterUserbase;
    @BindView(R.id.ccb_center_age)
    CommonCrosswiseBar mCenterAge;
    @BindView(R.id.ccb_center_school)
    CommonCrosswiseBar mCenterSchool;
    @BindView(R.id.ccb_center_introduce)
    CommonCrosswiseBar mCenterIntroduce;
    @BindView(R.id.ccb_center_education)
    CommonCrosswiseBar mCenterEducation;
    @BindView(R.id.tv_center_comfirm)
    CheckBox mConfirm;
    @BindView(R.id.ccb_center_city)
    CommonCrosswiseBar ccbCenterCity;
    @BindView(R.id.ccb_mine_title)
    CommonCrosswiseBar ccbMineTitle;

    private CommonCrosswiseBar commonText;
    private Subject mSubject;
    private PersonalDataBean personalDataBean;
    private SystemPersimManage manage = null;
    private String oldCity = "";

    @Override
    public void initData() {
        isChangeDate = false;
        mConfirm.setClickable(false);
        mConfirm.setChecked(false);
        String from = getIntent().getStringExtra(StringUtils.ACTIVITY_DATA);
        if (from.equals(StringUtils.btn_is_next))
            mConfirm.setText("下一步（1/2）");
        else
            mConfirm.setText("保存");

        personalDataBean = SharedPreferencesManager.getPersonaInfo();
            if(ConstantUrl.CLIEN_Center){
                ccbMineTitle.setTitleText("完善资料");
            }else {
                ccbMineTitle.setTitleText("个人资料");
            }

        if (!StringUtils.textIsEmpty(personalDataBean.getPhotoUrl())) {
            ImageUtil.setItemRoundImageViewOnlyDisplay(mCenterUserbase.getRightImage(), personalDataBean.getPhotoUrl());
            mCenterUserbase.setLeftButton(personalDataBean != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
        }
        if (!StringUtils.textIsEmpty(personalDataBean.getCityName())) {
            if (mCity == null)
                mCity = new CityBean();
            mCity.setCityCode(personalDataBean.getCity());
            mCity.setCityName(personalDataBean.getCityName());
            oldCity = personalDataBean.getCityName();
            ccbCenterCity.setRightText(personalDataBean.getCityName());
            ccbCenterCity.setLeftButton(personalDataBean != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
            ccbCenterCity.setRightButton(R.mipmap.ic_white_right_arrow);
            ccbCenterCity.setRightButtonVisibility();
            ccbCenterCity.setEnabled(false);
            ccbCenterCity.setClickable(false);
        }

        if (!StringUtils.textIsEmpty(personalDataBean.getSubjectName())) {
            mSubjectLayout.setRightText(personalDataBean.getSubjectName());
            mSubjectLayout.setLeftButton(personalDataBean.getSubjectName() != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
            mSubjectLayout.setRightButton(R.mipmap.ic_white_right_arrow);
            mSubjectLayout.setRightButtonVisibility();
            mSubjectLayout.setEnabled(false);
            mSubjectLayout.setClickable(false);
        }

        if (!StringUtils.textIsEmpty(personalDataBean.getGeaduateSchool())) {
            mCenterSchool.setRightText(personalDataBean.getGeaduateSchool());
            mCenterSchool.setLeftButton(personalDataBean.getGeaduateSchool() != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
        }
        if (personalDataBean.getTeachingAge() != 0) {
            mCenterAge.setRightText(personalDataBean.getTeachingAge() + "");
            mCenterAge.setLeftButton("" + personalDataBean.getTeachingAge() != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
        }
        if (!StringUtils.textIsEmpty(personalDataBean.getPersonalResume())) {
            mCenterIntroduce.setRightText(personalDataBean.getPersonalResume());
            mCenterIntroduce.setLeftButton(personalDataBean.getPersonalResume() != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
        }
        if (personalDataBean.getTeachingExperienceNum() > 0) {
            mCenterEducation.setRightText(personalDataBean.getTeachingExperienceNum() + "条");
        }

    }

    @Override
    public int setLayout() {
        return R.layout.activity_minecenter;
    }

    private CityBean mCity;

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_center_city, R.id.ccb_center_userbase, R.id.ccb_center_subject, R.id.ccb_center_age, R.id.ccb_center_school, R.id.ccb_center_introduce, R.id.ccb_center_education, R.id.tv_center_comfirm})
    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_center_userbase:
                //基本信息
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, UserBaseActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, personalDataBean);
                startActivity(intent);
                break;

            case R.id.ccb_center_city:
                //地址选择
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, OpenCityActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, mCity);
                startActivity(intent);
                break;

            case R.id.ccb_center_subject:
                //科目
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, SubjectSelectActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, personalDataBean);
                startActivity(intent);
                break;

            case R.id.ccb_center_age:
                //教龄
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "教龄");
                intent.putExtra("hintText", "请输入教龄1~99");
                intent.putExtra("text", commonText.getRightText());
                startActivity(intent);
                break;

            case R.id.ccb_center_school:
                //毕业院校
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "毕业院校");
                intent.putExtra("hintText", "请输入毕业院校");
                intent.putExtra("text", commonText.getRightText());
                intent.putExtra("edu", personalDataBean.getEdu());
                intent.putExtra("profession", personalDataBean.getProfession());
                startActivity(intent);
                break;

            case R.id.ccb_center_introduce:
                //简介
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "个人简介");
                intent.putExtra("hintText", "介绍一下自己吧\n");
                intent.putExtra("text", commonText.getRightText());
                startActivity(intent);
                break;

            case R.id.ccb_center_education:
                //经历
                if (personalDataBean.getTeachingAge() == 0) {
                    nofifyShowMesg("请先填写教龄");
                    return;
                }
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, TeacherExperienceActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, personalDataBean.getTeachingAge());
                startActivity(intent);
                break;

            case R.id.tv_center_comfirm:
                //提交
                if (personalDataBean == null) {
                    nofifyShowMesg("昵称和性别为必填项");
                    return;
                }

                if (StringUtils.textIsEmpty(personalDataBean.getNickName())) {
                    nofifyShowMesg("请填写昵称");
                    return;
                }

                if (personalDataBean.getNickName().length() < 1 || personalDataBean.getNickName().length() > 15) {
                    nofifyShowMesg("昵称最多填15个字");
                    return;
                }

                if (personalDataBean.getSex() == -1) {
                    nofifyShowMesg("请填写性别");
                    return;
                }
                if (StringUtils.textIsEmpty(personalDataBean.getPhotoUrl()) || personalDataBean.getPhotoUrl().contains("http")) {
                    //无序上传头像
                    if(manage==null)
                        manage = new SystemPersimManage(mContext){
                            @Override
                            public void resultPerm(boolean isCan, int requestCode) {
                                if (isCan){
                                    postSaveData();
                                }
                            }
                        };
                    manage.CheckedFile();
                }else{
                    postSaveData();
                }
                break;
        }
    }

    //保存用户信息
    private void postSaveData() {
        HttpService httpService = Http.getHttpService();
        MultipartBody.Builder builder = personalDataBean.getSaveDate(oldCity);
        File file;
        Map<String, Object> map = new HashMap<>();

        //城市修改，必进开课设置
        if (!StringUtils.textIsEmpty(mCity.getCityName()) && (!(oldCity.equals(mCity.getCityName())))) {
            map = personalDataBean.getSaveDate();
        }
        if (StringUtils.textIsEmpty(personalDataBean.getPhotoUrl()) || personalDataBean.getPhotoUrl().contains("http")) {
            //file = FileUtil.getNewFile("");//filePath 图片地址
            byte[] data = new byte[0];
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("uploadFile", "uploadFile", imageBody);//imgfile 后台接收图片流的参数名
            //map.put("uploadFile","");
        } else {
            file = new File(personalDataBean.getPhotoUrl());//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("uploadFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
            // map.put("uploadFile",personalDataBean.getPhotoUrl());
        }
        Gson gson = new Gson();
         ConstantUrl.CLIEN_Info=2;
        final String postData = gson.toJson(map);
        if (!StringUtils.textIsEmpty(mCity.getCityName()) && (!(oldCity.equals(mCity.getCityName())) && (!StringUtils.textIsEmpty(oldCity)))) {
            startClassActivity(postData);
        } else {
            List<MultipartBody.Part> parts = builder.build().parts();
            httpService.savePersonalData(parts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AbsAPICallback<String>(mContext, true) {
                        @Override
                        protected void onDone(String jsonObject) {
                            startCenter();
                        }

                        @Override
                        public void onResultError(ResultException ex) {
                            if(ex.getErrCode()==222222){
                                startCenter();
                            }else {
                                mConfirm.setClickable(false);
                                mConfirm.setChecked(false);
                                ToastUtil.showShort(ex.getMessage());
                            }
                        }
                    });
        }
    }

    private void startCenter(){
        ConstantUrl.CLIEN_Info = 1;
        setUser();
        reloadMainDatas();
        if (!mConfirm.getText().toString().toString().equals("保存")) {//区实名认证
            startActivity(new Intent(mContext, IdentityCardActivity.class));
        }
        finish();
    }

    private void setUser(){
        HomeBean homeBean = SharedPreferencesManager.getHomeBean();
        homeBean.setHomeStatus(1);
        SharedPreferencesManager.setHomeBean(homeBean);
    }

    /**
     * @param data
     */
    private void startClassActivity(final String data) {
        dialog = ConfirmDialog.newInstance("", "您修改了城市信息,需要对应修改您的授课地址,方可提交保存。", "取消", "继续");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        Intent intent = new Intent();
                        intent.setClass(MineCenterActivity.this, StartClassActivity.class);
                        intent.putExtra("postData", data);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    private boolean isChangeDate = false;

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        if (personalDataBean == null)
            personalDataBean = new PersonalDataBean();
        switch (bundle.getInt(StringUtils.EVENT_ID)) {

            case IntegerUtil.EVENT_ID_11017:
                //科目改变
                isChangeDate = true;
                mSubject = bundle.getParcelable(StringUtils.EVENT_DATA);
                mSubjectLayout.setRightText(mSubject.getSubjectName());
                personalDataBean.setSubjectId(mSubject.getSubjectId());
                personalDataBean.setSubjectName(mSubject.getSubjectName());
                mSubjectLayout.setLeftButton(mSubject != null ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
                setNotifyBtn();
                break;

            case IntegerUtil.EVENT_ID_11025://城市修改
                isChangeDate = true;
                CityBean city = bundle.getParcelable(StringUtils.EVENT_DATA);
                mCity = city;
                personalDataBean.setCityName(mCity.getCityName());
                personalDataBean.setCity(mCity.cityCode);
                ccbCenterCity.setRightText(personalDataBean.getCityName());
                SharedPreferencesManager.setUserCity(mCity);
                ccbCenterCity.setLeftButton(TextUtils.isEmpty(personalDataBean.getCityName())==true ? R.drawable.ic_tick_unchecked:R.drawable.ic_tick_checked );
                setNotifyBtn();
                break;

            case IntegerUtil.EVENT_ID_11030:
                //得到基本信息
                isChangeDate = true;
                personalDataBean = bundle.getParcelable(StringUtils.EVENT_DATA);
                if (personalDataBean == null)
                    return false;
                if (!StringUtils.textIsEmpty(personalDataBean.getPhotoUrl()))
                    ImageUtil.setItemRoundImageViewOnlyDisplay(mCenterUserbase.getRightImage(), personalDataBean.getPhotoUrl());
                mCenterUserbase.setLeftButton(personalDataBean != null && !StringUtils.textIsEmpty(personalDataBean.getPhotoUrl()) && !StringUtils.textIsEmpty(personalDataBean.getNickName()) && personalDataBean.getSex() != -1 ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
                setNotifyBtn();
                break;

            case IntegerUtil.EVENT_ID_11033:
                //毕业院校
                isChangeDate = true;
                String geaduateSchool = bundle.getString("geaduateSchool");
                personalDataBean.setGeaduateSchool(geaduateSchool);
                personalDataBean.setProfession(bundle.getString("profession"));
                personalDataBean.setEdu(bundle.getInt("postion"));
                mCenterSchool.setRightText(geaduateSchool);
                mCenterSchool.setLeftButton(!StringUtils.textIsEmpty(geaduateSchool) ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
                setNotifyBtn();
                break;

            case IntegerUtil.EVENT_ID_11034:
                //教龄
                isChangeDate = true;
                String teachingAge = bundle.getString("teachingAge");
                personalDataBean.setTeachingAge(Integer.parseInt(teachingAge));
                mCenterAge.setRightText(teachingAge);
                mCenterAge.setLeftButton(!StringUtils.textIsEmpty(teachingAge) ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
                setNotifyBtn();
                break;

            case IntegerUtil.EVENT_ID_11032:
                //个人简介
                isChangeDate = false;
                String personalResume = bundle.getString("personalResume");
                personalDataBean.setPersonalResume(personalResume);
                mCenterIntroduce.setRightText(personalResume);
                mCenterIntroduce.setLeftButton(!StringUtils.textIsEmpty(personalResume) ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
                setNotifyBtn();
                break;

            case IntegerUtil.EVENT_ID_11036:
                //经历编辑
                personalDataBean.setTeachingExperienceNum(bundle.getInt(StringUtils.EVENT_DATA));
                mCenterEducation.setRightText(personalDataBean.getTeachingExperienceNum() == 0 ? "" : personalDataBean.getTeachingExperienceNum() + "条");
                reloadMineDatas();
                break;

        }
        return false;
    }

    private void setNotifyBtn() {
        if (StringUtils.textIsEmpty(personalDataBean.getSubjectName()))
            return;
        if (StringUtils.textIsEmpty(personalDataBean.getPhotoUrl()))
            return;
        if (StringUtils.textIsEmpty(personalDataBean.getCityName()))
            return;
        if (StringUtils.textIsEmpty(personalDataBean.getGeaduateSchool()))
            return;
        if (personalDataBean.getTeachingAge() < 0)
            return;
        if (StringUtils.textIsEmpty(personalDataBean.getPersonalResume()))
            return;
        mConfirm.setClickable(true);
        mConfirm.setChecked(true);
    }

    private ConfirmDialog dialog;

    @Override
    public void onBackPressed() {
        if (!isChangeDate) {
            finish();
            return;
        }
        if (dialog == null) {
            dialog = ConfirmDialog.newInstance("", "您还没有保存，确定要退出？", "取消", "确定");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        colseDialog();
                        finish();
                        break;
                }
            }
        });
    }

    private void colseDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dismiss();
        }
    }

    /****
     * 显示提示信息
     *
     * @param mesg
     */
    private void nofifyShowMesg(String mesg) {
        ConfirmDialog.newInstance("", mesg, "", "确定").setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    /*****
     * 通知刷新数据
     */
    private void reloadMainDatas() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11002);
        EventBus.getDefault().post(bundle);
    }

    private void reloadMineDatas() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11013);
        EventBus.getDefault().post(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChangeDate = false;
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                postSaveData();
            } else {
                showPremission();
            }
        }
    }
    private void showPremission() {
        ConfirmDialog dialog =  ConfirmDialog.newInstance("提示", "您已禁止了访问设备存储空间 <br/>设置路径：设置 ->应用管理 ->" + SystemInfoUtil.getApplicationName() + " ->授权管理<br/><br/>特别提示：<br/><font color='#3a7bd5'>“取消”后您提交的数据将不会被保存！</font>", "取消", "设置");
        dialog.setMessageGravity(Gravity.LEFT);
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

            @Override
            public void dialogStatus(int id) {
                if (id == com.main.disanxuelib.R.id.tv_dialog_ok) {//设置界面
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    ActivityCompat.startActivityForResult((Activity) mContext,intent,IntegerUtil.PERMISSION_REQUEST_SETTING,null);
                }else{
                    finish();
                }
            }
        });
    }

}
