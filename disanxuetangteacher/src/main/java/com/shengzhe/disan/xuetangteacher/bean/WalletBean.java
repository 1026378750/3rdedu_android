package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/31.
 */

public class WalletBean implements Parcelable {
    public String courseName;
    public long settlementPrice;
    public long lastAccessTime;
    public String teachingMethodName;
    public String userName;
    public int courseType;
    public int teachingMethod;//授课方式值
    public int directType;//直播类型值


    protected WalletBean(Parcel in) {
        courseName = in.readString();
        settlementPrice = in.readLong();
        lastAccessTime = in.readLong();
        teachingMethodName = in.readString();
        userName = in.readString();
        courseType = in.readInt();
        teachingMethod=in.readInt();
        directType=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeLong(settlementPrice);
        dest.writeLong(lastAccessTime);
        dest.writeString(teachingMethodName);
        dest.writeString(userName);
        dest.writeInt(courseType);
        dest.writeInt(teachingMethod);
        dest.writeInt(directType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletBean> CREATOR = new Creator<WalletBean>() {
        @Override
        public WalletBean createFromParcel(Parcel in) {
            return new WalletBean(in);
        }

        @Override
        public WalletBean[] newArray(int size) {
            return new WalletBean[size];
        }
    };
}
