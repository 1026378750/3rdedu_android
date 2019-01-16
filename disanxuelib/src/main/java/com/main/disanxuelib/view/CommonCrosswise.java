package com.main.disanxuelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.disanxuelib.R;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;

/**
 * 自定义公共横向布局
 *
 * hy 2017/11/22 10:21
 *
 */

public class CommonCrosswise extends RelativeLayout {
    /**
     * 标题栏
     */
    private View common_bar;
    /**
     * 标题栏的左边按返回按钮
     */
    private TextView left_button;
    /**
     * 标题栏的右边按保存按钮
     */
    private TextView right_button;
    /**
     * 标题栏的中间的文字
     */
    private TextView title;

    /*****
     * 搜索输入框
     */
    private EditText etSearch;
    /*****
     * 右边图片
     */
    private ImageView rightImage;
    /**
     * 标题栏的背景颜色
     */
    private int background_color;
    /**
     * 标题栏的背景资源
     */
    private int background_color_res;
    /**
     * 标题栏的显示的标题文字
     */
    private String title_text;
    /**
     * 标题栏的显示的标题文字颜色
     */
    private int title_textColor;
    /**
     * 标题栏的显示的标题文字大小
     */
    private float title_textSize;

    /******
     * 搜索框文字
     */
    private String title_edit;

    /****
     * 搜索框隐藏文字
     */
    private String title_editHint;
    /****
     * 搜索框文字颜色
     */
    private int title_editColor;
    /****
     * 搜索框隐藏文字颜色
     */
    private int title_editHintColor;
    /****
     * 搜索框文字大小
     */
    private float title_editSize;

    /**
     * 返回按钮的资源图片
     */
    private int left_button_imageId;
    /**
     * 返回按钮上显示的文字
     */
    private String left_button_text;
    /**
     * 返回按钮上显示的文字颜色
     */
    private int left_button_textColor;
    /**
     * 返回按钮上显示的文字大小
     */
    private float left_button_textSize;
    /***
     * 左边图片大小
     */
    private float left_button_size;
    /**
     * 是否显示返回按钮
     */
    private boolean show_left_button = false;
    /***
     * 右边图片
     */
    private int right_image_imageId;

    /*****
     * 是否显示右边图片
     */
    private boolean show_right_image = false;

    /**
     * 右边保存按钮的资源图片
     */
    private int right_button_imageId;
    /**
     * 右边保存按钮的文字
     */
    private String right_button_text;
    /*****
     * 影藏右边标签默认提示语
     */
    private String right_button_hintText;
    /**
     * 右边保存按钮的文字颜色
     */
    private int right_button_textColor;
    /*****
     * 隐藏便签默认字体颜色
     */
    private int right_button_textHintColor;
    /**
     * 右边保存按钮的文字大小
     */
    private float right_button_textSize;
    /*****
     * 右边图片大小
     */
    private float right_button_size;
    /**
     * 是否显示右边保存按钮
     */
    private boolean show_right_button = false;

    public CommonCrosswise(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**加载布局文件*/
        LayoutInflater.from(context).inflate(R.layout.common_crosswise, this, true);
        common_bar = findViewById(R.id.common_bar);
        left_button = (TextView) findViewById(R.id.common_bar_leftBtn);
        right_button = (TextView) findViewById(R.id.common_bar_rightBtn);
        title = (TextView) findViewById(R.id.common_bar_title);
        etSearch = (EditText) findViewById(R.id.common_bar_et_search);
        rightImage = (ImageView) findViewById(R.id.common_bar_rightImage);

        /**获取属性值*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonCrosswiseBar);

        /**标题相关*/
        background_color = typedArray.getColor(R.styleable.CommonCrosswiseBar_bar_background, Color.WHITE);
        background_color_res = typedArray.getResourceId(R.styleable.CommonCrosswiseBar_bar_background_res, -1);

        title_text = typedArray.getString(R.styleable.CommonCrosswiseBar_title_text);
        title_textColor = typedArray.getColor(R.styleable.CommonCrosswiseBar_title_textColor, Color.BLACK);
        title_textSize = typedArray.getDimension(R.styleable.CommonCrosswiseBar_title_textSize, 22);

        /***搜索****/
        title_edit = typedArray.getString(R.styleable.CommonCrosswiseBar_title_edittext);
        title_editHint = typedArray.getString(R.styleable.CommonCrosswiseBar_title_edithinttext);
        title_editColor = typedArray.getColor(R.styleable.CommonCrosswiseBar_title_editColor, Color.BLACK);
        title_editHintColor = typedArray.getColor(R.styleable.CommonCrosswiseBar_title_edithintColor, Color.BLACK);
        title_editSize = typedArray.getDimension(R.styleable.CommonCrosswiseBar_title_editSize, 22);

        /**左边按钮相关*/
        left_button_imageId = typedArray.getResourceId(R.styleable.CommonCrosswiseBar_left_button_image, R.mipmap.ic_black_left_arrow);
        left_button_text = typedArray.getString(R.styleable.CommonCrosswiseBar_left_button_text);
        left_button_textColor = typedArray.getColor(R.styleable.CommonCrosswiseBar_left_button_textColor, Color.WHITE);
        left_button_textSize = typedArray.getDimension(R.styleable.CommonCrosswiseBar_left_button_textSize, 20);
        left_button_size = typedArray.getDimension(R.styleable.CommonCrosswiseBar_left_button_size, 15);
        show_left_button = typedArray.getBoolean(R.styleable.CommonCrosswiseBar_show_left_button, false);

        /***右边图片***/
        right_image_imageId = typedArray.getResourceId(R.styleable.CommonCrosswiseBar_right_image, R.mipmap.ic_black_right_arrow);
        show_right_image = typedArray.getBoolean(R.styleable.CommonCrosswiseBar_show_right_image, false);

        /**右边按钮相关*/
        right_button_imageId = typedArray.getResourceId(R.styleable.CommonCrosswiseBar_right_button_image, R.mipmap.ic_black_right_arrow);
        right_button_text = typedArray.getString(R.styleable.CommonCrosswiseBar_right_button_text);
        right_button_hintText = typedArray.getString(R.styleable.CommonCrosswiseBar_right_button_hintText);
        right_button_textColor = typedArray.getColor(R.styleable.CommonCrosswiseBar_right_button_textColor, Color.WHITE);
        right_button_textHintColor = typedArray.getColor(R.styleable.CommonCrosswiseBar_right_button_textHintColor, Color.WHITE);
        right_button_textSize = typedArray.getDimension(R.styleable.CommonCrosswiseBar_right_button_textSize, 20);
        right_button_size = typedArray.getDimension(R.styleable.CommonCrosswiseBar_right_button_size, 15);
        show_right_button = typedArray.getBoolean(R.styleable.CommonCrosswiseBar_show_right_button, false);
        isTopBar = typedArray.getBoolean(R.styleable.CommonCrosswiseBar_isTopBar, false);

        setParameter();
        setBackground();
        setLeftButton();
        setTitle();
        setSearch();
        setRightImage();
        setRightButton();
    }

    //是否头部导航
    private boolean isTopBar = false;

    /*****
     * 设置偏移量
     */
    private void setParameter() {
        if (!isTopBar)
            return;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemInfoUtil.dip2px(45) + SystemInfoUtil.getStatusBarHeight());
        //params.setMargins(0, SystemInfoUtil.getStatusBarHeight(), 0, 0);
        common_bar.setPadding(0,common_bar.getPaddingTop()+SystemInfoUtil.getStatusBarHeight(),0,0);
        common_bar.setLayoutParams(params);
    }

    /**
     * 设置背景颜色
     */
    public void setBackground() {
        if (background_color_res == -1)
            common_bar.setBackgroundColor(background_color);
        else
            common_bar.setBackgroundResource(background_color_res);
    }

    /**
     * 设置左边按钮
     */
    public void setLeftButton() {
        if (!TextUtils.isEmpty(left_button_text)) {//返回按钮显示为文字
            left_button.setText(left_button_text);
            left_button.setTextColor(left_button_textColor);
            left_button.setTextSize(left_button_textSize);
        } else {
            left_button.setText("");
        }
        if (show_left_button) {
            //left_button.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(left_button_imageId),null,null,null);
            ImageUtil.setCompoundDrawable(left_button, (int) left_button_size, left_button_imageId, Gravity.LEFT, 0);
        }
        //是否显示
        left_button.setVisibility(!TextUtils.isEmpty(left_button_text) || show_left_button ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题
     */
    public void setTitle() {
      title.setText(title_text);
       title.setTextColor(title_textColor);
      title.setTextSize(title_textSize);
        //是否显示
        //title.setVisibility(TextUtils.isEmpty(title_text) ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * 设置输入框
     */
    public void setSearch() {
        etSearch.setText(title_edit);
        etSearch.setText(title_editHint);
        etSearch.setTextColor(title_editColor);
        etSearch.setTextColor(title_editHintColor);
        etSearch.setTextSize(title_editSize);
        //是否显示
        etSearch.setVisibility(TextUtils.isEmpty(title_edit) && TextUtils.isEmpty(title_editHint) ? View.INVISIBLE : View.VISIBLE);
    }

    /******
     * 设置右边图片
     */
    public void setRightImage() {
        if (show_right_image) {
            rightImage.setImageResource(right_image_imageId);
            rightImage.setVisibility(View.VISIBLE);
        } else {
            rightImage.setVisibility(View.GONE);
        }
    }

    public void setRightButton(int right_button_imageId) {
        this.right_button_imageId = right_button_imageId;
        ImageUtil.setCompoundDrawable(right_button, (int) left_button_size, right_button_imageId, Gravity.RIGHT, 0);
    }


    public void setLeftButton(int left_button_imageId) {
        this.left_button_imageId = left_button_imageId;
        ImageUtil.setCompoundDrawable(left_button, (int) left_button_size, left_button_imageId, Gravity.LEFT, 0);
    }

    public void setOnClickListener(int resId, OnClickListener listener) {
        findViewById(resId).setOnClickListener(listener);
    }

    /******
     * 设置是否显示标题
     */
    public void setRightButton() {
        if (!TextUtils.isEmpty(right_button_text)) {//返回按钮显示为文字
            right_button.setText(right_button_text);
            right_button.setTextColor(right_button_textColor);
            right_button.setTextSize(right_button_textSize);
        } else {
            right_button.setText("");
        }
        right_button.setHint(right_button_hintText);
        right_button.setHintTextColor(right_button_textHintColor);
        if (show_right_button) {
            //right_button.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(right_button_imageId),null);
            ImageUtil.setCompoundDrawable(right_button, (int) right_button_size, right_button_imageId, Gravity.RIGHT, 0);
        }
        //是否显示
        right_button.setVisibility(!TextUtils.isEmpty(right_button_text) || show_right_button ? View.VISIBLE : View.INVISIBLE);
    }

    public void setRightText(String text) {
        if (!TextUtils.isEmpty(text)) {
            right_button.setText(text);
            right_button.setTextColor(right_button_textColor);
            right_button.setTextSize(right_button_textSize);
        }
    }

    public void setRightHintText(String text) {
        if (!TextUtils.isEmpty(text)) {
            right_button.setHint(text);
            right_button.setTextColor(right_button_textColor);
            right_button.setTextSize(right_button_textSize);
        }
    }

    public void setLeftText(String text) {
        if (!TextUtils.isEmpty(text)) {
            left_button.setText(text);
            left_button.setTextColor(left_button_textColor);
            left_button.setTextSize(left_button_textSize);
        }
    }

    public void setTitleText(String text) {
        if (!TextUtils.isEmpty(text)) {
            title.setText(text);
        }
    }
    public void setTitleTextHTml(String text) {
        if (!TextUtils.isEmpty(text)) {
            title.setText(BaseStringUtils.textFormatHtml(text));
        }
    }


    public ImageView getRightImage() {
        return rightImage;
    }

    public void setVisibility(int id, boolean show) {
        findViewById(id).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        findViewById(id).setEnabled(show);
    }

    public void setViewGone(int id, boolean show) {
        findViewById(id).setVisibility(show ? View.VISIBLE : View.GONE);
        findViewById(id).setEnabled(show);
    }

    public String getRightText() {
        return right_button.getText().toString().trim();
    }

    public String getTitleText() {
        return title.getText().toString().trim();
    }


}
