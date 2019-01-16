package com.shengzhe.disan.xuetangparent.http.responese;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.main.disanxuelib.util.AESUtils;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by hy on 2017/10/19.
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.type = type;
    }

    /**
     * @param value
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody value) {
        int  errorCode=0;
        String  errorMesage="";
        try {
            String result = value.string();
            JSONObject jsonObject = new JSONObject(result);
            if (ConstantUrl.IS_DENCRYPT) {
                String datas = jsonObject.optString("sign");
                if(jsonObject.optInt("error_code") != ConstantUrl.error_code1){
                    jsonObject = new JSONObject(AESUtils.decrypt(datas));
                }
            }
            if ((jsonObject.optInt("error_code") ==ConstantUrl.error_code3)||(jsonObject.optInt("error_code") == ConstantUrl.error_code2)) {
                String data = jsonObject.optString("data");
                //常规数据请求接口数据
                if (!TextUtils.isEmpty(data) && !data.equals("null")) {
                    if(type != String.class){
                        LogUtils.e("baseHttpResult = " + data);
                        return new Gson().fromJson(data,type);
                    }else{
                        return (T)data;
                    }
                } else {
                    //data里面没有数据
                    errorCode=jsonObject.optInt("error_code");
                    errorMesage=jsonObject.optString("error_message");
                    throw new ResultException(jsonObject.optInt("error_code"), jsonObject.optString("error_message"));
                }
            } else {
                //ErrResponse 将msg解析为异常消息文本
                errorCode=jsonObject.optInt("error_code");
                errorMesage=jsonObject.optString("error_message");
                throw new ResultException(jsonObject.optInt("error_code"), jsonObject.optString("error_message"));
            }
        } catch (IOException e) {
            throw new ResultException(0, UiUtils.getString(R.string.httpSubmitContextNull));
        } catch (JSONException e) {
            throw new ResultException(0, UiUtils.getString(R.string.JsonSyntaxException));
        } catch (RuntimeException e) {
            ErrResponse errResponse = new ErrResponse();
            errResponse.setMsg(UiUtils.getString(R.string.systemExpire));
            throw new ResultException(errorCode, errorMesage);
        }
    }
}
