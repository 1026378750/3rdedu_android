package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 日历实体 on 2017/12/5.
 */

public class Calendar implements Parcelable{
    public int id = 0;
    //周几
    public String week = "";
    //yyyy年MM月dd
    public String data = "";
    //HH:mm
    public String time ="";
    //
    public String canTime ="";

    public Calendar() {

    }

    public String getStrTime(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        if(!TextUtils.isEmpty(data) &&!TextUtils.isEmpty(time)&&year == Integer.parseInt(data.substring(0,4))){
            return  data.substring(5) + " " + time;
        }
        String str = TextUtils.isEmpty(data) || TextUtils.isEmpty(time) ? "请选择首次课时间" : data + " " + time;
        //return str.contains("年")?str.substring(str.indexOf("年")+1):str;
        return str;
    }

    public String getDate(){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy年MM月dd").parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }


    protected Calendar(Parcel in) {
        id = in.readInt();
        week = in.readString();
        data = in.readString();
        time = in.readString();
        canTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(week);
        dest.writeString(data);
        dest.writeString(time);
        dest.writeString(canTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Calendar> CREATOR = new Creator<Calendar>() {
        @Override
        public Calendar createFromParcel(Parcel in) {
            return new Calendar(in);
        }

        @Override
        public Calendar[] newArray(int size) {
            return new Calendar[size];
        }
    };
}
