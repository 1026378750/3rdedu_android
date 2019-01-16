package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangparent.bean.SquadRecommendInformation;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.view.ClassItemView;
import com.shengzhe.disan.xuetangparent.mvp.view.OfflineClassView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.util.List;

/**
 * Created by 线下班课 on 2018/4/26.
 */

public class ClassPresenter extends BasePresenter implements MVPRequestListener {
    private CourseModelImpl courseModel;
    private CommonModelImpl commonModel;

    private ClassItemView classItemView;
    private OfflineClassView offlineClassView;

    private ClassItemView.IClassItemView iClassItemView;
    private OfflineClassView.IOfflineClassView iOfflineClassView;

    public ClassPresenter(Context mContext, ClassItemView.IClassItemView iView) {
        super(mContext);
        this.iClassItemView = iView;
    }

    public ClassPresenter(Context mContext, OfflineClassView.IOfflineClassView iView) {
        super(mContext);
        this.iOfflineClassView = iView;
    }

    public void initClassItemUi() {
        if (classItemView == null)
            classItemView = new ClassItemView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iClassItemView.getClass().getName());
        classItemView.setIClassItemView(iClassItemView);
    }

    public void initClassListUi(int teacherId) {
        if (offlineClassView == null)
            offlineClassView = new OfflineClassView(mContext,teacherId);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iOfflineClassView.getClass().getName());
        offlineClassView.setIOfflineClassView(iOfflineClassView);
    }

    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_CourseList:
                //获取课程列表
                classItemView.setResultDatas(objects == null ? new BasePageBean<CourseLiveBean>() : (BasePageBean<CourseLiveBean>) objects);
                break;

            case IntegerUtil.WEB_API_SquadCourseList:
                //获取课程列表
                offlineClassView.setResultDatas(objects == null ? new BasePageBean<SquadRecommendInformation>() : (BasePageBean<SquadRecommendInformation>) objects);
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_CourseList:
                classItemView.finishLoad();
                break;
        }
    }


    public void setClassItemDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        classItemView.setDatas(listener);
    }

    public List<CourseLiveBean> getClassItemCourseList() {
        return classItemView.getClassItemCourseList();
    }

    public void loadClassItemCourseList(int teacherId,int pageNum) {
        courseModel.getCourseList(4,teacherId,pageNum);
    }

    public void initClassListDatas(Activity activity, DropDownMenu.DropMenuFragmentManage fragmentManage,RefreshCommonView.RefreshLoadMoreListener listener) {
        offlineClassView.initDatas(activity,fragmentManage,listener);
    }

    public void clearOfflineClassListDatas() {
        offlineClassView.clearDatas();
    }

    public void loadOfflineClassList(int pageNum) {
        courseModel.getSquadCourseList(offlineClassView.getParameter(),SharedPreferencesManager.getCityId(),pageNum);
    }
}
