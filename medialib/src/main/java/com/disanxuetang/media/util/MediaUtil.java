package com.disanxuetang.media.util;

import android.app.Application;
import android.content.Context;

import com.baijiahulian.BJVideoPlayerSDK;

/**
 * Created by 多媒体处理 on 2018/4/23.
 */

public class MediaUtil {

    public final static long PartnerId = 32975272 ;

    private static MediaUtil mediaUtil = null;

    public static MediaUtil getInstance(){
        if (mediaUtil==null)
            mediaUtil = new MediaUtil();
        return mediaUtil;
    }

    public void initBaiJia(Application application){
        //百家云视频初始化
        BJVideoPlayerSDK.getInstance().init(application);
    }
}
