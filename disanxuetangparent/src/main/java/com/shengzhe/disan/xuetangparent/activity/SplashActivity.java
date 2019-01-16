package com.shengzhe.disan.xuetangparent.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.UmengEventUtils;
import com.shengzhe.disan.xuetangparent.utils.APKVersionUtil;
import java.io.File;

import cn.jpush.android.api.JPushInterface;

/******
 * 闪屏页
 */
public class SplashActivity extends BaseActivity implements APKVersionUtil.OnVersionUpdateCallback {
    private SystemPersimManage manage = null;
    @Override
    public void initData() {
        //设置别名
        //JPushInterface.setAlias(SplashActivity.this, "qaz123456", null);
        ConstantUrl.TOKN = SharedPreferencesManager.getUserInfo()==null?"":SharedPreferencesManager.getUserInfo().getToken();
        UmengEventUtils.toInstallClick(mContext);
        APKVersionUtil.getInstance(this).setUpdateCallback(this).checkVersion();
    }

    private void switchLoginAndMain(){
        //是否第一次安装
        if(SharedPreferencesManager.getIsFirstEnter())
            startActivity(new Intent(SplashActivity.this, GuideActivity.class) );
        else
            startActivity(new Intent(this,MainActivity.class));
        onBackPressed();
    }

    @Override
    public int setLayout() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_FILE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                APKVersionUtil.getInstance(mContext).checkApk();
            } else {
                showPremissionDialog("访问设备存储空间");
            }
        }
    }

    long startTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMustUpdate)//说明有新版本出现，不可以取消
                return false;
            if (System.currentTimeMillis() - startTime < 2000) {
                AppManager.getAppManager().AppExit();
            } else {
                startTime = System.currentTimeMillis();
                Toast.makeText(this, "再次点击退出应用程序", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private boolean isMustUpdate = false;
    @Override
    public void onCanUpdate(String mesg, boolean bool) {
        isMustUpdate = bool;
        mesg = mesg.replace("\r\n","<br/>");
        mesg = mesg.replace("\n","<br/>");
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
                        //不升级
                        switchLoginAndMain();
                        break;

                    case R.id.tv_dialog_ok:
                        //升级
                        if(manage==null)
                            manage = new SystemPersimManage(mContext){
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

    @Override
    public void onUpdateSucced(String fileUrl) {
        instanllApk(fileUrl);
    }

    @Override
    public void onIgnoreUpdate() {
        switchLoginAndMain();
    }

    @Override
    public void onUpdateFaild(String mesg) {
        nodifyError(mesg);
    }

    /**
     * 安装apk
     */
    private void instanllApk(String fileUrl) {
        File file = new File(fileUrl);
        if (file == null || !file.exists()) {
            nodifyError("下载出错");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(this, "com.shengzhe.disan.xuetangparent.fileprovider", file);
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
