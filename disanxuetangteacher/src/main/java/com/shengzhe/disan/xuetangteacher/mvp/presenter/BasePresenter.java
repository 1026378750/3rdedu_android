package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/11/27.
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
    public OnItemClickPresenter listenerItem;
    public void setOnItemClickPresenter(OnItemClickPresenter listener){
        this.listenerItem = listener;
    }

    public OnjudgeClickPresenter listenerjudge;
    public void setOnpresenterjudgeClick(OnjudgeClickPresenter listener){
        this.listenerjudge = listener;
    }

    public interface OnClickPresenter{
        void presenterClick(View v, Object obj);
    }
    public interface OnjudgeClickPresenter{
        void presenterjudgeClick( Object obj);
    }

    public interface OnManClickPresenter{
        void presenterManClick(View v, Object obj);
    }
    public interface OnItemClickPresenter{
        void presenterItemClick(View v, Object obj);
    }
}
