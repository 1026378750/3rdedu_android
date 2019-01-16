package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by liukui on 2017/11/29.
 * <p>
 * 课程表
 */

public class Schedule implements Parcelable {
    public int id = 0;
    public String name = "";
    public String desc = "";
    public String date = "";
    public String time = "";
    public String status = "";
    public String week = "";
    public String startTime = "";
    public String canTime = "";
    public String title = "";

    public Schedule() {

    }

    public Map<String, Object> getAddParameter() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String[] array = time.split("-");
            //开始时间
            map.put("startTime", Long.parseLong(DateUtil.dateTimeStamp(date + " " + array[0], "yyyy-MM-dd HH:mm")));
            //结束时间
            if (array[1].contains("次日")) {
                java.util.Calendar calendar = DateUtil.getAfterDay(DateUtil.timeStamp2Calendar(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime()));
                date = DateUtil.forMatDateYYMMDD(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            }
            map.put("endTime", Long.parseLong(DateUtil.dateTimeStamp(date + " " + (array[1].contains("次日")?array[1].replace("次日",""):array[1]), "yyyy-MM-dd HH:mm")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }


    protected Schedule(Parcel in) {
        id = in.readInt();
        name = in.readString();
        desc = in.readString();
        date = in.readString();
        time = in.readString();
        status = in.readString();
        week = in.readString();
        startTime = in.readString();
        canTime = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(week);
        dest.writeString(startTime);
        dest.writeString(canTime);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", week='" + week + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

}
