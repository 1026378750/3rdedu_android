package com.shengzhe.disan.xuetangteacher.mvp.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.bean.TeacherOrderBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MIneLiveOrderDetailsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MineOfflineOrderActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.OfflineClassOrderDetailsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.OrderPresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOrderListView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by liukui on 2017/11/29.
 * <p>
 * 线下1对1，在线直播课 内的详情信息
 */

public class OrderItemFragment extends BaseFragment implements IOrderListView,BaseFragment.LazyLoadingListener ,BasePresenter.OnClickPresenter{
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private ConfirmDialog dialog;
    private OrderPresenter presenter;

    //构造fragment
    public static OrderItemFragment newInstance(String type, String orderStatus) {
        OrderItemFragment fragment = new OrderItemFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("orderStatus", orderStatus);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initData() {
        presenter = new OrderPresenter(mContext, this);
        presenter.initOrderDate(getArguments().getString("type"),getArguments().getString("orderStatus"));
        presenter.setDatas();
        presenter.setOnClickPresenter(this);
        setLazyLoadingListener(this);
    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11014:
            case IntegerUtil.EVENT_ID_11015:
                presenter.loadListDastes();
                break;
        }
    }

    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.claerAllDate();
    }

    @Override
    public void loadLazyDatas(boolean bool) {
        presenter.loadListDastes();
    }

    @Override
    public void presenterClick(View v,final Object obj) {
        if(v.getId()==R.id.tv_pay_type){
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
                            presenter.removeOrder(OrderItemFragment.class.getName(),(int)obj);
                            break;
                    }
                }
            });
            return;
        }
        //进入详情页面  1为线下一对，2为直播课
        TeacherOrderBean orderBean = (TeacherOrderBean)obj;
        Intent intent = new Intent();
        if (orderBean.courseType == 1) {
            intent.setClass(mContext, MineOfflineOrderActivity.class);
        } else if (orderBean.courseType == 2) {
            intent.setClass(mContext, MIneLiveOrderDetailsActivity.class);
        } else if (orderBean.courseType == 4) {
            intent.setClass(mContext, OfflineClassOrderDetailsActivity.class);
        }
        intent.putExtra(StringUtils.ORDER_ID,orderBean.orderId);
        startActivity(intent);
    }
}
