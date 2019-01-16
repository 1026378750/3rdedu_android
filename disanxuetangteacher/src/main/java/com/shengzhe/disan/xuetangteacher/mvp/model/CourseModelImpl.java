package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;
import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.bean.BuyerBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailsBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseItemBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangteacher.bean.courseSquadScheduleBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 *
 * 课程业务处理
 *
 */

public class CourseModelImpl extends BaseModelImpl {

    public CourseModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /******
     * 在线直播课开课
     * @param parameter
     */
    private void saveOnLiveCourse(List<MultipartBody.Part> parameter){
        getHttpService().addAndModifyDirect(parameter)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SaveOnLiveCourse, str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SaveOnLiveCourse, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SaveOnLiveCourse, ex.getMessage());
                        }
                    }
                });
    }

    /****
     * 线下一对一开课
     * @param map
     */
    public void saveOneToOneCourse(Map<String, Object> map) {
        getHttpService().addAndModifyOne(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SaveOneToOneCourse, str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SaveOneToOneCourse, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SaveOneToOneCourse, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 添加线下班课数据
     * @param builder
     */
    public void saveOfflineClassCourse(MultipartBody.Builder builder) {
        getHttpService().saveCourseSquad(builder.build().parts())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SaveOfflineClassCourse, str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SaveOfflineClassCourse, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SaveOfflineClassCourse, ex.getMessage());
                        }
                    }
                });
    }

    /****
     * 获取直播课课次列表
     * @param courseId
     */
    public void getOnliveCycleList(int courseId) {
        final Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        getHttpService().courseItem(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<CourseItemBean>>(getContext(), false) {
                    @Override
                    protected void onDone(List<CourseItemBean> courseItemBeanList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OnliveCycleList, courseItemBeanList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OnliveCycleList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OnliveCycleList, ex.getMessage());
                        }
                    }
                });
    }

    /******
     * 获取课程列表
     * @param pageNum
     * @param courseType
     */
    public void getCourseList(int pageNum,int courseType){
        Map<String, Object> map = getParameterMap();
        map.put("courseType", 4);
        map.put("pageNum", pageNum);
        map.put("pageSize",15);
        ConstantUrl.CLIEN_Info=2;
        getHttpService().courseLiveStartList(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<CourseLiveBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<CourseLiveBean>  mCourseStartList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseList, mCourseStartList);
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseList, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 获取课程详情
     * @param courseId
     * @param courseType
     */
    public void getCourseDetail(int courseId,int courseType) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("courseType", courseType);
        httpService.courseInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseDetailsBean>(getContext(), true) {
                    @Override
                    protected void onDone(CourseDetailsBean mCourse) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseDetail, mCourse);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseDetail, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseDetail, ex.getMessage());
                        }
                    }
                });
    }

    /******
     * 删除课程
     * @param courseId
     * @param courseType
     */
    private void removeCourse(int courseId,int courseType) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        map.put("courseType", courseType);
        getHttpService().removeCourse(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String mCourse) {
                        getListener().onSuccess(IntegerUtil.WEB_API_RemoveCourse, mCourse);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_RemoveCourse, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_RemoveCourse, ex.getMessage());
                        }
                    }
                });
    }

    /******
     * 获取下线班课详情
     * @param courseId
     */
    public void getCourseSquadDetail(int courseId) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        ConstantUrl.CLIEN_Info = 2;
        getHttpService().courseSquadInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseSquadBean>(getContext(), true) {
                    @Override
                    protected void onDone(CourseSquadBean squadBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseSquadDetail, squadBean);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseSquadDetail, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseSquadDetail, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 线下班课学生课表
     * @param courseId
     * @param pageNum
     */
    private void getSquadStudentList(int courseId,int pageNum) {
        Map<String,Object> map= getParameterMap();
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        getHttpService().courseSquadStudentSchedule(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BuyerBean>>(getContext(),true) {
                    @Override
                    protected void onDone(List<BuyerBean> beanList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SquadStudentList, beanList);
                    }
                    @Override
                    public void  onResultError(ResultException ex){
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SquadStudentList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SquadStudentList, ex.getMessage());
                        }
                    }
                });

    }

    /******
     * 线下班课大纲列表
     * @param courseId
     */
    private void getSquadOutlineList(int courseId) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId",  courseId);
        ConstantUrl.CLIEN_Info=2;
        httpService.courseSquadSchedule(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<courseSquadScheduleBean>>(getContext(), true) {
                    @Override
                    protected void onDone(List<courseSquadScheduleBean>  beenList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SquadOutlineList, beenList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SquadOutlineList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SquadOutlineList, ex.getMessage());
                        }
                    }
                });
    }

}
