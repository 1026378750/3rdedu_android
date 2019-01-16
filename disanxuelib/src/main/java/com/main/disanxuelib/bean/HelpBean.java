package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 帮助实体 on 2018/5/8.
 */

public class HelpBean implements Parcelable{
    public int id ;
    //主标题
    public String title;
    public String context;

    public HelpBean() {

    }

    protected HelpBean(Parcel in) {
        id = in.readInt();
        title = in.readString();
        context = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HelpBean> CREATOR = new Creator<HelpBean>() {
        @Override
        public HelpBean createFromParcel(Parcel in) {
            return new HelpBean(in);
        }

        @Override
        public HelpBean[] newArray(int size) {
            return new HelpBean[size];
        }
    };
}
