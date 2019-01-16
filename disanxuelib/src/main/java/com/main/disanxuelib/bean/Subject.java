/**
  * Copyright 2017 bejson.com 
  */
package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auto-generated: 2017-10-30 13:20:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Subject implements Parcelable{
    private String appUrl;
    private int subjectId=0;
    private String subjectName="";

    protected Subject(Parcel in) {
        subjectId = in.readInt();
        subjectName = in.readString();
        appUrl = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };


    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public void setSubjectId(int subjectId) {
         this.subjectId = subjectId;
     }
     public int getSubjectId() {
         return subjectId;
     }

    public void setSubjectName(String subjectName) {
         this.subjectName = subjectName;
     }
     public String getSubjectName() {
         return subjectName;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(subjectId);
        dest.writeString(subjectName);
        dest.writeString(appUrl);
    }
}