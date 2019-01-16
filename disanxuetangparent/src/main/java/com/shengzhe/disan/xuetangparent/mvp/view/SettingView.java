package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.SwitchButton;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.utils.APKVersionUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.io.File;

/**
 * Created by 设置界面 on 2018/4/19.
 */

public class SettingView extends BaseView implements APKVersionUtil.OnVersionUpdateCallback{
    private SystemPersimManage manage = null;
    private boolean isCanReturn = true;
    private boolean isMustUpdate = false;
    private FragmentManager manager;

    public SettingView(Context context, FragmentManager manager) {
        super(context);
        this.manager = manager;
    }

    private ISettingView iView;

    public void setISettingView(ISettingView view) {
        this.iView = view;
    }

    public void setDatas() {
        iView.getPhoneView().setRightText(SharedPreferencesManager.getUserInfo().getMobile() == null ? "" : SharedPreferencesManager.getUserInfo().getMobile());
        iView.getVersionView().setRightText("v" + SystemInfoUtil.getVersionName());
    }

    public void setSigninStatus(boolean signinStatus) {
        iView.getSigninView().setChecked(signinStatus);
    }

    public void checkVersion() {
        isCanReturn = false;
        APKVersionUtil.getInstance(mContext).setUpdateCallback(this).checkVersion();
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

    @Override
    public void onCanUpdate(String mesg, boolean bool) {
        isMustUpdate = bool;
        mesg = mesg.replace("\r\n","<br/>");
        mesg = mesg.replace("\n","<br/>");
        ConfirmDialog dialog = ConfirmDialog.newInstance("新版本来了", mesg, bool ? "" : "跳过", "升级");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(manager);
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
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.shengzhe.disan.xuetangparent.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
        isCanReturn = true;
    }

    private void nodifyError(String mesg) {
        ConfirmDialog dialog = ConfirmDialog.newInstance("系统提示", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(manager);
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                if (id == com.main.disanxuelib.R.id.tv_dialog_ok)
                    AppManager.getAppManager().AppExit();
            }
        });
    }

    public boolean isCanReturn() {
        return isCanReturn;
    }

    public boolean isMustUpdate() {
        return isMustUpdate;
    }


    public interface ISettingView {
        CommonCrosswiseBar getPhoneView();

        SwitchButton getSigninView();

        CommonCrosswiseBar getVersionView();

    }

}
