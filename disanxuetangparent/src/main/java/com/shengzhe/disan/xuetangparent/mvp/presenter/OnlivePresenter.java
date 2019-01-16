package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.CourseItem;
import com.shengzhe.disan.xuetangparent.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangparent.bean.LiveBean;
import com.shengzhe.disan.xuetangparent.bean.LiveInfo;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.view.LiveDetailView;
import com.shengzhe.disan.xuetangparent.mvp.view.LivePlanView;
import com.shengzhe.disan.xuetangparent.mvp.view.OnLiveView;
import com.shengzhe.disan.xuetangparent.mvp.view.OnliveItemView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

/**
 * Created by Administrator on 2018/4/26.
 */

public class OnlivePresenter extends BasePresenter implements MVPRequestListener {
    private CourseModelImpl courseModel;
    private CommonModelImpl commonModel;

    private OnliveItemView onliveItemView;
    private LiveDetailView liveDetailView;
    private OnLiveView onLiveView;
    private LivePlanView livePlanView;

    private OnliveItemView.IOnliveItemView iOnliveItemView;
    private LiveDetailView.ILiveDetailView iLiveDetailView;
    private OnLiveView.IOnLiveView iOnLiveView;
    private LivePlanView.ILivePlanView iLivePlanView;

    public OnlivePresenter(Context mContext, OnliveItemView.IOnliveItemView iView) {
        super(mContext);
        this.iOnliveItemView = iView;
    }

    public OnlivePresenter(Context mContext, LiveDetailView.ILiveDetailView iView) {
        super(mContext);
        this.iLiveDetailView = iView;
    }

    public OnlivePresenter(Context mContext, OnLiveView.IOnLiveView iView) {
        super(mContext);
        this.iOnLiveView = iView;
    }

    public OnlivePresenter(Context mContext, LivePlanView.ILivePlanView iView) {
        super(mContext);
        this.iLivePlanView = iView;
    }


    public void initOnliveItemUi() {
        if (onliveItemView == null)
            onliveItemView = new OnliveItemView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this, iOnliveItemView.getClass().getName());
        onliveItemView.setIOnliveItemView(iOnliveItemView);
    }

    public void initLiveDetailUi() {
        if (liveDetailView == null)
            liveDetailView = new LiveDetailView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this, iLiveDetailView.getClass().getName());
        liveDetailView.setILiveDetailView(iLiveDetailView);
    }

    public void initOnLiveUi() {
        if (onLiveView == null)
            onLiveView = new OnLiveView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this, iOnLiveView.getClass().getName());
        onLiveView.setIOnLiveView(iOnLiveView);
    }

    public void initLivePlanUi() {
        if (livePlanView == null)
            livePlanView = new LivePlanView(mContext);
        if (courseModel == null)
            courseModel = new CourseModelImpl(mContext, this, iLivePlanView.getClass().getName());
        livePlanView.setILivePlanView(iLivePlanView);
    }

    @Override
    public void onSuccess(int tager, Object objects, String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_CourseList:
                //获取课程列表
                onliveItemView.setResultDatas(objects == null ? new BasePageBean<CourseLiveBean>() : (BasePageBean<CourseLiveBean>) objects);
                break;

            case IntegerUtil.WEB_API_OnLiveList:
                //获取课程列表
                onLiveView.setResultDatas(objects==null?new BasePageBean<LiveBean>():(BasePageBean<LiveBean>) objects);
                break;

            case IntegerUtil.WEB_API_OnLiveDetail:
                //直播课详情
                liveDetailView.setResultDatas(objects == null ? new LiveInfo() : (LiveInfo) objects);
                break;

            case IntegerUtil.WEB_API_OnliveCycleList:
                livePlanView.setResultDatas(objects==null?new CourseItem():(CourseItem) objects);
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg, String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_CourseList:
                onliveItemView.finishLoad();
                break;

            case IntegerUtil.WEB_API_OnLiveList:
                //获取课程列表
                onLiveView.finishLoad();
                break;

            case IntegerUtil.WEB_API_OnliveCycleList:
                livePlanView.finishLoad();
                break;

            default:
                ToastUtil.showShort(mesg);
                break;
        }
    }

    public void setOnliveItemDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        onliveItemView.setDatas(listener);
    }

    public void loadOnliveDatas(int teacherId, int pageNum) {
        courseModel.getCourseList(2, teacherId, pageNum);
    }

    public void getLiveInfo(int courseId) {
        courseModel.getOnLiveDetail(courseId);
    }

    public int getLiveInfoTeacherId() {
        return liveDetailView.getTeacherId();
    }

    public void initOnLiveDatas(DropDownMenu.DropMenuFragmentManage fragmentManage, RefreshCommonView.RefreshLoadMoreListener listener) {
        onLiveView.initDatas(fragmentManage, listener);
    }

    public void clearOnLiveDatas() {
        onLiveView.clearDatas();
    }

    public void loadOnLiveList(int pageNum) {
        courseModel.getOnLiveList(onLiveView.getParameter(), SharedPreferencesManager.getCityId(), pageNum);
    }

    public void initLivePlanDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        livePlanView.initDatas(listener);
    }

    public void getOnliveCycleList(int courseId) {
        livePlanView.clearDatas();
        courseModel.getOnliveCycleList(courseId);
    }
}
