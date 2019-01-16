package com.shengzhe.disan.xuetangparent.bean;

/**
 * Created by acer on 2017/12/8.
 */

public class OrderBean {
    //订单id
    private int id;
    //课程id
    private int courseId;
    //课程名称
    private String courseName;
    //老师昵称
    private String teacherNickName;
    //课程类型
    private int courseType;
    //类型名称
    private String courseTypeName;
    //订单状态 1待付款 2已付款 8 完成 9 关闭  //已下架 当做已关闭处理
    private int status;
    //子状态 10 待付 20待审核 21待执行 22 正在执行 80上课完成 81结算完成 90未付款
    private int subStatus;
    //应付金额
    private long receivablePrice;
    //抵扣金额
    private Long offsetAmount;
    //下单时间
    private long buyerTime;
    //赠送数量
    private int giveNum;
    //购买数量
    private int buyNum;
    //课次数量
    private int classSum;
    //首次上课时间
    private long startTime;
    //单次课时
    private int teachingPeriod;
    //老师id
    private int sellerId;

    /*********
     * 一对一-返回参数说明、在线直播-返回参数说明
     */
    //授课方式名称
    private String teachingMethodName;
    //老师头像
    private String photoUrl;
    //授课地址
    private String address;

    /*******
     * 线下班课-返回参数说明
     */
    //是否可以插班 0否1是
    private int isJoin;


    //视频课播放id
    private int videoBjyId;
    //视频课播放token
    private String videoBjyToken;

    public int getUpperFrame() {
        return upperFrame;
    }

    public void setUpperFrame(int upperFrame) {
        this.upperFrame = upperFrame;
    }

    private int upperFrame;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherNickName() {
        return teacherNickName;
    }

    public void setTeacherNickName(String teacherNickName) {
        this.teacherNickName = teacherNickName;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(int subStatus) {
        this.subStatus = subStatus;
    }

    public long getReceivablePrice() {
        return receivablePrice;
    }

    public void setReceivablePrice(long receivablePrice) {
        this.receivablePrice = receivablePrice;
    }

    public Long getOffsetAmount() {
        return offsetAmount;
    }

    public void setOffsetAmount(Long offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    public long getBuyerTime() {
        return buyerTime;
    }

    public void setBuyerTime(long buyerTime) {
        this.buyerTime = buyerTime;
    }

    public int getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(int giveNum) {
        this.giveNum = giveNum;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getClassSum() {
        return classSum;
    }

    public void setClassSum(int classNum) {
        this.classSum = classNum;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getTeachingPeriod() {
        return teachingPeriod;
    }

    public void setTeachingPeriod(int teachingPeriod) {
        this.teachingPeriod = teachingPeriod;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getTeachingMethodName() {
        return teachingMethodName;
    }

    public void setTeachingMethodName(String teachingMethodName) {
        this.teachingMethodName = teachingMethodName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public int getVideoBjyId() {
        return videoBjyId;
    }

    public void setVideoBjyId(int videoBjyId) {
        this.videoBjyId = videoBjyId;
    }

    public String getVideoBjyToken() {
        return videoBjyToken;
    }

    public void setVideoBjyToken(String videoBjyToken) {
        this.videoBjyToken = videoBjyToken;
    }

}
