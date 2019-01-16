package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.ViewPageFragmentAdapter;
import com.main.disanxuelib.adapter.ViewPageIconAdapter;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.PagerSlidingTabStrip;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.ClassItemFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.teacher.OfflineItemFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.teacher.OnliveItemFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.teacher.TeacherMessageFragment;
import com.shengzhe.disan.xuetangparent.bean.TeaHomePageBean;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class OfflineTeacherView extends BaseView {
    private FragmentManager fragmentManager;
    int[] icn = new int[]{R.drawable.teacher_radio_mine, R.drawable.teacher_radio_one, R.drawable.teacher_radio_two,R.drawable.teacher_radio_onlive};
    String[] title = new String[]{"课程详情", "线下一对一", "线下班课","直播课"};
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private float lastAlpha = 0;

    public OfflineTeacherView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    public void initDatas(int teacherId){
        mFragmentList.add(0, TeacherMessageFragment.newInstance(teacherId));
        mFragmentList.add(1, OfflineItemFragment.newInstance(teacherId));
        mFragmentList.add(2, ClassItemFragment.newInstance(teacherId));
        mFragmentList.add(3, OnliveItemFragment.newInstance(teacherId));
        iView.getViewPagerView().setCurrentItem(0);
        iView.getViewPagerView().setOffscreenPageLimit(4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            iView.getViewPagerView().setAdapter(new ViewPageIconAdapter(fragmentManager, mFragmentList, icn));
        else
            iView.getViewPagerView().setAdapter(new ViewPageFragmentAdapter(fragmentManager, mFragmentList, title));

        iView.getTabLayoutView().setViewPager(iView.getViewPagerView());
        iView.getToolLayoutView().addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int dy) {
                float alpha = (float) Math.abs(dy)/iView.getTeacherBgView().getMeasuredHeight();
                alpha = alpha>1?1:alpha;
                if (lastAlpha==alpha)
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
            iView.getTitleView().setRightButton(R.mipmap.abc_ic_menu_share_pressed);
        } else {
            iView.getTitleView().setLeftButton(R.mipmap.ic_white_left_arrow);
            iView.getTitleView().setBGColor(R.color.color_00000000);
            iView.getTitleView().setTitleTextColor(R.color.color_00000000);
            iView.getTitleView().setRightButton(R.mipmap.abc_ic_menu_share_alpha);
        }
    }

    private IOfflineTeacherView iView;
    public void setIOfflineTeacherView(IOfflineTeacherView iView){
        this.iView = iView;
    }

    public void setParameter(TeaHomePageBean teacherInfo) {
        if (teacherInfo==null)
            return;
        iView.getTeacherNameView().setText(teacherInfo.getTeacherName());
        iView.getTitleView().setTitleText(teacherInfo.getTeacherName());
        if (teacherInfo.getIdentity()>0)
            ImageUtil.setCompoundDrawable(iView.getTeacherNameView(),16,R.mipmap.default_iamge, Gravity.RIGHT,0);

        ImageUtil.loadCircleImageView(mContext, teacherInfo.getPhotoUrl(), iView.getTeacherImageView(), R.mipmap.ic_personal_avatar);
        if(TextUtils.isEmpty(teacherInfo.getGradeName())){
            iView.getMessageView().setText("未填写");
        }else {
            iView.getMessageView().setText(StringUtils.getSex(teacherInfo.getSex()) +" | "+teacherInfo.getGradeName()+" "+teacherInfo.getSubjectName()+" | "+teacherInfo.getTeachingAge()+"年教龄");
        }

        iView.getQualityView().setVisibility(teacherInfo.getIpmpStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getRealNameView().setVisibility(teacherInfo.getCardApprStatus() != 0 ? View.VISIBLE : View.GONE);
        iView.getTeacherView().setVisibility(teacherInfo.getQtsStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getEducationView().setVisibility(teacherInfo.getQuaStatus() == 2 ? View.VISIBLE : View.GONE);

        iView.getNnumberView().setText(String.valueOf(teacherInfo.getStudentNum()<=0?0:teacherInfo.getStudentNum()));
        iView.getOrderNumView().setText(String.valueOf(teacherInfo.getOrderNum()<=0?0:teacherInfo.getOrderNum()) );
        iView.getTimerView().setText(String.valueOf(teacherInfo.getTimeLong()<=0?0:teacherInfo.getTimeLong()));
    }

    public interface IOfflineTeacherView{
        AppBarLayout getToolLayoutView();
        CommonCrosswiseBar getTitleView();
        ImageView getTeacherBgView();
        ImageView getTeacherImageView();
        TextView getTeacherNameView();
        TextView getMessageView();
        ImageView getQualityView();
        ImageView getRealNameView();
        ImageView getTeacherView();
        ImageView getEducationView();
        TextView getNnumberView();
        TextView getOrderNumView();
        TextView getTimerView();

        PagerSlidingTabStrip getTabLayoutView();
        ViewPager getViewPagerView();
    }

}
