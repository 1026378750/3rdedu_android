package com.shengzhe.disan.xuetangteacher.mvp.model;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface MVPRequestListener<T> {
    void onSuccess(int tager,T objects);
    void onFailed(int tager,String mesg);
}
