package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;
import com.main.disanxuelib.bean.VideoType;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangparent.bean.LiveBean;
import com.shengzhe.disan.xuetangparent.bean.OrderCourse;
import com.shengzhe.disan.xuetangparent.bean.SquadRecommendInformation;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.bean.VideoBean;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.bean.CourseItem;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadScheduleBean;
import com.shengzhe.disan.xuetangparent.bean.LiveAndVideo;
import com.shengzhe.disan.xuetangparent.bean.LiveInfo;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.bean.VideoDetails;
import com.shengzhe.disan.xuetangparent.bean.MyCourseInfo;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 * <p>
 * 课程业务处理
 */

public class CourseModelImpl extends BaseModelImpl {

    public CourseModelImpl(Context context, MVPRequestListener listener,String from) {
        super(context, listener,from);
    }

    /****
     * 获取直播课课次列表
     * @param courseId
     */
    public void getOnliveCycleList(int courseId) {
        final Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        getHttpService().courseItem(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseItem>(getContext(), true) {
                    @Override
                    protected void onDone(CourseItem strData) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OnliveCycleList, strData,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OnliveCycleList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OnliveCycleList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 视频课类型列表
     */
    public void getVideoTypeList() {
        getHttpService().videoType(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<VideoType>>(getContext(), true) {
                    @Override
                    protected void onDone(List<VideoType> str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_VideoTypeList, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_VideoTypeList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_VideoTypeList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 视频课列表
     * @param cityCode
     * @param pageNum
     * @param videoType
     */
    public void getVideoCourseList(String cityCode, int pageNum, int videoType) {
        Map<String, Object> map = new HashMap<>();
        map.put("cityCode", cityCode);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("videoType", videoType == 0 ? "" : videoType);

        getHttpService().preferredVideo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<VideoBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<VideoBean> preferredVideo) {
                        getListener().onSuccess(IntegerUtil.WEB_API_VideoCourseList, preferredVideo,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_VideoCourseList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_VideoCourseList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 视频课详情
     * @param courseId
     */
    public void getVideoDeatil(int courseId) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        getHttpService().videoInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<VideoDetails>(getContext(), true) {
                    @Override
                    protected void onDone(VideoDetails videoDetails) {
                        getListener().onSuccess(IntegerUtil.WEB_API_VideoDeatil, videoDetails,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_VideoDeatil, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_VideoDeatil, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 直播课列表
     * @param param
     * @param cityCode
     * @param pageNum
     */
    public void getOnLiveList(Map<String, Object> param, String cityCode, int pageNum) {
        param.put(StringUtils.cityCode, cityCode);
        param.put("pageNum", pageNum);
        param.put("pageSize", 15);
        getHttpService().hotLive(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(param))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<LiveBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<LiveBean> listMybanner) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OnLiveList, listMybanner,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OnLiveList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OnLiveList, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 直播课详情
     * @param courseId
     */
    public void getOnLiveDetail(int courseId) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        getHttpService().liveInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<LiveInfo>(getContext(), true) {
                    @Override
                    protected void onDone(LiveInfo strData) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OnLiveDetail, strData,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OnLiveDetail, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OnLiveDetail, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 获取一对一课程详情
     * @param courseId
     */
    public void getCourseOneDetail(int courseId) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        getHttpService().courseOneInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseOneInfo>(getContext(), true) {
                    @Override
                    protected void onDone(CourseOneInfo strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseOneDetail, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseOneDetail, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseOneDetail, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 一对一课程授课方式信息
     * @param courseId
     */
    public void getTeachingMethod(int courseId) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        getHttpService().teachingMethod(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<TeachingMethod>(getContext(), true) {
                    @Override
                    protected void onDone(TeachingMethod strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_TeachingMethod, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_TeachingMethod, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_TeachingMethod, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 首页申请试听
     * @param cityCode
     * @param gradeId
     * @param subjectId
     */
    public void applyTryListen(String cityCode,int gradeId,int subjectId) {
        Map<String, Object> map = getParameterMap();
        map.put("city", cityCode);
        map.put("grade", gradeId);
        map.put("subjectId", subjectId);
        getHttpService().homeApplyCourseListen(ConstantUrl.ApiVerCode2,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String mesg) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ApplyTryListen, mesg,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ApplyTryListen, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ApplyTryListen, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 申请一对一试听
     */
    public void applyOneListen(int courseId) {
        Map<String, Object> map = getParameterMap();
        map.put("courseId", courseId);
        getHttpService().applyCourseListen(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String baseHttpResult) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ApplyOneListen, baseHttpResult,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ApplyOneListen, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ApplyTryListen, ex.getMessage(),getFrom());
                        }
                    }
                });


    }

    /****
     * 查询热门直播课和视频课
     * @param cityCode
     */
    public void getLiveAndVideo(String cityCode) {
        Map<String, Object> map = getParameterMap();
        map.put(StringUtils.cityCode, cityCode);
        getHttpService().findLiveAndVideo(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<LiveAndVideo>(getContext(), true) {
                    @Override
                    protected void onDone(LiveAndVideo mListMybanner) {
                        getListener().onSuccess(IntegerUtil.WEB_API_LiveAndVideo, mListMybanner,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_LiveAndVideo, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_LiveAndVideo, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 获取线下班课
     * @param param
     * @param cityCode
     * @param pageNum
     */
    public void getSquadCourseList(Map<String, Object> param,String cityCode,int pageNum) {
        param.put("cityCode", cityCode);
        param.put("pageNum", pageNum);
        param.put("pageSize", 15);
        getHttpService().squadRecommend(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(param))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<SquadRecommendInformation>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<SquadRecommendInformation> teacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SquadCourseList, teacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SquadCourseList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SquadCourseList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 老师开课集合
     * @param teacherId
     * @param pageNum
     * @param isLoad
     */
    public void getCourseStartList(int teacherId,int pageNum,boolean isLoad) {
        Map<String, Object> map = getParameterMap();
        map.put("courseType", 1);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("teacherId", teacherId);
        getHttpService().teacherCourseArray(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<CourseOflineBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<CourseOflineBean> mCourseStartList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseStartList, mCourseStartList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseStartList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseStartList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 获取课程列表
     * @param pageNum
     * @param courseType
     */
    public void getCourseList(int courseType,int teacherId,int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("courseType", courseType);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("teacherId", teacherId);
        getHttpService().courseLiveStartList(ConstantUrl.ApiVerCode2,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<CourseLiveBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<CourseLiveBean> mCourseStartList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseList, mCourseStartList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 获取我的课程
     * @param classStatus
     * @param pageNum
     */
    public void getMyCourseList(int classStatus,int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("classStatus", classStatus);
        getHttpService().myCourse(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<OrderCourse>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<OrderCourse> myCourse) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyCourseList, myCourse,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyCourseList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyCourseList, ex.getMessage(),getFrom());
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
        getHttpService().courseSquadInfo(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseSquadBean>(getContext(), true) {
                    @Override
                    protected void onDone(CourseSquadBean squadBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseSquadDetail, squadBean,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseSquadDetail, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseSquadDetail, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 线下班课大纲列表
     * @param courseId
     */
    public void getSquadOutlineList(int courseId) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        httpService.courseSquadSchedule(ConstantUrl.ApiVerCode3,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<CourseSquadScheduleBean>>(getContext(), true) {
                    @Override
                    protected void onDone(List<CourseSquadScheduleBean> beenList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SquadOutlineList, beenList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SquadOutlineList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SquadOutlineList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 获取科目找老师
     * @param param
     * @param cityCode
     * @param pageNum
     * @param subjectId
     */
    public void getTeacherSubject(Map<String, Object> param,String cityCode,int pageNum,int subjectId) {
        param.put("cityCode", cityCode);
        param.put("pageNum", pageNum);
        param.put("pageSize", 15);
        param.put("subjectId",subjectId);
        getHttpService().teacherSubject(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(param))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<TeacherInformation>>(getContext(),true) {
                    @Override
                    protected void onDone(BasePageBean<TeacherInformation> teacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_TeacherSubject, teacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_TeacherSubject, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_TeacherSubject, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 我的课程详情
     * @param orderId
     */
    public void getMyCourseDetail(int orderId){
        Map<String, Object> map = getParameterMap();
        map.put("orderId",  orderId);
        getHttpService().myCourseInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyCourseInfo>(getContext(),true) {
                    @Override
                    protected void onDone(MyCourseInfo strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyCourseDetail, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyCourseDetail, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyCourseDetail, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 获取线下一对一老师
     * @param selectCityCode
     * @param pageNum
     */
    public void getRecommendTeacher(String selectCityCode,int pageNum) {
        Map<String, Object> map = new HashMap<>();
        map.put(StringUtils.cityCode, selectCityCode);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        getHttpService().recommendTeacher(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<TeacherInformation>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<TeacherInformation> teacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_RecommendTeacher, teacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_RecommendTeacher, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_RecommendTeacher, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

}
