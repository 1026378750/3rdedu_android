package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.main.disanxuelib.bean.VideoType;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.VideoBean;
import com.shengzhe.disan.xuetangparent.bean.VideoDetails;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.view.VideoDeatilView;
import com.shengzhe.disan.xuetangparent.mvp.view.VideoTypeView;
import com.shengzhe.disan.xuetangparent.mvp.view.ViedoItemView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 视频课业务 on 2018/4/26.
 */

public class VideoPresenter extends BasePresenter implements MVPRequestListener {
    private CourseModelImpl courseModel;
    private CommonModelImpl commonModel;

    private VideoTypeView viedoView;
    private ViedoItemView viedoItemView;
    private VideoDeatilView videoDeatilView;

    private VideoTypeView.IVideoView iViedoView;
    private ViedoItemView.IViedoItemView iViedoItemView;
    private VideoDeatilView.IVideoDeatilView iVideoDeatilView;


    public VideoPresenter(Context mContext, VideoTypeView.IVideoView iView) {
        super(mContext);
        this.iViedoView = iView;
    }

    public VideoPresenter(Context mContext, ViedoItemView.IViedoItemView iView) {
        super(mContext);
        this.iViedoItemView = iView;
    }

    public VideoPresenter(Context mContext, VideoDeatilView.IVideoDeatilView iView) {
        super(mContext);
        this.iVideoDeatilView = iView;
    }

    public void initVideoUi() {
        if (viedoView==null)
            viedoView = new VideoTypeView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iViedoView.getClass().getName());
        viedoView.setIVideoView(iViedoView);
    }

    public void initVideoItemUi() {
        if (viedoItemView==null)
            viedoItemView = new ViedoItemView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iViedoItemView.getClass().getName());
        viedoItemView.setIViedoItemView(iViedoItemView);
    }

    public void initVideoDeatilUi() {
        if (videoDeatilView==null)
            videoDeatilView = new VideoDeatilView(mContext);
        if (courseModel ==null)
            courseModel = new CourseModelImpl(mContext,this,iVideoDeatilView.getClass().getName());
        videoDeatilView.setIVideoDeatilView(iVideoDeatilView);
    }

    @Override
    public void onSuccess(int tager, Object objects, String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_VideoTypeList:
                //科目类型
                viedoView.setResultDatas(objects==null?new ArrayList<VideoType>():(List<VideoType>) objects);
                break;

            case IntegerUtil.WEB_API_VideoCourseList:
                //视频课列表
                viedoItemView.setResultDatas(objects==null?new BasePageBean< VideoBean >():(BasePageBean< VideoBean >) objects);
                break;

            case IntegerUtil.WEB_API_VideoDeatil:
                //视频课详情
                videoDeatilView.setResultDatas(objects==null?new VideoDetails():(VideoDetails) objects);
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg, String from) {
        switch (tager) {
            case IntegerUtil.WEB_API_VideoCourseList:
                //视频课列表
                viedoItemView.finishLoad();
                break;
        }
    }

    public void initVideoDatas(FragmentManager fragmentManager) {
        viedoView.initDatas(fragmentManager);
    }

    public void loadVideoList() {
        courseModel.getVideoTypeList();
    }

    public void initVideoItemDatas(int videotype,RefreshCommonView.RefreshLoadMoreListener listener) {
        viedoItemView.initDatas(videotype,listener);
    }

    public void loadVideoItemList(int pageNum) {
        courseModel.getVideoCourseList(SharedPreferencesManager.getCityId(),pageNum,viedoItemView.getVideoType());
    }

    public void clearVideoItemList() {
        viedoItemView.clearDatas();
    }

    public void getVideoDeatil(int courseId) {
       courseModel.getVideoDeatil(courseId);
    }
}
