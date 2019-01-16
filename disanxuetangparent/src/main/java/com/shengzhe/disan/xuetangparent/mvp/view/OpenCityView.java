package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ProgressBarDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.GuideActivity;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.bean.City;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.LocationUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class OpenCityView extends BaseView {
    private List<City> cityList = new ArrayList<>();
    private LocationUtil locationService;
    private SystemPersimManage manage = null;
    private ProgressBarDialog dialog = null;
    private SimpleAdapter adapter;
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private City city = new City();
    private BDAbstractLocationListener mListener;

    public OpenCityView(Context context) {
        super(context);
    }

    private IOpenCityView iView;
    public void setIOpenCityView(IOpenCityView iView){
        this.iView = iView;
    }

    public void initDatas(final String from, RefreshCommonView.RefreshLoadMoreListener listener) {
        dialog = new ProgressBarDialog(mContext);
        if(manage==null) {
            manage = new SystemPersimManage(mContext) {
                @Override
                public void resultPerm(boolean isCan, int requestCode) {
                    if (isCan)
                        startLoacl();
                }
            };
        }

        if(from.equals(GuideActivity.class.getName())){
            //欢迎页进入
            iView.getCommonCrosswiseBarView().setVisibility(R.id.common_bar_leftBtn,false);
        }else{
            //首页进入
            iView.getCommonCrosswiseBarView().setVisibility(R.id.common_bar_leftBtn,true);
        }
        adapter  = new SimpleAdapter<City>(mContext, cityList, R.layout.select_city) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final City data) {
                holder.setText(R.id.bt_content,data.getCityName())
                        .setOnClickListener(R.id.bt_content,new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11005);
                                bundle.putParcelable(StringUtils.EVENT_DATA,data);
                                SharedPreferencesManager.saveCityId(data.cityCode);
                                EventBus.getDefault().post(bundle);
                                if(from.equals(GuideActivity.class.getName())){
                                    //欢迎页进入
                                    mContext.startActivity(new Intent(mContext,MainActivity.class) );
                                }
                                AppManager.getAppManager().currentActivity().onBackPressed();
                            }
                        });
            }
        };

        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);


        mListener = new BDAbstractLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                locationService.stop();
                if (null != location && location.getLocType() != BDLocation.TypeServerError&&location.getCity()!=null) {
                    iView.getCityView().setText("当前定位城市：" + location.getCity());
                    iView.getRestView().setVisibility(View.GONE);
                    if(dialog!=null&&dialog.isShowing()){
                        dialog.closeProgress();
                    }

                    com.main.disanxuelib.gen.City loaclCity = CityDaoUtil.getCityByName(location.getCity(),"1");
                    city.cityName = loaclCity.getArea_name();
                    city.cityCode = String.valueOf(loaclCity.getArea_id());

                    if(!cityList.isEmpty())
                        setMessage();

                }
            }
        };
        locationService = new LocationUtil(mContext);
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

        manage.CheckedLoaction();
        closeDialog();
    }

    private void closeDialog(){
        iView.getRefreshCommonView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.closeProgress();
                }
            }
        },10000);
    }

    /*****
     * 获取定位信息
     */
    public void startLoacl(){
        if(locationService.isStart())
            locationService.stop();
        iView.getRefreshCommonView().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationService.start();// 定位SDK
                dialog.showProgress();
            }
        },100);
    }

    private void setMessage(){
        iView.getMessageView().setVisibility(View.VISIBLE);
        for(City itemCity : cityList){
            if(itemCity.cityCode.equals(SharedPreferencesManager.getCityId())) {
                iView.getMessageView().setVisibility(View.GONE);
                break;
            }
        }
    }

    public void stop() {
        if(locationService==null)
            return;
        locationService.stop(); //停止定位服务
        if(dialog!=null&&dialog.isShowing()){
            dialog.closeProgress();
        }
        dialog.closeProgress();
    }

    public void destroy() {
        if(locationService==null)
            return;
        locationService.unregisterListener(mListener); //注销掉监听
    }

    public void jumpActivity() {
        if(AppManager.getAppManager().lastActivity() instanceof MainActivity){
            SharedPreferencesManager.saveCityId(city.cityCode);
            Bundle bundle = new Bundle();
            bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11005);
            bundle.putParcelable(StringUtils.EVENT_DATA,city);
            EventBus.getDefault().post(bundle);
            AppManager.getAppManager().currentActivity().onBackPressed();
            return;
        }
        mContext.startActivity(new Intent(mContext,MainActivity.class) );
    }

    public void resrLocal() {
        manage.CheckedLoaction();
        closeDialog();
    }

    public void setResultDatas(String str) {
        finishLoad();
        SharedPreferencesManager.saveCity(str);
        cityList.clear();
        cityList.addAll(SharedPreferencesManager.getCity());
        iView.getRefreshCommonView().setIsEmpty(cityList.isEmpty());
        iView.getRefreshCommonView().setIsRefresh(false);
        iView.getRefreshCommonView().setIsLoadMore(false);
        adapter.notifyDataSetChanged();
        setMessage();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IOpenCityView{
        CommonCrosswiseBar getCommonCrosswiseBarView();
        View getAddressView();
        TextView getCityView();
        RefreshCommonView getRefreshCommonView();
        TextView getRestView();
        TextView getMessageView();
    }

}
