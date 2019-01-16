package com.shengzhe.disan.xuetangteacher.utils;

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

    public HelpSearcher getHelpLogin(){
        HelpSearcher helpSearcher = new HelpSearcher();
        helpSearcher.id=0;
        helpSearcher.title ="注册登录";
        List<HelpBean> helpList = new ArrayList<>();

        HelpBean helpBean = new HelpBean();
        helpBean.id=1;
        helpBean.title = "登录时提示“当前没有可用网络，请检查您的网络设置”";
        helpBean.context ="苹果手机用户请前往手机设置-蜂窝移动网络-蜂窝移动数据下找到“第三学堂”APP-打开允许使用“WLAN与蜂窝移动网”；\n\n" +
                "安卓手机用户请检查是否点亮了WIFI连接以及是否打开了移动数据。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=2;
        helpBean.title = "登陆APP后无法定位当前城市";
        helpBean.context="1、请前往手机设置-隐私-打开“定位服务”\n\n" +
                            "2、请前往您的手机设置-找到“第三学堂”APP-允许使用“无线局域网与蜂窝移动数据”\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=3;
        helpBean.title = "注册或登录时，收不到短信验证码。";
        helpBean.context ="1、请检查您输入的手机号码是否有误。\n\n" +
                "2、请检查验证码短信是否被您的手机软件拦截。\n\n" +
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
        helpBean.context="请您进入APP-点击【课表】就能看到课程安排的详情。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=2;
        helpBean.title = "如何操作考勤？";
        helpBean.context="请您进入APP内，点击【课表】，即将开始的课程会显示考勤按钮，点击考勤即可。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=3;
        helpBean.title = "如何调课？";
        helpBean.context="1对1课程的调课需要请您联系您的助教帮您进行调课。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=4;
        helpBean.title = "如何完善资料和进行开课设置？";
        helpBean.context="在【首页】点击“需完善”或进入【我的】-【个人资料】，编辑基本信息、城市、科目、教龄、毕业院校和个人简介（皆为必填项）。\n\n" +
                "个人资料完善后，点击-【首页】-【我要开课】或【开课设置】即可选择课程类型进行课程添加（设置授课年级、科目、授课方式、价格等）。\n";
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
        helpBean.title = "如何收取我的课酬？";
        helpBean.context="老师按时考勤，且家长及时确认并点击“课程结束”按钮后，该节课的课酬会自动转入老师的钱包余额。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=2;
        helpBean.title = "家长不结课能否收到课酬？";
        helpBean.context="家长如果没有点击结束课程，不会影响老师的课酬收入。系统超过3天将自动结课，并将课酬转给老师。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=3;
        helpBean.title = "如何提现？";
        helpBean.context="提现方面，第三学堂采用绑定银行卡的方式让用户提现。进入APP-【我的】-【钱包】点击银行卡，进入绑定银行卡界面，填完银行卡的信息点击绑定即可。\n\n" +
                "结课后，老师的课酬会在APP【我的】-【钱包】里，点击“全额提现”。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=4;
        helpBean.title = "如何查询到账情况？";
        helpBean.context="您可以在APP-【我的】-【钱包】资金记录中查看。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=5;
        helpBean.title = "提现多久能到账？";
        helpBean.context="到账时间取决于银行处理速度，一般1-3个工作日到账。您也可以在银行柜台或是网上银行登录您的银行账号查询账户信息。\n";
        helpList.add(helpBean);

        helpBean = new HelpBean();
        helpBean.id=6;
        helpBean.title = "为什么会提现失败？";
        helpBean.context="提现到账过程是由银行处理，如果出现提现失败的情况，建议您检查一下收款卡号和开户名是否匹配并重新尝试提现申请。\n";
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
        helpBean.context="老师必须实名认证、完善资料和绑定助教方可正式开课。请进入APP-【我的】-【我的助教】点击【绑定助教】并输入助教的手机号码即可绑定；\n\n如果您不知道助教的手机号码，请点击【申请分配助教】，" +
                "我们会在1-2工作日内帮您绑定助教。如有疑问，请联系客服电话400-000-5666（周一至周五 09:00 - 20:00）。\n";
        helpList.add(helpBean);

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpName(){
        HelpSearcher helpSearcher = new HelpSearcher();
        helpSearcher.id=3;
        helpSearcher.title ="实名认证";
        List<HelpBean> helpList = new ArrayList<>();

        HelpBean helpBean = new HelpBean();
        helpBean.id=1;
        helpBean.title = "如何进行实名认证？";
        helpBean.context="手机号码注册APP后，点击【首页】-“马上去认证”，进入实名认证中心，填写您的真实姓名和上传身份证正反面照片及本人手持身份证照片，" +
                "手持身份证照片要求 “本人五官及身份证信息清晰可见”。审核时间约1-2个工作日。\n";
        helpList.add(helpBean);

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpSettingCourse(){
        HelpSearcher helpSearcher = getHelpCourse();
        List<HelpBean> helpList = new ArrayList<>();
        helpList.add(helpSearcher.helpList.get(helpSearcher.helpList.size()-1));
        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }

    public HelpSearcher getHelpDaoZhang(){
        HelpSearcher helpSearcher = getHelpWallet();
        helpSearcher.id=3;
        helpSearcher.title ="我的钱包";
        List<HelpBean> helpList = new ArrayList<>();

        helpList.add(helpSearcher.helpList.get(4));

        helpSearcher.helpList.clear();
        helpSearcher.helpList.addAll(helpList);
        return helpSearcher;
    }


}
