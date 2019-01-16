package com.disanxuetang.media.util;

import android.content.Context;
import android.content.Intent;
import com.disanxuetang.media.activity.BaiJiaActivity;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ActivityAppManage {

    private static ActivityAppManage activityAppManage = null;
    private static Context context;

    public static ActivityAppManage getInstance(Context mContext){
        if (activityAppManage==null)
            activityAppManage = new ActivityAppManage();
        context = mContext;
        return activityAppManage;
    }

    public void goBaiJiaVideoActivity(int videoBjyId,String videoBjyToken){
        Intent intent = new Intent(context, BaiJiaActivity.class);
        intent.putExtra("videoBjyId",videoBjyId);
        intent.putExtra("videoBjyToken",videoBjyToken);
        context.startActivity(intent);

    }

}
