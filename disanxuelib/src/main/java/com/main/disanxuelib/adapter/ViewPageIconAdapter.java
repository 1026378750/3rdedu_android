package com.main.disanxuelib.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.main.disanxuelib.view.PagerSlidingTabStrip;

import java.util.List;

/**
 * 通用的ViewpageFragment 的Adapter
 */
public class ViewPageIconAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private List<Fragment> mFragments;
    private int[] icon;

    public ViewPageIconAdapter(FragmentManager fm, List<Fragment> mFragments, int[] icon) {
        super(fm);
        this.mFragments = mFragments;
        this.icon = icon;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return icon[position];
    }

}
