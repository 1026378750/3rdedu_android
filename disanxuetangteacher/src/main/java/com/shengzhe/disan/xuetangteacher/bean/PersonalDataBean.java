package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;

/**
 * Created by acer on 2018/1/22.
 */

public class PersonalDataBean implements Parcelable {
    //头像
    private String photoUrl = "";
    private int teachingExperienceNum = -1;
    //用户昵称
    private String nickName = "";
    //毕业学校
    private String geaduateSchool = "";
    //专业
    private String profession = "";
    //最高学历
    private int edu = -1;
    //老师性别
    private int sex = -1;
    //个人简历
    private String personalResume = "";
    //教龄
    private int teachingAge = 0;
    //科目id
    private int subjectId = -1;
    //科目名称
    private String subjectName = "";

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    //城市名称
    private String cityName="";
    //城市名称
    private String city="0";

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    protected PersonalDataBean(Parcel in) {
        photoUrl = in.readString();
        teachingExperienceNum = in.readInt();
        nickName = in.readString();
        geaduateSchool = in.readString();
        profession = in.readString();
        edu = in.readInt();
        sex = in.readInt();
        personalResume = in.readString();
        teachingAge = in.readInt();
        subjectName = in.readString();
        subjectId = in.readInt();
        cityName=in.readString();
        city=in.readString();
    }

    public PersonalDataBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoUrl);
        dest.writeInt(teachingExperienceNum);
        dest.writeString(nickName);
        dest.writeString(geaduateSchool);
        dest.writeString(profession);
        dest.writeInt(edu);
        dest.writeInt(sex);
        dest.writeString(personalResume);
        dest.writeInt(teachingAge);
        dest.writeString(subjectName);
        dest.writeInt(subjectId);
        dest.writeString(cityName);
        dest.writeString(city);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonalDataBean> CREATOR = new Creator<PersonalDataBean>() {
        @Override
        public PersonalDataBean createFromParcel(Parcel in) {
            return new PersonalDataBean(in);
        }

        @Override
        public PersonalDataBean[] newArray(int size) {
            return new PersonalDataBean[size];
        }
    };

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getTeachingExperienceNum() {
        return teachingExperienceNum;
    }

    public void setTeachingExperienceNum(int teachingExperienceNum) {
        this.teachingExperienceNum = teachingExperienceNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGeaduateSchool() {
        return geaduateSchool;
    }

    public void setGeaduateSchool(String geaduateSchool) {
        this.geaduateSchool = geaduateSchool;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getEdu() {
        return edu;
    }

    public void setEdu(int edu) {
        this.edu = edu;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPersonalResume() {
        return personalResume;
    }

    public void setPersonalResume(String personalResume) {
        this.personalResume = personalResume;
    }

    public int getTeachingAge() {
        return teachingAge;
    }

    public void setTeachingAge(int teachingAge) {
        this.teachingAge = teachingAge;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


    public MultipartBody.Builder getSaveDate(String oldCity) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        Map<String, Object> map = new HashMap<>();
        map.put("nickName", getNickName());//用户昵称
        map.put("sex", String.valueOf(getSex()));//老师性别

        if (getSubjectId() != -1)
            map.put("subjectId", String.valueOf(getSubjectId()));//	科目id
        if (getTeachingAge() != -1)
            map.put("teachingAge", String.valueOf(getTeachingAge()));//	教龄
        if (!StringUtils.textIsEmpty(getGeaduateSchool()))
            map.put("geaduateSchool", getGeaduateSchool());//毕业学校
        if (!StringUtils.textIsEmpty(getPersonalResume()))
            map.put("personalResume", getPersonalResume());//个人简历
        if (!StringUtils.textIsEmpty(getProfession()))
            map.put("profession", getProfession());//专业

        if (!StringUtils.textIsEmpty(getCity()))
            map.put("city", getCity());//城市code
        if (getEdu() != -1)
            map.put("edu", String.valueOf(getEdu()));//最高学历
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        return builder;
    }
    public Map<String, Object> getSaveDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("nickName", getNickName());//用户昵称
        map.put("sex", String.valueOf(getSex()));//老师性别
        if (getSubjectId() != -1)
            map.put("subjectId", String.valueOf(getSubjectId()));//	科目id
        if (getTeachingAge() != -1)
            map.put("teachingAge", String.valueOf(getTeachingAge()));//	教龄
        if (!StringUtils.textIsEmpty(getGeaduateSchool()))
            map.put("geaduateSchool", getGeaduateSchool());//毕业学校
        if (!StringUtils.textIsEmpty(getPersonalResume()))
            map.put("personalResume", getPersonalResume());//个人简历
        if (!StringUtils.textIsEmpty(getProfession()))
            map.put("profession", getProfession());//专业

        if (!StringUtils.textIsEmpty(getCity()))
            map.put("city", getCity());//城市code
        if (!StringUtils.textIsEmpty(getCityName()))
            map.put("cityName",getCityName());
        if (getEdu() != -1)
            map.put("edu", String.valueOf(getEdu()));//最高学历
       return map;

    }

}
