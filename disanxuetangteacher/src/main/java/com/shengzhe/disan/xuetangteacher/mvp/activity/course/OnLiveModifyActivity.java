package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailLiveBean;
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
 * Created by 在线直播编辑 on 2018/1/18.
 */

public class OnLiveModifyActivity extends BaseActivity implements IPhotoCall {
    @BindView(R.id.iv_onliveoperator_face)
    ImageView mFace;
    @BindView(R.id.et_onliveoperator_coursename)
    TextView mCourseName;
    @BindView(R.id.tv_onliveoperator_prename)
    TextView mPreName;
    @BindView(R.id.tv_onliveoperator_stage)
    TextView mStage;
    @BindView(R.id.tv_onliveoperator_type)
    TextView mType;
    @BindView(R.id.et_onliveoperator_number)
    TextView mNumber;
    @BindView(R.id.et_onliveoperator_times)
    TextView mTimes;
    @BindView(R.id.et_onliveoperator_singletime)
    TextView mSingleTime;
    @BindView(R.id.et_onliveoperator_singleprice)
    EditText mSinglePrice;
    @BindView(R.id.et_onliveoperator_introduction)
    EditText mInfo;
    @BindView(R.id.et_onliveoperator_target)
    EditText mTarget;
    @BindView(R.id.et_onliveoperator_fitcrowd)
    EditText mFitcrowd;
    @BindView(R.id.tv_onliveoperator_introduction)
    TextView mInfoNum;
    @BindView(R.id.tv_onliveoperator_target)
    TextView mTargetNum;
    @BindView(R.id.tv_onliveoperator_fitcrowd)
    TextView mFitcrowdNum;
    @BindView(R.id.ccb_onlive)
    CommonCrosswiseBar ccbOnlive;
    @BindView(R.id.et_onliveoperator_next)
    Button etOnliveoperatorNext;

    private CourseDetailLiveBean courseBean;

    @Override
    public void initData() {
        courseBean = getIntent().getParcelableExtra(StringUtils.FRAGMENT_DATA);
        if (courseBean == null)
            courseBean = new CourseDetailLiveBean();
        else
            ImageUtil.loadImageViewLoding(mContext, courseBean.photoUrl, mFace, R.mipmap.default_error, R.mipmap.default_error);
        int index = courseBean.courseName.indexOf("-");
        if (index>0) {
            mCourseName.setText(courseBean.courseName.substring(index + 1));
            mPreName.setText(courseBean.courseName);
            mStage.setText(courseBean.courseName.substring(0, index));
        }else{
            mCourseName.setText(courseBean.courseName);
            mStage.setText(courseBean.gradeName+courseBean.subjectName);
            mPreName.setText(mStage.getText().toString()+"-"+mCourseName.getText().toString());
        }
        mType.setText(courseBean.directTypeName);
        mNumber.setText(String.valueOf(courseBean.maxUser));
        mTimes.setText(String.valueOf(courseBean.classTime));
        mSingleTime.setText(String.valueOf(courseBean.duration));
        mSinglePrice.setText(ArithUtils.round(courseBean.price));
        mSinglePrice.setSelection(ArithUtils.round(courseBean.price).length());
        mInfo.setText(courseBean.courseRemark.equals(title)?"":courseBean.courseRemark);
        mTarget.setText(courseBean.target.equals(title)?"":courseBean.target);
        mFitcrowd.setText(courseBean.crowd.equals(title)?"":courseBean.crowd);
        ccbOnlive.setTitleText("编辑在线直播课");

        courseBean.photoUrl = "";

        mInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //50~500
                count += start;
                mInfoNum.setText(count == 0 ? "限50-500字" : count > 500 ? "-" + (500 - count) + "字" : count + "字");
                mInfoNum.setTextColor(UiUtils.getColor(count > 0 && (count < 50 || count > 500) ? R.color.color_ca4341 : R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //40~400
                count += start;
                mTargetNum.setText(count == 0 ? "限40-400字" : count > 400 ? "-" + (400 - count) + "字" : count + "字");
                mTargetNum.setTextColor(UiUtils.getColor(count > 0 && (count < 40 || count > 400) ? R.color.color_ca4341 : R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFitcrowd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //30~300
                count += start;
                mFitcrowdNum.setText(count == 0 ? "限30-300字" : count > 300 ? "-" + (300 - count) + "字" : count + "字");
                mFitcrowdNum.setTextColor(UiUtils.getColor(count > 0 && (count < 30 || count > 300) ? R.color.color_ca4341 : R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_onlivemodify;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.iv_onliveoperator_face, R.id.et_onliveoperator_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.iv_onliveoperator_face:
                //头像
                int width = SystemInfoUtil.getScreenWidth() - 40;
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE)
                        .setParam(width, (int) (width * 0.7));
                break;

            case R.id.et_onliveoperator_next:
                //保存
                if (StringUtils.textIsEmpty(mSinglePrice.getText().toString())) {
                    nofifyShowMesg("请输入单价", mSinglePrice);
                    return;
                }

                if (!StringUtils.textIsEmpty(mSinglePrice.getText().toString()) && (Double.parseDouble(mSinglePrice.getText().toString().trim()) < 0 || Double.parseDouble(mSinglePrice.getText().toString().trim()) > 9999)) {
                    nofifyShowMesg("单次课价格范围为0.0~9999元/小时", mSinglePrice);
                    return;
                }

                courseBean.price = ArithUtils.roundLong(mSinglePrice.getText().toString());

                /*if (StringUtils.textIsEmpty(mInfo.getText().toString())) {
                    nofifyShowMesg("请输入课程简介", mInfo);
                    return;
                }***/

                if (!StringUtils.textIsEmpty(mInfo.getText().toString())&&(mInfo.getText().toString().length() < 50 || mInfo.getText().toString().length() > 500)) {
                    nofifyShowMesg("课程简介字数范围为50~500字", mInfo);
                    return;
                }
                if (RegUtil.containsEmoji(mInfo.getText().toString())) {
                    nofifyShowMesg("禁止输入表情", mInfo);
                    return;
                }

                courseBean.courseRemark = mInfo.getText().toString();

              /*  if (StringUtils.textIsEmpty(mTarget.getText().toString())) {
                    nofifyShowMesg("请输入教学目标", mTarget);
                    return;
                }****/
                if (!StringUtils.textIsEmpty(mTarget.getText().toString())&&(mTarget.getText().toString().length() < 40 || mTarget.getText().toString().length() > 400)) {
                    nofifyShowMesg("教学目标字数范围为40~400字", mTarget);
                    return;
                }
                if (RegUtil.containsEmoji(mTarget.getText().toString())) {
                    nofifyShowMesg("禁止输入表情", mTarget);
                    return;
                }

                courseBean.target = mTarget.getText().toString();

              /*  if (StringUtils.textIsEmpty(mFitcrowd.getText().toString())) {
                    nofifyShowMesg("请输入适用人群", mFitcrowd);
                    return;
                }****/
                if (!StringUtils.textIsEmpty(mFitcrowd.getText().toString())&&(mFitcrowd.getText().toString().length() < 30 || mFitcrowd.getText().toString().length() > 300)) {
                    nofifyShowMesg("适用人群字数范围为30~300字", mFitcrowd);
                    return;
                }
                if (RegUtil.containsEmoji(mFitcrowd.getText().toString())) {
                    nofifyShowMesg("禁止输入表情", mFitcrowd);
                    return;
                }

                courseBean.crowd = mFitcrowd.getText().toString();
                postSaveDatas();
                break;
        }
    }

    private ConfirmDialog dialog;

    @Override
    public void onBackPressed() {
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
   String title="呐，这个人很懒，什么都米有留下 ┐(─__─)┌";

    private void postSaveDatas() {
        HttpService httpService = Http.getHttpService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        byte[] data = new byte[0];
        if (StringUtils.textIsEmpty(courseBean.photoUrl) || courseBean.photoUrl.contains("http://")) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("directPhotoFile", "directPhotoFile", imageBody);//imgfile 后台接收图片流的参数名
        } else {
            File file = new File(courseBean.photoUrl);//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("directPhotoFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }

        Map<String, Object> map = new HashMap<>();
        map.put("courseId", String.valueOf(courseBean.courseId));
        map.put("courseName", courseBean.courseName);
        map.put("gradeId", String.valueOf(courseBean.gradeId));
        map.put("directType", String.valueOf(courseBean.directType));
        map.put("maxUser", String.valueOf(courseBean.maxUser));
        map.put("classTime", String.valueOf(courseBean.classTime));
        map.put("duration", String.valueOf(courseBean.duration));
        map.put("price", String.valueOf(courseBean.price));
        map.put("courseRemark", TextUtils.isEmpty(courseBean.courseRemark)?title:String.valueOf(courseBean.courseRemark));
        map.put("target", TextUtils.isEmpty(courseBean.target)?title:String.valueOf(courseBean.target));
        map.put("crowd", TextUtils.isEmpty(courseBean.crowd)?title:String.valueOf(courseBean.crowd));
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.addAndModifyDirect(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        ToastUtil.showShort("提交成功");
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11024);
                        bundle.putParcelable(StringUtils.EVENT_DATA, courseBean);
                        EventBus.getDefault().post(bundle);
                        finish();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        courseBean.photoUrl = photoUrl;
        ImageUtil.setItemRoundImageViewOnlyDisplay(mFace, courseBean.photoUrl);
        ImageUtil.loadImageViewLoding(mContext, courseBean.photoUrl, mFace, R.mipmap.default_error, R.mipmap.default_error);
    }

    /****
     * 显示提示信息
     *
     * @param mesg
     */
    private void nofifyShowMesg(String mesg, final EditText currentEdit) {
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
                        if (currentEdit == null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }


}
