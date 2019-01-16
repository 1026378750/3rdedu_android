package com.shengzhe.disan.xuetangparent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**视频课
 * Created by acer on 2017/12/5.
 */

public class VideoBean implements Parcelable{
    //课程名称
    private String courseName;
    //分类名称
    private String videoTypeName;
    //课程价格
    private int coursePrice;
    //图片地址
    private String pictureUrl;
    //折扣价格
    private int discountPrice;
    //讲师名称
    private String lecturer;
    //课程id
    private int courseId;

    protected VideoBean(Parcel in) {
        courseName = in.readString();
        videoTypeName = in.readString();
        coursePrice = in.readInt();
        pictureUrl = in.readString();
        discountPrice = in.readInt();
        lecturer = in.readString();
        courseId = in.readInt();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getVideoTypeName() {
        return videoTypeName;
    }

    public void setVideoTypeName(String videoTypeName) {
        this.videoTypeName = videoTypeName;
    }

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(videoTypeName);
        dest.writeInt(coursePrice);
        dest.writeString(pictureUrl);
        dest.writeInt(discountPrice);
        dest.writeString(lecturer);
        dest.writeInt(courseId);
    }
}
