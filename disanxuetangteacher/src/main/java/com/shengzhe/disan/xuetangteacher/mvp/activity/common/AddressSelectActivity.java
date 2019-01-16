package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.main.disanxuelib.bean.CityBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 授课地址 on 2018/1/16.
 */

public class AddressSelectActivity extends BaseActivity {
    @BindView(R.id.tv_addressselect_city)
    TextView mCity;
    @BindView(R.id.tv_addressselect_direct)
    TextView mDirect;
    @BindView(R.id.tv_addressselect_address)
    EditText mAddress;

    private List<String> dataList = new ArrayList<>();
    private CityBean mCityBean;
    private Address address = new Address();
    private List<City> cityList = new ArrayList<>();


    @Override
    public void initData() {
        dataList.clear();
        cityList.clear();
        mCityBean = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        address = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA2);

        mCity.setText(mCityBean.cityName);
        cityList = CityDaoUtil.getCityByPid(Integer.parseInt(mCityBean.cityCode.length() == 4 ? mCityBean.cityCode + "00" : mCityBean.cityCode));
        if(address == null) {
            address = new Address();
            if (cityList != null && !cityList.isEmpty()) {
                address.district = cityList.get(0).area_name;
                address.districtId = cityList.get(0).area_id;
            }
        }
        for(City item : cityList){
            dataList.add(item.area_name);
        }

        if(dataList.isEmpty()){
            dataList.add("暂无市区");
            mDirect.setText(dataList.get(0));
            mDirect.setClickable(false);
            mDirect.setEnabled(false);
        }

        if(!TextUtils.isEmpty(address.district)){
            mDirect.setText(address.district);
        }

        if(!TextUtils.isEmpty(address.address)){
            mAddress.setText(address.address);
        }
    }

    private NiceDialog niceDialog;
    @Override
    public int setLayout() {
        return R.layout.activity_addressselect;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.tv_addressselect_direct,R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_addressselect_direct:
                //地址选择
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                showPopup();
                break;

            case R.id.common_bar_rightBtn:
                //提交
                if(StringUtils.textIsEmpty(mDirect.getText().toString())){
                    showToast("请选择市、区");
                    return;
                }
                if(StringUtils.textIsEmpty(mAddress.getText().toString())){
                    showToast("请输入详细地址");
                    return;
                }
                if (!RegUtil.isDetailAddress(mAddress.getText().toString().trim())) {
                    showToast("详细地址仅支持两个以上汉字、字母、数字");
                    return;
                }
                address.address = mAddress.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11027);
                bundle.putParcelable(StringUtils.EVENT_DATA,address);
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;
        }
    }
    private ConfirmDialog dialog;
    private void showToast(String str){
        if(dialog==null) {
            dialog = ConfirmDialog.newInstance("", str, "", "确定");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        return;
    }


    private void showPopup() {
        niceDialog.setLayoutId(R.layout.common_popup_string)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                        SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                        pickerView.setStringList(dataList,null);
                        if (address != null) {
                            pickerView.setShowStringPicker(address.district,null);
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
                                address.district = (String) obj;
                                address.districtId = cityList.get(dataList.indexOf(address.district)).area_id;
                                mDirect.setText(address.district.equals("暂无") ? "" : address.district);
                                colseDialog();
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }
}
