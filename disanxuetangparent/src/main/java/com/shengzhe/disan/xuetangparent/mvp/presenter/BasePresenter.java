package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.view.View;

/**
 * Created by 业务处理基类 on 2017/11/27.
 */

public class BasePresenter {
    public Context mContext;

    protected BasePresenter(Context context){
        mContext = context;
    }

    public OnClickPresenter listener;
    public void setOnClickPresenter(OnClickPresenter listener){
        this.listener = listener;
    }

    public OnManClickPresenter listenerMan;
    public void setOnManClickPresenter(OnManClickPresenter listener){
        this.listenerMan = listener;
    }

    public interface OnClickPresenter{
        void presenterClick(View v, Object obj);
    }
    public interface OnManClickPresenter{
        void presenterManClick(View v, Object obj);
    }

    public ResultDate resultDate;
    public void setResultDatePresenter(ResultDate resultDate){
        this.resultDate = resultDate;
    }
    public interface ResultDate{
        void resultDate(int tag,Object obj);
    }

}
