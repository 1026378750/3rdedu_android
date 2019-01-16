package com.shengzhe.disan.xuetangparent.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.app.MyApplication;
import com.shengzhe.disan.xuetangparent.mvp.activity.LoginActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineOneonOneDetailsActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.ApplyAuditionActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.LiveCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.VideoDeatilActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;

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
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    /*****
     * 登录返回处理
     * @param mPhone
     */
    public void LoginResult(String mPhone){
        if(context==null)
            context = MyApplication.getInstance().getmContext();
        context.startActivity(new Intent(context, MainActivity.class));
        if (tag!=null){
            ContextCompat.startActivity(this.context, new Intent(this.context, tag), null);
            return;
        }
        if(!tagName.equals(MainActivity.class.getName())&&!StringUtils.textIsEmpty(SharedPreferencesManager.getPhoneNum())&&!SharedPreferencesManager.getPhoneNum().equals(mPhone)){
            //号码不一致到首页
            jumpToMain();
            jumpToMime();
            SharedPreferencesManager.savePhoneNum(mPhone);
            return;
        }
        if(tagName.equals(MainActivity.class.getName())){
            MainActivityResult(true);
        }else if(tagName.equals(LiveCourseActivity.class.getName())){
            LiveDetailFragmentResult();
        }else if(tagName.equals(VideoDeatilActivity.class.getName())){
            VideoDeatilResult();
        } else if(tagName.equals(OfflineOneonOneDetailsActivity.class.getName())){
            OneonOneDetailsResult();
        }else if(tagName.equals(ApplyAuditionActivity.class.getName())) {
            if(SharedPreferencesManager.getUserInfo()!=null && SharedPreferencesManager.getUserInfo().getIsApplyCourseListen()==1){
                applyAuditionResult();
            }else {
                jumpToMain();
            }

        }
        jumpToMime();
        SharedPreferencesManager.savePhoneNum(mPhone);
    }

    /****
     * 取消登录
     */
    public void LoginCancel(){
        if(!tagName.equals(MainActivity.class.getName())){
            AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
        }
        MainActivityResult(false);
    }

    /****
     * 退出进首页
     */
    private void jumpToMain(){
        AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11007);
        EventBus.getDefault().post(bundle);
    }

    /*****
     * 首页登录返回
     */
    private void MainActivityResult(boolean isSuccess){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11006);
        bundle.putBoolean(StringUtils.EVENT_DATA,isSuccess);
        EventBus.getDefault().post(bundle);
    }

    /*****
     * 直播课课程详情
     */
    private void LiveDetailFragmentResult(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11008);
        EventBus.getDefault().post(bundle);
    }

    /*****
     * 直播课
     */
    private void OfflineTeacherResult(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11009);
        EventBus.getDefault().post(bundle);
    }

    /*****
     * 视频课详情
     */
    private void VideoDeatilResult(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11010);
        EventBus.getDefault().post(bundle);
    }

    /****
     * 线下一对一申请试听、立即购买返回处理
     */
    private void OneonOneDetailsResult(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11016);
        EventBus.getDefault().post(bundle);
    }

    /****
     * 线下一对一申请试听
     */
    private void  applyAuditionResult(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11017);
        EventBus.getDefault().post(bundle);
    }

    /****
     * 刷新我的
     */
    private void jumpToMime() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11013);
        EventBus.getDefault().post(bundle);
    }

    private Class<?> tag = null;
    public void jumpActivity(Context mContext, Class<?> tag){
        this.tag = tag;
        if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
            //尚未登录
            LoginRequest(mContext);
            return;
        }
        ContextCompat.startActivity(mContext, new Intent(mContext, tag), null);
    }

}
