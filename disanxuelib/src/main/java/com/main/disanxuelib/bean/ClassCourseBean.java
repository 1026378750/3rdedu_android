package com.main.disanxuelib.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/17.
 */

public class ClassCourseBean implements Parcelable{
    //课程编号
    public int courseId = -1;
    //老师编号
    public int teacherId = -1;
    //老师图片地址
    public String teacherUrl = "";
    //图片地址
    public String teacherName = "";
    //图片地址
    public String pictureUrl = "";
    //课程名称
    public String courseName = "";
    //课程名称（临时字段）
    public String courseName2 = "";
    //科目名称
    public String subjectName = "";
    //阶段年级id
    public int gradeId = -1;
    //阶段名称
    public String gradeName = "";
    //地址信息
    public CityBean address;
    //详细地址
    public String addressStr = "";
    //已有人数
    public int volume = 0;
    //上课人数
    public int salesVolume = 0;
    //课程次数
    public int courseTimes = 0;
    //单次课时长
    public int singleTime = 0;
    //单次课价格
    public long singlePrice = 0;
    //体验次数
    public int mExperience = 0;
    //插班限制
    public int mInlimit = -1;
    //课程简介
    public String introduction = "";
    //教学目标
    public String target = "";
    //适合人群
    public String fitCrowd = "";

    //上课时间
    public List<Schedule> scheduleList = new ArrayList<>();
    //选择时间
    public ArrayList<Calendar> calList = new ArrayList<>();

    public ClassCourseBean() {

    }


    protected ClassCourseBean(Parcel in) {
        courseId = in.readInt();
        pictureUrl = in.readString();
        courseName = in.readString();
        courseName2 = in.readString();
        subjectName = in.readString();
        gradeId = in.readInt();
        gradeName = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        addressStr = in.readString();
        salesVolume = in.readInt();
        courseTimes = in.readInt();
        singleTime = in.readInt();
        singlePrice = in.readLong();
        mExperience = in.readInt();
        mInlimit = in.readInt();
        introduction = in.readString();
        target = in.readString();
        fitCrowd = in.readString();
        scheduleList = in.createTypedArrayList(Schedule.CREATOR);
        calList = in.createTypedArrayList(Calendar.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseId);
        dest.writeString(pictureUrl);
        dest.writeString(courseName);
        dest.writeString(courseName2);
        dest.writeString(subjectName);
        dest.writeInt(gradeId);
        dest.writeString(gradeName);
        dest.writeParcelable(address, flags);
        dest.writeString(addressStr);
        dest.writeInt(salesVolume);
        dest.writeInt(courseTimes);
        dest.writeInt(singleTime);
        dest.writeLong(singlePrice);
        dest.writeInt(mExperience);
        dest.writeInt(mInlimit);
        dest.writeString(introduction);
        dest.writeString(target);
        dest.writeString(fitCrowd);
        dest.writeTypedList(scheduleList);
        dest.writeTypedList(calList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClassCourseBean> CREATOR = new Creator<ClassCourseBean>() {
        @Override
        public ClassCourseBean createFromParcel(Parcel in) {
            return new ClassCourseBean(in);
        }

        @Override
        public ClassCourseBean[] newArray(int size) {
            return new ClassCourseBean[size];
        }
    };

    /*****
     * 封装请求参数
     * @return
     */
    public Map<String, Object> getAddParameter(){
        Map<String, Object> map = new HashMap<>();
        //课程id，（新增时不填，编辑时必填）
        if(courseId!=-1) {
            //builder.addFormDataPart("courseId",String.valueOf(courseId));
            map.put("courseId",String.valueOf(courseId));
        }
        //课程名称，（新增时必填）
        //builder.addFormDataPart("courseName",courseName);
        map.put("courseName",courseName);
        //年级id
        map.put("gradeId",gradeId);
        //最大人数
        map.put("maxUser",salesVolume);
        //城市code （新增时必填）
        map.put("city",address.cityCode);
        //	详细授课地址 （新增时必填）
        map.put("address",addressStr);
        //课次数，（新增时必填）
        map.put("classTime",courseTimes);
        //单课次时长，（新增时必填）
        map.put("duration",singleTime);
        //单价
        map.put("price",singlePrice);
        //体验次数 （新增时必填）
        map.put("trialNum",mExperience);
        //是否可以插班 0否 1是
        map.put("canJoin",mInlimit);
        //课程简介
        map.put("remark", TextUtils.isEmpty(introduction)==true?"呐，这个人很懒，什么都米有留下 ┐(─__─)┌":introduction);
        //教学目标
        map.put("target", TextUtils.isEmpty(target)==true?"呐，这个人很懒，什么都米有留下 ┐(─__─)┌":target );
        //适应人群
        map.put("crowd",TextUtils.isEmpty(fitCrowd)==true?"呐，这个人很懒，什么都米有留下 ┐(─__─)┌":fitCrowd);
        //直播课次时间，（新增时必填），例如：[{“startTime”:1522555200000,”endTime”:1522562400000,”title”:”asdfsadfsdfasfdasdfas1”}]
        List<Map<String, Object>> newDartas = new ArrayList<>();
        for (Schedule schedule : scheduleList){
            Map<String, Object> parameter = schedule.getAddParameter();
            parameter.put("title",schedule.title);
            newDartas.add(parameter);
        }
        map.put("listAppCourseSquadItem",new Gson().toJson(newDartas));
        return map;
    }
}
