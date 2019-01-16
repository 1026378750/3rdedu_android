package com.shengzhe.disan.xuetangparent.utils;

import com.main.disanxuelib.util.BaseIntegerUtil;

/**
 * Created by 系统实数常量 on 2017/11/22.
 *
 * 家长端 以 2 开头
 *
 */

public class IntegerUtil extends BaseIntegerUtil{
    //课程表清空
    public static final int EVENT_ID_11003 = 21001;
    //地址结果
    public static final int EVENT_ID_11005 = 21002;
    //退出登录
    public static final int EVENT_ID_11007 = 21003;
    //直播课课程详情
    public static final int EVENT_ID_11008 = 21004;
    //线下一对一课程详情
    public static final int EVENT_ID_11009 = 21005;
    //视频课课程详情
    public static final int EVENT_ID_11010 = 21006;
    //地址添加修改
    public static final int EVENT_ID_11011 = 21007;
    //修改年级
    public static final int EVENT_ID_11012 = 21008;
    // 线下一对一申请试听、立即购买返回处理
    public static final int EVENT_ID_11016 = 21009;

    //微信支付成功
    public final static int EVENT_ID_7001 = 21010;
    //支付宝支付成功
    public final static int EVENT_ID_7002 = 21011;
    //余额 支付成功
    public final static int EVENT_ID_7003 = 21012;
    //免费 支付成功
    public final static int EVENT_ID_7004 = 21013;
    //支付取消
    public final static int EVENT_ID_7005 = 21014;
    //支付失败
    public final static int EVENT_ID_7006 = 21015;

    // 线下一对一申请试听
    public static final int EVENT_ID_11017 = 21016;
    //跳转到品牌课
    public static final int EVENT_ID_11018 = 21017;
    //跳转到直播课
    public static final int EVENT_ID_11019 = 21018;
    //推荐班课更多
    public static final int EVENT_ID_11020 = 21019;

    //待支付订单
    public final static int ORDER_STATUS_WAITPAY = 1;
    //已支付订单
    public final static int ORDER_STATUS_PAYED = 2;
    //已完成订单
    public final static int ORDER_STATUS_FINISH = 8;
    //已关闭订单
    public final static int ORDER_STATUS_ClOSE = 9;

    public static final int Activity_Modify = 22001;

    //提醒助教
    public final static int TEA__BINDING_32011= 32011;
    //绑定银行提醒
    public final static int TEA__BINDING_32012= 32012;

    //通知在线直播课是否关闭筛选框
    public final static int CONDITION_VIDEOPOPUP_ClOSE = 23001;
    //回调钱包页面
    public final static int TEA__BINDING_32014= 32014;

    //发送验证码请求
    public final static int WEB_API_SendVerifyLogin  = 24001;
    //发送验证码请求
    public final static int WEB_API_SendLogin  = 24002;
    //获取开头城市
    public final static int WEB_API_OpenCity  = 24003;
    //阶段和年级集合
    public final static int WEB_API_GradeList  = 24004;
    //获取城市下的区县信息
    public final static int WEB_API_CityByCode  = 24005;
    //获取首页科目集合
    public final static int WEB_API_CommonSubject  = 24006;
    //退出
    public final static int WEB_API_CommonLogout  = 24007;
    //考情状态
    public final static int WEB_API_SigninStatus  = 24008;
    //检查版本更新，保存更新的数据
    public final static int WEB_API_AppVersion  = 24009;
    //消息列表
    public final static int WEB_API_CommonMessage  = 24010;
    //获取直播课课次列表
    public final static int WEB_API_OnliveCycleList  = 24011;
    //视频课类型列表
    public final static int WEB_API_VideoTypeList  = 24012;
    //视频课列表
    public final static int WEB_API_VideoCourseList  = 24013;
    //视频课详情
    public final static int WEB_API_VideoDeatil  = 24014;
    //直播课列表
    public final static int WEB_API_OnLiveList  = 24015;
    //直播课详情
    public final static int WEB_API_OnLiveDetail  = 24016;
    //获取一对一课程详情
    public final static int WEB_API_CourseOneDetail  = 24017;
    //一对一课程授课方式信息
    public final static int WEB_API_TeachingMethod  = 24018;
    //首页申请试听
    public final static int WEB_API_ApplyTryListen  = 24019;
    //查询热门直播课和视频课
    public final static int WEB_API_LiveAndVideo  = 24020;
    //获取线下班课
    public final static int WEB_API_SquadCourseList  = 24021;
    //老师开课集合
    public final static int WEB_API_CourseStartList  = 24022;
    //获取课程列表
    public final static int WEB_API_CourseList  = 24023;
    //搜索列表
    public final static int WEB_API_SearchList  = 24024;
    //获取我的课程
    public final static int WEB_API_MyCourseList  = 24025;
    //直播课订单详情
    public final static int WEB_API_OnliveOrderDetail  = 24026;
    //获取下线班课详情
    public final static int WEB_API_CourseSquadDetail  = 24027;
    //线下班课大纲列表
    public final static int WEB_API_SquadOutlineList  = 24028;
    //个人资料页
    public final static int WEB_API_MyMessage  = 24029;
    //获取订单地址
    public final static int WEB_API_OrderAddress  = 24030;
    //获取科目找老师
    public final static int WEB_API_TeacherSubject  = 24031;
    //我的课程详情
    public final static int WEB_API_MyCourseDetail  = 24032;
    //我的课程详情
    public final static int WEB_API_ScheduleSignStatus  = 24033;
    //获取我的老师
    public final static int WEB_API_MyTeacherList  = 24034;
    //我的余额
    public final static int WEB_API_MyBalance  = 24035;
    //绑定银行卡
    public final static int WEB_API_BindingBank  = 24036;
    //我的银行卡
    public final static int WEB_API_MyBankCard  = 24037;
    //获取我的消息数量
    public final static int WEB_API_MessageNum  = 24038;
    //我的钱包账户记录信息
    public final static int WEB_API_WalletList  = 24039;
    //申请提现
    public final static int WEB_API_ExtractCash  = 24040;
    //更新个人数据
    public final static int WEB_API_UpDateUserPhoto  = 24041;
    //获取我的助教
    public final static int WEB_API_MinesSitant  = 24042;
    //查询有课信息
    public final static int WEB_API_MySchedule  = 24043;
    //查询月份月份标识
    public final static int WEB_API_MonthStatus  = 24044;
    //支付
    public final static int WEB_API_PayOrder  = 24045;
    //0元支付
    public final static int WEB_API_ZeroPay  = 24046;
    //创建订单
    public final static int WEB_API_CreateCourseOrder  = 24047;
    //获取线下一对一老师
    public final static int WEB_API_RecommendTeacher  = 24048;
    //获取线下一对一老师
    public final static int WEB_API_TeacherTimeType  = 24049;
    //获取老师周几上课
    public final static int WEB_API_CanTimer  = 24050;
    //视频课订单详情
    public final static int WEB_API_PayVideo  = 24051;
    //一对一申请试听
    public final static int WEB_API_ApplyOneListen  = 24052;
    //得到老师课程信息
    public final static int WEB_API_TeacherPage  = 24053;
    //更新个人数据
    public final static int WEB_API_UpDateUser  = 24054;
    //订单删除
    public final static int WEB_API_RemoveMessage  = 24055;

    //获取我的订单列表
    public final static int WEB_API_MyOrderList  = 24111;
    //删除订单
    public final static int WEB_API_RemoveOrder  = 24112;
    //订单详情
    public final static int WEB_API_DetailOrder  = 24113;
    //线下班课订单详情
    public final static int WEB_API_OfflineOrderDetail  = 24114;
    //线下班课订单详情
    public final static int WEB_API_RemoveOfflineOrder  = 24115;
    //更新考情状态
    public final static int WEB_API_UpDateSigninStatus  = 24116;

}
