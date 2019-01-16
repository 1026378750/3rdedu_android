package com.shengzhe.disan.xuetangparent.http.service;

import com.main.disanxuelib.bean.CourseDate;
import com.main.disanxuelib.bean.HelpBean;
import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.bean.VersionBean;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangparent.bean.LiveBean;
import com.shengzhe.disan.xuetangparent.bean.OrderBean;
import com.shengzhe.disan.xuetangparent.bean.OrderCourse;
import com.shengzhe.disan.xuetangparent.bean.SquadRecommendInformation;
import com.shengzhe.disan.xuetangparent.bean.VideoBean;
import com.shengzhe.disan.xuetangparent.http.UrlHelper;
import com.shengzhe.disan.xuetangparent.bean.CourseItem;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.CourseOrder;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangparent.bean.LiveAndVideo;
import com.shengzhe.disan.xuetangparent.bean.LiveInfo;
import com.shengzhe.disan.xuetangparent.bean.MyBalance;
import com.shengzhe.disan.xuetangparent.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangparent.bean.MySchedule;
import com.shengzhe.disan.xuetangparent.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangparent.bean.OrderOnfflineInfo;
import com.shengzhe.disan.xuetangparent.bean.PayDirectInfo;
import com.shengzhe.disan.xuetangparent.bean.PayVideo;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.bean.TeaHomePageBean;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.shengzhe.disan.xuetangparent.bean.UserAssistant;
import com.shengzhe.disan.xuetangparent.bean.VideoDetails;
import com.main.disanxuelib.bean.VideoType;
import com.main.disanxuelib.bean.Wallet;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadScheduleBean;
import com.shengzhe.disan.xuetangparent.bean.MyCourseInfo;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * service-下载接口
 * Created by hy on 2017/9/18.
 */

public interface HttpService {

    /*****************登录注册开通城市******************/
    //获取手机验证码
    @POST("getMethodCode")
    Observable<String> getMethodCode(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //登录接口
    @POST(UrlHelper.PUBLIC_URL + "sign/login")
    Observable<User> login(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //开通城市sign/login
    @GET("findCityList")
    Observable<String> findCityList(@Header("ApiCode") String ApiCode);
    //阶段和年级集合
    @GET(UrlHelper.BASE_URL + "genGradeListAll")
    Observable<String> genGradeListAll(@Header("ApiCode") String ApiCode);

    /******************在线课堂*****************/
    //视频课分类集合
    @GET("videoType")
    Observable<List<VideoType>> videoType(@Header("ApiCode") String ApiCode);

    //视频课列表
    @POST(UrlHelper.PUBLIC_URL + "video/preferredVideo")
    Observable<BasePageBean<VideoBean>> preferredVideo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //视频课详情
    @POST(UrlHelper.PUBLIC_URL + "video/videoInfo")
    Observable<VideoDetails> videoInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //视频课订单详情
    @POST(UrlHelper.PUBLIC_URL + "video/payVideoInfo")
    Observable<PayVideo> payVideoInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //直播课列表
    @POST(UrlHelper.PUBLIC_URL + "direct/hotLive")
    Observable<BasePageBean<LiveBean>> hotLive(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //直播课详情
    @POST(UrlHelper.PUBLIC_URL + "direct/liveInfo")
    Observable<LiveInfo> liveInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //课程安排
    @POST(UrlHelper.PUBLIC_URL + "direct/courseItem")
    Observable<CourseItem> courseItem(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    /******************老师主页*****************/

    //线下一对一老师主页 2.0
    @POST(UrlHelper.BASE_URL + "teaInfo/teaHomePage")
    Observable<TeaHomePageBean> teaHomePage(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    /******************线下一对一*****************/
    //线下一对一课程详情
    @POST(UrlHelper.PUBLIC_URL + "one/courseOneInfo")
    Observable<CourseOneInfo> courseOneInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //一对一课程授课方式信息
    @POST(UrlHelper.PUBLIC_URL + "one/teachingMethod")
    Observable<TeachingMethod> teachingMethod(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //一对一申请试听
    @POST(UrlHelper.PUBLIC_URL + "one/applyCourseListen")
    Observable<String> applyCourseListen(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //一对一申请试听 2.0
    @POST(UrlHelper.PUBLIC_URL + "center/homeApplyCourseListen")
    Observable<String> homeApplyCourseListen(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //获取老师周几上课
    @POST(UrlHelper.PUBLIC_URL + "/one/selectCourseDate")
    Observable<List<CourseDate>> selectCourseDate(@Header("ApiCode") String ApiCode,@Body RequestBody body);
    //获取老师当天上课状态
    @POST(UrlHelper.PUBLIC_URL + "/one/teacherTimeType")
    Observable<List<Integer>> teacherTimeType(@Header("ApiCode") String ApiCode,@Body RequestBody body);
    //获取订单地址
    @POST(UrlHelper.PUBLIC_URL + "/one/classAddress")
    Observable<String> orderAddress(@Header("ApiCode") String ApiCode,@Body RequestBody body);
    //通过城市code，获取城市下的区县信息
    @POST("findAreaCityByCode")
    Observable<String> findAreaCityByCode(@Header("ApiCode") String ApiCode,@Body RequestBody body);


    /******************首页*****************/
    //查询热门直播课和视频课
    @POST(UrlHelper.PUBLIC_URL + "homePage/findLiveAndVideo")
    Observable<LiveAndVideo> findLiveAndVideo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //获取科目集合
    @GET("subject")
    Observable<String> subject(@Header("ApiCode") String ApiCode);

    //获取科目找老师
    @POST(UrlHelper.PUBLIC_URL + "homePage/teacherSubject")
    Observable<BasePageBean<TeacherInformation>> teacherSubject(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //老师线下一对一
    @POST(UrlHelper.PUBLIC_URL + "one/recommendTeacher")
    Observable<BasePageBean<TeacherInformation>> recommendTeacher(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //线下班课查询 3.0
    @POST(UrlHelper.PUBLIC_URL + "squad/squadRecommend")
    Observable<BasePageBean<SquadRecommendInformation>> squadRecommend(@Header("ApiCode") String ApiCode,@Body RequestBody body);


    //老师开课集合 2.0
    @POST(UrlHelper.PUBLIC_URL + "teaInfo/teacherCourseArray")
    Observable<BasePageBean<CourseOflineBean>> teacherCourseArray(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //老师直播课 2.0
    @POST(UrlHelper.PUBLIC_URL + "teaInfo/teacherCourseArray")
    Observable<BasePageBean<CourseLiveBean>> courseLiveStartList(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //老师线下一对一
    @POST(UrlHelper.PUBLIC_URL + "homePage/overallSituationSearch")
    Observable<BasePageBean<TeacherInformation>> overallSituationSearch(@Header("ApiCode") String ApiCode,@Body RequestBody body);


    /******************个人中心*****************/
    //个人资料页
    @GET(UrlHelper.PUBLIC_URL + "center/personalData")
    Observable<Personal> personalData(@Header("ApiCode") String ApiCode);

    //我的订单列表
    @POST(UrlHelper.PUBLIC_URL + "center/myOrder")
    Observable<BasePageBean<OrderBean>> myOrder(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //删除订单
    @POST(UrlHelper.PUBLIC_URL + "center/removeOrder")
    Observable<String> removeOrder(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //订单详情myOrder/orderInfo
    @POST(UrlHelper.PUBLIC_URL + "myOrder/orderInfo")
    Observable<OrderOnfflineInfo> orderInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //线下班课订单详情 3.0
    @POST(UrlHelper.PUBLIC_URL+"myOrder/orderInfo")
    Observable<OrderOfflineDetailsBean> orderOfflineInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);
    //删除订单 3.0
    @POST(UrlHelper.PUBLIC_URL+"myOrder/removeOrder")
    Observable<String> removeOfflineOrder(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //我的课程
    @POST(UrlHelper.PUBLIC_URL + "center/myCourse")
    Observable<BasePageBean<OrderCourse>> myCourse(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //我的课程详情
    @POST(UrlHelper.PUBLIC_URL+"center/myCourseInfo")
    Observable<MyCourseInfo> myCourseInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //我的助教信息
    @GET(UrlHelper.BASE_URL + "myCenter/userAssistant")
    Observable<UserAssistant> userAssistant(@Header("ApiCode") String ApiCode);

    //我的课表月份状态
    @POST(UrlHelper.PUBLIC_URL + "schedule/monthStatus")
    Observable<List<String>> monthStatus(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //我的课表
    @POST(UrlHelper.PUBLIC_URL + "schedule/mySchedule")
    Observable<List<MySchedule>> mySchedule(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //用户签到
    @POST(UrlHelper.PUBLIC_URL + "center/myScheduleSign")
    Observable<String> myScheduleSign(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //我的老师
    @GET(UrlHelper.PUBLIC_URL + "center/myTeacher")
    Observable<List<TeacherInformation>> myTeacher(@Header("ApiCode") String ApiCode);

    //我的消息
    @POST(UrlHelper.BASE_URL + "myCenter/myMessage")
    Observable<BasePageBean<Message>> myMessage(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //账户余额信息
    @GET(UrlHelper.PUBLIC_URL + "myWallet/myBalance")
    Observable<MyBalance> myBalance(@Header("ApiCode") String ApiCode);

    //绑定银行卡 2.0
    @POST(UrlHelper.PUBLIC_URL + "myWallet/bindingBankInfo")
    Observable<String> bindingBankInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //我的银行卡  2.0
    @GET(UrlHelper.PUBLIC_URL + "myWallet/myBankCard")
    Observable<MyBankCardBean> myBankCard(@Header("ApiCode") String ApiCode);

    //我的消息数量
    @GET(UrlHelper.PUBLIC_URL + "center/myMsgNum")
    Observable<Integer> myMsgNum(@Header("ApiCode") String ApiCode);

    //我的钱包账户记录信息
    @POST(UrlHelper.PUBLIC_URL + "myWallet/myWallet")
    Observable<List<Wallet>> myWallet(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //申请提现
    @POST(UrlHelper.PUBLIC_URL + "myWallet/userWithdrawals")
    Observable<String> userWithdrawals(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //上传头像
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "center/uploadOnePhoto")
    Observable<String> uploadOnePhoto(@Header("ApiCode") String ApiCode,@Part List<MultipartBody.Part> partList);
    //保存用户信息
    @POST(UrlHelper.PUBLIC_URL+"center/savePersonalData")
    Observable<String> savePersonalData(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    /******************设置*****************/
    //家长退出登录
    @GET(UrlHelper.PUBLIC_URL+"sign/logout")
    Observable<String> logout(@Header("ApiCode") String ApiCode);
    @GET(UrlHelper.BASE_URL+"myCenter/userSetUpStatus")
    Observable<String> userSetUpStatus(@Header("ApiCode") String ApiCode);
    @POST(UrlHelper.BASE_URL+"/myCenter/userSetUp")
    Observable<String> userSetUp(@Header("ApiCode") String ApiCode,@Body RequestBody body);
    //关键词查询帮助
    @POST(UrlHelper.PUBLIC_URL+"help/queryHelp")
    Observable<BasePageBean<HelpBean>> queryHelp(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    /******************订单详情*****************/
    //直播课订单详情
    @POST(UrlHelper.PUBLIC_URL + "direct/payDirectInfo")
    Observable<PayDirectInfo> payDirectInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //查询班课详情
    @POST(UrlHelper.BASE_URL+"squad/courseSquadInfo")
    Observable<CourseSquadBean> courseSquadInfo(@Header("ApiCode") String ApiCode,@Body RequestBody body);
    //线下班课课表
    @POST(UrlHelper.BASE_URL+"squad/courseSquadSchedule")
    Observable<List<CourseSquadScheduleBean>> courseSquadSchedule(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    /******************支付*****************/
    //确认支付
    @POST(UrlHelper.PUBLIC_URL + "orderPay/confirmPay")
    Observable<String> aliConfirmPay(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //去支付
    @POST(UrlHelper.PUBLIC_URL + "orderPay/createCourseOrder")
    Observable<CourseOrder> createCourseOrder(@Header("ApiCode") String ApiCode,@Body RequestBody body);

    //0元订单支付
    @POST(UrlHelper.PUBLIC_URL + "orderPay/zeroOrder")
    Observable<String> zeroOrder(@Header("ApiCode") String ApiCode,@Body RequestBody body);


    //查询Android端APP版本是否是最新版版本
    @GET(UrlHelper.BASE_URL + "tool/androidAppVersion")
    Observable<VersionBean> appVersion(@Header("ApiCode") String ApiCode);

    //查询Android端APP版本是否是最新版版本
    @POST(UrlHelper.BASE_URL + "tool/updateVersion")
    Observable<Void> updateVersion(@Header("ApiCode") String ApiCode,@Body RequestBody body);

}
