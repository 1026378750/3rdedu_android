package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 校区信息 on 2018/1/19.
 */

public class CampusBean implements Parcelable{
    //校区自增id
    public int id;
    //校区城市名称
    public String campusCity;
    //校区区域名称
    public String campusArea;
    //校区城市code
    public String campusCityCode;
    //校区区域code
    public String campusAreaCode;
    //校区详细地址
    public String address;
    //校区名称
    public String campusName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusCity() {
        return campusCity;
    }

    public void setCampusCity(String campusCity) {
        this.campusCity = campusCity;
    }

    public String getCampusArea() {
        return campusArea;
    }

    public void setCampusArea(String campusArea) {
        this.campusArea = campusArea;
    }

    public String getCampusCityCode() {
        return campusCityCode;
    }

    public void setCampusCityCode(String campusCityCode) {
        this.campusCityCode = campusCityCode;
    }

    public String getCampusAreaCode() {
        return campusAreaCode;
    }

    public void setCampusAreaCode(String campusAreaCode) {
        this.campusAreaCode = campusAreaCode;
    }


    public CampusBean() {

    }

    protected CampusBean(Parcel in) {
        id = in.readInt();
        campusName=in.readString();
        campusCity=in.readString();
        campusArea=in.readString();
        campusCityCode=in.readString();
        campusAreaCode=in.readString();
        address=in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(campusName);
        dest.writeString(campusCity);
        dest.writeString(campusArea);
        dest.writeString(campusCityCode);
        dest.writeString(campusAreaCode);
        dest.writeString(address);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampusBean> CREATOR = new Creator<CampusBean>() {
        @Override
        public CampusBean createFromParcel(Parcel in) {
            return new CampusBean(in);
        }

        @Override
        public CampusBean[] newArray(int size) {
            return new CampusBean[size];
        }
    };
}
