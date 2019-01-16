package com.shengzhe.disan.xuetangparent.mvp.fragment.teacher;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.TeacherMessageView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import butterknife.BindView;

/**
 * Created by 老师信息 on 2018/3/13.l
 */

public class TeacherMessageFragment extends BaseFragment implements BaseFragment.LazyLoadingListener ,TeacherMessageView.ITeacherMessageView {
    @BindView(R.id.tv_rteacher_introduction)
    TextView mRteacherIntroduction;
    @BindView(R.id.tv_rteacher_experience)
    RecyclerView mExperience;
    @BindView(R.id.tv_rteacher_graduate)
    TextView mGraduate;

    private CoursePresenter presenter;

    public static TeacherMessageFragment newInstance(int teacherId) {
        TeacherMessageFragment fragment = new TeacherMessageFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.TEACHER_ID, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CoursePresenter(mContext,this);
        presenter.initTeacherMessageUi();
        presenter.initTeacherMessageDatas();
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_teacher_message;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.getTeacherMesg(getArguments().getInt(StringUtils.TEACHER_ID));
    }

    @Override
    public TextView getIntroductionView() {
        return mRteacherIntroduction;
    }

    @Override
    public RecyclerView getExperienceView() {
        return mExperience;
    }

    @Override
    public TextView getGraduateView() {
        return mGraduate;
    }
}
