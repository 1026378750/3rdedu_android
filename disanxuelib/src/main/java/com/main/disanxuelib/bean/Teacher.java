package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/27.
 */

public class Teacher implements Parcelable{
    public int id =0;
    public String name="";
    public String sex="";
    public String subject="";
    public String schoolAge="";
    public String address="";
    public String url="";
    public double price=0.00;
    public double oldPrice=0.00;
    public String latelyCourse="";
    public int qualityCertification=0;
    public int realnameCertification=0;
    public int teacherCertification=0;
    public int educationCertification=0;

    public Teacher() {
    }

    protected Teacher(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sex = in.readString();
        subject = in.readString();
        schoolAge = in.readString();
        address = in.readString();
        url = in.readString();
        price = in.readDouble();
        oldPrice = in.readDouble();
        latelyCourse = in.readString();
        qualityCertification = in.readInt();
        realnameCertification = in.readInt();
        teacherCertification = in.readInt();
        educationCertification = in.readInt();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
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
        dest.writeString(sex);
        dest.writeString(subject);
        dest.writeString(schoolAge);
        dest.writeString(address);
        dest.writeString(url);
        dest.writeDouble(price);
        dest.writeDouble(oldPrice);
        dest.writeString(latelyCourse);
        dest.writeInt(qualityCertification);
        dest.writeInt(realnameCertification);
        dest.writeInt(teacherCertification);
        dest.writeInt(educationCertification);
    }
}
