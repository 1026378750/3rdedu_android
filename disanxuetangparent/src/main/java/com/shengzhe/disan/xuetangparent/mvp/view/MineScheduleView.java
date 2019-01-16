package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.view.CalendarView;
import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.CourseBean;
import com.shengzhe.disan.xuetangparent.bean.MySchedule;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/2.
 */

public class MineScheduleView extends BaseView implements MonthPager.OnPageListener {
    private SimpleAdapter adapter;
    private ArrayList<CalendarView> currentCalendars = new ArrayList<>();
    private CalendarDate currentDate;
    private List<CourseBean> mySchedules = new ArrayList<>();
    private List<String> longArrayList = new ArrayList<>();//保存有日期时间类型
    private List<String> timeMark = new ArrayList<>();
    private String timeStr = "";
    private TrdViewHolder checkedHholder;
    /**
     * 查询月份月份标识
     */
    private boolean isExchage = true;

    public MineScheduleView(Context context) {
        super(context);
        isExchage = true;
        longArrayList.clear();
        timeMark.clear();
    }

    private IMineScheduleView iView;

    public void setIMineScheduleView(IMineScheduleView iView) {
        this.iView = iView;
    }

    public void initDatas(final FragmentManager childFragmentManager) {
        iView.getMineClassView().setHasFixedSize(true);
        iView.getCalendarView().setOnPageListener(this);
        iView.getCalendarView().setIsMineSchedule(true);
        iView.getCalendarView().setPagerAdapter();
        currentDate = new CalendarDate();
        iView.getMineCourseDateView().setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        timeStr = DateUtil.getDefaultDate(null);
        iView.getMineClassView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<CourseBean>(mContext, mySchedules, R.layout.fragment_schedule_item) {
            @Override
            protected void onBindViewHolder(final TrdViewHolder mHholder, final CourseBean data) {
                mHholder.setText(R.id.tv_title_time, data.getTimeDay())
                        .setVisible(R.id.tv_title_time, data.isFirstItem())
                        .setVisible(R.id.tv_subjct_class_line, !data.isFirstItem())
                        .setVisible(R.id.tv_subjct_class_button, mySchedules.indexOf(data) == mySchedules.size() - 1);

                if (data.getId() == -1) {
                    mHholder.setVisible(R.id.tv_nothing, true)
                            .setVisible(R.id.tv_subjct_class_childlyout, false);
                    return;
                }
                mHholder.setVisible(R.id.tv_nothing, false)
                        .setVisible(R.id.tv_subjct_class_childlyout, true)
                        .setText(R.id.tv_subjct_class_type, StringUtils.textStr(data.getCourseTypeName()))
                        .setText(R.id.tv_subject_status, "· " + StringUtils.textStr(data.getTeachingMethodName()))
                        .setText(R.id.tv_hot_name, StringUtils.textStr(data.getCourseName()))
                        .setText(R.id.tv_hot_clazz, StringUtils.textStr(data.getSellerName()))
                        .setText(R.id.tv_hot_time, StringUtils.textStr(data.getAddress()))
                        .setText(R.id.tv_pay_type, data.getCourseTime())
                        // .setText(R.id.tv_class_time, "第" + data.getOrderItemCode() + "次")
                        .setText(R.id.tv_class_after_time, "已上" + data.getOrderNum() + "次，剩余" + ((data.getClassNum()) - data.getOrderNum()) + "次")
                        .setVisible(R.id.iv_video_photo, data.getCourseType() != 1)
                        .setVisible(R.id.iv_hot_photo, data.getCourseType() == 1);
              /*  mHholder.setOnClickListener(R.id.tv_subjct_class_childlyout, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(data.getCourseType()==4){
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, CourseDetailActivity.class);
                                    intent.putExtra(StringUtils.COURSE_ID,data.getId());
                                    intent.putExtra(StringUtils.FRAGMENT_DATA,true);
                                    startActivity(intent);
                                }
                            }
                        });*/
                //课次状态 1待授课 2已授课 3已完成  待授课
                if (data.getCourseType() == 1) {
                    mHholder.setVisible(R.id.tv_mine_class_status, true);
                    if (data.getStatus() == 2) {
                        mHholder.setText(R.id.tv_mine_class_status, "签到");
                        mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.drawable.btn_bg_default_ok);
                    } else if (data.getStatus() == 3 || data.getStatus() == 4) {
                        mHholder.setText(R.id.tv_mine_class_status, "已签到");
                        mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.drawable.btn_item_selector);
                    } else if (data.getStatus() == 1) {
                        mHholder.setVisible(R.id.tv_mine_class_status, false);
                    }
                } else if (data.getCourseType() == 4) {
                    mHholder.setText(R.id.tv_subject_status, "");
                    mHholder.setText(R.id.tv_subjct_class_type, "线下班课");
                    mHholder.setText(R.id.tv_hot_clazz, "大纲：" + StringUtils.textStr(data.getCourseTitle()));
                    if (TextUtils.isEmpty(data.getAddress())) {
                        mHholder.setText(R.id.tv_hot_time, "地址:--");
                    } else {
                        mHholder.setText(R.id.tv_hot_time, "地址:" + StringUtils.textStr(data.getAddress()));
                    }
                    mHholder.setVisible(R.id.tv_mine_class_status, false);
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), mHholder.<ImageView>getView(R.id.iv_hot_photo), R.mipmap.loading_figure, R.mipmap.loading_figure);
                } else if (data.getCourseType() == 2) {
                    mHholder.setText(R.id.tv_hot_clazz, "参加码: " + data.getParCode());
                    mHholder.setText(R.id.tv_hot_time, data.getSellerName());
                    mHholder.setVisible(R.id.tv_mine_class_status, true);
                    mHholder.setText(R.id.tv_mine_class_status, "进入");
                    mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.drawable.btn_bg_default_ok);
                }
                if (data.getOrderItemCode().equals("null") || data.getOrderItemCode().equals("")) {
                    mHholder.setText(R.id.tv_class_time, "第" + 0 + "次");
                } else {
                    mHholder.setText(R.id.tv_class_time, "第" + data.getOrderItemCode() + "次");
                }
                if (data.getCourseType() == 1)//线下一对一
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), mHholder.<ImageView>getView(R.id.iv_hot_photo), R.mipmap.loading_figure, R.mipmap.loading_figure);
                else//视频课、直播课
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), mHholder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
                //做签到操作，并且弹框显示
                mHholder.setOnClickListener(R.id.tv_mine_class_status, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getCourseType() == 1 && data.getStatus() == 2) {
                            checkedHholder = mHholder;
                            apiListener.loadWebApi(IntegerUtil.WEB_API_ScheduleSignStatus, data);
                        } else if (data.getCourseType() == 2) {
                            //进入
                            ConfirmDialog dialog = ConfirmDialog.newInstance("", "暂未开通手机观看直播课，请电脑登陆官网www.3rdedu.com" +
                                    "<font color='#ffae12'>“家长个人中心-我的课表”</font>观看", "", "确定");
                            dialog.setMargin(60)
                                    .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                                    .setOutCancel(false)
                                    .show(childFragmentManager);
                        }
                    }
                });
            }
        };
        iView.getMineClassView().setAdapter(adapter);
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setResultDatas(List<MySchedule> strTeacher) {
        mySchedules.clear();
        longArrayList.clear();
        for (MySchedule mySchedule : strTeacher) {
            String ayStr = DateUtil.timeStamp(mySchedule.getTimeDay(), "yyyy-MM-dd");
            longArrayList.add(ayStr);
            List<CourseBean> childList = mySchedule.getCourseList();
            String timeStr = ayStr + " " + DateUtil.weekTimeStamp(mySchedule.getTimeDay(), "yyyy-MM-dd");
            if (childList == null || childList.isEmpty()) {
                CourseBean bean = new CourseBean();
                bean.setId(-1);
                bean.setTimeDay(timeStr);
                bean.setFirstItem(true);
                mySchedules.add(bean);
                continue;
            }
            for (int i = 0; i < childList.size(); i++) {
                CourseBean child = childList.get(i);
                child.setTimeDay(timeStr);
                child.setFirstItem(i == 0);
                mySchedules.add(child);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public boolean getExchange() {
        return isExchage;
    }

    public void setMonthStatus(List<String> monthStatus) {
        timeMark.clear();
        timeMark.addAll(DateUtil.formatList(monthStatus, "yyyy-MM-dd"));
        iView.getCalendarView().notifyMarkDataAsyn(timeMark);
    }

    public void clearAllDatas() {
        mySchedules.clear();
        timeMark.clear();
        currentCalendars.clear();
        iView.getCalendarView().removeAllViews();
        isExchage = true;
    }

    @Override
    public void onCurrentPage(ArrayList<CalendarView> calendarList, CalendarDate date) {
        isExchage = false;
        currentDate = date;
        iView.getMineCourseDateView().setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        timeStr = date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " 00:00";
        apiListener.loadWebApi(IntegerUtil.WEB_API_MonthStatus, null);
    }

    @Override
    public void onCheckedDate(CalendarDate date) {
        currentDate = date;
        iView.getMineCourseDateView().setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        //下方列表中的周时间内
        String time = date.getYear() + "-" + (date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()) + "-" + (date.getDay() < 10 ? "0" + date.getDay() : date.getDay());
        if (longArrayList.contains(time)) {
            iView.getMineClassView().scrollToPosition(longArrayList.indexOf(time));
            return;
        }
        try {
            timeStr = DateUtil.getDefaultDate(new SimpleDateFormat("yyyy-MM-dd").parse(time));
            apiListener.loadWebApi(IntegerUtil.WEB_API_MySchedule, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSignDatas(CourseBean courseBean) {
        if(checkedHholder==null)
            return;
        checkedHholder.setText(R.id.tv_mine_class_status, "已签到")
                .setBackgroundRes(R.id.tv_mine_class_status, R.drawable.btn_item_selector)
                .setTextColor(R.id.tv_mine_class_status, UiUtils.getColor(R.color.color_ffffff));
        //adapter.notifyItemChanged(mySchedules.indexOf(courseBean));
    }

    public interface IMineScheduleView {
        RecyclerView getMineClassView();

        TextView getMineCourseDateView();

        MonthPager getCalendarView();
    }

}
