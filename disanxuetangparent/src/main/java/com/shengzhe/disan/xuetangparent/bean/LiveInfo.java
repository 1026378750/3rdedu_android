package com.shengzhe.disan.xuetangparent.bean;

import java.util.List;

/**
 * Created by acer on 2017/12/7.
 */

public class LiveInfo {
    private int courseId;
    private int ipmpStatus;
    private int quaStatus;
    private String crowd;
    private int discountPrice;
    private String directTypeName;
    private String remark;
    private Object signUp;
    private int classTime;
    private String photoUrl;
    private String cityName;
    private String areaName;
    private int identity;
    private long startTime;
    private int courseTotalPrice;
    private int cardApprStatus;
    private int qtsStatus;
    private int isFullQuota;
    private int courseDiscount;
    private int isAreadyBuy;

    public List<CourseArrangementBean> getCourseArrangement() {
        return courseArrangement;
    }

    public void setCourseArrangement(List<CourseArrangementBean> courseArrangement) {
        this.courseArrangement = courseArrangement;
    }

    private List<CourseArrangementBean> courseArrangement;
    private String subjectName;
    private String profession;
    private String gradeName;
    private String teacherName;
    private int sex;
    private String geaduateSchool;
    private String pictureUrl;
    private Object teacherArea;
    private String target;
    private String courseName;
    private int teacherId;
    private int soldNum;//报名人数
    private String edu;
    private String personalResume;
    private int teachingAge;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getIpmpStatus() {
        return ipmpStatus;
    }

    public void setIpmpStatus(int ipmpStatus) {
        this.ipmpStatus = ipmpStatus;
    }

    public int getQuaStatus() {
        return quaStatus;
    }

    public void setQuaStatus(int quaStatus) {
        this.quaStatus = quaStatus;
    }

    public String getCrowd() {
        return crowd;
    }

    public void setCrowd(String crowd) {
        this.crowd = crowd;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDirectTypeName() {
        return directTypeName;
    }

    public void setDirectTypeName(String directTypeName) {
        this.directTypeName = directTypeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getSignUp() {
        return signUp;
    }

    public void setSignUp(Object signUp) {
        this.signUp = signUp;
    }

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public int getCourseTotalPrice() {
        return courseTotalPrice;
    }

    public void setCourseTotalPrice(int courseTotalPrice) {
        this.courseTotalPrice = courseTotalPrice;
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

    public int getIsFullQuota() {
        return isFullQuota;
    }

    public void setIsFullQuota(int isFullQuota) {
        this.isFullQuota = isFullQuota;
    }

    public int getCourseDiscount() {
        return courseDiscount;
    }

    public void setCourseDiscount(int courseDiscount) {
        this.courseDiscount = courseDiscount;
    }

    public int getIsAreadyBuy() {
        return isAreadyBuy;
    }

    public void setIsAreadyBuy(int isAreadyBuy) {
        this.isAreadyBuy = isAreadyBuy;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Object getTeacherArea() {
        return teacherArea;
    }

    public void setTeacherArea(Object teacherArea) {
        this.teacherArea = teacherArea;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }


    public static class CourseArrangementBean {
        private Long beginTime;

        public Long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Long beginTime) {
            this.beginTime = beginTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public int getClassTime() {
            return classTime;
        }

        public void setClassTime(int classTime) {
            this.classTime = classTime;
        }

        private Long endTime;
        private int classTime;

    }
}
