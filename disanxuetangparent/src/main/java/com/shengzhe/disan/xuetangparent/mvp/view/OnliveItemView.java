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
import com.shengzhe.disan.xuetangparent.bean.LiveBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.LiveCourseActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class OnliveItemView extends BaseView {
    private List<CourseLiveBean> courseStartList = new ArrayList<>();
    private SimpleAdapter adapter;
    private List<LiveBean> courseList = new ArrayList<>();

    public OnliveItemView(Context context) {
        super(context);
    }

    private IOnliveItemView iView;

    public void setIOnliveItemView(IOnliveItemView iView) {
        this.iView = iView;
    }

    public void setDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        courseList.clear();
        adapter = new SimpleAdapter<CourseLiveBean>(mContext, courseStartList, R.layout.item_onlivecourse) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseLiveBean data) {
                holder.setText(R.id.tv_subjct_class_type, data.getCourseName())
                        .setText(R.id.tv_order_time, data.duration + "小时/次，共" + data.classTime + "次")
                        .setText(R.id.tv_subjct_class_details, data.remark)
                        .setText(R.id.tv_order_type, data.directTypeName)
                        .setText(R.id.tv_order_price, "¥" + ArithUtils.round(data.price))
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,LiveCourseActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID,data.getCourseId());
                                mContext.startActivity(intent);
                            }
                        });

                ImageUtil.loadImageViewLoding(mContext, data.photoUrl, holder.<ImageView>getView(R.id.iv_hot_photo), R.mipmap.default_error, R.mipmap.default_error);
            }
        };
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.ic_norecord);
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsRefresh(false);
        iView.getRefreshCommonView().setIsLoadMore(true);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public void setResultDatas(BasePageBean<CourseLiveBean> resultDatas) {
        finishLoad();
        courseStartList.addAll(resultDatas.getList());
        if (courseStartList.isEmpty()) {
            courseStartList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(resultDatas.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad() {
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IOnliveItemView {
        RefreshCommonView getRefreshCommonView();
    }

}
