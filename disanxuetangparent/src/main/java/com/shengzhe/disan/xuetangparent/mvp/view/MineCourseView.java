package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.disanxuetang.media.util.ActivityAppManage;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.OrderCourse;
import com.shengzhe.disan.xuetangparent.mvp.activity.CourseDetailActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MyCourseDetailsActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我的课程 on 2018/4/20.
 */

public class MineCourseView extends BaseView {
    private RefreshCommonView.RefreshLoadMoreListener listener;
    private List<OrderCourse> mineClassArrayList = new ArrayList<>();
    private SimpleAdapter adapter;

    public MineCourseView(Context context, RefreshCommonView.RefreshLoadMoreListener listener) {
        super(context);
        this.listener = listener;
    }

    public void initDatas() {
        adapter = new SimpleAdapter<OrderCourse>(mContext, mineClassArrayList, R.layout.item_mine_class) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final OrderCourse data) {
                holder.setText(R.id.tv_subjct_class_type, data.getCourseTypeName() == null ? "" : data.getCourseTypeName())
                        .setText(R.id.tv_subject_status, "" + data.getTeachingMethodName() == null ? "" : "  ·  " + data.getTeachingMethodName())
                        .setText(R.id.tv_hot_name, data.getCourseName() == null ? "" : data.getCourseName())
                        .setText(R.id.tv_hot_clazz, data.getTeacherNickName() == null ? "" : data.getTeacherNickName())
                        .setText(R.id.tv_hot_time, data.getAddress() == null ? "" : data.getAddress())
                        .setVisible(R.id.item_view, true)
                        .setVisible(R.id.iv_video_photo, data.getCourseType() != 1)
                        .setVisible(R.id.iv_hot_photo, data.getCourseType() == 1)
                        //进入一对一和直播课详情
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (data.getCourseType() == 4) {
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, CourseDetailActivity.class);
                                    intent.putExtra(StringUtils.COURSE_ID, data.getCourseId());
                                    intent.putExtra(StringUtils.FRAGMENT_DATA, true);
                                    mContext.startActivity(intent);
                                } else if (data.getCourseType() != 3) {
                                    Intent intent = new Intent(mContext, MyCourseDetailsActivity.class);
                                    intent.putExtra(StringUtils.COURSE,data);
                                    mContext.startActivity(intent);
                                }
                            }
                        });

                //观看视频课
                holder.setOnClickListener(R.id.tv_mine_class_status, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getCourseType() == 3) {
                            ActivityAppManage.getInstance(mContext).goBaiJiaVideoActivity(data.getVideoBjyId(),data.getVideoBjyToken());
                        }
                    }
                });

                if (data.getCourseType() == 3) {
                    holder.setText(R.id.tv_subject_status, "· " + data.getTeachingMethodName() == null ? "" : "· " + data.getTeachingMethodName());
                    holder.setText(R.id.tv_mine_class_status, "立即播放");
                    holder.setVisible(R.id.tv_mine_class_status, true);
                    holder.setVisible(R.id.tv_class_time, false);
                    holder.setVisible(R.id.tv_class_after_time, false);
                    holder.setTextColor(R.id.tv_mine_class_status, UiUtils.getColor(R.color.color_ffffff));
                } else if (data.getCourseType() == 4) {

                    holder.setText(R.id.tv_hot_time, data.getIsJoin() == 1 ? "插班" : "全程");
                    holder.setText(R.id.tv_subjct_class_type, data.getCourseTypeName() == null ? "" : data.getCourseTypeName());
                    holder.setVisible(R.id.tv_mine_class_status, false);
                    holder.setText(R.id.tv_subject_status, "");
                    holder.setText(R.id.tv_class_time, DateUtil.timeStamp(data.getStartTime(), "MM-dd") + "开课");
                    holder.setText(R.id.tv_class_after_time, "已上" + data.getOrderNum() + "次，剩余" + ((data.getGiveNum() + data.getBuyNum()) - data.getOrderNum()) + "次");
                } else {
                    holder.setVisible(R.id.tv_class_time, true);
                    holder.setVisible(R.id.tv_class_after_time, true);
                    holder.setVisible(R.id.tv_mine_class_status, false);
                    holder.setText(R.id.tv_class_time, DateUtil.timeStamp(data.getStartTime(), "MM-dd") + "开课");
                    holder.setText(R.id.tv_class_after_time, "已上" + data.getOrderNum() + "次，剩余" + ((data.getGiveNum() + data.getBuyNum()) - data.getOrderNum()) + "次");
                }

                if (data.getSubStatus() == 21) {
                    holder.setText(R.id.tv_pay_type, "未开课");
                } else if (data.getSubStatus() == 22) {
                    holder.setText(R.id.tv_pay_type, "开课中");
                } else if (data.getSubStatus() == 80) {
                    holder.setText(R.id.tv_pay_type, "已完成");
                }
                if (data.getCourseType() == 1)//线下一对一
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_hot_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
                else//视频课、直播课
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
            }
        };
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.ic_norecord);
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    private IMineCourseView iView;
    public void setIMineCourseView(IMineCourseView iView){
        this.iView = iView;
    }

    public List<OrderCourse> getMineCourseArrayList() {
        return mineClassArrayList;
    }

    public void setResultDatas(BasePageBean<OrderCourse> myCourse) {
        finishLoad();
        mineClassArrayList.addAll(myCourse.getList());
        if (mineClassArrayList.isEmpty()) {
            mineClassArrayList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(myCourse.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IMineCourseView{
        RefreshCommonView getRefreshCommonView();
    }

}
