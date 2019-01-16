package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.CourseDate;
import com.main.disanxuelib.bean.Schedule;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.model.ScheduleModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.view.ModifyCalendarView;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineItemView;
import com.shengzhe.disan.xuetangparent.mvp.view.OneOnOneView;
import com.shengzhe.disan.xuetangparent.mvp.view.OneToOneDetailsView;
import com.shengzhe.disan.xuetangparent.mvp.view.ScheduleCalendarView;
import com.shengzhe.disan.xuetangparent.mvp.view.SelectSchooltimeView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 线下一对一 on 2018/4/26.
 */

public class OneToOnePresenter extends BasePresenter implements MVPRequestListener {
    private CourseModelImpl courseModel;
    private CommonModelImpl commonModel;
    private ScheduleModelImpl schedulesModel;

    private OfflineItemView offlineItemView;
    private OneToOneDetailsView oneToOneDetailsView;
    private SelectSchooltimeView selectSchooltimeView;
    private ScheduleCalendarView scheduleCalendarView;
    private ModifyCalendarView modifyCalendarView;
    private OneOnOneView oneOnOneView;

    private OfflineItemView.IOfflineItemView iOfflineItemView;
    private OneToOneDetailsView.IOneToOneDetailsView iOneToOneDetailsView;
    private SelectSchooltimeView.ISelectSchooltimeView iSelectSchooltimeView;
    private ScheduleCalendarView.IScheduleCalendarView iScheduleCalendarView;
    private ModifyCalendarView.IModifyCalendarView iModifyCalendarView;
    private OneOnOneView.IOneOnOneView iOneOnOneView;

    public OneToOnePresenter(Context mContext, OfflineItemView.IOfflineItemView iView) {
        super(mContext);
        this.iOfflineItemView = iView;
    }

    public OneToOnePresenter(Context mContext, OneToOneDetailsView.IOneToOneDetailsView iView) {
        super(mContext);
        this.iOneToOneDetailsView = iView;
    }

    public OneToOnePresenter(Context mContext, SelectSchooltimeView.ISelectSchooltimeView iView) {
        super(mContext);
        this.iSelectSchooltimeView = iView;
    }
    public OneToOnePresenter(Context context, ScheduleCalendarView.IScheduleCalendarView iView) {
        super(context);
        this.iScheduleCalendarView = iView;
    }

    public OneToOnePresenter(Context mContext, ModifyCalendarView.IModifyCalendarView iView) {
        super(mContext);
        this.iModifyCalendarView = iView;
    }

    public OneToOnePresenter(Context mContext,OneOnOneView.IOneOnOneView iView) {
        super(mContext);
        this.iOneOnOneView = iView;
    }

    public void initOfflineItemUi() {
        if (offlineItemView == null)
            offlineItemView = new OfflineItemView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iOfflineItemView.getClass().getName());
        offlineItemView.setIOfflineItemView(iOfflineItemView);
    }

    public void initOneToOneDetailsUi(FragmentManager fragmentManager) {
        if (oneToOneDetailsView == null)
            oneToOneDetailsView = new OneToOneDetailsView(mContext,fragmentManager);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iOneToOneDetailsView.getClass().getName());
        oneToOneDetailsView.setIOneToOneDetailsView(iOneToOneDetailsView);
    }

    public void initSelectSchooltimeUi(FragmentManager supportFragmentManager) {
        if (selectSchooltimeView == null)
            selectSchooltimeView = new SelectSchooltimeView(mContext,supportFragmentManager);
        if (schedulesModel == null)
            schedulesModel = new ScheduleModelImpl(mContext, this,iSelectSchooltimeView.getClass().getName());
        selectSchooltimeView.setISelectSchooltimeView(iSelectSchooltimeView);
    }

    public void initScheduleCalendarUi() {
        if (scheduleCalendarView == null)
            scheduleCalendarView = new ScheduleCalendarView(mContext);
        if (schedulesModel == null)
            schedulesModel = new ScheduleModelImpl(mContext, this,iScheduleCalendarView.getClass().getName());
        scheduleCalendarView.setIScheduleCalendarView(iScheduleCalendarView);
    }

    public void initModifyCalendarUi(Bundle bundle) {
        if (modifyCalendarView == null)
            modifyCalendarView = new ModifyCalendarView(mContext,bundle);
        if (schedulesModel == null)
            schedulesModel = new ScheduleModelImpl(mContext, this,iModifyCalendarView.getClass().getName());
        modifyCalendarView.setIModifyCalendarView(iModifyCalendarView);
    }

    public void initOneOnOneUi() {
        if (oneOnOneView==null)
            oneOnOneView = new OneOnOneView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iOneOnOneView.getClass().getName());
        oneOnOneView.setIOneOnOneView(iOneOnOneView);
    }


    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_CourseStartList:
                //老师开课集合
                offlineItemView.setResultDatas(objects==null?new BasePageBean<CourseOflineBean>():(BasePageBean<CourseOflineBean>) objects);
                break;

            case IntegerUtil.WEB_API_CourseOneDetail:
                //详情
                oneToOneDetailsView.setDetailDatas(objects==null?new CourseOneInfo():(CourseOneInfo)objects);
                break;

            case IntegerUtil.WEB_API_ApplyOneListen:
                //教学方式
                oneToOneDetailsView.setApplyListen(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_CanTimer:
                //
                selectSchooltimeView.setResultDatas(objects==null?new ArrayList<CourseDate>():(List<CourseDate>)objects);
                break;

            case IntegerUtil.WEB_API_TeacherTimeType:
                if (iScheduleCalendarView!=null&&from.equals(iScheduleCalendarView.getClass().getName()))
                    scheduleCalendarView.setResultDatas(objects==null?new ArrayList<Integer>():(List<Integer>) objects);
                else if(iModifyCalendarView!=null&&from.equals(iModifyCalendarView.getClass().getName()))
                    modifyCalendarView.setResultDatas(objects==null?new ArrayList<Integer>():(List<Integer>) objects);
                break;

            case IntegerUtil.WEB_API_RecommendTeacher:
                oneOnOneView.setResultDatas(objects==null?new BasePageBean<TeacherInformation>():(BasePageBean<TeacherInformation>)objects);
                break;
        }
    }
    @Override
    public void onFailed(int tager, String mesg,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_CourseStartList:
                offlineItemView.finishLoad();
                break;

            case IntegerUtil.WEB_API_RecommendTeacher:
                oneOnOneView.finishLoad();
                break;

            default:
                ToastUtil.showShort(mesg);
                break;
        }
    }

    public void initOfflineItemDatas(RefreshCommonView.RefreshLoadMoreListener listaner) {
        offlineItemView.initDatas(listaner);
    }

    public void loadOfflineTeacher(int courseType, int teacherId, boolean isLoad) {
        courseModel.getCourseStartList(courseType, teacherId, isLoad);
    }

    public void getCourseOneInfo(int courseId) {
        courseModel.getCourseOneDetail(courseId);
    }

    public void applyCourseListen(int courseId) {
        courseModel.applyOneListen(courseId);
    }

    public void oneDetailConfirm(Intent intent) {
        oneToOneDetailsView.comfirmBtn(intent);
    }

    public void applyDetailDatas(int courseId) {
        if (oneToOneDetailsView.applyDatas()) {
            //申请试听
            applyCourseListen(courseId);
        }
    }

    public void oneDetailBack() {
        oneToOneDetailsView.BackDatas();
    }

    public void initSelectSchooltimeDatas(int courseId, Bundle extras, int selectNum) {
        selectSchooltimeView.initDatas(courseId,extras,selectNum);
    }

    public void getCanTimer(int courseId) {
        schedulesModel.getCanTimer(courseId);
    }

    public void selectSchooltime(String[] args) {
        selectSchooltimeView.selectSchooltime(args);
    }

    public void clearSelectSchoolDatas() {
        selectSchooltimeView.clearDatas();
    }

    public ArrayList<Calendar> getCalList() {
        return selectSchooltimeView.getCalList();
    }

    public void initScheduleCalendarDatas(Intent extras, FragmentManager supportFragmentManager) {
        scheduleCalendarView.initDatas(extras,supportFragmentManager);
    }

    public void getTeacherTimeType(int courseId) {
        schedulesModel.getTeacherTimeType(courseId,scheduleCalendarView.getTimes());
    }

    public void initModifyCalendarDatas(FragmentManager supportFragmentManager) {
        modifyCalendarView.initDatas(supportFragmentManager);
    }

    public void getMTeacherTimeType(int courseId) {
        schedulesModel.getTeacherTimeType(courseId,modifyCalendarView.getTimes());
    }

    public Schedule getSchedule(){
        return modifyCalendarView.getSchedule();
    }

    public void clearModifyDatas() {
        modifyCalendarView.clearDatas();
    }

    public void clearScheduleCalendarDatas() {
        scheduleCalendarView.clearDatas();
    }

    public void initOneOnOneDatas(DropDownMenu.DropMenuFragmentManage fragmentManage, RefreshCommonView.RefreshLoadMoreListener listener) {
        oneOnOneView.initDatas(fragmentManage,listener);
    }

    public void loadOneOnOneList(int pageNum) {
        courseModel.getRecommendTeacher(SharedPreferencesManager.getCityId(),pageNum);
    }

    public void clearOneOnOneListDatas() {
        oneOnOneView.clearDatas();
    }
}
