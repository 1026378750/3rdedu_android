package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/3/16.
 */

public class MyBankCardBean implements Parcelable {

    /**
     * bankInfoId : 1   银行卡号，是否绑定银行卡判断此字段
     * bankNum : 6215555555555555555  银行卡号，是否绑定银行卡判断此字段
     * bankTypeName : 宇宙银行  开户银行名称
     * bankType : 3   	银行类型
     * bankMan : 比鲁斯  开户人
     * cardApprStatus : 1//实名认证状态 0 认证 2 认证 1 审核中 3 已驳回（家长端时没有此字段）
     * bankProvince :开户省份
     * bankCity :开户城市
     * bankSub :开户支行
     */

    private long bankInfoId;

    public MyBankCardBean() {
    }

    private long bankNum;
    private String bankTypeName;
    private int bankType;
    private String bankMan;
    private int cardApprStatus;
    private String bankProvince;
    private String bankCity;
    private String bankSub;

    public String getOpenMan() {
        return openMan;
    }

    public void setOpenMan(String openMan) {
        this.openMan = openMan;
    }

    private String openMan;

    protected MyBankCardBean(Parcel in) {
        bankInfoId = in.readLong();
        bankNum = in.readLong();
        bankTypeName = in.readString();
        bankType = in.readInt();
        bankMan = in.readString();
        cardApprStatus = in.readInt();
        bankProvince = in.readString();
        bankCity = in.readString();
        bankSub = in.readString();
        openMan=in.readString();
    }

    public static final Creator<MyBankCardBean> CREATOR = new Creator<MyBankCardBean>() {
        @Override
        public MyBankCardBean createFromParcel(Parcel in) {
            return new MyBankCardBean(in);
        }

        @Override
        public MyBankCardBean[] newArray(int size) {
            return new MyBankCardBean[size];
        }
    };

    public long getBankInfoId() {
        return bankInfoId;
    }

    public void setBankInfoId(long bankInfoId) {
        this.bankInfoId = bankInfoId;
    }

    public long getBankNum() {
        return bankNum;
    }

    public void setBankNum(long bankNum) {
        this.bankNum = bankNum;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public int getBankType() {
        return bankType;
    }

    public void setBankType(int bankType) {
        this.bankType = bankType;
    }

    public String getBankMan() {
        return bankMan;
    }

    public void setBankMan(String bankMan) {
        this.bankMan = bankMan;
    }

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankSub() {
        return bankSub;
    }

    public void setBankSub(String bankSub) {
        this.bankSub = bankSub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(bankInfoId);
        dest.writeLong(bankNum);
        dest.writeString(bankTypeName);
        dest.writeInt(bankType);
        dest.writeString(bankMan);
        dest.writeInt(cardApprStatus);
        dest.writeString(bankProvince);
        dest.writeString(bankCity);
        dest.writeString(bankSub);
        dest.writeString(openMan);
    }
}
