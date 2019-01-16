package com.shengzhe.disan.xuetangteacher.mvp.fragment.course;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 课程详情 on 2018/4/2.
 */

public class CourseDetailFragment extends BaseFragment implements BaseFragment.LazyLoadingListener{
    @BindView(R.id.tv_course_times)
    TextView mTimes;
    @BindView(R.id.tv_course_singleprice)
    TextView mSinglePrice;
    @BindView(R.id.tv_course_singletime)
    TextView mSingleTime;
    @BindView(R.id.tv_course_experience)
    TextView mExperience;
    @BindView(R.id.tv_course_inlimit)
    TextView mInlimit;
    @BindView(R.id.tv_course_introduction)
    TextView mIntroduction;
    @BindView(R.id.tv_course_target)
    TextView mTarget;
    @BindView(R.id.tv_course_fitcrowd)
    TextView mFitCrowd;
    private CourseSquadBean  courseSquadSchedule;
    private int courseId = 0;
    private String title="";

    //构造fragment
    public static CourseDetailFragment newInstance(int orderId) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.COURSE_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        courseId = getArguments().getInt(StringUtils.COURSE_ID);
        setLazyLoadingListener(this);
    }

    /**
     * 提交网络请求
     */
    private void postCourseSquadInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId",  courseId);
        ConstantUrl.CLIEN_Info=2;
        httpService.courseSquadInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CourseSquadBean>(mContext, true) {
                    @Override
                    protected void onDone(CourseSquadBean  mCourseSquadScheduleBean) {

                        if(mCourseSquadScheduleBean!=null){
                            courseSquadSchedule=mCourseSquadScheduleBean;
                            setCourseSquadSchedule();
                        }

                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }

    /**
     * 线下班课课表
     */
    private void setCourseSquadSchedule() {
        mTimes.setText(courseSquadSchedule.classTime+"次");
        mSinglePrice.setText(courseSquadSchedule.price==0?"--":("¥"+ ArithUtils.round(courseSquadSchedule.price)));
        mSingleTime.setText(courseSquadSchedule.duration==0?"--":(courseSquadSchedule.duration+"小时"));
        mExperience.setText(courseSquadSchedule.trialNum+"次");
        mInlimit.setText(courseSquadSchedule.canJoin==1?"可插班":"不可插班");
        mIntroduction.setText(TextUtils.isEmpty(courseSquadSchedule.remark)==true?title:courseSquadSchedule.remark);
        mTarget.setText(TextUtils.isEmpty(courseSquadSchedule.target)==true?title:courseSquadSchedule.target);
        mFitCrowd.setText( TextUtils.isEmpty(courseSquadSchedule.crowd)==true?title:courseSquadSchedule.crowd);
    }


    @Override
    public int setLayout() {
        return R.layout.fragment_coursedetail;
    }

    @Override
    public void onClick(View v) {

    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11043:
                //修改
                postCourseSquadInfo();
                break;

        }
    }


    @Override
    public void loadLazyDatas(boolean bool) {
        postCourseSquadInfo();
    }
}
