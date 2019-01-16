package com.main.disanxuelib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 设置状态栏 on 2017/12/26.
 */

public class SystemBarTintManager {

    /**
     * 自定义状态栏色值
     */
    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private boolean mStatusBarAvailable = true;
    private boolean mNavBarAvailable = false;
    private boolean mStatusBarTintEnabled;
    private boolean mNavBarTintEnabled;
    private View mStatusBarTintView;
    private View mNavBarTintView;

    @TargetApi(19)
    public SystemBarTintManager() {
        Window win = AppManager.getAppManager().currentActivity().getWindow();
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if ((winParams.flags & bits) != 0) {
                mNavBarAvailable = true;
            }
        }
        if (!SystemInfoUtil.hasNavigtionBar()) {
            mNavBarAvailable = false;
        }

        if (mStatusBarAvailable) {
            setupStatusBarView(decorViewGroup);
        }
        if (mNavBarAvailable) {
            setupNavBarView(decorViewGroup);
        }
    }

    public void setStatusBarTintEnabled(boolean enabled) {
        mStatusBarTintEnabled = enabled;
        if (mStatusBarAvailable) {
            mStatusBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    public void setNavigationBarTintEnabled(boolean enabled) {
        mNavBarTintEnabled = enabled;
        if (mNavBarAvailable) {
            mNavBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    public void setTintColor(int color) {
        setStatusBarTintColor(color);
        setNavigationBarTintColor(color);
    }

    public void setTintResource(int res) {
        setStatusBarTintResource(res);
        setNavigationBarTintResource(res);
    }

    public void setTintDrawable(Drawable drawable) {
        setStatusBarTintDrawable(drawable);
        setNavigationBarTintDrawable(drawable);
    }

    public void setTintAlpha(float alpha) {
        setStatusBarAlpha(alpha);
        setNavigationBarAlpha(alpha);
    }

    /**
     * 状态栏颜色
     *
     * @param color
     */
    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    public void setStatusBarTintResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundResource(res);
        }
    }

    @SuppressWarnings("deprecation")
    public void setStatusBarTintDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundDrawable(drawable);
        }
    }

    @TargetApi(11)
    public void setStatusBarAlpha(float alpha) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView.setAlpha(alpha);
        }
    }

    public void setNavigationBarTintColor(int color) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundColor(color);
        }
    }

    public void setNavigationBarTintResource(int res) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundResource(res);
        }
    }

    @SuppressWarnings("deprecation")
    public void setNavigationBarTintDrawable(Drawable drawable) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundDrawable(drawable);
        }
    }

    @TargetApi(11)
    public void setNavigationBarAlpha(float alpha) {
        if (mNavBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mNavBarTintView.setAlpha(alpha);
        }
    }

    public boolean isStatusBarTintEnabled() {
        return mStatusBarTintEnabled;
    }

    public boolean isNavBarTintEnabled() {
        return mNavBarTintEnabled;
    }

    private void setupStatusBarView(ViewGroup decorViewGroup) {
        mStatusBarTintView = new View(AppManager.getAppManager().currentActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SystemInfoUtil.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        if (mNavBarAvailable && !SystemInfoUtil.isNavigationAtBottom()) {
            params.rightMargin = SystemInfoUtil.getNavigationBarWidth();
        }
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mStatusBarTintView);
    }

    private void setupNavBarView(ViewGroup decorViewGroup) {
        mNavBarTintView = new View(AppManager.getAppManager().currentActivity());
        LinearLayout.LayoutParams params;
        if (SystemInfoUtil.isNavigationAtBottom()) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SystemInfoUtil.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new LinearLayout.LayoutParams(SystemInfoUtil.getNavigationBarWidth(), LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
        }
        mNavBarTintView.setLayoutParams(params);
        mNavBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mNavBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mNavBarTintView);
    }

    /**
     * 设置状态栏文字色值为深色调
     * @param useDart 是否使用深色调
     */
    public void setStatusTextColor(boolean useDart) {
        Activity activity = AppManager.getAppManager().currentActivity();
        /*if (isFlyme()) {
            processFlyMe(useDart, activity);
        } else if (isMIUI()) {
            processMIUI(useDart, activity);
        } else {*/
            if (useDart) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            //activity.getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, SystemInfoUtil.getNavigationBarHeight());
        //}
    }


    private final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 判断手机是否是小米
     * @return
     */
    private boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 判断手机是否是魅族
     * @return
     */
    private boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private static void processFlyMe(boolean isLightStatusBar, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
            int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
            Field field = instance.getDeclaredField("meizuFlags");
            field.setAccessible(true);
            int origin = field.getInt(lp);
            if (isLightStatusBar) {
                field.set(lp, origin | value);
            } else {
                field.set(lp, (~value) & origin);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上  lightStatusBar为真时表示黑色字体
     */
    private static void processMIUI(boolean lightStatusBar, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags",int.class,int.class);
            extraFlagField.invoke(activity.getWindow(), lightStatusBar? darkModeFlag : 0, darkModeFlag);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

}
