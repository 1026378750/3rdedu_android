package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.PayVideo;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/27.
 */

public class PayModelImpl extends BaseModelImpl {

    public PayModelImpl(Context context, MVPRequestListener listener,String from) {
        super(context, listener,from);
    }

    /*****
     * 支付
     * @param map
     */
    private void svaePayOrder(Map<String, Object> map) {
        getHttpService().aliConfirmPay(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String jsonObjects) {
                        try {
                            String resultSign = new JSONObject(jsonObjects).optString("resultSign");
                            getListener().onSuccess(IntegerUtil.WEB_API_PayOrder, resultSign,getFrom());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_PayOrder, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_PayOrder, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 0元支付
     * @param map
     */
    private void saveZeroPay(Map<String, Object> map) {
        HttpService httpService = Http.getHttpService();
        httpService.zeroOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ZeroPay, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ZeroPay, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ZeroPay, ex.getMessage(),getFrom());
                        }
                    }
                });
    }


    /*****
     * 视频课订单详情
     * @param courseId
     */
    private void getPayVideo(int courseId){
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        getHttpService().payVideoInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<PayVideo>(getContext(),true) {
                    @Override
                    protected void onDone(PayVideo str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_PayVideo, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_PayVideo, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_PayVideo, ex.getMessage(),getFrom());
                        }
                    }
                });

    }



}
