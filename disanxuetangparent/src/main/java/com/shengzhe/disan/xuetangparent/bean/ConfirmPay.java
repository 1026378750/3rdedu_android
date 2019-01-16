package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 支付用 on 2017/12/12.
 */
public class ConfirmPay implements Parcelable{
    //订单编号
    public int orderId = 0;
    //课程总额
    public long totalPrice = 0;
    //课程编号
    public int courseId = 0;
    //课程折扣后价格
    public long paidAmount = 0;

    public ConfirmPay(int orderId,long totalPrice,int courseId,long paidAmount) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.courseId = courseId;
        this.paidAmount = paidAmount;
    }


    protected ConfirmPay(Parcel in) {
        orderId = in.readInt();
        totalPrice = in.readLong();
        courseId = in.readInt();
        paidAmount = in.readLong();
    }

    public static final Creator<ConfirmPay> CREATOR = new Creator<ConfirmPay>() {
        @Override
        public ConfirmPay createFromParcel(Parcel in) {
            return new ConfirmPay(in);
        }

        @Override
        public ConfirmPay[] newArray(int size) {
            return new ConfirmPay[size];
        }
    };

    @Override
    public String toString() {
        return "ConfirmPay{" +
                "orderId=" + orderId +
                ", totalPrice=" + totalPrice +
                ", courseId=" + courseId +
                ", paidAmount=" + paidAmount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeLong(totalPrice);
        dest.writeInt(courseId);
        dest.writeLong(paidAmount);
    }
}
