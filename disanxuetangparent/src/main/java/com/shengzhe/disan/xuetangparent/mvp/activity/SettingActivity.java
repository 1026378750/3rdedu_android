package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.SwitchButton;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.SettingView;
import com.shengzhe.disan.xuetangparent.utils.HtmlJumpUtil;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.APKVersionUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 * <p>
 * liukui 2017/11/23 12:10
 */

public class SettingActivity extends BaseActivity implements SettingView.ISettingView {
    @BindView(R.id.ccb_setting_phone)
    CommonCrosswiseBar ccbPhone;
    @BindView(R.id.sw_signin)
    SwitchButton swSignin;
    @BindView(R.id.ccb_setting_version)
    CommonCrosswiseBar ccbVersion;

    private CommonPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.initSettingUi(getSupportFragmentManager());
        presenter.setSettingData();
        presenter.loadSettingDatas();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_setting_touchour, R.id.tv_signin_layout, R.id.ccb_setting_version, R.id.ccb_setting_aboutour, R.id.ccb_setting_agreement, R.id.tv_setting_exit})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_signin_layout:
                if (!presenter.isCanSignin()) {
                    return;
                }
                presenter.setSigninStatus();
                break;

            case R.id.ccb_setting_touchour:
                //联系我们（本地）
                startActivity(new Intent(this, SystemRelationActivity.class));
                break;

            case R.id.ccb_setting_version:
                //版本号(显示)
                presenter.checkVersion();
                break;

            case R.id.ccb_setting_aboutour:
                //关于我们(html)
                HtmlJumpUtil.gywmActivity();
                break;

            case R.id.ccb_setting_agreement:
                //服务协议(html)
                HtmlJumpUtil.fwxyActivity();
                break;

            case R.id.tv_setting_exit:
                if(backDialog==null) {
                    backDialog = ConfirmDialog.newInstance("", "您确定要退出吗？", "取消", "确定");
                }
                backDialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                backDialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

                    @Override
                    public void dialogStatus(int id) {
                        switch (id){
                            case R.id.tv_dialog_ok:
                                colseDialog();
                                presenter.loginOut();
                                break;
                        }
                    }
                });

                break;
        }
    }

    private ConfirmDialog backDialog;
    private void colseDialog() {
        if (backDialog != null && backDialog.isVisible()) {
            backDialog.dismiss();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                APKVersionUtil.getInstance(mContext).checkApk();
            } else {
                showPremissionDialog("访问设备存储空间");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (presenter.isCanReturn())
            onBackPressed();
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (presenter.isMustUpdate())//说明有新版本出现，不可以取消
                return false;
        }
        return false;
    }



    @Override
    public CommonCrosswiseBar getPhoneView() {
        return ccbPhone;
    }

    @Override
    public SwitchButton getSigninView() {
        return swSignin;
    }

    @Override
    public CommonCrosswiseBar getVersionView() {
        return ccbVersion;
    }
}
