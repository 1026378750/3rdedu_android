package com.shengzhe.disan.xuetangparent.mvp.fragment.offline;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/******
 * 线下课程
 */
public class OfflineCourseFragment extends BaseFragment{
    @BindView(R.id.pst_offline_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_offline_content)
    ViewPager mViewPager;

    String[] titles = new String[]{"线下1对1", "线下班课"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private int couurentIndex = 0;
    @Override
    public void initData() {
        setParameter();
        mFragmentList.add(0, new OfflineOneOnOneFragment());
        mFragmentList.add(1, new OfflineClassFragment());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getChildFragmentManager(), mFragmentList, titles));
        mTabLayout.setViewPager(mViewPager);
        setCurrentFragment(couurentIndex);
    }

    public void setCurrentFragment(int index){
        couurentIndex = index;
        mViewPager.setCurrentItem(index);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_offinecourse;
    }



    @Override
    public void onClick(View v) {

    }

    /*****
     * 设置偏移量
     */
    private void setParameter() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(UiUtils.getDimension(R.dimen.title_bar_height) + SystemInfoUtil.getStatusBarHeight()));
        mTabLayout.setPadding(0,mTabLayout.getPaddingTop()+SystemInfoUtil.getStatusBarHeight(),0,0);
        mTabLayout.setLayoutParams(params);
    }

}
