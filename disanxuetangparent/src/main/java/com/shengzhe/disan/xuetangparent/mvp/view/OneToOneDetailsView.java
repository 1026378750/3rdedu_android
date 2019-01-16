package com.shengzhe.disan.xuetangparent.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Discounts;
import com.main.disanxuelib.bean.TeacherMethod;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.mvp.activity.AddressCommonActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineBuyCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一对一课程详情 on 2018/4/26.
 */

public class OneToOneDetailsView extends BaseView {
    private FragmentManager fragmentManager;

    private CourseOneInfo courseOneInfo;

    private IOneToOneDetailsView iView;


    public OneToOneDetailsView(Context mContext, FragmentManager fragmentManager) {
        super(mContext);
        this.fragmentManager = fragmentManager;
    }

    public void setIOneToOneDetailsView(IOneToOneDetailsView iView) {
        this.iView = iView;
    }

    public void setDetailDatas(CourseOneInfo courseOneInfo) {
        this.courseOneInfo = courseOneInfo;
        //原值和折扣值
        iView.getPlatView().setVisibility(courseOneInfo.getIdentity() == 0 ? View.GONE : View.VISIBLE);
        iView.getTitleView().setText(courseOneInfo.getCourseName());

        //原价和折扣
        if (courseOneInfo.getCoursePrice() > courseOneInfo.getDiscountPrice()) {
            //原价大于折扣价
            iView.getPriceView().setText("¥" + ArithUtils.round(courseOneInfo.getDiscountPrice()));
            iView.getPrePriceView().setText("¥" + ArithUtils.round(courseOneInfo.getCoursePrice()));
            iView.getPrePriceView().setVisibility(View.VISIBLE);
        } else {
            //原价小于等于折扣价
            iView.getPriceView().setText("¥" + ArithUtils.round(courseOneInfo.getCoursePrice()));
            iView.getPrePriceView().setVisibility(View.GONE);
        }

        iView.getPrePriceView().getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        iView.getNameView().setText(courseOneInfo.getTeacherName());
        iView.getMessageView().setText(StringUtils.getSex(courseOneInfo.getSex()) + " | " + courseOneInfo.getGradeName() + " " + courseOneInfo.getSubjectName() + " | " + courseOneInfo.getTeachingAge() + "年教龄");
        iView.getQualityView().setVisibility(courseOneInfo.getIpmpStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getRealnameView().setVisibility(courseOneInfo.getCardApprStatus() != 0 ? View.VISIBLE : View.GONE);
        iView.getTeacherView().setVisibility(courseOneInfo.getQtsStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getEducationView().setVisibility(courseOneInfo.getQuaStatus() == 2 ? View.VISIBLE : View.GONE);

        iView.getKcxzView().setText("1. 线下1对1课程可调可退。\n" +
                "2. 购买课程后，我们将分配专门的助教，给您提供优质的服务。");
        iView.getKcjjView().setText(courseOneInfo.getRemark() == null ? "未填写" : courseOneInfo.getRemark());
        if (courseOneInfo.getAudition() == 1) {
            if (courseOneInfo.getIsAreadyListen() == 1) {
                iView.getApplyView().setText("申请试听");
                iView.getApplyView().setChecked(true);
                iView.getLineView().setVisibility(View.VISIBLE);
            } else {
                iView.getApplyView().setText("已申请试听");
                iView.getApplyView().setChecked(false);
                iView.getApplyView().setEnabled(false);
                iView.getLineView().setVisibility(View.GONE);
            }
        } else {
            iView.getApplyView().setVisibility(View.GONE);
        }
        ImageUtil.loadCircleImageView(mContext, courseOneInfo.getPhotoUrl(), iView.getImageView(), R.mipmap.ic_personal_avatar);
    }

    public void setApplyListen(String applyListen) {
        iView.getApplyView().setText("已申请试听");
        iView.getLineView().setVisibility(View.GONE);
        iView.getApplyView().setChecked(false);
        iView.getApplyView().setEnabled(false);
        ConfirmDialog.newInstance("", "你的试听申请已经发送给第三学堂,助教将尽快给您答复!", "", "确定")
                .setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(fragmentManager);
    }

    public void comfirmBtn(final Intent intent) {
        if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
            //尚未登录
            LoginOpentionUtil.getInstance().LoginRequest(mContext);
            return;
        }
        intent.setClass(mContext, OfflineBuyCourseActivity.class);
        intent.putExtra(StringUtils.teacher, courseOneInfo);
        mContext.startActivity(intent);
    }

    public boolean applyDatas() {
        if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
            //尚未登录
            LoginOpentionUtil.getInstance().LoginRequest(mContext);
            iView.getApplyView().setChecked(true);
            iView.getLineView().setVisibility(View.VISIBLE);
            return false;
        }
        return iView.getApplyView().isChecked();
    }

    public void BackDatas() {
        List<Activity> activityList = AppManager.getAppManager().getActivityStack();
        //if (activityList.get(activityList.size() - 2) instanceof TeacherNewPagerActivity) {
        if (activityList.get(activityList.size() - 2) instanceof OfflineTeacherActivity) {
            AppManager.getAppManager().currentActivity().onBackPressed();
            return;
        }
        //Intent intent = new Intent(mContext, TeacherNewPagerActivity.class);
        Intent intent = new Intent(mContext, OfflineTeacherActivity.class);
        intent.putExtra(StringUtils.TEACHER_ID, courseOneInfo.getTeacherId());
        mContext.startActivity(intent);
    }

    public interface IOneToOneDetailsView {
        ImageView getPlatView();

        TextView getTitleView();

        TextView getPriceView();

        TextView getPrePriceView();

        ImageView getImageView();

        TextView getNameView();

        TextView getMessageView();

        ImageView getQualityView();

        ImageView getRealnameView();

        ImageView getTeacherView();

        ImageView getEducationView();

        TextView getKcxzView();

        TextView getKcjjView();

        RadioButton getApplyView();

        Button getOfflineConfirmView();

        View getLineView();
    }

}
