package com.shengzhe.disan.xuetangparent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineMessageActivity;
import com.shengzhe.disan.xuetangparent.utils.MainOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.disanxuetang.shortcutbadger.ShortcutBadger;
import cn.jpush.android.api.JPushInterface;

/******
 * 消息推送广播接收处理
 */
public class MyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "消息推送";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String pushAction = intent.getAction();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(pushAction)) {
            // Logger.d(TAG, "JPush用户注册成功");
            Log.d(TAG, "JPush用户注册成功 接收Registration Id : " + bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
        }else if(JPushInterface.EXTRA_CONNECTION_CHANGE.equals(pushAction)){
            Log.d(TAG, "推送连接 : " + bundle.getBoolean(JPushInterface.EXTRA_CONNECTION_CHANGE, false));
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(pushAction)) {
            Log.d(TAG, "接受到推送下来的自定义消息");
        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(pushAction)) {
            //接收到推送下来的通知
            Log.e(TAG,"**************extra***************ACTION_NOTIFICATION_RECEIVED");
            onReceivedMessage(context,bundle);
        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            // 用户点击打开了通知
            Log.e(TAG,"**************extra***************ACTION_NOTIFICATION_OPENED");
            //打开相应的Notification
           onOpenNotification(context, bundle);
        }
    }

    private void onReceivedMessage(Context context,Bundle bundle) {
        final String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        final String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        final int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        final String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        int badgeCount = SharedPreferencesManager.getBadgeCount();
        badgeCount++;
        SharedPreferencesManager.setBadgeCount(badgeCount);
        boolean success = ShortcutBadger.applyCount(context, badgeCount);
        Log.e(TAG,"**************extra***************onReceivedMessage:"+success);
    }

    private void onOpenNotification(Context context, Bundle bundle) {
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        MainOpentionUtil.getInstance().jumpToMessageActivity(context,bundle);
    }
}
