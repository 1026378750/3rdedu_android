package com.shengzhe.disan.xuetangteacher.mvp.fragment.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by liukui on 2017/11/29.
 * <p>
 * 在线课程 课程详情
 */

public class MineOrderItemFragment extends BaseFragment {
    @BindView(R.id.pst_itemmineorder_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_itemmineorder_item)
    ViewPager mViewPager;

    String[] titles = new String[]{"待支付", "已支付","全部"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    //构造fragment
    public static MineOrderItemFragment newInstance(String type) {
        MineOrderItemFragment fragment = new MineOrderItemFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        String type = getArguments().getString("type");
        mFragmentList.add(0, OrderItemFragment.newInstance(type,titles[0]));
        mFragmentList.add(1, OrderItemFragment.newInstance(type,titles[1]));
        mFragmentList.add(2, OrderItemFragment.newInstance(type,titles[2]));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getChildFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public int setLayout() {
        return R.layout.mine_order_item;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFragmentList.clear();
    }

    @Override
    public void onClick(View v) {
    }

}
