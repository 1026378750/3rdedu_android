package com.shengzhe.disan.xuetangteacher.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.disanxuetang.shortcutbadger.ShortcutBadger;
import com.shengzhe.disan.xuetangteacher.activity.MainActivity;
import com.shengzhe.disan.xuetangteacher.utils.MainOpentionUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import cn.jpush.android.api.JPushInterface;

/*****
 * 推送接收服务
 */
public class MyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "消息推送";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String pushAction = intent.getAction();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            //接收到推送下来的通知
            //处理接收到的信息
            Log.e(TAG,"**************extra***************ACTION_NOTIFICATION_RECEIVED");
            onReceivedMessage(context,bundle);
        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            // 用户点击打开了通知
            Log.e(TAG,"**************extra***************ACTION_NOTIFICATION_OPENED");
            //打开相应的Notification
            onOpenNotification(context, bundle);
        }
    }

    private void onReceivedMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
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
