package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.CourseOrder;
import com.shengzhe.disan.xuetangparent.bean.PayVideo;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 视频课订单
 * Created by liukui on 2017/11/29.
 */

public class VideoOrderActivity extends BaseActivity {
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
    @BindView(R.id.tv_videoorder_count)
    TextView mCountText;

    private PayVideo payVideo;
    private int orderId = 0;
    private int courseId = 0;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID, courseId);
        mClazz.setLeftText("讲师名称");
        postPayVideo();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_videoorder;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_videoorder_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_videoorder_confirm:
                postCreateCourseOrder();
                break;
        }
    }

    /****
     * 创建订单
     */
    private void postCreateCourseOrder() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("courseVersion", payVideo.getCourseVersion());
        map.put("totalPrice", payVideo.getPrice());
        map.put("discountAmount", payVideo.getFavorablePrice());
        if (payVideo.getCourseDiscount() != 100) {
            map.put("courseDiscountId", payVideo.getCourseDiscountId());
            map.put("courseDiscountVersion", payVideo.getCourseDiscountVersion());
        }
        httpService.createCourseOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseOrder>(mContext, true) {
                    @Override
                    protected void onDone(CourseOrder courseOrder) {
                        orderId = courseOrder.getOrderId();
                        //提交支付
                        if (payVideo.getPrice() == 0) {
                            postZeroOrder();
                        } else {
                            ConfirmPay payUtil = new ConfirmPay(courseOrder.getOrderId(), payVideo.getPrice(), courseId, payVideo.getPrice() - payVideo.getFavorablePrice());
                            Intent intent = new Intent(VideoOrderActivity.this, OrderPayActivity.class);
                            intent.putExtra(StringUtils.COURSE_TYPE, 3);
                            intent.putExtra(StringUtils.ACTIVITY_DATA, payUtil);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }

    private void setVideoOrder() {
        mOrderNum.setVisibility(View.GONE);
        mTime.setVisibility(View.GONE);
        mName.setText(payVideo.getCourseName());
        mClazz.setRightText("" + payVideo.getLecturer());
        mMethods.setRightText("" + payVideo.getVideoTypeName());
        mCount.setRightText("¥" + ArithUtils.round(payVideo.getPrice()));
        if (payVideo.getCourseDiscount() == 100) {
            mMessage.setVisibility(View.GONE);
        } else {
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setRightText(String.format("%.1f 折", (float) payVideo.getCourseDiscount() / 10));
        }

        if (payVideo.getFavorablePrice() == 0) {
            mDisPrice.setVisibility(View.GONE);
        } else {
            mDisPrice.setVisibility(View.VISIBLE);
            mDisPrice.setRightText("-¥" + ArithUtils.round(Math.abs(payVideo.getFavorablePrice())));
        }
        mCountText.setText(StringUtils.textFormatHtml("总额：<font color='#FFAE12'>" + "¥" + ArithUtils.round(payVideo.getPrice() - payVideo.getFavorablePrice()) + "</font>"));
        mStatus.setText("已关闭");
        mStatus.setVisibility(View.GONE);
    }

    private void postZeroOrder() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        map.put("offsetAmount", 0);
        httpService.zeroOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        ToastUtil.showShort(strTeacher);
                       /* Intent intent = new Intent(mContext,PayResultActivity.class);
                        intent.putExtra(StringUtils.ORDER_ID,orderId);
                        startActivity(intent);*/
                        startPayResult();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                        if (ex.getErrCode() == 222222) {
                              /*  Intent intent = new Intent(mContext,PayResultActivity.class);
                                intent.putExtra(StringUtils.ORDER_ID,orderId);
                                startActivity(intent);*/
                            startPayResult();
                        }

                    }
                });
    }

    private void startPayResult() {
        Intent intent = new Intent(mContext, PayResultActivity.class);
        intent.putExtra(StringUtils.ORDER_ID, orderId);
        intent.putExtra(StringUtils.COURSE_TYPE, 3);
        startActivity(intent);
    }

    /****
     * 获取的订单信息
     */
    private void postPayVideo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        httpService.payVideoInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<PayVideo>(mContext, true) {
                    @Override
                    protected void onDone(PayVideo str) {
                        payVideo = str;
                        setVideoOrder();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }

}
