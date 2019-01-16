package com.shengzhe.disan.xuetangteacher.bean;

import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

/**
 * Created by acer on 2018/1/24.
 */

public class RealNameVerify {
    //	真实姓名
    private String name = "";
    //身份证号码
    private String cardNo= "";
    //实名认证状态 0 未认证 1 审核中 2 已认证 3 已驳回
    private int cardApprStatus = -1;
    //身份证正面URL
    private String cardFrontUrl = "";
    //手持身份证URL
    private String cardUrl="";
    //身份证反面URL
    private String cardBackUrl="";

    public String getName() {
        return StringUtils.textIsEmpty(name)?"":name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return StringUtils.textIsEmpty(cardNo)?"":cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
    }

    public String getCardFrontUrl() {
        return StringUtils.textIsEmpty(cardFrontUrl)?"":cardFrontUrl;
    }

    public void setCardFrontUrl(String cardFrontUrl) {
        this.cardFrontUrl = cardFrontUrl;
    }

    public String getCardUrl() {
        return StringUtils.textIsEmpty(cardUrl)?"":cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getCardBackUrl() {
        return StringUtils.textIsEmpty(cardBackUrl)?"":cardBackUrl;
    }

    public void setCardBackUrl(String cardBackUrl) {
        this.cardBackUrl = cardBackUrl;
    }
}
