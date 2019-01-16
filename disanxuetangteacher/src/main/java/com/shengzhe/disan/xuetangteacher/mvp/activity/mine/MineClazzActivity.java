package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.ClassCreateActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.IdentityCardActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineOperatorActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveCreateActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.StartClassActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OfflineItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnliveItemFragment;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 我的课程管理 on 2018/1/17.
 */

public class MineClazzActivity extends BaseActivity {
    @BindView(R.id.pst_mineclazz_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_mineclazz_item)
    ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    @Override
    public void initData() {
        mFragmentList.add(0, OfflineItemFragment.newInstance(MineClazzActivity.class.getName()));
        mFragmentList.add(1, ClassItemFragment.newInstance(MineClazzActivity.class.getName()));
        mFragmentList.add(2, OnliveItemFragment.newInstance(MineClazzActivity.class.getName()));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, ContentUtil.selectSelectClazz()));
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mineclazz;
    }

    private NiceDialog niceDialog;
    private ConfirmDialog dialog;

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                ConstantUrl.CLIEN_Center = true;
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setShowCancelBtn(false);
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        switch (index) {
                            case 0:
                                //线下1对1
                                if (!judgeClazzStatus()) return;
                                if (SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIsStartCourse()==1){
                                    startOpenClass();
                                } else {
                                    startActivity(new Intent(mContext, OfflineOperatorActivity.class));
                                }
                                break;

                            case 1:
                                //线下班课
                                if (!judgeClazzStatus()) return;
                                startActivity(new Intent(mContext, ClassCreateActivity.class));
                                break;

                            case 2:
                                //在线直播课
                                if (!judgeLiveClazzStatus()) return;
                                startActivity(new Intent(mContext, OnLiveCreateActivity.class));
                                break;
                        }
                    }
                });
                niceDialog.setCommonLayout(ContentUtil.selectSelectClazz(), false, getSupportFragmentManager());
                break;
        }
    }

    private ConfirmDialog confirmDialog;
    public void startOpenClass(){
        //开课设置
        if ((SharedPreferencesManager.getUserInfo() != null) && SharedPreferencesManager.getOpenCity() <= 1) {

            confirmDialog = ConfirmDialog.newInstance("", "您当前所选的城市未开通，不能填写开课设置您可以联系客服修改城市", "取消", "联系客服");
            confirmDialog.setMargin(60)
                    .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                    .setOutCancel(false)
                    .show(getSupportFragmentManager());

            confirmDialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void dialogStatus(int id) {
                    switch (id) {
                        case R.id.tv_dialog_ok:
                            SystemInfoUtil.callDialing("400005666");
                            break;
                    }
                }
            });
            return;
        }
            mContext.startActivity(new Intent(mContext, StartClassActivity.class));

    }
    private HomeBean homebean;

    /**
     * 判断课程管理状态
     */
    public boolean judgeClazzStatus() {

        homebean = SharedPreferencesManager.getHomeBean();
        //消息关闭
     /*   if (homebean == null)
            return false;*/
        if (homebean.getHomeStatus() == 0) {
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA, StringUtils.btn_is_next);
            startActivity(intent);
            return false;
        }

        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 1:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 2:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null)){
                  startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 0:
            case 3:
               /* if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }*/
                //实名认证
                startActivity(new Intent(mContext, IdentityCardActivity.class));
                return false;
        }

        return true;
    }

    /**
     * 判断在线直播课
     */
    public boolean judgeLiveClazzStatus() {

        homebean = SharedPreferencesManager.getHomeBean();
        //消息关闭
       /* if (homebean == null)
            return false;*/
        if (homebean.getHomeStatus() == 0) {
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA, StringUtils.btn_is_next);
            startActivity(intent);
            return false;
        }

        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (homebean.getRealNameAuthStatus()) {
            case 1:
                if (homebean.getAssistantId() == 0 && (SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIsStartCourse() == 1)) {
                    startActivity(new Intent(mContext, MinesSistantActivity.class));
                    return false;
                }
                break;
            case 2:
                if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null)){
                    startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }
                break;
            case 0:
            case 3:
               /* if(homebean.getAssistantId()==0 &&(SharedPreferencesManager.getUserInfo() != null&& SharedPreferencesManager.getUserInfo().getIsStartCourse()==1)){
                    startActivity(new Intent(mContext,MinesSistantActivity.class));
                    return  false;
                }*/
                //实名认证
                startActivity(new Intent(mContext, IdentityCardActivity.class));
                return false;
        }

        return true;
    }
}
