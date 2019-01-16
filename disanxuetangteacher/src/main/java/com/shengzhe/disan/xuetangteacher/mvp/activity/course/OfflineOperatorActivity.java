package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.main.disanxuelib.bean.GradeParentBean;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.OfflineCourseBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 线下一对一课程 on 2018/1/13.
 */

public class OfflineOperatorActivity extends BaseActivity {
    @BindView(R.id.et_oneclazzoperator_coursename)
    EditText mCourseName;
    @BindView(R.id.tv_oneclazzoperator_prename)
    TextView mPreName;
    @BindView(R.id.tv_oneclazzoperator_stage)
    TextView mStage;
    @BindView(R.id.et_oneclazzoperator_singletime)
    EditText mSingleTime;
    @BindView(R.id.et_oneclazzoperator_student)
    EditText mStudent;
    @BindView(R.id.et_oneclazzoperator_teach)
    EditText mTeach;
    @BindView(R.id.et_oneclazzoperator_school)
    EditText mSchool;
    @BindView(R.id.et_oneclazzoperator_introduction)
    EditText mIntroduction;
    @BindView(R.id.tv_oneclazzoperator_introduction)
    TextView mIntroductionNum;
    @BindView(R.id.rg_oneclazzoperator)
    RadioGroup mRadioGroup;
    @BindView(R.id.ccb_oneclass)
    CommonCrosswiseBar ccbOneclass;

    private OfflineCourseBean data;

    @Override
    public void initData() {
        data = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        mCourseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.textIsEmpty(mStage.getText().toString())){
                    mPreName.setText("请选择授课阶段");
                    return;
                }
                mPreName.setText(mStage.getText().toString()+"-"+mCourseName.getText().toString());
            }
        });

        mIntroduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //50~500
                count += start;
                mIntroductionNum.setText(count == 0 ? "限50-300字" : count > 500 ? "-" + (500 - count) + "字" : count + "字");
                mIntroductionNum.setTextColor(UiUtils.getColor(count > 0 && (count < 50 || count > 500) ? R.color.color_ca4341 : R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        GetGradeParent();
        if (data == null) {
            data = new OfflineCourseBean();
            data.subjectName =  SharedPreferencesManager.getSubjectName();
            return;
        }
        gradeId = data.gradeId;

        int index = data.courseName.indexOf("-");
        if (index>0) {
            mCourseName.setText(data.courseName.substring(index + 1));
            mCourseName.setSelection(mCourseName.getText().toString().length());
            mPreName.setText(data.courseName);
            mStage.setText(data.courseName.substring(0, index));
        }else{
            mCourseName.setText(data.courseName);
            mStage.setText(data.gradeName+data.subjectName);
            mStage.setHint(data.subjectName);
            mPreName.setText(mStage.getText().toString()+"-"+mCourseName.getText().toString());
        }

        mSingleTime.setText(data.singleTime);
        mStudent.setText(data.studentPrice == 0 ? "" : ArithUtils.roundLong(data.studentPrice));
        mTeach.setText(data.teacherPrice == 0 ? "" : ArithUtils.roundLong(data.teacherPrice));
        mSchool.setText(data.campusPrice == 0 ? "" : ArithUtils.roundLong(data.campusPrice));
        mIntroduction.setText(data.introduction.equals(title)?"":data.introduction);
        ccbOneclass.setTitleText("编辑线下1对1");

        if (data.isListenApply == 1) {
            mRadioGroup.check(R.id.rb_oneclazzoperator_support);
        } else {
            mRadioGroup.check(R.id.rb_oneclazzoperator_dissupport);
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_oneclazzoperator;
    }

    private NiceDialog niceDialog;

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_oneclazzoperator_stage, R.id.btn_oneclazzoperator_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_oneclazzoperator_stage:
                //选择授课阶段
                if (beanArray == null)
                    return;
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        gradeId = beanList.get(index).getGradeId();
                        mStage.setText(item+data.subjectName);
                        mStage.setHint(data.subjectName);
                        if (!StringUtils.textIsEmpty(mCourseName.getText().toString())){
                            mPreName.setText(mStage.getText().toString()+"-"+mCourseName.getText().toString());
                        }
                    }
                });
                niceDialog.setCommonLayout(beanArray, true, getSupportFragmentManager());
                break;

            case R.id.btn_oneclazzoperator_confirm:
                //提交
                if (StringUtils.textIsEmpty(mStage.getText().toString())) {
                    nofifyShowMesg("请选择授课阶段", null);
                    return;
                }
                data.gradeName = mStage.getHint().toString();

                if (StringUtils.textIsEmpty(mCourseName.getText().toString())) {
                    nofifyShowMesg("请填写课程名称", mCourseName);
                    return;
                }
                if (mCourseName.getText().toString().length() > 15) {
                    nofifyShowMesg("课程名称最多15个字", mCourseName);
                    return;
                }
                if (RegUtil.containsEmoji(mCourseName.getText().toString())) {
                    nofifyShowMesg("禁止输入表情", mCourseName);
                    return;
                }
                data.courseName = mPreName.getText().toString();

                if (StringUtils.textIsEmpty(mSingleTime.getText().toString())) {
                    nofifyShowMesg("请填写单次时长", mSingleTime);
                    return;
                }
                if (Long.parseLong(mSingleTime.getText().toString().trim()) < 1 || Long.parseLong(mSingleTime.getText().toString().trim()) > 6) {
                    nofifyShowMesg("单次课时长范围1~6小时", mSingleTime);
                    return;
                }
                data.singleTime = mSingleTime.getText().toString();

                if (StringUtils.textIsEmpty(mStudent.getText().toString()) && StringUtils.textIsEmpty(mTeach.getText().toString()) && StringUtils.textIsEmpty(mSchool.getText().toString())) {
                    nofifyShowMesg("至少输入一种授课价格", mStudent);
                    return;
                }

                if (!StringUtils.textIsEmpty(mStudent.getText().toString()) && (Double.parseDouble(mStudent.getText().toString().trim()) < 0.1 || Double.parseDouble(mStudent.getText().toString().trim()) > 9999)) {
                    nofifyShowMesg("学生上门价格范围为0.1~9999元/小时", mStudent);
                    return;
                }

                if (!StringUtils.textIsEmpty(mTeach.getText().toString()) && (Double.parseDouble(mTeach.getText().toString().trim()) < 0.1 || Double.parseDouble(mTeach.getText().toString().trim()) > 9999)) {
                    nofifyShowMesg("老师上门价格范围为0.1~9999元/小时", mTeach);
                    return;
                }

                if (!StringUtils.textIsEmpty(mSchool.getText().toString()) && (Double.parseDouble(mSchool.getText().toString().trim()) < 0.1 || Double.parseDouble(mSchool.getText().toString().trim()) > 9999)) {
                    nofifyShowMesg("校区上课价格范围为0.1~9999元/小时", mSchool);
                    return;
                }

                data.studentPrice = ArithUtils.roundLong(StringUtils.textIsEmpty(mStudent.getText().toString()) ? "0" : mStudent.getText().toString());
                data.teacherPrice = ArithUtils.roundLong(StringUtils.textIsEmpty(mTeach.getText().toString()) ? "0" : mTeach.getText().toString());
                data.campusPrice = ArithUtils.roundLong(StringUtils.textIsEmpty(mSchool.getText().toString()) ? "0" : mSchool.getText().toString());

                /* if (StringUtils.textIsEmpty(mIntroduction.getText().toString())) {
                    nofifyShowMesg("请填写课程简介", mIntroduction);
                    return;
                }*/
                if (RegUtil.containsEmoji(mIntroduction.getText().toString())) {
                    nofifyShowMesg("禁止输入表情", mIntroduction);
                    return;
                }

                if (!StringUtils.textIsEmpty(mIntroduction.getText().toString())&&(mIntroduction.getText().toString().length() < 50 || mIntroduction.getText().toString().length() > 500)) {
                    nofifyShowMesg("课程简介字数范围为50~500字", mIntroduction);
                    return;
                }

                postAddOne();
                break;
        }
    }

    /**
     * 线下一对一开课
     */
    private int gradeId = -1;
    String title = "呐，这个人很懒，什么都米有留下 ┐(─__─)┌";
    private void postAddOne() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        if (data.courseId != -1) {
            map.put("courseId", data.courseId);
        }
        if (gradeId != -1) {
            map.put("gradeId", gradeId);
        }
        map.put("courseName", data.courseName);
        map.put("duration", data.singleTime);
        map.put("courseRemark", TextUtils.isEmpty(mIntroduction.getText().toString())?title:mIntroduction.getText().toString());
        map.put("isListenApply", mRadioGroup.getCheckedRadioButtonId() == R.id.rb_oneclazzoperator_support ? 1 : 2);
        if (data.studentPrice != 0)
            map.put("studenToDoorPrice", data.studentPrice);
        if (data.teacherPrice != 0)
            map.put("teacherToDoorPrice", data.teacherPrice);
        if (data.campusPrice != 0)
            map.put("campusToDoorPrice", data.campusPrice);

        httpService.addAndModifyOne(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        startPage();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                            startPage();
                        }
                    }
                });

    }
    private void startPage(){
        ToastUtil.showShort("提交成功");
        data.introduction = mIntroduction.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11019);
        EventBus.getDefault().post(bundle);
        finish();

    }

    private ConfirmDialog dialog;

    @Override
    public void onBackPressed() {
        if (data.courseName.equals(mCourseName.getText().toString()) &&
                data.gradeName.equals(mStage.getText().toString()) &&
                data.singleTime.equals(mSingleTime.getText().toString()) &&
                ArithUtils.roundLong(data.studentPrice).equals(StringUtils.textIsEmpty(mStudent.getText().toString()) ? "0.00" : mStudent.getText().toString()) &&
                ArithUtils.roundLong(data.teacherPrice).equals(StringUtils.textIsEmpty(mTeach.getText().toString()) ? "0.00" : mTeach.getText().toString()) &&
                ArithUtils.roundLong(data.campusPrice).equals(StringUtils.textIsEmpty(mSchool.getText().toString()) ? "0.00" : mSchool.getText().toString()) &&
                data.introduction.equals(mIntroduction.getText().toString())
                ) {
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

    private String[] beanArray;
    private List<GradeParentBean> beanList;

    private void GetGradeParent() {
        beanList = SharedPreferencesManager.getGradePhase();
        if (beanList != null && !beanList.isEmpty()) {
            beanArray = new String[beanList.size()];
            for (int i = 0; i < beanList.size(); i++) {
                beanArray[i] = beanList.get(i).getGradeName();
            }
        }

        HttpService httpService = Http.getHttpService();
        httpService.gradeParent()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<GradeParentBean>>(mContext, true) {
                    @Override
                    protected void onDone(List<GradeParentBean> mBeanList) {
                        SharedPreferencesManager.saveGradePhase(beanList);
                        beanList = mBeanList;
                        if (beanList != null && !beanList.isEmpty()) {
                            beanArray = new String[beanList.size()];
                            for (int i = 0; i < beanList.size(); i++) {
                                beanArray[i] = beanList.get(i).getGradeName();
                            }
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {


                    }
                });

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
