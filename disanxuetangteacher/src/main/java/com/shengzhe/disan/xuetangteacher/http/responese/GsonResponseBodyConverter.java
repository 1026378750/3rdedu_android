package com.shengzhe.disan.xuetangteacher.http.responese;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.main.disanxuelib.util.AESUtils;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;

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
        int errorCode = 0;
        String errorMesage = "";

        try {
            String result = value.string();
            JSONObject jsonObject = new JSONObject(result);
            if (ConstantUrl.IS_DENCRYPT) {
                String datas = jsonObject.optString("sign");
                LogUtils.e("decrypt = " + AESUtils.decrypt(datas));
                if (jsonObject.optInt("error_code") != ConstantUrl.error_code1) {
                    jsonObject = new JSONObject(AESUtils.decrypt(datas));
                }
            }
            if (jsonObject.optInt("error_code") == ConstantUrl.error_code2) {
                String data = jsonObject.optString("data");
                LogUtils.e("datas = " + data);
                //常规数据请求接口数据
                if (!TextUtils.isEmpty(data) && !data.equals("null")) {
                    if (type != String.class) {
                        LogUtils.d("baseHttpResult = " + result);
                        return new Gson().fromJson(data, type);
                    } else {
                        return (T) data;
                    }
                } else {
                    //data里面没有数据
                   // return (T) jsonObject.optString("error_message");
                    //data里面没有数据
                    errorCode=jsonObject.optInt("error_code");
                    errorMesage=jsonObject.optString("error_message");
                    throw new ResultException(jsonObject.optInt("error_code"), jsonObject.optString("error_message"));
                }
            } else {
                //ErrResponse 将msg解析为异常消息文本
                errorCode = jsonObject.optInt("error_code");
                errorMesage = jsonObject.optString("error_message");
                throw new ResultException(jsonObject.optInt("error_code"), jsonObject.optString("error_message"));
            }
        } catch (IOException e) {
            ErrResponse errResponse = new ErrResponse();
            errResponse.setMsg(UiUtils.getString(R.string.httpSubmitContextNull));
            throw new ResultException(IntegerUtil.ABS_SYSTEM_EXPIRE, errResponse.getMsg());
        } catch (JSONException e) {
            ErrResponse errResponse = new ErrResponse();
            errResponse.setMsg(UiUtils.getString(R.string.JsonSyntaxException));
            throw new ResultException(IntegerUtil.ABS_SYSTEM_EXPIRE, errResponse.getMsg());
        } catch (RuntimeException e) {
            ErrResponse errResponse = new ErrResponse();
            //errResponse.setMsg(UiUtils.getString(R.string.systemExpire));
            throw new ResultException(errorCode, errorMesage);
        }
    }
}
