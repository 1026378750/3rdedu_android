package com.shengzhe.disan.xuetangparent.app;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.disanxuetang.media.util.MediaUtil;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.app.GreenDaoManager;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.exception.CrashHandler;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.shengzhe.disan.xuetangparent.utils.UmengUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hy on 2017/9/22.
 */
public  class MyApplication extends BaseApplication {
    private static Context mContext;
    private static MyApplication application;
    private static IWXAPI iwxApi;

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
                iwxApi = WXAPIFactory.createWXAPI(mContext, ConstantUrl.WX_APP_ID);
                iwxApi.registerApp(ConstantUrl.WX_APP_ID);
                //腾讯X5
                QbSdk.initX5Environment(contextApp,  null);
                //初始化DB(拷贝数据到数据库)
                GreenDaoManager.getInstance();
                //友盟
                UmengUtils.initUmeng();
                JPushInterface.setDebugMode(false);
                JPushInterface.init(application);
                //初始化百家云
                MediaUtil.getInstance().initBaiJia(application);
                PlatformConfig.setWeixin("wx5d452609f20dc076", "39fefcdd43a8867697dc6b0d2b283fc6");
                //新浪微博(第三个参数为回调地址)
                PlatformConfig.setSinaWeibo("4038352415", "ab874657c59840dbc7b267350b664f33","http://sns.whalecloud.com/sina2/callback");
                        //QQ
                        PlatformConfig.setQQZone("1106496187", "KEYCecxAZSDSy1ZfIb7");
                PlatformConfig.setAlipay("2017101109249035");
            }
        }).start();
        //百度地图
        SDKInitializer.initialize(application);
    }

    @Override
    public String getAppFrom() {
        return StringUtils.AppFromParent;
    }

    public static MyApplication getInstance() {
        return application;
    }
    /**
     * @return
     * 全局的上下文
     */
    public Context getmContext() {
        return mContext;
    }

    /***
     * 获取微信实例
     * @return
     */
    public IWXAPI getIwxApi(){
        return iwxApi;
    }

}
