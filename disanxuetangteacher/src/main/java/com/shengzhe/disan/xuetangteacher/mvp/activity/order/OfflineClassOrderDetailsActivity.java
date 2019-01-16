package com.shengzhe.disan.xuetangteacher.mvp.activity.order;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswise;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.CourseDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.OrderPresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOfflineClassOrderDetailView;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 *线下班课订单详情
 */
public class OfflineClassOrderDetailsActivity extends BaseActivity implements IOfflineClassOrderDetailView,BasePresenter.OnClickPresenter {
    //在线直播课
    @BindView(R.id.order_method_mame)
    TextView orderMethodMame;
    //下单状态
    @BindView(R.id.order_sub_status)
    TextView orderSubStatus;
    //下单编号
    @BindView(R.id.ccb_order_code)
    CommonCrosswise ccbOrderCode;
    //下单时间
    @BindView(R.id.ccb_orderpay_time)
    CommonCrosswise ccbOrderpayTime;
    //课程图
    @BindView(R.id.iv_class_photo)
    ImageView ivHotPhoto;
    //高中语文-高中语文文言文阅读…
    @BindView(R.id.tv_class_name)
    TextView tvSubjctClassType;
    //增加学生对现代文的于都理解能力，增加学 生对现代文…
    @BindView(R.id.tv_class_details)
    TextView tvSubjctClassDetails;
    @BindView(R.id.tv_class_number)
    TextView tvOfflineMannum;
    //2小时/次，共20次
    @BindView(R.id.tv_class_time)
    TextView tvOrderTime;
    //课程价格
    @BindView(R.id.tv_class_price)
    TextView tvOrderPrice;
    //家长姓名
    @BindView(R.id.ccb_order_parent_name)
    CommonCrosswise ccbOrderParentName;
    //家长的手机号
    @BindView(R.id.ccb_order_parent_mobile)
    CommonCrosswise ccbOrderParentMobile;
    //学姓姓名
    @BindView(R.id.ccb_order_student_name)
    CommonCrosswise ccbOrderStudentName;
    //学生姓名
    @BindView(R.id.ccb_order_student_sex)
    CommonCrosswise ccbOrderStudentSex;
    //优惠信息
    @BindView(R.id.ccb_order_free)
    CommonCrosswise ccbOrderFree;
    @BindView(R.id.ccb_order_buytype)
    CommonCrosswise ccbOrderBuytype;
    @BindView(R.id.ccb_order_buytimes)
    CommonCrosswise ccbOrderBuytimes;
    @BindView(R.id.ccb_order_sigleprice)
    CommonCrosswise ccbOrderSigleprice;
    @BindView(R.id.ccb_order_sumprice)
    CommonCrosswise ccbOrderSumprice;
    @BindView(R.id.ccb_order_parentprice)
    CommonCrosswise ccbOrderParentprice;
    @BindView(R.id.tv_offline_countdown)
    TextView tvOfflineCountdown;
    @BindView(R.id.item_offlineclazz)
    View viewDetail;
    //删除
    @BindView(R.id.bt_mine_liveorder_delte)
    Button btMineLiveorderDelte;

    private OrderPresenter presenter;
    private int orderId = 0;

    @Override
    public void initData() {
        orderId = getIntent().getIntExtra(StringUtils.ORDER_ID,0);
        presenter = new OrderPresenter(mContext, this);
        presenter.getOrderDetail(OfflineClassOrderDetailsActivity.class.getName(),orderId);
        presenter.setOnClickPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_offline_class_details;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.bt_mine_liveorder_delte})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.bt_mine_liveorder_delte:
                //删除
                ConfirmDialog dialog = ConfirmDialog.newInstance("提示", "您确定删除订单吗？删除后该订单信息将不可恢复！", "取消", "确定");
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                    @Override
                    public void dialogStatus(int id) {
                        switch (id) {
                            case R.id.tv_dialog_ok:
                                //删除
                                presenter.removeOrder(OfflineClassOrderDetailsActivity.class.getName(),orderId);
                                break;
                        }
                    }
                });
                break;
        }

    }



    @Override
    public TextView getOrderMethodName() {
        return orderMethodMame;
    }

    @Override
    public TextView getOrderSubStatus() {
        return orderSubStatus;
    }

    @Override
    public CommonCrosswise getOrderCode() {
        return ccbOrderCode;
    }

    @Override
    public CommonCrosswise getOrderpayTime() {
        return ccbOrderpayTime;
    }

    @Override
    public ImageView getHotPhoto() {
        return ivHotPhoto;
    }

    @Override
    public TextView getSubjctClassType() {
        return tvSubjctClassType;
    }

    @Override
    public TextView getSubjctClassDetails() {
        return tvSubjctClassDetails;
    }

    @Override
    public TextView getOfflineMannum() {
        return tvOfflineMannum;
    }

    @Override
    public TextView getOrderTime() {
        return tvOrderTime;
    }

    @Override
    public TextView getOrderPrice() {
        return tvOrderPrice;
    }

    @Override
    public CommonCrosswise getOrderParentName() {
        return ccbOrderParentName;
    }

    @Override
    public CommonCrosswise getOrderParentMobile() {
        return ccbOrderParentMobile;
    }

    @Override
    public CommonCrosswise getOrderStudentName() {
        return ccbOrderStudentName;
    }

    @Override
    public CommonCrosswise getOrderStudentSex() {
        return ccbOrderStudentSex;
    }

    @Override
    public CommonCrosswise getOrderFree() {
        return ccbOrderFree;
    }

    @Override
    public CommonCrosswise getOrderBuytype() {
        return ccbOrderBuytype;
    }

    @Override
    public CommonCrosswise getOrderBuytimes() {
        return ccbOrderBuytimes;
    }

    @Override
    public CommonCrosswise getOrderSigleprice() {
        return ccbOrderSigleprice;
    }

    @Override
    public CommonCrosswise getOrderSumprice() {
        return ccbOrderSumprice;
    }

    @Override
    public CommonCrosswise getOrderParentprice() {
        return ccbOrderParentprice;
    }

    @Override
    public TextView getOfflineCountdown() {
        return tvOfflineCountdown;
    }

    @Override
    public TextView getMineLiveorderDelte() {
        return btMineLiveorderDelte;
    }

    @Override
    public View getViewDetail() {
        return viewDetail;
    }

    @Override
    public void presenterClick(View v, Object obj) {
        switch (v.getId()){
            case R.id.item_offlineclazz:
                //进入课程详情
                ConstantUrl.IS_EDIT=false;
                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, (int)obj);
                startActivity(intent);
                break;
        }
    }
}
