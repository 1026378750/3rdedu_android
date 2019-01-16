package com.shengzhe.disan.xuetangparent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by acer on 2017/11/3.
 */

public class AppRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        // 将该app注册到微信
        api.registerApp(ConstantUrl.WX_APP_ID);
    }
}
