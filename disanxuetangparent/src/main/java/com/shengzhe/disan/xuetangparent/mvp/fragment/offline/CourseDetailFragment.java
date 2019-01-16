package com.shengzhe.disan.xuetangparent.mvp.fragment.offline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.main.disanxuelib.util.ArithUtils;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.CourseDetailChildView;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 课程详情 on 2018/4/2.
 */

public class CourseDetailFragment extends BaseFragment implements BaseFragment.LazyLoadingListener,CourseDetailChildView.ICourseDetailChildView {
    @BindView(R.id.tv_course_times)
    TextView mTimes;
    @BindView(R.id.tv_course_singleprice)
    TextView mSinglePrice;
    @BindView(R.id.tv_course_singletime)
    TextView mSingleTime;
    @BindView(R.id.tv_course_experience)
    TextView mExperience;
    @BindView(R.id.tv_course_inlimit)
    TextView mInlimit;
    @BindView(R.id.tv_course_introduction)
    TextView mIntroduction;
    @BindView(R.id.tv_course_target)
    TextView mTarget;
    @BindView(R.id.tv_course_fitcrowd)
    TextView mFitCrowd;

    private CoursePresenter presenter;
    private int courseId;

    public static CourseDetailFragment getInstance(int courseId) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.COURSE_ID,courseId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        courseId = getArguments().getInt(StringUtils.COURSE_ID);
        if (presenter==null)
            presenter = new CoursePresenter(mContext,this);
        presenter.initCourseDetailChildUi();
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_coursedetail;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.getCourseSquadInfo(courseId);
    }

    @Override
    public TextView getTimesView() {
        return mTimes;
    }

    @Override
    public TextView getSinglePriceView() {
        return mSinglePrice;
    }

    @Override
    public TextView getSingleTimeView() {
        return mSingleTime;
    }

    @Override
    public TextView getExperienceView() {
        return mExperience;
    }

    @Override
    public TextView getInlimitView() {
        return mInlimit;
    }

    @Override
    public TextView getIntroductionView() {
        return mIntroduction;
    }

    @Override
    public TextView getTargetView() {
        return mTarget;
    }

    @Override
    public TextView getFitCrowdView() {
        return mFitCrowd;
    }
}
