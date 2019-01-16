package com.shengzhe.disan.xuetangteacher.bean;

/**
 * Created by acer on 2018/1/22.
 */

public class HomeBean {

    /**
     * photoUrl : http://101.131.162.157/static/images/4af17388a7a44a269bb1f038c75c6a051511839404850.jpg
     * nickName : 熬夜的爬虫
     * identity : 1
     * sex : 1
     * subjectName : 语文
     * teachingAge : 2
     * orderNum : 1
     * studentNum : 1
     * timeLong : 10
     * homeStatus : 0
     */

    private String photoUrl;
    private String nickName;
    private int identity;//教师身份 0 自由老师，1 平台教师
    private int sex=-1;
    private String subjectName;
    private int teachingAge;
    private int orderNum;
    private int studentNum;
    private int timeLong;//授课时长
    private int homeStatus;//	资料完善状态 0 未完善资料，1 已完善资料
    private int startCourseStatus;//开课数量 0代表未开课
    private int realNameAuthStatus;//实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回

    public int getIsOpenCity() {
        return isOpenCity;
    }

    public void setIsOpenCity(int isOpenCity) {
        this.isOpenCity = isOpenCity;
    }

    private int isOpenCity;//	是否是开通线下教育的城市 1、不是 2、是

    public long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(long assistantId) {
        this.assistantId = assistantId;
    }

    private long assistantId;//助教id，为0或null时没有绑定助教

    public int getRealNameAuthStatus() {
        return realNameAuthStatus;
    }

    public void setRealNameAuthStatus(int realNameAuthStatus) {
        this.realNameAuthStatus = realNameAuthStatus;
    }


    public int getStartCourseStatus() {
        return startCourseStatus;
    }

    public void setStartCourseStatus(int startCourseStatus) {
        this.startCourseStatus = startCourseStatus;
    }



    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTeachingAge() {
        return teachingAge;
    }

    public void setTeachingAge(int teachingAge) {
        this.teachingAge = teachingAge;
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

    public int getHomeStatus() {
        return homeStatus;
    }

    public void setHomeStatus(int homeStatus) {
        this.homeStatus = homeStatus;
    }
}
