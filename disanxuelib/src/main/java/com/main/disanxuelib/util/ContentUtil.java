package com.main.disanxuelib.util;

import com.main.disanxuelib.bean.CourseSubject;
import com.main.disanxuelib.bean.CourseType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liukui on 2015/12/4.
 */
public class ContentUtil {

    /*****
     * 授课方式
     * @return
     */
    public static ArrayList<CourseType> getTeacherMethod() {
        ArrayList<CourseType> courseList = new ArrayList<>();
        CourseType courseType = new CourseType();
        courseType.id = 0;
        courseType.name = "不限";
        courseList.add(0, courseType);

        courseType = new CourseType();
        courseType.id = 1;
        courseType.name = "老师上门";
        courseList.add(1, courseType);

        courseType = new CourseType();
        courseType.id = 2;
        courseType.name = "校区上课";
        courseList.add(2, courseType);

        courseType = new CourseType();
        courseType.id = 3;
        courseType.name = "学生上门";
        courseList.add(3, courseType);

        return courseList;
    }

    /****
     * 获取班课人数
     * @return
     */
    public static ArrayList<CourseSubject> getCourseNumber() {
        ArrayList<CourseSubject> courseList = new ArrayList<>();
        CourseSubject courseType = new CourseSubject();
        courseType.id = 0;
        courseType.name = "不限";
        courseList.add(0, courseType);

        courseType = new CourseSubject();
        courseType.id = 1;
        courseType.name = "2-3人";
        courseList.add(1, courseType);

        courseType = new CourseSubject();
        courseType.id = 2;
        courseType.name = "4-10人";
        courseList.add(2, courseType);

        courseType = new CourseSubject();
        courseType.id = 3;
        courseType.name = "11-50人";
        courseList.add(3, courseType);

        courseType = new CourseSubject();
        courseType.id = 4;
        courseType.name = "50人以上";
        courseList.add(4, courseType);

        return courseList;
    }

    /*****
     * 排序
     * @return
     */
    public static ArrayList<CourseType> getOrder() {
        ArrayList<CourseType> courseList = new ArrayList<>();
        List<CourseSubject> childList = new ArrayList<>();
        CourseType courseType = new CourseType();
        CourseSubject courseSubject = new CourseSubject();

        courseType.id = 10;
        courseType.name = "性别：";
        courseSubject.id = -1;
        courseSubject.name = "不限";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 1;
        courseSubject.name = "男";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 0;
        courseSubject.name = "女";
        childList.add(courseSubject);

        courseType.childList.addAll(childList);
        courseList.add(0, courseType);

        childList.clear();
        courseType = new CourseType();
        courseType.id = 11;
        courseType.name = "教龄：";
        courseSubject = new CourseSubject();
        courseSubject.id = -1;
        courseSubject.name = "不限";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 1;
        courseSubject.name = "5年以下";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 2;
        courseSubject.name = "5-10年";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 3;
        courseSubject.name = "11-15年";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 4;
        courseSubject.name = "16-20年";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 5;
        courseSubject.name = "20年以上";
        childList.add(courseSubject);

        courseType.childList.addAll(childList);
        courseList.add(1, courseType);

        childList.clear();
        courseType = new CourseType();
        courseType.id = 12;
        courseType.name = "老师推荐：";
        courseSubject = new CourseSubject();
        courseSubject.id = -1;
        courseSubject.name = "不限";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 1;
        courseSubject.name = "第三学堂";
        childList.add(courseSubject);

        courseSubject = new CourseSubject();
        courseSubject.id = 0;
        courseSubject.name = "自由老师";
        childList.add(courseSubject);

        courseType.childList.addAll(childList);
        courseList.add(2, courseType);

        childList.clear();
        courseType = new CourseType();
        courseType.id = 13;
        courseType.name = "授课区域：";
        courseSubject = new CourseSubject();
        courseSubject.id = -1;
        courseSubject.name = "不限";
        childList.add(courseSubject);
        courseType.childList.addAll(childList);
        courseList.add(3, courseType);

        return courseList;
    }

    public static ArrayList<CourseType> selectClazz() {
        ArrayList<CourseType> courseList = new ArrayList<>();
        CourseType courseType = new CourseType();

        courseType.id = 1;
        courseType.name = "线下1对1";
        courseList.add(0, courseType);

        courseType = new CourseType();
        courseType.id = 2;
        courseType.name = "在线直播课";
        courseList.add(1, courseType);

        courseType = new CourseType();
        courseType.id = 3;
        courseType.name = "品牌课";
        courseList.add(2, courseType);

        courseType = new CourseType();
        courseType.id = 4;
        courseType.name = "线下班课";
        courseList.add(3, courseType);
        return courseList;
    }

    public static ArrayList<CourseType> selectLive() {
        ArrayList<CourseType> courseList = new ArrayList<>();
        CourseType courseType = new CourseType();


        courseType.id = -1;
        courseType.name = "不限";
        courseList.add(0, courseType);

        courseType = new CourseType();

        courseType.id = 1;
        courseType.name = "一对一授课";
        courseList.add(1, courseType);

        courseType = new CourseType();
        courseType.id = 2;
        courseType.name = "互动小班课";
        courseList.add(2, courseType);

        courseType = new CourseType();
        courseType.id = 3;
        courseType.name = "普通大班课";
        courseList.add(3, courseType);


        return courseList;
    }

    public static ArrayList<CourseType> selectStatus() {
        ArrayList<CourseType> courseList = new ArrayList<>();
        CourseType courseType = new CourseType();
        courseType.id = 1;
        courseType.name = "全部";
        courseList.add(0, courseType);

        courseType = new CourseType();
        courseType.id = 21;
        courseType.name = "未开课";
        courseList.add(1, courseType);

        courseType = new CourseType();
        courseType.id = 22;
        courseType.name = "开课中";
        courseList.add(2, courseType);

        courseType = new CourseType();
        courseType.id = 80;
        courseType.name = "已完成";
        courseList.add(3, courseType);
        return courseList;
    }

    public static ArrayList<CourseType> selectPAyStatus() {
        ArrayList<CourseType> courseList = new ArrayList<>();
        CourseType courseType = new CourseType();
        courseType.id = 1;
        courseType.name = "全部";
        courseList.add(0, courseType);

        courseType = new CourseType();
        courseType.id = 2;
        courseType.name = "待支付";
        courseList.add(1, courseType);

        courseType = new CourseType();
        courseType.id = 3;
        courseType.name = "已支付";
        courseList.add(2, courseType);
        return courseList;
    }

    public static String selectZXKCKM[] = {"不限年级", "授课方式", "筛选"};
    public static String selectZXKCXXYDY[] = {"不限年级", "授课科目","直播类型", "筛选"};
    public static String selectXXYDY[] = {"不限年级", "授课科目", "授课方式", "筛选"};
    public static String selectXXYDY2[] = {"不限年级", "授课科目", "筛选"};
    public static String selectWDDD[] = {"线下1对1", "支付状态"};
    public static String selectWDKC[] = {"线下1对1", "开课状态"};
    public static String selectCANJOIN[] = {BaseStringUtils.CAN_JOIN, BaseStringUtils.CAN_NOT_JOIN};

    public static List<String> selectDate () {
        List<String> strList = new ArrayList<>();
        strList.add("06:00");
        strList.add("07:00");
        strList.add("08:00");
        strList.add("09:00");
        strList.add("10:00");
        strList.add("11:00");
        strList.add("12:00");
        strList.add("13:00");
        strList.add("14:00");
        strList.add("15:00");
        strList.add("16:00");
        strList.add("17:00");
        strList.add("18:00");
        strList.add("19:00");
        strList.add("20:00");
        strList.add("21:00");
        strList.add("22:00");
        strList.add("23:00");
        return strList;
    };

    public static List<String> selectBank() {
         List<String> dataList = new ArrayList<>();
        dataList.add("中国工商银行");
        dataList.add("中国建设银行");
        dataList.add("中国农业银行");
        dataList.add("中国银行");
        dataList.add("招商银行");
        dataList.add("交通银行");
       /* dataList.add("广发银行");
        dataList.add("中信银行");
        dataList.add("中国光大银行");
        dataList.add("中国民生银行");
        dataList.add("平安银行");
        dataList.add("华夏银行");
        dataList.add("兴业银行");
        dataList.add("上海浦东发展银行");
        dataList.add("中国邮政储蓄银行");
        dataList.add("东亚银行");*/
        return dataList;
    }

    /****
     * 课程类型
     * @return
     */
    public static String[] selectSelectClazz() {
        return new String[]{"线下1对1","线下班课", "在线直播课"};
    }

    public static List<String> sharePageList(){
        List<String> list = new ArrayList<>();
        //钉钉
        list.add("com.alibaba.android.rimet");
        //MOMO陌陌
        list.add("com.immomo.momo");
        //微博
        list.add("com.sina.weibo");
        //微信
        list.add("com.tencent.mm");
        //QQ
        list.add("com.tencent.mobileqq");
        return list;
    }

    /*****
     * 获取手机地图应用
     * @return
     */
    public static List<String> mapList(){
        List<String> list = new ArrayList<>();
        if (SystemInfoUtil.isPackageInstalled("com.baidu.BaiduMap")){
            //百度地图
            list.add(BaseStringUtils.MapBaiDu);
        }
        if (SystemInfoUtil.isPackageInstalled("com.autonavi.minimap")){
            //高德地图
            list.add(BaseStringUtils.MapGaoDe);
        }
        if (SystemInfoUtil.isPackageInstalled("com.tencent.map")){
            //腾讯地图
            list.add(BaseStringUtils.MapTenXun);
        }
        /*if (SystemInfoUtil.isPackageInstalled("com.google.android.apps.maps")){
            //谷歌地图
            list.add(BaseStringUtils.MapGuGe);
        }*/
        return list;
    }

}
