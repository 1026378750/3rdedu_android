package com.main.disanxuelib.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.StringUtils;
import com.ldf.calendar.utils.CalendarDate;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.CanTimer;
import com.main.disanxuelib.bean.Schedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/6.
 */

public class ClassScheduleUtil {

    /*****
     *冒泡排序，经过优化 选择上课时间
     * @param arr
     */
    public static Calendar[] bubbleSort(Calendar[] arr) {
        int low = 0, j;
        int high = arr.length - 1; //设置变量的初始值
        Calendar tmp;
        while (low < high) {
            for (j = low; j < high; ++j) //正向冒泡,找到最大者
                if (arr[j].id > arr[j + 1].id) {
                    tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            --high; //修改high值, 前移一位
            for (j = high; j > low; --j) //反向冒泡,找到最小者
                if (arr[j].id < arr[j - 1].id) {
                    tmp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = tmp;
                }
            ++low;//修改low值,后移一位
        }
        return arr;
    }

    /*****
     * 不包含生成标题的
     * @param times
     * @param calList
     * @param handler
     */
    public static void createSchedule(int times, ArrayList<Calendar> calList, Handler handler) {
        createSchedule(times,calList,"",handler);
    }

    /*****
     * 生成课程表
     *
     * @param times
     * @param calList
     * @return
     */
    public static void createSchedule(final int times, final ArrayList<Calendar> calList,final String courseName, final Handler handler) {
        if(calList==null||calList.isEmpty())
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //存储日期
                List<Schedule> dateList = new ArrayList<>();
                //保存上次日期
                Map<String, java.util.Calendar> lastMap = new HashMap<>();
                try {
                    ArrayList<String> weekList = new ArrayList<>();
                    int j = 0;
                    int num  = 0;
                    for (int i = 0; i < times; i++) {
                        //获取变量值
                        j = j >= calList.size() ? 0 : j;
                        //保存星期
                        String week = calList.get(j).week;
                        if (!weekList.contains(week)) {
                            weekList.add(week);
                        }
                        //获取上一次日期
                        java.util.Calendar lastDate = lastMap.get(week);
                        if (lastDate == null) {
                            //获取当前星期日期
                            lastDate = java.util.Calendar.getInstance();
                            lastDate.setTime(new SimpleDateFormat("yyyy年MM月dd日").parse(calList.get(j).data));
                        }
                        Schedule schedule = new Schedule();
                        int year = lastDate.get(java.util.Calendar.YEAR);
                        int month = lastDate.get(java.util.Calendar.MONTH) + 1;
                        int day = lastDate.get(java.util.Calendar.DAY_OF_MONTH);
                        schedule.week = week;
                        schedule.date = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
                        schedule.time = calList.get(j).time;
                        schedule.canTime = calList.get(j).canTime;
                        if (!BaseStringUtils.textIsEmpty(courseName)) {
                            schedule.title = courseName;
                        }
                        dateList.add(schedule);
                        lastDate.add(lastDate.DATE, 7);//把日期往后增加7天.正数往后推,负数往前移动
                        lastMap.put(week, lastDate);
                        j++;
                    }
                    sortSchedule(dateList.toArray(new Schedule[dateList.size()]),weekList, handler);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*****
     * 排序
     * @param arr
     * @param handler
     * @return
     */
    private static void sortSchedule(final Schedule[] arr,ArrayList<String> weekList, final Handler handler) {
        int low = 0, j;
        int high = arr.length - 1; //设置变量的初始值
        Schedule tmp;
        while (low < high) {
            for (j = low; j < high; ++j) //正向冒泡,找到最大者
                try {
                    if (new SimpleDateFormat("yyyy-MM-dd").parse(arr[j].date).getTime() > new SimpleDateFormat("yyyy-MM-dd").parse(arr[j + 1].date).getTime()) {
                        tmp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = tmp;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            --high; //修改high值, 前移一位
            for (j = high; j > low; --j) //反向冒泡,找到最小者
                try {
                    if (new SimpleDateFormat("yyyy-MM-dd").parse(arr[j].date).getTime() < new SimpleDateFormat("yyyy-MM-dd").parse(arr[j - 1].date).getTime()) {
                        tmp = arr[j];
                        arr[j] = arr[j - 1];
                        arr[j - 1] = tmp;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            ++low;//修改low值,后移一位
        }
        Message message = new Message();
        message.what = BaseIntegerUtil.MESSAGE_ID_60001;
        Bundle bundle = new Bundle();
        bundle.putParcelableArray(BaseStringUtils.HANDLER_DATA,arr);
        bundle.putStringArrayList(BaseStringUtils.HANDLER_DATA2,weekList);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /****
     * 获取时间间隔
     * @param data
     * @param hour
     * @return
     */
    public static String getTimeLag(String data, String hour) {

        switch (data) {

            case "06:00":
                return repairZero(6, hour);

            case "07:00":
                return repairZero(7, hour);

            case "08:00":
                return repairZero(8, hour);

            case "09:00":
                return repairZero(9, hour);

            case "10:00":
                return repairZero(10, hour);

            case "11:00":
                return repairZero(11, hour);

            case "12:00":
                return repairZero(12, hour);

            case "13:00":
                return repairZero(13, hour);

            case "14:00":
                return repairZero(14, hour);

            case "15:00":
                return repairZero(15, hour);

            case "16:00":
                return repairZero(16, hour);

            case "17:00":
                return repairZero(17, hour);

            case "18:00":
                return repairZero(18, hour);

            case "19:00":
                return repairZero(19, hour);

            case "20:00":
                return repairZero(20, hour);

            case "21:00":
                return repairZero(21, hour);

            case "22:00":
                return repairZero(22, hour);

            case "23:00":
                return repairZero(23, hour);
        }
        return "";
    }

    /*****
     * 老师端
     * @param data
     * @param hour
     * @return
     */
    public static String getTimeLagTeacher(String data, String hour) {

        switch (data) {

            case "06:00":
                return repairZeroTeacher(6, hour);

            case "07:00":
                return repairZeroTeacher(7, hour);

            case "08:00":
                return repairZeroTeacher(8, hour);

            case "09:00":
                return repairZeroTeacher(9, hour);

            case "10:00":
                return repairZeroTeacher(10, hour);

            case "11:00":
                return repairZeroTeacher(11, hour);

            case "12:00":
                return repairZeroTeacher(12, hour);

            case "13:00":
                return repairZeroTeacher(13, hour);

            case "14:00":
                return repairZeroTeacher(14, hour);

            case "15:00":
                return repairZeroTeacher(15, hour);

            case "16:00":
                return repairZeroTeacher(16, hour);

            case "17:00":
                return repairZeroTeacher(17, hour);

            case "18:00":
                return repairZeroTeacher(18, hour);

            case "19:00":
                return repairZeroTeacher(19, hour);

            case "20:00":
                return repairZeroTeacher(20, hour);

            case "21:00":
                return repairZeroTeacher(21, hour);

            case "22:00":
                return repairZeroTeacher(22, hour);

            case "23:00":
                return repairZeroTeacher(23, hour);
        }
        return "";
    }

    /*****
     * 以小时为单位
     * @param time
     * @return
     */
    public static String repairZero(int hour, String time) {
        if (time.contains(".")) {
            String str = String.valueOf(hour + Double.parseDouble(time));
            String[] arr = str.split("\\.");
            if (Integer.parseInt(arr[0]) > 23) {
                return "24:00";
            }
            double decimals = Double.parseDouble("0." + arr[1]);
            str = (Integer.parseInt(arr[0]) < 10 ? "0" + arr[0] : arr[0]) + ":" + (decimals * 60 < 10 ? "0" + decimals * 60 : decimals * 60);
            return str.substring(0, 5);
        } else {
            String str = String.valueOf(hour + Integer.parseInt(time));
            if (Integer.parseInt(str) > 23) {
                return "24:00";
            }
            return (Integer.parseInt(str) < 10 ? "0" + str : str) + ":00";
        }
    }

    public static String repairZeroTeacher(int hour, String time) {
        if (time.contains(".")) {
            double account = hour + Double.parseDouble(time);
            String subHour = "";
            if (Math.floor(account) > 23) {//返回小于等于a的最大整数
                subHour = "次日0"+(String.format("%.f",Math.floor(account) - 24))+":";
            }
            double decimals = account - Math.floor(account);
            subHour += String.format("%.f",(decimals * 60 < 10 ? "0" + decimals * 60 : decimals * 60));
            return subHour;
        } else {
            String str = String.valueOf(hour + Integer.parseInt(time));
            if (Integer.parseInt(str) > 23) {
                return "次日0"+(Integer.parseInt(str)-24)+":00";
            }
            return (Integer.parseInt(str) < 10 ? "0" + str : str) + ":00";
        }
    }

    /*****
     * 根据时间段获取时间数组
     * @param time HH:mm-HH:mm
     * @return
     */
    public static List<String> getTimeSet(String date,int time) {
        String[] arr = date.split(":");
        List<String> child = new ArrayList<>();
        for(int i= 0;i<= time;i++){
           long hour = Long.parseLong(arr[0])+i;
           child.add((hour < 10 ? "0" + hour : String.valueOf(hour))+ ":00");
        }
        return child;
    }

    /****
     * 获取时间阶段
     * @param date
     * @param time
     * @return
     */
    public static List<String> getTimeList(String date,int time) {
        String[] arr = date.split(":");
        List<String> child = new ArrayList<>();
        for(int i= 0;i<= time;i++){
           long hour = Long.parseLong(arr[0])+i;
           child.add((hour < 10 ? "0" + hour : String.valueOf(hour))+ ":00");
        }
        return child;
    }

    /*****
     *时间是否有效
     * @param date
     * @param time
     * @param statusMap
     * @return
     */
    public static boolean isValidTime(String date,int time,Map<String,Integer> statusMap){
        List<String> dataTime = getTimeSet(date,time);
        for (String data : dataTime){
            if(statusMap.containsKey(data)){
                return false;
            }
        }
        return true;
    }

    /****
     * * 判断是否包含无效时间段
     *
     * 1、是否包含其他时间段
     * 2、是否超出23点
     * @param date 当前时间段
     * @param time 时间
     * @param statusSet 时间数组
     * @return
     */
    public static boolean isValidTime(String date,int time,List<String> statusSet){
        List<String> dataTime = getTimeList(date,time);
        for (String data : dataTime){
            int hours = Integer.parseInt(data.split(":")[0]);
            if(hours<6 || hours>23){
                return true;
            }
            if (statusSet!=null&&!statusSet.isEmpty()&&statusSet.contains(data)){
                return true;
            }
        }
        return false;
    }

    /*****
     * 获取时间间隔
     * @param date
     * @param time
     * @return
     */
    public static List<String> getTimeQuantumTeacher(String date, String time) {
        time =  time.replace("次日","");
        String[] arr = time.split("-");
        List<String> child = new ArrayList<>();
        int hour = Integer.parseInt(arr[0].split(":")[0]);
        int maxHour = Integer.parseInt(arr[1].split(":")[0]);
        if (maxHour<6)
            maxHour +=24;
        int minHour =  hour - (maxHour-hour)+1;
        minHour = minHour<6?6:minHour;
        while (minHour<maxHour) {
            child.add(date + " " + (minHour < 10 ? "0" + minHour : String.valueOf(minHour)) + ":00");
            minHour++;
        }
        return child;
    }

    /*****
     * 获取时间间隔
     * @param date
     * @param time
     * @return
     */
    public static List<String> getTimeQuantum(String date, String time) {
        String[] arr = time.split("-");
        List<String> child = new ArrayList<>();
        int hour = Integer.parseInt(arr[0].split(":")[0]);
        while (hour <= Integer.parseInt(arr[1].split(":")[0])) {
            child.add(date + " " + (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":00");
            hour++;
        }
        return child;
    }

    /****
     * 数字转换星期
     * @param num
     * @return
     */
    public static String numToWeek(int num) {
        switch (num) {

            case java.util.Calendar.MONDAY:
                return "周一";

            case java.util.Calendar.TUESDAY:
                return "周二";

            case java.util.Calendar.WEDNESDAY:
                return "周三";

            case java.util.Calendar.THURSDAY:
                return "周四";

            case java.util.Calendar.FRIDAY:
                return "周五";

            case java.util.Calendar.SATURDAY:
                return "周六";

            case java.util.Calendar.SUNDAY:

                return "周日";

        }
        return "周一";
    }

    /*****
     * 数字转化星期
     * @param num
     * @return
     */
    public static String numToWeek2(int num) {
        switch (num) {
            case 1:
                return "周一";

            case 2:
                return "周二";

            case 3:
                return "周三";

            case 4:
                return "周四";

            case 5:
                return "周五";

            case 6:
                return "周六";

            case 7:
                return "周日";

        }
        return "周一";
    }

    /*****
     * 获取不可上课时间
     * @param canTime "1,2,3" 哪个时间段可以上课 1：上午 2：下午 3：晚上
     * @return
     */
    public static Map<String, Integer> getCanNotTime(String canTime) {
        Map<String, Integer> canNotMap = new HashMap<>();
        //上午
        canNotMap.put("06:00", 2);
        canNotMap.put("07:00", 2);
        canNotMap.put("08:00", 2);
        canNotMap.put("09:00", 2);
        canNotMap.put("10:00", 2);
        canNotMap.put("11:00", 2);
        //下午
        canNotMap.put("12:00", 2);
        canNotMap.put("13:00", 2);
        canNotMap.put("14:00", 2);
        canNotMap.put("15:00", 2);
        canNotMap.put("16:00", 2);
        canNotMap.put("17:00", 2);
        //晚上
        canNotMap.put("18:00", 2);
        canNotMap.put("19:00", 2);
        canNotMap.put("20:00", 2);
        canNotMap.put("21:00", 2);
        canNotMap.put("22:00", 2);
        canNotMap.put("23:00", 2);
        //不安排时间
        if (canTime.contains("1")) {
            canNotMap.remove("06:00");
            canNotMap.remove("07:00");
            canNotMap.remove("08:00");
            canNotMap.remove("09:00");
            canNotMap.remove("10:00");
            canNotMap.remove("11:00");
        }
        if (canTime.contains("2")) {
            canNotMap.remove("12:00");
            canNotMap.remove("13:00");
            canNotMap.remove("14:00");
            canNotMap.remove("15:00");
            canNotMap.remove("16:00");
            canNotMap.remove("17:00");
        }
        if (canTime.contains("3")) {
            canNotMap.remove("18:00");
            canNotMap.remove("19:00");
            canNotMap.remove("20:00");
            canNotMap.remove("21:00");
            canNotMap.remove("22:00");
            canNotMap.remove("23:00");
        }
        return canNotMap;
    }

    /*****
     *封装老师上课时间忙
     * @param busyList 忙数组
     * @return
     */
    public static Map<String, Integer> getBusyTime(List<Integer> busyList) {
        Map<String, Integer> busyMap = new HashMap<>();
        for (Integer busy : busyList) {
            busyMap.put((busy < 10 ? "0" + busy : busy) + ":00", 1);
        }
        return busyMap;
    }

    /*****
     * 星期转换数字
     * @param week
     * @return
     */
    public static int weekToNum(String week) {
        switch (week) {

            case "周一":
                return java.util.Calendar.MONDAY;

            case "周二":
                return java.util.Calendar.TUESDAY;

            case "周三":
                return java.util.Calendar.WEDNESDAY;

            case "周四":
                return java.util.Calendar.THURSDAY;

            case "周五":
                return java.util.Calendar.FRIDAY;

            case "周六":
                return java.util.Calendar.SATURDAY;

            case "周日":
                return java.util.Calendar.SUNDAY;
        }
        return java.util.Calendar.MONDAY;
    }

    /******
     * 根据已知日期的星期获取最近星期的日期
     * @param weekStr (周几)
     * @return 日期
     */
    public static String getDateForWeek(String weekStr) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        int currentWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        int oldWeek = weekToNum(weekStr);
        if (currentWeek > oldWeek) {
            //取下一周的日期(往前推到本周后+7天)
            calendar.add(calendar.DATE, oldWeek - currentWeek + 7);
        } else if (currentWeek < oldWeek) {
            //区本周的日期（往后退）
            calendar.add(calendar.DATE, oldWeek - currentWeek);
        }
        //取当天日期
        return calendar.get(java.util.Calendar.YEAR) + "-" + (calendar.get(java.util.Calendar.MONTH) + 1 < 10 ? "0" + (calendar.get(java.util.Calendar.MONTH) + 1) : calendar.get(java.util.Calendar.MONTH) + 1) +
                "-" + (calendar.get(java.util.Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(java.util.Calendar.DAY_OF_MONTH) : calendar.get(java.util.Calendar.DAY_OF_MONTH));
    }

    /******
     * 排除重复的数据
     * @param pool 池
     * @param sour 需要排除的数据
     * @return
     */
    public static List<String> removeRepetition(List<String> pool, List<String> sour) {
        for (String item : sour) {
            for (String item1 : pool) {
                if (item.equals(item1)) {
                    pool.remove(item1);
                    continue;
                }
            }
        }
        return pool;
    }

    /****
     * 是否无无效时间
     * @param dateTime
     * @return
     */
    public static boolean isPastDue(CalendarDate dateTime,String time){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int YEAR =  calendar.get(java.util.Calendar.YEAR);
        int MONTH =  calendar.get(java.util.Calendar.MONTH)+1;
        int DAY =  calendar.get(java.util.Calendar.DAY_OF_MONTH);
        int HOUR =  calendar.get(java.util.Calendar.HOUR_OF_DAY);

        return YEAR == dateTime.getYear()&&MONTH == dateTime.getMonth()&&DAY == dateTime.getDay()&&Integer.parseInt(time.split(":")[0])<=HOUR+1;
    }

    /*****
     * 获取键值对形式的一天中哪几个时间忙容器
     * @return (date.getYear() + "年"+(date.getMonth()<10?"0"+date.getMonth():date.getMonth()) + "月"+(date.getDay()<10?"0"+date.getDay():date.getDay())+"日"
     */
    public static Map<String,int[]> getCantimerMap(List<CanTimer> canTimerList){
        Map<String,int[]> cantimerMap = new HashMap<>();
        if (canTimerList!=null&&!canTimerList.isEmpty()){
            for (CanTimer canTimer : canTimerList){
                if (canTimer.getHoureTimes()==null)
                    continue;
                cantimerMap.put(DateUtil.timeStampDate(canTimer.getDayTime(),"yyyy年MM月dd"),canTimer.getHoureTimes());
            }
        }
        return cantimerMap;
    }

}
