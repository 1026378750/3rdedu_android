package com.main.disanxuelib.util;

/**
 * Created by 系统实数常量 on 2017/11/22.
 *
 * 基础 以 1 开头
 *
 */

public class BaseIntegerUtil {

    public static final int ABS_UNAUTHORIZED = 401;//请求授权失败
    public static final int ABS_FORBIDDEN = 403;//请求不允许
    public static final int ABS_NOT_FOUND = 404;//没有发现文件、查询或URl
    public static final int ABS_REQUEST_TIMEOUT = 408;//客户端没有在用户指定的饿时间内完成请求
    public static final int ABS_INTERNAL_SERVER_ERROR = 500;//服务器产生内部错误
    public static final int ABS_BAD_GATEWAY = 502;//服务器暂时不可用，有时是为了防止发生系统过载
    public static final int ABS_SERVICE_UNAVAILABLE = 503;//服务器过载或暂停维修
    public static final int ABS_GATEWAY_TIMEOUT = 504;//关口过载，服务器使用另一个关口或服务来响应用户，等待时间设定值较长
    public static final int ABS_TOKEN_FAIL=000004;//token验证失败
    public static final int ABS_TOKEN_EXPIRE=000005;//	token过期
    public static final int ABS_SYSTEM_EXPIRE=000006;//	验证码超时，请重新获取
    public static final int ABS_TOKEN_POST_NULL=000003;//	请求token不能为空
    public static final int ABS_USER_VERIFICATION=000006;//验证码超时，请重新获取
    public static final int ABS_USER_GETMOBILE=000007;//获取手机验证码失败
    public static final int ABS_USER_PARAMETER=8;//参数验证失败
    public static final int ABS_USER_VERIFIAGAIN=9;//验证码有误，请重新获取
    public static final int PERMISSION_REQUEST_FILE = 1002;
    public static final int PERMISSION_REQUEST_LOCATION = 1003;
    public static final int PERMISSION_REQUEST_CAMERA = 1004;
    public static final int PERMISSION_REQUEST_ALBUM = 1005;
    public static final int PERMISSION_REQUEST_SETTING = 1006;
    public static final int PERMISSION_SYSEX_SETTING = 111111;

    //打开筛选框
    public final static int CONDITION_POPUP_OPEN = 11001;
    //关闭筛选框
    public final static int CONDITION_POPUP_ClOSE = 11002;

    //evenbus标识
    public static final int EVENT_ID_11001 = 12001;
    //课程表修改
    public static final int EVENT_ID_11004 = 12002;
    //登录成功结果
    public static final int EVENT_ID_11006 = 12003;
    //刷新我的
    public static final int EVENT_ID_11013 = 12004;
    //订单删除
    public static final int EVENT_ID_11014 = 12005;
    //订单刷新
    public static final int EVENT_ID_11015 = 12006;
    //生成课程表
    public static final int MESSAGE_ID_60001 = 13001;
}
