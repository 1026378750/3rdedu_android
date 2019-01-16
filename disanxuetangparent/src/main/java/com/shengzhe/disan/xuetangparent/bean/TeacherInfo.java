package com.shengzhe.disan.xuetangparent.bean;

import com.main.disanxuelib.bean.TeachingExperienceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/12/7.
 */

public class TeacherInfo {

    private int ipmpStatus;
    private String profession;
    private String gradeName;
    private int quaStatus;
    private String teacherName;
    private int sex;
    private String geaduateSchool;
    private String teacherArea;
    //是否有直播课，返回的直播课数目，大于0即为有
    private int isHaveDirect;
    private String photoUrl;
    private int teacherId;
    private String cityName;
    private String areaName;
    //教师身份0自由老师，1平台教师
    private int identity;
    private String edu;
    private String personalResume;
    private int teachingAge;
    private int cardApprStatus;
    private int qtsStatus;
    private String subjectName;
    private List<OneCourseArrayBean> oneCourseArray;
    private List<TeacherArea> teacherAreaList;
    private List<TeachingExperienceBean> teachingExperience;

    public int getIpmpStatus() {
        return ipmpStatus;
    }

    public void setIpmpStatus(int ipmpStatus) {
        this.ipmpStatus = ipmpStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getQuaStatus() {
        return quaStatus;
    }

    public void setQuaStatus(int quaStatus) {
        this.quaStatus = quaStatus;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getSex() {
        return sex;
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

    public int getIsHaveDirect() {
        return isHaveDirect;
    }

    public void setIsHaveDirect(int isHaveDirect) {
        this.isHaveDirect = isHaveDirect;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
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

    public List<OneCourseArrayBean> getOneCourseArray() {
        return oneCourseArray==null?new ArrayList<OneCourseArrayBean>():oneCourseArray;
    }

    public void setOneCourseArray(List<OneCourseArrayBean> oneCourseArray) {
        this.oneCourseArray = oneCourseArray;
    }

    public List<TeacherArea> getTeacherAreaList() {
        return teacherAreaList==null?new ArrayList<TeacherArea>():teacherAreaList;
    }

    public void setTeacherAreaList(List<TeacherArea> teacherAreaList) {
        this.teacherAreaList = teacherAreaList;
    }

    public List<TeachingExperienceBean> getTeachingExperience() {
        return teachingExperience==null?new ArrayList<TeachingExperienceBean>():teachingExperience;
    }

    public void setTeachingExperience(List<TeachingExperienceBean> teachingExperience) {
        this.teachingExperience = teachingExperience;
    }
}