package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.VideoDetails;

/**
 * Created by 视频课详情 on 2018/4/26.
 */

public class VideoDeatilView extends BaseView {
    public VideoDeatilView(Context context) {
        super(context);
    }

    private IVideoDeatilView iView;

    public void setIVideoDeatilView(IVideoDeatilView iView) {
        this.iView = iView;
    }

    public void setResultDatas(VideoDetails videoDetails) {
        if (videoDetails.getIsAreadyBuy() == 2) {
            iView.getLiveorderDelteView().setVisibility(View.VISIBLE);
            iView.getLiveorderDelteView().setText("已购买");
        } else {
            if (videoDetails.getCourseDiscount() != 0) {
                iView.getVideodetailSubmitView().setVisibility(View.VISIBLE);
                iView.getVideodetailSubmitView().setText((videoDetails.getCourseDiscount()==100?"立即购买":"优惠购买(" + String.format("%.1f",(float)videoDetails.getCourseDiscount() / 10) + "折)"));
            } else {
                iView.getVideodetailSubmitView().setText("立即购买");
                iView.getVideodetailSubmitView().setVisibility(View.VISIBLE);
            }
        }

        ImageUtil.loadImageViewLoding(mContext, videoDetails.getPictureUrl(), iView.getImageView(), R.mipmap.default_iamge, R.mipmap.default_iamge);
        iView.getNameView().setText(videoDetails.getCourseName());

        //原价和折扣
        if(videoDetails.getPrice()==0){
            //原价为0 免费
            iView.getPriceView().setText("免费");
            iView.getPrePriceView().setVisibility(View.GONE);
        } else if(videoDetails.getPrice()>videoDetails.getDiscountPrice()){
            //原价大于折扣价
            iView.getPriceView().setText("¥"+ ArithUtils.round(videoDetails.getDiscountPrice()));
            iView.getPrePriceView().setText("¥"+ArithUtils.round(videoDetails.getPrice()));
            iView.getPrePriceView().setVisibility(View.VISIBLE);
        }else {
            //原价小于等于折扣价
            iView.getPriceView().setText("¥"+ArithUtils.round(videoDetails.getPrice()));
            iView.getPrePriceView().setVisibility(View.GONE);
        }

        iView.getPrePriceView().getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        iView.getClassifyView().setText("分类:" + videoDetails.getVideoTypeName());
        iView.getTeacherView().setText(videoDetails.getLecturer());
        iView.getNoticeView().setText("1. 品牌课程随时可在个人中心在线观看。\n" +
                "2. 购买后不支持退课，请知悉。");//
        iView.getIntroductionView().setText(videoDetails.getRemark()==null?title:videoDetails.getRemark());
        iView.getTeacherdetailIntroductionView().setText(videoDetails.getLecturerInfo()==null?title:videoDetails.getLecturerInfo());
    }
    String title="呐，这个人很懒，什么都米有留下 ┐(─__─)┌";
    public interface IVideoDeatilView {
        ImageView getImageView();
        TextView getNameView();
        TextView getPriceView();
        TextView getPrePriceView();
        TextView getClassifyView();
        TextView getTeacherView();
        TextView getNoticeView();
        TextView getIntroductionView();
        TextView getTeacherdetailIntroductionView();
        TextView getLiveorderDelteView();
        Button getVideodetailSubmitView();
    }

}
