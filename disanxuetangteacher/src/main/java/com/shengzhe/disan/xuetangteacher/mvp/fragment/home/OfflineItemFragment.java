package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherPagerActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 我的课程 线下1对1 on 2018/1/17.
 */

public class OfflineItemFragment extends BaseFragment implements RefreshCommonView.RefreshLoadMoreListener , BaseFragment.LazyLoadingListener{
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private SimpleAdapter adapter;
    private List<CourseOflineBean> courseList = new ArrayList<>();
    private int pageNum=1;
    private String data = "";

       public static OfflineItemFragment newInstance(String from) {
        OfflineItemFragment fragment = new OfflineItemFragment();
        Bundle args = new Bundle();
        args.putString(StringUtils.FRAGMENT_DATA, from);
        fragment.setArguments(args);
        return fragment;
    }

    private int index = -1;
    @Override
    public void initData() {
        data = getArguments().getString(StringUtils.FRAGMENT_DATA);
        courseList.clear();
        adapter = new SimpleAdapter<CourseOflineBean>(mContext, courseList, R.layout.item_offlineclazz) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseOflineBean data) {
                holder.setText(R.id.tv_offlineclazz_name, data.courseName)
                        .setText(R.id.tv_offlineclazz_hour, data.singleClassTime+"小时/次")
                        .setText(R.id.tv_offlineclazz_content, data.remark)
                        .setText(R.id.tv_offlineclazz_student, data.studentPrice==0?"--": StringUtils.textFormatHtml("<font color='#D92B2B'>¥"+ArithUtils.round(data.studentPrice)+"</font> /小时"))
                        .setText(R.id.tv_offlineclazz_teacher, data.teacherPrice==0?"--": StringUtils.textFormatHtml("<font color='#D92B2B'>¥"+ArithUtils.round(data.teacherPrice)+"</font> /小时"))
                        .setText(R.id.tv_offlineclazz_school, data.campusPrice==0?"--": StringUtils.textFormatHtml("<font color='#D92B2B'>¥"+ArithUtils.round(data.campusPrice)+"</font> /小时"))
                        .setOnItemListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                index = courseList.indexOf(data);
                                Intent intent =  new Intent(mContext,OfflineDetailActivity.class);
                                intent.putExtra(StringUtils.ACTIVITY_DATA,data);
                                startActivity(intent);
                            }
                        });
            }

        };
        refreshCommonView.setEmptyImage(R.mipmap.ic_nothing);
        refreshCommonView.setEmptyText("太低调了~ 一门课程都没有...~");
        refreshCommonView.setRecyclerViewAdapter(adapter);
        //refreshCommonView.setIsRefresh(!data.equals(TeacherNewPagerActivity.class.getName()));
        refreshCommonView.setIsRefresh(!data.equals(TeacherPagerActivity.class.getName()));
        refreshCommonView.setIsLoadMore(true);
        refreshCommonView.setIsAutoLoad(false);
        refreshCommonView.setRefreshLoadMoreListener(this);
        setLazyLoadingListener(this);
    }


    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle;
    }

    @Override
    public void onClick(View v) {

    }
    /**
     * 开课列表
     */
    private void postCourseStartList() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseType", 1);
        map.put("pageNum", pageNum);
        map.put("pageSize",15);
        ConstantUrl.CLIEN_Info=2;
        httpService.courseOfflineStartList(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<CourseOflineBean>>(mContext, true) {
                    @Override
                    protected void onDone(BasePageBean<CourseOflineBean>  mCourseStartList) {
                        courseList.addAll(mCourseStartList.getList());
                        //if (!data.equals(TeacherNewPagerActivity.class.getName()))
                        if (!data.equals(TeacherPagerActivity.class.getName()))
                            refreshCommonView.finishRefresh();
                        refreshCommonView.finishLoadMore();
                        if (courseList.isEmpty()) {
                            courseList.clear();
                            refreshCommonView.setIsEmpty(true);
                        } else {
                            refreshCommonView.setIsEmpty(false);
                            refreshCommonView.setIsLoadMore(mCourseStartList.isHasNextPage());
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        //if (!data.equals(TeacherNewPagerActivity.class.getName()))
                        if (!data.equals(TeacherPagerActivity.class.getName()))
                            refreshCommonView.finishRefresh();
                        refreshCommonView.finishLoadMore();
                    }
                });
    }

    @Override
    public void startRefresh() {
        courseList.clear();
        pageNum = 1;
        postCourseStartList();
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        postCourseStartList();
    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11019:
            case IntegerUtil.EVENT_ID_11020:
                refreshCommonView.notifyData();
                break;
        }
    }

    @Override
    public void loadLazyDatas(boolean bool) {
        //if (data.equals(TeacherNewPagerActivity.class.getName()))
        if (data.equals(TeacherPagerActivity.class.getName()))
            postCourseStartList();
        else
            refreshCommonView.notifyData();
    }
}
