package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.pay.IAlPayResultListener;
import com.main.disanxuelib.pay.PayAsyncTask;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.app.MyApplication;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.MyBalance;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单详情
 * Created by liukui on 2017/11/29.
 */

public class OrderPayActivity extends BaseActivity {
    @BindView(R.id.ccb_orderpay_money)
    CommonCrosswiseBar mMoney;
    @BindView(R.id.tv_orderpay_deduction)
    TextView mDeduction;
    //支付页面，“账户余额”字段改成“钱包余额抵扣”。如果钱包余额为0，则此条显示为：钱包余额为0，不可抵扣。不显示勾选框。
    @BindView(R.id.rb_orderpay_deductionbtn)
    RadioButton mDeductionBtn;
    @BindView(R.id.tv_orderpay_gpfdpay)
    TextView mGpfdPay;
    @BindView(R.id.ccb_orderpay_alipay)
    CommonCrosswiseBar mAliPay;
    @BindView(R.id.ccb_orderpay_wchatpay)
    CommonCrosswiseBar mWchatPay;
    //是否显示数据
    @BindView(R.id.ll_order_pay)
    LinearLayout llOrderPay;
    @BindView(R.id.tv_order_pay)
    TextView tvOrderPay;

    private MyBalance myBalance;
    //是否0元支付
    private boolean isZeroPay = false;
    ConfirmPay confirmPay;
    private Map<String, Object> paramRequest = new HashMap<String, Object>();
    private int COURSE_TYPE;
    @Override
    public void initData() {
        paramRequest.clear();
        confirmPay =  getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        COURSE_TYPE=  getIntent().getIntExtra(StringUtils.COURSE_TYPE, 0);
        if(confirmPay==null){
            ToastUtil.showShort("支付异常");
            onBackPressed();
            return;
        }
        mMoney.setRightText("¥" + ArithUtils.round(confirmPay.paidAmount));
        postMyBalance();
        paramRequest.put("orderId", confirmPay.orderId);
    }

    //设置订单
    private void setLlOrderPay() {
        String contentMony = "";
        if (myBalance.getAvailAmount() == 0) {
            //可用余额为0
            contentMony = "<font color='#FFAE12'>钱包余额为0，不可抵扣</font>";
            mDeductionBtn.setVisibility(View.GONE);
            mGpfdPay.setText("¥" + ArithUtils.round(confirmPay.paidAmount));
        } else if (myBalance.getAvailAmount() >= confirmPay.paidAmount) {
            //可用金额大于总金额
            contentMony ="可抵扣：<font color='#FFAE12'>¥" + ArithUtils.round(confirmPay.paidAmount) + "</font>";
            mGpfdPay.setText("¥0.00");
            paymethod = -1;
            llOrderPay.setVisibility(View.GONE);
            tvOrderPay.setVisibility(View.GONE);
            mDeductionBtn.setChecked(true);
            isZeroPay = true;
        } else {
            contentMony  ="可抵扣：<font color='#FFAE12'>¥" + ArithUtils.roundLong(myBalance.getAvailAmount()) + "</font>";
            mDeductionBtn.setChecked(true);
            mGpfdPay.setText("¥" + ArithUtils.roundLong(confirmPay.paidAmount - myBalance.getAvailAmount()));
        }
        mDeduction.setText(StringUtils.textFormatHtml(contentMony));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_orderpay;
    }

    //得到账户余额信息
    private void postMyBalance() {
        HttpService httpService = Http.getHttpService();
        httpService.myBalance(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBalance>(mContext,true) {
                    @Override
                    protected void onDone(MyBalance strTeacher) {
                        myBalance = strTeacher;
                        setLlOrderPay();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.ll_orderpay_deductionlay, R.id.ccb_orderpay_alipay, R.id.ccb_orderpay_wchatpay, R.id.tv_liveorder_confirm, R.id.rl_orderpay_offlinepay})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ll_orderpay_deductionlay:

                if(myBalance.getAvailAmount()==0)//余额为0
                    return;

                if(mDeductionBtn.isChecked()){
                    //不抵扣
                    if(paymethod==-1){
                        paymethod = 1;
                        mAliPay.setRightButton(R.mipmap.btn_checked);
                        mWchatPay.setRightButton(R.mipmap.btn_unchecked);
                    }
                    isZeroPay = false;
                    llOrderPay.setVisibility(View.VISIBLE);
                    tvOrderPay.setVisibility(View.VISIBLE);
                    String contentMony  ="可抵扣：<font color='#FFAE12'>¥" + ArithUtils.roundLong(myBalance.getAvailAmount()>confirmPay.paidAmount?confirmPay.paidAmount:myBalance.getAvailAmount()) + "</font>";
                    mGpfdPay.setText("¥" + ArithUtils.roundLong(confirmPay.paidAmount));
                    mDeduction.setText(StringUtils.textFormatHtml(contentMony));
                }else{
                    //抵扣
                    String contentMony = "";
                    if (myBalance.getAvailAmount() >= confirmPay.paidAmount) {
                        //可用金额大于总金额
                        contentMony = "可抵扣：<font color='#FFAE12'>¥" + ArithUtils.round(confirmPay.paidAmount) + "</font>";
                        mGpfdPay.setText("¥0.00");
                        paymethod = -1;
                        llOrderPay.setVisibility(View.GONE);
                        tvOrderPay.setVisibility(View.GONE);
                        isZeroPay = true;
                    }else{
                        if(paymethod==-1){
                            paymethod = 1;
                            mAliPay.setRightButton(R.mipmap.btn_checked);
                            mWchatPay.setRightButton(R.mipmap.btn_unchecked);
                        }
                        llOrderPay.setVisibility(View.VISIBLE);
                        tvOrderPay.setVisibility(View.VISIBLE);
                        //可用金额小于总金额（联合支付）
                        contentMony  ="可抵扣：<font color='#FFAE12'>¥" + ArithUtils.roundLong(myBalance.getAvailAmount()) + "</font>";
                        mGpfdPay.setText("¥" + ArithUtils.roundLong((confirmPay.paidAmount) - (myBalance.getAvailAmount())));
                        isZeroPay = false;
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        mDeduction.setText(Html.fromHtml(contentMony,Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        mDeduction.setText(Html.fromHtml(contentMony));
                    }
                }
                mDeductionBtn.setChecked(!mDeductionBtn.isChecked());

                break;

            case R.id.rl_orderpay_offlinepay:
                //线下支付
                startActivity(new Intent(this, UpLoadPayActivity.class));
                break;

            case R.id.ccb_orderpay_alipay:
                mAliPay.setRightButton(R.mipmap.btn_checked);
                mWchatPay.setRightButton(R.mipmap.btn_unchecked);
                paymethod = 1;
                break;

            case R.id.ccb_orderpay_wchatpay:
                mWchatPay.setRightButton(R.mipmap.btn_checked);
                mAliPay.setRightButton(R.mipmap.btn_unchecked);
                paymethod = 2;
                break;

            case R.id.tv_liveorder_confirm:
                //提交
                if(isZeroPay){
                    paramRequest.put("offsetAmount", confirmPay.paidAmount==0 ?0 : confirmPay.paidAmount);
                    zeroPayRequest(paramRequest);
                  }else {
                    paramRequest.put("payMethod", paymethod);
                    paramRequest.put("paidAmount", confirmPay.paidAmount);
                    if(mDeductionBtn.isChecked()){
                        //有抵扣且全部抵扣掉
                        paramRequest.put("offsetAmount",myBalance.getAvailAmount());
                        paramRequest.put("paidPrice", confirmPay.paidAmount-myBalance.getAvailAmount());
                    }else{
                        //常规支付
                        paramRequest.put("offsetAmount",0);
                        paramRequest.put("paidPrice", confirmPay.paidAmount);
                    }
                    confirmPay(paramRequest);
                  }
                break;
        }
    }

    //支付方式 1：支付宝支付 2.
    private int paymethod = 1;
    private void confirmPay(Map<String, Object> map){
        HttpService httpService = Http.getHttpService();
        httpService.aliConfirmPay(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String jsonObjects) {
                        try {
                            String resultSign = new JSONObject(jsonObjects).optString("resultSign");
                            if (paymethod == 1) {
                                aliPay(resultSign);
                            } else {
                                if(isWeChatAppInstalled()){
                                    JSONObject jsonObject = new JSONObject(resultSign);
                                    PayReq payReq = new PayReq();
                                    payReq.appId = ConstantUrl.WX_APP_ID;
                                    payReq.sign = jsonObject.optString("sign");//加密签名
                                    payReq.prepayId = jsonObject.optString("prepay_id");//订单id
                                    payReq.partnerId = jsonObject.optString("partnerid");//商户id
                                    payReq.timeStamp = jsonObject.optString("timeStamp");
                                    payReq.packageValue = jsonObject.optString("package");
                                    payReq.nonceStr = jsonObject.optString("nonceStr");//随机数
                                    MyApplication.getInstance().getIwxApi().sendReq(payReq);
                                }else {
                                    ToastUtil.showShort("微信没有安装,请安装微信");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }

    // 0元支付
    private void zeroPayRequest(Map<String, Object> map) {
        HttpService httpService = Http.getHttpService();
        httpService.zeroOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        ToastUtil.showShort(strTeacher);
                      /*  Intent intent = getIntent();
                        intent.setClass(mContext,PayResultActivity.class);
                        intent.putExtra(StringUtils.ORDER_ID,confirmPay.orderId);
                        intent.putExtra(StringUtils.COURSE_TYPE,COURSE_TYPE);
                        startActivity(intent);*/
                        startPayResult();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                        if(ex.getErrCode()==222222){
                            startPayResult();
                        }
                    }
                });
    }
    private void startPayResult(){
        Intent intent = getIntent();
        intent.setClass(mContext,PayResultActivity.class);
        intent.putExtra(StringUtils.ORDER_ID,confirmPay.orderId);
        intent.putExtra(StringUtils.COURSE_TYPE,COURSE_TYPE);
        startActivity(intent);
    }

    private void aliPay(String resultSign) {

        PayAsyncTask payAsyncTask = new PayAsyncTask(this, new IAlPayResultListener() {
            @Override
            public void onPaySuccess() {
                Intent intent = getIntent();
                intent.setClass(mContext,PayResultActivity.class);
                intent.putExtra(StringUtils.ORDER_ID,confirmPay.orderId);
                intent.putExtra(StringUtils.COURSE_TYPE,COURSE_TYPE);
                startActivity(intent);
            }

            @Override
            public void onPaying() {
                ToastUtil.showToast("支付完成");
            }

            @Override
            public void onPayFail() {
                ToastUtil.showToast("支付失败");
            }

            @Override
            public void onPayCancel() {
                ToastUtil.showToast("用户取消支付");
            }

            @Override
            public void onPayConnectError() {

            }
        });
        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, resultSign);
    }

    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_7001:
                Intent intent = getIntent();
                intent.setClass(mContext,PayResultActivity.class);
                intent.putExtra(StringUtils.ORDER_ID,confirmPay.orderId);
                intent.putExtra(StringUtils.COURSE_TYPE,COURSE_TYPE);
                startActivity(intent);
                break;
            case IntegerUtil.EVENT_ID_7005:
            case IntegerUtil.EVENT_ID_7006:
                //支付失败到订单界面
                /*AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                startActivity(new Intent(OrderPayActivity.this, MineOrderActivity.class));*/
                break;
        }
        return false;
    }

    /**
     * 判断微信客户端是否存在
     *SDK判断和包名
     * @return true安装, false未安装
     */
    private boolean isWeChatAppInstalled() {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, ConstantUrl.WX_APP_ID);
        if(api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
            return true;
        } else {
            final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (paramRequest!=null)
            paramRequest.clear();
    }

}
