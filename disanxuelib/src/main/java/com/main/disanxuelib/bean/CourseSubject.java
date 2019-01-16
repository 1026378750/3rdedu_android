package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/27.
 */

public class CourseSubject implements Parcelable {
    public int id=0;
    public String name="";
    public String url="";

    public CourseSubject(){};

    protected CourseSubject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
    }

    public void formatJson(JSONObject jsonObject){
        id = jsonObject.optInt("gradeId");
        name = jsonObject.optString("gradeName");
    }

    public static final Creator<CourseSubject> CREATOR = new Creator<CourseSubject>() {
        @Override
        public CourseSubject createFromParcel(Parcel in) {
            return new CourseSubject(in);
        }

        @Override
        public CourseSubject[] newArray(int size) {
            return new CourseSubject[size];
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
        dest.writeString(url);
    }
}
