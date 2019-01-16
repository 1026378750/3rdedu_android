package com.shengzhe.disan.xuetangparent.utils;

import com.main.disanxuelib.bean.HelpBean;
import com.main.disanxuelib.bean.HelpSearcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 帮助中心文档数据 on 2018/5/8.
 */

public class HelpUtil {

    public static HelpUtil helpUtil;

    public static HelpUtil getInstance(){
        if (helpUtil==null)
            helpUtil = new HelpUtil();
        return helpUtil;
    }

    public List<HelpSearcher> getHelpDetailList(){
        List<HelpSearcher> helpList = new ArrayList<>();

        return helpList;
    }

    public HelpSearcher getHelpLogin(){
        HelpSearcher helpSearcher = new HelpSearcher();
        helpSearcher.id=0;
        helpSearcher.title ="注册登录";
        List<HelpBean> helpList = new ArrayList<>();

        HelpBean helpBean = new HelpBean();
        helpBean.id=1;
        helpBean.title = "登录时提示“当前没有可用网络，请检查您的网络设置”";
        helpBean.context="苹果手机用户请前往手机设置-蜂窝移动网络-蜂窝移动数据下找到“第三学堂”APP-打开允许使用“WLAN与蜂窝移动网”；\n\n" +
                "安卓手机用户请检查是否点亮了WIFI连接以及是否打开了移动数据。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=2;
        helpBean.title = "登陆APP后无法定位当前城市";
        helpBean.context="1、请前往手机设置-隐私-打开“定位服务”\n" +
                            "2、请前往您的手机设置-找到“第三学堂”APP-允许使用“无线局域网与蜂窝移动数据”\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=3;
        helpBean.title = "注册或登录时，收不到短信验证码。";
        helpBean.context="1、请检查您输入的手机号码是否有误。\n" +
                "2、请检查验证码短信是否被您的手机软件拦截。\n" +
                "3、请确认手机内存是否已满，建议清除内存后重新获取验证码。\n\n"+
                "温馨提示：如果是信号网络延迟，可重启手机稍后尝试重新获取验证码。\n";
        helpList.add(helpBean);

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpCourse(){
        HelpSearcher helpSearcher = new HelpSearcher();
        helpSearcher.id=1;
        helpSearcher.title ="课程管理";
        List<HelpBean> helpList = new ArrayList<>();

        HelpBean helpBean = new HelpBean();
        helpBean.id=1;
        helpBean.title = "如何查看课程安排？";
        helpBean.context="请您进入APP-【我的】-点击【我的课程】就能看到课程安排的详情。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=2;
        helpBean.title = "如何对课程进行签到？";
        helpBean.context="请您进入APP-【我的】-【我的课程】点击【我的课表】。对于直播课，点击“进入”会引导您到电脑网页端上课。对于线下课程，" +
                "如果负责授课的老师已考勤，您会看到课程显示“签到”按钮，就可以点击签到了。此外，签到后的课程不能再进行调课退课等操作。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=3;
        helpBean.title = "调课退课问题";
        helpBean.context="线下1对1课程的调课或退课都需要您联系您的助教，让其帮助您调课或退课；线下班课只能退课，退课也需联系您的助教。" +
                "此外，直播课和品牌课一经购买，概不退费。退课的费用会退至【我的】-【我的钱包】中。\n";
        helpList.add(helpBean);

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpWallet(){
        HelpSearcher helpSearcher = new HelpSearcher();
        helpSearcher.id=2;
        helpSearcher.title ="我的钱包";
        List<HelpBean> helpList = new ArrayList<>();

        HelpBean helpBean = new HelpBean();
        helpBean.id=1;
        helpBean.title = "如何提现？";
        helpBean.context="若您对购买的课程不满意而要求退课，退课后的费用会进入【我的】-【我的钱包】并可提现或者用于其他课程购买。\n" +
                "目前提现需要联系您的助教帮忙操作提现。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=2;
        helpBean.title = "如何查询到账情况？";
        helpBean.context="您可以在APP-【我的】-【我的钱包】资金记录中查看。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=3;
        helpBean.title = "提现多久能到账？";
        helpBean.context="提现方面，您需要在【我的】-【我的钱包】绑定银行卡再申请提现。一般1-3个工作日到账。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=4;
        helpBean.title = "为什么会提现失败？";
        helpBean.context="提现失败的余额会退回至您的钱包中，请放心。\n" +
                "请您再次联系您的助教核对您提供的收款信息是否有误。\n";
        helpList.add(helpBean);

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpZhuJiao(){
        HelpSearcher helpSearcher = new HelpSearcher();
        helpSearcher.id=3;
        helpSearcher.title ="我的助教";
        List<HelpBean> helpList = new ArrayList<>();

        HelpBean helpBean = new HelpBean();
        helpBean.id=1;
        helpBean.title = "如何绑定助教？";
        helpBean.context="请您在APP-【我的】-【我的助教】点击【联系客服】，让客服帮您绑定助教。\n";
        helpList.add(helpBean);

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpDaoZhang(){
        HelpSearcher helpSearcher = getHelpWallet();
        helpSearcher.id=3;
        helpSearcher.title ="我的钱包";
        List<HelpBean> helpList = new ArrayList<>();

        helpList.add(helpSearcher.helpList.get(1));

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpTuiKe(){
        HelpSearcher helpSearcher = getHelpCourse();
        List<HelpBean> helpList = new ArrayList<>();
        helpList.add(helpSearcher.helpList.get(helpSearcher.helpList.size()-1));
        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpCash(){
        HelpSearcher helpSearcher = getHelpWallet();
        helpSearcher.id=3;
        helpSearcher.title ="我的钱包";
        List<HelpBean> helpList = new ArrayList<>();

        helpList.add(helpSearcher.helpList.get(2));

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

}
