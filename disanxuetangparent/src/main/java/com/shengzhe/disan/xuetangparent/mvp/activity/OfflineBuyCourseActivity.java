package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.bean.Discounts;
import com.main.disanxuelib.bean.TeacherMethod;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ProgressBarDialog;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.utils.HtmlJumpUtil;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/30.
 */

public class OfflineBuyCourseActivity extends BaseActivity implements MVPRequestListener, OnGetGeoCoderResultListener {
    @BindView(R.id.tv_teachingmethod_title)
    TextView methodTitle;
    @BindView(R.id.rv_pay_teachingmethod)
    RecyclerView methodView;
    @BindView(R.id.tv_discounts_title)
    TextView discountsViewTitle;
    @BindView(R.id.rv_pay_discounts)
    RecyclerView discountsView;
    @BindView(R.id.rv_pay_price)
    TextView price;
    @BindView(R.id.tv_offline_address)
    TextView mAddressText;
    @BindView(R.id.ccb_offline_address)
    CommonCrosswiseBar mAddress;

    @BindView(R.id.ccb_offline_buynum)
    CommonCrosswiseBar buyNum;
    @BindView(R.id.ccb_offline_signaltime)
    CommonCrosswiseBar signalTime;
    @BindView(R.id.iv_offline_confirm)
    Button mConfirm;

    private CourseModelImpl courseModel;
    private NiceDialog niceDialog;
    private CourseOneInfo teacher;
    private TeachingMethod teachingMethod;
    private TeacherMethod selectMet = null;
    private Discounts selectDis = null;
    private List<TeacherMethod> methodList = new ArrayList<>();
    private List<Discounts> discountsList = new ArrayList<>();
    private SimpleAdapter payTypeAdapter, discountsTypeAdapter;
    private View lastTypeView;
    private RadioButton lastDiscountsView;
    private GeoCoder mSearch;
    private Map<Integer, LatLng> latLngMap = new HashMap<>();
    private List<String> enableMapsList;
    private ProgressBarDialog dialog = null;

    @Override
    public void initData() {
        latLngMap.clear();
        methodList.clear();
        discountsList.clear();
        teacher = getIntent().getParcelableExtra(StringUtils.teacher);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mAddressText.setVisibility(View.GONE);
        mAddress.setVisibility(View.GONE);

        payTypeAdapter = new SimpleAdapter<TeacherMethod>(mContext, methodList, R.layout.item_offline_pay) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeacherMethod data) {
                final View payLay = holder.getView(R.id.ll_pay_layout);
                final RadioButton payTitle = holder.getView(R.id.rb_pay_title);
                final RadioButton paySubhead = holder.getView(R.id.rb_pay_subhead);
                payTitle.setText(data.teachingMethodName);
                paySubhead.setText("¥" + ArithUtils.round2((double) data.signPrice) + "/小时");

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastTypeView == v) {
                            return;
                        }
                        if (!mAddressText.isShown()){
                            mAddressText.setVisibility(View.VISIBLE);
                            mAddress.setVisibility(View.VISIBLE);
                        }
                        payLay.setBackgroundResource(R.drawable.btn_round_yellow_normal);
                        payTitle.setChecked(true);
                        paySubhead.setChecked(true);
                        if (lastTypeView != null) {
                            lastTypeView.findViewById(R.id.ll_pay_layout).setBackgroundResource(R.drawable.btn_round_white_normal);
                            ((RadioButton) lastTypeView.findViewById(R.id.rb_pay_title)).setChecked(false);
                            ((RadioButton) lastTypeView.findViewById(R.id.rb_pay_subhead)).setChecked(false);
                        }
                        lastTypeView = v;
                        selectMet = data;
                        if (selectMet != null) {
                            price.setText("¥ " + ArithUtils.round((selectMet.campusDiscountSignPrice < selectMet.signPrice ? selectMet.campusDiscountSignPrice : selectMet.signPrice))+"/小时");
                            setConfirmStatus(true);
                        }
                    }
                });
            }
        };

        discountsTypeAdapter = new SimpleAdapter<Discounts>(mContext, discountsList, R.layout.item_offline_discounts) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Discounts data) {
                int index = discountsList.indexOf(data);
                holder.setText(R.id.rb_discounts_content, "优惠" + (index + 1) + ":购买" + data.buyNum + "节课,赠送" + data.giveNum + "节")
                        .setVisible(R.id.rb_discounts_line, index == discountsList.size() - 1)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton content = (RadioButton) v.findViewById(R.id.rb_discounts_content);
                                if (lastDiscountsView != content) {
                                    content.setChecked(true);
                                    if (lastDiscountsView != null) {
                                        lastDiscountsView.setChecked(false);
                                    }
                                    lastDiscountsView = content;
                                    selectDis = data;
                                } else {
                                    content.setChecked(false);
                                    lastDiscountsView = null;
                                    selectDis = null;
                                }
                                if (selectMet != null)
                                    setConfirmStatus(false);
                            }
                        });
            }
        };
        //把LayoutManager设置给RecyclerView
        methodView.setLayoutManager(UiUtils.getGridLayoutManager(3));
        //把我们自定义的ItemDecoration设置给RecyclerView
        discountsView.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        methodView.setAdapter(payTypeAdapter);
        discountsView.setAdapter(discountsTypeAdapter);

        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this, OfflineBuyCourseActivity.class.getName());

        courseModel.getTeachingMethod(teacher.getCourseId());
        buyNum.setEnabled(false);
    }

    private void setResultDatas(TeachingMethod strTeacher) {
        teachingMethod = strTeacher;
        methodList.clear();
        discountsList.clear();
        methodList.addAll(strTeacher.coursePrices);
        if (strTeacher.campusDiscountGive!=null)
            discountsList.addAll(strTeacher.campusDiscountGive);
        methodTitle.setVisibility(methodList.isEmpty() ? View.GONE : View.VISIBLE);
        discountsViewTitle.setVisibility(discountsList.isEmpty() ? View.GONE : View.VISIBLE);
        payTypeAdapter.notifyDataSetChanged();
        discountsTypeAdapter.notifyDataSetChanged();
        price.setText("¥0.00");
        signalTime.setRightText(strTeacher.classTime + "小时");
    }

    private void setConfirmStatus(boolean isPayType) {
        if (selectDis == null) {
            //单次购买
            mConfirm.setEnabled(false);
            mConfirm.setClickable(false);
            buyNum.setEnabled(true);
            selectNum = 0;
        } else {//折扣
            selectNum = selectDis.buyNum;
            mConfirm.setEnabled(true);
            mConfirm.setClickable(true);
            buyNum.setEnabled(false);
        }
        buyNum.setRightText(selectNum == 0 ? "请选择" : (selectNum + "次"));
        //需要支付
        if (!isPayType)
            return;
        if (selectMet.teachingMethod == 2)
            mAddress.setRightButton(R.mipmap.ic_black_right_arrow);
        else
            mAddress.setRightButton(R.mipmap.icn_btn_address,25);
        if (selectMet.teachingMethod == 2 && TextUtils.isEmpty(selectMet.address)) {
            mAddress.setLeftTextHtml(StringUtils.textFormatHtml("您还未设置老师上门地址，马上<font color='#ffae12'>去设置。</font>"));
            return;
        }
        mAddress.setLeftText(selectMet.address);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_offlinebuy;
    }

    private int selectNum = 0;

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_offline_buynum, R.id.ccb_offline_address, R.id.iv_offline_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_offline_address:
                //地址选中
                if (selectMet.teachingMethod == 2) {
                    if (TextUtils.isEmpty(selectMet.address)) {//老师上门
                        mContext.startActivity(new Intent(mContext, AddressCommonActivity.class));
                    }else if (address!=null){
                        Intent intent = new Intent(this, AddressCommonActivity.class);
                        intent.putExtra(StringUtils.ACTIVITY_DATA, address);
                        startActivity(intent);
                    }else{
                        address = new Address();
                        address.city = selectMet.city;
                        address.district = selectMet.area;
                        address.address = selectMet.address;
                        Intent intent = new Intent(this, AddressCommonActivity.class);
                        intent.putExtra(StringUtils.ACTIVITY_DATA, address);
                        startActivity(intent);
                    }
                    return;
                }
                if (TextUtils.isEmpty(selectMet.address)) {
                    return;
                }
                if (!latLngMap.containsKey(selectMet.id)) {
                    if (dialog == null) {
                        dialog = new ProgressBarDialog(mContext);
                    }
                    if (dialog != null) {
                        dialog.showProgress();
                    }
                    LogUtils.d("selectMet.city =====>"+selectMet.city+"=======>"+selectMet.address);
                    mSearch.geocode(new GeoCodeOption().city(StringUtils.textIsEmpty(selectMet.city)?"":selectMet.city).address(selectMet.address));
                    return;
                }
                showLocalMap();
                break;

            case R.id.ccb_offline_buynum:
                if (selectDis != null) {
                    return;
                }
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                final String num = selectNum == 0 ? "1" : String.valueOf(selectNum);
                niceDialog.setLayoutId(R.layout.common_popup_num)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                pickerView.setShowNumPicker(num);
                                holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        colseDialog();
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        selectNum = (int) obj;
                                        mConfirm.setEnabled(true);
                                        mConfirm.setClickable(true);
                                        buyNum.setRightText(selectNum + "次");
                                        colseDialog();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.iv_offline_confirm:
                if (selectNum <= 0) {
                    ToastUtil.showToast("请选择课次。");
                    return;
                }
                Intent intent = new Intent(this, SelectSchooltimeActivity.class);
                Bundle bundle = getIntent().getExtras();
                intent.putExtra(StringUtils.selectMet, selectMet);
                intent.putExtra(StringUtils.selectDis, selectDis);
                intent.putExtra(StringUtils.teachingMethod, teachingMethod);
                bundle.putParcelable(StringUtils.teacher, teacher);
                bundle.putString(StringUtils.num, String.valueOf(selectNum));

                selectMet.campusDiscountId = teachingMethod.campusDiscountId;
                selectMet.campusDiscountVersion = teachingMethod.campusDiscountVersion;
                selectMet.campusDiscountPercent = teachingMethod.campusDiscountPercent;
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }

    private void showLocalMap() {
        //调用第三方地图导航
        if (enableMapsList == null)
            enableMapsList = ContentUtil.mapList();

        if (enableMapsList == null || enableMapsList.isEmpty()) {
            ToastUtil.showShort("您手机上暂无可用地图应用...");
            return;
        }

        if (niceDialog == null) {
            niceDialog = NiceDialog.init();
        }

        niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
            @Override
            public void onItemListener(int index, String item) {
                //跳转到第三方地图
                HtmlJumpUtil.gotoMapsActivity(item, latLngMap.get(selectMet.id), selectMet.teachingMethodName, selectMet.address);
            }
        });
        niceDialog.setCommonLayout(enableMapsList.toArray(new String[enableMapsList.size()]),true,getSupportFragmentManager());
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    private Address address;
    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11011:
                Object data = bundle.getParcelable(StringUtils.EVENT_DATA);
                if (data == null)
                    return false;
                address = (Address) data;
                if (address == null)
                    return false;
                selectMet.address = address.city + address.district + address.address;
                selectMet.city = address.city;
                mAddress.setLeftText(selectMet.address);
                mAddress.setRightButton(R.mipmap.ic_black_right_arrow);
                break;
        }
        return false;
    }

    @Override
    public void onSuccess(int tager, Object objects, String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_TeachingMethod:
                //教学方式
                setResultDatas(objects == null ? new TeachingMethod() : (TeachingMethod) objects);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
        latLngMap.clear();
        methodList.clear();
        discountsList.clear();
    }

    @Override
    public void onFailed(int tager, String mesg, String from) {

    }

    //（地理编码（即地址转坐标））
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (dialog != null && dialog.isShowing())
            dialog.closeProgress();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            ToastUtil.showShort("地址编码失败,请联系客服...");
            return;
        }
        //获取地理编码结果
        latLngMap.put(selectMet.id, result.getLocation());
        showLocalMap();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有找到检索结果
        }
        //获取反向地理编码结果
    }
}
