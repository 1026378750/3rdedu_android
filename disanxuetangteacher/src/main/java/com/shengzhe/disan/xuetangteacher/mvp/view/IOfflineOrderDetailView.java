package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.main.disanxuelib.view.CommonCrosswise;

/**
 * Created by 线下一对一 on 2018/4/11.
 */

public interface IOfflineOrderDetailView {
    TextView getOrderMethodName();
    //在线直播课
    TextView getOrderTypeName();
    //互动小班课
    TextView getOrderSubStatus();
    //已关闭
    CommonCrosswise getOrderCode();
    //订单编号
    CommonCrosswise getOrderpayTime();
    //下单时间
    TextView getOrderType();
    //高中语文-高中语文文言文阅读…
    TextView getOrderTime();
    //2小时/次，共20次
    TextView getOrderTypeDetails();
    //授课方式 增加学生对现代文的于都理解能力，增加学生对现代文…
    CommonCrosswise getOrderTeachingMethod();
    //课程单价
    CommonCrosswise getOrderOnePrice();
    //课表
    TextView getOrderClassForm();
    //2017-12-13 周三 19:00
    TextView getOrderClassTime();
    //全部
    TextView getOrderClassAll();
    //家长昵称
    CommonCrosswise getOrderParentName();
    //家长手机
    CommonCrosswise getOrderParentMobile();
    //学生姓名
    CommonCrosswise getOrderStudentName();
    //学生地趾
    CommonCrosswise getOrderStudentAdress();
    //学生姓别
    CommonCrosswise getOrderStudentSex();
    //优惠信息
    CommonCrosswise getOrderFree();
    //总价：¥999.00
    TextView getOfflineSumprice();
    Button getMineOfflineorderDelte();
    //课程次数
    CommonCrosswise getOrderOneTimes();
    //课程详情
    View getViewDetail();
    View getMineLiveorderDelte();
}
