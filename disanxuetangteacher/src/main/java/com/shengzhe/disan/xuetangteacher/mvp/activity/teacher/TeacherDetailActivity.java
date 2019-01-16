package com.shengzhe.disan.xuetangteacher.mvp.activity.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.MyRecyclerView;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveDetailActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangteacher.bean.OneCourseArrayBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherArea;
import com.shengzhe.disan.xuetangteacher.bean.TeacherInfo;
import com.shengzhe.disan.xuetangteacher.bean.TeacherLive;
import com.shengzhe.disan.xuetangteacher.bean.TeachingExperienceMode;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/13.
 */

public class TeacherDetailActivity extends BaseActivity {
    @BindView(R.id.rl_rteacher_layout)
    RelativeLayout mTeacherLayout;
    //老师名称，图像
    @BindView(R.id.iv_rteacher_image)
    ImageView mImage;
    @BindView(R.id.iv_rteacher_plat)
    ImageView mPlat;
    @BindView(R.id.tv_rteacher_message)
    TextView mMessage;
    @BindView(R.id.iv_quality_certification)
    ImageView mQualityCertification;
    @BindView(R.id.iv_realname_certification)
    ImageView mRealnameCertification;
    @BindView(R.id.iv_teacher_certification)
    ImageView mTeacherCertification;
    @BindView(R.id.iv_education_certification)
    ImageView mEducationCertification;
    @BindView(R.id.tv_rteacher_address)
    TextView mAddress;
    @BindView(R.id.tv_rteacher_detailaddress)
    TextView mDetailAddress;
    @BindView(R.id.tv_rteacher_area)
    TextView mArea;
    @BindView(R.id.tv_rteacher_graduate)
    TextView mGraduate;
    @BindView(R.id.tv_rteacher_introduction)
    TextView mRteacherIntroduction;
    @BindView(R.id.tv_rteacher_experience)
    MyRecyclerView mExperience;
    @BindView(R.id.tv_rteacher_name)
    TextView tvRteacherName;
    @BindView(R.id.mrv_offline_teacher)

    MyRecyclerView mrvOfflineTeacher;
    @BindView(R.id.mrv_live_teacher)
    MyRecyclerView mrvLiveTeacher;

    private List<OneCourseArrayBean> oneOneList = new ArrayList<>();
    private List<TeachingExperienceMode> oneOneExPerience = new ArrayList<>();
    private List<TeacherArea> teacherArea = new ArrayList<>();
    private List<TeacherLive>  teacherLives=new ArrayList<>();
    private TeacherInfo teacherInfo;

    @Override
    public void initData() {
        postMyHomePage();
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
                        teacherInfo=strTeacher;
                        //设置老师Area
                        teacherArea = teacherInfo.getTeacherAreaList();
                        //一对一课程
                        oneOneList = teacherInfo.getCourseList();
                        //教学经历
                        oneOneExPerience = teacherInfo.getTeachingExperience();
                        if(oneOneExPerience.size()>0 && oneOneExPerience!=null){
                            mExperience.setVisibility(View.VISIBLE);
                            setexprience();
                        }
                        setTeacherDetail();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });

    }

    //设置详情信息
    private void setTeacherDetail(){
        tvRteacherName.setText(teacherInfo.getTeacherName());
        mPlat.setVisibility(teacherInfo.getIdentity()==0?View.GONE:View.VISIBLE);
        ImageUtil.loadCircleImageView(mContext, teacherInfo.getPhotoUrl(), mImage, R.mipmap.ic_personal_avatar);
        if(TextUtils.isEmpty(teacherInfo.getGradeName())){
            mMessage.setText("未填写");
        }else {
            mMessage.setText(StringUtils.getSex(teacherInfo.getSex()) +" | "+teacherInfo.getGradeName()+" "+teacherInfo.getSubjectName()+" | "+teacherInfo.getTeachingAge()+"年教龄");
        }

        mQualityCertification.setVisibility(teacherInfo.getIpmpStatus() == 2 ? View.VISIBLE : View.GONE);
        mRealnameCertification.setVisibility(teacherInfo.getCardApprStatus() != 0 ? View.VISIBLE : View.GONE);
        mTeacherCertification.setVisibility(teacherInfo.getQtsStatus() == 2 ? View.VISIBLE : View.GONE);
        mEducationCertification.setVisibility(teacherInfo.getQuaStatus() == 2 ? View.VISIBLE : View.GONE);
        String  teacherAreas="";
        for (int i=0;i<teacherArea.size();i++){
            teacherAreas+=teacherArea.get(i).getAreaName()+"  ";
        }
        mAddress.setText(TextUtils.isEmpty(teacherInfo.getAreaName())==true?teacherInfo.getCityName():teacherInfo.getCityName()+" "+teacherInfo.getAreaName());
        mDetailAddress.setText(teacherInfo.getTeacherArea());
        mArea.setText(teacherAreas);
        String  school="";
        if(!TextUtils.isEmpty(teacherInfo.getGeaduateSchool())){
            school+=teacherInfo.getGeaduateSchool();
        }
        if(!TextUtils.isEmpty(teacherInfo.getProfession())){
            school+=" "+teacherInfo.getProfession();
        }
        if(!TextUtils.isEmpty(teacherInfo.getEdu())){
            school+=" "+teacherInfo.getEdu();
        }
        mGraduate.setText(school==null?"未填写":school);
        mRteacherIntroduction.setText(teacherInfo.getPersonalResume());
    }

    //设置一对一课程信息
    private void setOfflineTeacher() {

        mrvOfflineTeacher.setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器，封装后的适配器只需要实现一个函数
        mrvOfflineTeacher.setAdapter(new SimpleAdapter<OneCourseArrayBean>(mContext, oneOneList, R.layout.item_teacher_subject) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final OneCourseArrayBean data) {
                holder.setText(R.id.iv_subject_course, data.getCourseName())
                        .setVisible(R.id.iv_subject_time, true)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ConstantUrl.TESTCOUSRID = data.getCourseId();

                                if (data.getCourseType() == 1) {
                                    CourseOflineBean course = new CourseOflineBean();
                                    course.courseId = data.getCourseId();
                                    course.courseType = data.getCourseType();
                                    Intent intent = new Intent(TeacherDetailActivity.this, OfflineDetailActivity.class);
                                    intent.putExtra(StringUtils.ACTIVITY_DATA, course);
                                    startActivity(intent);

                                } else {
                                    CourseLiveBean course = new CourseLiveBean();
                                    course.setCourseId(data.getCourseId());
                                    course.setCourseType(data.getCourseType());
                                    Intent intent = new Intent(TeacherDetailActivity.this, OnLiveDetailActivity.class);
                                    intent.putExtra(StringUtils.ACTIVITY_DATA, course);
                                    startActivity(intent);

                                }

                            }
                        });

                holder.setText(R.id.tv_subject_price, "¥" + ArithUtils.round(data.getCoursePrice()));
                if (data.getCourseType() == 1) {
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_subject_course), 16, R.mipmap.one_lines, Gravity.LEFT, 0);
                } else {
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_subject_course), 16, R.mipmap.live_class, Gravity.LEFT, 0);
                }

            }
        });
    }


    //设置老师经历信息
    private void setexprience() {
        mExperience.setLayoutManager(new LinearLayoutManager(mContext));
        //设置适配器，封装后的适配器只需要实现一个函数
        mExperience.setAdapter(new SimpleAdapter<TeachingExperienceMode>(mContext, oneOneExPerience, R.layout.item_teacher_experience) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeachingExperienceMode data) {
                holder.setText(R.id.tv_experience_title,data.school)
                        .setVisible(R.id.ccb_experience_delte,false)
                        .setVisible(R.id.tv_experience_line,oneOneExPerience.indexOf(data)==0)
                        .setText(R.id.tv_experience_content,data.remark);
                String mesg = DateUtil.timeStamp(data.startTime,"yyyy-MM-dd")+" 至 "+DateUtil.timeStamp(data.endTime,"yyyy-MM-dd");
                holder.setText(R.id.ccb_experience_times,mesg);
            }
        });
    }
    @Override
    public int setLayout() {
        return R.layout.activity_teacherdetail;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.rl_rteacher_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.rl_rteacher_layout:
               //进入个人资料页面
                break;
        }
    }

}
