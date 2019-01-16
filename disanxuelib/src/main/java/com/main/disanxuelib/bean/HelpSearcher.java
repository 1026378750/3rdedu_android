package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 搜索结果实体 on 2018/5/8.
 */

public class HelpSearcher implements Parcelable{
    public int id = 0;
    public String title = "";
    public List<HelpBean> helpList = new ArrayList<>();

    public HelpSearcher() {

    }

    protected HelpSearcher(Parcel in) {
        id = in.readInt();
        title = in.readString();
        helpList = in.createTypedArrayList(HelpBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeTypedList(helpList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HelpSearcher> CREATOR = new Creator<HelpSearcher>() {
        @Override
        public HelpSearcher createFromParcel(Parcel in) {
            return new HelpSearcher(in);
        }

        @Override
        public HelpSearcher[] newArray(int size) {
            return new HelpSearcher[size];
        }
    };
}
