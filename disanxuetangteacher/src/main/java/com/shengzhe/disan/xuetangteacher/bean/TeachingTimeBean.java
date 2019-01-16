package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/25.
 */

public class TeachingTimeBean implements Parcelable{
    private String times;
    private int week;

    public TeachingTimeBean() {

    }
    protected TeachingTimeBean(Parcel in) {
        times = in.readString();
        week = in.readInt();
    }

    public static final Creator<TeachingTimeBean> CREATOR = new Creator<TeachingTimeBean>() {
        @Override
        public TeachingTimeBean createFromParcel(Parcel in) {
            return new TeachingTimeBean(in);
        }

        @Override
        public TeachingTimeBean[] newArray(int size) {
            return new TeachingTimeBean[size];
        }
    };

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(times);
        dest.writeInt(week);
    }
}
