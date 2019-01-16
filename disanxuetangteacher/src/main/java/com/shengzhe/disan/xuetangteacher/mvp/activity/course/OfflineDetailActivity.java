package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailsBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangteacher.bean.OfflineCourseBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 线下一对一课程详情 on 2018/1/17.
 */

public class OfflineDetailActivity extends BaseActivity {
    @BindView(R.id.et_offlinedetail_coursename)
    TextView mCourseName;
    @BindView(R.id.tv_offlinedetail_subjectname)
    TextView mSubjectName;
    @BindView(R.id.tv_offlinedetail_stage)
    TextView mStage;
    @BindView(R.id.et_offlinedetail_singletime)
    TextView mSingleTime;
    @BindView(R.id.et_offlinedetail_student)
    TextView mStudent;
    @BindView(R.id.et_offlinedetail_teach)
    TextView mTeach;
    @BindView(R.id.et_offlinedetail_school)
    TextView mSchool;
    @BindView(R.id.et_offlinedetail_info)
    TextView mInfo;
    @BindView(R.id.et_offlinedetail_infonum)
    TextView mInfoNum;
    @BindView(R.id.tv_offlinedetail_support)
    TextView mOfflinedetailSupport;
    @BindView(R.id.btn_offlinedetail_delete)
    Button btnOfflinedetailDelete;
    @BindView(R.id.offlinedetail_ccb)
    CommonCrosswiseBar offlinedetailCcb;

    private CourseOflineBean data;
    private CourseDetailsBean courseDetailsBean;

    @Override
    public void initData() {
        if(! ConstantUrl.IS_EDIT){
            //查看课程详情不做修改
            btnOfflinedetailDelete.setVisibility(View.GONE);
            offlinedetailCcb.setRightText("");
            offlinedetailCcb.setRightButtonVisibility();
        }
        data = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);

        if (data == null) {
            data = new CourseOflineBean();
            return;
        }
        postCourseInfo();
    }

    /**
     * 删除课程
     */
    private void postRemoveCourse() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", data.courseId);
        map.put("courseType", data.courseType);
        httpService.removeCourse(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String mCourse) {
                        ToastUtil.showShort(mCourse);
                        finish();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showShort(ex.getMessage());
                    }
                });


    }

    /**
     * 开课详情
     */
    private void postCourseInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", data.courseId);
        map.put("courseType", data.courseType);
        httpService.courseInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseDetailsBean>(mContext, true) {
                    @Override
                    protected void onDone(CourseDetailsBean mCourse) {
                        courseDetailsBean = mCourse;
                        setDatas();
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_offlinedetail;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn, R.id.btn_offlinedetail_delete})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                //编辑
                OfflineCourseBean courseBean = new OfflineCourseBean();
                courseBean.introduction = courseDetailsBean.courseRemark;
                courseBean.courseName = courseDetailsBean.courseName;
                courseBean.gradeName = courseDetailsBean.gradeName;
                courseBean.singleTime = courseDetailsBean.duration + "";
                courseBean.teacherPrice = courseDetailsBean.teacherPrice;
                courseBean.campusPrice = courseDetailsBean.campusPrice;
                courseBean.studentPrice = courseDetailsBean.studentPrice;
                courseBean.gradeId = courseDetailsBean.gradeId;
                courseBean.isListenApply = courseDetailsBean.isListenApply;
                courseBean.subjectName = courseDetailsBean.subjectName;
                courseBean.gradeId = courseDetailsBean.gradeId;
                courseBean.courseId = data.courseId;
                Intent intent = new Intent(mContext, OfflineOperatorActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, courseBean);
                startActivity(intent);
                break;

            case R.id.btn_offlinedetail_delete:
                //删除
                if (dialog == null) {
                    dialog = ConfirmDialog.newInstance("", "确定要删除此课程？", "取消", "确定");
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
                                Bundle bundle = new Bundle();
                                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11020);
                                EventBus.getDefault().post(bundle);
                                onBackPressed();
                                postRemoveCourse();
                                break;
                        }
                    }
                });
                break;
        }
    }

    private ConfirmDialog dialog;

    private void colseDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11019:
                //添加、修改
                postCourseInfo();
                break;

        }
        return false;
    }

    /**
     * 设置线下一对一详情页面
     */
    String title="呐，这个人很懒，什么都米有留下 ┐(─__─)┌";
    private void setDatas() {
        mCourseName.setText(courseDetailsBean.courseName);
        mSubjectName.setText(courseDetailsBean.subjectName);
        mStage.setText(courseDetailsBean.gradeName);
        mSingleTime.setText(courseDetailsBean.duration + " 小时/次");
        mStudent.setText(courseDetailsBean.studentPrice==0?"--":StringUtils.textFormatHtml("<font color='#D92B2B'>" + ArithUtils.roundLong(courseDetailsBean.studentPrice) + "</font>/小时"));
        mTeach.setText(courseDetailsBean.teacherPrice==0?"--":StringUtils.textFormatHtml("<font color='#D92B2B'>" + ArithUtils.roundLong(courseDetailsBean.teacherPrice) + "</font>/小时"));
        mSchool.setText(courseDetailsBean.campusPrice==0?"--":StringUtils.textFormatHtml("<font color='#D92B2B'>" + ArithUtils.roundLong(courseDetailsBean.campusPrice) + "</font>/小时"));

        mOfflinedetailSupport.setText(courseDetailsBean.isListenApply == 1 ? "支持" : "不支持");
        mInfo.setText(TextUtils.isEmpty(courseDetailsBean.courseRemark)==true?"":courseDetailsBean.courseRemark);
        mInfoNum.setText(courseDetailsBean.courseRemark.length() + "字");

    }
}
