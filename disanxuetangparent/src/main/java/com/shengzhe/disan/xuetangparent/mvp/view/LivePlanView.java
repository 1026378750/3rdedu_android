package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.CourseItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class LivePlanView extends BaseView {

    private List<CourseItem.CourseImtemBean> dataList = new ArrayList<>();
    private SimpleAdapter adapter;
    private CourseItem courseItem;

    public LivePlanView(Context context) {
        super(context);
    }

    private ILivePlanView iView;
    public void setILivePlanView(ILivePlanView iView){
        this.iView = iView;
    }

    public void initDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        adapter = new SimpleAdapter<CourseItem.CourseImtemBean>(mContext, dataList, R.layout.item_class_plan) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseItem.CourseImtemBean data) {
                holder.setText(R.id.tv_class_videoclass, (dataList.indexOf(data) + 1) + "." + DateUtil.weekTimeStamp(data.getStartTime() , "yyyy-MM-dd") + " " +
                        DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtil.timeStamp(data.getEndTime(), "HH:mm"));
                if (data.getCourseStatus() == 1) {
                    holder.setText(R.id.tv_class_status, "直播中");
                    holder.setTextColor(R.id.tv_class_status, UiUtils.getColor(R.color.color_ffae12));
                } else if (data.getCourseStatus() == 2) {
                    holder.setText(R.id.tv_class_status, "待上课");
                    holder.setTextColor(R.id.tv_class_status, UiUtils.getColor(R.color.color_999999));
                } else if (data.getCourseStatus() == 3) {
                    holder.setText(R.id.tv_class_status, "可回放");
                    holder.setTextColor(R.id.tv_class_status, UiUtils.getColor(R.color.color_0d3662));
                }
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsLoadMore(false);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public void clearDatas() {
        dataList.clear();
    }

    public void setResultDatas(CourseItem strData) {
        finishLoad();
        courseItem = strData;
        dataList.addAll(courseItem.getCourseImtem());
        iView.getNotifyView().setText(courseItem.getBeginTime() + "开课，" + courseItem.getEndTime() + "结课，共" + courseItem.getClassTime() + "节");
        if (dataList.isEmpty()) {
            dataList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(false);
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }


    public interface ILivePlanView{
        TextView getNotifyView();
        RefreshCommonView getRefreshCommonView();
    }

}
