package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.OrderDiscount;
import com.shengzhe.disan.xuetangparent.bean.OrderOnfflineInfo;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class OfflineOrderView extends BaseView {
    private List<Long> startTimeArray = new ArrayList<>();
    private OrderOnfflineInfo orderInfo;

    public OfflineOrderView(Context context) {
        super(context);
    }

    private IOfflineOrderView iView;

    public void setIOfflineOrderView(IOfflineOrderView iView) {
        this.iView = iView;
    }

    public void setResultDatas(OrderOnfflineInfo orderOnfflineInfo) {
        this.orderInfo = orderOnfflineInfo;
        startTimeArray = orderInfo.getStartTimeArray();
        iView.getLiveOrderRV().setVisibility(View.GONE);
        iView.getName().setText(orderInfo.getCourseName());
        iView.getClazz().setRightText(orderInfo.getCourseTypeName());
        iView.getMethods().setRightText(orderInfo.getTeachingMethodName());
        iView.getSubject().setRightText(orderInfo.getGradeName() + " " + orderInfo.getSubjectName());
        iView.getLiveorderOrdernum().setRightText(orderInfo.getOrderCode());
        iView.getLiveorderOrdertime().setRightText(DateUtil.timeStamp(orderInfo.getBuyerTime(), "yyyy-MM-dd HH:mm"));
        iView.getTimes().setRightText(orderInfo.getClassSum() + "次");
        iView.getLiveTeacherName().setRightText(orderInfo.getTeacherNick());
        iView.getLiveorderAdress().setText(orderInfo.getAddress());
        iView.getPrice().setRightText("¥" + ArithUtils.round(orderInfo.getSignPrice()) + "/小时");
        iView.getSchoolTime().setRightText(DateUtil.weekTimeStamp(startTimeArray.get(0), "yyyy-MM-dd") + " " + DateUtil.timeStamp(startTimeArray.get(0), "yyyy-MM-dd HH:mm"));
       // iView.getTime().setRightText((orderInfo.getBuyNum() + orderInfo.getGiveNum()) * orderInfo.getTeachingPeriod() + "小时");
        int Num=orderInfo.getBuyNum()+orderInfo.getGiveNum();
        if(Num!=0){
            iView.getTime().setRightText((orderInfo.getBuyNum() + orderInfo.getGiveNum()) * orderInfo.getTeachingPeriod() + "小时");
       }else {
            iView.getTime().setRightText(orderInfo.getClassSum() * orderInfo.getTeachingPeriod() + "小时");
        }
        String mDiscounts = "";
        List<OrderDiscount> orderDiscountList = orderInfo.getOrderDiscountList();
        if (orderDiscountList != null && orderDiscountList.size() > 0) {
            for (OrderDiscount item : orderDiscountList) {
                //0：课程优惠 1：校区优惠 2:调价优惠 3:线下优惠
                if (item.getCampusDiscountPercent() != 100 && item.getCampusDiscountPercent() != 0 && item.getCampusDiscountPercent() != 1) {
                    //有折扣
                    mDiscounts = String.format("%.1f", (float) item.getCampusDiscountPercent() / 10) + "折;";
                }
                switch (item.getType()) {
                    case 0:
                        break;
                    case 1:
                        //有折扣
                        mDiscounts = String.format("%.1f", (float) item.getCampusDiscountPercent() / 10) + "折";
                        break;
                    case 2:
                        break;
                    case 3:
                        mDiscounts += "线下优惠¥" + ArithUtils.round(item.getCampusDiscountPercent());
                        orderInfo.setDiscountAmount(item.getCampusDiscountPercent());
                        break;

                }
            }
        }
        if (orderInfo.getDiscountPercent() != 100 && orderInfo.getDiscountPercent() != 0 && orderInfo.getDiscountPercent() != 1) {
            //有折扣
            mDiscounts = orderInfo.getDiscountPercent() + "折";
        }
        if (orderInfo.getGiveNum() != 0) {
            mDiscounts += "购买" + orderInfo.getBuyNum() + "节课" + ";赠送" + orderInfo.getGiveNum() + "节课";
        }

        LogUtils.e("mDiscounts = " + mDiscounts);
        if (StringUtils.textIsEmpty(mDiscounts)) {
            iView.getMessage().setViewGone(R.id.ccb_liveorder_message, false);
        } else {
            iView.getMessage().setRightText(mDiscounts);
        }

        iView.getCount().setRightText("¥" + ArithUtils.round(orderInfo.getTotalPrice()));

        LogUtils.e("getDiscountAmount = " + ArithUtils.roundLong(orderInfo.getDiscountAmount()));
        if (ArithUtils.roundLong(orderInfo.getDiscountAmount()).equals("0.00")) {
            iView.getFreeprice().setViewGone(R.id.ccb_liveorder_freeprice, false);
        } else {
            iView.getFreeprice().setRightText("-¥" + ArithUtils.round(orderInfo.getDiscountAmount()));
        }


        iView.getSchoolTime().setRightText(DateUtil.weekTimeStamp(startTimeArray.get(0), "yyyy-MM-dd") + " " + DateUtil.timeStamp(startTimeArray.get(0), "yyyy-MM-dd HH:mm"));
        startTimeArray.remove(0);
        iView.getLiveOrderRV().setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器，封装后的适配器只需要实现一个函数
        iView.getLiveOrderRV().setAdapter(new SimpleAdapter<Long>(mContext, startTimeArray, R.layout.course_open_time_item) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Long data) {
                holder.setText(R.id.tv_open_time, DateUtil.weekTimeStamp(data, "yyyy-MM-dd") + " " + DateUtil.timeStamp(data, "yyyy-MM-dd HH:mm"));
            }
        });
        switchStatus();
    }

    //线下一对一状态改变
    private void switchStatus() {
        String content = "";
        if (orderInfo.getUpperFrame() == 0) {
            iView.getMineAlready().setText(StringUtils.textFormatHtml("已关闭：<font color='#FFAE12'>"+"¥"+ArithUtils.round(orderInfo.getTotalPrice()-orderInfo.getDiscountAmount()) + "</font>"));
            setBtnDelete();
            return;
        }
        String btnStr = "";
        switch (orderInfo.getOrderStatus()) {
            case IntegerUtil.ORDER_STATUS_WAITPAY:
                //待支付
                if (orderInfo.getPayTime() > 0) {
                    btnStr = "立即支付";
                    content = "待支付：";
                    setCutDownTimer(orderInfo.getPayTime());
                } else {
                    btnStr = "删除";
                    content = "已关闭：";
                    setBtnDelete();
                }
                break;

            case IntegerUtil.ORDER_STATUS_PAYED:
                //已支付,已付款
            case IntegerUtil.ORDER_STATUS_FINISH:
                //已支付, 完成
                btnStr = "再次购买";
                content = "已支付：";
                break;

            case IntegerUtil.ORDER_STATUS_ClOSE:
                btnStr = "删除";
                content = "已关闭：";
                setBtnDelete();
                break;
        }
        if (StringUtils.textIsEmpty(btnStr)){
            iView.getLiveorderConfirm().setVisibility(View.GONE);
        }else{
            iView.getLiveorderConfirm().setVisibility(View.VISIBLE);
            iView.getLiveorderConfirm().setText(btnStr);
        }
        iView.getMineAlready().setText(StringUtils.textFormatHtml(content+"<font color='#FFAE12'>" + "¥" + ArithUtils.round(orderInfo.getTotalPrice() - orderInfo.getDiscountAmount()) + "</font>"));

    }

    private void setCutDownTimer(long payTime) {
        if (orderInfo.getPayTime() <= 0 || orderInfo.getUpperFrame() < 1) {
            return;
        }
        iView.getOfflineCountdown().setVisibility(View.VISIBLE);
        cutDownTimer = new CountDownTimer(payTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long cutDownTime = millisUntilFinished / 1000;
                long minute = cutDownTime / 60;
                long second = cutDownTime % 60;
                String contentTime = "剩余支付时间：<font color='#D92B2B'>" + (minute > 0 && minute < 10 ? "0" + minute : minute) + "分钟" + (second > 0 && second < 10 ? "0" + second : second) + "秒</font>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    iView.getOfflineCountdown().setText(Html.fromHtml(contentTime, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    iView.getOfflineCountdown().setText(Html.fromHtml(contentTime));
                }
            }

            @Override
            public void onFinish() {
                iView.getOfflineCountdown().setVisibility(View.GONE);
                setBtnDelete();
                orderInfo.setOrderStatus(9);
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11015);
                EventBus.getDefault().post(bundle);
            }
        };
        cutDownTimer.start();
    }

    private void setBtnDelete(){
        iView.getLiveorderConfirm().setVisibility(View.VISIBLE);
        iView.getLiveorderConfirm().setText("删除");
        iView.getLiveorderConfirm().setBackgroundResource(R.drawable.btn_item_selector);
    }

    private CountDownTimer cutDownTimer;

    public OrderOnfflineInfo getOrderOnfflineInfo() {
        return orderInfo;
    }

    public void setResultRemove() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11014);
        EventBus.getDefault().post(bundle);
        AppManager.getAppManager().currentActivity().onBackPressed();
    }

    public interface IOfflineOrderView {
        TextView getName();

        CommonCrosswiseBar getClazz();

        CommonCrosswiseBar getMethods();

        CommonCrosswiseBar getSubject();

        CommonCrosswiseBar getTimes();

        CommonCrosswiseBar getTime();

        CommonCrosswiseBar getSchoolTime();

        CommonCrosswiseBar getPrice();

        CommonCrosswiseBar getCount();

        CommonCrosswiseBar getMessage();

        CommonCrosswiseBar getFreeprice();

        RecyclerView getLiveOrderRV();

        TextView getOpenTime();

        CommonCrosswiseBar getLiveTeacherName();

        Button getLiveorderConfirm();

        TextView getOfflineCountdown();

        CommonCrosswiseBar getLiveorderOrdernum();

        CommonCrosswiseBar getLiveorderOrdertime();

        TextView getLiveorderAdress();

        TextView getMineAlready();

    }
}
