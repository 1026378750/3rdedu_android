package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.share.sdk.Constant;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*****
 * 直播课订单详情
 */
public class MIneLiveOrderActivity extends BaseActivity {
    @BindView(R.id.tv_liveorder_name)
    TextView mName;
    @BindView(R.id.ccb_liveorder_clazz)
    CommonCrosswiseBar mClazz;
    @BindView(R.id.ccb_liveorder_methods)
    CommonCrosswiseBar mMethods;
    @BindView(R.id.ccb_liveorder_subject)
    CommonCrosswiseBar mSubject;
    @BindView(R.id.ccb_liveorder_times)
    CommonCrosswiseBar mTimes;
    @BindView(R.id.ccb_liveorder_time)
    CommonCrosswiseBar mTime;
    @BindView(R.id.ccb_liveorder_schooltime)
    CommonCrosswiseBar mSchoolTime;
    @BindView(R.id.ccb_liveorder_price)
    CommonCrosswiseBar mPrice;
    @BindView(R.id.ccb_liveorder_count)
    CommonCrosswiseBar mCount;
    @BindView(R.id.ccb_liveorder_message)
    CommonCrosswiseBar mMessage;
    @BindView(R.id.ccb_liveorder_freeprice)
    CommonCrosswiseBar mFreeprice;

    @BindView(R.id.rv_liveorder)
    RecyclerView rvLiveorder;
    @BindView(R.id.tv_open_time)
    TextView tvOpenTime;
    @BindView(R.id.ccb_live_teachname)
    CommonCrosswiseBar ccbLiveTeachname;
    @BindView(R.id.btn_liveorder_confirm)
    Button tvLiveorderConfirm;
    @BindView(R.id.tv_offline_countdown)
    TextView tvOfflineCountdown;
    @BindView(R.id.ccb_liveorder_ordernum)
    CommonCrosswiseBar ccbLiveorderOrdernum;
    @BindView(R.id.ccb_liveorder_ordertime)
    CommonCrosswiseBar ccbLiveorderOrdertime;
    @BindView(R.id.ccb_liveorder_adress)
    CommonCrosswiseBar ccbLiveorderAdress;
    @BindView(R.id.tv_mine_already)
    TextView tvMineAlready;
    @BindView(R.id.tv_liveorder_delte)
    TextView tvLiveorderDelte;
    private CountDownTimer cutDownTimer;

    private List<Long> startTimeArray = new ArrayList<>();

    private int id;
    private OrderOnfflineInfo orderOnfflineInfo;

    @Override
    public void initData() {
        id = getIntent().getIntExtra(StringUtils.ORDER_ID, 0);
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
                        startTimeArray = orderOnfflineInfo.getStartTimeArray();
                        setMineVideOrder();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }

    private long mAccount = 0;
    private long needPay = 0;
    private void setMineVideOrder() {
        rvLiveorder.setVisibility(View.GONE);
        mName.setText(orderOnfflineInfo.getCourseName());
        mClazz.setRightText(orderOnfflineInfo.getCourseTypeName());
        mMethods.setRightText(orderOnfflineInfo.getDirectTypeName());
        mSubject.setRightText(orderOnfflineInfo.getGradeName()+" "+orderOnfflineInfo.getSubjectName());
        ccbLiveorderOrdernum.setRightText(orderOnfflineInfo.getOrderCode());
        ccbLiveorderOrdertime.setRightText(DateUtil.timeStamp(orderOnfflineInfo.getBuyerTime(), "yyyy-MM-dd HH:mm"));
        mTimes.setRightText(orderOnfflineInfo.getClassSum() + "次");
        int teacherHour=orderOnfflineInfo.getClassSum() * orderOnfflineInfo.getTeachingPeriod();
        mTime.setRightText(teacherHour+ "小时");
        ccbLiveTeachname.setRightText(orderOnfflineInfo.getTeacherNick());
        mSchoolTime.setRightText(DateUtil.weekTimeStamp(startTimeArray.get(0), "yyyy-MM-dd") +" "+ DateUtil.timeStamp(startTimeArray.get(0), "yyyy-MM-dd HH:mm"));
        mPrice.setRightText("¥" + ArithUtils.round(orderOnfflineInfo.getOnePrice()) + "/次");
       // mAccount = orderOnfflineInfo.getOnePrice()*(orderOnfflineInfo.getBuyNum()+orderOnfflineInfo.getGiveNum());
        mAccount=orderOnfflineInfo.getTotalPrice();
        //得到优惠信息
        if(orderOnfflineInfo.getDiscountPercent() !=100 && orderOnfflineInfo.getDiscountPercent()!=0){
            mMessage.setRightText(String.format("%.1f",(float)orderOnfflineInfo.getDiscountPercent() / 10)+"折");
           // needPay = orderOnfflineInfo.getBuyNum()*orderOnfflineInfo.getOnePrice()*orderOnfflineInfo.getDiscountPercent()/100;
            needPay = orderOnfflineInfo.getTotalPrice()*orderOnfflineInfo.getDiscountPercent()/100;
            mFreeprice.setRightText("-¥" + ArithUtils.round(mAccount-needPay));
            mMessage.setViewGone(R.id.ccb_liveorder_message,true);
            mFreeprice.setViewGone(R.id.ccb_liveorder_freeprice,true);
        }else {
           // needPay = orderOnfflineInfo.getBuyNum()*orderOnfflineInfo.getOnePrice();
            needPay=orderOnfflineInfo.getTotalPrice();
            mFreeprice.setRightText("");
            mMessage.setViewGone(R.id.ccb_liveorder_message,false);
            mFreeprice.setViewGone(R.id.ccb_liveorder_freeprice,false);
        }
        mCount.setRightText("¥" + ArithUtils.round3(mAccount));
        switch (orderOnfflineInfo.getOrderStatus()) {

            case IntegerUtil.ORDER_STATUS_WAITPAY:
                //待支付
                if (orderOnfflineInfo.getUpperFrame() == 0) {
                    tvLiveorderDelte.setVisibility(View.VISIBLE);
                    tvLiveorderDelte.setText("课程已经下架");
                }else {
                    tvLiveorderConfirm.setVisibility(View.VISIBLE);
                    if(orderOnfflineInfo.getPayTime()>0){
                        tvLiveorderConfirm.setText("立即支付");
                    }else{
                        orderOnfflineInfo.setOrderStatus(9);
                        tvLiveorderConfirm.setText("删除");
                    }
                }
                tvMineAlready.setText(StringUtils.textFormatHtml("待支付：<font color='#FFAE12'>"+"¥"+ ArithUtils.round3(needPay) + "</font>"));
                setCutDownTimer(orderOnfflineInfo.getPayTime());
                break;

            case IntegerUtil.ORDER_STATUS_PAYED:
                //已支付,已付款
                if (orderOnfflineInfo.getUpperFrame() == 0) {
                   // tvLiveorderDelte.setVisibility(View.VISIBLE);
                    //tvLiveorderDelte.setText("课程已经下架");
                }
                tvMineAlready.setText(StringUtils.textFormatHtml("已支付：<font color='#FFAE12'>"+"¥"+ ArithUtils.round3(needPay) + "</font>"));
                break;

            case IntegerUtil.ORDER_STATUS_FINISH:
                //已支付, 完成
                if (orderOnfflineInfo.getUpperFrame() == 0) {
                    //tvLiveorderDelte.setVisibility(View.VISIBLE);
                    //tvLiveorderDelte.setText("课程已经下架");
                }
                tvMineAlready.setText(StringUtils.textFormatHtml("已支付：<font color='#FFAE12'>"+"¥"+ ArithUtils.round3(needPay) + "</font>"));
                break;

            case IntegerUtil.ORDER_STATUS_ClOSE:
                tvLiveorderConfirm.setVisibility(View.VISIBLE);
                tvLiveorderConfirm.setText("删除");
                tvMineAlready.setText(StringUtils.textFormatHtml( "已关闭：<font color='#FFAE12'>"+"¥"+ ArithUtils.round3(needPay) + "</font>"));
                break;
        }
        startTimeArray.remove(0);
        rvLiveorder.setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器，封装后的适配器只需要实现一个函数
        rvLiveorder.setAdapter(new SimpleAdapter<Long>(getApplication(), startTimeArray, R.layout.course_open_time_item) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Long data) {
                holder.setText(R.id.tv_open_time, DateUtil.weekTimeStamp(data, "yyyy-MM-dd")  +" "+ DateUtil.timeStamp(data, "yyyy-MM-dd HH:mm"));
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_live_order;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.btn_liveorder_confirm, R.id.tv_open_time})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.tv_open_time:
                rvLiveorder.setVisibility(View.VISIBLE);
                tvOpenTime.setVisibility(View.GONE);
                break;
            case R.id.btn_liveorder_confirm:
                if(orderOnfflineInfo.getOrderStatus()==1){
                    ConfirmPay payUtil = new ConfirmPay(orderOnfflineInfo.getOrderId(),mAccount,0, needPay);
                    Intent intent = new Intent(MIneLiveOrderActivity.this, OrderPayActivity.class);
                    intent.putExtra(StringUtils.COURSE_TYPE,2);
                    intent.putExtra(StringUtils.ACTIVITY_DATA,payUtil);
                    startActivity(intent);
                }else if(orderOnfflineInfo.getOrderStatus()==9){
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
        if (startTimeArray!=null)
            startTimeArray.clear();
    }

}
