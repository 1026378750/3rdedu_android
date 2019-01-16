package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by acer on 2018/3/15.
 */

public  class WithdrawalsRecordListBean {
    /**
     * id : 1
     * createTime : 1512356466600
     * amount : 1000000
     * auditStatus : 1
     */

    private long id;
    private long createTime;
    private long amount;
    private int auditStatus;

    public boolean isFirstItem() {
        return FirstItem;
    }

    public void setFirstItem(boolean firstItem) {
        FirstItem = firstItem;
    }

    private boolean FirstItem;
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    private String  month;

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    private int dateType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }
}