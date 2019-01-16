package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveCreateOneFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveCreateThreeFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnLiveCreateTwoFragment;
import com.shengzhe.disan.xuetangteacher.bean.OnliveCourseBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.MyViewPager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 创建在线直播课 on 2018/1/16.
 */

public class OnLiveCreateActivity extends BaseActivity {
    @BindView(R.id.vp_onliveoperator)
    MyViewPager mViewPager;

    String[] titles = new String[]{"在线直播课 第一步", "在线直播课 第二步","在线直播课 第三步"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    private OnliveCourseBean data;
    private ConfirmDialog dialog;

    private OnLiveCreateOneFragment oneFragment;
    private OnLiveCreateTwoFragment twoFragment;
    private OnLiveCreateThreeFragment threeFragment;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        data = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        if(data ==null){
            data = new OnliveCourseBean();
        }
        oneFragment = OnLiveCreateOneFragment.newInstance(data);
        twoFragment = OnLiveCreateTwoFragment.newInstance(data);
        threeFragment =  OnLiveCreateThreeFragment.newInstance(data);
        mFragmentList.add(0, oneFragment);
        mFragmentList.add(1,twoFragment);
        mFragmentList.add(2,threeFragment);

        mViewPager.setCurrentItem(index);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, titles));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_onlivecreate;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(index>0) {
            mViewPager.setCurrentItem(--index);
            if(index==1){
                twoFragment.setConfirmCheckable();
            }
            return;
        }

        if(data==null|| (TextUtils.isEmpty(data.pictureUrl)&&
                TextUtils.isEmpty(data.courseName)&&
                TextUtils.isEmpty(data.gradeName)&&
                TextUtils.isEmpty(data.directTypeName)&&
                data.salesVolume==0&&
                data.courseTimes==0&&
                data.singleTime==0&&
                TextUtils.isEmpty(data.introduction)&&
                TextUtils.isEmpty(data.target)&&
                TextUtils.isEmpty(data.fitCrowd))
                ){
            finish();
            return;
        }
        if(dialog==null) {
            dialog = ConfirmDialog.newInstance("", "您还没有保存，确定要退出？", "取消", "确定");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

            @Override
            public void dialogStatus(int id) {
                switch (id){
                    case R.id.tv_dialog_ok:
                        colseDialog();
                        finish();
                        break;
                }
            }
        });
    }

    private void colseDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dismiss();
        }
    }

    private int index = 0;
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11021:
                data = bundle.getParcelable(StringUtils.EVENT_DATA);
                index = bundle.getInt(StringUtils.EVENT_DATA2);
                if(index==1) {
                    twoFragment.setOnliveCourseBean(data);
                    mViewPager.setCurrentItem(1);
                }if(index==2) {
                    threeFragment.setOnliveCourseBean(data);
                    mViewPager.setCurrentItem(3);
                }
                break;
        }
        return false;
    }
}
