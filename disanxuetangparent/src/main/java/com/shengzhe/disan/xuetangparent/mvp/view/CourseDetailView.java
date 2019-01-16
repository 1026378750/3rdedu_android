package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.CourseDetailFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.CourseOutlineFragment;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineLessonOrderActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 课程详情 on 2018/4/20.
 */

public class CourseDetailView extends BaseView {
    String[] titles = new String[]{"详情", "大纲"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private float lastAlpha = 0;
    private int courseId = 0;
    private CourseSquadBean courseSquadSchedule;
    private boolean showBuy;
    private boolean upperFrame;

    private FragmentManager fragmentManager;

    public CourseDetailView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    public void initDatas(Intent intent) {
        courseId = intent.getIntExtra(StringUtils.COURSE_ID, 0);
        showBuy = intent.getBooleanExtra(StringUtils.FRAGMENT_DATA, false);
        upperFrame = intent.getBooleanExtra(StringUtils.select_payStatus, false);
        mFragmentList.add(0, CourseDetailFragment.getInstance(courseId));
        mFragmentList.add(1, CourseOutlineFragment.getInstance(courseId));

        iView.getViewPagerView().setCurrentItem(0);
        iView.getViewPagerView().setOffscreenPageLimit(2);
        iView.getViewPagerView().setAdapter(new ViewPageFragmentAdapter(fragmentManager, mFragmentList, titles));
        iView.getTabLayoutView().setViewPager(iView.getViewPagerView());
        iView.getToolLayoutView().addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int dy) {
                float alpha = (float) Math.abs(dy) / iView.getCourseBgView().getMeasuredHeight();
                alpha = alpha > 1 ? 1 : alpha;
                if (lastAlpha == alpha)
                    return;
                lastAlpha = alpha;
                setCCBTitleAlpha(lastAlpha);
            }
        });
    }

    private void setCCBTitleAlpha(float alpha) {
        if (alpha>0.8) {
            iView.getTitleView().setLeftButton(R.mipmap.ic_black_left_arrow);
            iView.getTitleView().setBGColor(R.color.color_ffffff);
            iView.getTitleView().setTitleTextColor(R.color.color_333333);
        } else {
            iView.getTitleView().setLeftButton(R.mipmap.ic_white_left_arrow);
            iView.getTitleView().setBGColor(R.color.color_00000000);
            iView.getTitleView().setTitleTextColor(R.color.color_00000000);
        }
    }

    private ICourseDetailView iView;
    public void setICourseDetailView(ICourseDetailView iView){
        this.iView = iView;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setResultDatas(CourseSquadBean courseBean) {
        if (courseBean != null) {
            courseSquadSchedule = courseBean;
            setPrameter(courseSquadSchedule);
        }
    }

    private CourseSquadBean courseBean;
    private void setPrameter(CourseSquadBean courseBean) {
        if (courseBean == null) {
            this.courseBean = new CourseSquadBean();
            return;
        }
        this.courseBean = courseBean;
        iView.getNameView().setText(courseBean.courseName);
        iView.getNumberView().setText(StringUtils.textFormatHtml("<font color='#1d97ea'>" + courseBean.salesVolume + "</font>" + "/" + courseBean.maxUser + "人"));
        iView.getDetailsView().setText(courseBean.subjectName + " " + courseBean.gradeName);
        iView.getTimeView().setText(courseBean.duration + "小时/次，共" + courseBean.classTime + "次");
        iView.getPriceView().setText(StringUtils.textFormatHtml("<font color='#FFAE12'>" + "¥" + ArithUtils.round(courseBean.totalPrice) + "</font>"));
        iView.getAddressView().setText(courseBean.cityName + " " + courseBean.address);
        iView.getTeacherNameView().setText(courseBean.teacherName);
        ImageUtil.loadImageViewLoding(mContext, courseBean.pictureUrl, iView.getCourseBgView(), R.mipmap.default_error, R.mipmap.default_error);

        ImageUtil.loadCircleImageView(mContext, courseBean.getTeacherPhotoUrl(), iView.getImageView(), R.mipmap.ic_personal_avatar);
        if (TextUtils.isEmpty(courseBean.getGradeName())) {
            iView.getMessageView().setText("未填写");
        } else {
            iView.getMessageView().setText(StringUtils.getSex(courseBean.getSex()) + " | " + courseBean.getGradeName() + " " + courseBean.getSubjectName() + " | " + courseBean.getTeachingAge() + "年教龄");
        }
        iView.getQualityView().setVisibility(courseBean.getIpmpStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getRealnameView().setVisibility(courseBean.getCardApprStatus() != 0 ? View.VISIBLE : View.GONE);
        iView.getTeacherView().setVisibility(courseBean.getQtsStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getEducationView().setVisibility(courseBean.getQuaStatus() == 2 ? View.VISIBLE : View.GONE);
        if (showBuy) {
            iView.getCoureseditailView().setVisibility(View.GONE);
        } else {
            setBtnStatus(courseBean);
        }
    }

    private long Payprice = 0l, Preprice = 0l;
    private String times = "";

    /**
     * 插班 无优惠  购买课次X单次价格=实际价格
     * 插班时 底部可购买   课次=总课次-已完成课次
     *
     * @param courseBean
     */
    public void setBtnStatus(CourseSquadBean courseBean) {
        iView.getCreatescheduleConfirmView().setVisibility(View.VISIBLE);
        if (courseBean.areadyCourseClass == 0 && (courseBean.isAreadyBuy == 1 && courseBean.salesVolume != courseBean.maxUser)) {
            iView.getCreatescheduleConfirmView().setText("立即报名");
            iView.getCreatescheduleConfirmView().setEnabled(true);
            if(courseBean.canJoin == 1){
                if(courseBean.getCampusDiscountId() != 0) {
                    Payprice = courseBean.getDiscountTotalPrice();
                    Preprice = courseBean.totalPrice;
                }else {
                    Payprice = courseBean.totalPrice;
                }
            }else {
                if(courseBean.getCampusDiscountId() != 0) {
                    Payprice = courseBean.getDiscountTotalPrice();
                    Preprice = courseBean.totalPrice;
                } else {
                    Payprice = courseBean.totalPrice;
                    // Preprice = courseBean.totalPrice;
                }
            }
            times = courseBean.classTime + "";

        } else if (courseBean.isAreadyBuy == 2) {
            //已购买过该课程,  显示实际支付的价格和,     购买的次数
            iView.getCreatescheduleConfirmView().setText("已报名");
            iView.getCreatescheduleConfirmView().setEnabled(false);
            if (courseBean.canJoin == 1) {
                Payprice = courseBean.surplusCoursePrice;
                times = courseBean.surplusCourseClass + "";
            } else {
                Payprice = courseBean.totalPrice;
                times = courseBean.classTime + "";
            }

        } else if (courseBean.salesVolume == courseBean.maxUser) {
            iView.getCreatescheduleConfirmView().setText("名额已满");
            iView.getCreatescheduleConfirmView().setEnabled(false);
            //是否可以插班 0否1是
            if (courseBean.canJoin == 1) {
                times = courseBean.surplusCourseClass + "";//剩余课次数
                Payprice = courseBean.surplusCoursePrice;//剩余课次总价
            } else {
                if (courseBean.getCampusDiscountId() != 0){
                    Payprice =courseBean.getDiscountTotalPrice();//显示优惠价格 ，总课次 ，原价
                    Preprice=courseBean.totalPrice;
                    times = courseBean.classTime + "";//总课次
                }else {
                    Payprice = courseBean.totalPrice;//显示优惠价格 ，总课次 ，原价
                    times = courseBean.classTime + "";//总课次
                }

            }

        } else if (courseBean.areadyCourseClass > 0 && (courseBean.canJoin != 1)) {
            //已授课节数 （用于判断立即报名/我要插班）
            //不可插班     显示优惠价格 ，总课次 ，原价      如无优惠则显示  原价    课次
            iView.getCreatescheduleConfirmView().setText("报名结束");
            iView.getCreatescheduleConfirmView().setEnabled(false);
            if(courseBean.canJoin==0){
                if (courseBean.getCampusDiscountId() != 0){
                    Payprice = courseBean.discountTotalPrice;
                    // Preprice = courseBean.totalPrice;
                }else {
                    Payprice = courseBean.totalPrice;
                    // Preprice = courseBean.totalPrice;
                }
            }else {

            }

            times = courseBean.classTime + "";//总课次
        } else {
            //是否可以插班 0否1是
            if (courseBean.canJoin == 1) {
                //插班 无优惠  购买课次X单次价格=实际价格
                iView.getCreatescheduleConfirmView().setText("我要插班");
                iView.getCreatescheduleConfirmView().setEnabled(true);
                Payprice = courseBean.surplusCoursePrice;
                times = courseBean.surplusCourseClass + "";

            }
        }
        //是否已经购买过该课程 1未购买 2已购买
        if (courseBean.campusDiscountId >= 1 && courseBean.canJoin == 0 ) {
            Payprice = courseBean.getDiscountTotalPrice();
        }
        if (upperFrame) {
            iView.getCreatescheduleConfirmView().setText("已下架");
            iView.getCreatescheduleConfirmView().setEnabled(false);
        }
        iView.getCreateschedulePaypriceView().setText("¥" + ArithUtils.round(Payprice));
        if (Preprice == 0) {
            iView.getCreateschedulePrepriceView().setText("共" + times + "课");
        } else {
            iView.getCreateschedulePrepriceView().setText("共" + times + "课, ¥");
            iView.getCreateschedulePriceView().setText(ArithUtils.round(Preprice));
            iView.getCreateschedulePriceView().getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public int getTeacherId() {
        return courseBean.getTeacherId();
    }

    public void goOfflineLessonOrder() {
        Intent intent = new Intent();
        intent.setClass(mContext, OfflineLessonOrderActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_DATA, courseSquadSchedule);
        intent.putExtra("Payprice", Payprice);
        intent.putExtra("Preprice", Preprice);
        intent.putExtra("times", times);
        intent.putExtra("courseId", courseId);
        mContext.startActivity(intent);
    }

    public interface ICourseDetailView{
        AppBarLayout getToolLayoutView();
        CommonCrosswiseBar getTitleView();
        ImageView getCourseBgView();
        TextView getNameView();
        TextView getNumberView();
        TextView getDetailsView();
        TextView getTimeView();
        TextView getPriceView();
        ImageView getImageView();
        TextView getTeacherNameView();
        ImageView getIsplantView();
        TextView getMessageView();
        ImageView getQualityView();
        ImageView getRealnameView();
        ImageView getTeacherView();
        ImageView getEducationView();


        TextView getAddressView();
        PagerSlidingTabStrip getTabLayoutView();
        ViewPager getViewPagerView();

        TextView getCreateschedulePaypriceView();
        TextView getCreateschedulePrepriceView();
        Button getCreatescheduleConfirmView();
        Toolbar getToolbarView();
        LinearLayout getCoureseditailView();
        LinearLayout getCourseHeadView();
        TextView getCreateschedulePriceView();
    }

}
