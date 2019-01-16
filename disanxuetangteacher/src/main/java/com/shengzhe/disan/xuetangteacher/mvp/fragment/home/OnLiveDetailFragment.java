package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveDetailActivity;
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
 * Created by Administrator on 2018/1/17.
 */

public class OnLiveDetailFragment extends BaseFragment{
    @BindView(R.id.tv_onlivedetail_image)
    ImageView mFace;
    @BindView(R.id.tv_onlivedetail_coursename)
    TextView mCourseName;
    @BindView(R.id.tv_onlivedetail_subject)
    TextView mSubjectName;
    @BindView(R.id.tv_onlivedetail_grade)
    TextView mStage;
    @BindView(R.id.tv_onlivedetail_type)
    TextView mType;
    @BindView(R.id.tv_onlivedetail_volume)
    TextView mNumber;
    @BindView(R.id.tv_onlivedetail_times)
    TextView mTimes;
    @BindView(R.id.tv_onlivedetail_signaltime)
    TextView mSingleTime;
    @BindView(R.id.tv_onlivedetail_signalprice)
    TextView mSinglePrice;
    @BindView(R.id.tv_onlivedetail_info)
    TextView mInfo;
    @BindView(R.id.tv_onlivedetail_infonum)
    TextView mInfoNum;
    @BindView(R.id.tv_onlivedetail_target)
    TextView mTarget;
    @BindView(R.id.tv_onlivedetail_targetnum)
    TextView mTargetNum;
    @BindView(R.id.tv_onlivedetail_fitcrowd)
    TextView mFitcrowd;
    @BindView(R.id.tv_onlivedetail_fitcrowdnum)
    TextView mFitcrowdNum;
    @BindView(R.id.tv_onlivedetail_delete)
    Button tvOnlivedetailDelete;

    private CourseLiveBean  courseLiveBean;
    private CourseDetailLiveBean courseDetailLiveBean;
    public static OnLiveDetailFragment newInstance(CourseLiveBean data) {
        OnLiveDetailFragment fragment = new OnLiveDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initData() {
        courseLiveBean = getArguments().getParcelable(StringUtils.FRAGMENT_DATA);
        if (courseLiveBean==null){
            courseLiveBean = new CourseLiveBean();
        }
        if(! ConstantUrl.IS_EDIT){
            tvOnlivedetailDelete.setVisibility(View.GONE);
        }
        postCourseInfo();
    }
    /**
     * 开课中的直播课详情
     */
    private void postCourseInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseLiveBean.courseId);
        map.put("courseType", courseLiveBean.courseType);
        httpService.courseLiveInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseDetailLiveBean>(mContext, true) {
                    @Override
                    protected void onDone(CourseDetailLiveBean mCourse) {
                        courseDetailLiveBean = mCourse;
                        if (getActivity() instanceof OnLiveDetailActivity){
                            ((OnLiveDetailActivity)getActivity()).courseBean = mCourse;
                        }
                        setDatas();
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }


    /**
     * 删除课程
     */
    private void postRemoveCourse() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseLiveBean.courseId);
        map.put("courseType", courseLiveBean.courseType);
        httpService.removeCourse(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String mCourse) {
                        ToastUtil.showShort(mCourse);
                        getActivity().finish();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showShort(ex.getMessage());

                    }
                });


    }

    @Override
    public int setLayout() {
        return R.layout.fragment_onlivedetail;
    }

    private ConfirmDialog dialog;
    @OnClick({R.id.tv_onlivedetail_delete})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_onlivedetail_delete:
                //删除
                if(dialog==null) {
                    dialog = ConfirmDialog.newInstance("", "确定要删除此课程？", "取消", "确定");
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getFragmentManager());
                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

                    @Override
                    public void dialogStatus(int id) {
                        switch (id){
                            case R.id.tv_dialog_ok:
                                Bundle newBundle = new Bundle();
                                newBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11022);
                                EventBus.getDefault().post(newBundle);
                                getActivity().finish();
                                postRemoveCourse();
                                break;
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11024:
                //修改
                postCourseInfo();
                break;
        }
    }

    private void setDatas() {
        ImageUtil.loadImageViewLoding(mContext, courseDetailLiveBean.photoUrl, mFace,R.mipmap.default_error,R.mipmap.default_error);
        mCourseName.setText(courseDetailLiveBean.courseName);
        mSubjectName.setText(courseDetailLiveBean.subjectName);
        mStage.setText(courseDetailLiveBean.gradeName);
        mType.setText(courseDetailLiveBean.directTypeName);
        mNumber.setText(courseDetailLiveBean.maxUser+"人");
        mTimes.setText(courseDetailLiveBean.classTime+"次");
        mSingleTime.setText(courseDetailLiveBean.duration+"小时");
        mSinglePrice.setText(StringUtils.textFormatHtml("<font color='#D92B2B'>"+ ArithUtils.round(courseDetailLiveBean.price)+"</font>元/次"));
        mInfo.setText(TextUtils.isEmpty(courseDetailLiveBean.courseRemark)==true?courseDetailLiveBean.courseRemark:courseDetailLiveBean.courseRemark);
        mInfoNum.setText(TextUtils.isEmpty(courseDetailLiveBean.courseRemark)==true?"":courseDetailLiveBean.courseRemark.length()+"字");
        mTarget.setText(TextUtils.isEmpty(courseDetailLiveBean.target)==true?courseDetailLiveBean.target:courseDetailLiveBean.target);
        mTargetNum.setText(TextUtils.isEmpty(courseDetailLiveBean.target)==true?"":courseDetailLiveBean.target.length()+"字");
        mFitcrowd.setText(TextUtils.isEmpty(courseDetailLiveBean.crowd)==true?courseDetailLiveBean.crowd:courseDetailLiveBean.crowd);
        mFitcrowdNum.setText(TextUtils.isEmpty(courseDetailLiveBean.crowd)==true?"":courseDetailLiveBean.crowd.length()+"字");
    }
}
