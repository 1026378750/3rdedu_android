package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2017/12/4.
 */

public class CityBean implements Parcelable{
    public String cityName = "";//开通城市
    public String cityCode = "";//开通code

    public CityBean() {

    }

    protected CityBean(Parcel in) {
        cityName = in.readString();
        cityCode = in.readString();
    }

    public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel in) {
            return new CityBean(in);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(cityCode);
    }
}
