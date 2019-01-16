package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.mine.OrderItemFragment;
import com.shengzhe.disan.xuetangteacher.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherOrderBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MIneLiveOrderDetailsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MineOfflineOrderActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.OfflineClassOrderDetailsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangteacher.mvp.model.OrderModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.view.ILiveOrderDetailView;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOfflineClassOrderDetailView;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOfflineOrderDetailView;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOrderListView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */
public class OrderPresenter extends BasePresenter implements MVPRequestListener, RefreshCommonView.RefreshLoadMoreListener {
    private IOrderListView view;
    private IOfflineOrderDetailView offlineView;
    private ILiveOrderDetailView liveView;
    private IOfflineClassOrderDetailView classView;
    private OrderModelImpl modelImpl;

    private SimpleAdapter adapter;
    private BasePageBean<TeacherOrderBean> myTeacherOrder;
    private List<TeacherOrderBean> teacherOrderBeanList = new ArrayList<>();
    private int orderCourseType = 1, payStatus = 1;

    public OrderPresenter(Context context, IOrderListView view) {
        super(context);
        this.view = view;
        if (modelImpl == null)
            modelImpl = new OrderModelImpl(context, this);
    }

    public OrderPresenter(Context context, IOfflineOrderDetailView view) {
        super(context);
        this.offlineView = view;
        if (modelImpl == null)
            modelImpl = new OrderModelImpl(context, this);
    }

    public OrderPresenter(Context context, ILiveOrderDetailView view) {
        super(context);
        this.liveView = view;
        if (modelImpl == null)
            modelImpl = new OrderModelImpl(context, this);
    }

    public OrderPresenter(Context context, IOfflineClassOrderDetailView view) {
        super(context);
        this.classView = view;
        if (modelImpl == null)
            modelImpl = new OrderModelImpl(context, this);
    }

    public void initOrderDate(String type, String orderStatus) {
        teacherOrderBeanList.clear();
        if (type.equals("线下1对1")) {
            orderCourseType = 1;
        } else if (type.equals("在线直播课")) {
            orderCourseType = 2;
        } else if (type.equals("线下班课")) {
            orderCourseType = 4;
        }

        switch (orderStatus) {
            case "待支付":
                payStatus = 2;
                break;

            case "已支付":
                payStatus = 3;
                break;

            case "全部":
                payStatus = 1;
                break;
        }
    }

    public void setDatas() {
        adapter = new SimpleAdapter<TeacherOrderBean>(mContext, teacherOrderBeanList, R.layout.fragment_order_item) {
            @Override
            protected void onBindViewHolder(final TrdViewHolder holder, final TeacherOrderBean orderBean) {
                holder.setText(R.id.tv_subjct_class_type, orderBean.courseTypeName)
                        .setText(R.id.tv_hot_name, orderBean.courseName)
                        .setText(R.id.tv_subject_status, "    " + (orderBean.courseType != 1 ? orderBean.directName : orderBean.teachingMethodName))
                        .setText(R.id.tv_hot_clazz, "家长昵称：" + (TextUtils.isEmpty(orderBean.userName) ? "--" : orderBean.userName))
                        .setText(R.id.tv_hot_num, orderBean.classSum > 0 ? StringUtils.textFormatHtml("共" + orderBean.classSum + "次,    总价: " + "<font color='#D92B2B'>" + "¥" + ArithUtils.round(orderBean.receivablePrice) + "</font>") : "")
                        .setText(R.id.tv_open_time, DateUtil.timeStampDate(orderBean.buyerTime, "yyyy-MM-dd HH:mm"))
                        .setVisible(R.id.tv_hot_num, orderBean.classSum > 0)
                        .setVisible(R.id.tv_subject_status, orderBean.courseType != 4)
                        .setVisible(R.id.iv_video_photo, orderBean.courseType != 1);

                switch (orderBean.status) {
                    case IntegerUtil.ORDER_STATUS_WAITPAY:
                        holder.setText(R.id.tv_pay_type, "待支付");
                        break;

                    case IntegerUtil.ORDER_STATUS_PAYED:
                        holder.setText(R.id.tv_pay_type, "已支付");
                        break;

                    case IntegerUtil.ORDER_STATUS_FINISH:
                        holder.setText(R.id.tv_pay_type, "已完成");
                        break;

                    case IntegerUtil.ORDER_STATUS_ClOSE:
                        holder.setText(R.id.tv_pay_type, "删除");
                        holder.setText(R.id.tv_pay_close, "已关闭");
                        break;
                }

                if (orderBean.courseType == 1) {//线下一对一
                    if (orderBean.teachingMethodName.equals("学生上门")) {
                        holder.setText(R.id.tv_hot_adress, "家长手机：" + orderBean.userMobile);
                    } else if (orderBean.teachingMethodName.equals("老师上门")) {//老师上门,校区上课
                        holder.setText(R.id.tv_hot_adress, "学生地址：" + orderBean.teachingAddress);
                    } else {
                        holder.setText(R.id.tv_hot_adress, "校区地址：" + orderBean.teachingAddress);
                    }
                } else if (orderBean.courseType == 2) {//在线直播课
                    holder.setText(R.id.tv_hot_adress, "家长手机：" + orderBean.userMobile);
                    ImageUtil.loadImageViewLoding(mContext, orderBean.photoUrl, holder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.default_error, R.mipmap.default_error);
                } else if (orderBean.courseType == 4) {//线下班课
                    holder.setText(R.id.tv_hot_adress, "购买类型：" + (orderBean.isJoin == 0 ? "全程" : "插班"));
                    ImageUtil.loadImageViewLoding(mContext, orderBean.photoUrl, holder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.default_error, R.mipmap.default_error);
                }

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.presenterClick(v,orderBean);
                    }
                });

                if (orderBean.status!=IntegerUtil.ORDER_STATUS_ClOSE)
                    return;
                holder.setOnClickListener(R.id.tv_pay_type, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除操作
                        listener.presenterClick(v,orderBean.orderId);
                    }
                });
            }

        };
        view.getRefreshCommonView().setEmptyImage(R.mipmap.ic_empty);
        view.getRefreshCommonView().setEmptyText("太低调了~ 一个订单都没有...");
        view.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        view.getRefreshCommonView().setIsAutoLoad(false);
        view.getRefreshCommonView().setRefreshLoadMoreListener(this);
    }



    public void claerAllDate() {
        teacherOrderBeanList.clear();
    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        pageNum = 1;
        teacherOrderBeanList.clear();
        modelImpl.postMyTeacherOrder(orderCourseType, payStatus, pageNum);
    }

    @Override
    public void startLoadMore() {
        modelImpl.postMyTeacherOrder(orderCourseType, payStatus, ++pageNum);
    }

    @Override
    public void onSuccess(int tager, Object objects) {
        switch (tager) {
            case IntegerUtil.WEB_API_MyTeacherOrder:
                view.getRefreshCommonView().finishRefresh();
                view.getRefreshCommonView().finishLoadMore();
                myTeacherOrder = (BasePageBean<TeacherOrderBean>) objects;
                teacherOrderBeanList.addAll(myTeacherOrder.getList());
                if (teacherOrderBeanList.isEmpty()) {
                    teacherOrderBeanList.clear();
                    view.getRefreshCommonView().setIsEmpty(true);
                } else {
                    view.getRefreshCommonView().setIsEmpty(false);
                    view.getRefreshCommonView().setIsLoadMore(myTeacherOrder.isHasNextPage());
                }
                adapter.notifyDataSetChanged();
                break;

            case IntegerUtil.WEB_API_OrderDetail:
                if (fromTag.equals(MineOfflineOrderActivity.class.getName())){
                    setOfflineOrderDetail((OrderOfflineDetailsBean)objects);
                }else if(fromTag.equals(MIneLiveOrderDetailsActivity.class.getName())){
                    setOnliveDetail((OrderOfflineDetailsBean)objects);
                } else if (fromTag.equals(OfflineClassOrderDetailsActivity.class.getName())){
                    setOfflineClassDetail((OrderOfflineDetailsBean)objects);
                }
                break;

            case IntegerUtil.WEB_API_RemoveOrder:
                if(fromTag.equals(OrderItemFragment.class.getName())){
                    ToastUtil.showToast((String) objects);
                    view.getRefreshCommonView().notifyData();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11014);
                EventBus.getDefault().post(bundle);
                AppManager.getAppManager().currentActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg) {
        switch (tager) {
            case IntegerUtil.WEB_API_MyTeacherOrder:
                view.getRefreshCommonView().finishRefresh();
                view.getRefreshCommonView().finishLoadMore();
                break;

            default:
                ToastUtil.showToast(mesg);
                break;
        }
    }

    public void loadListDastes() {
        view.getRefreshCommonView().notifyData();
    }

    private String fromTag ="";
    /*****
     * 获取订单详情
     * @param fromTag
     * @param orderId
     */
    public void getOrderDetail(String fromTag, int orderId) {
        this.fromTag = fromTag;
        modelImpl.getOrderDetail(orderId);
    }

    /*****
     * 删除订单
     * @param fromTag
     * @param orderId
     */
    public void removeOrder(String fromTag, int orderId) {
        this.fromTag = fromTag;
        modelImpl.postRemoveOrder(orderId);
    }

    /**
     * 详情返回的值（一对一）
     */
    private void setOfflineOrderDetail(final OrderOfflineDetailsBean detailBean) {
        offlineView.getOrderMethodName().setText(detailBean.courseTypeName);
        offlineView.getOrderTypeName().setText(detailBean.teachingMethodName);

        switch (detailBean.status) {
            case IntegerUtil.ORDER_STATUS_WAITPAY:
                offlineView.getOrderSubStatus().setText("待支付");
                break;

            case IntegerUtil.ORDER_STATUS_PAYED:
                offlineView.getOrderSubStatus().setText("已支付");
                break;

            case IntegerUtil.ORDER_STATUS_FINISH:
                offlineView.getOrderSubStatus().setText("已完成");
                break;

            case IntegerUtil.ORDER_STATUS_ClOSE:
                offlineView.getMineOfflineorderDelte().setVisibility(View.VISIBLE);
                offlineView.getOrderSubStatus().setText("已关闭");
                break;
        }
        offlineView.getOrderCode().setTitleText(detailBean.orderCode);
        offlineView.getOrderpayTime().setTitleText(DateUtil.timeStampDate(detailBean.buyerTime, "yyyy-MM-dd HH:mm"));
        offlineView.getOrderTime().setText(detailBean.duration + "小时/次");
        offlineView.getOrderType().setText(detailBean.courseName);
        offlineView.getOrderTypeDetails().setText(detailBean.remark);
        offlineView.getOrderTeachingMethod().setTitleText(detailBean.teachingMethodName);
        offlineView.getOrderOnePrice().setTitleTextHTml("<font color='#D92B2B'>" + "¥" + ArithUtils.round(detailBean.onePrice) + "</font>" + "元/小时");
        offlineView.getOrderParentName().setTitleText(TextUtils.isEmpty(detailBean.userName) == true ? "--" : detailBean.userName);
        offlineView.getOrderParentMobile().setTitleText(detailBean.userMobile);
        offlineView.getOrderStudentName().setTitleText(TextUtils.isEmpty(detailBean.studentName) == true ? "--" : detailBean.studentName);
        offlineView.getOrderStudentSex().setTitleText(StringUtils.getSex(detailBean.studentSex));

        offlineView.getOrderStudentAdress().setLeftText(detailBean.teachingMethodName.equals("校区上课") == true ? "校区地址" : "学生地址");
        offlineView.getOrderStudentAdress().setTitleText(detailBean.teachingAddress);
        if (!TextUtils.isEmpty(detailBean.discountInfo)) {
            offlineView.getOrderFree().setTitleText(detailBean.discountInfo);
        } else {
            offlineView.getOrderFree().setViewGone(R.id.ccb_order_free, false);
        }

        offlineView.getOrderOneTimes().setTitleText(+detailBean.duration + "小时*" + (detailBean.giveSum + detailBean.buySum) + "次");

        String sumPrice = "总价:    " + "<font color='#D92B2B'>" + "¥" + ArithUtils.round(detailBean.receivablePrice) + "</font>" + "";
        offlineView.getOfflineSumprice().setText(StringUtils.textFormatHtml(sumPrice));
        if (detailBean.orderItem!=null&&!detailBean.orderItem.isEmpty())
            offlineView.getOrderClassTime().setText(DateUtil.timeStamp(detailBean.orderItem.get(0).startTime, "yyyy-MM-dd") + "  " + DateUtil.weekTimeStamp(detailBean.orderItem.get(0).startTime, "yyyy-MM-dd HH:mm") + "    " + DateUtil.timeStamp(detailBean.orderItem.get(0).startTime, "HH:mm"));

        offlineView.getOrderClassAll().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.presenterClick(v,detailBean.orderItem);
            }
        });

        offlineView.getViewDetail().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.presenterClick(v,detailBean);
            }
        });
    }

    /**
     * 设置返回的值(在线直播课)
     */
    private void setOnliveDetail(final OrderOfflineDetailsBean detailBean) {
        liveView.getOrderMethodName().setText(detailBean.courseTypeName);
        liveView.getOrderTypeName().setText(detailBean.directTypeName);
        //liveView.getOrderSubStatus().setText(detailBean.directTypeName);
        liveView.getMineLiveorderDelte().setVisibility(detailBean.status == 9 ? View.VISIBLE : View.GONE);
        liveView.getOrderCode().setTitleText(detailBean.orderCode);
        liveView.getOrderpayTime().setTitleText(DateUtil.timeStampDate(detailBean.buyerTime, "yyyy-MM-dd HH:mm"));
        switch (detailBean.status) {
            case IntegerUtil.ORDER_STATUS_WAITPAY:
                liveView.getOrderSubStatus().setText("待支付");
                break;

            case IntegerUtil.ORDER_STATUS_PAYED:
                liveView.getOrderSubStatus().setText("已支付");
                break;

            case IntegerUtil.ORDER_STATUS_FINISH:
                liveView.getOrderSubStatus().setText("已完成");
                break;

            case IntegerUtil.ORDER_STATUS_ClOSE:
               // liveView.getMineLiveorderDelte().setVisibility(View.VISIBLE);
                liveView.getOrderSubStatus().setText("已关闭");
                break;
        }

        //课程信息
        liveView.getSubjctClassType().setText(detailBean.courseName);
        liveView.getSubjctClassDetails().setText(detailBean.remark);
        liveView.getOrderType().setText(detailBean.directTypeName);
        liveView.getOrderTime().setText(detailBean.duration + "小时/次，共" + (detailBean.giveSum + detailBean.buySum) + "次");
        liveView.getOrderPrice().setText("¥" + ArithUtils.round(detailBean.price*(detailBean.giveSum + detailBean.buySum)));

        liveView.getOrderParentName().setTitleText(TextUtils.isEmpty(detailBean.userName) == true ? "--" : detailBean.userName);
        liveView.getOrderParentMobile().setTitleText(detailBean.userMobile);
        liveView.getOrderStudentName().setTitleText(TextUtils.isEmpty(detailBean.studentName) == true ? "--" : detailBean.studentName);
        liveView.getOrderStudentSex().setTitleText(StringUtils.getSex(detailBean.studentSex));
        if (!TextUtils.isEmpty(detailBean.discountInfo)) {
            liveView.getOrderFree().setTitleText(detailBean.discountInfo);
        } else {
            liveView.getOrderFree().setViewGone(R.id.ccb_order_free, false);
        }
        liveView.getLiveorderSumprice().setText(StringUtils.textFormatHtml("总价:    " + "<font color='#D92B2B'>" + "¥" + ArithUtils.round(detailBean.receivablePrice) + "</font>"));
        ImageUtil.loadImageViewLoding(mContext, detailBean.photoUrl, liveView.getHotPhoto(), R.mipmap.default_error, R.mipmap.default_error);

        liveView.getViewDetail().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.presenterClick(v,detailBean);
            }
        });

    }

    /**
     * 设置返回的值(线下班课)
     */
    private void setOfflineClassDetail(final OrderOfflineDetailsBean detailBean) {
        classView.getOrderMethodName().setText(detailBean.courseTypeName);
        switch (detailBean.status) {
            case IntegerUtil.ORDER_STATUS_WAITPAY:
                classView.getOrderSubStatus().setText("待支付");
                break;

            case IntegerUtil.ORDER_STATUS_PAYED:
                classView.getOrderSubStatus().setText("已支付");
                break;

            case IntegerUtil.ORDER_STATUS_FINISH:
                classView.getOrderSubStatus().setText("已完成");
                break;

            case IntegerUtil.ORDER_STATUS_ClOSE:
                classView.getOrderSubStatus().setText( "已关闭");
                classView.getMineLiveorderDelte().setVisibility(View.VISIBLE);
                break;
        }
        classView.getMineLiveorderDelte().setVisibility(detailBean.status == 9 ? View.VISIBLE : View.GONE);
        classView.getOrderCode().setTitleText(detailBean.orderCode);
        classView.getOrderpayTime().setTitleText(DateUtil.timeStampDate(detailBean.buyerTime, "yyyy-MM-dd HH:mm"));

        //课程信息
        classView.getSubjctClassType().setText(detailBean.courseName);
        classView.getSubjctClassDetails().setText(detailBean.gradeName + " " + detailBean.subjectName);
        classView.getOrderTime().setText(detailBean.duration + "小时/次，共" + detailBean.classTime + "次");
        classView.getOrderPrice().setText("¥" + ArithUtils.round(detailBean.onePrice*detailBean.classTime));
        classView.getOfflineMannum().setText(StringUtils.textFormatHtml("<font color='#1D97EA'>" + detailBean.salesVolume + "</font>" + "/" + detailBean.maxUser+"人"));

        classView.getOrderParentName().setTitleText(TextUtils.isEmpty(detailBean.userName) == true ? "--" : detailBean.userName);
        classView.getOrderParentMobile().setTitleText(detailBean.userMobile);
        classView.getOrderStudentName().setTitleText(TextUtils.isEmpty(detailBean.studentName) == true ? "--" : detailBean.studentName);
        classView.getOrderStudentSex().setTitleText(StringUtils.getSex(detailBean.studentSex));
        classView.getOrderFree().setTitleText(TextUtils.isEmpty(detailBean.discountInfo)?"无":detailBean.discountInfo);
        classView.getOrderParentprice().setTitleText(StringUtils.textFormatHtml("<font color='#D92B2B'>" + "¥" + ArithUtils.round(detailBean.receivablePrice) + "</font>")+"");
        ImageUtil.loadImageViewLoding(mContext, detailBean.photoUrl, classView.getHotPhoto(), R.mipmap.default_error, R.mipmap.default_error);
        if(detailBean.isJoin == 1){
            classView.getOrderBuytimes().setTitleText(detailBean.classSum == 0 ? "--" : (detailBean.classSum + "次"));
            classView.getOrderSumprice().setTitleText(detailBean.onePrice == 0 ? "--" : ("￥"+ArithUtils.round(detailBean.classSum *detailBean.onePrice)));
        }else {
            classView.getOrderBuytimes().setTitleText(detailBean.classTime == 0 ? "--" : (detailBean.classTime + "次"));
            classView.getOrderSumprice().setTitleText(detailBean.totalPrice == 0 ? "--" : ("￥"+ArithUtils.round(detailBean.totalPrice)));

        }

        classView.getOrderBuytype().setTitleText(detailBean.isJoin == 1 ? "插班" : "全程");
        classView.getOrderSigleprice().setTitleText(detailBean.onePrice == 0 ? "--" : ("￥"+ArithUtils.round(detailBean.onePrice) + "/次"));
      //  classView.getOrderSumprice().setTitleText(detailBean.totalPrice == 0 ? "--" : ("￥"+ArithUtils.round(detailBean.totalPrice)));

        classView.getViewDetail().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.presenterClick(v,detailBean.courseId);
            }
        });

        if (detailBean.payTime != 0) {
            setCutDownTimer(detailBean.payTime);
        }
    }

    private CountDownTimer cutDownTimer;
    private void setCutDownTimer(long payTime) {
        classView.getOfflineCountdown().setVisibility(View.VISIBLE);
        cutDownTimer = new CountDownTimer(payTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long cutDownTime = millisUntilFinished / 1000;
                long minute = cutDownTime / 60;
                long second = cutDownTime % 60;
                String contentTime = "剩余支付时间：<font color='#1D97EA'>" + (minute > 0 && minute < 10 ? "0" + minute : minute)
                        + "分钟" + (second > 0 && second < 10 ? "0" + second : second) + "秒</font>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    classView.getOfflineCountdown().setText(Html.fromHtml(contentTime, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    classView.getOfflineCountdown().setText(Html.fromHtml(contentTime));
                }
            }

            @Override
            public void onFinish() {
                classView.getOfflineCountdown().setVisibility(View.GONE);
                classView.getMineLiveorderDelte().setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11015);
                EventBus.getDefault().post(bundle);
            }
        };
        cutDownTimer.start();
    }

}
