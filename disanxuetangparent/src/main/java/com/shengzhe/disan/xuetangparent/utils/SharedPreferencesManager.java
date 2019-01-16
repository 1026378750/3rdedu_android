package com.shengzhe.disan.xuetangparent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.main.disanxuelib.bean.CourseSubject;
import com.main.disanxuelib.bean.CourseType;
import com.shengzhe.disan.xuetangparent.bean.City;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.main.disanxuelib.bean.Subject;
import com.shengzhe.disan.xuetangparent.bean.User;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/****
 * 共享文件工具
 */
public class SharedPreferencesManager extends SharePrefUtils{

    /****
     * 保存用户信息
     * @param user
     */
    public static void setUserInfo(User user) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString("user", gson.toJson(user));
        editor.commit();
    }

    /****
     * 获取用户信息
     * @return
     */
    public static User getUserInfo() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            User user = gson.fromJson(sp.getString("user", ""), User.class);
            return user;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /****
     * 保存用户信息
     * @param data
     */
    public static void setPersonaInfo(String data) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Persona", data);
        editor.commit();
    }

    /****
     * 获取用户信息
     * @return
     */
    public static Personal getPersonaInfo() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        try {
            Personal personal = new Gson().fromJson(sp.getString("Persona", ""), Personal.class);
            return personal==null?new Personal(): personal;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*****
     * 保存是否第一次使用
     * @param isEnter
     */
    public static void setIsEnter(boolean isEnter) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.System_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("isEnter", isEnter);
        editor.commit();
    }

    /****
     * 获取是否第一次装APP
     * @return
     */
    public static boolean getIsFirstEnter() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.System_Datas, Context.MODE_PRIVATE);
        return sp.getBoolean("isEnter", true);
    }

    /*****
     * 获取地址信息
     * @return
     */
    public static String getCityId() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        return sp.getString("cityCode", "3101");
    }

    /****
     * 保存地址信息
     * @param cityCode
     */
    public static void saveCityId(String cityCode) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("cityCode", cityCode.substring(0, 4));
        editor.commit();
    }

   /****
     * 消息通知角标数
     * @return
     */
    public static int getBadgeCount() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.BadgeCount, Context.MODE_PRIVATE);
        return sp.getInt("BadgeCount_parent", 0);
    }

    /****
     * 消息通知角标数
     */
    public static void setBadgeCount(int BadgeCount) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.BadgeCount, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("BadgeCount_parent", BadgeCount);
        editor.commit();
    }

    /*****
     * 获取手机号码
     */
    public static String getPhoneNum() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.System_Datas, Context.MODE_PRIVATE);
        return  sp.getString(StringUtils.Share_Phone,"");
    }


    /*****
     * 保存手机号码
     * @param phoneNum
     */
    public static void savePhoneNum(String phoneNum) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.System_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(StringUtils.Share_Phone, phoneNum);
        editor.commit();
    }

    /*****
     * 保存科目
     */
    public static void saveSubject(String subject) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(StringUtils.Share_Subject, subject);
        editor.commit();
    }

    /*****
     * 清空科目
     */
    public static void clearSubject() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(StringUtils.Share_Subject, "");
        editor.commit();
    }

    /*****
     * 获取科目
     * @return
     */
    public static List<Subject> getSubject() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        String subject = sp.getString(StringUtils.Share_Subject, "");
        return TextUtils.isEmpty(subject)?null:(List<Subject>) new Gson().fromJson(subject, new TypeToken<List<Subject>>() {
        }.getType());
    }
    /****
     * 保存年级
     * @param subject
     */
    public static void saveGrade(String subject) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(StringUtils.Share_Grade, subject);
        editor.commit();
    }
    /****
     * 保存区县
     * @param subject
     */
    public static void saveArea(String subject) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(StringUtils.Share_Area, subject);
        editor.commit();
    }

    /****
     * 获取区县
     */
    public static List<CourseSubject> getArea() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        String subject = sp.getString(StringUtils.Share_Area, "");
        List<CourseSubject> list = new ArrayList<>();
        try {
            list.clear();
            JSONArray array = new JSONArray(subject);
            for (int i=0;i<array.length();i++){
                CourseSubject type = new CourseSubject();
               org.json.JSONObject jsonObject=array.getJSONObject(i);
                type.id=jsonObject.optInt("code");
                type.name=jsonObject.optString("name");
                list.add(type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*****
     * 获取年级
     * @return
     */
    public static List<CourseType> getGrade() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        String subject = sp.getString(StringUtils.Share_Grade, "");
        List<CourseType> list = new ArrayList<>();
        try {
            list.clear();
            CourseType courseType = new CourseType();
            List<CourseSubject> childList = new ArrayList<>();
            CourseSubject subject1 = new CourseSubject();
            courseType.id = -1;
            courseType.name = "";

            subject1.id = -1;
            subject1.name ="不限年级";
            childList.add(subject1);
            courseType.childList.addAll(childList);
            list.add(courseType);

            JSONArray array = new JSONArray(subject);
            for (int i=0;i<array.length();i++){
                CourseType type = new CourseType();
                type.formatJson(array.optJSONObject(i));
                list.add(type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /****
     * 保存城市
     * @param subject
     */
    public static void saveCity(String subject) {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(StringUtils.Share_City, subject);
        editor.commit();
    }

    /*****
     * 获取城市
     * @return
     */
    public static List<City> getCity() {
        SharedPreferences sp = context.getSharedPreferences(StringUtils.Share_Datas, Context.MODE_PRIVATE);
        String subject = sp.getString(StringUtils.Share_City, "");
        return TextUtils.isEmpty(subject)?null:(List<City>) new Gson().fromJson(subject, new TypeToken<List<City>>() {
        }.getType());
    }

    /****
     * 清空用户本地缓存信息
     */
    public static void clearDatas(){
        Editor UserInfo_Datas = context.getSharedPreferences(StringUtils.UserInfo_Datas, Context.MODE_PRIVATE).edit();
        UserInfo_Datas.clear();
        UserInfo_Datas.commit();
    }

}
