package com.shengzhe.disan.xuetangteacher.mvp.activity.order;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.mine.MineOrderItemFragment;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/13.
 */

public class MineOrderActivity extends BaseActivity {
    @BindView(R.id.rl_mineorder_title)
    RelativeLayout mTitle;
    @BindView(R.id.pst_mineorder_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_mineorder_item)
    ViewPager mViewPager;

    String[] titles = new String[]{"线下1对1", "线下班课","在线直播课"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    @Override
    public void initData() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(UiUtils.getDimension(R.dimen.title_bar_height)) + SystemInfoUtil.getStatusBarHeight());
        mTitle.setPadding(0,mTitle.getPaddingTop()+SystemInfoUtil.getStatusBarHeight(),0,0);
        mTitle.setLayoutParams(params);

        mFragmentList.add(0, MineOrderItemFragment.newInstance(titles[0]));
        mFragmentList.add(1, MineOrderItemFragment.newInstance(titles[1]));
        mFragmentList.add(2, MineOrderItemFragment.newInstance(titles[2]));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mineorder;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentList.clear();
    }
}
