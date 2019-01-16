package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;

/**
 * Created by Administrator on 2018/5/2.
 */

public class CourseDetailChildView extends BaseView {
    public CourseDetailChildView(Context context) {
        super(context);
    }

    private ICourseDetailChildView iView;
    public void setICourseDetailChildView(ICourseDetailChildView iView){
        this.iView = iView;
    }

    public void setResultDatas(CourseSquadBean resultDatas) {
        iView.getTimesView().setText(resultDatas.classTime+"次");
        iView.getSinglePriceView().setText(resultDatas.price==0?"--":("¥"+ ArithUtils.round(resultDatas.price)));
        iView.getSingleTimeView().setText(resultDatas.duration==0?"--":(resultDatas.duration+"小时"));
        iView.getExperienceView().setText(resultDatas.trialNum+"次");
        iView.getInlimitView().setText(resultDatas.canJoin==1?"可插班":"不可插班");
        iView.getIntroductionView().setText(TextUtils.isEmpty(resultDatas.remark)==true?title:resultDatas.remark);
        iView.getTargetView().setText(TextUtils.isEmpty(resultDatas.target)==true?title:resultDatas.target);
        iView.getFitCrowdView().setText( TextUtils.isEmpty(resultDatas.crowd)==true?title:resultDatas.crowd);
    }
String title="呐，这个人很懒，什么都米有留下 ┐(─__─)┌";
    public interface ICourseDetailChildView{
        TextView getTimesView();
        TextView getSinglePriceView();
        TextView getSingleTimeView();
        TextView getExperienceView();
        TextView getInlimitView();
        TextView getIntroductionView();
        TextView getTargetView();
        TextView getFitCrowdView();
    }

}
