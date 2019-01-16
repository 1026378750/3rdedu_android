package com.shengzhe.disan.xuetangteacher.utils;

import com.main.disanxuelib.util.BaseStringUtils;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherPagerActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.course.CourseDetailFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCreateOneFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCreateThreeFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCreateTwoFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.HomeFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OfflineItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveCreateThreeFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveCreateTwoFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveDetailFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnliveItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.mine.MineFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.mine.OrderItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.schedule.ScheduleFragment;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.AddBankCardActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.BankCardActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.BindingAssistantActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.ClassCreateActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.CourseDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.EditCourseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.LoginActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MIneLiveOrderDetailsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineCenterActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineDollarsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MineOfflineOrderActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MinesSistantActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveCreateActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.StartClassActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherExperienceActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.UserBaseActivity;

/**
 * Created by Administrator on 2017/12/5.
 */

public class StringUtils extends BaseStringUtils {

    //Activity中需要注册RXBUS事件的类
    public static final String RxBusActivityNames = MineCenterActivity.class.getName()
            +TeacherExperienceActivity.class.getName()
            +OnLiveCreateActivity.class.getName()
            +OfflineDetailActivity.class.getName()
            +StartClassActivity.class.getName()
            +LoginActivity.class.getName()
            +UserBaseActivity.class.getName()
            +MineOfflineOrderActivity.class.getName()
            +MIneLiveOrderDetailsActivity.class.getName()
            +OnLiveDetailActivity.class.getName()
            +BindingAssistantActivity.class.getName()
            +MinesSistantActivity.class.getName()
            +AddBankCardActivity.class.getName()
            +BankCardActivity.class.getName()
            +MineDollarsActivity.class.getName()
            +ClassCreateActivity.class.getName()
            +CourseDetailActivity.class.getName()
            +EditCourseActivity.class.getName()
            +TeacherPagerActivity.class.getName()
            +TeacherNewPagerActivity.class.getName()
            ;

    //Fragment中需要注册RXBUS事件的类
    public static final String EventBusFragmentNames = HomeFragment.class.getName()
                + ScheduleFragment.class.getName()
                + OnliveItemFragment.class.getName()
                + OfflineItemFragment.class.getName()
                + OnLiveCreateTwoFragment.class.getName()
                + OnLiveCreateThreeFragment.class.getName()
                + OnLiveDetailFragment.class.getName()
                + OrderItemFragment.class.getName()
                + MineFragment.class.getName()
                + ClassCreateTwoFragment.class.getName()
                + ClassCreateThreeFragment.class.getName()
                + CourseDetailFragment.class.getName()
                +ClassItemFragment.class.getName()
            ;
}
