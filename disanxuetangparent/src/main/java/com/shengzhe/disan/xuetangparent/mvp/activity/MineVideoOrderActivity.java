package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.OrderOnfflineInfo;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
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
/**
 * 视频课订单详情
 */
public class MineVideoOrderActivity extends BaseActivity {
    @BindView(R.id.tv_videoorder_name)
    TextView mName;
    @BindView(R.id.tv_videoorder_ordernum)
    CommonCrosswiseBar mOrderNum;
    @BindView(R.id.tv_videoorder_time)
    CommonCrosswiseBar mTime;
    @BindView(R.id.ccb_videoorder_clazz)
    CommonCrosswiseBar mClazz;
    @BindView(R.id.ccb_videoorder_methods)
    CommonCrosswiseBar mMethods;
    @BindView(R.id.ccb_videoorder_count)
    CommonCrosswiseBar mCount;
    @BindView(R.id.ccb_videoorder_message)
    CommonCrosswiseBar mMessage;
    @BindView(R.id.ccb_videoorder_disprice)
    CommonCrosswiseBar mDisPrice;
    @BindView(R.id.ccb_videoorder_status)
    TextView mStatus;
    @BindView(R.id.btn_void_confirm)
    Button tvLiveorderConfirm;
    @BindView(R.id.tv_liveorder_delte)
    TextView tvLiveorderDelte;
    @BindView(R.id.tv_offline_countdown)
    TextView tvOfflineCountdown;
    private CountDownTimer cutDownTimer;

    private int id;
    private int CourseType;
    private OrderOnfflineInfo orderOnfflineInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        id = getIntent().getIntExtra(StringUtils.ORDER_ID, 0);
        CourseType = getIntent().getIntExtra(StringUtils.COURSE_TYPE, 0);
        postOrderInfo(id);
    }

    private void postOrderInfo(int id) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", id);
        httpService.orderInfo(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<OrderOnfflineInfo>(mContext,true) {
                    @Override
                    protected void onDone(OrderOnfflineInfo strTeacher) {
                        orderOnfflineInfo = strTeacher;
                        setVideoOrder();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }


    @Override
    public int setLayout() {
        return R.layout.activity_mine_video_order;
    }

    private long mAccount = 0;
    private long needPay = 0;
    private void setVideoOrder() {
        mOrderNum.setRightText(orderOnfflineInfo.getOrderCode());
        mTime.setRightText(DateUtil.timeStamp(orderOnfflineInfo.getBuyerTime(), "yyyy-MM-dd HH:mm"));
        mName.setText(orderOnfflineInfo.getCourseName());
        mClazz.setRightText( orderOnfflineInfo.getCourseVideoName());
        mMethods.setRightText(orderOnfflineInfo.getCourseTypeName());
        mMessage.setVisibility(View.GONE);
        mAccount = orderOnfflineInfo.getTotalPrice();
        if (orderOnfflineInfo.getDiscountPercent()!=0 && orderOnfflineInfo.getDiscountPercent()!=100) {
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setRightText(String.format("%.1f",(float)orderOnfflineInfo.getDiscountPercent() / 10)+"折");
            needPay = mAccount*orderOnfflineInfo.getDiscountPercent() / 100 ;
            //优惠价格
            mDisPrice.setRightText("-¥" + (ArithUtils.round3( mAccount - needPay) ));
            mDisPrice.setViewGone(R.id.ccb_videoorder_disprice,true);
            mMessage.setViewGone(R.id.ccb_videoorder_message,true);
        }else {
            needPay = mAccount;
            mDisPrice.setViewGone(R.id.ccb_videoorder_disprice,false);
            mMessage.setViewGone(R.id.ccb_videoorder_message,false);
        }
        //课程总价
        mCount.setRightText("¥" + ArithUtils.round(mAccount));
         //优惠信息
        String content = "";
        switch (orderOnfflineInfo.getOrderStatus()) {
            case IntegerUtil.ORDER_STATUS_WAITPAY:
                //待支付
                if (orderOnfflineInfo.getUpperFrame() == 0) {
                    tvLiveorderDelte.setVisibility(View.VISIBLE);
                    tvLiveorderDelte.setText("课程已经下架");
                } else {
                    tvLiveorderConfirm.setVisibility(View.VISIBLE);
                    if(orderOnfflineInfo.getPayTime()>0){
                        tvLiveorderConfirm.setText("立即支付");
                    }else{
                        orderOnfflineInfo.setOrderStatus(9);
                        tvLiveorderConfirm.setText("删除");
                    }
                }
                content = "待支付：<font color='#FFAE12'>" + ArithUtils.round3(needPay) + "</font>";
                mStatus.setText(StringUtils.textFormatHtml(content));
                setCutDownTimer(orderOnfflineInfo.getPayTime());
                break;
            case IntegerUtil.ORDER_STATUS_PAYED:
                //已支付,已付款
                if (orderOnfflineInfo.getUpperFrame() == 0) {
                  //  tvLiveorderDelte.setVisibility(View.VISIBLE);
                  //  tvLiveorderDelte.setText("课程已经下架");
                }
                content = "已支付：<font color='#FFAE12'>" + ArithUtils.round3(needPay) + "</font>";
                mStatus.setText(StringUtils.textFormatHtml(content));
                break;
            case IntegerUtil.ORDER_STATUS_FINISH:
                //已支付, 完成
                if (orderOnfflineInfo.getUpperFrame() == 0) {
                   // tvLiveorderDelte.setVisibility(View.VISIBLE);
                  //  tvLiveorderDelte.setText("课程已经下架");
                }
                content = "已支付：<font color='#FFAE12'>" + ArithUtils.round3(needPay) + "</font>";
                mStatus.setText(StringUtils.textFormatHtml(content));
                break;
            case IntegerUtil.ORDER_STATUS_ClOSE:
                tvLiveorderConfirm.setVisibility(View.VISIBLE);
                tvLiveorderConfirm.setText("删除");
                content = "已关闭：<font color='#FFAE12'>" + ArithUtils.round3(needPay) + "</font>";
                mStatus.setText(StringUtils.textFormatHtml(content));
                break;
        }


    }

    @OnClick({R.id.common_bar_leftBtn, R.id.btn_void_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.btn_void_confirm:
                if(orderOnfflineInfo.getOrderStatus()==1){
                    ConfirmPay payUtil = new ConfirmPay(orderOnfflineInfo.getOrderId(),mAccount,0, needPay);
                    Intent intent = new Intent(MineVideoOrderActivity.this, OrderPayActivity.class);
                    intent.putExtra(StringUtils.COURSE_TYPE,3);
                    intent.putExtra(StringUtils.ACTIVITY_DATA,payUtil);
                    startActivity(intent);

                }else if(orderOnfflineInfo.getOrderStatus()==9) {
                    ConfirmDialog dialog = ConfirmDialog.newInstance("提示", "您确定删除订单吗？删除后该订单信息将不可恢复！", "取消", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    //删除
                                    postRemoveOrder();
                                    break;
                            }
                        }
                    });
                }
                break;
        }

    }

    private void postRemoveOrder(){
        HttpService httpService = Http.getHttpService();
        Map<String,Object> map= new HashMap<String, Object>();
        map.put("orderId", id);
        httpService.removeOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        ToastUtil.showToast(strTeacher);
                        showEventBus();
                    }
                    @Override
                    public void  onResultError(ResultException ex){
                        ToastUtil.showToast(ex.getMessage());
                        ToastUtil.showToast(ex.getMessage());
                        if(ex.getErrCode()==222222){
                            showEventBus();
                        }
                    }
                });
    }
    private void showEventBus(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11014);
        EventBus.getDefault().post(bundle);
        onBackPressed();

    }

    private void setCutDownTimer(long payTime){
        if(orderOnfflineInfo.getPayTime()<=0 || orderOnfflineInfo.getUpperFrame() <1){
            return;
        }
        tvOfflineCountdown.setVisibility(View.VISIBLE);
        cutDownTimer = new CountDownTimer(payTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long cutDownTime = millisUntilFinished / 1000;
                long minute = cutDownTime/60;
                long second = cutDownTime%60;
                String contentTime = "剩余支付时间：<font color='#D92B2B'>" + (minute>0&&minute<10?"0"+minute:minute)
                        + "分钟" + (second>0&&second<10?"0"+second:second)+ "秒</font>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tvOfflineCountdown.setText(Html.fromHtml(contentTime,Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvOfflineCountdown.setText(Html.fromHtml(contentTime));
                }
            }
            @Override
            public void onFinish() {
                tvOfflineCountdown.setVisibility(View.GONE);
                tvLiveorderConfirm.setText("删除");
                orderOnfflineInfo.setOrderStatus(9);
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11015);
                EventBus.getDefault().post(bundle);
            }
        };
        cutDownTimer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cutDownTimer==null){
            return;
        }
        cutDownTimer.cancel();
    }
}
