package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.VideoBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.VideoDeatilActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ViedoItemView extends BaseView{
    private SimpleAdapter adapter;
    private List<VideoBean> courseList = new ArrayList<>();
    private int videoType = 0;

    public ViedoItemView(Context context) {
        super(context);
    }

    private IViedoItemView iView;
    public void setIViedoItemView(IViedoItemView iView){
        this.iView = iView;
    }

    public void initDatas(int videotype,RefreshCommonView.RefreshLoadMoreListener listener) {
        courseList.clear();
        this.videoType = videotype;
        adapter = new SimpleAdapter<VideoBean>(mContext, courseList, R.layout.item_good_course) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final VideoBean data) {
                holder.setText(R.id.tv_course_name, data.getCourseName())
                        .setText(R.id.tv_course_clazz, data.getVideoTypeName())
                        .setText(R.id.tv_course_teacher, data.getLecturer())
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, VideoDeatilActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID,data.getCourseId());
                                mContext.startActivity(intent);
                            }
                        });
                //原价和折扣
                if (data.getCoursePrice() == 0) {
                    //原价为0 免费
                    holder.setText(R.id.tv_course_price, "免费");
                    holder.setVisible(R.id.tv_course_priprice, false);
                } else if (data.getCoursePrice() > data.getDiscountPrice()) {
                    //原价大于折扣价
                    holder.setText(R.id.tv_course_price, "¥" + ArithUtils.round(data.getDiscountPrice()));
                    holder.setText(R.id.tv_course_priprice, "¥" + ArithUtils.round(data.getCoursePrice()));
                    holder.setVisible(R.id.tv_course_priprice, true);
                } else {
                    //原价小于等于折扣价
                    holder.setText(R.id.tv_course_price, "¥" + ArithUtils.round(data.getCoursePrice()));
                    holder.setVisible(R.id.tv_course_priprice, false);
                }
                holder.<TextView>getView(R.id.tv_course_priprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageUtil.loadImageViewLoding(mContext, data.getPictureUrl(), holder.<ImageView>getView(R.id.im_course_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.ic_norecord);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public int getVideoType() {
        return videoType;
    }

    public void clearDatas() {
        courseList.clear();
    }

    public void setResultDatas(BasePageBean< VideoBean > preferredVideo) {
        finishLoad();
        courseList.addAll(preferredVideo.getList());
        if (courseList.isEmpty()) {
            courseList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(preferredVideo.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IViedoItemView{
        RefreshCommonView getRefreshCommonView();
    }
}
