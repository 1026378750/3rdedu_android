package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hy on 2018/3/14.
 */

public class UserAssistantMode implements Parcelable {

    /**
     * mobile : 13120850176
     * name : 赵朋
     * photoUrl : http://www.3rd.com/static/images/aslaksdfkjhafskldjfhaksjdfhkjahgfjha.img
     */

    public String mobile;

    public UserAssistantMode() {
    }

    public String name;
    public String photoUrl;


    protected UserAssistantMode(Parcel in) {
        mobile = in.readString();
        name = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<UserAssistantMode> CREATOR = new Creator<UserAssistantMode>() {
        @Override
        public UserAssistantMode createFromParcel(Parcel in) {
            return new UserAssistantMode(in);
        }

        @Override
        public UserAssistantMode[] newArray(int size) {
            return new UserAssistantMode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(photoUrl);
    }
}
