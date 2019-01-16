package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;
import com.main.disanxuelib.util.DateUtil;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.bean.CourseBean;
import com.shengzhe.disan.xuetangteacher.bean.MySchedule;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ScheduleModelImpl extends BaseModelImpl{

    public ScheduleModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /**
     * 查询月份月份标识
     */
    public void getMonthStatus(boolean isExchage , String timeStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("courseTime", isExchage ? DateUtil.getThisFirstDay(timeStr, "yyyy-MM-dd HH:mm") : DateUtil.dateTimeStamp(timeStr, "yyyy-MM-dd HH:mm"));//搜索开始时间
        map.put("type", 1);//时间类型 1:月份 2:周
        getHttpService().monthStatus(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<String>>(getContext(), true) {
                    @Override
                    protected void onDone(List<String> timeList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MonthStatus,timeList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_MonthStatus,ex.getMessage());
                    }
                });
    }

    /**
     * 查询有课信息
     */
    public void getMySchedule(String timeStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("courseTime", DateUtil.dateTimeStamp(timeStr, "yyyy-MM-dd HH:mm"));
        getHttpService().mySchedule(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<MySchedule>>(getContext(), true) {
                    @Override
                    protected void onDone(List<MySchedule> strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MySchedule,strTeacher);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_MySchedule,ex.getMessage());
                    }
                });
    }

    /****
     * 更改状态
     * @param data
     */
    public void modifyCourseStatus(final CourseBean data) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderItemId", data.getId());
        getHttpService().teacherCheck(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ModifyCourseStatus,data);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                            getListener().onSuccess(IntegerUtil.WEB_API_ModifyCourseStatus,data);
                        }else {
                            getListener().onFailed(IntegerUtil.WEB_API_ModifyCourseStatus,ex.getMessage());
                        }
                    }

                });
    }

}
