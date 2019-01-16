package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 地址界面 on 2018/4/17.
 */

public class AddressCommonView extends BaseView {
    private FragmentManager manager;
    private IAddressCommonView iView;
    private NiceDialog niceDialog;
    private Address address;
    private List<City> dataList = new ArrayList<>();
    private boolean isShown = false, isLoadOk = false;

    public AddressCommonView(Context mContext, FragmentManager manager) {
        super(mContext);
        this.manager = manager;
        isShown = false;
        isLoadOk = false;
    }

    public void setIAddressCommonView(IAddressCommonView iView){
        this.iView = iView;
    }

    public void setDatas(Address mAddress) {
        if (mAddress!=null){
            address = mAddress;
            iView.getSelectView().setLeftText(address.city + " " + address.district);
            iView.getDeatilView().setText(address.address);
            Selection.setSelection(iView.getDeatilView().getText(), address.address.length());
            return;
        }
        address = new Address();
        Personal personal = SharedPreferencesManager.getPersonaInfo();
        if (CityDaoUtil.getCityById(personal.getCity())!=null && !TextUtils.isEmpty(personal.getCity())&&!TextUtils.isEmpty(personal.getArea())) {
            address.city = CityDaoUtil.getCityById(personal.getCity()).getArea_name();
            address.cityId = Integer.parseInt(personal.getCity());
            if(!(CityDaoUtil.getCityById(personal.getArea())==null)){
                address.district = CityDaoUtil.getCityById(personal.getArea()).getArea_name();
            }
            address.districtId = Integer.parseInt(personal.getArea());
            address.address = personal.getAddress();
            iView.getSelectView().setLeftText(address.city + " " + address.district);
        }
        iView.getDeatilView().setText(address.address);
        //设置光标
        Selection.setSelection(iView.getDeatilView().getText(), address.address.length());
    }

    private void showPopup() {
        niceDialog.setLayoutId(R.layout.common_popup_address)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                        SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                        pickerView.setShowCityDatas(dataList);
                        if (address != null) {
                            pickerView.setShowAddressPicker(address);
                        }
                        holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colseDialog();
                                isShown = false;
                            }
                        });
                        pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                            @Override
                            public void onResultPicker(Object obj) {
                                address = (Address) obj;
                                iView.getSelectView().setLeftText(address.city + " " + (address.district.equals("暂无") ? "" : address.district));
                                colseDialog();
                                isShown = false;
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(manager);
    }

    public void setResultDatas(String mesg) {
        SharedPreferencesManager.saveCity(mesg);
        List<com.shengzhe.disan.xuetangparent.bean.City> mDataList = SharedPreferencesManager.getCity();
        dataList.clear();
        for (com.shengzhe.disan.xuetangparent.bean.City city : mDataList) {
            City mCity = new City();
            mCity.area_id = Integer.parseInt(city.cityCode.length() == 4 ? city.cityCode + "00" : city.cityCode);
            mCity.area_name = city.cityName;
            dataList.add(mCity);
        }
        isLoadOk = true;
        if (isShown) {
            showPopup();
        }
    }

    public void selectCity() {
        if (niceDialog == null) {
            niceDialog = NiceDialog.init();
        }
        isShown = true;
        if (isLoadOk) {
            showPopup();
        }
    }

    public Address getAddress() {
        return address;
    }

    public interface IAddressCommonView{
        CommonCrosswiseBar getSelectView();
        EditText getDeatilView();
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

}
