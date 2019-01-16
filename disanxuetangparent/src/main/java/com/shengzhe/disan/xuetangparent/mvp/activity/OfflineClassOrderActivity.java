package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswise;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import butterknife.BindView;

/**
 * 订单详情
 */
public class OfflineClassOrderActivity extends BaseActivity {


    @BindView(R.id.iv_teacher_header)
    ImageView ivItemHead;
    @BindView(R.id.iv_teacher_mesg)
    TextView tvItemTeacher;
    @BindView(R.id.iv_class_photo)
    ImageView ivHotPhoto;
    @BindView(R.id.tv_order_type)
    TextView tvOrderType;
    @BindView(R.id.tv_class_name)
    TextView tvSubjctClassType;
    @BindView(R.id.tv_class_number)
    TextView tvOfflineMannum;
    @BindView(R.id.tv_class_details)
    TextView tvSubjctClassDetails;
    @BindView(R.id.tv_class_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_class_price)
    TextView tvOrderPrice;
    @BindView(R.id.item_offlineclazz)
    LinearLayout itemOfflineclazz;
    @BindView(R.id.ccb_order_buytype)
    CommonCrosswise ccbOrderBuytype;
    @BindView(R.id.ccb_order_buytimes)
    CommonCrosswise ccbOrderBuytimes;
    @BindView(R.id.ccb_order_sigleprice)
    CommonCrosswise ccbOrderSigleprice;
    @BindView(R.id.ccb_order_sumprice)
    CommonCrosswise ccbOrderSumprice;
    @BindView(R.id.ccb_order_free)
    CommonCrosswise ccbOrderFree;
    @BindView(R.id.ccb_order_parentprice)
    CommonCrosswise ccbOrderParentprice;
    @BindView(R.id.btn_offlineorder_confirm)
    Button btnOfflineorderConfirm;

    @Override
    public void initData() {


    }

    @Override
    public int setLayout() {
        return R.layout.activity_offline_class_order;
    }

    @Override
    public void onClick(View v) {

    }


}
