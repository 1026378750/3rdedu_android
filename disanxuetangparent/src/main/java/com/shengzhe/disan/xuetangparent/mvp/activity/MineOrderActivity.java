package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.MineOrderItemFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的订单
 * Created by Administrator on 2017/11/23.
 */

public class MineOrderActivity extends BaseActivity {
    @BindView(R.id.pst_mineclazz_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_mineclazz_item)
    ViewPager mViewPager;

    String[] titles = new String[]{"待支付", "已支付","全部"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    @Override
    public void initData() {
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
}
