package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.TeachingExperienceBean;
import com.main.disanxuelib.util.DateUtil;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.TeaHomePageBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class TeacherMessageView extends BaseView {
    private List<TeachingExperienceBean> oneOneExPerience = new ArrayList<>();
    private SimpleAdapter adapter;

    public TeacherMessageView(Context context) {
        super(context);
    }

    private ITeacherMessageView iView;
    public void setITeacherMessageView(ITeacherMessageView iView){
        this.iView = iView;
    }

    public void initDatas() {
        iView.getExperienceView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<TeachingExperienceBean>(mContext, oneOneExPerience, R.layout.item_teacher_experience) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeachingExperienceBean data) {
                holder.setText(R.id.tv_experience_title, data.getSchool())
                        .setVisible(R.id.ccb_experience_delte, false)
                        .setVisible(R.id.tv_experience_line, oneOneExPerience.indexOf(data) == 0)
                        .setText(R.id.tv_experience_content, data.getRemark());
                String mesg = DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd") + " 至 " + DateUtil.timeStamp(data.getEndTime(), "yyyy-MM-dd");
                holder.setText(R.id.ccb_experience_times, mesg);
            }
        };
        //设置适配器，封装后的适配器只需要实现一个函数
        iView.getExperienceView().setAdapter(adapter);
        iView.getExperienceView().setNestedScrollingEnabled(false);
    }

    public void setResultDatas(TeaHomePageBean teacher) {
        //教学经历
        oneOneExPerience.addAll(teacher.getTeachingExperience());
        if (oneOneExPerience.size() > 0 && oneOneExPerience != null) {
            iView.getExperienceView().setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        setTeacherDetail(teacher);
        if(mContext instanceof OfflineTeacherActivity){
            ((OfflineTeacherActivity)mContext).setPrameter(teacher);
        }
    }

    //设置详情信息
    private void setTeacherDetail(TeaHomePageBean strTeacher) {
        if (strTeacher == null)
            return;
        String school = "";
        if (!TextUtils.isEmpty(strTeacher.getGeaduateSchool())) {
            school += strTeacher.getGeaduateSchool();
        }
        if (!TextUtils.isEmpty(strTeacher.getProfession())) {
            school += " " + strTeacher.getProfession();
        }
        if (!TextUtils.isEmpty(strTeacher.getEdu())) {
            school += " " + strTeacher.getEdu();
        }
        iView.getGraduateView().setText(school == null ? "未填写" : school);
        iView.getIntroductionView().setText(strTeacher.getPersonalResume());
    }
    String title="呐，这个人很懒，什么都米有留下 ┐(─__─)┌";

    public interface ITeacherMessageView{
        TextView getIntroductionView();
        RecyclerView getExperienceView();
        TextView getGraduateView();
    }

}
