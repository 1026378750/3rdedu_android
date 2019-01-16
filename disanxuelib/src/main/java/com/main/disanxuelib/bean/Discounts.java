package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/30.
 */

public class Discounts implements Parcelable {
    public int id =0;
    public String title="" ;
    public String content="" ;
    //购买数量
    public int buyNum=0;
    //赠送数量
    public int giveNum=0;
    //折扣描述
    public String desc;
    //课程时长
    public int classTime;

    public int campusDiscountId;
    public int campusDiscountVersion;

    protected Discounts(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        buyNum = in.readInt();
        giveNum = in.readInt();
        desc = in.readString();
        classTime = in.readInt();
        campusDiscountId=in.readInt();
        campusDiscountVersion=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(buyNum);
        dest.writeInt(giveNum);
        dest.writeString(desc);
        dest.writeInt(classTime);
        dest.writeInt(campusDiscountId);
        dest.writeInt(campusDiscountVersion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Discounts> CREATOR = new Creator<Discounts>() {
        @Override
        public Discounts createFromParcel(Parcel in) {
            return new Discounts(in);
        }

        @Override
        public Discounts[] newArray(int size) {
            return new Discounts[size];
        }
    };
}
