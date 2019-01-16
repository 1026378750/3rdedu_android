package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadScheduleBean;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/2.
 */

public class CourseOutlineView extends BaseView {
    private SimpleAdapter adapter;
    private List<CourseSquadScheduleBean> catalogList = new ArrayList<>();

    public CourseOutlineView(Context context) {
        super(context);
    }

    private ICourseOutlineView iView;
    public void setICourseOutlineView(ICourseOutlineView iView){
        this.iView = iView;
    }

    public void initDatas() {
        catalogList.clear();
        adapter = new SimpleAdapter<CourseSquadScheduleBean>(mContext, catalogList, R.layout.item_coursecatalog) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseSquadScheduleBean data) {
                int index = catalogList.indexOf(data)+1;
                holder.setText(R.id.tv_catalog_number,index+".");
                holder.setText(R.id.tv_catalog_title,"第"+ ArithUtils.munberToChinese(String.valueOf(index)) +"次  "+data.getTitle());
                //课程状态 1：直播中 2：未开始 3：已结束
                switch(data.getCourseStatus()){
                    case 1:

                        holder.setText(R.id.tv_catalog_status, DateUtil.timeStamp(data.getStartTime() , "MM") + "月" +
                                DateUtil.timeStamp(data.getStartTime(), "dd") + " " + DateUtil.timeStamp(data.getStartTime(), "HH:mm")+"-"+DateUtil.timeStamp(data.getEndTime(), "HH:mm"));
                        holder.setTextColorRes(R.id.tv_catalog_status,R.color.color_ffae12);
                        holder.setTextColorRes(R.id.tv_catalog_title,R.color.color_ffae12);
                        holder.setTextColorRes(R.id.tv_catalog_number,R.color.color_ffae12);
                        break;
                    case 2:
                        //4月17 13:00-15:00
                        holder.setText(R.id.tv_catalog_status, DateUtil.timeStamp(data.getStartTime() , "MM") + "月" +
                                DateUtil.timeStamp(data.getStartTime(), "dd") + " " + DateUtil.timeStamp(data.getStartTime(), "HH:mm")+"-"+DateUtil.timeStamp(data.getEndTime(), "HH:mm"));
                        holder.setTextColorRes(R.id.tv_catalog_status,R.color.color_333333);
                        holder.setTextColorRes(R.id.tv_catalog_title,R.color.color_333333);
                        holder.setTextColorRes(R.id.tv_catalog_number,R.color.color_333333);
                        break;
                    case 3:
                        holder.setText(R.id.tv_catalog_status,"已完成");
                        holder.setTextColorRes(R.id.tv_catalog_status,R.color.color_999999);
                        holder.setTextColorRes(R.id.tv_catalog_title,R.color.color_999999);
                        holder.setTextColorRes(R.id.tv_catalog_number,R.color.color_999999);
                        break;
                }

            }
        };
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.ic_norecord);
        iView.getRefreshCommonView().setIsRefresh(false);
        iView.getRefreshCommonView().setIsLoadMore(true);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
    }

    public void setResultDatas(List<CourseSquadScheduleBean> resultDatas) {
        catalogList.clear();
        catalogList.addAll(resultDatas);
        if (resultDatas.isEmpty()) {
            catalogList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(false);
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().setIsEmpty(false);
        iView.getRefreshCommonView().setIsLoadMore(false);
    }

    public interface ICourseOutlineView{
        RefreshCommonView getRefreshCommonView();
    }

}
