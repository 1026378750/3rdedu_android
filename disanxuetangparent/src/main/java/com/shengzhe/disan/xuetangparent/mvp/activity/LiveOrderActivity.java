package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.MyRecyclerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.CourseItemBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOrder;
import com.shengzhe.disan.xuetangparent.bean.PayDirectInfo;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 直播课订单详情
 * Created by Administrator on 2017/11/29.
 */

public class LiveOrderActivity extends BaseActivity {
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
    @BindView(R.id.tv_liveorder_count)
    TextView mCountText;
    @BindView(R.id.rv_liveorder)
    RecyclerView rvLiveorder;
    @BindView(R.id.tv_open_time)
    TextView tvOpenTime;
    @BindView(R.id.ccb_live_teachname)
    CommonCrosswiseBar ccbLiveTeachname;
    @BindView(R.id.tv_liveorder_confirm)
    TextView tvLiveorderConfirm;
    private PayDirectInfo payDirectInfo;
    private List<CourseItemBean> courseItemBeanList = new ArrayList<>();
    private int courseId = -1;
    private int orderId = 0;
    private int courseTypeName = 0;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID, -1);
        postPayDirectInfo();
    }

    private void setRvLiveorder() {
        rvLiveorder.setVisibility(View.GONE);
        mName.setText(payDirectInfo.getCourseName());
        mClazz.setRightText(payDirectInfo.getCourseTypeName());
        mMethods.setRightText(payDirectInfo.getDirectTypeName());
        mSubject.setRightText(payDirectInfo.getGradeName() + " " + payDirectInfo.getSubjectName());
        mTimes.setRightText(payDirectInfo.getClassTime() + "次");
        mTime.setRightText(payDirectInfo.getDuration() * payDirectInfo.getClassTime() + "小时");
        ccbLiveTeachname.setRightText(payDirectInfo.getTeacherNickName());
        mSchoolTime.setRightText(DateUtil.timeStampWeek(courseItemBeanList.get(0).getStartTiem(), "yyyy-MM-dd") + " " + DateUtil.timeStamp(courseItemBeanList.get(0).getStartTiem(), "yyyy-MM-dd HH:mm"));
        mPrice.setRightText("¥" + ArithUtils.round(payDirectInfo.getPrice()) + "/次");
        mCount.setRightText("¥" + ArithUtils.round(payDirectInfo.getCoursePrice()));
        if (payDirectInfo.getCourseDiscount() == 100) {
            mMessage.setViewGone(R.id.ccb_liveorder_message, false);
        } else {
            mMessage.setViewGone(R.id.ccb_liveorder_message, true);
            mMessage.setRightText(String.format("%.1f 折", (double) payDirectInfo.getCourseDiscount() / 10));
        }

        long needPay = payDirectInfo.getCoursePrice() * (payDirectInfo.getCourseDiscount() / 100);
        if (payDirectInfo.getFavorablePrice() == 0) {
            mFreeprice.setViewGone(R.id.ccb_liveorder_freeprice, false);
        } else {
            mFreeprice.setViewGone(R.id.ccb_liveorder_freeprice, true);
            mFreeprice.setRightText("-¥" + ArithUtils.round(payDirectInfo.getCoursePrice() - needPay));
        }
        mCountText.setText(BaseStringUtils.textFormatHtml("总额：<font color='#FFAE12'>" + "¥" + ArithUtils.round(needPay) + "</font>"));

        courseItemBeanList.remove(0);

        rvLiveorder.setLayoutManager(new LinearLayoutManager(mContext));
        rvLiveorder.setNestedScrollingEnabled(false);
        //添加分割线
        //  rvMineWallet.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //设置适配器，封装后的适配器只需要实现一个函数
        rvLiveorder.setAdapter(new SimpleAdapter<CourseItemBean>(getApplication(), courseItemBeanList, R.layout.course_open_time_item) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseItemBean data) {
                holder.setText(R.id.tv_open_time, DateUtil.weekTimeStamp(data.getStartTiem(), "yyyy-MM-dd") + " " + DateUtil.timeStamp(data.getStartTiem(), "yyy-MM-dd HH:mm"));
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_liveorder;
    }

    private void postPayDirectInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        httpService.payDirectInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<PayDirectInfo>(mContext, true) {
                    @Override
                    protected void onDone(PayDirectInfo strTeacher) {
                        payDirectInfo = strTeacher;
                        courseItemBeanList = payDirectInfo.getCourseItem();
                        setRvLiveorder();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_liveorder_confirm, R.id.tv_open_time})
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
            case R.id.tv_liveorder_confirm:
                postCreateCourseOrder();
                break;
        }
    }

    private void postCreateCourseOrder() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("courseVersion", payDirectInfo.getCourseVersion());
        map.put("totalPrice", payDirectInfo.getCoursePrice());
        map.put("discountAmount", payDirectInfo.getFavorablePrice());
        map.put("discountAmount", payDirectInfo.getFavorablePrice());
        map.put("discountAmount", payDirectInfo.getFavorablePrice());
        if (payDirectInfo.getCourseDiscount() != 100) {
            map.put("courseDiscountVersion", payDirectInfo.getCourseDiscountVersion());
            map.put("courseDiscountId", payDirectInfo.getCourseDiscountId());
        }
        httpService.createCourseOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseOrder>(mContext, true) {
                    @Override
                    protected void onDone(CourseOrder courseOrder) {
                        courseTypeName = 2;
                        orderId = courseOrder.getOrderId();
                        //提交支付
                        if (payDirectInfo.getCoursePrice() == 0) {
                            postZeroOrder();
                        } else {
                            ConfirmPay payUtil = new ConfirmPay(courseOrder.getOrderId(), payDirectInfo.getCoursePrice(), payDirectInfo.getCourseId(), payDirectInfo.getCoursePrice() * (payDirectInfo.getCourseDiscount() / 100));
                            Intent intent = new Intent(mContext, OrderPayActivity.class);
                            intent.putExtra(StringUtils.COURSE_TYPE,courseTypeName);
                            intent.putExtra(StringUtils.ACTIVITY_DATA, payUtil);
                            intent.putExtra(StringUtils.ORDER_ID, orderId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                        ToastUtil.showToast(ex.getMessage());
                    }
                });
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
        Intent intent = new Intent(mContext, PayResultActivity.class);
        intent.putExtra(StringUtils.ORDER_ID, orderId);
        intent.putExtra(StringUtils.COURSE_TYPE, courseTypeName);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (courseItemBeanList != null)
            courseItemBeanList.clear();
    }

}
