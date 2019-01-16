package com.shengzhe.disan.xuetangteacher.bean;

import java.util.List;

/**
 * Created by acer on 2018/3/23.
 */

public class WalletDetailsBean {
    private long sumsettlementAmount;

    public List<WalletBean> getListMap() {
        return listMap;
    }

    public void setListMap(List<WalletBean> listMap) {
        this.listMap = listMap;
    }

    public long getSumsettlementAmount() {
        return sumsettlementAmount;
    }

    public void setSumsettlementAmount(long sumsettlementAmount) {
        this.sumsettlementAmount = sumsettlementAmount;
    }

    private List<WalletBean> listMap;
}
