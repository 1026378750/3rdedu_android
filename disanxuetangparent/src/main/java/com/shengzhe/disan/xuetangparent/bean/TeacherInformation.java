package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2017/12/5.
 */

public class TeacherInformation implements Parcelable {
    //课程类型名称
    private String maxCourseTypeName;
    //最近开课时间
    private long maxCreateTime;
    //课程类型 1 线下一对一 2 在线直播 3视频课程
    private int courseType;
    //区县名称
    private String areaName;
    //省市名称
    private String cityName;
    //课程价格
    private int coursePrice;
    //折扣价格
    private int discountPrice;
    //阶段名称
    private String gradeName;
    //最近开课的课程id
    private int maxCreateTimeCourseId;
    //最低价格的课程id
    private int minPriceCourseId;
    //教师身份0自由老师，1平台教师
    private int identity;
    //课程名称
    private String maxCourseName;
    //老师昵称
    private String nickName;
    //
    private String personalResume;
    //头像url
    private String photoUrl;
    //科目名称
    private String subjectName;
    private String profession;
    //教师资格认证状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int qtsStatus;
    //学历认证状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int quaStatus;
    //专业资质状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int ipmpStatus;
    //实名认证状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int cardApprStatus;
    //性别
    private int sex;
    //教龄
    private String teacherArea;
    //老师id
    private int teacherId;
    //老师姓名
    private String teacherName;

    //教龄
    private int teachingAge;

    public String getMaxCourseTypeName() {
        return maxCourseTypeName;
    }

    public void setMaxCourseTypeName(String maxCourseTypeName) {
        this.maxCourseTypeName = maxCourseTypeName;
    }

    public long getMaxCreateTime() {
        return maxCreateTime;
    }

    public void setMaxCreateTime(long maxCreateTime) {
        this.maxCreateTime = maxCreateTime;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getMaxCreateTimeCourseId() {
        return maxCreateTimeCourseId;
    }

    public void setMaxCreateTimeCourseId(int maxCreateTimeCourseId) {
        this.maxCreateTimeCourseId = maxCreateTimeCourseId;
    }

    public int getMinPriceCourseId() {
        return minPriceCourseId;
    }

    public void setMinPriceCourseId(int minPriceCourseId) {
        this.minPriceCourseId = minPriceCourseId;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getMaxCourseName() {
        return maxCourseName;
    }

    public void setMaxCourseName(String maxCourseName) {
        this.maxCourseName = maxCourseName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonalResume() {
        return personalResume;
    }

    public void setPersonalResume(String personalResume) {
        this.personalResume = personalResume;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getQtsStatus() {
        return qtsStatus;
    }

    public void setQtsStatus(int qtsStatus) {
        this.qtsStatus = qtsStatus;
    }

    public int getQuaStatus() {
        return quaStatus;
    }

    public void setQuaStatus(int quaStatus) {
        this.quaStatus = quaStatus;
    }

    public int getIpmpStatus() {
        return ipmpStatus;
    }

    public void setIpmpStatus(int ipmpStatus) {
        this.ipmpStatus = ipmpStatus;
    }

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTeacherArea() {
        return teacherArea;
    }

    public void setTeacherArea(String teacherArea) {
        this.teacherArea = teacherArea;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTeachingAge() {
        return teachingAge;
    }

    public void setTeachingAge(int teachingAge) {
        this.teachingAge = teachingAge;
    }

    public TeacherInformation() {

    }


    protected TeacherInformation(Parcel in) {
        maxCourseTypeName = in.readString();
        maxCreateTime = in.readLong();
        courseType = in.readInt();
        areaName = in.readString();
        cityName = in.readString();
        coursePrice = in.readInt();
        discountPrice = in.readInt();
        gradeName = in.readString();
        maxCreateTimeCourseId = in.readInt();
        minPriceCourseId = in.readInt();
        identity = in.readInt();
        maxCourseName = in.readString();
        nickName = in.readString();
        personalResume = in.readString();
        photoUrl = in.readString();
        subjectName = in.readString();
        profession = in.readString();
        qtsStatus = in.readInt();
        quaStatus = in.readInt();
        ipmpStatus = in.readInt();
        cardApprStatus = in.readInt();
        sex = in.readInt();
        teacherArea = in.readString();
        teacherId = in.readInt();
        teacherName = in.readString();
        teachingAge = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maxCourseTypeName);
        dest.writeLong(maxCreateTime);
        dest.writeInt(courseType);
        dest.writeString(areaName);
        dest.writeString(cityName);
        dest.writeInt(coursePrice);
        dest.writeInt(discountPrice);
        dest.writeString(gradeName);
        dest.writeInt(maxCreateTimeCourseId);
        dest.writeInt(minPriceCourseId);
        dest.writeInt(identity);
        dest.writeString(maxCourseName);
        dest.writeString(nickName);
        dest.writeString(personalResume);
        dest.writeString(photoUrl);
        dest.writeString(subjectName);
        dest.writeString(profession);
        dest.writeInt(qtsStatus);
        dest.writeInt(quaStatus);
        dest.writeInt(ipmpStatus);
        dest.writeInt(cardApprStatus);
        dest.writeInt(sex);
        dest.writeString(teacherArea);
        dest.writeInt(teacherId);
        dest.writeString(teacherName);
        dest.writeInt(teachingAge);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherInformation> CREATOR = new Creator<TeacherInformation>() {
        @Override
        public TeacherInformation createFromParcel(Parcel in) {
            return new TeacherInformation(in);
        }

        @Override
        public TeacherInformation[] newArray(int size) {
            return new TeacherInformation[size];
        }
    };
}


