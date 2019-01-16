package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by acer on 2018/3/14.
 */

public class SearchAssistantMode {

    /**
     * id : 1
     * mobile : 13120850176
     * name : 赵朋
     * photoUrl : http://www.3rd.com/status/image/asdkfjhaskd2w3ekjrhwkerjhiuxciuv23kjh34.img
     * campusName : 长宁校区
     */

    private int id;
    private String mobile;
    private String name;
    private String photoUrl;
    private String campusName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }
}
