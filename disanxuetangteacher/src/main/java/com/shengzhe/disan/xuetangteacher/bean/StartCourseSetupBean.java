package com.shengzhe.disan.xuetangteacher.bean;

import java.util.List;

/**
 * 开课设置数据
 * Created by acer on 2018/1/25.
 */

public class StartCourseSetupBean {
    //城市名称
    private String city = "";
    //区域名称
    private String area ="";
    //城市code
    private String cityCode ="";
    //区域code
    private String areaCode="";
    //老师详细地址
    private String address="";
    //是否第三学堂老师
    private int identity=0;
    //校区信息
    private CampusBean campus;
    //授课区域
    private List<TeachingAreasBean> teachingAreas;
    //授课时间
    private List<TeachingTimeBean> teachingTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public CampusBean getCampus() {
        return campus;
    }

    public void setCampus(CampusBean campus) {
        this.campus = campus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public List<TeachingAreasBean> getTeachingAreas() {
        return teachingAreas;
    }

    public void setTeachingAreas(List<TeachingAreasBean> teachingAreas) {
        this.teachingAreas = teachingAreas;
    }

    public List<TeachingTimeBean> getTeachingTime() {
        return teachingTime;
    }

    public void setTeachingTime(List<TeachingTimeBean> teachingTime) {
        this.teachingTime = teachingTime;
    }


}
