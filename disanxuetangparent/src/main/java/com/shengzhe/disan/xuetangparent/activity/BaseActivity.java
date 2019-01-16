package com.shengzhe.disan.xuetangparent.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import com.bumptech.glide.Glide;
import com.common.camera.activity.AlbumActivity;
import com.common.camera.activity.CameraActivity;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.main.disanxuelib.util.SystemBarTintManager;
import com.shengzhe.disan.xuetangparent.utils.UmengUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类
 * Created by hy
 * 2017/10/24 18:48
 * Note :
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public static String tag;
    protected Context mContext;
    public Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        mContext = this;
        if (AppManager.getAppManager().isTopActivity(this.getClass())){
            onBackPressed();
            return;
        }
        AppManager.getAppManager().addActivity(this);

        initWindow();
        if (setLayout() != 0) {
            if (setLayout() == 0) {
                initData();
                return;
            }
            View view = getLayoutInflater().inflate(setLayout(), null);
            unbinder = ButterKnife.bind(this, view);
            //  注册时会自动从当前类里面拿取注释为@Produce 和 @Subscribe，解析处理，会发送出来消息
            if (StringUtils.RxBusActivityNames.contains(this.getClass().getName()) && !EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            setContentView(view);
        }
        initData();
    }
    private SystemBarTintManager tintManager;
    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            tintManager = new SystemBarTintManager();
            tintManager.setTintResource(R.drawable.top_bar_ffff);
            tintManager.setTintAlpha(0f);
        }
        tintManager.setStatusTextColor(true);
    }

    public void setStatusTextColor(boolean useDart){
        if(tintManager!=null)
            tintManager.setStatusTextColor(useDart);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        Glide.get(this).clearMemory();
        if (StringUtils.RxBusActivityNames.contains(this.getClass().getName())) {
            //销毁
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 数据初始化
     * 默认在onStart中调用
     * 建议增加数据恢复判断，配合onSaveInstanceState使用
     */
    public abstract void initData();

    /*****
     * 绑定布局
     * @return
     */
    public abstract int setLayout();

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPauseToActivity(mContext);
    }


    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResumeToActivity(mContext);
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case IntegerUtil.PERMISSION_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(mContext, CameraActivity.class));
                } else if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    //如果用户点击了不在提醒, 并拒绝之后
                    showPremissionDialog("系统相机、录制视频权限");
                }
                break;

            case IntegerUtil.PERMISSION_REQUEST_ALBUM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(mContext, AlbumActivity.class));
                } else if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    //如果用户点击了不在提醒, 并拒绝之后
                    showPremissionDialog("文件读取权限");
                }
                break;
        }
    }

    //EventBus的回调方法。
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean onEventMainThread(Bundle bundle) {
        return false;
    }

    /****
     * 如果拒绝授予权限,且勾选了再也不提醒
     * @param contentStr
     */
    public void showPremissionDialog(String contentStr) {
        ConfirmDialog dialog =  ConfirmDialog.newInstance("提示","您已禁止了 "+ contentStr + "<br/>设置路径：设置 ->应用管理 ->" + SystemInfoUtil.getApplicationName() + " ->权限", "取消", "设置");
        dialog.setMessageGravity(Gravity.LEFT);
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

            @Override
            public void dialogStatus(int id) {
                if (id == com.main.disanxuelib.R.id.tv_dialog_ok) {//设置界面
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    ActivityCompat.startActivityForResult((Activity) mContext,intent,IntegerUtil.PERMISSION_REQUEST_SETTING,null);
                }
            }
        });
    }

}
