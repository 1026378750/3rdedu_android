package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.OrderBean;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.CourseOrder;
import com.shengzhe.disan.xuetangparent.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangparent.bean.OrderOnfflineInfo;
import com.shengzhe.disan.xuetangparent.bean.PayDirectInfo;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 */

public class OrderModelImpl extends BaseModelImpl {

    public OrderModelImpl(Context context, MVPRequestListener listener,String from) {
        super(context, listener,from);
    }

    /****
     * 获取订单列表
     * @param status
     * @param pageNum
     */
    public void getOrderList(int status, int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("payStatus", status);
        getHttpService().myOrder(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<OrderBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<OrderBean> strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyOrderList, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyOrderList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyOrderList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 删除订单
     * @param id
     */
    public void removeOrder(int id) {
        Map<String, Object> map = getParameterMap();
        map.put("orderId", id);
        getHttpService().removeOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        ToastUtil.showToast(strTeacher);
                        getListener().onSuccess(IntegerUtil.WEB_API_RemoveOrder, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_RemoveOrder,"删除成功",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_RemoveOrder, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 获取订单详情
     * @param orderId
     */
    public void getDetailOrder(int orderId) {
        Map<String, Object> map = getParameterMap();
        map.put("orderId", orderId);
        getHttpService().orderInfo(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<OrderOnfflineInfo>(getContext(),true) {
                    @Override
                    protected void onDone(OrderOnfflineInfo orderInfo) {
                        getListener().onSuccess(IntegerUtil.WEB_API_DetailOrder, orderInfo,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_DetailOrder,null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_DetailOrder, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 线下班课订单详情
     * @param orderId
     */
    public void getOfflineOrderDetail(int orderId) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        httpService.orderOfflineInfo(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<OrderOfflineDetailsBean>(getContext(), true) {
                    @Override
                    protected void onDone(OrderOfflineDetailsBean orderInfo) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OfflineOrderDetail, orderInfo,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OfflineOrderDetail,null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OfflineOrderDetail, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 线下班课删除
     * @param orderId
     */
    public void removeOfflineOrder(int orderId) {
        Map<String, Object> map = getParameterMap();
        map.put("orderId", orderId);
        getHttpService().removeOfflineOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_RemoveOfflineOrder, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_RemoveOfflineOrder,"",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_RemoveOfflineOrder, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 直播课订单详情
     * @param courseId
     */
    public void getOnliveOrderDetail(int courseId) {
        Map<String, Object> map =getParameterMap();
        map.put("courseId", courseId);
        getHttpService().payDirectInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<PayDirectInfo>(getContext(), true) {
                    @Override
                    protected void onDone(PayDirectInfo strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OnliveOrderDetail, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OnliveOrderDetail, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OnliveOrderDetail, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /****
     * 获取订单地址
     * @param courseId
     * @param teacherMethod1
     * @param teacherMethod2
     * @param teacherMethod3
     */
    public void getOrderAddress(int courseId,int teacherMethod1,int teacherMethod2,int teacherMethod3) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId",courseId);
        //是否学生上门 1是 0否
        map.put("teacherMethod1", teacherMethod1);
        //是否老师上门 1是 0否
        map.put("teacherMethod2", teacherMethod2);
        //是否校区上课 1是 0否
        map.put("teacherMethod3", teacherMethod3);
        getHttpService().orderAddress(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(),true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OrderAddress, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OrderAddress, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OrderAddress, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /******
     * 创建订单
     * @param map
     */
    public void createCourseOrder(Map<String, Object> map) {
        getHttpService().createCourseOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseOrder>(getContext(), true) {
                    @Override
                    protected void onDone(CourseOrder courseOrder) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CreateCourseOrder, courseOrder,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CreateCourseOrder, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CreateCourseOrder, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

}
