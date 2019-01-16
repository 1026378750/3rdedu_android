package com.shengzhe.disan.xuetangparent.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.baidu.mapapi.model.LatLng;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.banner.BannerBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.CommWebActivity;

import java.net.URISyntaxException;

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

    /*****
     * 第三方地图跳转
     * @param mapName
     * @param address
     */
    public static void gotoMapsActivity(String mapName, LatLng latLng,String name,String address) {
        Activity activity = AppManager.getAppManager().currentActivity();
        Intent intent = null;
        switch (mapName){
            case BaseStringUtils.MapBaiDu:
               // 百度：http://lbsyun.baidu.com/index.php?title=uri/api/android
                intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/marker?location="+latLng.latitude+","+latLng.longitude+"&title="+name+"&content="+address+"&traffic=on"));
                break;

            case BaseStringUtils.MapGaoDe:
                //高德：http://lbs.amap.com/api/amap-mobile/guide/android/route
                intent = new Intent("android.intent.action.VIEW",
                        android.net.Uri.parse("androidamap://viewMap?sourceApplication="+ SystemInfoUtil.getApplicationName()+"&poiname="+address+"&lat="+latLng.latitude+"&lon="+latLng.longitude+"&dev=0"));
                intent.setPackage("com.autonavi.minimap");
                break;

            case BaseStringUtils.MapTenXun:
                //腾讯：http://lbs.qq.com/uri_v1/guide-route.html
                //腾讯    bus:公交, :drive: 步行:walk
                try {
                    intent = Intent.getIntent("qqmap://map/geocoder?coord="+latLng.latitude+","+latLng.longitude);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case BaseStringUtils.MapGuGe:
                //google    d:行车  w:步行  b:骑行
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address + "&mode=d");
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                break;
        }
        activity.startActivity(intent);
    }
}
