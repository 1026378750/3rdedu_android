package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/11/27.
 */

public class BaseView {
    public Context mContext;

    protected BaseView(Context context){
        mContext = context;
    }

    public OnClickView listener;
    public void setOnClickView(OnClickView listener){
        this.listener = listener;
    }

    public interface OnClickView{
        void viewClick(View v, Object obj);
    }

}
