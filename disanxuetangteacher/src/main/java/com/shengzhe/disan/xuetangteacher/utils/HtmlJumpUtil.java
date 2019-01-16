package com.shengzhe.disan.xuetangteacher.utils;

import android.app.Activity;
import android.content.Intent;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.view.banner.BannerBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.CommWebActivity;

/**
 * Created by Html跳转公共处理 on 2017/12/29.
 */

public class HtmlJumpUtil {

    /*****
     * 服务协议
     */
    public static void fwxyActivity(){
        Activity activity = AppManager.getAppManager().currentActivity();
        Intent intent = new Intent(activity, CommWebActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_title,"服务协议");
        intent.putExtra(StringUtils.WEB_TYPE,StringUtils.WEB_TYPE_fileHtml5);
        intent.putExtra(StringUtils.url,"user_agreement.html");
        activity.startActivity(intent);
    }

    /*****
     * 首页banner点击
     */
    public static void bannerActivity(BannerBean banner){
        Activity activity = AppManager.getAppManager().currentActivity();
        Intent intent = new Intent(activity, CommWebActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_title,banner.getAdName());
        intent.putExtra(StringUtils.WEB_TYPE,StringUtils.WEB_TYPE_url);
        intent.putExtra(StringUtils.url,banner.getPicLink());
        activity.startActivity(intent);
    }

    /*****
     * 关于我们
     */
    public static void gywmActivity(){
        Activity activity = AppManager.getAppManager().currentActivity();
        Intent intent = new Intent(activity, CommWebActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_title,"关于我们");
        intent.putExtra(StringUtils.WEB_TYPE,StringUtils.WEB_TYPE_fileHtml5);
        intent.putExtra(StringUtils.url,"info.html");
        activity.startActivity(intent);
    }

    /****
     * 用户协议
     */
    public static void yhxyActivity(){
        Activity activity = AppManager.getAppManager().currentActivity();
        Intent intent = new Intent(activity, CommWebActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_title,"第三学堂服务协议");
        intent.putExtra(StringUtils.WEB_TYPE,StringUtils.WEB_TYPE_fileHtml5);
        intent.putExtra(StringUtils.url,"user_agreement.html");
        activity.startActivity(intent);
    }

}
