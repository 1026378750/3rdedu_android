package com.shengzhe.disan.xuetangparent.mvp.model;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface MVPRequestListener<T> {
    void onSuccess(int tager, T objects,String from);
    void onFailed(int tager, String mesg,String from);
}
