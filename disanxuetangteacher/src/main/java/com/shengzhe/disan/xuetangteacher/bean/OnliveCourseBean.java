package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.Schedule;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/1/17.
 */

public class OnliveCourseBean implements Parcelable{
    //课程编号
    public int courseId = -1;
    //图片地址
    public String pictureUrl = "";
    //课程名称
    public String courseName = "";
    //科目名称
    public String subjectName = "";
    //阶段年级id
    public int gradeId = -1;
    //阶段名称
    public String gradeName = "";
    //直播类型
    public int directType = -1;
    //直播类型
    public String directTypeName = "";
    //上课人数
    public int salesVolume = 0;
    //课程次数
    public int courseTimes = 0;
    //单次课时长
    public int singleTime = 0;
    //单次课价格
    public long singlePrice = 0;
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

    public OnliveCourseBean() {

    }

    protected OnliveCourseBean(Parcel in) {
        courseId = in.readInt();
        pictureUrl = in.readString();
        courseName = in.readString();
        subjectName = in.readString();
        gradeId = in.readInt();
        gradeName = in.readString();
        directType = in.readInt();
        directTypeName = in.readString();
        salesVolume = in.readInt();
        courseTimes = in.readInt();
        singleTime = in.readInt();
        singlePrice = in.readLong();
        introduction = in.readString();
        target = in.readString();
        fitCrowd = in.readString();
        scheduleList = in.createTypedArrayList(Schedule.CREATOR);
        calList = in.createTypedArrayList(Calendar.CREATOR);
    }

    public static final Creator<OnliveCourseBean> CREATOR = new Creator<OnliveCourseBean>() {
        @Override
        public OnliveCourseBean createFromParcel(Parcel in) {
            return new OnliveCourseBean(in);
        }

        @Override
        public OnliveCourseBean[] newArray(int size) {
            return new OnliveCourseBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseId);
        dest.writeString(pictureUrl);
        dest.writeString(courseName);
        dest.writeString(subjectName);
        dest.writeInt(gradeId);
        dest.writeString(gradeName);
        dest.writeInt(directType);
        dest.writeString(directTypeName);
        dest.writeInt(salesVolume);
        dest.writeInt(courseTimes);
        dest.writeInt(singleTime);
        dest.writeLong(singlePrice);
        dest.writeString(introduction);
        dest.writeString(target);
        dest.writeString(fitCrowd);
        dest.writeTypedList(scheduleList);
        dest.writeTypedList(calList);
    }

    /*****
     * 封装请求参数
     * @return
     */
    public List<MultipartBody.Part> getAddParameter(){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        File file = new File(pictureUrl);//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
        builder.addFormDataPart("directPhotoFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
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
        map.put("gradeId",String.valueOf(gradeId));
        //	直播类型
        map.put("directType",String.valueOf(directType));
        //最大人数
        map.put("maxUser",String.valueOf(salesVolume));
        //课次数，（新增时必填）
        map.put("classTime",String.valueOf(courseTimes));
        //单课次时长，（新增时必填）
        map.put("duration",String.valueOf(singleTime));
        //单价
        map.put("price",String.valueOf(singlePrice));
        //课程简介
        map.put("courseRemark",TextUtils.isEmpty(introduction)==true?"呐，这个人很懒，什么都米有留下 ┐(─__─)┌":introduction );
        //教学目标
        map.put("target",TextUtils.isEmpty(target)==true?"呐，这个人很懒，什么都米有留下 ┐(─__─)┌":target  );
        //适应人群
        map.put("crowd", TextUtils.isEmpty(fitCrowd)==true?"呐，这个人很懒，什么都米有留下 ┐(─__─)┌":fitCrowd );
        //直播课次时间，（新增时必填），例如：[{“startTime”:1111111111111,”endTime”:22222222222222},…]
        List<Map<String, Object>> newDartas = new ArrayList<>();
        for (Schedule schedule : scheduleList){
            newDartas.add(schedule.getAddParameter());
        }
        map.put("courseItemTimes",new Gson().toJson(newDartas));
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));

        return builder.build().parts();
    }
}
