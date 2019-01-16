package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass.LiveDetailFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass.LivePlanFragment;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.bean.LiveInfo;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/29.
 * <p>
 * 直播课 课程详情
 */

public class LiveCourseActivity extends BaseActivity {
    @BindView(R.id.psts_common_tables)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_common_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_solive_confirm)
    Button tvSoliveConfirm;

    String[] titles = new String[]{"课程详情", "课程安排"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    @Override
    public void initData() {
        int courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,-1);
        mFragmentList.add(0, LiveDetailFragment.getInstance(courseId));
        mFragmentList.add(1, LivePlanFragment.getInstance(courseId));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public int setLayout() {
        return R.layout.common_viewpager_tab;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragmentList != null)
            mFragmentList.clear();
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_solive_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_solive_confirm:
                if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
                    //尚未登录
                    LoginOpentionUtil.getInstance().LoginRequest(mContext);
                    return;
                }
                Intent intent = new Intent(mContext, LiveOrderActivity.class);
                intent.putExtra(StringUtils.COURSE_ID,liveInfo.getCourseId());
                startActivity(intent);
                break;
        }
    }

    private LiveInfo liveInfo;

    public void setBtnStatus(LiveInfo liveInfo) {
        this.liveInfo = liveInfo;
        tvSoliveConfirm.setVisibility(View.VISIBLE);
        if (liveInfo.getIsFullQuota() == 1) {
            tvSoliveConfirm.setText("人数已满");
            tvSoliveConfirm.setEnabled(false);
        } else {
            if (liveInfo.getIsAreadyBuy() == 2) {
                tvSoliveConfirm.setText("已购买");
                tvSoliveConfirm.setEnabled(false);
            } else {
                tvSoliveConfirm.setEnabled(true);
                if (liveInfo.getCourseDiscount() != 0) {
                    tvSoliveConfirm.setText("" + (liveInfo.getCourseDiscount() == 100 ? "立即购买" : "优惠购买(" + String.format("%.1f", (float) liveInfo.getCourseDiscount() / 10) + "折)"));
                } else {
                    tvSoliveConfirm.setText("立即购买");
                }
            }
        }

    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11008:
                Intent intent = new Intent(mContext, LiveOrderActivity.class);
                intent.putExtra(StringUtils.COURSE_ID,liveInfo.getCourseId());
                startActivity(intent);
                break;
        }

        return false;
    }

}
