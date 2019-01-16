package com.shengzhe.disan.xuetangteacher.utils;

import com.main.disanxuelib.util.BaseIntegerUtil;

/**
 * Created by 系统实数常量 on 2017/11/22.
 *
 *老师端 以 3 开头
 *
 */

public class IntegerUtil extends BaseIntegerUtil{
    //刷新首页数据
    public static final int EVENT_ID_11002 = 31001;
    //科目选择
    public static final int EVENT_ID_11017 = 31002;
    //添加老师教学经历
    public static final int EVENT_ID_11018 = 31003;
    //线下一对一课程编辑
    public static final int EVENT_ID_11019 = 31004;
    //线下一对一课程删除
    public static final int EVENT_ID_11020 = 31005;
    //在线直播课程下一步
    public static final int EVENT_ID_11021 = 31006;
    //在线直播课程删除
    public static final int EVENT_ID_11022 = 31007;
    //在线直播课程创建
    public static final int EVENT_ID_11023 = 31008;
    //在线直播课程编辑
    public static final int EVENT_ID_11024 = 3109;
    //城市修改
    public static final int EVENT_ID_11025 = 31011;
    //所属校区修改
    public static final int EVENT_ID_11026 = 31012;
    //所授课地址修改
    public static final int EVENT_ID_11027 = 31013;
    //授课区域修改
    public static final int EVENT_ID_11028 = 31014;
    //授课时间修改
    public static final int EVENT_ID_11029 = 31015;
    //基本信息
    public static final int EVENT_ID_11030 = 31016;
    //昵称修改
    public static final int EVENT_ID_11031 = 31017;
    //个人简介修改
    public static final int EVENT_ID_11032 = 31018;
    //毕业院校修改
    public static final int EVENT_ID_11033 = 31019;
    //教龄修改
    public static final int EVENT_ID_11034 = 31020;
    //通知编辑教学经历
    public static final int EVENT_ID_11036 = 31021;
    //实名认证后通知首页刷新
    public static final int EVENT_ID_11038 = 31022;
    //刷新课表
    public static final int EVENT_ID_11039 = 31023;
    //线下班课下一步
    public static final int EVENT_ID_11040 = 31024;
    //线下班课删除
    public static final int EVENT_ID_11041 = 31025;
    //线下班课创建
    public static final int EVENT_ID_11042 = 31026;
    //线下班课编辑
    public static final int EVENT_ID_11043 = 31027;

    //待支付订单
    public final static int ORDER_STATUS_WAITPAY = 1;
    //已支付订单
    public final static int ORDER_STATUS_PAYED = 2;
    //已完成订单
    public final static int ORDER_STATUS_FINISH = 8;
    //已关闭订单
    public final static int ORDER_STATUS_ClOSE = 9;

    //查询月份月份标识
    public final static int WEB_API_MonthStatus = 32001;
    //查询有课信息
    public final static int WEB_API_MySchedule = 32002;
    //查询有课信息
    public final static int WEB_API_ModifyCourseStatus = 32003;
    //发送验证码
    public final static int WEB_API_SendVerifyLogin = 32004;
    //登录
    public final static int WEB_API_UserLogin = 32005;
    //更新用户信息
    public final static int WEB_API_UpDateUser = 32006;
    //获取首页上的数据
    public final static int WEB_API_HomeData = 32007;
    //获取首页banner
    public final static int WEB_API_TeacherBanner = 32008;
    //得到支付类型和订单状态并且进行网络请求
    public final static int WEB_API_MyTeacherOrder = 32009;
    //删除订单
    public final static int WEB_API_RemoveOrder = 32010;
    //订单详情
    public final static int WEB_API_OrderDetail = 32011;

    //提醒助教
    public final static int TEA__BINDING_32011= 32011;
    //绑定银行提醒
    public final static int TEA__BINDING_32012= 32012;
    //回调钱包页面
    public final static int TEA__BINDING_32014= 32014;

    //授课阶段
    public final static int WEB_API_ConnomGrade = 33011;
    //获取开放城市
    public final static int WEB_API_OpenCity = 33012;
    //获取城市下面的校区
    public final static int WEB_API_CampusByCity = 33013;
    //获取首页科目集合
    public final static int WEB_API_CommonSubject = 33014;
    //我的消息
    public final static int WEB_API_CommonMessage = 33015;
    //查询Android端APP版本是否是最新版版本
    public final static int WEB_API_AppVersion = 33016;
    //考情状态查询
    public final static int WEB_API_SigninStatus = 33017;


    //在线直播课开课
    public final static int WEB_API_SaveOnLiveCourse = 34001;
    //线下一对一开课
    public final static int WEB_API_SaveOneToOneCourse = 34002;
    //添加线下班课数据
    public final static int WEB_API_SaveOfflineClassCourse = 34003;
    //获取直播课课次列表
    public final static int WEB_API_OnliveCycleList = 34004;
    //获取课程列表
    public final static int WEB_API_CourseList = 34005;
    //获取课程详情
    public final static int WEB_API_CourseDetail = 34006;
    //课程删除
    public final static int WEB_API_RemoveCourse = 34007;
    //班课详情
    public final static int WEB_API_CourseSquadDetail = 34008;
    //线下班课参加学生列表
    public final static int WEB_API_SquadStudentList = 34009;
    //线下班课大纲列表
    public final static int WEB_API_SquadOutlineList = 34010;

    //获取我的消息数量
    public final static int WEB_API_MessageNum = 35001;
    //获取老师教学经历
    public final static int WEB_API_ExperienceList = 35002;
    //删除老师教学经历
    public final static int WEB_API_DeleteTeacherExperience = 35003;
    //保存老师教学经历
    public final static int WEB_API_SaveTeacherExperience = 35004;
    //获取认证状态
    public final static int WEB_API_AutherStatus = 35005;
    //实名认证提交审核
    public final static int WEB_API_NameAuthentication = 35006;
    //实名认证状态数据
    public final static int WEB_API_CardApprData = 35007;
    //保存认证信息（教师资格、学历、专业资格认证）
    public final static int WEB_API_SaveCardAppr = 35008;
    //开课设置数据
    public final static int WEB_API_CourseSetting = 35009;
    //保存开课设置数据
    public final static int WEB_API_SaveCourseSetting = 35010;
    //获取老师课程信息
    public final static int WEB_API_TeacherMessage = 35011;
    //我的钱包账户记录信息
    public final static int WEB_API_WalletList = 35012;
    //账户余额信息
    public final static int WEB_API_MyBalance = 35013;
    //申请提现
    public final static int WEB_API_ExtractCash = 35014;
    //获取我的助教
    public final static int WEB_API_MinesSitant = 35015;
    //查询助教列表
    public final static int WEB_API_AssistantList = 35016;
    //绑定助教
    public final static int WEB_API_BindingAssistant = 35017;
    //获取提现记录
    public final static int WEB_API_PresentRecordList = 35018;
    //绑定银行卡
    public final static int WEB_API_BindingBank = 35019;
    //获取我的银行卡
    public final static int WEB_API_MyBankCard = 35020;
    //获取我的信息
    public final static int WEB_API_MyMessage = 35021;

}
