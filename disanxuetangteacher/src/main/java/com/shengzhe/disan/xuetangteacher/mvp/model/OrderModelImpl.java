package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;

import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherOrderBean;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 */

public class OrderModelImpl extends BaseModelImpl {

    public OrderModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /**
     * 得到支付类型和订单状态并且进行网络请求
     */
    public void postMyTeacherOrder(int orderCourseType, int payStatus, int pageNum) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderCourseType", orderCourseType);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("payStatus", payStatus);
        getHttpService().myTeacherOrder(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<TeacherOrderBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<TeacherOrderBean> mCourseStartList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyTeacherOrder, mCourseStartList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_MyTeacherOrder, ex.getMessage());
                    }
                });
    }

    /****
     * 删除订单
     * @param id
     */
    public void postRemoveOrder(int id) {
        Map<String, Object> map = getParameterMap();
        map.put("orderId", id);
        getHttpService().removeOrder(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_RemoveOrder, strTeacher);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == 222222) {
                            getListener().onSuccess(IntegerUtil.WEB_API_RemoveOrder, "删除成功");
                            return;
                        }
                        getListener().onFailed(IntegerUtil.WEB_API_RemoveOrder, ex.getMessage());
                    }
                });
    }

    /*****
     * 订单详情
     * @param orderId
     */
    public void getOrderDetail(int orderId) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        httpService.orderInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<OrderOfflineDetailsBean>(getContext(), true) {
                    @Override
                    protected void onDone(OrderOfflineDetailsBean orderInfo) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OrderDetail, orderInfo);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_OrderDetail, ex.getMessage());
                    }
                });
    }
}
