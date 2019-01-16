package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.banner.BannerBean;
import com.main.disanxuelib.view.banner.MyBanner;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.ClassCreateActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.IdentityCardActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineCenterActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MinesSistantActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineOperatorActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveCreateActivity;
import com.shengzhe.disan.xuetangteacher.mvp.model.HomeModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangteacher.mvp.view.IHomeView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.List;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */
public class HomePresenter extends BasePresenter implements MVPRequestListener,MyBanner.BannerItemOnClickListener ,RefreshCommonView.RefreshLoadMoreListener {
    private IHomeView view;
    private HomeModelImpl modelImpl;
    private HomeBean homebean;

    public HomePresenter(Context context, IHomeView view) {
        super(context);
        this.view = view;
        if (modelImpl == null)
            modelImpl = new HomeModelImpl(context, this);
    }

    public void initDate() {
        view.getRefreshCommonView().setRefreshLoadMoreListener(this);
        view.getRefreshCommonView().setIsLoadMore(false);
    }

    //设置初始化数据
    private void setHomeBean() {
        // 资料完善状态 0 未完善资料，1 已完善资料
        String homestatus = "";
        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 0:
                homestatus = "您还没有实名认证,不能开课哦！<font color='#1D97EA'>马上去认证</font>";
                break;
            case 1:
                homestatus = "您的实名认证正在审核中,请您耐心等待！";
                break;
            case 2:
                homestatus = "您的实名认证已通过,可以开课啦！";
                break;
            case 3:
                homestatus = "您的实名认证未通过,<font color='#1D97EA'>请重新认证</font>";
                break;
        }

        if(homebean.getHomeStatus()==0){
            homestatus = "你还没有完善资料,不能开课哦！<font color='#1D97EA'>去完善</font>";
        }
        /* if(homebean.getAssistantId()==0&&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==2)){
            homestatus = "你还没有绑定助教,不能开课哦！<font color='#1D97EA'>去完善</font>";
        }
        */
        //realNameAuthStatusjudgeClazzStatus
        SharedPreferencesManager.setOpenCity(homebean.getIsOpenCity());
        SharedPreferencesManager.setHomeBean(homebean);
        view.getNotify().setVisibility(View.GONE);
        if (!StringUtils.textIsEmpty(homestatus)) {
            view.getNotify().setText(StringUtils.textFormatHtml(homestatus));
            view.getNotify().setVisibility(View.VISIBLE);
        }
        if(homebean.getStartCourseStatus()>0){
            view.getLayout().setVisibility(View.GONE);
        }
        ImageUtil.loadCircleImageView(mContext, homebean.getPhotoUrl(), view.getHeadImage(), R.mipmap.ic_personal_avatar);
        view.getNiceName().setText(homebean.getNickName());
        if (homebean.getIdentity() != 0)
            ImageUtil.setCompoundDrawable(view.getNiceName(), 14, R.mipmap.ic_teacher_launcher, Gravity.RIGHT,0);
        if(homebean.getSex()==-1){
            view.getMessage().setText("未填写");
        }else {
            String teacherAge="";
            if(homebean.getTeachingAge()!=0){
                teacherAge+= " | " + homebean.getTeachingAge() + "年教龄";
            }

            view.getMessage().setText(StringUtils.getSex(homebean.getSex()) + " | " + homebean.getSubjectName()+teacherAge );
            SharedPreferencesManager.setSubjectName(homebean.getSubjectName());
        }

        view.getOrderNum().setText(homebean.getOrderNum()<=0?"--":String.valueOf(homebean.getOrderNum()));
        view.getMineClass().setText(homebean.getStudentNum()<=0?"--":String.valueOf(homebean.getStudentNum()));
        view.getTimes().setText(homebean.getTimeLong()<=0?"--":String.valueOf(homebean.getTimeLong()));

    }

    /**
     *  判断状态
     */
    public boolean judgeStatus(){
        //消息关闭
    /*    if (homebean==null)
            return false;*/
        if(homebean.getHomeStatus()==0){
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,StringUtils.btn_is_next);
            mContext.startActivity(intent);
            return false;
        }

        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 1:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                   // context.startActivity(new Intent(context,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 0:
            case 3:
                //实名认证
                mContext.startActivity(new Intent(mContext,IdentityCardActivity.class));
                return false;
        }


        return true;
    }

    /**
     *  判断我要开课状态
     */
    public boolean judgeOpenStatus(){
     /*   //消息关闭
        if (homebean==null)
            return false;*/
        if(homebean.getHomeStatus()==0){
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,StringUtils.btn_is_next);
            mContext.startActivity(intent);
            return false;
        }

        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 1:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                     mContext.startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 2:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null)){
                    mContext.startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 0:
            case 3:
               /* if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    context.startActivity(new Intent(context,MinesSistantActivity.class));
                    return  false;
                }*/
                //实名认证
                mContext.startActivity(new Intent(mContext,IdentityCardActivity.class));
                return false;
        }

        return true;
    }

    /**
     *  判断在线直播课
     */
    public boolean judgeOpenLineStatus(){
     /*   //消息关闭
        if (homebean==null)
            return false;*/
        if(homebean.getHomeStatus()==0){
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,StringUtils.btn_is_next);
            mContext.startActivity(intent);
            return false;
        }

        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 1:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    mContext.startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 2:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null)){
                    mContext.startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 0:
            case 3:
               /* if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    context.startActivity(new Intent(context,MinesSistantActivity.class));
                    return  false;
                }*/
                //实名认证
                mContext.startActivity(new Intent(mContext,IdentityCardActivity.class));
                return false;
        }

        return true;
    }
    /**
     *  判断课程管理状态
     */
    public boolean judgeClazzStatus(){
        //消息关闭
        if (homebean==null)
            return false;
        if(homebean.getHomeStatus()==0){
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,StringUtils.btn_is_next);
            mContext.startActivity(intent);
            return false;
        }

        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 1:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    mContext.startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 0:
            case 2:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    mContext.startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 3:
                //实名认证
                mContext.startActivity(new Intent(mContext,IdentityCardActivity.class));
                return false;
        }

        return true;
    }
    /**
     *  判断老师主页状态
     */
    public boolean judgeTeacherStatus(){
     /*   //消息关闭
        if (homebean==null)
            return false;*/
        if(homebean.getHomeStatus()==0){
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,StringUtils.btn_is_next);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }

    public void setNotifyFloat() {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                //X轴初始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, -1.0f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, 0.0f);
        //3秒完成动画
        translateAnimation.setDuration(1000);
        //如果fillAfter的值为真的话，动画结束后，控件停留在执行后的状态
        animationSet.setFillAfter(true);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(translateAnimation);
        view.getLayout().startAnimation(animationSet);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.getLayout().setVisibility(View.GONE);
                view.getLayout().clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void bannerOnItemClick(View view, BannerBean banner) {

    }

    @Override
    public void startRefresh() {
        modelImpl.postBanner();
        modelImpl.postHomeData();
    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void onSuccess(int tager, Object objects) {
        switch (tager){
            case IntegerUtil.WEB_API_HomeData:
                view.getRefreshCommonView().finishRefresh();
                if(objects!=null){
                    homebean = (HomeBean)objects;
                    ConstantUrl.homebean = homebean;
                    setHomeBean();
                }
                break;

            case IntegerUtil.WEB_API_TeacherBanner:
                if(objects!=null) {
                    view.getHomeBanner().initPageIndex((List<BannerBean>) objects);
                }
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg) {
        switch (tager){
            case IntegerUtil.WEB_API_HomeData:
                view.getRefreshCommonView().finishRefresh();
                break;

            default:
                ToastUtil.showShort(mesg);
                break;
        }
    }




    private NiceDialog niceDialog;
    public void dialogCreateCourse() {
        ConstantUrl.CLIEN_Center=true;

        if (niceDialog == null) {
            niceDialog = NiceDialog.init();
        }
        niceDialog.setShowCancelBtn(false);
        niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
            @Override
            public void onItemListener(int index, String item) {
                ConstantUrl.IS_EDIT=true;
                switch (index){
                    case 0:
                        //线下1对1
                        if(!judgeOpenStatus()) return;

                        if (SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIsStartCourse()==1){
                            listenerjudge.presenterjudgeClick("");
                        } else {
                            mContext.startActivity(new Intent(mContext, OfflineOperatorActivity.class));
                        }

                        break;

                    case 1:
                        //线下班课
                        if(!judgeOpenStatus()) return;
                        mContext.startActivity(new Intent(mContext, ClassCreateActivity.class));
                        break;

                    case 2:
                        //在线直播课
                        if(!judgeOpenLineStatus()) return;
                        mContext.startActivity(new Intent(mContext, OnLiveCreateActivity.class));
                        break;

                }
            }
        });
        niceDialog.setCommonLayout(ContentUtil.selectSelectClazz(), false, view.getFragmentManagers());
    }

    public void postHomeData() {
        modelImpl.postHomeData();
    }
}
