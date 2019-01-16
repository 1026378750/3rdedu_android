package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;
import com.main.disanxuelib.bean.CourseDate;
import com.main.disanxuelib.util.DateUtil;
import com.shengzhe.disan.xuetangparent.bean.CourseBean;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.MySchedule;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 课程网络请求 on 2017/11/27.
 */

public class ScheduleModelImpl extends BaseModelImpl{

    public ScheduleModelImpl(Context context, MVPRequestListener listener,String from) {
        super(context, listener,from);
    }

    /**
     * 查询月份月份标识
     */
    public void getMonthStatus(boolean isExchage , String timeStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("courseTime", isExchage ? DateUtil.getThisFirstDay(timeStr, "yyyy-MM-dd HH:mm") : DateUtil.dateTimeStamp(timeStr, "yyyy-MM-dd HH:mm"));//搜索开始时间
        map.put("type", 1);//时间类型 1:月份 2:周
        getHttpService().monthStatus(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<String>>(getContext(), true) {
                    @Override
                    protected void onDone(List<String> timeList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MonthStatus,timeList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MonthStatus, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MonthStatus, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /**
     * 查询有课信息
     */
    public void getMySchedule(String timeStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("courseTime", DateUtil.dateTimeStamp(timeStr, "yyyy-MM-dd HH:mm"));
        getHttpService().mySchedule(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<MySchedule>>(getContext(), true) {
                    @Override
                    protected void onDone(List<MySchedule> strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MySchedule,strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MySchedule, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MySchedule, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 用户签到
     * @param courseBean
     */
    public void saveScheduleSignStatus(final CourseBean courseBean){
        Map<String, Object> map = getParameterMap();
        map.put("orderItemId", courseBean.getId());
        getHttpService().myScheduleSign(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ScheduleSignStatus,courseBean,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ScheduleSignStatus, courseBean,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ScheduleSignStatus, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 获取具体日期老师上课状态
     * @param courseId
     * @param seleteDate
     */
    public void getTeacherTimeType(int courseId,long seleteDate) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        map.put("seleteDate", seleteDate);
        getHttpService().teacherTimeType(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<Integer>>(getContext(),true) {
                    @Override
                    protected void onDone(List<Integer> dataList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_TeacherTimeType,dataList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_TeacherTimeType, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_TeacherTimeType, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 获取老师周几上课
     * @param courseId
     */
    public void getCanTimer(int courseId){
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        httpService.selectCourseDate(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<CourseDate>>(getContext(),true) {
                    @Override
                    protected void onDone(List<CourseDate> dataList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CanTimer,dataList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CanTimer, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CanTimer, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

}
