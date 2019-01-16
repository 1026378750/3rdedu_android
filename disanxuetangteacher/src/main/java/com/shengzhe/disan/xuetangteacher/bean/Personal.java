package com.shengzhe.disan.xuetangteacher.bean;

import android.text.TextUtils;

/**
 * Created by acer on 2017/12/8.
 */
//个人中心
public class Personal {

    //头像地址
    private String photoUrl;
    //学生名字
    private String studenName;
    //学生年级名称
    private String gradeName;
    //城市code
    private String city;
    //区域code
    private String area;
    //学生地址
    private String address;
    //学生就读学校
    private String school;
    //家长昵称
    private String nickName;
    //学生性别
    private int sex;

    public String getPhotoUrl() {
        return TextUtils.isEmpty(photoUrl)?"":photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStudenName() {
        return TextUtils.isEmpty(studenName)?"":studenName;
    }

    public void setStudenName(String studenName) {
        this.studenName = studenName;
    }

    public String getGradeName() {
        return TextUtils.isEmpty(gradeName)?"":gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getCity() {
        return TextUtils.isEmpty(city)?"":city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return TextUtils.isEmpty(area)?"":area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return TextUtils.isEmpty(address)?"":address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return TextUtils.isEmpty(school)?"":school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getNickName() {
        return TextUtils.isEmpty(nickName)?"":nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
