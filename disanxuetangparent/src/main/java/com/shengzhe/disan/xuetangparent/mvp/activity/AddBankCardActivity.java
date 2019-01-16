package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.AddBankCardView;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 添加银行卡
 */
public class AddBankCardActivity extends BaseActivity implements AddBankCardView.IAddBankCardView {
    @BindView(R.id.tv_bankselect_bank)
    TextView mBank;
    @BindView(R.id.tv_banksselect_name)
    EditText mBanksName;
    @BindView(R.id.et_addbank_number)
    EditText mNumber;
    @BindView(R.id.tv_addressselect_address)
    TextView mAddress;
    @BindView(R.id.tv_addbank_comfirm)
    Button mComfirm;
    @BindView(R.id.et_addbank_sub)
    EditText mAddbankSub;
    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar ccbDollarTitle;

    private UserPresenter presenter;

    @Override
    public void initData() {
        MyBankCardBean myBankCardBean = getIntent().getParcelableExtra("myBankCardBean");
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initAddBankCardUi(getSupportFragmentManager());
        presenter.setAddBankCardDatas(myBankCardBean);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_add_bank_card;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_bankselect_bank, R.id.tv_addressselect_address, R.id.tv_addbank_comfirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_bankselect_bank:
                //银行选择
                presenter.selectBank();
                break;

            case R.id.tv_addressselect_address:
                //省、市
                presenter.selectCity();
                break;

            case R.id.tv_addbank_comfirm:
                presenter.bindingBank();
                break;
        }
    }

    @Override
    public TextView getBankView() {
        return mBank;
    }

    @Override
    public EditText getBanksNameView() {
        return mBanksName;
    }

    @Override
    public EditText getNumberView() {
        return mNumber;
    }

    @Override
    public TextView getAddressView() {
        return mAddress;
    }

    @Override
    public Button getComfirmView() {
        return mComfirm;
    }

    @Override
    public EditText getAddbankSubView() {
        return mAddbankSub;
    }

    @Override
    public CommonCrosswiseBar getDollarTitleView() {
        return ccbDollarTitle;
    }
}
