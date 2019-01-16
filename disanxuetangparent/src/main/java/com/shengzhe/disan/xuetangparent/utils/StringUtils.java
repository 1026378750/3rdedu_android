package com.shengzhe.disan.xuetangparent.utils;

import com.main.disanxuelib.util.BaseStringUtils;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineBuyCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OpenCityActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineOneonOneDetailsActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OrderPayActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.PayResultActivity;
import com.shengzhe.disan.xuetangparent.fragment.ScreenConditionFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.MineOrderItemFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineClassFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineOneOnOneFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.home.HomeFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.MineFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass.OnLiveFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass.VideoItemFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.AddBankCardActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.ApplyAuditionActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.BankCardActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.CreateScheduleActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.LiveCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineOrderActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.ModifyCalendarActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.ScheduleCalendarActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.SelectSchooltimeActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.UserMessageActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.VideoDeatilActivity;

/**
 * Created by Administrator on 2017/12/5.
 */

public class StringUtils extends BaseStringUtils {

    //Activity中需要注册RXBUS事件的类
    public static final String RxBusActivityNames = SelectSchooltimeActivity.class.getName()
            +ScheduleCalendarActivity.class.getName()
            +CreateScheduleActivity.class.getName()
            +OpenCityActivity.class.getName()
            +MainActivity.class.getName()
            +LiveCourseActivity.class.getName()
            +VideoDeatilActivity.class.getName()
            +OfflineTeacherActivity.class.getName()
            +OfflineBuyCourseActivity.class.getName()
            +UserMessageActivity.class.getName()
            +PayResultActivity.class.getName()
            +MineOrderActivity.class.getName()
            +OrderPayActivity.class.getName()
            +ModifyCalendarActivity.class.getName()
            +BankCardActivity.class.getName()
            + AddBankCardActivity.class.getName()
            +ApplyAuditionActivity.class.getName()
            ;

    //Fragment中需要注册RXBUS事件的类
    public static final String EventBusFragmentNames = HomeFragment.class.getName()
            + OfflineOneOnOneFragment.class.getName()
            + OnLiveFragment.class.getName()
            + VideoItemFragment.class.getName()
            + MineFragment.class.getName()
            + ScreenConditionFragment.class.getName()
            + MineOrderItemFragment.class.getName()
            + OfflineClassFragment.class.getName()
            ;
}
