package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OpenCityView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/****
 * 地址选择
 */
public class OpenCityActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener,OpenCityView.IOpenCityView {
    @BindView(R.id.ccb_title)
    CommonCrosswiseBar commonCrosswiseBar;
    @BindView(R.id.ll_address)
    View btnAddress;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;
    @BindView(R.id.tv_rest)
    TextView tvRest;
    @BindView(R.id.tv_notify_message)
    TextView mMessage;

    private CommonPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.initOpenCityUi();
        presenter.initOpenCityDatas(getIntent().getStringExtra(StringUtils.activcity_from),this);
    }

    @Override
    public void startRefresh() {
        presenter.loadOpencityAddress();
    }

    @Override
    public void startLoadMore() {

    }

    @OnClick({R.id.common_bar_leftBtn,R.id.ll_address,R.id.tv_rest})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ll_address:
                presenter.openCityJump();
                break;

            case R.id.tv_rest:
                presenter.openCityRest();
                break;
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_open_city;
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.openCitystartLoacl();
            } else {
                showPremissionDialog("使用设备位置信息");
            }
        }
    }

    @Override
    protected void onStop() {
        presenter.openCityStop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.openCityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.openCityDestroy();
    }

    @Override
    public CommonCrosswiseBar getCommonCrosswiseBarView() {
        return commonCrosswiseBar;
    }

    @Override
    public View getAddressView() {
        return btnAddress;
    }

    @Override
    public TextView getCityView() {
        return tvCity;
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    @Override
    public TextView getRestView() {
        return tvRest;
    }

    @Override
    public TextView getMessageView() {
        return mMessage;
    }
}
