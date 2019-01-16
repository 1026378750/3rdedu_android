package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.OrderBean;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class OrderItemView extends BaseView {

    private List<OrderBean> orderList = new ArrayList<>();
    private SimpleAdapter adapter;

    public OrderItemView(Context context) {
        super(context);
    }

    private IOrderItemView iView;
    public void setIOrderItemView(IOrderItemView iView){
        this.iView = iView;
    }

    public void initDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        adapter = new SimpleAdapter<OrderBean>(mContext, orderList, R.layout.item_order_course) {
            @Override
            protected void onBindViewHolder(final TrdViewHolder holder, final OrderBean data) {
                holder.setText(R.id.tv_hot_name, data.getCourseName())
                        .setText(R.id.tv_hot_clazz, data.getTeacherNickName())
                        .setText(R.id.tv_hot_num, "¥" + ArithUtils.round(data.getReceivablePrice()))
                       // .setText(R.id.tv_subject_status, data.getTeachingMethodName())
                        .setText(R.id.tv_start_time, DateUtil.timeStamp(data.getBuyerTime(), "MM-dd HH:mm"))
                        .setText(R.id.tv_hot_paymethod, data.getOffsetAmount() / 10000 == 0 ? "" : "(已抵扣¥" + ArithUtils.roundLong(data.getOffsetAmount()) + ")")
                        .setVisible(R.id.iv_video_photo, data.getCourseType() != 1)
                        .setVisible(R.id.iv_hot_photo, data.getCourseType() == 1)
                        .setVisible(R.id.tv_hot_time, data.getClassSum() > 0)
                        // .setVisible(R.id.tv_hot_clazz, data.getCourseType() == 4)
                        .setVisible(R.id.tv_hot_clazz, !StringUtils.textIsEmpty(data.getTeacherNickName()));
                holder.setOnClickListener(R.id.bt_hot_status, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderItemView.this.listener.viewClick(v, data);
                    }
                });
                holder.setOnClickListener(R.id.tv_pay_type, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderItemView.this.listener.viewClick(v, data);
                    }
                });

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderItemView.this.listener.viewClick(v, data);
                    }
                });

                if (data.getCourseType() == 1) {
                    holder.setText(R.id.tv_subjct_class_type, "1对1课程");
                    holder.setText(R.id.tv_hot_time, data.getTeachingMethodName());
                } else if (data.getCourseType() == 2) {
                    holder.setText(R.id.tv_subjct_class_type, "直播课程");
                    holder.setText(R.id.tv_hot_time, data.getTeachingMethodName());
                } else if (data.getCourseType() == 3) {
                    holder.setText(R.id.tv_subjct_class_type, "品牌课程");
                    holder.setText(R.id.tv_hot_time,data.getTeachingMethodName());
                } else if (data.getCourseType() == 4) {
                    holder.setText(R.id.tv_subjct_class_type, "线下班课");
                    holder.setText(R.id.tv_hot_time, data.getIsJoin() == 1 ? "插班" : "全程");
                }

                switch (data.getStatus()) {
                    case IntegerUtil.ORDER_STATUS_WAITPAY:
                        holder.setText(R.id.tv_pay_type, "待支付");
                        holder.setText(R.id.bt_hot_status, "去支付");
                        holder.setBackgroundRes(R.id.bt_hot_status, R.drawable.btn_bg_default_ok);
                        holder.setTextColor(R.id.bt_hot_status, UiUtils.getColor(R.color.color_ffffff));
                        break;

                    case IntegerUtil.ORDER_STATUS_PAYED:
                        if(data.getUpperFrame()==0){
                            holder.setVisible(R.id.bt_hot_status, false);
                            holder.setText(R.id.tv_pay_type, "已支付");
                        }else {
                            holder.setText(R.id.tv_pay_type, "已支付");
                            if (data.getCourseType() == 1) {
                                holder.setText(R.id.bt_hot_status, "再次购买");
                                holder.setBackgroundRes(R.id.bt_hot_status, R.drawable.btn_bg_default_ok);
                                holder.setTextColor(R.id.bt_hot_status, UiUtils.getColor(R.color.color_ffffff));
                            } else {
                                holder.setVisible(R.id.bt_hot_status, false);
                            }
                        }

                        break;

                    case IntegerUtil.ORDER_STATUS_FINISH:
                        if(data.getUpperFrame()==0){
                            holder.setVisible(R.id.bt_hot_status, false);
                            holder.setText(R.id.tv_pay_type, "已支付");
                        }else {
                            holder.setText(R.id.tv_pay_type, "已支付");
                            if (data.getCourseType() == 1) {
                                holder.setText(R.id.bt_hot_status, "再次购买");
                                holder.setBackgroundRes(R.id.bt_hot_status, R.drawable.btn_bg_default_ok);
                                holder.setTextColor(R.id.bt_hot_status, UiUtils.getColor(R.color.color_ffffff));
                            } else {
                                holder.setVisible(R.id.bt_hot_status, false);
                            }
                        }
                        break;

                    case IntegerUtil.ORDER_STATUS_ClOSE:
                        holder.setText(R.id.tv_del_type,"已关闭");
                        holder.setText(R.id.tv_pay_type, "删除");
                        holder.setVisible(R.id.tv_del_type,true);
                        holder.setVisible(R.id.bt_hot_status,false);
                      //  holder.setBackgroundRes(R.id.bt_hot_status, R.drawable.bt_order_del);
                       // holder.setTextColor(R.id.bt_hot_status, UiUtils.getColor(R.color.color_d92b2b));
                        break;
                }
                if (data.getClassSum() > 0)
                    holder.setText(R.id.tv_hot_times,"共" + data.getClassSum() + "次, ");
                holder.setText(R.id.tv_hot_sum," 总价：");
                //holder.setText(R.id.tv_hot_time, "共" + data.getClassSum() + "次（" + (data.getClassSum() * data.getTeachingPeriod()) + "小时)");

                if (data.getCourseType() == 1)//线下一对一
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_hot_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
                else//视频课、直播课
                    ImageUtil.loadImageViewLoding(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_video_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
            }
        };
        iView.getRefreshCommonView().setEmptyImage(R.mipmap.c_noorder);
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public void clearDatas() {
        orderList.clear();
    }

    public void setResultDatas( BasePageBean<OrderBean> myOrder) {
        finishLoad();
        orderList.addAll(myOrder.getList());
        if (orderList.isEmpty()) {
            orderList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(myOrder.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    public void setResultRemove() {
        iView.getRefreshCommonView().notifyData();
    }

    public interface IOrderItemView {
        RefreshCommonView getRefreshCommonView();
    }
}
