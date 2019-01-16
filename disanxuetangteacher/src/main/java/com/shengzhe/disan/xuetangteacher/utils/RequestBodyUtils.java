package com.shengzhe.disan.xuetangteacher.utils;

import com.google.gson.Gson;
import com.main.disanxuelib.util.AESUtils;
import com.main.disanxuelib.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by acer on 2017/12/5.
 */

public class RequestBodyUtils {

    /**
     * 加密数据
     * @param map
     * @return
     */
    public static RequestBody setRequestBody(Map<String,Object> map){
        Gson gson = new Gson();
        String postInfoStr = gson.toJson(map);
        if(ConstantUrl.IS_ENCRYPT){
            LogUtils.e("datas = " +postInfoStr);
            postInfoStr= AESUtils.encrypt(postInfoStr);
            Map<String,Object> mapDecrypt=new HashMap<>();
            mapDecrypt.put("sign",postInfoStr);
            postInfoStr=gson.toJson(mapDecrypt);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),postInfoStr);
        return body;

    }

    /**
     * 加密数据
     * @param map
     * @return
     */
    public static String setEncrypt(Map<String,Object> map){
        Gson gson = new Gson();
        String postInfoStr = gson.toJson(map);
            if(ConstantUrl.IS_ENCRYPT){
                postInfoStr= AESUtils.encrypt(postInfoStr);
        }
         return postInfoStr;

    }
}
