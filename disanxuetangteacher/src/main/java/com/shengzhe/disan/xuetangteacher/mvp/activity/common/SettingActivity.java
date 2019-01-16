package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.SwitchButton;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.utils.HtmlJumpUtil;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.APKVersionUtil;
import com.shengzhe.disan.xuetangteacher.utils.UmengEventUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 设置
 *
 * liukui 2017/11/23 12:10
 *
 */

public class SettingActivity extends BaseActivity implements APKVersionUtil.OnVersionUpdateCallback {
    @BindView(R.id.ccb_setting_phone)
    CommonCrosswiseBar ccbPhone;
    @BindView(R.id.sw_signin)
    SwitchButton swSignin;
    @BindView(R.id.ccb_setting_version)
    CommonCrosswiseBar ccbVersion;
    HttpService httpService;
    private SystemPersimManage manage = null;
    private boolean isCanReturn = true;

    @Override
    public void initData() {
        isChecked = true;
        httpService  = Http.getHttpService();
        ccbPhone.setRightText(SharedPreferencesManager.getUserInfo().getMobile()==null?"":SharedPreferencesManager.getUserInfo().getMobile());
        ccbVersion.setRightText("v"+ SystemInfoUtil.getVersionName());
        getSigninStatus();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_setting;
    }

    private boolean isChecked = false;
    private void getSigninStatus(){
        httpService.userSetUpStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String messages) {
                        try {
                            isChecked = true;
                            swSignin.setChecked(new JSONObject(messages).optInt("isEnableSign")==1);
                        } catch (JSONException e) {

                        }
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }

    private void setSigninStatus(){
        Map<String, Object> map = new HashMap<>();
        //个人设置是否开启签到通知 1：是 2：否
        map.put("isEnableSign",swSignin.isChecked()?1:2);
        httpService.userSetUp(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String str) {
                        ToastUtil.showToast(str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.ccb_setting_touchour,R.id.tv_signin_layout,R.id.ccb_setting_version,R.id.ccb_setting_aboutour,R.id.ccb_setting_agreement,R.id.tv_setting_exit})
    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_signin_layout:
                if(!isChecked){
                    return;
                }
                swSignin.setChecked(!swSignin.isChecked());
                setSigninStatus();
                break;

            case R.id.ccb_setting_touchour:
                //联系我们（本地）
                startActivity(new Intent(this,SystemRelationActivity.class));
                break;

            case R.id.ccb_setting_version:
                //版本号(显示)
                isCanReturn = false;
                APKVersionUtil.getInstance(this).setUpdateCallback(this).checkVersion();
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
                //退出
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
                                UmengEventUtils.toLogoutClick(mContext,SharedPreferencesManager.getUserInfo().getMobile());
                                SharedPreferencesManager.clearDatas();
                                onBackPressed();
                                LoginOpentionUtil.getInstance().LoginRequest(mContext);
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

    @Override
    public void onUpdateSucced(String fileUrl) {
        instanllApk(fileUrl);
    }

    @Override
    public void onIgnoreUpdate() {
        isCanReturn = true;
        ToastUtil.showShort("当前已是最新版本");
    }

    @Override
    public void onUpdateFaild(String mesg) {
        isCanReturn = true;
        nodifyError(mesg);
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
        if (isCanReturn)
            onBackPressed();
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMustUpdate)//说明有新版本出现，不可以取消
                return false;
        }
        return false;
    }

    private boolean isMustUpdate = false;

    @Override
    public void onCanUpdate(String mesg, boolean bool) {
        isMustUpdate = bool;
        ConfirmDialog dialog = ConfirmDialog.newInstance("新版本来了", mesg, bool ? "" : "跳过", "升级");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void dialogStatus(int id) {
                switch (id) {

                    case R.id.tv_dialog_cancel:
                        isCanReturn = true;
                        break;

                    case R.id.tv_dialog_ok:
                        //升级
                        if (manage == null)
                            manage = new SystemPersimManage(mContext) {
                                @Override
                                public void resultPerm(boolean isCan, int requestCode) {
                                    if (isCan)
                                        APKVersionUtil.getInstance(mContext).checkApk();
                                }
                            };
                        manage.CheckedFile();
                        break;
                }

            }
        });
    }

    /**
     * 安装apk
     */
    private void instanllApk(String fileUrl) {
        File file = new File(fileUrl);
        if (file != null && file.exists()) {
            nodifyError("下载出错");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(this, "com.shengzhe.disan.xuetangteacher.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    private void nodifyError(String mesg) {
        ConfirmDialog dialog = ConfirmDialog.newInstance("系统提示", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                if (id == com.main.disanxuelib.R.id.tv_dialog_ok)
                    AppManager.getAppManager().AppExit();
            }
        });
    }

}
