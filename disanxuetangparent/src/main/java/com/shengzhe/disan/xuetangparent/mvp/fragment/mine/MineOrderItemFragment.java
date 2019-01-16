package com.shengzhe.disan.xuetangparent.mvp.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineOneonOneDetailsActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OrderPayActivity;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.OrderBean;
import com.shengzhe.disan.xuetangparent.mvp.activity.MIneLiveOrderActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MIneOfflineClassDetailsActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineOfflineOrderActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineVideoOrderActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OrderPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OrderItemView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/****
 * 订单列表 liukui 2018/04/10
 */
public class MineOrderItemFragment extends BaseFragment implements OrderItemView.IOrderItemView,OrderItemView.OnClickView,BaseFragment.LazyLoadingListener,RefreshCommonView.RefreshLoadMoreListener{
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;
    private String statusStr = "";

    private OrderPresenter presenter;

    public static MineOrderItemFragment newInstance(String from) {
        MineOrderItemFragment fragment = new MineOrderItemFragment();
        Bundle args = new Bundle();
        args.putString(StringUtils.FRAGMENT_DATA, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        statusStr = getArguments().getString(StringUtils.FRAGMENT_DATA);
        if (presenter==null)
            presenter = new OrderPresenter(mContext,this);
        presenter.initMineOrderItemUi(this);
        presenter.setMineOrderItemDatas(this);

        presenter.setFromTag(MineOrderItemFragment.class.getName());
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle;
    }

    @Override
    public void loadLazyDatas(boolean bool) {
        refreshCommonView.notifyData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    private ConfirmDialog dialog;

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11014:
            case IntegerUtil.EVENT_ID_11015:
                refreshCommonView.notifyData();
                break;
        }
    }

    @Override
    public void viewClick(View v, Object obj) {
        Intent intent = new Intent();
        final OrderBean data = (OrderBean)obj;
        switch (v.getId()){
            case R.id.tv_pay_type:
           /* if (data.getStatus() == IntegerUtil.ORDER_STATUS_WAITPAY) {
                //立即支付
                ConfirmPay payUtil = new ConfirmPay(data.getId(), data.getReceivablePrice(), 0, data.getReceivablePrice());
                intent.setClass(mContext, OrderPayActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, payUtil);
                startActivity(intent);
            } else */if (data.getStatus() == IntegerUtil.ORDER_STATUS_ClOSE) {
                if (dialog == null) {
                    dialog = ConfirmDialog.newInstance("提示", "您确定删除订单吗？删除后该订单信息将不可恢复！", "取消", "确定");
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                        .setOutCancel(false)
                        .show(getChildFragmentManager());
                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                    @Override
                    public void dialogStatus(int id) {
                        switch (id) {
                            case R.id.tv_dialog_ok:
                                //删除
                                presenter.removeOrder(data.getId());
                                break;
                        }
                    }
                });
            }
                break;
            case R.id.bt_hot_status:
                //待支付
                //if(data.getUpperFrame()!=0){
                    if (data.getStatus() == IntegerUtil.ORDER_STATUS_WAITPAY) {
                        //立即支付
                        ConfirmPay payUtil = new ConfirmPay(data.getId(), data.getReceivablePrice(), 0, data.getReceivablePrice());
                        intent.setClass(mContext, OrderPayActivity.class);
                        intent.putExtra(StringUtils.ACTIVITY_DATA, payUtil);
                        startActivity(intent);
                    }/* else if (data.getStatus() == IntegerUtil.ORDER_STATUS_ClOSE) {
                        if (dialog == null) {
                            dialog = ConfirmDialog.newInstance("提示", "您确定删除订单吗？删除后该订单信息将不可恢复！", "取消", "确定");
                        }
                        dialog.setMargin(60)
                                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                                .setOutCancel(false)
                                .show(getChildFragmentManager());
                        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                            @Override
                            public void dialogStatus(int id) {
                                switch (id) {
                                    case R.id.ok:
                                        //删除
                                        presenter.removeOrder(data.getId());
                                        break;
                                }
                            }
                        });
                    } */else if (data.getCourseType() == 1) {
                        //再次购买线下一对一
                        intent.setClass(mContext, OfflineOneonOneDetailsActivity.class);
                        intent.putExtra(StringUtils.COURSE_ID,data.getCourseId());
                        startActivity(intent);
                    }
                //}
                break;

            case R.id.item_order_course:
                //订单详情
                if (data.getCourseType() == 1) {
                    //线下1对1
                    intent.putExtra(StringUtils.ORDER_ID, data.getId());
                    intent.putExtra(StringUtils.COURSE_TYPE, data.getCourseType());
                    intent.setClass(mContext, MineOfflineOrderActivity.class);
                    startActivity(intent);
                } else if (data.getCourseType() == 2) {
                    //在线直播课
                    intent.putExtra(StringUtils.ORDER_ID, data.getId());
                    intent.putExtra(StringUtils.COURSE_TYPE, data.getCourseType());
                    intent.setClass(mContext, MIneLiveOrderActivity.class);
                    startActivity(intent);

                } else if (data.getCourseType() == 3) {
                    //视频课
                    intent.putExtra(StringUtils.ORDER_ID, data.getId());
                    intent.putExtra(StringUtils.COURSE_TYPE, data.getCourseType());
                    intent.setClass(mContext, MineVideoOrderActivity.class);
                    startActivity(intent);
                } else if (data.getCourseType() == 4) {
                    //线下班课
                    intent.putExtra(StringUtils.ORDER_ID, data.getId());
                    intent.putExtra(StringUtils.COURSE_TYPE, data.getCourseType());
                    intent.setClass(mContext, MIneOfflineClassDetailsActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private int pageNum = 1;
    @Override
    public void startRefresh() {
        pageNum = 1;
        presenter.clearMineOrderDatas();
        presenter.getMineOrderItemList(statusStr,pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.getMineOrderItemList(statusStr,pageNum);
    }

}
