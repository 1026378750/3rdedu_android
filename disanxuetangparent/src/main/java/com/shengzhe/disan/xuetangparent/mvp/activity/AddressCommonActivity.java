package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.AddressCommonView;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/12/2.
 */

public class AddressCommonActivity extends BaseActivity implements AddressCommonView.IAddressCommonView {
    @BindView(R.id.ccb_address_select)
    CommonCrosswiseBar mSelect;
    @BindView(R.id.ccb_address_detail)
    EditText mDeatil;

    private CommonPresenter presenter;
    @Override
    public void initData() {
        if (presenter==null) {
            presenter = new CommonPresenter(mContext,this);
        }
        Address address = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        presenter.initAddressUi(getSupportFragmentManager());
        presenter.setAddressDatas(address);
        presenter.loadAddressDatas();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_address;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn, R.id.ccb_address_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_address_select:
                //添加
                presenter.selectCity();
                break;

            case R.id.common_bar_rightBtn:
                //保存
                Address address = presenter.getAddress();
                address.address = mDeatil.getText().toString().trim();
                if (TextUtils.isEmpty(address.address)) {
                    ToastUtil.showToast("地址信息填写不完整");
                    return;
                }
                presenter.saveCity(address);
                break;
        }
    }

    @Override
    public CommonCrosswiseBar getSelectView() {
        return mSelect;
    }

    @Override
    public EditText getDeatilView() {
        return mDeatil;
    }
}
