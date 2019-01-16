package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.CourseDetailActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ClassItemView extends BaseView {
    private SimpleAdapter adapter;
    private List<CourseLiveBean> courseList = new ArrayList<>();

    public ClassItemView(Context context) {
        super(context);
    }

    public void setDatas(RefreshCommonView.RefreshLoadMoreListener listaner){
        courseList.clear();
        adapter = new SimpleAdapter<CourseLiveBean>(mContext, courseList, R.layout.item_classcourse) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseLiveBean data) {
                holder.setText(R.id.tv_class_name, data.courseName)
                        .setText(R.id.tv_class_time, data.singleClassTime + "小时/次，共" + data.classTime + "次")
                        .setText(R.id.tv_class_number, StringUtils.textFormatHtml("<font color='#1d97ea'>" + data.salesVolume + "</font>" + "/" + data.maxUser + "人"))
                        .setText(R.id.tv_class_details, data.subjectName + " " + data.gradeName)
                        .setText(R.id.tv_class_price, "¥" + ArithUtils.round(data.totalPrice))
                        .setVisible(R.id.ll_teacher_header, false)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID, data.courseId);
                                mContext.startActivity(intent);
                            }
                        });
                ImageUtil.loadImageViewLoding(mContext, data.photoUrl, holder.<ImageView>getView(R.id.iv_class_photo), R.mipmap.default_error, R.mipmap.default_error);
            }
        };
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.ic_norecord);
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsRefresh(false);
        iView.getRefreshCommonView().setIsLoadMore(true);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listaner);
    }

    private IClassItemView iView;
    public void setIClassItemView(IClassItemView iView){
        this.iView = iView;
    }

    public List<CourseLiveBean> getClassItemCourseList() {
        return courseList;
    }

    public void setResultDatas(BasePageBean<CourseLiveBean> courseBean) {
        courseList.clear();
        courseList.addAll(courseBean.getList());
        finishLoad();
        if (courseList.isEmpty()) {
            courseList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(courseBean.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IClassItemView{
        RefreshCommonView getRefreshCommonView();
    }

}
