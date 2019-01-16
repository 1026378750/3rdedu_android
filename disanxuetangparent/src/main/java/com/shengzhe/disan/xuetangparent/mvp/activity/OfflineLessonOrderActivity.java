package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswise;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.CourseOrder;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*******
 * 线下班课订单结算
 */
public class OfflineLessonOrderActivity extends BaseActivity {

    @BindView(R.id.iv_teacher_header)
    ImageView ivItemHead;
    @BindView(R.id.iv_teacher_mesg)
    TextView tvItemTeacher;
    @BindView(R.id.iv_class_photo)
    ImageView ivHotPhoto;
    @BindView(R.id.tv_class_name)
    TextView tvSubjctClassType;
    @BindView(R.id.tv_class_number)
    TextView tvOfflineMannum;
    @BindView(R.id.tv_class_details)
    TextView tvSubjctClassDetails;
    @BindView(R.id.tv_class_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_class_price)
    TextView tvOrderPrice;

    @BindView(R.id.ccb_order_buytype)
    CommonCrosswise ccbOrderBuytype;
    @BindView(R.id.ccb_order_buytimes)
    CommonCrosswise ccbOrderBuytimes;
    @BindView(R.id.ccb_order_sigleprice)
    CommonCrosswise ccbOrderSigleprice;
    @BindView(R.id.ccb_order_sumprice)
    CommonCrosswise ccbOrderSumprice;
    @BindView(R.id.ccb_order_free)
    CommonCrosswise ccbOrderFree;
    @BindView(R.id.ccb_order_parentprice)
    CommonCrosswise ccbOrderParentprice;
    @BindView(R.id.btn_offlineorder_confirm)
    Button btnOfflineorderConfirm;

    private CourseSquadBean courseBean;
    private  long  Payprice=0l, Preprice=0l;
    private int orderId = 0;
    private String times;

    @Override
    public void initData() {
        courseBean = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        Payprice=getIntent().getLongExtra("Payprice",0l);
        Preprice=getIntent().getLongExtra("Preprice",0l);
          times=getIntent().getStringExtra("times");

        if (courseBean == null) {
            courseBean = new CourseSquadBean();
            return;
        }
        btnOfflineorderConfirm.setText("¥"+ArithUtils.round(Payprice)+" - 立即支付");

        tvSubjctClassType.setText(courseBean.courseName);
        tvOfflineMannum.setText(StringUtils.textFormatHtml("<font color='#1d97ea'>" + courseBean.salesVolume + "</font>" + "/" + courseBean.maxUser + "人"));
        tvSubjctClassDetails.setText(courseBean.subjectName + " " + courseBean.gradeName);
        tvOrderTime.setText(courseBean.duration + "小时/次，共" + courseBean.classTime + "次");
        tvOrderPrice.setText(StringUtils.textFormatHtml("<font color='#FFAE12'>" + "¥" + ArithUtils.round(courseBean.totalPrice) + "</font>"));
        tvItemTeacher.setText(courseBean.teacherName);
        ImageUtil.loadImageViewLoding(mContext, courseBean.pictureUrl, ivHotPhoto, R.mipmap.default_error, R.mipmap.default_error);
        ImageUtil.loadCircleImageView(mContext, courseBean.getTeacherPhotoUrl(), ivItemHead, R.mipmap.ic_personal_avatar);

        ccbOrderBuytype.setTitleText(courseBean.canJoin==1?"插班":"不可插班");
        if (courseBean.areadyCourseClass == 0 && (courseBean.isAreadyBuy == 1 && courseBean.salesVolume != courseBean.maxUser)) {
            ccbOrderBuytype.setTitleText("全程");
        }
        ccbOrderBuytimes.setTitleText(times+"次");
        ccbOrderSigleprice.setTitleText("¥"+ArithUtils.round(courseBean.price)+"/次");
        ccbOrderSumprice.setTitleText("¥"+ArithUtils.round(courseBean.price*Integer.parseInt(times)));
        if(courseBean.campusDiscountId==0||(courseBean.getCanJoin()==1 && courseBean.classTime!=courseBean.surplusCourseClass) ){
            ccbOrderFree.setTitleText("无");
        }else {
            ccbOrderFree.setTitleText(String.format("%.1f",(float)(courseBean.totalPrice/courseBean.getDiscountTotalPrice())/1000) + "折");
        }

        ccbOrderParentprice.setTitleText("¥"+ArithUtils.round(Payprice));

    }

    @Override
    public int setLayout() {
        return R.layout.activity_offline_lesson_order;
    }

    private void postCreateCourseOrder() {
        LogUtils.d(" SystemInfoUtil.getInstallationId() = " + SystemInfoUtil.getInstallationId());
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseBean.courseId);
        map.put("courseVersion", courseBean.courseVersion);
        //  map.put("discountAmount", courseBean.totalPrice-Payprice);
        if(courseBean.canJoin==1){
            map.put("buyNumber",courseBean.surplusCourseClass);
            if(courseBean.campusDiscountId!=0 && courseBean.surplusCourseClass<courseBean.classTime){
                map.put("totalPrice", courseBean.surplusCoursePrice);
            }else if(courseBean.surplusCourseClass<courseBean.classTime) {
                map.put("totalPrice", courseBean.surplusCoursePrice);
                if(courseBean.campusDiscountId!=0){
                    map.put("courseDiscountVersion",courseBean.campusDiscountVersion);
                    map.put("courseDiscountId", courseBean.campusDiscountId);
                }
            }else {
                map.put("totalPrice", courseBean.totalPrice);
                if(courseBean.campusDiscountId!=0){
                    map.put("courseDiscountVersion",courseBean.campusDiscountVersion);
                    map.put("courseDiscountId", courseBean.campusDiscountId);
                }
            }

        }else {
            map.put("buyNumber",courseBean.classTime);
            map.put("totalPrice", courseBean.totalPrice);
            if(courseBean.campusDiscountId!=0){
                map.put("courseDiscountVersion",courseBean.campusDiscountVersion);
                map.put("courseDiscountId", courseBean.campusDiscountId);
            }

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
                        if (Payprice == 0) {
                            postZeroOrder();
                        } else {
                            long  paidAmount=0l;
                            if(courseBean.canJoin==1){
                                // paidAmount=  courseBean.surplusCoursePrice-courseBean.discountSurplusCoursePrice;
                                //paidAmount=courseBean.surplusCoursePrice;
                               /* if(courseBean.campusDiscountId==0){
                                    paidAmount=courseBean.surplusCoursePrice;
                                }else{
                                    paidAmount=courseBean.surplusCoursePrice;
                                }*/
                                if(courseBean.campusDiscountId!=0  && courseBean.surplusCourseClass<courseBean.classTime){
                                    paidAmount=courseBean.surplusCoursePrice;
                                }else if(courseBean.surplusCourseClass<courseBean.classTime)
                                {
                                    paidAmount=courseBean.surplusCoursePrice;
                                }else {
                                    paidAmount=courseBean.getDiscountTotalPrice();
                                }

                            }else {
                                if(courseBean.campusDiscountId!=0){
                                    paidAmount= courseBean.discountTotalPrice;
                                }else {
                                    paidAmount= courseBean.totalPrice;
                                }
                            }
                            ConfirmPay payUtil = new ConfirmPay(courseOrder.getOrderId(), courseBean.totalPrice, courseBean.courseId, paidAmount);
                            Intent intent = new Intent(mContext, OrderPayActivity.class);
                            intent.putExtra(StringUtils.COURSE_TYPE,4);
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
                      /*  Intent intent = new Intent(mContext, PayResultActivity.class);
                        intent.putExtra(StringUtils.ORDER_ID, orderId);
                        intent.putExtra(StringUtils.COURSE_TYPE, 4);
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
        Intent intent = new Intent(mContext, PayResultActivity.class);
        intent.putExtra(StringUtils.ORDER_ID, orderId);
        intent.putExtra(StringUtils.COURSE_TYPE, 4);
        startActivity(intent);

    }
    @OnClick({R.id.common_bar_leftBtn, R.id.btn_offlineorder_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.btn_offlineorder_confirm:
                postCreateCourseOrder();
                break;
        }
    }
}
