package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.course.CourseDetailFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.course.CourseOutlineFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 线下班课课程详情 on 2018/4/2.
 */

public class CourseDetailActivity extends BaseActivity {
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
    @BindView(R.id.tv_course_address)
    TextView mAddress;
    @BindView(R.id.pst_course_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_course_content)
    ViewPager mViewPager;
    @BindView(R.id.ll_coursedetail)
    LinearLayout llCoursedetail;

    private int courseId;

    String[] titles = new String[]{"详情", "大纲"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private float lastAlpha = 0;
    private CourseSquadBean courseSquadSchedule;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.ACTIVITY_DATA, 0);
        mFragmentList.add(0, CourseDetailFragment.newInstance(courseId));
        mFragmentList.add(1, CourseOutlineFragment.newInstance(courseId));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);
        mToolLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int dy) {
                float alpha = (float) Math.abs(dy) / mCourseBg.getMeasuredHeight();
                alpha = alpha > 1 ? 1 : alpha;
                if (lastAlpha == alpha)
                    return;
                lastAlpha = alpha;
                setCCBTitleAlpha(lastAlpha);
            }
        });
        postCourseSquadInfo();

        if (!ConstantUrl.IS_EDIT) {
            mTitle.setRightText("");
            mTitle.setRightButtonVisibility();
            llCoursedetail.setVisibility(View.GONE);
        }
    }

    /**
     * 提交网络请求
     */
    private void postCourseSquadInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        ConstantUrl.CLIEN_Info = 2;
        httpService.courseSquadInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseSquadBean>(mContext, true) {
                    @Override
                    protected void onDone(CourseSquadBean mCourseSquadScheduleBean) {
                        if (mCourseSquadScheduleBean != null) {
                            courseSquadSchedule = mCourseSquadScheduleBean;
                            setPrameter(courseSquadSchedule);
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }

    private void setCCBTitleAlpha(float alpha) {
        if (alpha > 0.6) {
            mTitle.setLeftButton(R.mipmap.ic_black_left_arrow);
            mTitle.setRightTextColor(R.color.color_333333);
            mTitle.setTitleTextColor(R.color.color_333333);
            mTitle.setBGColor(R.color.color_ffffff);
        } else {
            mTitle.setLeftButton(R.mipmap.ic_white_left_arrow);
            mTitle.setRightTextColor(R.color.color_ffffff);
            mTitle.setTitleTextColor(R.color.color_00000000);
            mTitle.setBGColor(R.color.color_00000000);
        }
        //mTitle.setViewAlpha(alpha);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_coursedetail;
    }

    private CourseSquadBean courseBean;

    public void setPrameter(CourseSquadBean courseBean) {
        if (courseBean == null) {
            this.courseBean = new CourseSquadBean();
            return;
        }
        this.courseBean = courseBean;
        mName.setText(courseBean.courseName);
        mNumber.setText(StringUtils.textFormatHtml("<font color='#1d97ea'>" + courseBean.salesVolume + "</font>" + "/" + courseBean.maxUser + "人"));
        mDetails.setText(courseBean.subjectName + " " + courseBean.gradeName);
        mTime.setText(courseBean.duration + "小时/次，共" + courseBean.classTime + "次");
        mPrice.setText("¥" + ArithUtils.round(courseBean.totalPrice));
        mAddress.setText(courseBean.cityName + " " + courseBean.address);
        ImageUtil.loadImageViewLoding(mContext, courseBean.pictureUrl, mCourseBg, R.mipmap.default_error, R.mipmap.default_error);
    }

    private ConfirmDialog dialog;

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_class_number, R.id.common_bar_rightBtn, R.id.btn_offlinedetail_delete})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                //编辑
                if (courseBean == null)
                    return;
                Intent editIntent = new Intent(this, EditCourseActivity.class);
                editIntent.putExtra(StringUtils.ACTIVITY_DATA, courseBean);
                startActivity(editIntent);
                break;

            case R.id.tv_class_number:
                if (courseBean == null)
                    return;
                //购买人数
                Intent intent = new Intent(this, CourseBuyerActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, courseId);
                startActivity(intent);
                break;

            case R.id.btn_offlinedetail_delete:
                //删除
                if(dialog==null) {
                    dialog = ConfirmDialog.newInstance("", "确定要删除此课程？", "取消", "确定");
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

                    @Override
                    public void dialogStatus(int id) {
                        switch (id){
                            case R.id.tv_dialog_ok:
                                postRemoveCourse();
                                break;
                        }
                    }
                });
                break;
        }
    }

    /**
     * 删除课程
     */
    private void postRemoveCourse() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("courseType", 4);
        httpService.removeCourse(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String mCourse) {
                        ToastUtil.showShort(mCourse);
                        getClassItem();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showShort(ex.getMessage());
                        if (ex.getErrCode() == 222222) {
                            getClassItem();
                        }
                    }
                });


    }

    /**
     * 转向列表页面
     */
    private void getClassItem() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11041);
        EventBus.getDefault().post(bundle);
        onBackPressed();
    }

}
