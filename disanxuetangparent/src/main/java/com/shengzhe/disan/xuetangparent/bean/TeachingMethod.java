package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.main.disanxuelib.bean.Discounts;
import com.main.disanxuelib.bean.TeacherMethod;

import java.util.List;

/**
 * Created by acer on 2017/12/7.
 * 线下一对一老师授课方式
 */

public class TeachingMethod implements Parcelable {
    //家长是否设置过上门地址 （YES:是 NO:无）
    //public String isHaveAddress;
    //校区折扣id
    public int campusDiscountId;
    //课程时长
    public int classTime;
    //校区折扣版本
    public int campusDiscountVersion;
    //校区折扣值
    public int campusDiscountPercent;
    //授课方式折扣信息
    public List<TeacherMethod> coursePrices;
    //校区折扣信息
    public List<Discounts> campusDiscountGive;

   /* public boolean getHaveAddress() {
        return isHaveAddress.equals("YES") ? true : false;
    }

    public void setHaveAddress(boolean bool){
        isHaveAddress = bool?"YES":"NO";
    }*/

    public TeachingMethod() {
    }

    protected TeachingMethod(Parcel in) {
       // isHaveAddress = in.readString();
        campusDiscountId = in.readInt();
        classTime = in.readInt();
        campusDiscountVersion = in.readInt();
        campusDiscountPercent = in.readInt();
        coursePrices = in.createTypedArrayList(TeacherMethod.CREATOR);
        campusDiscountGive = in.createTypedArrayList(Discounts.CREATOR);
    }

    public static final Creator<TeachingMethod> CREATOR = new Creator<TeachingMethod>() {
        @Override
        public TeachingMethod createFromParcel(Parcel in) {
            return new TeachingMethod(in);
        }

        @Override
        public TeachingMethod[] newArray(int size) {
            return new TeachingMethod[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(isHaveAddress);
        dest.writeInt(campusDiscountId);
        dest.writeInt(classTime);
        dest.writeInt(campusDiscountVersion);
        dest.writeInt(campusDiscountPercent);
        dest.writeTypedList(coursePrices);
        dest.writeTypedList(campusDiscountGive);
    }
}
