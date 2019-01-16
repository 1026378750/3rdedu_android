package com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass;

import android.support.v4.view.ViewPager;
import android.view.View;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.MainPresenter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.VideoPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.VideoTypeView;
import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/27.
 */

public class VideoFragment extends BaseFragment implements VideoTypeView.IVideoView,MainPresenter.OnClickPresenter{
    @BindView(R.id.vp_video_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.psts_video_tabs)
    PagerSlidingTabStrip mPagerSlidingTabStrip;

    private VideoPresenter presenter;
    @Override
    public void initData() {
        if (presenter==null)
            presenter = new VideoPresenter(mContext,this);
        presenter.initVideoUi();
        presenter.initVideoDatas(getFragmentManager());
        presenter.loadVideoList();
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public PagerSlidingTabStrip getPagerSlidingTabStrip() {
        return mPagerSlidingTabStrip;
    }

    @Override
    public void presenterClick(View v, Object obj) {

    }
}
