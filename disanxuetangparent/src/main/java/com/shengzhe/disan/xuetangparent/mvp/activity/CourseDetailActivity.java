package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.CourseDetailView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 课程详情 on 2018/4/2.
 */

public class CourseDetailActivity extends BaseActivity implements CourseDetailView.ICourseDetailView {
    @BindView(R.id.ab_course_head)
    AppBarLayout mToolLayout;
    @BindView(R.id.ccb_course_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.iv_course_bg)
    ImageView mCourseBg;
    @BindView(R.id.tv_class_name)
    TextView mName;
    @BindView(R.id.tv_class_number)
    TextView mNumber;
    @BindView(R.id.tv_class_details)
    TextView mDetails;
    @BindView(R.id.tv_class_time)
    TextView mTime;
    @BindView(R.id.tv_class_price)
    TextView mPrice;
    @BindView(R.id.iv_class_image)
    ImageView mImage;
    @BindView(R.id.tv_class_teachername)
    TextView mTeacherName;
    @BindView(R.id.iv_class_isplant)
    ImageView mIsplant;
    @BindView(R.id.tv_class_message)
    TextView mMessage;
    @BindView(R.id.iv_quality_certification)
    ImageView mQuality;
    @BindView(R.id.iv_realname_certification)
    ImageView mRealname;
    @BindView(R.id.iv_teacher_certification)
    ImageView mTeacher;
    @BindView(R.id.iv_education_certification)
    ImageView mEducation;


    @BindView(R.id.tv_course_address)
    TextView mAddress;
    @BindView(R.id.pst_course_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_course_content)
    ViewPager mViewPager;

    @BindView(R.id.tv_createschedule_payprice)
    TextView tvCreateschedulePayprice;
    @BindView(R.id.tv_createschedule_preprice)
    TextView tvCreateschedulePreprice;
    @BindView(R.id.btn_createschedule_confirm)
    Button btnCreatescheduleConfirm;
    @BindView(R.id.appbar_course_toolbar)
    Toolbar appbarCourseToolbar;
    @BindView(R.id.ll_coureseditail)
    LinearLayout llCoureseditail;
    @BindView(R.id.ll_course_head)
    LinearLayout llCourseHead;
    @BindView(R.id.tv_createschedule_price)
    TextView tvCreateschedulePrice;

    private CoursePresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CoursePresenter(mContext,this);
        presenter.initMineCourseDetailUi(getSupportFragmentManager());
        presenter.initMineCourseDetailDatas(getIntent());
        presenter.loadCourseDetail();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_coursedetail;
    }


    @OnClick({R.id.common_bar_leftBtn, R.id.btn_createschedule_confirm, R.id.ll_course_head})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.btn_createschedule_confirm:
                presenter.goOfflineLessonOrder();
                break;

            case R.id.ll_course_head:
                //Intent intents = new Intent(mContext, TeacherNewPagerActivity.class);
                Intent intents = new Intent(mContext, OfflineTeacherActivity.class);
                intents.putExtra(StringUtils.TEACHER_ID, presenter.getTeacherId());
                startActivity(intents);
                break;
        }
    }


    @Override
    public AppBarLayout getToolLayoutView() {
        return mToolLayout;
    }

    @Override
    public CommonCrosswiseBar getTitleView() {
        return mTitle;
    }

    @Override
    public ImageView getCourseBgView() {
        return mCourseBg;
    }

    @Override
    public TextView getNameView() {
        return mName;
    }

    @Override
    public TextView getNumberView() {
        return mNumber;
    }

    @Override
    public TextView getDetailsView() {
        return mDetails;
    }

    @Override
    public TextView getTimeView() {
        return mTime;
    }

    @Override
    public TextView getPriceView() {
        return mPrice;
    }

    @Override
    public ImageView getImageView() {
        return mImage;
    }

    @Override
    public TextView getTeacherNameView() {
        return mTeacherName;
    }

    @Override
    public ImageView getIsplantView() {
        return mIsplant;
    }

    @Override
    public TextView getMessageView() {
        return mMessage;
    }

    @Override
    public ImageView getQualityView() {
        return mQuality;
    }

    @Override
    public ImageView getRealnameView() {
        return mRealname;
    }

    @Override
    public ImageView getTeacherView() {
        return mTeacher;
    }

    @Override
    public ImageView getEducationView() {
        return mEducation;
    }

    @Override
    public TextView getAddressView() {
        return mAddress;
    }

    @Override
    public PagerSlidingTabStrip getTabLayoutView() {
        return mTabLayout;
    }

    @Override
    public ViewPager getViewPagerView() {
        return mViewPager;
    }

    @Override
    public TextView getCreateschedulePaypriceView() {
        return tvCreateschedulePayprice;
    }

    @Override
    public TextView getCreateschedulePrepriceView() {
        return tvCreateschedulePreprice;
    }

    @Override
    public Button getCreatescheduleConfirmView() {
        return btnCreatescheduleConfirm;
    }

    @Override
    public Toolbar getToolbarView() {
        return appbarCourseToolbar;
    }

    @Override
    public LinearLayout getCoureseditailView() {
        return llCoureseditail;
    }

    @Override
    public LinearLayout getCourseHeadView() {
        return llCourseHead;
    }

    @Override
    public TextView getCreateschedulePriceView() {
        return tvCreateschedulePrice;
    }
}
