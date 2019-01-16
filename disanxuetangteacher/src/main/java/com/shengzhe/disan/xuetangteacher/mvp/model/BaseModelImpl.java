package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;

import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/27.
 */

public class BaseModelImpl{
    private HttpService httpService;
    private MVPRequestListener listener;
    private Context context;

    public BaseModelImpl(Context context,MVPRequestListener listener){
        if(httpService==null){
            httpService = Http.getHttpService();
        }
        this.listener = listener;
        this.context = context;
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
}
