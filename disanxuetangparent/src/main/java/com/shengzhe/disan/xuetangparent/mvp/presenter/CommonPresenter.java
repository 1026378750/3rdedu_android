package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.adapter.CourseExpandableAdapter;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.fragment.ScreenConditionFragment;
import com.shengzhe.disan.xuetangparent.mvp.view.HeadSearchView;
import com.shengzhe.disan.xuetangparent.mvp.view.OpenCityView;
import com.shengzhe.disan.xuetangparent.mvp.view.ScreenConditionView;
import com.shengzhe.disan.xuetangparent.mvp.view.SelectGradeView;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.model.UserModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.view.AddressCommonView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineMessageView;
import com.shengzhe.disan.xuetangparent.mvp.view.SettingView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.shengzhe.disan.xuetangparent.utils.UmengEventUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 公共业务处理 on 2018/4/17.
 */

public class CommonPresenter extends BasePresenter implements MVPRequestListener {
    private AddressCommonView addressView;
    private SettingView settingView;
    private MineMessageView mineMessageView;
    private HeadSearchView headSearchView;
    private OpenCityView openCityView;
    private SelectGradeView selectGradeView;
    private ScreenConditionView screenConditionView;

    private AddressCommonView.IAddressCommonView iAddressView;
    private SettingView.ISettingView iSettingView;
    private MineMessageView.IMineMessageView iMineMessageView;
    private HeadSearchView.IHeadSearchView iHeadSearchView;
    private OpenCityView.IOpenCityView iOpenCityView;
    private SelectGradeView.ISelectGradeView iSelectGradeView;
    private ScreenConditionView.IScreenConditionView iScreenConditionView;

    private CommonModelImpl commonModel;
    private UserModelImpl userModel;

    public CommonPresenter(Context context, AddressCommonView.IAddressCommonView iView) {
        super(context);
        this.iAddressView = iView;
    }

    public CommonPresenter(Context context, SettingView.ISettingView iView) {
        super(context);
        this.iSettingView = iView;
    }

    public CommonPresenter(Context context, MineMessageView.IMineMessageView iView) {
        super(context);
        this.iMineMessageView = iView;
    }

    public CommonPresenter(Context context, HeadSearchView.IHeadSearchView iView) {
        super(context);
        this.iHeadSearchView = iView;
    }

    public CommonPresenter(Context context, OpenCityView.IOpenCityView iView) {
        super(context);
        this.iOpenCityView = iView;
    }

    public CommonPresenter(Context context, SelectGradeView.ISelectGradeView iView) {
        super(context);
        this.iSelectGradeView = iView;
    }

    public CommonPresenter(Context context, ScreenConditionView.IScreenConditionView iView) {
        super(context);
        this.iScreenConditionView = iView;
    }

    public void initAddressUi(FragmentManager manager) {
        if (addressView == null)
            addressView = new AddressCommonView(mContext, manager);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iAddressView.getClass().getName());
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iAddressView.getClass().getName());
        addressView.setIAddressCommonView(iAddressView);
    }

    public void initSettingUi(FragmentManager manager) {
        if (settingView == null)
            settingView = new SettingView(mContext, manager);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iSettingView.getClass().getName());
        settingView.setISettingView(iSettingView);
    }

    public void initMineMessageUi() {
        if (mineMessageView == null)
            mineMessageView = new MineMessageView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iMineMessageView.getClass().getName());
        mineMessageView.setIMineMessageView(iMineMessageView);
    }

    public void initHeadSearchUi() {
        if (headSearchView == null)
            headSearchView = new HeadSearchView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iHeadSearchView.getClass().getName());
        headSearchView.setIHeadSearchView(iHeadSearchView);
    }

    public void initOpenCityUi() {
        if (openCityView == null)
            openCityView = new OpenCityView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iOpenCityView.getClass().getName());
        openCityView.setIOpenCityView(iOpenCityView);
    }

    public void initSelectGradeUi() {
        if (selectGradeView == null)
            selectGradeView = new SelectGradeView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iSelectGradeView.getClass().getName());
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iSelectGradeView.getClass().getName());
        selectGradeView.setISelectGradeView(iSelectGradeView);
    }

    public void initScreenConditionUi() {
        if (screenConditionView == null)
            screenConditionView = new ScreenConditionView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iScreenConditionView.getClass().getName());
        screenConditionView.setIScreenConditionView(iScreenConditionView);
    }

    public void setAddressDatas(Address address) {
        addressView.setDatas(address);
    }

    public void setSettingData() {
        settingView.setDatas();
    }

    public void loadSettingDatas() {
        isCanSignin = false;
        commonModel.getSigninStatus();
    }

    public boolean isCanSignin() {
        return isCanSignin;
    }


    public boolean isCanReturn() {
        return settingView.isCanReturn();
    }

    public void setSigninStatus() {
        //个人设置是否开启签到通知 1：是 2：否
        commonModel.upDateSigninStatus(iSettingView.getSigninView().isChecked() ? 2 : 1);
    }

    public void loginOut() {
        commonModel.getCommonLogout();
    }

    public void checkVersion() {
        settingView.checkVersion();
    }

    public boolean isMustUpdate() {
        return settingView.isMustUpdate();
    }

    public void loadAddressDatas() {
        commonModel.getOpenCityList();
    }

    public void selectCity() {
        addressView.selectCity();
    }

    public void saveCity(Address address) {
        Map<String, Object> map = new HashMap<>();
        map.put("city", String.valueOf(address.cityId).length()>4?String.valueOf(address.cityId).substring(0,4):String.valueOf(address.cityId));//市
        map.put("area", address.districtId);//区、县
        map.put("address", address.address);
        userModel.upDateUser(map);
    }

    public Address getAddress() {
        return addressView.getAddress();
    }

    public void initMineMessageDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        mineMessageView.initDatas(listener);
    }

    public ArrayList<Message> getMessageList() {
        return mineMessageView.getMessageList();
    }

    public void loadMineMessage(int pageNum) {
        commonModel.getCommonMessage(pageNum);
    }

    private boolean isCanSignin = false;

    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_CityByCode:
                //获取城市
                screenConditionView.setResultDatas(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_GradeList:
                //获取年级数据
                if (iSelectGradeView!=null&&from.equals(iSelectGradeView.getClass().getName()))
                    selectGradeView.setResultDatas(objects==null?"":String.valueOf(objects));
                else if (iScreenConditionView!=null&&from.equals(iScreenConditionView.getClass().getName()))
                    screenConditionView.setResultGradeDatas(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_SearchList:
                //搜索
                headSearchView.setResultDatas(objects==null?new BasePageBean<TeacherInformation>():(BasePageBean<TeacherInformation>)objects);
                break;

            case IntegerUtil.WEB_API_CommonMessage:
                //消息列表
                mineMessageView.setResultDatas(objects == null ? new BasePageBean<Message>() : (BasePageBean<Message>) objects);
                break;

            case IntegerUtil.WEB_API_OpenCity:
                //地址查询
                if (iOpenCityView!=null&&from.equals(iOpenCityView.getClass().getName()))
                    openCityView.setResultDatas(objects==null?"":String.valueOf(objects));
                else if (iAddressView!=null&&from.equals(iAddressView.getClass().getName()))
                    addressView.setResultDatas(objects==null?"":String.valueOf(objects));
                break;

            case IntegerUtil.WEB_API_UpDateUser:
                if (iAddressView!=null&&from.equals(iAddressView.getClass().getName())) {
                    //更新地址信息
                    ToastUtil.showShort(objects==null?"":String.valueOf(objects));
                    Bundle addressBundle = new Bundle();
                    addressBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11011);
                    addressBundle.putParcelable(StringUtils.EVENT_DATA, getAddress());
                    EventBus.getDefault().post(addressBundle);
                    AppManager.getAppManager().currentActivity().onBackPressed();
                }else if (iSelectGradeView!=null&&from.equals(iSelectGradeView.getClass().getName())){
                    ToastUtil.showShort(objects==null?"":String.valueOf(objects));
                    selectGradeView.showEvbus();
                }
                break;

            case IntegerUtil.WEB_API_SigninStatus:
                //通知
                UmengEventUtils.toLogoutClick(mContext, SharedPreferencesManager.getUserInfo().getMobile());
                isCanSignin = true;
                settingView.setSigninStatus(objects == null ? false : (boolean) objects);
                break;

            case IntegerUtil.WEB_API_UpDateSigninStatus:
                //更新通知
                ToastUtil.showShort(objects==null?"":String.valueOf(objects));
                settingView.setSigninStatus(!iSettingView.getSigninView().isChecked());
                break;

            case IntegerUtil.WEB_API_CommonLogout:
                //退出
                UmengEventUtils.toLogoutClick(mContext, SharedPreferencesManager.getUserInfo().getMobile());
                ConstantUrl.TOKN = "";
                SharedPreferencesManager.clearDatas();
                AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                Bundle loginOutBundle = new Bundle();
                loginOutBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11007);
                EventBus.getDefault().post(loginOutBundle);
                break;

            default:
                ToastUtil.showShort(objects==null?"":String.valueOf(objects));
                break;
        }
    }

    @Override
    public void onFailed(int tager, String mesg ,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_CommonMessage:
                //消息列表
                mineMessageView.finishLoad();
                break;

            case IntegerUtil.WEB_API_SearchList:
                //搜索
                headSearchView.finishLoad();
                break;

            case IntegerUtil.WEB_API_OpenCity:
                //地址查询
                if (from.equals(iOpenCityView.getClass().getName()))
                    openCityView.finishLoad();
                else
                    ToastUtil.showShort(mesg);
                break;

            default:
                ToastUtil.showShort(mesg);
                break;
        }
    }

    public void initHeadSearchDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        headSearchView.initDatas(listener);
    }

    public List<TeacherInformation> getHeadsearchDatas() {
        return headSearchView.getDatasList();
    }

    public void loadHeadSearcherDatas(int pageNum) {
        commonModel.getSearchList(headSearchView.getInputText(),SharedPreferencesManager.getCityId(),pageNum);
    }

    public void searcherDatas() {
        headSearchView.seacherDatas();
    }

    public void initOpenCityDatas(String stringExtra,RefreshCommonView.RefreshLoadMoreListener listener) {
        openCityView.initDatas(stringExtra,listener);
    }

    public void openCityStop() {
        openCityView.stop();
    }

    public void openCityDestroy() {
        openCityView.destroy();
    }

    public void openCityJump() {
        openCityView.jumpActivity();
    }

    public void openCityRest() {
        openCityView.resrLocal();
    }

    public void openCitystartLoacl() {
        openCityView.startLoacl();
    }

    public void loadOpencityAddress() {
        commonModel.getOpenCityList();
    }

    public void initSelectGradeDatas(CourseType type, int fatherId, CourseExpandableAdapter.SelectTagListener listener) {
        selectGradeView.initDatas(fatherId,type,listener);
    }

    public void getGradeListAll() {
        commonModel.getGradeList();
    }

    public void initScreenConditionDatas(String from,String tag,ScreenConditionFragment.SelectListener listener) {
        screenConditionView.initDatas(from,tag,listener);
    }

    public Map<Integer,CourseType> getSelectMapDatas() {
        return screenConditionView.getSelectMapDatas();
    }

    public void getAreaCityByCode() {
        commonModel.getCityByCode(SharedPreferencesManager.getCityId());
    }

    public void getScreenConditionGradeListAll() {
        commonModel.getGradeList();
    }

    public void saveGradePersonalData(CourseType type, int fatherId) {
        selectGradeView.setGradePersonalData(type,fatherId);
        Map<String, Object> map = new HashMap<>();
        map.put("grade", type.id);
        userModel.upDateUser(map);
    }
}
