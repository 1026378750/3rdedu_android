package com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OnlivePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.LiveDetailView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/29.
 * <p>
 * 在线课程 课程详情
 */

public class LiveDetailFragment extends BaseFragment implements BaseFragment.LazyLoadingListener ,LiveDetailView.ILiveDetailView {
    @BindView(R.id.iv_solive_cover)
    ImageView mCover;
    @BindView(R.id.tv_solive_name)
    TextView mName;
    @BindView(R.id.iv_solive_isplant)
    ImageView isPlant;
    @BindView(R.id.tv_solive_price)
    TextView mPrice;
    @BindView(R.id.tv_solive_preprice)
    TextView mPrePrice;
    @BindView(R.id.iv_solive_teacherimage)
    ImageView mTeacherImage;
    @BindView(R.id.tv_solive_teachername)
    TextView mTeacherName;
    @BindView(R.id.tv_solive_teachermessage)
    TextView mTeacherMessage;
    @BindView(R.id.iv_quality_certification)
    ImageView mCertification;
    @BindView(R.id.iv_realname_certification)
    ImageView mRealnameCertification;
    @BindView(R.id.iv_teacher_certification)
    ImageView mTeacherCertification;
    @BindView(R.id.iv_education_certification)
    ImageView mEducationCertification;
    @BindView(R.id.tv_solive_course)
    TextView mCourse;
    @BindView(R.id.tv_solive_time)
    TextView mTime;
    @BindView(R.id.tv_solive_notice)
    TextView mNotice;
    @BindView(R.id.tv_solive_introduction)
    TextView mIntroduction;
    @BindView(R.id.tv_solive_target)
    TextView mTarget;
    @BindView(R.id.tv_solive_suit)
    TextView mSuit;
    @BindView(R.id.rl_solive_teacher)
    RelativeLayout rlSoliveTeacher;

    private OnlivePresenter presenter;
    private int courseId = -1;
    public static Fragment getInstance(int courseId) {
        LiveDetailFragment fragment = new LiveDetailFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        courseId = getArguments().getInt(StringUtils.COURSE_ID);
        if (presenter == null)
            presenter = new OnlivePresenter(mContext,this);
        presenter.initLiveDetailUi();
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_soliveclass_details;
    }

    @OnClick({R.id.rl_solive_teacher})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_solive_teacher:
                Intent intent = new Intent();
                //intent.setClass(mContext, TeacherNewPagerActivity.class);
                intent.setClass(mContext, OfflineTeacherActivity.class);
                intent.putExtra(StringUtils.TEACHER_ID,presenter.getLiveInfoTeacherId());
                startActivity(intent);
                break;

        }
    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.getLiveInfo(courseId);
    }

    @Override
    public ImageView getCoverView() {
        return mCover;
    }

    @Override
    public TextView getNameView() {
        return mName;
    }

    @Override
    public ImageView getPlantView() {
        return isPlant;
    }

    @Override
    public TextView getPriceView() {
        return mPrice;
    }

    @Override
    public TextView getPrePriceView() {
        return mPrePrice;
    }

    @Override
    public ImageView getTeacherImageView() {
        return mTeacherImage;
    }

    @Override
    public TextView getTeacherNameView() {
        return mTeacherName;
    }

    @Override
    public TextView getTeacherMessageView() {
        return mTeacherMessage;
    }

    @Override
    public ImageView getCertificationView() {
        return mCertification;
    }

    @Override
    public ImageView getRealnameCertificationView() {
        return mRealnameCertification;
    }

    @Override
    public ImageView getTeacherCertificationView() {
        return mTeacherCertification;
    }

    @Override
    public ImageView getEducationCertificationView() {
        return mEducationCertification;
    }

    @Override
    public TextView getCourseView() {
        return mCourse;
    }

    @Override
    public TextView getTimeView() {
        return mTime;
    }

    @Override
    public TextView getNoticeView() {
        return mNotice;
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
    public TextView getSuitView() {
        return mSuit;
    }

    @Override
    public RelativeLayout getSoliveTeacherView() {
        return rlSoliveTeacher;
    }
}
