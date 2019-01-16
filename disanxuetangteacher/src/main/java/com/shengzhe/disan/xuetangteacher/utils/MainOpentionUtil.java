package com.shengzhe.disan.xuetangteacher.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangteacher.activity.MainActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineMessageActivity;

/**
 * Created by liukui on 2017/12/14.
 *
 * 公共用户登录处理
 *
 */

public class MainOpentionUtil {
    private static MainOpentionUtil model;
    private String tagName;

    /*****
     * 初始化
     * @return
     */
    public static MainOpentionUtil getInstance(){
        if(model==null){
             model = new MainOpentionUtil();
        }
        return model;
    }

    /****
     * 跳转到消息列表
     * @param context
     */
    public void jumpToMessageActivity(final Context context,Bundle bundle){
        LoginOpentionUtil.getInstance().jumpActivity(context,MineMessageActivity.class);
    }

}
