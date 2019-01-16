package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.BankCardUtils;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class AddBankCardView extends BaseView {
    private MyBankCardBean myBankCardBean;
    private ConfirmDialog dialog;
    private List<City> dataList = new ArrayList<>();
    private Address address;
    private boolean isCanShown = true;
    //上次输入框中的内容
    private String lastString;
    //光标的位置
    private int selectPosition;
    private NiceDialog niceDialog;
    private FragmentManager fragmentManager;

    public AddBankCardView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    private IAddBankCardView iView;

    public void setIAddBankCardView(IAddBankCardView iView) {
        this.iView = iView;
    }

    public void setDatas(MyBankCardBean myBankCardBean) {
        iView.getComfirmView().setEnabled(false);
        isCanShown = true;
        if (myBankCardBean != null) {
            iView.getDollarTitleView().setTitleText("更换银行卡");
            iView.getBankView().setText(myBankCardBean.getBankTypeName());
            iView.getBanksNameView().setText(myBankCardBean.getBankSub());
            String regex = "(.{4})";
            String bankCard = myBankCardBean.getBankNum() + "";
            bankCard = bankCard.replaceAll(regex, "$1 ");
            iView.getNumberView().setText(myBankCardBean.getBankNum() == 0 ? "" : bankCard + "");
            iView.getNumberView().setHint("请输入银行卡号码");

            iView.getAddbankSubView().setText(TextUtils.isEmpty(myBankCardBean.getBankMan()) == true ? "" : myBankCardBean.getBankMan());
            iView.getAddbankSubView().setHint("请输入开户人姓名");

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
                city += " " + address.district;
            } else {
                iView.getDollarTitleView().setTitleText("添加银行卡");
            }
            iView.getAddressView().setText(city);
        }
        if (myBankCardBean == null) {
            myBankCardBean = new MyBankCardBean();
        }
        this.myBankCardBean = myBankCardBean;
        dataList.clear();
        dataList.addAll(CityDaoUtil.getAllCityProvince());

        iView.getNumberView().addTextChangedListener(new TextWatcher() {
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
                String textTrim = iView.getNumberView().getText().toString();
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
                        iView.getNumberView().setText(stringBuffer.toString());
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
                String etContent = iView.getNumberView().getText().toString();
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
                    iView.getNumberView().setText(newContent);
                    //保证光标的位置
                    iView.getNumberView().setSelection(selectPosition > newContent.length() ? newContent.length() : selectPosition);
                }
                iView.getComfirmView().setEnabled(judgeSumbit());
            }
        });
        iView.getBanksNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iView.getComfirmView().setEnabled(judgeSumbit());
            }
        });
        iView.getAddbankSubView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iView.getComfirmView().setEnabled(judgeSumbit());
            }
        });
    }

    public void selectBank() {
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
                        if (TextUtils.isEmpty(iView.getBankView().getText().toString().trim())) {
                            pickerView.setShowStringPicker("中国银行", "");
                        } else {
                            pickerView.setShowStringPicker(iView.getBankView().getText().toString().trim(), "");
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
                                colseDialog();
                                iView.getBanksNameView().setText("");
                                iView.getNumberView().setText("");
                                iView.getBankView().setText(obj.toString());
                                iView.getComfirmView().setEnabled(judgeSumbit());
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
    }

    private boolean judgeSumbit() {
        if (StringUtils.textIsEmpty(iView.getBankView().getText().toString())) {
            return false;
        }
        if (StringUtils.textIsEmpty(iView.getBanksNameView().getText().toString())) {
            return false;
        }
        if (StringUtils.textIsEmpty(iView.getNumberView().getText().toString())) {
            return false;
        }
        if (StringUtils.textIsEmpty(iView.getAddressView().getText().toString())) {
            return false;
        }

        if (StringUtils.textIsEmpty(iView.getAddbankSubView().getText().toString())) {
            return false;
        }

        return true;
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    public void selectCity() {
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
                                isCanShown = true;
                                address = (Address) obj;
                                iView.getAddressView().setText(address.city + " " + (address.district.equals("暂无") ? "" : address.district));
                                iView.getComfirmView().setEnabled(judgeSumbit());
                                colseDialog();
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
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
                .show(fragmentManager);
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

    public boolean buindingBank() {
        if (StringUtils.textIsEmpty(iView.getBankView().getText().toString())) {
            nofifyShowMesg("请选择银行", null);
            return false;
        }
        myBankCardBean.setBankTypeName(iView.getBankView().getText().toString().trim());

        if (StringUtils.textIsEmpty(iView.getBanksNameView().getText().toString())) {
            nofifyShowMesg("请输入开户支行", null);
            return false;
        }
        myBankCardBean.setBankSub(iView.getBanksNameView().getText().toString().trim());

        if (StringUtils.textIsEmpty(iView.getNumberView().getText().toString())) {
            nofifyShowMesg("请输入银行卡号码", null);
            return false;
        }
        String bankNum = iView.getNumberView().getText().toString().trim().replace(" ", "");
        if (!BankCardUtils.checkBankCard(bankNum)) {
            nofifyShowMesg("请输入正确银行卡号码", null);
            return false;
        }
        myBankCardBean.setBankNum(Long.parseLong(bankNum));

        if (RegUtil.containsEmoji(iView.getNumberView().getText().toString())) {
            nofifyShowMesg("禁止输入表情", iView.getNumberView());
            return false;
        }
        myBankCardBean.setBankMan(iView.getNumberView().getText().toString().trim().replace(" ", ""));

        if (RegUtil.checkBankCard(iView.getAddbankSubView().getText().toString())) {
            nofifyShowMesg("请输入开户人姓名", null);
            return false;
        }
        if (RegUtil.containsEmoji(iView.getAddbankSubView().getText().toString())) {
            nofifyShowMesg("禁止输入表情",iView.getAddbankSubView());
            return false;
        }
        myBankCardBean.setOpenMan(iView.getAddbankSubView().getText().toString().trim());

        if (StringUtils.textIsEmpty(iView.getAddressView().getText().toString())) {
            nofifyShowMesg("请选择省", null);
            return false;
        }
        myBankCardBean.setBankProvince((address.cityId + "").substring(0, 2));
        myBankCardBean.setBankCity((address.districtId + "").substring(0, 4));
        return true;
    }

    public MyBankCardBean getMyBankCard(){
        List<String> selectBank = ContentUtil.selectBank();
        int bankType = 0;
        for (int i = 0; i < selectBank.size(); i++) {
            if (selectBank.get(i).equals(iView.getBankView().getText().toString().trim())) {
                bankType = i + 1;
                break;
            }
        }
        myBankCardBean.setBankType(bankType);
        return myBankCardBean;
    }

    public void setBindingBank() {
        String bankNum = iView.getNumberView().getText().toString().trim().replace(" ", "");
        if (dialog==null)
            dialog = ConfirmDialog.newInstance("", "绑定成功,提现金额将会打入尾号为[" + bankNum.substring(bankNum.length() - 4, bankNum.length()) + "]？", "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(fragmentManager);
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.TEA__BINDING_32012);
                        bundle.putParcelable(StringUtils.EVENT_DATA, myBankCardBean);
                        EventBus.getDefault().post(bundle);
                        AppManager.getAppManager().currentActivity().onBackPressed();
                        break;
                }
            }
        });
    }

    public interface IAddBankCardView {
        TextView getBankView();
        EditText getBanksNameView();
        EditText getNumberView();
        TextView getAddressView();
        EditText getAddbankSubView();
        Button getComfirmView();
        CommonCrosswiseBar getDollarTitleView();
    }

}
