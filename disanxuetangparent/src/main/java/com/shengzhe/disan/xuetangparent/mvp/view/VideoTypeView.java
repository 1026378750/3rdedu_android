package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.bean.VideoType;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass.VideoItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 科目类型 on 2017/11/27.
 */

public class VideoTypeView extends BaseView{
    private FragmentManager fragmentManager;
    //页卡视图集合
    private List<Fragment> mFragments = new ArrayList<>();
    public String[] titles;

    public VideoTypeView(Context context) {
        super(context);
    }

    //设置视频类
    public  void setResultDatas(List<VideoType> arrListVt){
        ArrayList<VideoType> videoTypes=new ArrayList<VideoType>();
        VideoType  videoType=new VideoType();
        videoType.setTypeName("全部");
        videoType.setAppUrl("");
        videoType.setId(0);
        videoTypes.add(videoType);
        for(int j=0;j<arrListVt.size();j++){
            videoTypes.add(arrListVt.get(j));
        }

        titles = new String[videoTypes.size()];

        for (int i = 0; i <videoTypes.size(); i++) {
            VideoType title = videoTypes.get(i);
            titles[i] = title.getTypeName();
            mFragments.add(VideoItemFragment.newInstance(title.getTypeName(),title.getId()));
        }
        ViewPageFragmentAdapter viewPageAdapter = new ViewPageFragmentAdapter(fragmentManager, mFragments, titles);
        iView.getViewPager().setCurrentItem(0);
        iView.getViewPager().setOffscreenPageLimit(videoTypes.size());
        iView.getViewPager().setAdapter(viewPageAdapter);
        iView.getPagerSlidingTabStrip().setViewPager(iView.getViewPager());
        iView.getPagerSlidingTabStrip().setShouldExpand(false);
    }

    private IVideoView iView;
    public void setIVideoView(IVideoView iView){
        this.iView = iView;
    }

    public void initDatas(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public interface IVideoView{
        ViewPager getViewPager();
        PagerSlidingTabStrip getPagerSlidingTabStrip();
    }
}
