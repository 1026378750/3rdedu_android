package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.ConfirmPay;
import com.shengzhe.disan.xuetangparent.bean.OrderOnfflineInfo;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OrderPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineOrderView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/****
 * 我的线下一对一订单
 */
public class MineOfflineOrderActivity extends BaseActivity implements OfflineOrderView.IOfflineOrderView{
    @BindView(R.id.tv_liveorder_name)
    TextView mName;
    @BindView(R.id.ccb_liveorder_clazz)
    CommonCrosswiseBar mClazz;
    @BindView(R.id.ccb_liveorder_methods)
    CommonCrosswiseBar mMethods;
    @BindView(R.id.ccb_liveorder_subject)
    CommonCrosswiseBar mSubject;
    @BindView(R.id.ccb_liveorder_times)
    CommonCrosswiseBar mTimes;
    @BindView(R.id.ccb_liveorder_time)
    CommonCrosswiseBar mTime;
    @BindView(R.id.ccb_liveorder_schooltime)
    CommonCrosswiseBar mSchoolTime;
    @BindView(R.id.ccb_liveorder_price)
    CommonCrosswiseBar mPrice;
    @BindView(R.id.ccb_liveorder_count)
    CommonCrosswiseBar mCount;
    @BindView(R.id.ccb_liveorder_message)
    CommonCrosswiseBar mMessage;
    @BindView(R.id.ccb_liveorder_freeprice)
    CommonCrosswiseBar mFreeprice;
    @BindView(R.id.rv_liveorder)
    RecyclerView rvLiveorder;
    @BindView(R.id.tv_open_time)
    TextView tvOpenTime;
    @BindView(R.id.ccb_live_teachname)
    CommonCrosswiseBar ccbLiveTeachname;
    @BindView(R.id.btn_offlineorder_confirm)
    Button tvLiveorderConfirm;
    @BindView(R.id.tv_offline_countdown)
    TextView tvOfflineCountdown;
    @BindView(R.id.ccb_liveorder_ordernum)
    CommonCrosswiseBar ccbLiveorderOrdernum;
    @BindView(R.id.ccb_liveorder_ordertime)
    CommonCrosswiseBar ccbLiveorderOrdertime;
    @BindView(R.id.ccb_liveorder_adress)
    TextView ccbLiveorderAdress;
    @BindView(R.id.tv_mine_already)
    TextView tvMineAlready;

    private OrderPresenter presenter;
    private int orderId = 0;

    @Override
    public void initData() {
        orderId = getIntent().getIntExtra(StringUtils.ORDER_ID, 0);
        if (presenter == null)
            presenter = new OrderPresenter(mContext,this);
        presenter.initMineOfflineOrderUi();
        presenter.getMineOfflineOrderDatas(orderId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_offline_order;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.btn_offlineorder_confirm, R.id.tv_open_time})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_open_time:
                rvLiveorder.setVisibility(View.VISIBLE);
                tvOpenTime.setVisibility(View.GONE);
                break;

            case R.id.btn_offlineorder_confirm:
                //线下一对一
                OrderOnfflineInfo orderInfo = presenter.getOrderOnfflineInfo();
                String btnStr = getLiveorderConfirm().getText().toString().trim();
                switch (btnStr){
                    case "删除":
                        ConfirmDialog  dialog = ConfirmDialog.newInstance("提示", "您确定删除订单吗？删除后该订单信息将不可恢复！", "取消", "确定");
                        dialog.setMargin(60)
                                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                                .setOutCancel(false)
                                .show(getSupportFragmentManager());
                        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                            @Override
                            public void dialogStatus(int id) {
                                switch (id) {
                                    case R.id.tv_dialog_ok:
                                        //删除
                                        presenter.removeOrder(orderId);
                                        break;
                                }
                            }
                        });
                        break;

                    case "立即支付":
                        ConfirmPay payUtil = new ConfirmPay(orderId,orderInfo.getTotalPrice(),0,orderInfo.getTotalPrice() - orderInfo.getDiscountAmount());
                        Intent intent = new Intent(mContext, OrderPayActivity.class);
                        intent.putExtra(StringUtils.COURSE_TYPE,1);
                        intent.putExtra(StringUtils.ACTIVITY_DATA,payUtil);
                        startActivity(intent);
                        break;

                    case "再次购买":
                         intent = new Intent(mContext, OfflineOneonOneDetailsActivity.class);
                        intent.putExtra(StringUtils.COURSE_ID,orderInfo.getCourseId());
                        startActivity(intent);
                        break;

                }
                break;
        }
    }

    @Override
    public TextView getName() {
        return mName;
    }

    @Override
    public CommonCrosswiseBar getClazz() {
        return mClazz;
    }

    @Override
    public CommonCrosswiseBar getMethods() {
        return mMethods;
    }

    @Override
    public CommonCrosswiseBar getSubject() {
        return mSubject;
    }

    @Override
    public CommonCrosswiseBar getTimes() {
        return mTimes;
    }

    @Override
    public CommonCrosswiseBar getTime() {
        return mTime;
    }

    @Override
    public CommonCrosswiseBar getSchoolTime() {
        return mSchoolTime;
    }

    @Override
    public CommonCrosswiseBar getPrice() {
        return mPrice;
    }

    @Override
    public CommonCrosswiseBar getCount() {
        return mCount;
    }

    @Override
    public CommonCrosswiseBar getMessage() {
        return mMessage;
    }

    @Override
    public CommonCrosswiseBar getFreeprice() {
        return mFreeprice;
    }

    @Override
    public RecyclerView getLiveOrderRV() {
        return rvLiveorder;
    }

    @Override
    public TextView getOpenTime() {
        return tvOpenTime;
    }

    @Override
    public CommonCrosswiseBar getLiveTeacherName() {
        return ccbLiveTeachname;
    }

    @Override
    public Button getLiveorderConfirm() {
        return tvLiveorderConfirm;
    }

    @Override
    public TextView getOfflineCountdown() {
        return tvOfflineCountdown;
    }

    @Override
    public CommonCrosswiseBar getLiveorderOrdernum() {
        return ccbLiveorderOrdernum;
    }

    @Override
    public CommonCrosswiseBar getLiveorderOrdertime() {
        return ccbLiveorderOrdertime;
    }

    @Override
    public TextView getLiveorderAdress() {
        return ccbLiveorderAdress;
    }

    @Override
    public TextView getMineAlready() {
        return tvMineAlready;
    }

}
