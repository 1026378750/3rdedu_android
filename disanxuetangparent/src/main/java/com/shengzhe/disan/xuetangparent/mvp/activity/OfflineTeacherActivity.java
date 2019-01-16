package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.bean.ShareBean;
import com.main.disanxuelib.util.ShareUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.TeaHomePageBean;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineTeacherView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.umeng.socialize.UMShareAPI;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 线下一对一老师主页
 * <p>
 * liukui
 */
public class OfflineTeacherActivity extends BaseActivity implements OfflineTeacherView.IOfflineTeacherView , ShareUtil.ShareSelectListener{
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

    private CoursePresenter presenter;
    private SystemPersimManage manage = null;
    private ShareUtil shareUtil;

    @Override
    public void initData() {
        int teacherId = getIntent().getIntExtra(StringUtils.TEACHER_ID, 0);
        if (presenter == null)
            presenter = new CoursePresenter(mContext, this);
        presenter.initOfflineTeacherUi(getSupportFragmentManager());
        presenter.initOfflineTeacherDatas(teacherId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_teacherpager;
    }

    public void setPrameter(TeaHomePageBean teacherInfo) {
        presenter.setOfflineTeacherDatas(teacherInfo);
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                shareUtil = ShareUtil.getInsatnce();
                shareUtil.setShareLogo(R.mipmap.ic_launcher_logo)
                        .initDatas(mContext, getSupportFragmentManager())
                        .setExtoryBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.share_parent))
                        .show(new ShareBean(0,"第三学堂分享老师","","","",mTabLayout.getCurrentPosition()))
                        .setShareSelectListener(OfflineTeacherActivity.this);
                break;
        }
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
    public AppBarLayout getToolLayoutView() {
        return mToolLayout;
    }

    @Override
    public CommonCrosswiseBar getTitleView() {
        return mTitle;
    }

    @Override
    public ImageView getTeacherBgView() {
        return mTeacherBg;
    }

    @Override
    public ImageView getTeacherImageView() {
        return mTeacherImage;
    }

    @Override
    public TextView getTeacherNameView() {
        return mTeacherName;
    }

    @Override
    public TextView getMessageView() {
        return mMessage;
    }

    @Override
    public ImageView getQualityView() {
        return mQuality;
    }

    @Override
    public ImageView getRealNameView() {
        return mRealName;
    }

    @Override
    public ImageView getTeacherView() {
        return mTeacher;
    }

    @Override
    public ImageView getEducationView() {
        return mEducation;
    }

    @Override
    public TextView getNnumberView() {
        return mNnumber;
    }

    @Override
    public TextView getOrderNumView() {
        return mOrderNum;
    }

    @Override
    public TextView getTimerView() {
        return mTimer;
    }

    @Override
    public PagerSlidingTabStrip getTabLayoutView() {
        return mTabLayout;
    }

    @Override
    public ViewPager getViewPagerView() {
        return mViewPager;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
