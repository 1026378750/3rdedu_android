package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.RadioButton;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CityBean;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOpenCityView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */
public class CommonPresenter extends BasePresenter implements MVPRequestListener {
    private IOpenCityView cityView;
    private CommonModelImpl commonModelImpl;

    private SimpleAdapter cityAdapter;
    private List<CityBean> cityList;
    private CityBean mCity;
    private CityBean adapterCity;
    private boolean cityIsShow = false;

    public CommonPresenter(Context context, IOpenCityView view) {
        super(context);
        this.cityView = view;
        if (commonModelImpl == null)
            commonModelImpl = new CommonModelImpl(context, this);
    }

    public void initOpenCity(CityBean mCity, boolean ishow) {
        this.mCity = mCity;
        this.cityIsShow = ishow;
    }


    public void setOpenCityDate() {
        if(cityIsShow){
            cityView.getOpencity().setVisibility(View.GONE);
            cityView.getCenterOpencity().setViewGone(R.id.ccb_center_opencity,false);
        }
        if (mCity == null) {
            mCity = new CityBean();
        } else {
            cityView.getCenterOpencity().setRightText(mCity.getCityName());
        }

        cityView.getRecViw().setLayoutManager(UiUtils.getGridLayoutManager(4));
        cityList = SharedPreferencesManager.getCity();
        if (cityList == null) {
            cityList = new ArrayList<>();
        }

        cityAdapter = new SimpleAdapter<CityBean>(mContext, cityList, R.layout.item_select_time) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CityBean data) {
                final RadioButton item = holder.getView(R.id.rb_select_item);
                item.setText(data.cityName);
                item.setChecked(!StringUtils.textIsEmpty(mCity.cityCode) && mCity.cityCode.equals(data.cityCode));
                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCity = data;
                        if (adapterCity == null) {
                            adapterCity = new CityBean();
                        }
                        adapterCity = data;
                        cityList.clear();
                        cityList.addAll(SharedPreferencesManager.getCity());
                        SharedPreferencesManager.setOpenCity(2);
                        cityAdapter.notifyDataSetChanged();
                        listener.presenterClick(v,data);
                    }
                });
            }
        };
        cityView.getRecViw().setAdapter(cityAdapter);
    }

    public CityBean getAdapterCity() {
        return adapterCity;
    }

    public SimpleAdapter getCityAdapter() {
        return cityAdapter;
    }

    /****
     * 获取开通城市列表
     */
    public void postGetCityList() {
        commonModelImpl.getOpenCityList();
    }

    @Override
    public void onSuccess(int tager, Object objects) {
        switch (tager){
            case IntegerUtil.WEB_API_OpenCity:
                if (StringUtils.textIsEmpty(String.valueOf(objects)))
                    return;
                SharedPreferencesManager.saveCity(String.valueOf(objects));
                cityList.clear();
                cityList.addAll(SharedPreferencesManager.getCity());
                cityAdapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    public void onFailed(int tager, String mesg) {
        ToastUtil.showToast(mesg);
        switch (tager){
            case IntegerUtil.WEB_API_OpenCity:

                break;

        }
    }

}
