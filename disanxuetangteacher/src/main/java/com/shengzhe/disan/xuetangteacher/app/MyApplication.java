package com.shengzhe.disan.xuetangteacher.app;

import android.content.Context;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.app.GreenDaoManager;
import com.shengzhe.disan.xuetangteacher.exception.CrashHandler;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import com.shengzhe.disan.xuetangteacher.utils.UmengUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hy on 2017/9/22.
 */
public  class MyApplication extends BaseApplication {
    private static Context mContext;
    static MyApplication application;
    public   long pauseTime = 0;
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mContext = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //全局错误信息
                CrashHandler crashHandler = CrashHandler.getInstance();
                crashHandler.init(application);
                Thread.setDefaultUncaughtExceptionHandler(crashHandler);
                //腾讯X5
                QbSdk.initX5Environment(contextApp, null);
                //初始化DB(拷贝数据到数据库)
                GreenDaoManager.getInstance();
                //友盟
                UmengUtils.initUmeng();
                //友盟错误统计
                MobclickAgent.setDebugMode(true);
                JPushInterface.setDebugMode(false);
                JPushInterface.init(application);
                PlatformConfig.setWeixin("wxfe473c4f012aac9d", "0e12be7eebb88f5c265e8f83b018269a");
                //新浪微博(第三个参数为回调地址)
                PlatformConfig.setSinaWeibo("1313088549", "87adb092dad02d35e64197b7e74a7a9c","http://sns.whalecloud.com/sina2/callback");
                        //QQ
                        PlatformConfig.setQQZone("1106864438", "UKih95anOV0PFSAZ");
                PlatformConfig.setAlipay("2018042002585751 ");
            }
        }).start();

    }

    @Override
    public String getAppFrom() {
        return StringUtils.AppFromTeacher;
    }

    public static MyApplication getInstance() {
        return application;
    }

    public   void onAppPause() {
        pauseTime = System.currentTimeMillis();
    }


    public   void onAppResume() {
        pauseTime =0;
    }

    public Context getmContext() {
        return mContext;
    }
}
