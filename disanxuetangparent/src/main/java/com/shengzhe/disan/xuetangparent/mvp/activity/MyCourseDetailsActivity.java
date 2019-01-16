package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.view.MyRecyclerView;
import com.shengzhe.disan.xuetangparent.bean.OrderCourse;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.MyCourseDetailsView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的课程里面的课程详情页
 */
public class MyCourseDetailsActivity extends BaseActivity implements MyCourseDetailsView.IMyCourseDetailsView {
    @BindView(R.id.tv_course_type)
    TextView mType;
    @BindView(R.id.tv_course_teaching_method)
    TextView mMethod;
    @BindView(R.id.tv_course_status)
    TextView mStatus;
    @BindView(R.id.iv_course_image)
    ImageView mImage;
    @BindView(R.id.iv_course_name)
    TextView mName;
    @BindView(R.id.iv_course_message)
    TextView mMessage;
    @BindView(R.id.tv_course_city)
    TextView mCity;
    @BindView(R.id.tv_course_time)
    TextView mTime;
    @BindView(R.id.tv_course_class)
    TextView mClass;
    @BindView(R.id.tv_course_problem)
    TextView mProblem;
    @BindView(R.id.rv_course_content)
    MyRecyclerView mRecyclerView;

    private CoursePresenter presenter;
    private OrderCourse course;
    @Override
    public void initData() {
        course = getIntent().getParcelableExtra(StringUtils.COURSE);
        if(presenter==null)
            presenter = new CoursePresenter(mContext,this);
        presenter.initMyCourseDetailsUi();
        presenter.initMyCourseDetailsDatas(course,getSupportFragmentManager());
        presenter.myCourseDetail(course.getId());
    }

    @Override
    public int setLayout() {
        return R.layout.activity_my_course_details;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.tv_course_problem})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_course_problem:
                //确定
                presenter.myCourseProblem();
                break;
        }
    }

    @Override
    public TextView getTypeView() {
        return mType;
    }

    @Override
    public TextView getMethodView() {
        return mMethod;
    }

    @Override
    public TextView getStatusView() {
        return mStatus;
    }

    @Override
    public ImageView getImageView() {
        return mImage;
    }

    @Override
    public TextView getNameView() {
        return mName;
    }

    @Override
    public TextView getMessageView() {
        return mMessage;
    }

    @Override
    public TextView getCityView() {
        return mCity;
    }

    @Override
    public TextView getTimeView() {
        return mTime;
    }

    @Override
    public TextView getClassView() {
        return mClass;
    }

    @Override
    public TextView getProblemView() {
        return mProblem;
    }

    @Override
    public MyRecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}