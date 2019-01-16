package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.main.disanxuelib.bean.CityBean;
import com.shengzhe.disan.xuetangteacher.bean.CampusBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 所属校区 on 2018/1/16.
 */

public class IndustrySchoolActivity extends BaseActivity {
    @BindView(R.id.rv_industryschool_school)
    RecyclerView mSchool;
    private SimpleAdapter adapter;

    private List<CampusBean> campusList = new ArrayList<>();
    private CityBean mCity;
    private CampusBean campusBean;

    @Override
    public void initData() {
        campusList.clear();
        mCity = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        campusBean = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA2);
        if (campusBean==null)
            campusBean = new CampusBean();

        mSchool.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        adapter = new SimpleAdapter<CampusBean>(mContext, campusList, R.layout.item_common_text) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CampusBean data) {
                holder.setText(R.id.rb_item_text, data.campusName)
                        .setTextColor(R.id.rb_item_text, campusBean.id == data.id &&campusBean.id!=0 ? UiUtils.getColor(R.color.color_ff1d97ea) : UiUtils.getColor(R.color.color_666666))
                        .setVisible(R.id.rb_item_line, campusList.indexOf(data) != campusList.size())
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11026);
                                bundle.putParcelable(StringUtils.EVENT_DATA, data);
                                EventBus.getDefault().post(bundle);
                                onBackPressed();
                            }
                        });
            }
        };
        mSchool.setAdapter(adapter);
        postFindCampusByCity();
    }

    //获取城市下面的校区
    private void postFindCampusByCity() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("city",mCity.cityCode);
        httpService.findCampusByCity(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<CampusBean>>(mContext, false) {
                    @Override
                    protected void onDone(List<CampusBean> campusBeanList) {
                        campusList.clear();
                        if(campusBeanList!=null && !campusBeanList.isEmpty()){
                            campusList.addAll(campusBeanList);
                            adapter.notifyDataSetChanged();
                        }else{
                            CampusBean campusBean = new CampusBean();
                            campusBean.campusName = "不限";
                            campusList.add(campusBean);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_industryschool;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
        }
    }
}
