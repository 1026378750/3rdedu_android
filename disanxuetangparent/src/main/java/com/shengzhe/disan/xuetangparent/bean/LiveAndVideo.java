package com.shengzhe.disan.xuetangparent.bean;


import com.main.disanxuelib.view.banner.BannerBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/5.
 */

public class LiveAndVideo {

    private List<BannerBean> banner;
    private List<VideoBean> video;
    private List<LiveBean> live;

    public List<SquadBean> getSquad() {
        return squad;
    }

    public void setSquad(List<SquadBean> squad) {
        this.squad = squad;
    }

    private List<SquadBean> squad;



    public int getTodayCourse() {
        return todayCourse;
    }

    public void setTodayCourse(int todayCourse) {
        this.todayCourse = todayCourse;
    }

    private int todayCourse;//今天是否有课 1：没有 2：有

    public int getIsApplyCourseListen() {
        return isApplyCourseListen;
    }

    public void setIsApplyCourseListen(int isApplyCourseListen) {
        this.isApplyCourseListen = isApplyCourseListen;
    }

    private  int  isApplyCourseListen;//1、否 2、是
    public List<BannerBean> getBanner() {
        return banner ==null ?new ArrayList<BannerBean>():banner;
    }
    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<VideoBean> getVideo() {
        return video==null ?new ArrayList<VideoBean>():video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public List<LiveBean> getLive() {
        return live==null ?new ArrayList<LiveBean>():live;
    }

    public void setLive(List<LiveBean> live) {
        this.live = live;
    }

}
