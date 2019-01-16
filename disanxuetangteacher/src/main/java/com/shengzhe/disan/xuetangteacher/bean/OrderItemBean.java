package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by acer on 2018/1/31.
 */

public class OrderItemBean implements Parcelable {

    /**
     * number : 1
     * startTime : 1514448000000
     */

    public int number;
    public long startTime;

    protected OrderItemBean(Parcel in) {
        number = in.readInt();
        startTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeLong(startTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderItemBean> CREATOR = new Creator<OrderItemBean>() {
        @Override
        public OrderItemBean createFromParcel(Parcel in) {
            return new OrderItemBean(in);
        }

        @Override
        public OrderItemBean[] newArray(int size) {
            return new OrderItemBean[size];
        }
    };
}
