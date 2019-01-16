package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 老师上课时间 on 2017/12/15.
 */

public class CourseDate implements Parcelable{
    //周几可以上课
    public int week;
    //哪个时间段可以上课 1：上午 2：下午 3：晚上
    public String times;

    protected CourseDate(Parcel in) {
        week = in.readInt();
        times = in.readString();
    }

    public static final Creator<CourseDate> CREATOR = new Creator<CourseDate>() {
        @Override
        public CourseDate createFromParcel(Parcel in) {
            return new CourseDate(in);
        }

        @Override
        public CourseDate[] newArray(int size) {
            return new CourseDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(week);
        dest.writeString(times);
    }
}
