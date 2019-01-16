package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/7.
 */

public class CourseOneInfo implements Parcelable {
    //专业资质状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int ipmpStatus;
    private int campusId;
    private int courseVersion;
    //学历认证状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int quaStatus;
    //课程折扣价格
    private int discountPrice;
    //课程简介
    private String remark;
    //头像图片地址
    private String photoUrl;
    //城市名称
    private String cityName;
    //区县名称
    private String areaName;
    //教师身份 0自由老师，1平台教师
    private int identity;
    //课程价格
    private int coursePrice;
    //实名认证状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int cardApprStatus;
    //课程id
    private int courseId;
    //教师资格认证状态（0：未认证 1：审核中 2：已认证 3：已驳回，查看认证数据传”attes_status”）
    private int qtsStatus;
    //科目名称
    private String subjectName;
    //专业
    private String profession;
    //是否支持试听 1是，0否
    private int audition;
    //阶段名称
    private String gradeName;
    //老师名称
    private String teacherName;
    //老师性别 0男，1女
    private int sex;
    //毕业学校
    private String geaduateSchool;
    //学生上门地址
    private String teacherArea;
    //是否已经申请试听 1未试听 2已试听
    private int isAreadyListen;
    //课程名称
    private String courseName;
    //老师id
    private int teacherId;
    //学历
    private String edu;
    //老师简介
    private String personalResume;
    //老师教龄
    public int teachingAge;
    public List<TeacherArea> teacherAreaList;

    public CourseOneInfo() {

    }

    protected CourseOneInfo(Parcel in) {
        ipmpStatus = in.readInt();
        campusId = in.readInt();
        courseVersion = in.readInt();
        quaStatus = in.readInt();
        discountPrice = in.readInt();
        remark = in.readString();
        photoUrl = in.readString();
        cityName = in.readString();
        areaName = in.readString();
        identity = in.readInt();
        coursePrice = in.readInt();
        cardApprStatus = in.readInt();
        courseId = in.readInt();
        qtsStatus = in.readInt();
        subjectName = in.readString();
        profession = in.readString();
        audition = in.readInt();
        gradeName = in.readString();
        teacherName = in.readString();
        sex = in.readInt();
        geaduateSchool = in.readString();
        teacherArea = in.readString();
        isAreadyListen = in.readInt();
        courseName = in.readString();
        teacherId = in.readInt();
        edu = in.readString();
        personalResume = in.readString();
        teachingAge = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ipmpStatus);
        dest.writeInt(campusId);
        dest.writeInt(courseVersion);
        dest.writeInt(quaStatus);
        dest.writeInt(discountPrice);
        dest.writeString(remark);
        dest.writeString(photoUrl);
        dest.writeString(cityName);
        dest.writeString(areaName);
        dest.writeInt(identity);
        dest.writeInt(coursePrice);
        dest.writeInt(cardApprStatus);
        dest.writeInt(courseId);
        dest.writeInt(qtsStatus);
        dest.writeString(subjectName);
        dest.writeString(profession);
        dest.writeInt(audition);
        dest.writeString(gradeName);
        dest.writeString(teacherName);
        dest.writeInt(sex);
        dest.writeString(geaduateSchool);
        dest.writeString(teacherArea);
        dest.writeInt(isAreadyListen);
        dest.writeString(courseName);
        dest.writeInt(teacherId);
        dest.writeString(edu);
        dest.writeString(personalResume);
        dest.writeInt(teachingAge);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseOneInfo> CREATOR = new Creator<CourseOneInfo>() {
        @Override
        public CourseOneInfo createFromParcel(Parcel in) {
            return new CourseOneInfo(in);
        }

        @Override
        public CourseOneInfo[] newArray(int size) {
            return new CourseOneInfo[size];
        }
    };

    public int getIpmpStatus() {
        return ipmpStatus;
    }

    public void setIpmpStatus(int ipmpStatus) {
        this.ipmpStatus = ipmpStatus;
    }

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }

    public int getCourseVersion() {
        return courseVersion;
    }

    public void setCourseVersion(int courseVersion) {
        this.courseVersion = courseVersion;
    }

    public int getQuaStatus() {
        return quaStatus;
    }

    public void setQuaStatus(int quaStatus) {
        this.quaStatus = quaStatus;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
    }

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getQtsStatus() {
        return qtsStatus;
    }

    public void setQtsStatus(int qtsStatus) {
        this.qtsStatus = qtsStatus;
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

    public int getAudition() {
        return audition;
    }

    public void setAudition(int audition) {
        this.audition = audition;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getGeaduateSchool() {
        return geaduateSchool;
    }

    public void setGeaduateSchool(String geaduateSchool) {
        this.geaduateSchool = geaduateSchool;
    }

    public String getTeacherArea() {
        return teacherArea;
    }

    public void setTeacherArea(String teacherArea) {
        this.teacherArea = teacherArea;
    }

    public int getIsAreadyListen() {
        return isAreadyListen;
    }

    public void setIsAreadyListen(int isAreadyListen) {
        this.isAreadyListen = isAreadyListen;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
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

    public List<TeacherArea> getTeacherAreaList() {
        return teacherAreaList==null?new ArrayList<TeacherArea>():teacherAreaList;
    }

    public void setTeacherAreaList(List<TeacherArea> teacherAreaList) {
        this.teacherAreaList = teacherAreaList;
    }
    public int getSex() {
        return sex;
    }

}
