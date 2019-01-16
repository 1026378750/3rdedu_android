package com.shengzhe.disan.xuetangteacher.mvp.activity.order;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswise;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveDetailActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.OrderPresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.ILiveOrderDetailView;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 直播课订单详情
 */
public class MIneLiveOrderDetailsActivity extends BaseActivity implements ILiveOrderDetailView,BasePresenter.OnClickPresenter {
    //在线直播课
    @BindView(R.id.order_method_mame)
    TextView orderMethodMame;
    //互动小班课
    @BindView(R.id.order_type_name)
    TextView orderTypeName;
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
    @BindView(R.id.iv_hot_photo)
    ImageView ivHotPhoto;
    //高中语文-高中语文文言文阅读…
    @BindView(R.id.tv_subjct_class_type)
    TextView tvSubjctClassType;
    //增加学生对现代文的于都理解能力，增加学 生对现代文…
    @BindView(R.id.tv_subjct_class_details)
    TextView tvSubjctClassDetails;
    //互动小班课
    @BindView(R.id.tv_order_type)
    TextView tvOrderType;
    //2小时/次，共20次
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    //课程价格
    @BindView(R.id.tv_order_price)
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
    //总价
    @BindView(R.id.tv_liveorder_sumprice)
    TextView tvLiveorderSumprice;
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
        presenter.getOrderDetail(MIneLiveOrderDetailsActivity.class.getName(),orderId);
        presenter.setOnClickPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_liveorder_details;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.bt_mine_liveorder_delte})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.bt_mine_liveorder_delte:
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
                                presenter.removeOrder(MIneLiveOrderDetailsActivity.class.getName(),orderId);
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
    public TextView getOrderTypeName() {
        return orderTypeName;
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
    public TextView getOrderType() {
        return tvOrderType;
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
    public TextView getLiveorderSumprice() {
        return tvLiveorderSumprice;
    }

    @Override
    public View getViewDetail() {
        return viewDetail;
    }

    @Override
    public View getMineLiveorderDelte() {
        return btMineLiveorderDelte;
    }

    @Override
    public void presenterClick(View v, final Object obj) {
        switch (v.getId()){
            case R.id.item_offlineclazz:
                //进入课程详情
                OrderOfflineDetailsBean orderOfflineDetailBean = (OrderOfflineDetailsBean) obj;
                Intent intent = new Intent(mContext, OnLiveDetailActivity.class);
                CourseLiveBean data = new CourseLiveBean();
                data.courseId = orderOfflineDetailBean.courseId;
                data.courseType = orderOfflineDetailBean.courseType;
                ConstantUrl.IS_EDIT = false;
                intent.putExtra(StringUtils.ACTIVITY_DATA, data);
                startActivity(intent);
                break;
        }
    }
}
