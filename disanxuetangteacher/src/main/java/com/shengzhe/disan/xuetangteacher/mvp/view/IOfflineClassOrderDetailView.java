package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.disanxuelib.view.CommonCrosswise;

/**
 * Created by Administrator on 2018/4/11.
 */

public interface IOfflineClassOrderDetailView {
    //在线直播课
    TextView getOrderMethodName();

    //下单状态
    TextView getOrderSubStatus();

    //下单编号
    CommonCrosswise getOrderCode();

    //下单时间
    CommonCrosswise getOrderpayTime();

    //课程图
    ImageView getHotPhoto();

    //高中语文-高中语文文言文阅读…
    TextView getSubjctClassType();

    //增加学生对现代文的于都理解能力，增加学 生对现代文…
    TextView getSubjctClassDetails();

    TextView getOfflineMannum();

    //2小时/次，共20次
    TextView getOrderTime();

    //课程价格
    TextView getOrderPrice();

    //家长姓名
    CommonCrosswise getOrderParentName();

    //家长的手机号
    CommonCrosswise getOrderParentMobile();

    //学姓姓名
    CommonCrosswise getOrderStudentName();

    //学生姓名
    CommonCrosswise getOrderStudentSex();

    //优惠信息
    CommonCrosswise getOrderFree();

    CommonCrosswise getOrderBuytype();

    CommonCrosswise getOrderBuytimes();

    CommonCrosswise getOrderSigleprice();

    CommonCrosswise getOrderSumprice();

    CommonCrosswise getOrderParentprice();

    TextView getOfflineCountdown();
    View getMineLiveorderDelte();
    View getViewDetail();
}
