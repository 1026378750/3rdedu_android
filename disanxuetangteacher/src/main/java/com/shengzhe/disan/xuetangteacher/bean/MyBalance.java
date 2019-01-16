package com.shengzhe.disan.xuetangteacher.bean;

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
    //已提现金额
    private long totalAmount;

    public long getSumsettlementAmount() {
        return sumsettlementAmount;
    }

    public void setSumsettlementAmount(long sumsettlementAmount) {
        this.sumsettlementAmount = sumsettlementAmount;
    }

    //老师已完成金额 （当当前用户是第三学堂老师时才会有此字段）
    private long sumsettlementAmount;

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public int getIsWithdrawAppy() {
        return isWithdrawAppy;
    }

    public void setIsWithdrawAppy(int isWithdrawAppy) {
        this.isWithdrawAppy = isWithdrawAppy;
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
