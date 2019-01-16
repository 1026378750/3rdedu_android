package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCreateOneFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCreateThreeFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassCreateTwoFragment;
import com.main.disanxuelib.bean.ClassCourseBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.MyViewPager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/******
 * 添加线下班课
 *
 * liukui 2018/03/30
 *
 */
public class ClassCreateActivity extends BaseActivity {
    @BindView(R.id.vp_classoperator)
    MyViewPager mViewPager;

    String[] titles = new String[]{"线下班课 第一步", "线下班课 第二步", "线下班课 第三步"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    private ClassCourseBean data;
    private ConfirmDialog dialog;

    private ClassCreateOneFragment oneFragment;
    private ClassCreateTwoFragment twoFragment;
    private ClassCreateThreeFragment threeFragment;

    @Override
    public void initData() {
        oneFragment = ClassCreateOneFragment.newInstance(data);
        twoFragment = ClassCreateTwoFragment.newInstance(data);
        threeFragment = ClassCreateThreeFragment.newInstance(data);
        mFragmentList.add(0, oneFragment);
        mFragmentList.add(1, twoFragment);
        mFragmentList.add(2, threeFragment);

        mViewPager.setCurrentItem(index);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, titles));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_classcreate;
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

    private int index = 0;

    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11040:
                data = bundle.getParcelable(StringUtils.EVENT_DATA);
                index = bundle.getInt(StringUtils.EVENT_DATA2);
                if (index == 1) {
                    twoFragment.setClassCourseBean(data);
                    mViewPager.setCurrentItem(1);
                }
                if (index == 2) {
                    threeFragment.setClassCourseBean(data);
                    mViewPager.setCurrentItem(3);
                }
                break;
        }
        return false;
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

        if(data==null||(TextUtils.isEmpty(data.pictureUrl)&&
                TextUtils.isEmpty(data.courseName)&&
                TextUtils.isEmpty(data.gradeName)&&
                TextUtils.isEmpty(data.addressStr)&&
                data.mInlimit==-1&&
                data.address==null&&
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

}
