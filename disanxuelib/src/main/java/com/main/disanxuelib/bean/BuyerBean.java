package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 参加人员实体 on 2018/4/2.
 */

public class BuyerBean  implements Parcelable {
    public int isJoin;
    public String photoUrl;
    public int buyNum;
    public int classSum;
    public String studentName;

    public BuyerBean() {
    }

    public String mobile;
    public int giveNum;
    public int areadyNum;
    public String orderCode;
    public long buyerTime;

    protected BuyerBean(Parcel in) {
        isJoin = in.readInt();
        photoUrl = in.readString();
        buyNum = in.readInt();
        classSum = in.readInt();
        studentName = in.readString();
        mobile = in.readString();
        giveNum = in.readInt();
        areadyNum = in.readInt();
        orderCode = in.readString();
        buyerTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isJoin);
        dest.writeString(photoUrl);
        dest.writeInt(buyNum);
        dest.writeInt(classSum);
        dest.writeString(studentName);
        dest.writeString(mobile);
        dest.writeInt(giveNum);
        dest.writeInt(areadyNum);
        dest.writeString(orderCode);
        dest.writeLong(buyerTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BuyerBean> CREATOR = new Creator<BuyerBean>() {
        @Override
        public BuyerBean createFromParcel(Parcel in) {
            return new BuyerBean(in);
        }

        @Override
        public BuyerBean[] newArray(int size) {
            return new BuyerBean[size];
        }
    };
}
