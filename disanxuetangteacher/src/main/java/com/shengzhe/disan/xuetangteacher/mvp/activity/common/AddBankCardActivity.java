package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.MyBankCardBean;
import com.main.disanxuelib.util.BankCardUtils;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
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

/*****
 * 添加银行卡
 */
public class AddBankCardActivity extends BaseActivity {
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
    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar ccbDollarTitle;

    private MyBankCardBean myBankCardBean;
    private ConfirmDialog dialog;
    private int postion = 0;

    private List<City> dataList = new ArrayList<>();
    private Address address;
    private boolean isCanShown = true;
    //上次输入框中的内容
    private String lastString;
    //光标的位置
    private int selectPosition;

    private boolean selectAddress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

        //tvAddbankComfirm.setChecked(false);
        //tvAddbankComfirm.setClickable(false);
        myBankCardBean = (MyBankCardBean) getIntent().getParcelableExtra("myBankCardBean");
        if (myBankCardBean == null) {
            myBankCardBean = new MyBankCardBean();
        }
        mComfirm.setEnabled(false);
        isCanShown = true;
        if (myBankCardBean != null) {
            ccbDollarTitle.setTitleText("更换银行卡");
            mBank.setText(myBankCardBean.getBankTypeName());
            mBanksName.setText(myBankCardBean.getBankSub());
            String regex = "(.{4})";
            String bankCard = myBankCardBean.getBankNum() + "";
            bankCard = bankCard.replaceAll(regex, "$1 ");
            mNumber.setText(myBankCardBean.getBankNum() == 0 ? "" : bankCard + "");
            mNumber.setHint("请输入银行卡号码");

            address = new Address();
            String city = "";
            if (!TextUtils.isEmpty(myBankCardBean.getBankProvince()) && (Integer.parseInt(myBankCardBean.getBankProvince()) != 0)) {
                address.cityId = Integer.parseInt(myBankCardBean.getBankProvince());
                address.city = CityDaoUtil.getCityById(myBankCardBean.getBankProvince()).getArea_name();
                city += address.city;
            }
            if (!TextUtils.isEmpty(myBankCardBean.getBankCity()) && (Integer.parseInt(myBankCardBean.getBankProvince()) != 0)) {
                address.districtId = Integer.parseInt(myBankCardBean.getBankCity());
                address.district = CityDaoUtil.getCityById(myBankCardBean.getBankCity()).getArea_name();
                city += " "+ address.district;
            }
            mAddress.setText(city);
        } else {
            ccbDollarTitle.setTitleText("添加银行卡");
        }
        myBankCardBean = new MyBankCardBean();
        dataList.clear();
        dataList.addAll(CityDaoUtil.getAllCityProvince());
        //银行号码4位一空格
        mNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * 当输入框内容改变时的回调
             * @param s  改变后的字符串
             * @param start 改变之后的光标下标
             * @param before 删除了多少个字符
             * @param count 添加了多少个字符
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //因为重新排序之后setText的存在
                //会导致输入框的内容从0开始输入，这里是为了避免这种情况产生一系列问题
                if (start == 0 && count > 1) {
                    return;
                }
                String textTrim = mNumber.getText().toString();
                if (TextUtils.isEmpty(textTrim)) {
                    return;
                }
                //如果before > 0,代表此次操作是删除操作
                if (before > 0) {
                    selectPosition = start;
                    if (TextUtils.isEmpty(lastString)) {
                        return;
                    }
                    //将上次的字符串去空格 和 改变之后的字符串去空格 进行比较
                    //如果一致，代表本次操作删除的是空格
                    if (textTrim.equals(lastString.replaceAll(" ", ""))) {
                        //帮助用户删除该删除的字符，而不是空格
                        StringBuffer stringBuffer = new StringBuffer(lastString);
                        stringBuffer.deleteCharAt(start - 1);
                        selectPosition = start - 1;
                        mNumber.setText(stringBuffer.toString());
                    }
                } else {
                    //此处代表是添加操作
                    //当光标位于空格之前，添加字符时，需要让光标跳过空格，再按照之前的逻辑计算光标位置
                    //第一次空格出现的位置是4，第二次是4+1(空格)+4=9，第三次是4+1(空格)+4+1(空格)+4=14
                    //如果按照数学公式，则当start = 5n-1时，需要让光标跳过空格
                    //也就是当stat%5 == 4时
                    if (start % 5 == 4) {
                        selectPosition = start + count + 1;
                    } else {
                        selectPosition = start + count;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //获取输入框中的内容,不可以去空格
                String etContent = mNumber.getText().toString();
                if (TextUtils.isEmpty(etContent)) {
                    return;
                }
                //重新拼接字符串
                String newContent = StringUtils.addSpeaceByCredit(etContent);
                //保存本次字符串数据
                lastString = newContent;

                //如果有改变，则重新填充
                //防止EditText无限setText()产生死循环
                if (!newContent.equals(etContent)) {
                    mNumber.setText(newContent);
                    //保证光标的位置
                    mNumber.setSelection(selectPosition > newContent.length() ? newContent.length() : selectPosition);
                }
                mComfirm.setEnabled(judgeSumbit());
            }
        });
        mBanksName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mComfirm.setEnabled(judgeSumbit());
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_add_bank_card;
    }

    private NiceDialog niceDialog;

    String city="";
    @OnClick({R.id.common_bar_leftBtn, R.id.tv_bankselect_bank, R.id.tv_addressselect_address, R.id.tv_addbank_comfirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_bankselect_bank:
                //地址选择
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.common_popup_string)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                pickerView.setTitle("选择银行卡");
                                pickerView.setStringList(ContentUtil.selectBank(), null);
                                if (TextUtils.isEmpty(mBank.getText().toString().trim())) {
                                    pickerView.setShowStringPicker("中国银行", "");
                                } else {
                                    pickerView.setShowStringPicker(mBank.getText().toString().trim(), "");
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
                                        mBanksName.setText("");
                                        mNumber.setText("");
                                        mBank.setText(obj.toString());
                                        colseDialog();
                                        mComfirm.setEnabled(judgeSumbit());
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.tv_addressselect_address:
                //省、市
                if (!isCanShown) {
                    return;
                }
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
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
                                        isCanShown = true;
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        if (address == null)
                                            address = new Address();
                                        address = (Address) obj;
                                        selectAddress = true;
                                        LogUtils.e("district = " + address.provinceId);
                                        mAddress.setText(address.city + " " + (address.district.equals("暂无") ? "" : address.district));
                                        colseDialog();
                                        isCanShown = true;
                                        mComfirm.setEnabled(judgeSumbit());
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.tv_addbank_comfirm:

                if (StringUtils.textIsEmpty(mBank.getText().toString())) {
                    nofifyShowMesg("请选择银行", null);
                    return;
                }
                if (StringUtils.textIsEmpty(mBanksName.getText().toString())) {
                    nofifyShowMesg("请输入开户支行", null);
                    return;
                }
                if (StringUtils.textIsEmpty(mNumber.getText().toString())) {
                    nofifyShowMesg("请输入银行卡号码", null);
                    return;
                }
                String bankNum = mNumber.getText().toString().trim().replace(" ", "");
                if (!BankCardUtils.checkBankCard(bankNum)) {
                    nofifyShowMesg("请输入正确银行卡号码", null);
                    return;
                }
               /* String  bank=mBank.getText().toString();
                if(bank.length()==6){
                    bank=bank.substring(2,bank.length());
                }
                if (BankCardUtils.getDetailNameOfBank(bankNum).indexOf(bank)==-1) {
                    nofifyShowMesg("输入的银行卡号和银行不符合", null);
                    return;
                }*/
                if (RegUtil.containsEmoji(mNumber.getText().toString())) {
                    nofifyShowMesg("禁止输入表情", mNumber);
                    return;
                }
                if (StringUtils.textIsEmpty(mAddress.getText().toString())) {
                    nofifyShowMesg("请选择省", null);
                    return;
                }
                postBindingBankInfo();
                break;
        }
    }

    /**
     * 提交状态
     *
     * @return
     */
    private boolean judgeSumbit() {
        if (StringUtils.textIsEmpty(mBank.getText().toString())) {
            return false;
        }
        if (StringUtils.textIsEmpty(mBanksName.getText().toString())) {
            return false;
        }
        if (StringUtils.textIsEmpty(mNumber.getText().toString())) {
            return false;
        }
        if (StringUtils.textIsEmpty(mAddress.getText().toString())) {
            return false;
        }

        return true;
    }
  private   String bankNum="";
    /**
     * 请求网络
     */
    private void postBindingBankInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        List<String> selectBank = ContentUtil.selectBank();
        int bankType = 0;
        for (int i = 0; i < selectBank.size(); i++) {
            if (selectBank.get(i).equals(mBank.getText().toString().trim())) {
                bankType = i + 1;
                break;
            }
        }
          bankNum = mNumber.getText().toString().trim().replace(" ", "");
        map.put("bankType", bankType);
        map.put("bankNum", mNumber.getText().toString().trim().replace(" ", ""));
        map.put("bankSub", mBanksName.getText().toString().trim());


        //绑定数据，给上个页面刷新
        myBankCardBean.setBankTypeName(mBank.getText().toString().trim());
        myBankCardBean.setBankMan(mNumber.getText().toString().trim().replace(" ", ""));
        myBankCardBean.setBankType(bankType);
        myBankCardBean.setBankSub(mBanksName.getText().toString().trim());
        if (!selectAddress) {
            myBankCardBean.setBankProvince(address.cityId + "");
            myBankCardBean.setBankCity(address.districtId + "");
            map.put("bankProvince", address.cityId + "");
            map.put("bankCity", address.districtId + "");
        } else {
            map.put("bankProvince", (address.cityId + "").substring(0, 2));
            map.put("bankCity", (address.districtId + "").substring(0, 4));
            myBankCardBean.setBankProvince((address.cityId + "").substring(0, 2));
            myBankCardBean.setBankCity((address.districtId + "").substring(0, 4));
        }

        httpService.bindingBankInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String methodCode) {
                        sucessResult();

                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 ) {
                            sucessResult();
                        } else {
                            ToastUtil.showShort(ex.getMessage());
                        }
                    }
                });

    }
    private void sucessResult(){
        dialog = ConfirmDialog.newInstance("", "绑定成功,提现金额将会打入尾号为[" + bankNum.substring(bankNum.length() - 4, bankNum.length()) + "]？", "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.TEA__BINDING_32012);
                        bundle.putParcelable(StringUtils.EVENT_DATA, myBankCardBean);
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                        break;
                }
            }
        });



    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    /****
     * 显示提示信息
     *
     * @param mesg
     */
    private void nofifyShowMesg(String mesg, final EditText currentEdit) {
        ConfirmDialog dialog = ConfirmDialog.newInstance("", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        if (currentEdit == null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }

}
