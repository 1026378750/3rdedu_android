package com.shengzhe.disan.xuetangparent.utils;

import android.content.Context;

import com.main.disanxuelib.util.SystemInfoUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by acer on 2018/1/22.
 */

public class UmengEventUtils {

    private static final String DEVICEID = "deviceid";
    private static final String MAC = "mac";
    private static final String PHONEMODEL = "phone_model";
    private static final String PHONE = "phone";


    /**
     * 首次安装
     *
     * @param ctx
     */
    public static void toInstallClick(Context ctx) {
        HashMap<String, String> map = getInstallMap();
        MobclickAgent.onEvent(ctx, "install", map);
    }

    /**
     * 登录
     *
     * @param ctx
     * @param phone
     */
    public static void toLoginClick(Context ctx, String phone) {
        HashMap<String, String> map = getInstallMap();
        map.put(PHONE, phone);
        MobclickAgent.onEvent(ctx, "login", map);
    }

    /**
     * 退出
     *
     * @param ctx
     * @param userId
     */
    public static void toLogoutClick(Context ctx, String userId) {
        HashMap<String, String> map = getInstallMap();
        map.put(PHONE, userId);
        MobclickAgent.onEvent(ctx, "logout", map);
    }



    private static HashMap<String, String> getInstallMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(PHONEMODEL, SystemInfoUtil.getSystemModel());
        map.put(DEVICEID, SystemInfoUtil.getInstallationId());
        return map;
    }
}
