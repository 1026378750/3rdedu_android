package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/25.
 */

public class TeachingAreasBean implements Parcelable{
    public String areaCode;
    public String areaId;
    public String areaName;

    public TeachingAreasBean() {

    }
    protected TeachingAreasBean(Parcel in) {
        areaId = in.readString();
        areaName = in.readString();
        areaCode = in.readString();
    }

    public static final Creator<TeachingAreasBean> CREATOR = new Creator<TeachingAreasBean>() {
        @Override
        public TeachingAreasBean createFromParcel(Parcel in) {
            return new TeachingAreasBean(in);
        }

        @Override
        public TeachingAreasBean[] newArray(int size) {
            return new TeachingAreasBean[size];
        }
    };

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaId);
        dest.writeString(areaName);
        dest.writeString(areaCode);
    }
}
