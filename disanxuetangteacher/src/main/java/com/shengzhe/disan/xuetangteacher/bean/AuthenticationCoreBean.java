package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by acer on 2018/1/22.
 */

public class AuthenticationCoreBean {
    //实名认证状态 0 未认证 1 审核中 2 已认证 3 已驳回
    private int cardApprStatus = 0;
    //教师资格认证状态 0 未认证 1 审核中 2 已认证 3 已驳回
    private int qtsStatus = -1;
    //学历认证状态 0 未认证 1 审核中 2 已认证 3 已驳回
    private int quaStatus =-1;
    //专业资质状态认证 0 未认证 1 审核中 2 已认证 3 已驳回
    private int ipmpStatus =-1;
    //教师资格认证url
    private String teachingCertifyUrl = "";
    //专业次质认证url
    private String professionaCertifyUrl = "";
    //学历认证url
    private String eduCertifyUrl = "";

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
    }

    public int getQtsStatus() {
        return qtsStatus;
    }

    public void setQtsStatus(int qtsStatus) {
        this.qtsStatus = qtsStatus;
    }

    public int getQuaStatus() {
        return quaStatus;
    }

    public void setQuaStatus(int quaStatus) {
        this.quaStatus = quaStatus;
    }

    public int getIpmpStatus() {
        return ipmpStatus;
    }

    public void setIpmpStatus(int ipmpStatus) {
        this.ipmpStatus = ipmpStatus;
    }

    public String getTeachingCertifyUrl() {
        return teachingCertifyUrl;
    }

    public void setTeachingCertifyUrl(String teachingCertifyUrl) {
        this.teachingCertifyUrl = teachingCertifyUrl;
    }

    public String getProfessionaCertifyUrl() {
        return professionaCertifyUrl;
    }

    public void setProfessionaCertifyUrl(String professionaCertifyUrl) {
        this.professionaCertifyUrl = professionaCertifyUrl;
    }

    public String getEduCertifyUrl() {
        return eduCertifyUrl;
    }

    public void setEduCertifyUrl(String eduCertifyUrl) {
        this.eduCertifyUrl = eduCertifyUrl;
    }
}
