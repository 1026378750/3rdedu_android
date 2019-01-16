package com.shengzhe.disan.xuetangparent.bean;

/**
 * Created by acer on 2017/12/8.
 */

public class MyBalance {
    //账户id
    private int accountId;
    //总余额
    private long balanceAmount;
    //可用金额
    private long availAmount;
    //锁定金额
    private long lockAmount;

    public int getIsWithdrawAppy() {
        return isWithdrawAppy;
    }

    public void setIsWithdrawAppy(int isWithdrawAppy) {
        this.isWithdrawAppy = isWithdrawAppy;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    //今天是否提现，大于0则是
    private int isWithdrawAppy;
    //银行卡号，为空则没有绑定银行卡，不为空则绑定过
    private String bankNum;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public long getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(long balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public long getAvailAmount() {
        return availAmount;
    }

    public void setAvailAmount(long availAmount) {
        this.availAmount = availAmount;
    }

    public long getLockAmount() {
        return lockAmount;
    }

    public void setLockAmount(long lockAmount) {
        this.lockAmount = lockAmount;
    }
}
