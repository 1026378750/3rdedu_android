package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseBean;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadScheduleBean;
import com.shengzhe.disan.xuetangparent.bean.OrderCourse;
import com.shengzhe.disan.xuetangparent.bean.CourseSquadBean;
import com.shengzhe.disan.xuetangparent.bean.MySchedule;
import com.shengzhe.disan.xuetangparent.bean.TeaHomePageBean;
import com.shengzhe.disan.xuetangparent.bean.MyCourseInfo;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.model.ScheduleModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.view.BaseView;
import com.shengzhe.disan.xuetangparent.mvp.view.CourseDetailChildView;
import com.shengzhe.disan.xuetangparent.mvp.view.CourseDetailView;
import com.shengzhe.disan.xuetangparent.mvp.view.CourseOutlineView;
import com.shengzhe.disan.xuetangparent.mvp.view.HomeSubjectView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineCourseView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineScheduleView;
import com.shengzhe.disan.xuetangparent.mvp.view.MyCourseDetailsView;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineTeacherView;
import com.shengzhe.disan.xuetangparent.mvp.view.TeacherMessageView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 课程业务 on 2018/4/20.
 */

public class CoursePresenter extends BasePresenter implements MVPRequestListener {
    private CourseModelImpl courseModel;
    private CommonModelImpl commonModel;
    private ScheduleModelImpl scheduleModel;

    private MineCourseView mineCourseView;
    private CourseDetailView courseDetailView;
    private OfflineTeacherView offlineTeahcerView;
    private TeacherMessageView teacherMessageView;
    private HomeSubjectView homeSubjectView;
    private MineScheduleView mineScheduleView;
    private MyCourseDetailsView myCourseDetailsView;
    private CourseDetailChildView courseDetailChildView;
    private CourseOutlineView courseOutlineView;

    private MineCourseView.IMineCourseView iMineCourseView;
    private CourseDetailView.ICourseDetailView iCourseDetailView;
    private OfflineTeacherView.IOfflineTeacherView iOfflineTeacherView;
    private TeacherMessageView.ITeacherMessageView iTeacherMessageView;
    private HomeSubjectView.IHomeSubjectView iHomeSubjectView;
    private MineScheduleView.IMineScheduleView iMineScheduleView;
    private MyCourseDetailsView.IMyCourseDetailsView iMyCourseDetailsView;
    private CourseDetailChildView.ICourseDetailChildView iCourseDetailChildView;
    private CourseOutlineView.ICourseOutlineView iCourseOutlineView;


    public CoursePresenter(Context context, MineCourseView.IMineCourseView iView) {
        super(context);
        this.iMineCourseView = iView;
    }

    public CoursePresenter(Context mContext, CourseDetailView.ICourseDetailView iView) {
        super(mContext);
        this.iCourseDetailView = iView;
    }

    public CoursePresenter(Context mContext, OfflineTeacherView.IOfflineTeacherView iView) {
        super(mContext);
        this.iOfflineTeacherView = iView;
    }

    public CoursePresenter(Context mContext, TeacherMessageView.ITeacherMessageView iView) {
        super(mContext);
        this.iTeacherMessageView = iView;
    }

    public CoursePresenter(Context mContext,HomeSubjectView.IHomeSubjectView iView) {
        super(mContext);
        this.iHomeSubjectView = iView;
    }

    public CoursePresenter(Context mContext,MineScheduleView.IMineScheduleView iView) {
        super(mContext);
        this.iMineScheduleView = iView;
    }

    public CoursePresenter(Context mContext, MyCourseDetailsView.IMyCourseDetailsView iView) {
        super(mContext);
        this.iMyCourseDetailsView = iView;
    }

    public CoursePresenter(Context mContext, CourseDetailChildView.ICourseDetailChildView iView) {
        super(mContext);
        this.iCourseDetailChildView = iView;
    }

    public CoursePresenter(Context mContext, CourseOutlineView.ICourseOutlineView iView) {
        super(mContext);
        this.iCourseOutlineView = iView;
    }

    public void initMineCourseItemUi(RefreshCommonView.RefreshLoadMoreListener listener) {
        if (mineCourseView == null)
            mineCourseView = new MineCourseView(mContext, listener);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iMineCourseView.getClass().getName());
        mineCourseView.setIMineCourseView(iMineCourseView);
    }

    public void initMineCourseDetailUi(FragmentManager fragmentManager) {
        if (courseDetailView == null)
            courseDetailView = new CourseDetailView(mContext, fragmentManager);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iCourseDetailView.getClass().getName());
        courseDetailView.setICourseDetailView(iCourseDetailView);
    }

    public void initOfflineTeacherUi(FragmentManager fragmentManager) {
        if (offlineTeahcerView == null)
            offlineTeahcerView = new OfflineTeacherView(mContext, fragmentManager);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iOfflineTeacherView.getClass().getName());
        offlineTeahcerView.setIOfflineTeacherView(iOfflineTeacherView);
    }

    public void initTeacherMessageUi() {
        if (teacherMessageView == null)
            teacherMessageView = new TeacherMessageView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iTeacherMessageView.getClass().getName());
        teacherMessageView.setITeacherMessageView(iTeacherMessageView);
    }

    public void initHomeSubjectUi() {
        if (homeSubjectView==null)
            homeSubjectView = new HomeSubjectView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iHomeSubjectView.getClass().getName());
        homeSubjectView.setIHomeSubjectView(iHomeSubjectView);
    }

    public void initMineScheduleUi(BaseView.WebApiListener listener) {
        if (mineScheduleView==null)
            mineScheduleView = new MineScheduleView(mContext);
        if (scheduleModel ==null)
            scheduleModel = new ScheduleModelImpl(mContext,this,iMineScheduleView.getClass().getName());
        mineScheduleView.setIMineScheduleView(iMineScheduleView);
        mineScheduleView.setWebApiListener(listener);
    }

    public void initMyCourseDetailsUi() {
        if (myCourseDetailsView==null)
            myCourseDetailsView = new MyCourseDetailsView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iMyCourseDetailsView.getClass().getName());
        myCourseDetailsView.setIMyCourseDetailsView(iMyCourseDetailsView);
    }

    public void initCourseDetailChildUi() {
        if (courseDetailChildView==null)
            courseDetailChildView = new CourseDetailChildView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iCourseDetailChildView.getClass().getName());
        courseDetailChildView.setICourseDetailChildView(iCourseDetailChildView);
    }

    public void initCourseOutlineUi() {
        if (courseOutlineView==null)
            courseOutlineView = new CourseOutlineView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iCourseOutlineView.getClass().getName());
        courseOutlineView.setICourseOutlineView(iCourseOutlineView);
    }

    public void initMineCourseItemDatas() {
        mineCourseView.initDatas();
    }


    public List<OrderCourse> getMineCourseArrayList() {
        return mineCourseView.getMineCourseArrayList();
    }


    public void loadMineCourse(int status, int pageNum) {
        courseModel.getMyCourseList(status, pageNum);
    }

    public void initMineCourseDetailDatas(Intent intent) {
        courseDetailView.initDatas(intent);

    }

    public void loadCourseDetail() {
        courseModel.getCourseSquadDetail(courseDetailView.getCourseId());
    }

    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_TeacherSubject:
                //科目列表
                homeSubjectView.setResultDatas(objects==null?new BasePageBean<TeacherInformation>():(BasePageBean<TeacherInformation>)objects);
                break;

            case IntegerUtil.WEB_API_MyCourseList:
                //课程列表
                mineCourseView.setResultDatas(objects==null? new BasePageBean<OrderCourse>():(BasePageBean<OrderCourse>) objects);
                break;

            case IntegerUtil.WEB_API_CourseSquadDetail:
                //课程详情
                if (iCourseDetailView!=null&&from.equals(iCourseDetailView.getClass().getName()))
                    courseDetailView.setResultDatas(objects==null?new CourseSquadBean():(CourseSquadBean) objects);
                else if(iCourseDetailChildView!=null&&from.equals(iCourseDetailChildView.getClass().getName()))
                    courseDetailChildView.setResultDatas(objects==null?new CourseSquadBean():(CourseSquadBean) objects);
                break;

            case IntegerUtil.WEB_API_TeacherPage:
                //得到老师课程信息
                teacherMessageView.setResultDatas(objects==null?new TeaHomePageBean():(TeaHomePageBean) objects);
                break;

            case IntegerUtil.WEB_API_MySchedule:
                mineScheduleView.setResultDatas(objects==null?new ArrayList<MySchedule>():(List<MySchedule>)objects);
                break;

            case IntegerUtil.WEB_API_MonthStatus:
                mineScheduleView.setMonthStatus(objects==null?new ArrayList<String>():(List<String>)objects);
                break;

            case IntegerUtil.WEB_API_MyCourseDetail:
                myCourseDetailsView.setResultDatas(objects==null?new MyCourseInfo():(MyCourseInfo)objects);
                break;

            case IntegerUtil.WEB_API_ScheduleSignStatus:
                ToastUtil.showToast("签到成功");
                mineScheduleView.setSignDatas((CourseBean)objects);
                break;

            case IntegerUtil.WEB_API_SquadOutlineList:
                courseOutlineView.setResultDatas(objects==null?new ArrayList<CourseSquadScheduleBean>():(List<CourseSquadScheduleBean>)objects);
                break;

        }
    }

    @Override
    public void onFailed(int tager, String mesg,String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_MyCourseList:
                mineCourseView.finishLoad();
                break;

            case IntegerUtil.WEB_API_TeacherSubject:
                homeSubjectView.finishLoad();
                break;

            case IntegerUtil.WEB_API_SquadOutlineList:
                courseOutlineView.finishLoad();
                break;

            default:
                ToastUtil.showShort(mesg);
                break;
        }
    }

    public int getTeacherId() {
        return courseDetailView.getTeacherId();
    }


    public int getCourseId() {
        return courseDetailView.getCourseId();
    }

    public void goOfflineLessonOrder() {
        courseDetailView.goOfflineLessonOrder();
    }

    public void setOfflineTeacherDatas(TeaHomePageBean offlineTeacherDatas) {
        offlineTeahcerView.setParameter(offlineTeacherDatas);
    }

    public void initOfflineTeacherDatas(int teacherId) {
        offlineTeahcerView.initDatas(teacherId);
    }

    public void initTeacherMessageDatas() {
        teacherMessageView.initDatas();
    }

    public void getTeacherMesg(int teacherId) {
        commonModel.getTeacherPage(teacherId);
    }

    public void setHomeSubjectDatas(DropDownMenu.DropMenuFragmentManage fragmentManageListener, Subject subject, RefreshCommonView.RefreshLoadMoreListener listener) {
        homeSubjectView.initDatas(fragmentManageListener,subject,listener);
    }

    public void clearSubject() {
        homeSubjectView.clearDatas();
    }

    public void loadHomeSubject(int pageNum) {
        courseModel.getTeacherSubject(homeSubjectView.getParameter(), SharedPreferencesManager.getCityId(),pageNum,homeSubjectView.getSubjectId());
    }

    public void initMineScheduleDatas(FragmentManager childFragmentManager) {
        mineScheduleView.initDatas(childFragmentManager);
    }

    public void getMonthStatus() {
        scheduleModel.getMonthStatus(mineScheduleView.getExchange(),mineScheduleView.getTimeStr());
    }

    public void getMySchedule() {
        scheduleModel.getMySchedule(mineScheduleView.getTimeStr());
    }

    public void clearMineScheduleDatas() {
        mineScheduleView.clearAllDatas();
    }

    public void initMyCourseDetailsDatas(OrderCourse course, FragmentManager fragmentManager) {
        myCourseDetailsView.initDatas(course,fragmentManager);
    }

    public void myCourseDetail(int id) {
        courseModel.getMyCourseDetail(id);
    }

    public void myCourseProblem() {
        myCourseDetailsView.courseProblem();
    }

    public void saveScheduleSignStatus(CourseBean courseBean) {
       scheduleModel.saveScheduleSignStatus(courseBean);
    }

    public void getCourseSquadInfo(int courseId) {
        courseModel.getCourseSquadDetail(courseId);
    }

    public void initCourseOutlineDatas() {
        courseOutlineView.initDatas();
    }

    public void getSquadScheduleList(int courseId) {
        courseModel.getSquadOutlineList(courseId);
    }
}
