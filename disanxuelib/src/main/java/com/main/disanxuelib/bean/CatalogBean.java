package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.main.disanxuelib.util.DateUtil;

/**
 * Created by 课程目录 on 2018/4/2.
 */

public class CatalogBean implements Parcelable{
    public int id;
    public String name;
    public long times;
    public String status;

    protected CatalogBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        times = in.readLong();
        status = in.readString();
    }

    public static final Creator<CatalogBean> CREATOR = new Creator<CatalogBean>() {
        @Override
        public CatalogBean createFromParcel(Parcel in) {
            return new CatalogBean(in);
        }

        @Override
        public CatalogBean[] newArray(int size) {
            return new CatalogBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(times);
        dest.writeString(status);
    }

    public String getTimesStr() {
        return "测试数据";
    }
}
