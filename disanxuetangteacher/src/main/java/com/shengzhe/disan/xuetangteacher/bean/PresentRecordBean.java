package com.shengzhe.disan.xuetangteacher.bean;

import java.util.List;

/**
 * Created by acer on 2018/3/14.
 */

public class PresentRecordBean {
    private long timeDay;
    private List<WithdrawalsRecordListBean> withdrawalsRecordList;

    public long getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(long timeDay) {
        this.timeDay = timeDay;
    }

    public List<WithdrawalsRecordListBean> getWithdrawalsRecordList() {
        return withdrawalsRecordList;
    }

    public void setWithdrawalsRecordList(List<WithdrawalsRecordListBean> withdrawalsRecordList) {
        this.withdrawalsRecordList = withdrawalsRecordList;
    }


}
