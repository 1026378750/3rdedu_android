package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/29.
 */

public class PayResultActivity extends BaseActivity {
    @BindView(R.id.ccb_payresult_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.tv_payresult_status)
    TextView mStatus;

    private int ORDER_ID = 0;
    private int COURSE_TYPE = 0;

    @Override
    public void initData() {
        //1为一对1，2为直播课 3为视频课
        COURSE_TYPE =  getIntent().getIntExtra(StringUtils.COURSE_TYPE, 0);
        ORDER_ID=  getIntent().getIntExtra(StringUtils.ORDER_ID, 0);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_payresult;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_payresult_goorder, R.id.tv_payresult_gomain})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_payresult_goorder:
                AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                //去订单  分为3种情况下的订单详情
                Intent intent = getIntent();
                intent.putExtra(StringUtils.ORDER_ID, ORDER_ID);
                intent.putExtra(StringUtils.COURSE_TYPE, COURSE_TYPE);
                //订单详情
                if (COURSE_TYPE == 1) {
                    //线下1对1
                    intent.setClass(mContext, MineOfflineOrderActivity.class);
                    startActivity(intent);
                } else if (COURSE_TYPE == 2) {
                    //在线直播课
                    intent.setClass(mContext, MIneLiveOrderActivity.class);
                    startActivity(intent);
                } else if (COURSE_TYPE == 3) {
                    //视频课
                    intent.setClass(mContext, MineVideoOrderActivity.class);
                    startActivity(intent);
                }else if (COURSE_TYPE == 4) {
                    //线下班课
                    intent.setClass(mContext, MIneOfflineClassDetailsActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.tv_payresult_gomain:
                //去首页
                AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
