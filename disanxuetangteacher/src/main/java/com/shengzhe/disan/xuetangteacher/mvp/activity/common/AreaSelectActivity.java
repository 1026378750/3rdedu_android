package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.main.disanxuelib.bean.CityBean;
import com.shengzhe.disan.xuetangteacher.bean.TeachingAreasBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 授课区域 on 2018/1/16.
 */

public class AreaSelectActivity extends BaseActivity {
    @BindView(R.id.rv_areaselect_city)
    RecyclerView mRecViw;

    private SimpleAdapter adapter;
    private List<City> cityList = new ArrayList<>();

    private CityBean mCity;
    private ArrayList<TeachingAreasBean> seleteList;

    private Set<String> seleteSet = new HashSet<>();

    @Override
    public void initData() {
        seleteSet.clear();
        mCity = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        seleteList = getIntent().getParcelableArrayListExtra(StringUtils.ACTIVITY_DATA2);
        if (seleteList == null) {
            seleteList = new ArrayList<>();
        }

        for (int i = 0; i < seleteList.size(); i++) {
            seleteSet.add(seleteList.get(i).getAreaId());
        }

        cityList.clear();
        final City city = CityDaoUtil.getCityById(mCity.cityCode);
        cityList = CityDaoUtil.getCityByPid(city.area_id);
        if (cityList.size() <= 0) {
            cityList.clear();
            City citys = new City();
            citys.area_name = "不限";
            cityList.add(citys);
        }
        mRecViw.setLayoutManager(UiUtils.getGridLayoutManager(4));
        adapter = new SimpleAdapter<City>(mContext, cityList, R.layout.item_select_time) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final City data) {
                final RadioButton item = holder.getView(R.id.rb_select_item);
                item.setText(data.area_name);
                item.setChecked(seleteSet.contains(String.valueOf(data.area_id)));
                item.setEnabled(false);
                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setChecked(!item.isChecked());
                        if (item.isChecked())
                            //表示选中了
                            seleteSet.add(String.valueOf(data.area_id));
                        else
                            //表示取消了
                            seleteSet.remove(String.valueOf(data.area_id));
                    }
                });
            }
        };
        mRecViw.setAdapter(adapter);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_areaselect;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                //选中
                seleteList.clear();
                for (City city : cityList) {
                    if (seleteSet.contains(String.valueOf(city.area_id))) {
                        TeachingAreasBean cityBean = new TeachingAreasBean();
                        cityBean.setAreaId(String.valueOf(city.area_id));
                        cityBean.setAreaName(city.area_name);
                        seleteList.add(cityBean);
                    }
                }
                if (seleteList.isEmpty()) {
                    ConfirmDialog.newInstance("", "请选择授课区域", "", "确定").setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11028);
                bundle.putParcelableArrayList(StringUtils.EVENT_DATA, seleteList);
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;
        }
    }
}
