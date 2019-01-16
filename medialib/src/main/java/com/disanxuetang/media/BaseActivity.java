package com.disanxuetang.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import com.disanxuetang.media.util.StringUtils;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemBarTintManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.ButterKnife;
import com.disanxuetang.media.R;
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
            tintManager.setStatusTextColor(true);
            tintManager.setTintAlpha(0f);
        }
    }

    public void setStatusTextColor(boolean useDart){
        if(tintManager!=null)
            tintManager.setStatusTextColor(useDart);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
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
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    //EventBus的回调方法。
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean onEventMainThread(Bundle bundle) {
        return false;
    }

}
