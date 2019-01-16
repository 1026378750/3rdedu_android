package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;

import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.OrderBean;
import com.shengzhe.disan.xuetangparent.bean.OrderOnfflineInfo;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.model.OrderModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineOrderView;
import com.shengzhe.disan.xuetangparent.mvp.view.OrderItemView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */
public class OrderPresenter extends BasePresenter implements MVPRequestListener {

    private OfflineOrderView offlineOrderView;
    private OrderItemView orderItemView;

    private OrderItemView.IOrderItemView iOrderItemView;
    private OfflineOrderView.IOfflineOrderView iOfflineOrderView;

    private OrderModelImpl modelImpl;

    private String fromTag = "";

    public OrderPresenter(Context context, OrderItemView.IOrderItemView view) {
        super(context);
        this.iOrderItemView = view;
    }

    public OrderPresenter(Context context, OfflineOrderView.IOfflineOrderView view) {
        super(context);
        this.iOfflineOrderView = view;
    }

    public void initMineOrderItemUi(OrderItemView.OnClickView listener) {
        if (orderItemView==null)
            orderItemView = new OrderItemView(mContext);
        if (modelImpl == null)
            modelImpl = new OrderModelImpl(mContext, this,iOrderItemView.getClass().getName());
        orderItemView.setIOrderItemView(iOrderItemView);
        orderItemView.setOnClickView(listener);
    }

    public void initMineOfflineOrderUi() {
        if (offlineOrderView==null)
            offlineOrderView = new OfflineOrderView(mContext);
        if (modelImpl == null)
            modelImpl = new OrderModelImpl(mContext, this,iOfflineOrderView.getClass().getName());
        offlineOrderView.setIOfflineOrderView(iOfflineOrderView);
    }


    public void setMineOrderItemDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        orderItemView.initDatas(listener);
    }

    public void getMineOfflineOrderDatas(int orderId) {
        modelImpl.getDetailOrder(orderId);
    }

    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_MyOrderList:
                orderItemView.setResultDatas(objects==null?new BasePageBean<OrderBean>():(BasePageBean<OrderBean>) objects);
                break;

            case IntegerUtil.WEB_API_RemoveOrder:
                if (iOfflineOrderView!=null && from.equals(iOfflineOrderView.getClass().getName())){
                    offlineOrderView.setResultRemove();
                }else if (iOrderItemView!=null && from.equals(iOrderItemView.getClass().getName())){
                    orderItemView.setResultRemove();
                }
                break;

            case IntegerUtil.WEB_API_DetailOrder:
                offlineOrderView.setResultDatas(objects==null?new OrderOnfflineInfo():(OrderOnfflineInfo)objects);
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg,String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_MyOrderList:
                orderItemView.finishLoad();
                break;

            default:
                ToastUtil.showToast(mesg);
                break;
        }
    }

    public void removeOrder(int id) {
        modelImpl.removeOrder(id);
    }


    public String getFromTag() {
        return fromTag;
    }

    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public OrderOnfflineInfo getOrderOnfflineInfo() {
        return offlineOrderView.getOrderOnfflineInfo();
    }

    public void clearMineOrderDatas() {
        orderItemView.clearDatas();
    }

    public void getMineOrderItemList(String statusStr ,int pageNum) {
        int status = 0;
        if (statusStr.equals("待支付")) {
            status = 2;
        } else if (statusStr.equals("已支付")) {
            status = 3;
        } else {
            status = 1;
        }
        modelImpl.getOrderList(status, pageNum);
    }
}
