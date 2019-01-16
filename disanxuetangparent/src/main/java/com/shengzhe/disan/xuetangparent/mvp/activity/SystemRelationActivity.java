package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.Gravity;
import android.view.View;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.HfFileUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.shengzhe.disan.xuetangparent.view.X5WebView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/23.
 *
 * 联系我们
 *
 */

public class SystemRelationActivity extends BaseActivity implements ConfirmDialog.ConfirmDialogListener{
    @BindView(R.id.sw_tv_servicecall)
    CommonCrosswiseBar tvServiceCall;
    @BindView(R.id.x5wv_connectwe)
    X5WebView x5WebView;

    private ConfirmDialog dialog;

    @Override
    public void initData() {
        tvServiceCall.setRightText(StringUtils.System_Service_Phone);
        x5WebView.loadDataWithBaseURL("file:///android_asset/", HfFileUtil.readAssetsByName(mContext, "contact.html", "utf-8"),"text/html","utf-8","");
    }

    @Override
    public int setLayout() {
        return R.layout.activity_relation;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.sw_tv_servicecall})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.sw_tv_servicecall:
                //客服电话
                if(dialog==null) {
                    dialog = ConfirmDialog.newInstance("", "您确定要拨打客服电话<br/><font color='#FFAE12'>"+StringUtils.System_Service_Phone+"</font>？", "取消", "立即拨打");
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(this);
                break;
        }
    }
    @Override
    public void dialogStatus(int id) {
        switch (id){
            case R.id.tv_dialog_ok:
                //确定
                SystemInfoUtil.callDialing(StringUtils.System_Service_Phone);
                break;
        }
    }
}
