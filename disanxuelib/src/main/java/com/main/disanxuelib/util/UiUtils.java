package com.main.disanxuelib.util;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.main.disanxuelib.R;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.view.CustomGridLayoutManager;
import com.main.disanxuelib.view.CustomLinearLayoutManager;

public class UiUtils {

    public enum LayoutManager{
        HORIZONTAL,
        VERTICAL;
    }

    private static final int ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int CHECKED_ATTR = android.R.attr.state_checked;
    private static final int PRESSED_ATTR = android.R.attr.state_pressed;

    public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {PRESSED_ATTR, -CHECKED_ATTR},
                {PRESSED_ATTR, CHECKED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {CHECKED_ATTR, PRESSED_ATTR},
                {-CHECKED_ATTR, PRESSED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }

    /******
     * 获取资源颜色
     * @param color
     * @return
     * liukui 2017/06/23 10:38
     */
    public static int getColor(int color){
        return ContextCompat.getColor(BaseApplication.getContext(),color);
    }

    public static ColorStateList getColorStateList(int color){
        return ContextCompat.getColorStateList(BaseApplication.getContext(),color);
    }

    /*****
     * 获取本地资源文件
     * @param resources
     * @return
     *
     * liukui 2017/06/23 10:38
     *
     */
    public static Drawable getDrawable(int resources){
        return ContextCompat.getDrawable(BaseApplication.getContext(),resources);
    }

    /*****
     * 格式化字符串
     * @param strRes
     * @param str
     * @return
     *
     * liukui 2017/06/23 10:38
     *
     */
    public static String getFormatString(int strRes ,Object str){
        return String.format(BaseApplication.getContext().getResources().getString(strRes), str);
    }


    /**
     * 根据百分比改变颜色透明度
     */
    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    /****
     * 获取RecyclerView布局
     * @param orientation
     * @return
     */
    public static RecyclerView.LayoutManager getLayoutManager(LayoutManager orientation){
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(BaseApplication.getContext());
        switch (orientation){
            case HORIZONTAL:
                linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
                break;

            case VERTICAL:
                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                break;
        }
        return linearLayoutManager;
    }

    public static RecyclerView.LayoutManager getGridLayoutManager(int spanCount){
        return new CustomGridLayoutManager(BaseApplication.getContext(),spanCount);
    }

    public static String getString(int resId) {
        return BaseApplication.getContext().getString(resId);
    }

    public static float getDimension(int resId) {
        return BaseApplication.getContext().getResources()
                .getDimension(resId);
    }

}
