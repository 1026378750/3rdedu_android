package com.shengzhe.disan.xuetangteacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/4/4.
 */

public class CourseSquadBean implements Parcelable {
    public CourseSquadBean() {
    }

    /**
     * gradeName : 小学
     * areadyCourseClass : 0
     * address : 阿道夫
     * salesVolume : 0
     * crowd : 再次测试物理-3再次测试物理-3再次测试物理-3再次测试物理-3再次测试物理-3再次测
     * totalPrice : 990000
     * surplusCoursePrice : 990000
     * discountTotalPrice : 495000
     * remark : 再次测试物理-3再次测试物理-3再次测试物理-3再次测试物理
     * classTime : 3
     * target : 再次测试物理-3再次测试物理-3再次测试物理-3再次测试物理-
     * duration : 2
     * photoUrl : http://10.8.1.133/static/images/6ab355bf6a834d9b81c8f429416729e31522479249072.jpg
     * courseName : 再次测试物理
     * cityName : 上海市
     * trialNum : null
     * surplusCourseClass : 3
     * price : 330000
     * coursePrice : 495000
     * discountSurplusCoursePrice : 495000
     * canJoin : 1
     * subjectName : 物理
     * maxUser : 3
     * isAreadyBuy : 1
     * campusDiscountId : 1
     * campusDiscountVersion : 1
     */

    public String gradeName;
    public int areadyCourseClass;
    public String address;
    public int salesVolume;
    public String crowd;
    public long totalPrice;
    public long surplusCoursePrice;
    public long discountTotalPrice;
    public String remark;
    public int classTime;
    public String target;
    public int duration;
    public String pictureUrl;
    public String courseName;
    public String cityName;
    public int trialNum;
    public int surplusCourseClass;
    public long price;
    public long coursePrice;
    public long discountSurplusCoursePrice;
    public int canJoin;
    public String subjectName;
    public int maxUser;
    public int isAreadyBuy;
    public long campusDiscountId;
    public int campusDiscountVersion;
    public int teacherId;
    public String teacherPhotoUrl;
    public String teacherName;
    public String teacherSubjectName;
    public int ipmpStatus;//	专业资质状态（0：未认证 1：审核中 2：已认证 3：已驳回）
    public int quaStatus;//学历认证状态（0：未认证 1：审核中 2：已认证 3：已驳回）
    public int qtsStatus;//教师资格认证状态（0：未认证 1：审核中 2：已认证 3：已驳回）
    public int sex;
    public int teachingAge;
    public int courseId;


    protected CourseSquadBean(Parcel in) {
        gradeName = in.readString();
        areadyCourseClass = in.readInt();
        address = in.readString();
        salesVolume = in.readInt();
        crowd = in.readString();
        totalPrice = in.readLong();
        surplusCoursePrice = in.readLong();
        discountTotalPrice = in.readLong();
        remark = in.readString();
        classTime = in.readInt();
        target = in.readString();
        duration = in.readInt();
        pictureUrl = in.readString();
        courseName = in.readString();
        cityName = in.readString();
        trialNum = in.readInt();
        surplusCourseClass = in.readInt();
        price = in.readLong();
        coursePrice = in.readLong();
        discountSurplusCoursePrice = in.readLong();
        canJoin = in.readInt();
        subjectName = in.readString();
        maxUser = in.readInt();
        isAreadyBuy = in.readInt();
        campusDiscountId = in.readLong();
        campusDiscountVersion = in.readInt();
        teacherId = in.readInt();
        teacherPhotoUrl = in.readString();
        teacherName = in.readString();
        teacherSubjectName = in.readString();
        ipmpStatus = in.readInt();
        quaStatus = in.readInt();
        qtsStatus = in.readInt();
        sex = in.readInt();
        teachingAge = in.readInt();
        courseId=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gradeName);
        dest.writeInt(areadyCourseClass);
        dest.writeString(address);
        dest.writeInt(salesVolume);
        dest.writeString(crowd);
        dest.writeLong(totalPrice);
        dest.writeLong(surplusCoursePrice);
        dest.writeLong(discountTotalPrice);
        dest.writeString(remark);
        dest.writeInt(classTime);
        dest.writeString(target);
        dest.writeInt(duration);
        dest.writeString(pictureUrl);
        dest.writeString(courseName);
        dest.writeString(cityName);
        dest.writeInt(trialNum);
        dest.writeInt(surplusCourseClass);
        dest.writeLong(price);
        dest.writeLong(coursePrice);
        dest.writeLong(discountSurplusCoursePrice);
        dest.writeInt(canJoin);
        dest.writeString(subjectName);
        dest.writeInt(maxUser);
        dest.writeInt(isAreadyBuy);
        dest.writeLong(campusDiscountId);
        dest.writeInt(campusDiscountVersion);
        dest.writeInt(teacherId);
        dest.writeString(teacherPhotoUrl);
        dest.writeString(teacherName);
        dest.writeString(teacherSubjectName);
        dest.writeInt(ipmpStatus);
        dest.writeInt(quaStatus);
        dest.writeInt(qtsStatus);
        dest.writeInt(sex);
        dest.writeInt(teachingAge);
        dest.writeInt(courseId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseSquadBean> CREATOR = new Creator<CourseSquadBean>() {
        @Override
        public CourseSquadBean createFromParcel(Parcel in) {
            return new CourseSquadBean(in);
        }

        @Override
        public CourseSquadBean[] newArray(int size) {
            return new CourseSquadBean[size];
        }
    };
}
