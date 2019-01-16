package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/1/30.
 */

public class TeachingExperienceMode implements Parcelable {
    public String school;
    public long startTime;
    public long endTime;
    public String remark;
    public int expeId;
    public int id;

    public TeachingExperienceMode() {
    }


    protected TeachingExperienceMode(Parcel in) {
        school = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        remark = in.readString();
        expeId = in.readInt();
        id = in.readInt();
    }

    public static final Creator<TeachingExperienceMode> CREATOR = new Creator<TeachingExperienceMode>() {
        @Override
        public TeachingExperienceMode createFromParcel(Parcel in) {
            return new TeachingExperienceMode(in);
        }

        @Override
        public TeachingExperienceMode[] newArray(int size) {
            return new TeachingExperienceMode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(school);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(remark);
        dest.writeInt(expeId);
        dest.writeInt(id);
    }
}
