package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.annotation.Nullable;
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

    public WebApiListener apiListener;
    public void setWebApiListener(WebApiListener listener){
        this.apiListener = listener;
    }

    public  interface WebApiListener{
        void loadWebApi(int tag,Object obj);
    }

}
