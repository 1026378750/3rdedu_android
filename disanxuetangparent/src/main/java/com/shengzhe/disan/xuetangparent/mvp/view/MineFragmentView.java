package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

/**
 * Created by Administrator on 2018/4/17.
 */

public class MineFragmentView extends BaseView {

    public MineFragmentView(Context context) {
        super(context);
    }

    public void initUi(){
        this.view.getRefreshCommonView().addScrollViewChild(R.layout.fragment_mine_child);
        this.view.getPointView().setImageResource(R.drawable.point_select);
    }

    public void setIMineView(IMineView view){
        this.view = view;
    }

    private IMineView view;

    /*****
     * 设置网络返回数据
     * @param personal
     */
    public void setResultDatas(Personal personal) {
        this.view.getRefreshCommonView().finishRefresh();
        SharedPreferencesManager.setPersonaInfo(new Gson().toJson(personal));
        ImageUtil.loadCircleImageView(mContext, personal.getPhotoUrl(), this.view.getHeadimageView(), R.mipmap.ic_personal_avatar);
        this.view.getNiceNameView().setText(personal.getNickName() != null ? personal.getNickName() : SharedPreferencesManager.getUserInfo().getMobile());
        this.view.getPhoneNumView().setText(SharedPreferencesManager.getUserInfo().getMobile());
    }

    public void setErrorDatas() {
        this.view.getRefreshCommonView().finishRefresh();
    }

    public void setPointDatas(int visable) {
        this.view.getPointView().setVisibility(visable);
    }

    public void setDatas(RefreshCommonView.RefreshLoadMoreListener refreshListener, View.OnClickListener listener) {
        this.view.getHeadimageView().setOnClickListener(listener);
        this.view.getUsermesgView().setOnClickListener(listener);
        this.view.getMineOrderView().setOnClickListener(listener);
        this.view.getMineCourseView().setOnClickListener(listener);
        this.view.getMineteacherView().setOnClickListener(listener);
        this.view.getMineteacherSsistantView().setOnClickListener(listener);
        this.view.getMinewalletView().setOnClickListener(listener);
        this.view.getSettingView().setOnClickListener(listener);
        this.view.getMinemessageView().setOnClickListener(listener);
        this.view.getHelpView().setOnClickListener(listener);
        this.view.getRefreshCommonView().setIsLoadMore(false);
        this.view.getRefreshCommonView().setRefreshLoadMoreListener(refreshListener);
    }

    public interface IMineView{
        RefreshCommonView getRefreshCommonView();
        ImageView getHeadimageView();
        TextView getNiceNameView();
        TextView getPhoneNumView();
        TextView getUsermesgView();
        ImageView getPointView();
        TextView getMineOrderView();
        TextView getMineCourseView();
        CommonCrosswiseBar getMinemessageView();
        CommonCrosswiseBar getMineteacherView();
        CommonCrosswiseBar getMineteacherSsistantView();
        CommonCrosswiseBar getMinewalletView();
        CommonCrosswiseBar getHelpView();
        CommonCrosswiseBar getSettingView();
    }

}
