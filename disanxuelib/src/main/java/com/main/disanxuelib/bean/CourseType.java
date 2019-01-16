package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class CourseType implements Parcelable {
    public int id=0;
    public String name="";
    public List<CourseSubject> childList = new ArrayList<>();

    public CourseType(){};

    protected CourseType(Parcel in) {
        id = in.readInt();
        name = in.readString();
        childList = in.createTypedArrayList(CourseSubject.CREATOR);
    }

    public void formatJson(JSONObject jsonObject){
        id = jsonObject.optInt("gradePid");
        name = jsonObject.optString("gradePname");
        childList.clear();
        JSONArray arrays = jsonObject.optJSONArray("arrays");
        if(arrays==null||arrays.length()==0){
            return;
        }
        for(int i=0;i<arrays.length();i++){
            CourseSubject courseSubject = new CourseSubject();
            courseSubject.formatJson(arrays.optJSONObject(i));
            childList.add(courseSubject);
        }
    }


    public static final Creator<CourseType> CREATOR = new Creator<CourseType>() {
        @Override
        public CourseType createFromParcel(Parcel in) {
            return new CourseType(in);
        }

        @Override
        public CourseType[] newArray(int size) {
            return new CourseType[size];
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
        dest.writeTypedList(childList);
    }

    public List<String> getChildStrList(){
        List<String> strList = new ArrayList<>();
        strList.clear();
        if (this.childList==null||childList.isEmpty())
            return strList;

        for (CourseSubject child : this.childList){
            strList.add(child.name);
        }
        return strList;
    }

}
