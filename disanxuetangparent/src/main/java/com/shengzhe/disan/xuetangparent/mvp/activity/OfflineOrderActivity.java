package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Discounts;
import com.main.disanxuelib.bean.Schedule;
import com.main.disanxuelib.bean.TeacherMethod;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.CourseOrder;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/4.
 *
 * 线下一对一订单支付
 *
 */

public class OfflineOrderActivity extends BaseActivity {
    @BindView(R.id.tv_offline_name)
    TextView mName;
    @BindView(R.id.ccb_offline_methods)
    CommonCrosswiseBar mMethods;
    @BindView(R.id.ccb_offline_nicename)
    CommonCrosswiseBar mNiceName;
    @BindView(R.id.ccb_offline_subject)
    CommonCrosswiseBar mSubject;
    @BindView(R.id.ccb_offline_times)
    CommonCrosswiseBar mTimes;
    @BindView(R.id.ccb_offline_time)
    CommonCrosswiseBar mTime;
    @BindView(R.id.ccb_offline_address)
    TextView mAddress;
    @BindView(R.id.ccb_offline_schooltime)
    CommonCrosswiseBar mSchoolTime;
    @BindView(R.id.ccb_offline_openall)
    TextView mOpenAll;
    @BindView(R.id.rv_offline_timelist)
    RecyclerView mTimeList;
    @BindView(R.id.ccb_offline_price)
    CommonCrosswiseBar mPrice;
    @BindView(R.id.ccb_offline_account)
    CommonCrosswiseBar mAcount;
    @BindView(R.id.ccb_offline_message)
    CommonCrosswiseBar mMessage;
    @BindView(R.id.ccb_offline_freeprice)
    CommonCrosswiseBar mFreePrice;
    @BindView(R.id.tv_offline_count)
    TextView mCount;

    private ArrayList<Schedule> scheduleList;
    private CourseOneInfo courseOneInfo;
    private TeacherMethod selectMet;
    private Discounts selectDis;
    private TeachingMethod teachingMethod;
    private Bundle bundle;

    private long mAccount = 0;
    private long needPay = 0;
    private int courseId = 0;
    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,0);
        bundle = getIntent().getExtras();
        teachingMethod = getIntent().getParcelableExtra(StringUtils.teachingMethod);
        courseOneInfo = bundle.getParcelable(StringUtils.teacher);
        selectMet = bundle.getParcelable(StringUtils.selectMet);
        selectDis = bundle.getParcelable(StringUtils.selectDis);
        scheduleList = getIntent().getExtras().getParcelableArrayList(StringUtils.scheduleList);
        getTeacherTimeType();
        setDatas();
    }

    private void setDatas() {
        mName.setText(courseOneInfo.getCourseName());
        mMethods.setRightText(selectMet.teachingMethodName);
        mNiceName.setRightText(TextUtils.isEmpty(courseOneInfo.getTeacherName())?"无":courseOneInfo.getTeacherName());
        mSubject.setRightText(courseOneInfo.getGradeName()+" "+courseOneInfo.getSubjectName());

        if(selectDis==null){
            number = Integer.parseInt(bundle.getString(StringUtils.num));
            mTimes.setRightText(number+"次");
            mTime.setRightText(number*teachingMethod.classTime+"小时");
            mAccount = number * selectMet.signPrice*teachingMethod.classTime;
        }else{
            //打包购买
            number =selectDis.buyNum;
            mTimes.setRightText((number+selectDis.giveNum)+"次");
            mTime.setRightText((number+selectDis.giveNum)*teachingMethod.classTime+"小时");
            mAccount = (number+selectDis.giveNum) * selectMet.signPrice*teachingMethod.classTime;
        }

        if(selectMet.campusDiscountSignPrice==0){
            //无折扣
            needPay = selectMet.signPrice*number*teachingMethod.classTime;
        }else{
            needPay = selectMet.campusDiscountSignPrice*number*teachingMethod.classTime;
        }
        mAcount.setRightText("¥"+ArithUtils.round(mAccount));
        mCount.setText(StringUtils.textFormatHtml("总额：<font color='#FFAE12'>¥"+ArithUtils.round(needPay)+"</font>"));
        mFreePrice.setRightText("-¥"+ArithUtils.round(Math.abs(mAccount - needPay)));
        mFreePrice.setVisibility(mAccount - needPay<=0?View.GONE:View.VISIBLE);

        if(selectMet.campusDiscountSignPrice < selectMet.signPrice||(selectDis!=null&&selectDis.giveNum>0)){
            if(selectMet.campusDiscountSignPrice < selectMet.signPrice) {//有折扣

                if(selectMet.campusDiscountPercent>=1&&selectDis==null){
                    mMessage.setRightText(String.format("%.1f",(float)selectMet.campusDiscountPercent / 10) +"折");

                }else {
                    mMessage.setRightText((selectMet.campusDiscountPercent==100?"0":selectMet.campusDiscountPercent/10+"折")+"; 购买"+number+"节课"+(selectDis==null?"":",赠送"+selectDis.giveNum+"节"));
                }
            }else{
                //无折扣但是有满赠的
                mMessage.setRightText("购买"+number+"节课"+(selectDis==null?"":",赠送"+selectDis.giveNum+"节"));
            }
            mMessage.setVisibility(View.VISIBLE);
        }else{
            mMessage.setVisibility(View.GONE);
        }

        mPrice.setRightText("¥ " + ArithUtils.round(selectMet.signPrice)+"/小时");
        mTimeList.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        mTimeList.setNestedScrollingEnabled(false);
        mTimeList.setAdapter(new SimpleAdapter<Schedule>(this, scheduleList.subList(1, scheduleList.size()), R.layout.item_common_righttext) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Schedule data) {
                holder.setText(R.id.rb_item_righttext, data.week + " " + data.date + " " + data.time);
            }
        });
        if (scheduleList != null && !scheduleList.isEmpty()) {
            Schedule schedule = scheduleList.get(0);
            mSchoolTime.setRightText(schedule.week + " " + schedule.date + " " + schedule.time);
        }
    }

    private void getTeacherTimeType() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        map.put("courseId",courseId);
        //是否学生上门 1是 0否
        map.put("teacherMethod1", selectMet.teachingMethodName.trim().equals("学生上门") ? 1 : 0);
        //是否老师上门 1是 0否
        map.put("teacherMethod2", selectMet.teachingMethodName.trim().equals("老师上门") ? 1 : 0);
        //是否校区上课 1是 0否
        map.put("teacherMethod3", selectMet.teachingMethodName.trim().equals("校区上课") ? 1 : 0);
        httpService.orderAddress(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext,true) {
                    @Override
                    protected void onDone(String str) {
                        try {
                            JSONObject address = new JSONObject(str);
                            String cityName = address.optString("cityName");
                            String areaName = address.optString("areaName");
                            String addressStr = address.optString("address");
                            mAddress.setText((StringUtils.textIsEmpty(cityName)?"":cityName)+(StringUtils.textIsEmpty(areaName)?"":areaName)+(StringUtils.textIsEmpty(addressStr)?"":addressStr));
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

    @Override
    public int setLayout() {
        return R.layout.activity_offlineorder;
    }
    private int number=0;

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_offline_openall, R.id.tv_offline_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_offline_openall:
                mOpenAll.setVisibility(View.GONE);
                mTimeList.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_offline_confirm:
                postCreateCourseOrder();
                break;
        }
    }
    //去支付一对一
    private void postCreateCourseOrder(){

        String classPeriod="";
        List<Long> classTimeArray=new ArrayList<>();
        for(int i=0;i<scheduleList.size();i++){
            String[] mTime=scheduleList.get(i).time.split("-");
            String week = DateUtil.getWeek(scheduleList.get(i).week);
            if(!classPeriod.contains(week)){
                classPeriod+=week + ",";
            }
            Long startTime=DateUtil.dateTimeStampLong(scheduleList.get(i).date+" "+mTime[0],"yyyy-MM-dd HH:mm");
            classTimeArray.add(startTime);
        }
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        //常规
        map.put("courseId", courseId);
        map.put("courseVersion", courseOneInfo.getCourseVersion());
        if(selectMet.campusDiscountId!=0){
            map.put("courseDiscountVersion",selectMet.campusDiscountVersion);
            map.put("courseDiscountId",selectMet.campusDiscountId);
        }
        map.put("giveNumber",selectDis==null?0:selectDis.giveNum);
        map.put("buyNumber",number);
        map.put("totalPrice",mAccount);
        map.put("discountAmount",mAccount-needPay);
        map.put("classTime",DateUtil.dateTimeStampLong(scheduleList.get(0).date+" "+scheduleList.get(0).time.split("-")[0],"yyyy-MM-dd HH:mm"));
        map.put("teachingMethod",selectMet.teachingMethod);
        map.put("classPeriod",classPeriod.substring(0,classPeriod.length()-1));
        map.put("classTimeArray",classTimeArray);
        httpService.createCourseOrder(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseOrder>(mContext,true) {
                    @Override
                    protected void onDone(CourseOrder  courseOrder) {
                        ConfirmPay payUtil = new ConfirmPay(courseOrder.getOrderId(),mAccount,courseOneInfo.getCourseId(),needPay);
                        Intent intent = new Intent(mContext, OrderPayActivity.class);
                        intent.putExtra(StringUtils.COURSE_TYPE,1);
                        intent.putExtra(StringUtils.ACTIVITY_DATA,payUtil);
                        startActivity(intent);
                    }
                    @Override
                    public void  onResultError(ResultException ex){

                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduleList!=null)
            scheduleList.clear();
    }
}
