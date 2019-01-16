package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.LiveInfo;
import com.shengzhe.disan.xuetangparent.mvp.activity.LiveCourseActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

/**
 * Created by 直播课详情 on 2018/4/20.
 */

public class LiveDetailView extends BaseView {
    public LiveDetailView(Context context) {
        super(context);
    }

    private ILiveDetailView iView;
    public void setILiveDetailView(ILiveDetailView iVew){
        this.iView = iVew;
    }

    private LiveInfo liveInfo;
    public void setResultDatas(LiveInfo liveInfo) {
        this.liveInfo = liveInfo;
        ImageUtil.loadImageViewLoding(mContext, liveInfo.getPictureUrl(), iView.getCoverView(), R.mipmap.default_iamge, R.mipmap.default_iamge);
        iView.getNameView().setText(liveInfo.getCourseName());
        iView.getPlantView().setVisibility(liveInfo.getIdentity() == 0 ? View.GONE : View.VISIBLE);

        //原价和折扣
        if(liveInfo.getCourseTotalPrice()==0){
            //原价为0 免费
            iView.getPriceView().setText("免费");
            iView.getPrePriceView().setVisibility(View.GONE);
        } else if(liveInfo.getCourseTotalPrice()>liveInfo.getDiscountPrice()){
            //原价大于折扣价
            iView.getPriceView().setText("¥"+ ArithUtils.round(liveInfo.getDiscountPrice()));
            iView.getPrePriceView().setText("¥"+ArithUtils.round(liveInfo.getCourseTotalPrice()));
            iView.getPrePriceView().setVisibility(View.VISIBLE);
        }else {
            //原价小于等于折扣价
            iView.getPriceView().setText("¥"+ArithUtils.round(liveInfo.getCourseTotalPrice()));
            iView.getPrePriceView().setVisibility(View.GONE);
        }
        iView.getPrePriceView().getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        ImageUtil.loadCircleImageView(mContext, liveInfo.getPhotoUrl(), iView.getTeacherImageView(), R.mipmap.teacher);
        iView.getTeacherNameView().setText(liveInfo.getTeacherName());
        iView.getTeacherMessageView().setText(StringUtils.getSex(liveInfo.getSex()) +" | "+liveInfo.getGradeName()+" "+liveInfo.getSubjectName()+" | "+liveInfo.getTeachingAge()+"年教龄");
        iView.getCertificationView().setVisibility(liveInfo.getIpmpStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getRealnameCertificationView().setVisibility(liveInfo.getCardApprStatus() != 0 ? View.VISIBLE : View.GONE);
        iView.getTeacherCertificationView().setVisibility(liveInfo.getQtsStatus() == 2 ? View.VISIBLE : View.GONE);
        iView.getEducationCertificationView().setVisibility(liveInfo.getQuaStatus() == 2 ? View.VISIBLE : View.GONE);

        iView.getCourseView().setText(liveInfo.getDirectTypeName()+","+liveInfo.getSoldNum()+"人报名");
        iView.getTimeView().setText(DateUtil.timeStamp(liveInfo.getStartTime(),"yyyy-MM-dd HH:mm")+"开课,共" + liveInfo.getClassTime() + "节");
        iView.getNoticeView().setText("1. 直播课程随时可以插班，老师的往期直播，可以观看回放。\n" +
                "2. 直播课程时间固定，请注意安排好时间，及时参加。\n" +
                "3. 购买后不支持退课，请知悉。");
        iView.getIntroductionView().setText(liveInfo.getRemark()==null?title:liveInfo.getRemark());
        iView.getTargetView().setText(liveInfo.getTarget()==null?title:liveInfo.getTarget());
        iView.getSuitView().setText(liveInfo.getCrowd()==null?title:liveInfo.getCrowd());
        if(mContext instanceof LiveCourseActivity){
            ((LiveCourseActivity)mContext).setBtnStatus(liveInfo);
        }
    }
    String title="呐，这个人很懒，什么都米有留下 ┐(─__─)┌";

    public int getTeacherId() {
        return liveInfo.getTeacherId();
    }

    public interface ILiveDetailView{
        ImageView getCoverView();
        TextView getNameView();
        ImageView getPlantView();
        TextView getPriceView();
        TextView getPrePriceView();
        ImageView getTeacherImageView();
        TextView getTeacherNameView();
        TextView getTeacherMessageView();
        ImageView getCertificationView();
        ImageView getRealnameCertificationView();
        ImageView getTeacherCertificationView();
        ImageView getEducationCertificationView();
        TextView getCourseView();
        TextView getTimeView();
        TextView getNoticeView();
        TextView getIntroductionView();
        TextView getTargetView();
        TextView getSuitView();
        RelativeLayout getSoliveTeacherView();
    }

}
