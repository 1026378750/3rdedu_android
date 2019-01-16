package com.shengzhe.disan.xuetangteacher.http.service;

import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.bean.BuyerBean;
import com.main.disanxuelib.bean.GradeParentBean;
import com.main.disanxuelib.bean.HelpBean;
import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.bean.TeachingExperienceBean;
import com.main.disanxuelib.bean.VersionBean;
import com.main.disanxuelib.view.banner.BannerBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherOrderBean;
import com.shengzhe.disan.xuetangteacher.bean.WalletDetailsBean;
import com.shengzhe.disan.xuetangteacher.http.UrlHelper;
import com.shengzhe.disan.xuetangteacher.bean.AuthenticationCoreBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseDetailsBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseItemBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.bean.MyBalance;
import com.shengzhe.disan.xuetangteacher.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangteacher.bean.MySchedule;
import com.shengzhe.disan.xuetangteacher.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangteacher.bean.PersonalDataBean;
import com.shengzhe.disan.xuetangteacher.bean.PresentRecordBean;
import com.shengzhe.disan.xuetangteacher.bean.RealNameVerify;
import com.shengzhe.disan.xuetangteacher.bean.SearchAssistantMode;
import com.shengzhe.disan.xuetangteacher.bean.StartCourseSetupBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherInfo;
import com.shengzhe.disan.xuetangteacher.bean.User;
import com.shengzhe.disan.xuetangteacher.bean.UserAssistantMode;
import com.shengzhe.disan.xuetangteacher.bean.courseSquadScheduleBean;
import com.shengzhe.disan.xuetangteacher.bean.CampusBean;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * service-下载接口
 * Created by hy on 2017/9/18.
 */

public interface HttpService {
    /*****************登录注册开通城市******************/
    //获取手机验证码
    @POST("getMethodCode")
    Observable<String> getMethodCode(@Body RequestBody body);

    //开通城市sign/login
    @GET("findCityList")
    Observable<String> findCityList();

    /******************在线课堂*****************/
    //通过城市code，获取城市下面的校区
    @POST("findCampusByCity")
    Observable<List<CampusBean>> findCampusByCity(@Body RequestBody body);

    //获取阶段集合
    @GET("gradeParent")
    Observable<List<GradeParentBean>> gradeParent();

    //获取科目集合
    @GET("subject")
    Observable<List<Subject>> subject();

    /**********************首页********************************/
    //获取首页banner
    @GET(UrlHelper.PUBLIC_URL + "homePage/banner")
    Observable<List<BannerBean>> banner();

    //获取老师首页数据
    @GET(UrlHelper.PUBLIC_URL + "homePage/homeData")
    Observable<HomeBean> homeData();

    //老师订单列表
    @POST(UrlHelper.PUBLIC_URL + "myOrder/myTeacherOrder")
    Observable<BasePageBean<TeacherOrderBean>> myTeacherOrder(@Body RequestBody body);

    //订单详情
    @POST(UrlHelper.PUBLIC_URL + "myOrder/orderInfo")
    Observable<OrderOfflineDetailsBean> orderInfo(@Body RequestBody body);

    //删除订单
    @POST(UrlHelper.PUBLIC_URL + "myOrder/removeOrder")
    Observable<String> removeOrder(@Body RequestBody body);


    /*******************开课********************************/
    //在线直播课开课
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "startCourse/addAndModifyDirect")
    Observable<String> addAndModifyDirect(@Part List<MultipartBody.Part> partList);

    //线下一对一开课
    @POST(UrlHelper.PUBLIC_URL + "startCourse/addAndModifyOne")
    Observable<String> addAndModifyOne(@Body RequestBody body);

    //获取直播课课次列表
    @POST(UrlHelper.PUBLIC_URL + "startCourse/courseItem")
    Observable<List<CourseItemBean>> courseItem(@Body RequestBody body);

    //开课直播课列表
    @POST(UrlHelper.BASE_URL + "teaInfo/teacherCourseArray")
    Observable<BasePageBean<CourseLiveBean>> courseLiveStartList(@Body RequestBody body);

    //添加线下班课数据
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "squad/saveCourseSquad")
    Observable<String> saveCourseSquad(@Part List<MultipartBody.Part> partList);

    //开课线下一对一列表
    @POST(UrlHelper.BASE_URL + "teaInfo/teacherCourseArray")
    Observable<BasePageBean<CourseOflineBean>> courseOfflineStartList(@Body RequestBody body);

    //开课详情
    @POST(UrlHelper.PUBLIC_URL + "startCourse/courseInfo")
    Observable<CourseDetailsBean> courseInfo(@Body RequestBody body);

    //开课中的直播出课详情
    @POST(UrlHelper.PUBLIC_URL + "startCourse/courseInfo")
    Observable<CourseDetailLiveBean> courseLiveInfo(@Body RequestBody body);

    //删除课程
    @POST(UrlHelper.PUBLIC_URL + "startCourse/removeCourse")
    Observable<String> removeCourse(@Body RequestBody body);

    //查询班课详情
    @POST(UrlHelper.BASE_URL + "squad/courseSquadInfo")
    Observable<CourseSquadBean> courseSquadInfo(@Body RequestBody body);

    //线下班课课表
    @POST(UrlHelper.BASE_URL + "squad/courseSquadSchedule")
    Observable<List<courseSquadScheduleBean>> courseSquadSchedule(@Body RequestBody body);

    //线下班课学生列表
    @POST(UrlHelper.BASE_URL + "schedule/courseSquadStudentSchedule")
    Observable<List<BuyerBean>> courseSquadStudentSchedule(@Body RequestBody body);


    /**********************课表*******************************/
    //我的课表月份状态
    @POST(UrlHelper.BASE_URL + "schedule/monthStatus")
    Observable<List<String>> monthStatus(@Body RequestBody body);

    //我的课表
    @POST(UrlHelper.BASE_URL + "schedule/mySchedule")
    Observable<List<MySchedule>> mySchedule(@Body RequestBody body);

    //老师考勤
    @POST(UrlHelper.BASE_URL + "schedule/teacherCheck")
    Observable<String> teacherCheck(@Body RequestBody body);


     //登录接口
    @POST(UrlHelper.PUBLIC_URL + "sign/login")
    Observable<User> login(@Body RequestBody body);

    /**********************设置*******************************/
    //考情状态
    @GET(UrlHelper.BASE_URL + "myCenter/userSetUpStatus")
    Observable<String> userSetUpStatus();

    //老师考勒
    @POST(UrlHelper.BASE_URL + "myCenter/userSetUp")
    Observable<String> userSetUp(@Body RequestBody body);

    //个人资料页
    @GET(UrlHelper.PUBLIC_URL + "center/personalData")
    Observable<PersonalDataBean> personalData();

    //个人资料页
    @GET(UrlHelper.BASE_URL + "myCenter/myCenter")
    Observable<String> myCenter();

    //关键词查询帮助
    @POST(UrlHelper.PUBLIC_URL+"help/queryHelp")
    Observable<BasePageBean<HelpBean>> queryHelp(@Body RequestBody body);

    //上传头像
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "center/savePersonalData")
    Observable<String> uploadOnePhoto(@Part List<MultipartBody.Part> partList);

    //查询老师教学经历
    @GET(UrlHelper.PUBLIC_URL + "center/queryTeachingExperience")
    Observable<List<TeachingExperienceBean>> queryTeachingExperience();

    //删除教学经历
    @POST(UrlHelper.PUBLIC_URL + "center/deleteTeachingExperience")
    Observable<String> deleteTeachingExperience(@Body RequestBody body);

    //保存教学经历
    @POST(UrlHelper.PUBLIC_URL + "center/saveTeachingExperience")
    Observable<TeachingExperienceBean> saveTeachingExperience(@Body RequestBody body);

    //查询老师教学经历
    @GET(UrlHelper.PUBLIC_URL + "center/authenticationCore")
    Observable<AuthenticationCoreBean> authenticationCore();

    //实名认证提交审核
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "center/realNameAuthentication")
    Observable<String> realNameAuthentication(@Part List<MultipartBody.Part> partList);

    //实名认证状态数据
    @GET(UrlHelper.PUBLIC_URL + "center/cardApprData")
    Observable<RealNameVerify> cardApprData();

    //保存用户信息
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "center/savePersonalData")
    Observable<String> savePersonalData(@Part List<MultipartBody.Part> partList);

    //教师资格、学历、专业资格认证
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "center/authenticationOther")
    Observable<String> authenticationOther(@Part List<MultipartBody.Part> partList);

    //开课设置数据
    @GET(UrlHelper.PUBLIC_URL + "startCourse/startCourseSetupData")
    Observable<StartCourseSetupBean> startCourseSetupData();

    //保存开课设置数据
    @POST(UrlHelper.PUBLIC_URL + "startCourse/addStartCourseData")
    Observable<String> addStartCourseData(@Body RequestBody body);

    //保存开课设置数据 2.0
    @Multipart
    @POST(UrlHelper.PUBLIC_URL + "startCourse/addStartCourseData")
    Observable<String> addStartComplexCourse(@Part List<MultipartBody.Part> partList);

    //我的消息
    @POST(UrlHelper.BASE_URL + "myCenter/myMessage")
    Observable<BasePageBean<Message>> myMessage(@Body RequestBody body);

    //个人中心
    @GET(UrlHelper.PUBLIC_URL + "teaInfo/teaHomePage")
    Observable<TeacherInfo> myHomePage();

    //账户余额信息
    @GET(UrlHelper.PUBLIC_URL + "myWallet/myBalance")
    Observable<MyBalance> myBalance();

    //我的钱包账户记录信息
    @POST(UrlHelper.PUBLIC_URL + "myWallet/myWallet")
    Observable<BasePageBean<WalletDetailsBean>> myWallet(@Body RequestBody body);

    //我的助教信息
    @GET(UrlHelper.BASE_URL + "myCenter/userAssistant")
    Observable<UserAssistantMode> userAssistant();

    //绑定助教
    @POST(UrlHelper.BASE_URL + "myCenter/bindingAssistant")
    Observable<UserAssistantMode> bindingAssistant(@Body RequestBody body);

    //查询助教
    @POST(UrlHelper.BASE_URL + "myCenter/searchAssistant")
    Observable<SearchAssistantMode> searchAssistant(@Body RequestBody body);

    //提现记录
    @GET(UrlHelper.PUBLIC_URL + "myWallet/presentRecord")
    Observable<List<PresentRecordBean>> presentRecord();

    //绑定银行卡
    @POST(UrlHelper.PUBLIC_URL + "myWallet/bindingBankInfo")
    Observable<String> bindingBankInfo(@Body RequestBody body);

    //我的银行卡
    @GET(UrlHelper.PUBLIC_URL + "myWallet/myBankCard")
    Observable<MyBankCardBean> myBankCard();

    //查询Android端APP版本是否是最新版版本
    @GET(UrlHelper.BASE_URL + "tool/androidAppVersion")
    Observable<VersionBean> appVersion();

    //查询校区体验课次数
    @GET(UrlHelper.BASE_URL + "tool/campushExperienctNum")
    Observable<Integer> campushExperienctNum();


    //查询Android端APP版本是否是最新版版本
    @POST(UrlHelper.BASE_URL + "tool/updateVersion")
    Observable<Void> updateVersion(@Body RequestBody body);

    //申请提现
    @POST(UrlHelper.PUBLIC_URL + "myWallet/userWithdrawals")
    Observable<String> userWithdrawals(@Body RequestBody body);
}
