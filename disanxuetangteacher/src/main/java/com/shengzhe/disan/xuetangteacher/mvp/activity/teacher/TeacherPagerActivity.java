package com.shengzhe.disan.xuetangteacher.mvp.activity.teacher;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.adapter.ViewPageIconAdapter;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.bean.ShareBean;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.ShareUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.ClassItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OfflineItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.OnliveItemFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.mine.TeacherMessageFragment;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherInfo;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 老师主页 on 2018/1/13.
 */

public class TeacherPagerActivity extends BaseActivity implements ShareUtil.ShareSelectListener{
    @BindView(R.id.ab_teacherpager)
    AppBarLayout mToolLayout;
    @BindView(R.id.ccb_teacher_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.iv_teacher_bg)
    ImageView mTeacherBg;
    @BindView(R.id.iv_teacher_image)
    ImageView mTeacherImage;
    @BindView(R.id.tv_teacher_name)
    TextView mTeacherName;
    @BindView(R.id.tv_teacher_message)
    TextView mMessage;
    @BindView(R.id.iv_quality_certification)
    ImageView mQuality;
    @BindView(R.id.iv_realname_certification)
    ImageView mRealName;
    @BindView(R.id.iv_teacher_certification)
    ImageView mTeacher;
    @BindView(R.id.iv_education_certification)
    ImageView mEducation;
    @BindView(R.id.tv_teacher_number)
    TextView mNnumber;
    @BindView(R.id.tv_teacher_ordernum)
    TextView mOrderNum;
    @BindView(R.id.tv_teacher_timer)
    TextView mTimer;
    @BindView(R.id.pst_teacher_title)
    PagerSlidingTabStrip mTabLayout;
    @BindView(R.id.vp_teacher_content)
    ViewPager mViewPager;
    @BindView(R.id.teachercdl)
    CoordinatorLayout teacherCdl;
    private SystemPersimManage manage = null;
    private ShareUtil shareUtil;

    int[] icn = new int[]{R.drawable.teacher_radio_mine, R.drawable.teacher_radio_one,R.drawable.teacher_radio_two,R.drawable.teacher_radio_onlive};
    String[] title = new String[]{"课程详情", "线下一对一", "线下班课","直播课"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private float lastAlpha = 0;

    @Override
    public void initData() {
        mFragmentList.add(0, TeacherMessageFragment.newInstance());
        mFragmentList.add(1, OfflineItemFragment.newInstance(TeacherPagerActivity.class.getName()));
        mFragmentList.add(2, ClassItemFragment.newInstance(TeacherPagerActivity.class.getName()));
        mFragmentList.add(3, OnliveItemFragment.newInstance(TeacherPagerActivity.class.getName()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mViewPager.setAdapter(new ViewPageIconAdapter(getSupportFragmentManager(), mFragmentList, icn));
        else
            mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), mFragmentList, title));


        mTabLayout.setViewPager(mViewPager);
        mToolLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int dy) {
                float alpha = (float) Math.abs(dy)/ mTeacherBg.getMeasuredHeight();
                alpha = alpha>1?1:alpha;
                if (lastAlpha==alpha)
                    return;
                lastAlpha = alpha;
                setCCBTitleAlpha(lastAlpha);
            }
        });
    }

    private void setCCBTitleAlpha(float alpha) {
        if (alpha > 0.6) {
            mTitle.setLeftButton(R.mipmap.ic_black_left_arrow);
            mTitle.setBGColor(R.color.color_ffffff);
            mTitle.setRightButton(R.mipmap.abc_ic_menu_share_pressed);
        } else {
            mTitle.setLeftButton(R.mipmap.ic_white_left_arrow);
            mTitle.setBGColor(R.color.color_00000000);
            mTitle.setRightButton(R.mipmap.abc_ic_menu_share_alpha);
        }
        mTitle.setViewAlpha(alpha);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_teacherpager;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                //分享
                shareUtil = ShareUtil.getInsatnce();
                shareUtil.setShareLogo(R.mipmap.ic_launcher_logo)
                        .initDatas(mContext, getSupportFragmentManager())
                        .setExtoryBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.share_parent))
                        .show(new ShareBean(0,"第三学堂老师分享","","","",mTabLayout.getCurrentPosition()))
                        .setShareSelectListener(TeacherPagerActivity.this);
                break;

        }
    }


    public void setPrameter(TeacherInfo teacherInfo){
        if (teacherInfo==null)
            return;
        mTeacherName.setText(teacherInfo.getTeacherName());
        mTitle.setTitleText(teacherInfo.getTeacherName());
        if (teacherInfo.getIdentity()>0)
            ImageUtil.setCompoundDrawable(mTeacherName,16,R.mipmap.ic_teacher_launcher,Gravity.RIGHT,0);

        ImageUtil.loadCircleImageView(mContext, teacherInfo.getPhotoUrl(), mTeacherImage, R.mipmap.ic_personal_avatar);
        if(teacherInfo==null){
            mMessage.setText("未填写");
        }else {
            String teacherAge="";
            if(teacherInfo.getTeachingAge()!=0){
                teacherAge+= " | " + teacherInfo.getTeachingAge() + "年教龄";
            }
            mMessage.setText(StringUtils.getSex(teacherInfo.getSex()) +" | "+teacherInfo.getGradeName()+" "+teacherInfo.getSubjectName()+teacherAge);
        }

        mQuality.setVisibility(teacherInfo.getIpmpStatus() == 2 ? View.VISIBLE : View.GONE);
        mRealName.setVisibility(teacherInfo.getCardApprStatus() != 0 ? View.VISIBLE : View.GONE);
        mTeacher.setVisibility(teacherInfo.getQtsStatus() == 2 ? View.VISIBLE : View.GONE);
        mEducation.setVisibility(teacherInfo.getQuaStatus() == 2 ? View.VISIBLE : View.GONE);
        HomeBean homebean=SharedPreferencesManager.getHomeBean();
        mNnumber.setText(String.valueOf(homebean.getStudentNum()<=0?0:homebean.getStudentNum()));
        mOrderNum.setText(String.valueOf(homebean.getOrderNum()<=0?0:homebean.getOrderNum()));
        mTimer.setText(String.valueOf(homebean.getTimeLong()<=0? 0:homebean.getTimeLong()));
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                shareUtil.shareImage(mesg);
            } else {
                showPremissionDialog("访问设备存储空间");
            }
        }
    }

    @Override
    public void selectShareItem(String mesg) {
        ToastUtil.showShort(mesg);
    }

    private String mesg = "";
    @Override
    public void selectLoggPicture(String mMesg) {
        this.mesg = mMesg;
        if (manage == null)
            manage = new SystemPersimManage(mContext) {
                @Override
                public void resultPerm(boolean isCan, int requestCode) {
                    if (isCan)
                        shareUtil.shareImage(mesg);
                }
            };
        manage.CheckedFile();
    }
}
