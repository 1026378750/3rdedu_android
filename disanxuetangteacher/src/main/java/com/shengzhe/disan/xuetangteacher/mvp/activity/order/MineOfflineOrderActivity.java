package com.shengzhe.disan.xuetangteacher.mvp.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswise;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.schedule.ScheduleTimeActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangteacher.bean.OrderItemBean;
import com.shengzhe.disan.xuetangteacher.bean.OrderOfflineDetailsBean;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.OrderPresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOfflineOrderDetailView;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 我的线下一对一订单
 */
public class MineOfflineOrderActivity extends BaseActivity implements IOfflineOrderDetailView, BasePresenter.OnClickPresenter {
    @BindView(R.id.order_method_mame)
    TextView orderMethodMame;
    //在线直播课
    @BindView(R.id.order_type_name)
    TextView orderTypeName;
    //互动小班课
    @BindView(R.id.order_sub_status)
    TextView orderSubStatus;
    //已关闭
    @BindView(R.id.ccb_order_code)
    CommonCrosswise ccbOrderCode;
    //订单编号
    @BindView(R.id.ccb_orderpay_time)
    CommonCrosswise ccbOrderpayTime;
    //下单时间
    @BindView(R.id.tv_order_type)
    TextView tvOrderType;
    //高中语文-高中语文文言文阅读…
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    //2小时/次，共20次
    @BindView(R.id.tv_order_type_details)
    TextView tvOrderTypeDetails;
    //授课方式 增加学生对现代文的于都理解能力，增加学生对现代文…
    @BindView(R.id.ccb_order_teaching_method)
    CommonCrosswise ccbOrderTeachingMethod;
    //课程单价
    @BindView(R.id.ccb_order_one_price)
    CommonCrosswise ccbOrderOnePrice;
    //课表
    @BindView(R.id.tv_order_class_form)
    TextView tvOrderClassForm;
    //2017-12-13 周三 19:00
    @BindView(R.id.tv_order_class_time)
    TextView tvOrderClassTime;
    //全部
    @BindView(R.id.tv_order_class_all)
    TextView tvOrderClassAll;
    //家长昵称
    @BindView(R.id.ccb_order_parent_name)
    CommonCrosswise ccbOrderParentName;
    //家长手机
    @BindView(R.id.ccb_order_parent_mobile)
    CommonCrosswise ccbOrderParentMobile;
    //学生姓名
    @BindView(R.id.ccb_order_student_name)
    CommonCrosswise ccbOrderStudentName;
    //学生地趾
    @BindView(R.id.ccb_order_student_adress)
    CommonCrosswise ccbOrderStudentAdress;
    //学生姓别
    @BindView(R.id.ccb_order_student_sex)

    CommonCrosswise ccbOrderStudentSex;
    //优惠信息
    @BindView(R.id.ccb_order_free)

    CommonCrosswise ccbOrderFree;
    //总价：¥999.00
    @BindView(R.id.tv_offline_sumprice)

    TextView tvOfflineSumprice;
    @BindView(R.id.bt_mine_offlineorder_delte)
    Button btMineOfflineorderDelte;
    //课程次数
    @BindView(R.id.rl_offline_details)
    View viewDetail;
    @BindView(R.id.ccb_order_one_times)
    CommonCrosswise ccbOrderOneTimes;

    private OrderPresenter presenter;
    private int orderId = 0;

    @Override
    public void initData() {
        orderId = getIntent().getIntExtra(StringUtils.ORDER_ID,0);
        presenter = new OrderPresenter(mContext, this);
        presenter.getOrderDetail(MineOfflineOrderActivity.class.getName(),orderId);
        presenter.setOnClickPresenter(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_offline_order;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.bt_mine_offlineorder_delte})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.bt_mine_offlineorder_delte:
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
                               presenter.removeOrder(MineOfflineOrderActivity.class.getName(),orderId);
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
    public TextView getOrderType() {
        return tvOrderType;
    }

    @Override
    public TextView getOrderTime() {
        return tvOrderTime;
    }

    @Override
    public TextView getOrderTypeDetails() {
        return tvOrderTypeDetails;
    }

    @Override
    public CommonCrosswise getOrderTeachingMethod() {
        return ccbOrderTeachingMethod;
    }

    @Override
    public CommonCrosswise getOrderOnePrice() {
        return ccbOrderOnePrice;
    }

    @Override
    public TextView getOrderClassForm() {
        return tvOrderClassForm;
    }

    @Override
    public TextView getOrderClassTime() {
        return tvOrderClassTime;
    }

    @Override
    public TextView getOrderClassAll() {
        return tvOrderClassAll;
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
    public CommonCrosswise getOrderStudentAdress() {
        return ccbOrderStudentAdress;
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
    public TextView getOfflineSumprice() {
        return tvOfflineSumprice;
    }

    @Override
    public Button getMineOfflineorderDelte() {
        return btMineOfflineorderDelte;
    }

    @Override
    public CommonCrosswise getOrderOneTimes() {
        return ccbOrderOneTimes;
    }

    @Override
    public View getViewDetail() {
        return viewDetail;
    }

    @Override
    public View getMineLiveorderDelte() {
        return btMineOfflineorderDelte;
    }

    @Override
    public void presenterClick(View v, Object obj) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.tv_order_class_all:
                //打开当前订单所有的课表时间ScheduleTimeActivity.class
                Bundle bundle = new Bundle();
                intent.setClass(MineOfflineOrderActivity.this, ScheduleTimeActivity.class);
                bundle.putParcelableArrayList("orderItem", (ArrayList<OrderItemBean>)obj);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.rl_offline_details:
                //进入课程详情
                intent.setClass(mContext, OfflineDetailActivity.class);
                OrderOfflineDetailsBean orderOfflineDetailBean = (OrderOfflineDetailsBean) obj;
                CourseOflineBean data = new CourseOflineBean();
                data.courseId = orderOfflineDetailBean.courseId;
                data.courseType = orderOfflineDetailBean.courseType;
                ConstantUrl.IS_EDIT = false;
                intent.putExtra(StringUtils.ACTIVITY_DATA, data);
                startActivity(intent);
                break;
        }
    }
}
