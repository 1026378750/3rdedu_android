package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 2018/4/9.
 */

public class SquadBean implements Parcelable{

    /**
     * classTime : 25
     * courseId : 262
     * courseName : 078269
     * courseTotalPrice : 250000
     * discountPrice : 250000
     * gradeName : 高中
     * identity : 1
     * maxUser : 25
     * nickName : 高中数学张老师
     * photoUrl : http://10.8.1.133/static/images/59ea0c8d9e074a96910256a5134fa6fe1520318963940.jpg
     * pictureUrl : http://10.8.1.133/static/images/d38061834c5d457e90d27068e258a7211523165667504.jpg
     * salesVolume : 5
     * singleClassTime : 1
     * subjectName : 数学
     * teacherId : 25
     */

    public int classTime;
    public int courseId;
    public String courseName;
    public long courseTotalPrice;
    public long discountPrice;
    public String gradeName;
    public int identity;
    public int maxUser;
    public String nickName;
    public String photoUrl;
    public String pictureUrl;
    public int salesVolume;
    public int singleClassTime;
    public String subjectName;
    public int teacherId;

    protected SquadBean(Parcel in) {
        classTime = in.readInt();
        courseId = in.readInt();
        courseName = in.readString();
        courseTotalPrice = in.readLong();
        discountPrice = in.readLong();
        gradeName = in.readString();
        identity = in.readInt();
        maxUser = in.readInt();
        nickName = in.readString();
        photoUrl = in.readString();
        pictureUrl = in.readString();
        salesVolume = in.readInt();
        singleClassTime = in.readInt();
        subjectName = in.readString();
        teacherId = in.readInt();
    }

    public static final Creator<SquadBean> CREATOR = new Creator<SquadBean>() {
        @Override
        public SquadBean createFromParcel(Parcel in) {
            return new SquadBean(in);
        }

        @Override
        public SquadBean[] newArray(int size) {
            return new SquadBean[size];
        }
    };

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
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

    public long getCourseTotalPrice() {
        return courseTotalPrice;
    }

    public void setCourseTotalPrice(long courseTotalPrice) {
        this.courseTotalPrice = courseTotalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(int maxUser) {
        this.maxUser = maxUser;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public int getSingleClassTime() {
        return singleClassTime;
    }

    public void setSingleClassTime(int singleClassTime) {
        this.singleClassTime = singleClassTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(classTime);
        dest.writeInt(courseId);
        dest.writeString(courseName);
        dest.writeLong(courseTotalPrice);
        dest.writeLong(discountPrice);
        dest.writeString(gradeName);
        dest.writeInt(identity);
        dest.writeInt(maxUser);
        dest.writeString(nickName);
        dest.writeString(photoUrl);
        dest.writeString(pictureUrl);
        dest.writeInt(salesVolume);
        dest.writeInt(singleClassTime);
        dest.writeString(subjectName);
        dest.writeInt(teacherId);
    }
}
