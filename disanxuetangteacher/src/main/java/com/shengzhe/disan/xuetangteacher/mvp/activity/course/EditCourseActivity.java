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
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
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
 * Created by 线下班课课程编辑 on 2018/4/2.
 */

public class EditCourseActivity extends BaseActivity {
    @BindView(R.id.tv_class_name)
    TextView mName;
    @BindView(R.id.tv_class_time)
    TextView mTime;
    @BindView(R.id.tv_class_number)
    TextView mNumber;
    @BindView(R.id.tv_class_details)
    TextView mDetails;
    @BindView(R.id.tv_class_price)
    TextView mPrice;
    @BindView(R.id.tv_calssone_inlimit)
    TextView mInlimit;
    @BindView(R.id.et_calssone_introduction)
    EditText mIntroduction;
    @BindView(R.id.et_calssone_target)
    EditText mTarget;
    @BindView(R.id.et_calssone_fitcrowd)
    EditText mFitcrowd;
    @BindView(R.id.tv_calssone_introduction)
    TextView mIntroductionNum;
    @BindView(R.id.tv_calssone_target)
    TextView mTargetNum;
    @BindView(R.id.tv_calssone_fitcrowd)
    TextView mFitcrowdNum;
    @BindView(R.id.btn_calssone_confirm)
    Button mConfirm;
    @BindView(R.id.iv_class_photo)
    ImageView ivClassPhoto;

    private CourseSquadBean data;
    private NiceDialog niceDialog;

    @Override
    public void initData() {
        data = getIntent().getParcelableExtra(StringUtils.FRAGMENT_DATA);
        if (data == null) {
            data = new CourseSquadBean();
        }
        mName.setText(data.courseName);
        mNumber.setText(StringUtils.textFormatHtml("<font color='#1d97ea'>" + data.salesVolume + "</font>" + "/" + data.maxUser + "人"));
        mDetails.setText(data.subjectName + " " + data.gradeName);
        mTime.setText(data.duration + "小时/次，共" + data.classTime + "次");
        mPrice.setText("¥" + ArithUtils.round(data.totalPrice));
        ImageUtil.loadImageViewLoding(mContext, data.pictureUrl, ivClassPhoto, R.mipmap.default_error, R.mipmap.default_error);
        mFitcrowd.setText(data.crowd.equals(title)?"":data.crowd);
        mTarget.setText(data.target.equals(title)?"":data.target);
        mIntroduction.setText(data.remark.equals(title)?"":data.remark);
        mInlimit.setText(StringUtils.canJoinToStr(data.canJoin));

        mIntroduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //50~500
                count += start;
                mIntroductionNum.setText(count == 0 ? "限50-500字" : count > 500 ? "-" + (500 - count) + "字" : count + "字");
                mIntroductionNum.setTextColor(UiUtils.getColor(count > 0 && (count < 50 || count > 500) ? R.color.color_ca4341 : R.color.color_999999));
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
        return R.layout.activity_editcourse;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_calssone_inlimit, R.id.btn_calssone_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_calssone_inlimit:
                //插班人数
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        mInlimit.setText(item);
                    }
                });

                niceDialog.setCommonLayout(ContentUtil.selectCANJOIN,true, getSupportFragmentManager());
                break;

            case R.id.btn_calssone_confirm:
             /*   //保存
               if(StringUtils.textIsEmpty(mIntroduction.getText().toString())){
                    nofifyShowMesg("请输入课程简介",mIntroduction);
                    return;
                }****/
                if (!StringUtils.textIsEmpty(mIntroduction.getText().toString())&&(mIntroduction.getText().toString().length()<50 || mIntroduction.getText().toString().length()>500)) {
                    nofifyShowMesg("课程简介字数范围为50~500字",mIntroduction);
                    return;
                }
                if(RegUtil.containsEmoji(mIntroduction.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mIntroduction);
                    return;
                }
                data.remark = mIntroduction.getText().toString();

             /*   if(StringUtils.textIsEmpty(mTarget.getText().toString())){
                    nofifyShowMesg("请输入教学目标",mTarget);
                    return;
                }****/
                if (!StringUtils.textIsEmpty(mTarget.getText().toString())&&(mTarget.getText().toString().length()<40 || mTarget.getText().toString().length()>400)) {
                    nofifyShowMesg("教学目标字数范围为40~400字",mTarget);
                    return;
                }
                if(RegUtil.containsEmoji(mTarget.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mTarget);
                    return;
                }
                data.target = mTarget.getText().toString();

             /*   if(StringUtils.textIsEmpty(mFitcrowd.getText().toString())){
                    nofifyShowMesg("请输入适用人群",mFitcrowd);
                    return;
                }****/

                if (!StringUtils.textIsEmpty(mFitcrowd.getText().toString())&&(mFitcrowd.getText().toString().length()<30 || mFitcrowd.getText().toString().length()>300)) {
                    nofifyShowMesg("适用人群字数范围为30~300字",mFitcrowd);
                    return;
                }
                if(RegUtil.containsEmoji(mFitcrowd.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mFitcrowd);
                    return;
                }
                data.crowd = mFitcrowd.getText().toString();

                data.canJoin = StringUtils.canJoinToInt(mInlimit.getText().toString());
                postSaveDatas();
                break;
        }
    }

    String title = "呐，这个人很懒，什么都米有留下 ┐(─__─)┌";

    /**
     * 提交数据
     */
    private void postSaveDatas() {
        HttpService httpService = Http.getHttpService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        byte[] datas = new byte[0];
        RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), datas);
        builder.addFormDataPart("directPhotoFile", "directPhotoFile", imageBody);//imgfile 后台接收图片流的参数名

        Map<String, Object> map = new HashMap<>();
        map.put("courseId", String.valueOf(data.courseId));
        map.put("canJoin", String.valueOf(data.canJoin));
        map.put("remark", TextUtils.isEmpty(data.remark)?title: String.valueOf(data.remark));
        map.put("target", TextUtils.isEmpty(data.target)?title:String.valueOf(data.target));
        map.put("crowd",  TextUtils.isEmpty(data.crowd)?title:String.valueOf( data.crowd));
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.saveCourseSquad(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        ToastUtil.showShort("提交成功");
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11043);
                        bundle.putParcelable(StringUtils.EVENT_DATA, data);
                        EventBus.getDefault().post(bundle);
                        finish();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showShort(ex.getMessage());
                        if (ex.getErrCode()==222222){
                            Bundle bundle = new Bundle();
                            bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11043);
                            bundle.putParcelable(StringUtils.EVENT_DATA, data);
                            EventBus.getDefault().post(bundle);
                            finish();
                        }
                    }
                });


    }

    /****
     * 显示提示信息
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
