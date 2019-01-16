package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;

import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/27.
 */

public class BaseModelImpl {
    private HttpService httpService;
    private MVPRequestListener listener;
    private Context context;
    private String from;

    public BaseModelImpl(Context context, MVPRequestListener listener,String from){
        if(httpService==null){
            httpService = Http.getHttpService();
        }
        this.listener = listener;
        this.context = context;
        this.from = from;
    }

    public HttpService getHttpService(){
        return  httpService;
    }

    public MVPRequestListener getListener(){
        return  listener;
    }

    public Context getContext(){
        return  context;
    }

    public Map<String,Object> getParameterMap(){
        return new HashMap<>();
    }

    public String getFrom(){
        return from;
    }

}
