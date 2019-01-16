package com.main.disanxuelib.view.popup;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.CityDaoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liukui on 2016/9/28.
 */
public class CustomAddressPicker {

    private PickerToolsView city_pv, district_pv;
    private List<String> cityList, districtList;
    public Address selected;
    private List<City> initCity,initDistrict;
    private Map<String , City> cityMap,districtMap;
    private boolean isInit = false;

    public CustomAddressPicker(PickerToolsView oneView,PickerToolsView twoView) {
        city_pv = oneView;
        district_pv = twoView;
        selected = new Address();
        cityMap = new HashMap<>();
        districtMap = new HashMap<>();
        isInit = true;
        initArrayList();
        setViewVISIBLE();
    }

    /*****
     * 初始化数据
     */
    private void initArrayList() {
        if (cityList == null) cityList = new ArrayList<>();
        cityList.clear();

        if (districtList == null) districtList = new ArrayList<>();
        districtList.clear();
    }

    private void setViewVISIBLE() {
        if(city_pv!=null){
            city_pv.setVisibility(View.VISIBLE);//市
        }
        if(district_pv!=null){
            district_pv.setVisibility(View.VISIBLE);//区
        }
    }

    /*****
     * 显示数据
     * @param address
     */
    public void show(Address address) {
        setSelectedAddress(address);
    }

    public void initTimer(List<City> initCity) {
        if(cityList!=null&&initCity!=null&&!initCity.isEmpty()){
            //请求市
            cityList.clear();
            cityMap.clear();
            for (City city : initCity){
                cityList.add(city.area_name);
                cityMap.put(city.area_name,city);
            }
            selected.city = initCity.get(0).area_name;
            selected.cityId = initCity.get(0).area_id;
        }

        if(districtList!=null){
            //请求县、区
            initDistrict = CityDaoUtil.getCityByPid(selected.cityId);
            districtList.clear();
            districtMap.clear();
            for (City city : initDistrict){
                districtList.add(city.area_name);
                districtMap.put(city.area_name,city);
            }
            if(initDistrict==null||initDistrict.isEmpty()){
                selected.district ="-";
                selected.districtId = 0;
                districtList.add(selected.district);
            }else {
                selected.district = initDistrict.get(0).area_name;
                selected.districtId = initDistrict.get(0).area_id;
            }
        }
        loadComponent();
        addListener();
    }

    private void loadComponent() {
        if (city_pv != null) {
            city_pv.setData(cityList);
            city_pv.setSelected(0);
        }
        if (district_pv != null) {
            district_pv.setData(districtList);
            district_pv.setSelected(0);
        }
        executeScroll();
    }

    /*****
     * 添加滚动事件
     */
    private void addListener() {
        if (city_pv != null)
            city_pv.setOnSelectListener(new PickerToolsView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    City city = cityMap.get(text);
                    selected.city = city.area_name;
                    selected.cityId = city.area_id;
                    isInit = true;
                    if(district_pv!=null)
                        districtChange();
                }
            });

        if (district_pv != null)
            district_pv.setOnSelectListener(new PickerToolsView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    City city = districtMap.get(text);
                    selected.district = city.area_name;
                    selected.districtId = city.area_id;
                    isInit = true;
                }
            });
    }

    //县、区选择
    private void districtChange() {
        if(districtList.isEmpty())
            return;
        if (selected.cityId==0)
            return;
        districtList.clear();
        districtMap.clear();
        if(districtList!=null){
            //请求县、区
            initDistrict = CityDaoUtil.getCityByPid(selected.cityId);
            for (City city : initDistrict){
                districtList.add(city.area_name);
                districtMap.put(city.area_name,city);
            }
            if(initDistrict==null||initDistrict.isEmpty()){
                selected.district = "-";
                selected.districtId = 0;
                districtList.add(selected.district);

            }else if(isInit) {
                selected.district = initDistrict.get(0).area_name;
                selected.districtId = initDistrict.get(0).area_id;
            }
        }
        district_pv.setData(districtList);
        district_pv.setSelected(selected.district);
        executeAnimator(district_pv);
        executeScroll();
    }

    private void executeScroll() {
        if (city_pv != null) {
            city_pv.setCanScroll(cityList.size() > 1);
        }
        if (district_pv != null) {
            district_pv.setCanScroll(districtList.size() > 1);
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if(this.city_pv!=null)
            this.city_pv.setIsLoop(isLoop);
        if(this.district_pv!=null)
            this.district_pv.setIsLoop(isLoop);
    }

    /**
     * 设置地址控件默认选中
     */
    private void setSelectedAddress(Address address) {
        this.selected = address;
        city_pv.setSelected(address.city);
        executeAnimator(city_pv);
        isInit = false;
        districtChange();
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }
}
