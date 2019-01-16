package com.shengzhe.disan.xuetangparent.bean;

import com.main.disanxuelib.bean.TeachingExperienceBean;

import java.util.List;

/**
 * Created by acer on 2018/3/16.
 */

public class TeaHomePageBean {

    /**
     * teacherId : 1
     * photoUrl :
     * teacherName : 111111111111
     * teachingAge : 20
     * sex : 0
     * cardApprStatus : 2
     * profession : 啊啊啊
     * edu : 高中
     * qtsStatus : 1
     * quaStatus : 2
     * ipmpStatus : 1
     * subjectName : 数学
     * personalResume : 阿斯顿发斯蒂芬阿斯蒂芬付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付张三
     * geaduateSchool : 家里蹲屎壳郎大学
     * gradeName : 小学/高中
     * orderNum : 200
     * studentNum : 111
     * timeLong : 222
     */

    private long teacherId;
    private String photoUrl;
    private String teacherName;
    private int teachingAge;
    private int sex;
    private int cardApprStatus;
    private String profession;
    private String edu;
    private int qtsStatus;
    private int quaStatus;
    private int ipmpStatus;
    private String subjectName;
    private String personalResume;
    private String geaduateSchool;
    private String gradeName;
    private int orderNum;
    private int studentNum;
    private int timeLong;

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    //教师身份0自由老师，1平台教师
    private int identity;

    public List<TeachingExperienceBean> getTeachingExperience() {
        return teachingExperience;
    }

    public void setTeachingExperience(List<TeachingExperienceBean> teachingExperience) {
        this.teachingExperience = teachingExperience;
    }

    private List<TeachingExperienceBean> teachingExperience;

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCardApprStatus() {
        return cardApprStatus;
    }

    public void setCardApprStatus(int cardApprStatus) {
        this.cardApprStatus = cardApprStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPersonalResume() {
        return personalResume;
    }

    public void setPersonalResume(String personalResume) {
        this.personalResume = personalResume;
    }

    public String getGeaduateSchool() {
        return geaduateSchool;
    }

    public void setGeaduateSchool(String geaduateSchool) {
        this.geaduateSchool = geaduateSchool;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }
}
