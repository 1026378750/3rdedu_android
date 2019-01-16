package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;

import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.banner.MyBanner;
import com.mbg.library.IRefreshListener;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.City;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.view.ApplyAuditionView;
import com.shengzhe.disan.xuetangparent.mvp.view.MainView;
import com.shengzhe.disan.xuetangparent.bean.LiveAndVideo;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class MainPresenter extends BasePresenter implements MVPRequestListener {
    private CourseModelImpl courseModel;
    private CommonModelImpl commonModel;


    private MainView mainView;
    private ApplyAuditionView applyAuditionView;

    private MainView.IMainView iMainView;
    private ApplyAuditionView.IApplyAuditionView iApplyAuditionView;

    public MainPresenter(Context context, MainView.IMainView iView) {
        super(context);
        this.iMainView = iView;
    }

    public MainPresenter(Context mContext, ApplyAuditionView.IApplyAuditionView iView) {
        super(mContext);
        this.iApplyAuditionView = iView;
    }

    public void initMainUi() {
        if (mainView == null)
            mainView = new MainView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iMainView.getClass().getName());
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iMainView.getClass().getName());
        mainView.setIMainView(iMainView);
    }

    public void initApplyAuditionUi(FragmentManager fragmentManager) {
        if (applyAuditionView == null)
            applyAuditionView = new ApplyAuditionView(mContext,fragmentManager);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this,iApplyAuditionView.getClass().getName());
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iApplyAuditionView.getClass().getName());
        applyAuditionView.setIApplyAuditionView(iApplyAuditionView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initMainDatas(MyBanner.BannerItemOnClickListener bannerListener, IRefreshListener listener) {
        mainView.initDatas(bannerListener, listener);
    }

    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_CommonSubject:
                if (iMainView!=null&&from.equals(iMainView.getClass().getName()))
                    mainView.setSubjectResultDates(objects==null?"":String.valueOf(objects));
                else if (iApplyAuditionView!=null&&from.equals(iApplyAuditionView.getClass().getName()))
                    applyAuditionView.setResultSubjectDatas(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_LiveAndVideo:
                mainView.setLiveVideoResultDates(objects==null? new LiveAndVideo():(LiveAndVideo) objects);
                break;

            case IntegerUtil.WEB_API_RecommendTeacher:
                mainView.setTeacherResultDates(objects==null? new BasePageBean<TeacherInformation>():(BasePageBean<TeacherInformation>) objects);
                break;

            case IntegerUtil.WEB_API_OpenCity:
                //地址查询
                applyAuditionView.setResultAddressDatas(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_GradeList:
                //年级查询
                applyAuditionView.setResultGradeDatas(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_ApplyTryListen:
                //申请试听
                applyAuditionView.setResultTryListen();
                break;

        }
    }

    @Override
    public void onFailed(int tager, String mesg,String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_RecommendTeacher:
                mainView.loadFinish();
                break;
            default:
                ToastUtil.showShort(mesg);
                break;
        }
    }

    public void loadMainRefreshDatas(int pageNum) {
        if (mainView.isSubjectListEmpty()) {
            commonModel.getCommonSubject();
        }
        mainView.clearDatas();
        String cityId = SharedPreferencesManager.getCityId();
        courseModel.getLiveAndVideo(cityId);
        courseModel.getRecommendTeacher(cityId, pageNum);
    }

    public void loadMainLoadMoreDatas(int pageNum) {
        courseModel.getRecommendTeacher(SharedPreferencesManager.getCityId(), pageNum);
    }

    public void setNotifyFloat() {
        mainView.setNotifyFloat();
    }


    public void setApplyAuditionDatas(List<City> cityList, List<CourseType> gradeList, List<Subject> subjectList) {
        applyAuditionView.initDatas(cityList,gradeList,subjectList);
    }

    public void getCityList() {
        commonModel.getOpenCityList();
    }

    public void getGradeList() {
        commonModel.getGradeList();
    }

    public void getSubjectList() {
        commonModel.getCommonSubject();
    }

    public void appCourseListen() {
        if (applyAuditionView.judgeCondition(true)){
            //提交
            courseModel.applyTryListen(applyAuditionView.cityCode(),applyAuditionView.getGradeId(),applyAuditionView.getSubjectId());
        }
    }

    public void selectApplyAuditionCity() {
        applyAuditionView.selectCityPopup();
    }

    public void selectApplyAuditionGrade() {
        applyAuditionView.selectGradePopup();
    }

    public void selectApplyAuditionSubject() {
        applyAuditionView.selectSubjectPopup();
    }
}
