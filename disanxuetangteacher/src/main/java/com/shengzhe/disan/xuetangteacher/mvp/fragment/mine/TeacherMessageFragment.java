package com.shengzhe.disan.xuetangteacher.mvp.fragment.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.TeacherInfo;
import com.shengzhe.disan.xuetangteacher.bean.TeachingExperienceMode;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherPagerActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/13.
 */

public class TeacherMessageFragment extends BaseFragment implements BaseFragment.LazyLoadingListener{
    @BindView(R.id.tv_rteacher_introduction)
    TextView mRteacherIntroduction;
    @BindView(R.id.tv_rteacher_experience)
    RecyclerView mExperience;
    @BindView(R.id.tv_rteacher_graduate)
    TextView mGraduate;

    private List<TeachingExperienceMode> oneOneExPerience = new ArrayList<>();
    private SimpleAdapter adapter;

    public static TeacherMessageFragment newInstance() {
        TeacherMessageFragment fragment = new TeacherMessageFragment();
        return fragment;
    }

    @Override
    public void initData() {
        mExperience.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<TeachingExperienceMode>(mContext, oneOneExPerience, R.layout.item_teacher_experience) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeachingExperienceMode data) {
                holder.setText(R.id.tv_experience_title,data.school)
                        .setVisible(R.id.ccb_experience_delte,false)
                        .setVisible(R.id.tv_experience_line,oneOneExPerience.indexOf(data)==0)
                        .setText(R.id.tv_experience_content,data.remark);
                String mesg = DateUtil.timeStamp(data.startTime,"yyyy-MM-dd")+" 至 "+DateUtil.timeStamp(data.endTime,"yyyy-MM-dd");
                holder.setText(R.id.ccb_experience_times,mesg);
            }
        };
        //设置适配器，封装后的适配器只需要实现一个函数
        mExperience.setAdapter(adapter);
        mExperience.setNestedScrollingEnabled(false);
        setLazyLoadingListener(this);
    }

    //得到老师课程信息
    private void postMyHomePage() {
        HttpService httpService = Http.getHttpService();
        httpService.myHomePage()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<TeacherInfo>(mContext,true) {
                    @Override
                    protected void onDone(TeacherInfo strTeacher) {
                        //教学经历
                        oneOneExPerience.addAll(strTeacher.getTeachingExperience());
                        if(oneOneExPerience.size()>0 && oneOneExPerience!=null){
                            mExperience.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                        setTeacherDetail(strTeacher);
                        if (getActivity() instanceof TeacherPagerActivity){
                            ((TeacherPagerActivity)getActivity()).setPrameter(strTeacher);
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }

    //设置详情信息
    private void setTeacherDetail(TeacherInfo strTeacher){
        if (strTeacher==null)
            return;
        String  school="";
        if(!TextUtils.isEmpty(strTeacher.getGeaduateSchool())){
            school+=strTeacher.getGeaduateSchool();
        }
        if(!TextUtils.isEmpty(strTeacher.getProfession())){
            school+=" "+strTeacher.getProfession();
        }
        if(!TextUtils.isEmpty(strTeacher.getEdu())){
            school+=" "+strTeacher.getEdu();
        }
        mGraduate.setText(school==null?"未填写":school);
        mRteacherIntroduction.setText(strTeacher.getPersonalResume());
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_teacher_message;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        postMyHomePage();
    }
}
