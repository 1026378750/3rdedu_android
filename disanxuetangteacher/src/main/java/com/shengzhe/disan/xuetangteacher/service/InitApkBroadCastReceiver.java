package com.shengzhe.disan.xuetangteacher.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.FileUtil;
import com.main.disanxuelib.util.LogUtils;
import com.shengzhe.disan.xuetangteacher.utils.APKVersionUtil;

/******
 * APK文件安装成功过后删除原来文件
 *
 * liukui 2018/03/23
 *
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            //监听到系统广播添加
            FileUtil.rmoveAPKFile(APKVersionUtil.getInstance(context).getAPKPath());
            AppManager.getAppManager().AppExit();
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            //监听到系统广播移除
            FileUtil.rmoveAPKFile(APKVersionUtil.getInstance(context).getAPKPath());
            AppManager.getAppManager().AppExit();
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            //监听到系统广播替换
            FileUtil.rmoveAPKFile(APKVersionUtil.getInstance(context).getAPKPath());
            AppManager.getAppManager().AppExit();
        }
    }

}
