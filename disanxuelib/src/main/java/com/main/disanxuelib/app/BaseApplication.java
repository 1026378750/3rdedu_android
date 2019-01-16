package com.main.disanxuelib.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import com.main.disanxuelib.util.NetworkUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by hy on 2017/9/22.
 */

public abstract class BaseApplication extends Application {
    private static BaseApplication mApplication;
    public static Context contextApp;
    //保存一些状态数据的SharedPreferences
    protected SharedPreferences mSettings;
    //Application为整个应用保存全局的RefWatcher
    private RefWatcher refWatcher;

    public static BaseApplication getInstance() {
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        contextApp = getApplicationContext();
        //内存泄漏检测
        refWatcher = setupLeakCanary();
        // 初始化mSettings
        getDefaultSharedPreference();


        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            }
        }).start();
    }

    //获取应用包名
    String MyPackageName;

    public String getMyPackageName() {
        if (MyPackageName == null) {
            MyPackageName = getPackageName();
        }
        return MyPackageName;
    }

    /**
     * 获取DefaultSharedPreference实例
     *
     * @return
     */
    public SharedPreferences getDefaultSharedPreference() {
        if (mSettings == null) {
            mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return mSettings;
    }

    private int netWarnning = 0;

    //内部类ConnectionChangeReceiver
    public class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtil.isNetWorkAviliable() && netWarnning < 1) {
                netWarnning++;
                ToastUtil.showShort("当前网络不可用");
            } else {
                netWarnning = 0;
            }
        }
    }

    @Override
    public void onLowMemory() {
        //ToastUtil.showDefaultGravityToast("低内存警告");
//		//如果内存不够用，就清空Activities，释放掉Activity的引用
        //AppManager.getAppManager().finishOtherButCurrentActivity();
        super.onLowMemory();

    }

    //内存泄漏检测
    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static Context getContext() {
        return contextApp;
    }

    public abstract String getAppFrom();

}
