package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineOneonOneDetailsActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class OfflineItemView extends BaseView {
    private SimpleAdapter adapter;
    private List<CourseOflineBean> courseList = new ArrayList<>();
    private List<CourseOflineBean> courseStartList = new ArrayList<>();

    public OfflineItemView(Context context) {
        super(context);
    }

    private IOfflineItemView iView;
    public void setIOfflineItemView(IOfflineItemView iView){
        this.iView = iView;
    }

    public void initDatas(RefreshCommonView.RefreshLoadMoreListener listaner) {
        courseList.clear();
        adapter = new SimpleAdapter<CourseOflineBean>(mContext, courseStartList, R.layout.item_offlineclazz) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseOflineBean data) {
                holder.setText(R.id.tv_offlineclazz_name, data.courseName)
                        .setText(R.id.tv_offlineclazz_hour, data.singleClassTime + "小时/次")
                        .setText(R.id.tv_offlineclazz_content, data.remark)
                        .setText(R.id.tv_offlineclazz_student, data.studentPrice == 0 ? "--" : StringUtils.textFormatHtml("<font color='#D92B2B'>¥" + ArithUtils.round(data.studentPrice) + "</font> /小时"))
                        .setText(R.id.tv_offlineclazz_teacher, data.teacherPrice == 0 ? "--" : StringUtils.textFormatHtml("<font color='#D92B2B'>¥" + ArithUtils.round(data.teacherPrice) + "</font> /小时"))
                        .setText(R.id.tv_offlineclazz_school, data.campusPrice == 0 ? "--" : StringUtils.textFormatHtml("<font color='#D92B2B'>¥" + ArithUtils.round(data.campusPrice) + "</font> /小时"))
                        .setOnItemListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, OfflineOneonOneDetailsActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID, data.courseId);
                                mContext.startActivity(intent);
                            }
                        });
            }

        };
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.ic_norecord);
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsRefresh(false);
        iView.getRefreshCommonView().setIsLoadMore(true);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listaner);
    }

    public void setResultDatas(BasePageBean<CourseOflineBean> courseStart) {
        finishLoad();
        courseStartList.addAll(courseStart.getList());
        if (courseStartList.isEmpty()) {
            courseStartList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(courseStart.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IOfflineItemView{
        RefreshCommonView getRefreshCommonView();
    }

}
