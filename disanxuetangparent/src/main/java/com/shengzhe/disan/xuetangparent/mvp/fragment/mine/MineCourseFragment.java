package com.shengzhe.disan.xuetangparent.mvp.fragment.mine;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by liukui on 2017/12/4.
 */
public class MineCourseFragment extends BaseFragment {

    @BindView(R.id.pst_mineclazz_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_mineclazz_item)
    ViewPager mViewPager;


    String[] titles = new String[]{"未开课", "已开课","已完成"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    @Override
    public void initData() {
        mFragmentList.add(0, MineCourseItemFragment.newInstance(titles[0]));
        mFragmentList.add(1, MineCourseItemFragment.newInstance(titles[1]));
        mFragmentList.add(2, MineCourseItemFragment.newInstance(titles[2]));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getActivity().getSupportFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public int setLayout() {
        return R.layout.mine_open_class;
    }

    @Override
    public void onClick(View v) {

    }
}
