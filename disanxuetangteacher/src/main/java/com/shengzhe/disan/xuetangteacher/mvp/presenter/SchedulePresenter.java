package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.ldf.calendar.utils.CalendarDate;
import com.ldf.calendar.view.CalendarView;
import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.bean.CourseBean;
import com.shengzhe.disan.xuetangteacher.bean.MySchedule;
import com.shengzhe.disan.xuetangteacher.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangteacher.mvp.model.ScheduleModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.view.IScheduleView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */

public class SchedulePresenter extends BasePresenter implements MVPRequestListener,MonthPager.OnPageListener {
    private IScheduleView view;
    private ArrayList<CalendarView> currentCalendars = new ArrayList<>();
    private CalendarDate currentDate;
    private List<CourseBean> mySchedulesList = new ArrayList<>();
    private List<String> longArrayList = new ArrayList<>();//保存有日期时间类型
    private List<String> timeMarkList = new ArrayList<>();
    private String timeStr = "";
    private SimpleAdapter adapter;
    private ScheduleModelImpl modelImpl;

    public SchedulePresenter(Context context, IScheduleView view) {
        super(context);
        this.view = view;
        if (modelImpl == null)
            modelImpl = new ScheduleModelImpl(context, this);
    }

    public void initDate() {
        isExchage = true;
        longArrayList.clear();
        timeMarkList.clear();
        view.getRvMineClass().removeAllViews();
        view.getRvMineClass().setHasFixedSize(true);
        currentDate = new CalendarDate();
        view.getTvMineCourseDate().setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        timeStr = DateUtil.getDefaultDate(null);
        view.getCalendarView().setOnPageListener(this);
        view.getCalendarView().setIsMineSchedule(true);
        view.getCalendarView().setPagerAdapter();
        initMineClass();
    }

    public void notifyMarkDataAsyn() {
        view.getCalendarView().setDefaultData("");
        view.getCalendarView().setDisable(false);
        view.getCalendarView().setIsMineSchedule(false);
        view.getCalendarView().setWeekAble("",null);
        view.getCalendarView().notifyMarkDataAsyn(timeMarkList);
    }

    @Override
    public void onCurrentPage(ArrayList<CalendarView> calendarList,CalendarDate date) {
        currentDate = date;
        view.getTvMineCourseDate().setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        timeStr = date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " 00:00";
        isExchage = false;
        getMonthStatus();
    }

    @Override
    public void onCheckedDate(CalendarDate date) {
        currentDate = date;
        view.getTvMineCourseDate().setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        //下方列表中的周时间内
        String time = date.getYear() + "-" + (date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()) + "-" + (date.getDay() < 10 ? "0" + date.getDay() : date.getDay());
        if (longArrayList.contains(time)) {
            view.getRvMineClass().scrollToPosition(longArrayList.indexOf(time));
            return;
        }
        try {
            timeStr = DateUtil.getDefaultDate(new SimpleDateFormat("yyyy-MM-dd").parse(time));
            getMySchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询月份月份标识
     */
    private boolean isExchage = true;

    /**
     * 初始化课程信息
     */
    private void initMineClass() {
        view.getRvMineClass().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<CourseBean>(mContext, mySchedulesList, R.layout.fragment_schedule_item) {
            @Override
            protected void onBindViewHolder(final TrdViewHolder mHholder, final CourseBean data) {
                mHholder.setText(R.id.tv_title_time, data.getTimeDay())
                        .setVisible(R.id.tv_title_time, data.isFirstItem())
                        .setVisible(R.id.tv_subjct_class_line, !data.isFirstItem())
                        .setVisible(R.id.tv_subjct_class_button, mySchedulesList.indexOf(data) == mySchedulesList.size() - 1);
                if (data.getId() == -1) {
                    mHholder.setVisible(R.id.tv_nothing, true)
                            .setVisible(R.id.tv_subjct_class_childlyout, false);
                    return;
                }

                mHholder.setVisible(R.id.tv_nothing, false)
                        .setVisible(R.id.tv_subjct_class_childlyout, true)
                        .setText(R.id.tv_class_after_time, "已上" + data.getOrderNum() + "次，剩余" + ((data.getClassNum())-data.getOrderNum()) + "次")
                        .setText(R.id.tv_subjct_class_type, StringUtils.textStr(data.getCourseTypeName()))
                        .setText(R.id.tv_subject_status, "     " + StringUtils.textStr(data.getTeachingMethodName()))
                        .setText(R.id.tv_hot_name, StringUtils.textStr(data.getCourseName())+"")
                        .setText(R.id.tv_pay_type, data.getCourseTime())
                        .setVisible(R.id.iv_video_photo, (data.getCourseType() == 2||data.getCourseType() == 4));
                if (data.getCourseType() == 1) {
                    if (TextUtils.isEmpty(data.getStudentName())) {
                        mHholder.setText(R.id.tv_hot_clazz, "学生姓名：--");
                    } else {
                        mHholder.setText(R.id.tv_hot_clazz, "学生姓名：" + data.getStudentName());
                    }
                    if (StringUtils.textStr(data.getTeachingMethodName()).equals("校区上课")) {
                        mHholder.setText(R.id.tv_hot_time, "校区地址：" + StringUtils.textStr(data.getAddress()));
                    } else {
                        mHholder.setText(R.id.tv_hot_time, "家长手机：" + StringUtils.textStr(data.getUserPhone()));
                    }
                } else if (data.getCourseType() == 2){
                    mHholder.setText(R.id.tv_hot_clazz, "学生数：" + StringUtils.textStr(data.getStudentNum() + ""));//?
                    mHholder.setText(R.id.tv_hot_time, "参加码：" + StringUtils.textStr(data.getTeacherCode() + ""));
                }else if(data.getCourseType() == 4){
                    mHholder .setText(R.id.tv_subject_status, "");
                    mHholder .setText(R.id.tv_subjct_class_type, StringUtils.textStr(data.getCourseTypeName()));
                    mHholder .setText(R.id.tv_hot_clazz, "大纲："+StringUtils.textStr(data.getCourseTitle()));
                 //   mHholder .setText(R.id.tv_hot_time,  "地址："+StringUtils.textStr(data.getAddress()));
                    mHholder.setVisible(R.id.tv_mine_class_status, false);
                }
                if (data.getCourseType() == 1) {
                    //1对1的课程
                    //课次状态  1待授课 2已授课 3待审核 4已完成 5已退款
                    if (data.getStatus() == 1) {
                        mHholder.setText(R.id.tv_mine_class_status, "考勤");
                        mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.mipmap.btn_schedule_checkingin);
                    } else if (data.getStatus() == 2) {
                        mHholder.setText(R.id.tv_mine_class_status, "已授课");
                        mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.mipmap.btn_schedule_checked);
                    } else if (data.getStatus() == 3) {
                        mHholder.setText(R.id.tv_mine_class_status, "审核中");
                        mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.mipmap.btn_schedule_checked);
                    } else if (data.getStatus() == 4) {
                        mHholder.setText(R.id.tv_mine_class_status, "已完成");
                        mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.mipmap.btn_schedule_checked);
                    }
                } else if(data.getCourseType() == 2) {
                    //在线直播课
                    mHholder.setText(R.id.tv_mine_class_status, data.getStatus() == 1 ? "进入" : "回放");
                    mHholder.setBackgroundRes(R.id.tv_mine_class_status, R.mipmap.btn_schedule_enter);
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), mHholder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.loading_figure, R.mipmap.loading_figure);
                }else if(data.getCourseType() == 4){
                         mHholder.setText(R.id.tv_hot_clazz, "大纲：" + StringUtils.textStr(data.getCourseTitle() + ""));//?
                          mHholder.setText(R.id.tv_hot_time,TextUtils.isEmpty(data.getAddress())==true?"地址：--": "地址：" +data.getAddress());
                        //mHholder.setText(R.id.tv_hot_mannum, StringUtils.textFormatHtml("<font color='#1d97ea'>"+data.maxUser+"</font>"));
                    mHholder.setText(R.id.tv_hot_mannum, StringUtils.textFormatHtml("<font color='#1d97ea'>" + StringUtils.textStr(data.getStudentNum() + "</font>" + "人")));
                    mHholder.setVisible(R.id.tv_hot_mannum,true);
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), mHholder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.loading_figure, R.mipmap.loading_figure);
                }
                if(data.getOrderItemCode().equals("null")||data.getOrderItemCode().equals("")){
                    mHholder.setText(R.id.tv_class_time, "第" + 0 + "次");
                }else {
                    mHholder.setText(R.id.tv_class_time, "第" + data.getOrderItemCode() + "次");
                }
                //查询3参与人数
                mHholder.setOnClickListener(R.id.tv_hot_mannum, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenerMan.presenterManClick(v, data);
                    }
                });
                //做考勤操作，并且弹框显示
                mHholder.setOnClickListener(R.id.tv_mine_class_status, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.presenterClick(v, data);
                    }
                });
                mHholder.setOnClickListener(R.id.tv_subjct_class_childlyout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenerItem.presenterItemClick(v, data);
                    }
                });

            }
        };
        view.getRvMineClass().setAdapter(adapter);
    }

    public void getMonthStatus() {
        modelImpl.getMonthStatus(isExchage, timeStr);
    }

    public void getMySchedule() {
        modelImpl.getMySchedule(timeStr);
    }

    public void modifyCourseStatus(CourseBean data) {
        modelImpl.modifyCourseStatus(data);
    }

    public void claerAllDate() {
        mySchedulesList.clear();
        timeMarkList.clear();
        currentCalendars.clear();
        isExchage = true;
    }

    @Override
    public void onSuccess(int tager, Object objects) {
        switch (tager) {
            case IntegerUtil.WEB_API_MonthStatus:
                timeMarkList.clear();
                timeMarkList.addAll(DateUtil.formatList((List<String>) objects, "yyyy-MM-dd"));
                view.getCalendarView().notifyMarkDataAsyn(timeMarkList);
                break;

            case IntegerUtil.WEB_API_MySchedule:
                mySchedulesList.clear();
                longArrayList.clear();
                for (MySchedule mySchedule : (List<MySchedule>) objects) {
                    String ayStr = DateUtil.timeStamp(mySchedule.getTimeDay(), "yyyy-MM-dd");
                    longArrayList.add(ayStr);
                    List<CourseBean> childList = mySchedule.getCourseList();
                    String timeStr = ayStr + " " + DateUtil.weekTimeStamp(mySchedule.getTimeDay(), "yyyy-MM-dd");
                    if (childList == null || childList.isEmpty()) {
                        CourseBean bean = new CourseBean();
                        bean.setId(-1);
                        bean.setTimeDay(timeStr);
                        bean.setFirstItem(true);
                        mySchedulesList.add(bean);
                        continue;
                    }
                    for (int i = 0; i < childList.size(); i++) {
                        CourseBean child = childList.get(i);
                        child.setTimeDay(timeStr);
                        child.setFirstItem(i == 0);
                        mySchedulesList.add(child);
                    }
                }
                adapter.notifyDataSetChanged();
                break;

            case IntegerUtil.WEB_API_ModifyCourseStatus:
                CourseBean data = (CourseBean) objects;
                data.setStatus(2);
                adapter.notifyItemChanged(mySchedulesList.indexOf(data));
                ToastUtil.showToast("考勤成功");
                break;
        }

    }

    @Override
    public void onFailed(int tager, String mesg) {
       // ToastUtil.showShort(mesg);
    }

}
