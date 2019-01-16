package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.bean.CityBean;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOpenCityView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 地址选择 on 2018/1/16.
 */

public class OpenCityActivity extends BaseActivity implements IOpenCityView,BasePresenter.OnClickPresenter {
    @BindView(R.id.rv_opencity_city)
    RecyclerView mRecViw;
    @BindView(R.id.ccb_center_opencity)
    CommonCrosswiseBar ccbCenterOpencity;
    @BindView(R.id.tv_opencity)
    TextView tvOpencity;

    private CityBean mCity;
    private CommonPresenter presenter;

    @Override
    public void initData() {
        mCity = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        boolean ishow = getIntent().getBooleanExtra(StringUtils.ACTIVITY_DATA2, false);

        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.setOnClickPresenter(this);
        presenter.initOpenCity(mCity,ishow);
        presenter.setOpenCityDate();
        presenter.postGetCityList();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_opencity;
    }

    private NiceDialog niceDialog;
    private Address address;

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_center_opencity})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                CityBean adapterCity = presenter.getAdapterCity();
                if (adapterCity != null) {
                    mCity = adapterCity;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11025);
                bundle.putParcelable(StringUtils.EVENT_DATA, mCity);
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;

            case R.id.ccb_center_opencity:
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.common_popup_address)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                pickerView.setShowCityDatas(CityDaoUtil.getAllCityProvince());
                                if (address != null) {
                                    pickerView.setShowAddressPicker(address);
                                }
                                holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        colseDialog();
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        if (address == null)
                                            address = new Address();
                                        address = (Address) obj;
                                        ccbCenterOpencity.setRightText(address.district);
                                        mCity.setCityName(address.district);
                                        mCity.setCityCode((address.districtId + "").substring(0, 4));
                                        presenter.getCityAdapter().notifyDataSetChanged();
                                        colseDialog();
                                        SharedPreferencesManager.setOpenCity(1);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11025);
                                        bundle.putParcelable(StringUtils.EVENT_DATA, mCity);
                                        EventBus.getDefault().post(bundle);
                                        onBackPressed();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());

                break;
        }
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    @Override
    public RecyclerView getRecViw() {
        return mRecViw;
    }

    @Override
    public CommonCrosswiseBar getCenterOpencity() {
        return ccbCenterOpencity;
    }

    @Override
    public TextView getOpencity() {
        return tvOpencity;
    }

    @Override
    public void presenterClick(View v, Object obj) {
        switch (v.getId()){
            case R.id.item_select_time:
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11025);
                bundle.putParcelable(StringUtils.EVENT_DATA, (CityBean)obj);
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;

        }
    }
}
