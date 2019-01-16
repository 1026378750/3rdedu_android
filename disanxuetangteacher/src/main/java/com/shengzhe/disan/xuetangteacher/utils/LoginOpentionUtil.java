package com.shengzhe.disan.xuetangteacher.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangteacher.activity.MainActivity;
import com.shengzhe.disan.xuetangteacher.app.MyApplication;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.LoginActivity;
import org.greenrobot.eventbus.EventBus;
import java.util.Calendar;

/**
 * Created by liukui on 2017/12/14.
 *
 * 公共用户登录处理
 *
 */

public class LoginOpentionUtil {
    private static LoginOpentionUtil model;
    private String tagName;
    private Context context;

    /*****
     * 初始化
     * @return
     */
    public static LoginOpentionUtil getInstance(){
        if(model==null){
             model = new LoginOpentionUtil();
        }
        return model;
    }

    /******
     * 登录请求
     * @param context
     */
    private  long currentTime = 0;
    public void LoginRequest(Context context){
        this.context = context;
        long time = Calendar.getInstance().getTimeInMillis();
        if(time-currentTime<100){
            return;
        }
        currentTime = time;
        this.tagName = context.getClass().getName();
        //判断当前登录界面是否在栈顶
        if(AppManager.getAppManager().currentActivity() instanceof LoginActivity) {
            AppManager.getAppManager().currentActivity().finish();
        }
        Intent intent=new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*****
     * 登录返回处理
     * @param mPhone
     */
    public void LoginResult(String mPhone){
        if (tag!=null){
            jumpMessage(context);
            return;
        }
        //关闭所有之前界面
        AppManager.getAppManager().finishAllActivity();
        if(context==null)
            context = MyApplication.getInstance().getmContext();
        context.startActivity(new Intent(context, MainActivity.class));
        SharedPreferencesManager.savePhoneNum(mPhone);
    }

    /****
     * 首页数据
     */
    private void RefreshMainDatas() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11002);
        EventBus.getDefault().post(bundle);
    }


    private Class<?> tag = null;
    public void jumpActivity(Context mContext, Class<?> tag){
        this.tag = tag;
        if (StringUtils.textIsEmpty(SharedPreferencesManager.getUserToken())) {
            //尚未登录
            LoginRequest(mContext);
            return;
        }
        jumpMessage(mContext);
    }

    private void jumpMessage(final Context mContext){
        if (!AppManager.getAppManager().isHasActivity(MainActivity.class)){
            //不包含ManActivity先跳转到MainActivity
            ContextCompat.startActivity(mContext, new Intent(mContext, MainActivity.class), null);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContextCompat.startActivity(mContext, new Intent(mContext, tag), null);
            }
        },500);
    }

}
