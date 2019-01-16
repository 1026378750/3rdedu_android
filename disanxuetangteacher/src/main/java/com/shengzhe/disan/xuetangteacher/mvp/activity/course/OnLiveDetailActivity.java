package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCycleFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveDetailFragment;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 在线直播课详情 on 2018/1/17.
 */

public class OnLiveDetailActivity extends BaseActivity {
    @BindView(R.id.pst_onlivedetail_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_onlivedetail_item)
    ViewPager mViewPager;

    String[] titles = new String[]{"课程信息", "上课周期"};
    @BindView(R.id.onLiveDetail_ccb)
    CommonCrosswiseBar onLiveDetailCcb;
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    public CourseDetailLiveBean courseBean;
    private int courseId = 0;

    @Override
    public void initData() {
        CourseLiveBean data = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        courseId = data.courseId;
        mFragmentList.add(0, OnLiveDetailFragment.newInstance(data));
        mFragmentList.add(1, ClassCycleFragment.newInstance(data));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);

        if(! ConstantUrl.IS_EDIT){
            onLiveDetailCcb.setRightText("");
            onLiveDetailCcb.setRightButtonVisibility();

        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_onlivedetail;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn})
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
                Intent intent = new Intent(mContext, OnLiveModifyActivity.class);
                courseBean.courseId = courseId;
                intent.putExtra(StringUtils.ACTIVITY_DATA, courseBean);
                startActivity(intent);
                break;
        }
    }
}
