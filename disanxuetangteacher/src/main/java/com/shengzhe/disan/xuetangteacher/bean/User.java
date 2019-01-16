package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by acer on 2017/11/23.
 */

public class User {
    private String photoUrl = "";//	用户头像
    private int isFirstLogin = 1;// 是否首次登录 1：是 2：否
    private String mobile = "";//	手机号码
    private String nickName = "";//	用户昵称
    private String token = "";//	 登录返回token
    private String code = "";//	验证码
    private String registrationId = "";//推送id
    private Integer sex;//性别
    private String subjectName = "";//	科目名称
    private int teachingAge;//	教龄
    private int identity;//教师身份0自由老师，1平台教师

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    private String city;//城市Cody
    private String cityName;//城市名称

    public int getIsOpenCity() {
        return isOpenCity;
    }

    public void setIsOpenCity(int isOpenCity) {
        this.isOpenCity = isOpenCity;
    }

    private int isOpenCity;//是否是开通线下教育的城市 1、不是 2、是
    public int getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(int assistantId) {
        this.assistantId = assistantId;
    }

    public int getIsStartCourse() {
        return isStartCourse;
    }

    public void setIsStartCourse(int isStartCourse) {
        this.isStartCourse = isStartCourse;
    }

    private int assistantId;//助教id，为0或null时没有绑定助教
    private int isStartCourse;//是否开课设置 1 未开课设置 2 已开课设置

    public int getTeachingAge() {
        return teachingAge;
    }

    public void setTeachingAge(int teachingAge) {
        this.teachingAge = teachingAge;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }


    public int getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(int isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
