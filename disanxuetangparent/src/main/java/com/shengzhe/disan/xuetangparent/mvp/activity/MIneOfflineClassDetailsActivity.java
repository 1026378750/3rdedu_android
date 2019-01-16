package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswise;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/******
 * 线下班课详情
 */
public class MIneOfflineClassDetailsActivity extends BaseActivity {
    //在线直播课
    @BindView(R.id.order_method_mame)
    TextView orderMethodMame;
    @BindView(R.id.iv_teacher_header)
    ImageView mTeacherHead;
    @BindView(R.id.iv_teacher_mesg)
    TextView mTeacherMesg;
    //互动小班课
    @BindView(R.id.order_type_name)
    TextView orderTypeName;
    //下单状态
    @BindView(R.id.order_sub_status)
    TextView orderSubStatus;
    //下单编号
    @BindView(R.id.ccb_order_code)
    CommonCrosswise ccbOrderCode;
    //下单时间
    @BindView(R.id.ccb_orderpay_time)
    CommonCrosswise ccbOrderpayTime;
    //课程图
    @BindView(R.id.iv_class_photo)
    ImageView ivHotPhoto;
    //高中语文-高中语文文言文阅读…
    @BindView(R.id.tv_class_name)
    TextView tvSubjctClassType;
    //增加学生对现代文的于都理解能力，增加学 生对现代文…
    @BindView(R.id.tv_class_details)
    TextView tvSubjctClassDetails;
    //2小时/次，共20次
    @BindView(R.id.tv_class_time)
    TextView tvOrderTime;
    //课程价格
    @BindView(R.id.tv_class_price)
    TextView tvOrderPrice;
    //优惠信息
    @BindView(R.id.ccb_order_free)
    CommonCrosswise ccbOrderFree;

    @BindView(R.id.ccb_order_buytype)
    CommonCrosswise ccbOrderBuytype;
    @BindView(R.id.ccb_order_buytimes)
    CommonCrosswise ccbOrderBuytimes;
    @BindView(R.id.ccb_order_sigleprice)
    CommonCrosswise ccbOrderSigleprice;
    @BindView(R.id.ccb_order_sumprice)
    CommonCrosswise ccbOrderSumprice;
    @BindView(R.id.ccb_order_parentprice)
    CommonCrosswise ccbOrderParentprice;
    @BindView(R.id.tv_class_number)
    TextView tvOfflineMannum;
    @BindView(R.id.btn_offlineorder_confirm)
    Button btnOfflineorderConfirm;
    @BindView(R.id.tv_liveorder_delte)
    TextView tvLiveorderDelte;
    @BindView(R.id.tv_offline_countdown)
    TextView tvOfflineCountdown;

    private OrderOfflineDetailsBean orderOfflineDetailBean;
    private CountDownTimer cutDownTimer;
    private int id;

    @Override
    public void initData() {
        id = getIntent().getIntExtra(StringUtils.ORDER_ID, 0);
        postOrderInfo(id);
    }

    /**
     * 订单详情
     */
    private void postOrderInfo(int id) {
        //
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", id);
        httpService.orderOfflineInfo(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<OrderOfflineDetailsBean>(mContext, true) {
                    @Override
                    protected void onDone(OrderOfflineDetailsBean orderInfo) {
                        orderOfflineDetailBean = orderInfo;
                        setData();
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });

    }

    private long Parentprice = 0l;

    /**
     * 设置返回的值
     */
    private void setData() {
        orderMethodMame.setText(orderOfflineDetailBean.courseTypeName);
        mTeacherMesg.setText(orderOfflineDetailBean.getTeacherNick());
        ccbOrderCode.setTitleText(orderOfflineDetailBean.orderCode);
        ccbOrderpayTime.setTitleText(DateUtil.timeStampDate(orderOfflineDetailBean.buyerTime, "yyyy-MM-dd HH:mm"));

        //课程信息
        tvSubjctClassType.setText(orderOfflineDetailBean.courseName);
        tvSubjctClassDetails.setText(orderOfflineDetailBean.gradeName + " " + orderOfflineDetailBean.subjectName);
        tvOrderTime.setText(orderOfflineDetailBean.teachingPeriod + "小时/次，共" + orderOfflineDetailBean.classTime + "次");
        tvOfflineMannum.setText(StringUtils.textFormatHtml("<font color='#1D97EA'>" + orderOfflineDetailBean.salesVolume + "</font>/" + orderOfflineDetailBean.maxUser + "人"));
        tvOrderPrice.setText("¥" + ArithUtils.round(orderOfflineDetailBean.totalPrice));
        String mDiscounts = "";
       /* if (orderOfflineDetailBean.courseDiscountId != 0) {
            Parentprice = orderOfflineDetailBean.totalPrice - orderOfflineDetailBean.discountAmount;
            mDiscounts = String.format("%.1f", (float) orderOfflineDetailBean.discountPercent / 10) + "折";
        } else {
           // Parentprice = (orderOfflineDetailBean.buyNum*orderOfflineDetailBean.onePrice);
            Parentprice=orderOfflineDetailBean.totalPrice;
        }*/
        if(orderOfflineDetailBean.isJoin==1){
            Parentprice=orderOfflineDetailBean.onePrice*orderOfflineDetailBean.classSum;
        }else {
            if (orderOfflineDetailBean.courseDiscountId != 0) {
                Parentprice = orderOfflineDetailBean.totalPrice - orderOfflineDetailBean.discountAmount;
                mDiscounts = String.format("%.1f", (float) orderOfflineDetailBean.discountPercent / 10) + "折";
            } else {
                // Parentprice = (orderOfflineDetailBean.buyNum*orderOfflineDetailBean.onePrice);
                Parentprice=orderOfflineDetailBean.onePrice*orderOfflineDetailBean.classSum;
            }

        }
        ccbOrderFree.setTitleText(TextUtils.isEmpty(mDiscounts)?"无":mDiscounts);
        String sumPrice = "" + "<font color='#FFAE12'>" + "¥" + ArithUtils.round(Parentprice) + "</font>" + "";
      //  String sumPrice = "" + "<font color='#FFAE12'>" + "¥" + ArithUtils.round(orderOfflineDetailBean.totalPrice) + "</font>" + "";
        ccbOrderParentprice.setTitleText(StringUtils.textFormatHtml(sumPrice) + "");
        ImageUtil.loadCircleImageView(mContext, orderOfflineDetailBean.teacherPhotoUrl, mTeacherHead, R.mipmap.ic_personal_avatar);
        ImageUtil.loadImageViewLoding(mContext, orderOfflineDetailBean.photoUrl, ivHotPhoto, R.mipmap.default_error, R.mipmap.default_error);
        if(orderOfflineDetailBean.isJoin == 1){
            ccbOrderBuytimes.setTitleText(orderOfflineDetailBean.classSum == 0 ? "--" : (orderOfflineDetailBean.classSum + "次"));
            ccbOrderSumprice.setTitleText(orderOfflineDetailBean.onePrice == 0 ? "--" : "¥" + ArithUtils.round(orderOfflineDetailBean.classSum*orderOfflineDetailBean.onePrice));
        }else {
            ccbOrderBuytimes.setTitleText(orderOfflineDetailBean.classTime == 0 ? "--" : (orderOfflineDetailBean.classTime + "次"));
            ccbOrderSumprice.setTitleText(orderOfflineDetailBean.totalPrice == 0 ? "--" : "¥" + ArithUtils.round(orderOfflineDetailBean.totalPrice));

        }
       // ccbOrderBuytimes.setTitleText(orderOfflineDetailBean.classTime == 0 ? "--" : (orderOfflineDetailBean.classTime + "次"));
        ccbOrderBuytype.setTitleText(orderOfflineDetailBean.isJoin == 1 ? "插班" : "全程");
        ccbOrderSigleprice.setTitleText(orderOfflineDetailBean.onePrice == 0 ? "--" : "¥" + ArithUtils.round(orderOfflineDetailBean.onePrice));


        //倒计时30分钟
        if (orderOfflineDetailBean.payTime != 0) {
            setCutDownTimer(orderOfflineDetailBean.payTime);
        }
        switchStatus();
    }

    //线下班课状态改变
    private void switchStatus() {
        String content = "";
        switch (orderOfflineDetailBean.orderStatus) {
            case IntegerUtil.ORDER_STATUS_WAITPAY:
                //待支付
                if (orderOfflineDetailBean.getUpperFrame() == 0) {
                    //tvLiveorderDelte.setVisibility(View.VISIBLE);
                   // tvLiveorderDelte.setText("课程已经下架");
                    content = "已关闭";
                } else {
                    btnOfflineorderConfirm.setVisibility(View.VISIBLE);
                    if (orderOfflineDetailBean.getPayTime() > 0) {
                        btnOfflineorderConfirm.setText("立即支付");
                        content = "待支付";
                    } else {
                        // orderOnfflineInfo.setOrderStatus(9);
                        btnOfflineorderConfirm.setText("删除");
                        //tvLiveorderConfirm.setText("立即支付");
                        content = "已关闭";
                    }
                }
                setCutDownTimer(orderOfflineDetailBean.getPayTime());
                break;

            case IntegerUtil.ORDER_STATUS_PAYED:
                //已支付,已付款
                if (orderOfflineDetailBean.getUpperFrame() == 0) {
                 //   tvLiveorderDelte.setVisibility(View.VISIBLE);
                  //  tvLiveorderDelte.setText("课程已经下架");
                    content = "已关闭";
                }
                content = "已支付";
                break;

            case IntegerUtil.ORDER_STATUS_FINISH:
                //已支付, 完成
                if (orderOfflineDetailBean.getUpperFrame() == 0) {
                   // tvLiveorderDelte.setVisibility(View.VISIBLE);
                   // tvLiveorderDelte.setText("课程已经下架");
                }
                content = "已支付";
                break;
            case IntegerUtil.ORDER_STATUS_ClOSE:
                btnOfflineorderConfirm.setVisibility(View.VISIBLE);
                btnOfflineorderConfirm.setText("删除");
                content = "已关闭";
                break;
        }
        orderSubStatus.setText(content);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_mine_offline_class_details;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.item_offlineclazz, R.id.btn_offlineorder_confirm})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.item_offlineclazz:
                intent.setClass(mContext, CourseDetailActivity.class);
                intent.putExtra(StringUtils.COURSE_ID,orderOfflineDetailBean.courseId);
                intent.putExtra(StringUtils.select_payStatus,orderOfflineDetailBean.getUpperFrame()==0?true:false);
                startActivity(intent);
                break;

            case R.id.btn_offlineorder_confirm:
                //线下班课
                if (orderOfflineDetailBean.getOrderStatus() == 1) {
                    ConfirmPay payUtil = new ConfirmPay(orderOfflineDetailBean.getOrderId(), orderOfflineDetailBean.getTotalPrice() , orderOfflineDetailBean.courseId,Parentprice);
                    intent.setClass(mContext, OrderPayActivity.class);
                    intent.putExtra(StringUtils.COURSE_TYPE, 4);
                    intent.putExtra(StringUtils.ACTIVITY_DATA, payUtil);
                    startActivity(intent);
                } else if (orderOfflineDetailBean.getOrderStatus() == 9) {
                    ConfirmDialog dialog = ConfirmDialog.newInstance("提示", "您确定删除订单吗？删除后该订单信息将不可恢复！", "取消", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    //删除
                                    postDel();
                                    break;
                            }
                        }
                    });
                } else if (orderOfflineDetailBean.getOrderStatus() == 2 || orderOfflineDetailBean.getOrderStatus() == 8) {
                    intent.setClass(mContext, OfflineOneonOneDetailsActivity.class);
                    intent.putExtra(StringUtils.COURSE_ID,orderOfflineDetailBean.courseId);
                    startActivity(intent);
                }
                break;

        }

    }

    //删除订单
    private void postDel() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", id);
        httpService.removeOfflineOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        ToastUtil.showToast(strTeacher);
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11014);
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                        if (ex.getErrCode() == 222222) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11014);
                            EventBus.getDefault().post(bundle);
                            onBackPressed();
                        }
                    }
                });
    }

    private void setCutDownTimer(long payTime) {

        tvOfflineCountdown.setVisibility(View.VISIBLE);
        cutDownTimer = new CountDownTimer(payTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long cutDownTime = millisUntilFinished / 1000;
                long minute = cutDownTime / 60;
                long second = cutDownTime % 60;
                String contentTime = "剩余支付时间：<font color='#D92B2B'>" + (minute > 0 && minute < 10 ? "0" + minute : minute)
                        + "分钟" + (second > 0 && second < 10 ? "0" + second : second) + "秒</font>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvOfflineCountdown.setText(Html.fromHtml(contentTime, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvOfflineCountdown.setText(Html.fromHtml(contentTime));
                }
            }

            @Override
            public void onFinish() {
                tvOfflineCountdown.setVisibility(View.GONE);
                btnOfflineorderConfirm.setText("删除");
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11015);
                EventBus.getDefault().post(bundle);
            }
        };
        cutDownTimer.start();
    }


}
