package com.main.disanxuelib.util;

import android.widget.Switch;

import com.main.disanxuelib.app.GreenDaoManager;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.gen.CityDao;
import com.main.disanxuelib.gen.DaoSession;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by liukui on 2016/12/14 0014.
 */

public class CityDaoUtil {
    /**
     * 查询所有【省】级城市
     */
    public static List<City> getAllCityProvince() {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.Parent_id.eq('0'))
                .orderAsc(CityDao.Properties.Area_id).build();
        return query.list();
    }

    /**
     * 查询所有【pid】的城市
     */
    public static List<City> getCityByPid(int pid) {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        daoSession.clear();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.Parent_id.eq(addZeroId(pid)))
                .orderAsc(CityDao.Properties.Area_id).build();
        return query.list();
    }

    /**
     * 根据上一级的PD和当前区域名字查询得到当前区域名字
     */
    public static City getCityByIdAndName(int id, String name) {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        Query query = cityDao.queryBuilder().where(CityDao.Properties.Parent_id.eq(addZeroId(id)), CityDao.Properties.Area_name.eq(name)).build();
        return (City) query.unique();
    }

    /**
     * 查询【id】的城市
     */
    public static City getCityById(String id) {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        daoSession.clear();
        CityDao cityDao = daoSession.getCityDao();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.Area_id.eq(addZeroId(id))).build();
        return (City) query.unique();
    }

    /**
     * 查询【name】的城市
     */
    public static City getCityByName(String name,String areaLevel) {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        daoSession.clear();
        QueryBuilder<City> query = cityDao.queryBuilder()
                .where(CityDao.Properties.Area_name.eq(name),CityDao.Properties.Area_level.eq(areaLevel))
                .orderAsc(CityDao.Properties.Area_id);

        return query.unique();
    }

    /**
     * 查询【name】的城市
     */
    public static City getCityByName(String name) {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        daoSession.clear();
        QueryBuilder<City> query = cityDao.queryBuilder()
                .where(CityDao.Properties.Area_name.eq(name))
                .orderAsc(CityDao.Properties.Area_id);

        return query.unique();
    }

    /**
     * 查询所有【市】级城市
     */
    public static List<City> getAllCity() {
        DaoSession daoSession = GreenDaoManager.getInstance().getmDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        QueryBuilder<City> query = cityDao.queryBuilder()
                .where(new WhereCondition.StringCondition("PID IN " + "(SELECT ID FROM CITY WHERE PID = 0)"))
                .orderAsc(CityDao.Properties.English_name);
        return query.list();
    }

    /****
     * 地址补0
     * @param pid
     */
     private static String addZeroId(Object pid){
         String pidStr = pid instanceof String ? (String) pid : String.valueOf(pid);
         switch (pidStr.length()){
             case 1:
                 pidStr +="00000";
                 break;

             case 2:
                 pidStr +="0000";
                 break;

             case 3:
                 pidStr +="000";
                 break;

             case 4:
                 pidStr +="00";
                 break;

             case 5:
                 pidStr +="0";
                 break;

         }
         return pidStr;
     }

}
