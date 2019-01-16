package com.shengzhe.disan.xuetangparent.mvp.presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import com.google.gson.Gson;
import com.main.disanxuelib.bean.Wallet;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.bean.MyBalance;
import com.shengzhe.disan.xuetangparent.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.shengzhe.disan.xuetangparent.bean.UserAssistant;
import com.shengzhe.disan.xuetangparent.mvp.activity.ModifyMessageActivity;
import com.shengzhe.disan.xuetangparent.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangparent.mvp.model.UserModelImpl;
import com.shengzhe.disan.xuetangparent.mvp.view.AddBankCardView;
import com.shengzhe.disan.xuetangparent.mvp.view.BankCardView;
import com.shengzhe.disan.xuetangparent.mvp.view.LoginView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineFragmentView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineTeacherView;
import com.shengzhe.disan.xuetangparent.mvp.view.MineWallView;
import com.shengzhe.disan.xuetangparent.mvp.view.MinesSistantView;
import com.shengzhe.disan.xuetangparent.mvp.view.ModifyMessageView;
import com.shengzhe.disan.xuetangparent.mvp.view.UserMessageView;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 用户公共业务处理层 on 2018/4/17.
 */

public class UserPresenter extends BasePresenter implements MVPRequestListener {
    private MineFragmentView mineView;
    private UserMessageView userView;
    private ModifyMessageView modifyView;
    private MineWallView mineWallView;
    private BankCardView bankCardView;
    private AddBankCardView addBankCardView;
    private MinesSistantView minesSistantView;
    private MineTeacherView mineTeacherView;
    private LoginView loginView;

    private MineFragmentView.IMineView iMineView;
    private UserMessageView.IUserMessageView iUserView;
    private ModifyMessageView.IModifyMessageView iModifyView;
    private MineWallView.IMineWallView iMineWallView;
    private BankCardView.IBankCardView iBankCardView;
    private AddBankCardView.IAddBankCardView iAddBankCardView;
    private MinesSistantView.IMinesSistantView iMinesSistantView;
    private MineTeacherView.IMineTeacherView iMineTeacherView;
    private LoginView.ILoginView iLoginView;

    private UserModelImpl userModel;
    private CommonModelImpl commonModel;

    public UserPresenter(Context context, MineFragmentView.IMineView iMineView) {
        super(context);
        this.iMineView = iMineView;
    }

    public UserPresenter(Context context, UserMessageView.IUserMessageView iUserView) {
        super(context);
        this.iUserView = iUserView;
    }

    public UserPresenter(Context context, ModifyMessageView.IModifyMessageView iModifyView) {
        super(context);
        this.iModifyView = iModifyView;
    }

    public UserPresenter(Context context, MineWallView.IMineWallView iMineWallView) {
        super(context);
        this.iMineWallView = iMineWallView;
    }

    public UserPresenter(Context mContext, BankCardView.IBankCardView iBankCardView) {
        super(mContext);
        this.iBankCardView = iBankCardView;
    }

    public UserPresenter(Context mContext, AddBankCardView.IAddBankCardView iAddBankCardView) {
        super(mContext);
        this.iAddBankCardView = iAddBankCardView;
    }

    public UserPresenter(Context mContext, MinesSistantView.IMinesSistantView iMinesSistantView) {
        super(mContext);
        this.iMinesSistantView = iMinesSistantView;
    }

    public UserPresenter(Context mContext, MineTeacherView.IMineTeacherView iMineTeacherView) {
        super(mContext);
        this.iMineTeacherView = iMineTeacherView;
    }
    public UserPresenter(Context context, LoginView.ILoginView iView) {
        super(context);
        this.iLoginView = iView;
    }


    /*****
     * 初始化我的组件
     */
    public void initMineUi() {
        if (mineView == null)
            mineView = new MineFragmentView(mContext);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iMineView.getClass().getName());
        mineView.setIMineView(iMineView);
        mineView.initUi();
    }

    /****
     * 用户中心组件
     */
    public void initUserUi() {
        if (userView == null)
            userView = new UserMessageView(mContext);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iUserView.getClass().getName());
        userView.setIUserMessageView(iUserView);
    }

    /***
     * 用户修改组件
     */
    public void initModifyUi() {
        if (modifyView == null)
            modifyView = new ModifyMessageView(mContext);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iModifyView.getClass().getName());
        modifyView.setIModifyMessageView(iModifyView);
    }

    public void initMineWalletUi(FragmentManager fragmentManager) {
        if (mineWallView == null)
            mineWallView = new MineWallView(mContext,fragmentManager);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iMineWallView.getClass().getName());
        mineWallView.setIMineWallView(iMineWallView);
    }

    public void initBankUi(FragmentManager fragmentManager) {
        if (bankCardView == null)
            bankCardView = new BankCardView(mContext,fragmentManager);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iBankCardView.getClass().getName());
        bankCardView.setIBankCardView(iBankCardView);
    }

    public void initAddBankCardUi(FragmentManager fragmentManager) {
        if (addBankCardView == null)
            addBankCardView = new AddBankCardView(mContext,fragmentManager);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iAddBankCardView.getClass().getName());
        addBankCardView.setIAddBankCardView(iAddBankCardView);
    }

    public void initMinesSistantUi(FragmentManager fragmentManager) {
        if (minesSistantView == null)
            minesSistantView = new MinesSistantView(mContext,fragmentManager);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iMinesSistantView.getClass().getName());
        minesSistantView.setIMinesSistantView(iMinesSistantView);
    }

    public void initMineTeacherUi() {
        if (mineTeacherView == null)
            mineTeacherView = new MineTeacherView(mContext);
        if (userModel == null)
            userModel = new UserModelImpl(mContext, this,iMineTeacherView.getClass().getName());
        mineTeacherView.setIMineTeacherView(iMineTeacherView);
    }
    public void setLoginUi() {
        if (loginView == null)
            loginView = new LoginView(mContext);
        if (commonModel == null)
            commonModel = new CommonModelImpl(mContext, this,iLoginView.getClass().getName());
        loginView.setILoginView(iLoginView);
    }


    /*****
     * 设置我的数据
     * @param refreshListener
     * @param listener
     */
    public void setMineDatas(RefreshCommonView.RefreshLoadMoreListener refreshListener, View.OnClickListener listener) {
        mineView.setDatas(refreshListener, listener);
    }


    public void setMineWalletDatas(RefreshCommonView.RefreshLoadMoreListener refreshListener) {
        mineWallView.setDatas(refreshListener);
    }

    /****
     * 获取网络数据
     */
    public void loadMineDatas() {
        userModel.getMessage();
        userModel.getMessageNum();
    }

    @Override
    public void onSuccess(int tager, Object objects,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_SendVerifyLogin:
                //获取验证码
                ToastUtil.showShort(objects==null?"":String.valueOf(objects));
                loginView.setVerifyResult();
                break;

            case IntegerUtil.WEB_API_SendLogin:
                //用户登录
                loginView.setLoginResult(objects==null?new User():(User)objects);
                break;

            case IntegerUtil.WEB_API_MyTeacherList:
                //我的老师列表
                mineTeacherView.finishLoad();
                mineTeacherView.setResultdatas(objects==null?new ArrayList<TeacherInformation>():(List< TeacherInformation >)objects);
                break;

            case IntegerUtil.WEB_API_MyMessage:
                //消息列表
                if (objects == null) {
                    mineView.setErrorDatas();
                    return;
                }
                mineView.setResultDatas(objects==null? new Personal(): (Personal) objects);
                break;

            case IntegerUtil.WEB_API_MinesSitant:
                //我的助教
                minesSistantView.setResultDatas(objects==null?new UserAssistant():(UserAssistant)objects);
                break;

            case IntegerUtil.WEB_API_MessageNum:
                //我的消息数量，为0表示没有
                mineView.setPointDatas((int) objects > 0 ? View.VISIBLE : View.GONE);
                break;

            case IntegerUtil.WEB_API_UpDateUser:
                if (AppManager.getAppManager().currentActivity() instanceof ModifyMessageActivity&&resultDate!=null){
                    resultDate.resultDate(-1,null);
                    return;
                }
                ToastUtil.showToast(objects==null?"":String.valueOf(objects));
                personal.setSex(sexInt);
                SharedPreferencesManager.setPersonaInfo(new Gson().toJson(personal));
                break;

            case IntegerUtil.WEB_API_WalletList:
                //我的钱包账户记录信息
                mineWallView.loadFinishDatas();
                mineWallView.setWalletList(objects==null?new ArrayList<Wallet>():(List<Wallet>)objects);
                break;

            case IntegerUtil.WEB_API_MyBalance:
                //我的钱包账户信息
                mineWallView.setBalance(objects==null? new MyBalance():(MyBalance)objects);
                break;

            case IntegerUtil.WEB_API_ExtractCash:
                //申请提现
                mineWallView.notifyDatas();
                break;

            case IntegerUtil.WEB_API_MyBankCard:
                //我的银行卡
                bankCardView.setMyBankMesg(objects==null? new MyBankCardBean():(MyBankCardBean)objects);
                break;

            case IntegerUtil.WEB_API_BindingBank:
                //绑定银行卡
                addBankCardView.setBindingBank();
                break;

            default:
                ToastUtil.showShort(objects==null?"":String.valueOf(objects));
                break;

        }
    }

    @Override
    public void onFailed(int tager, String mesg,String from) {
        switch (tager) {

            case IntegerUtil.WEB_API_MyTeacherList:
                //我的老师列表
                mineTeacherView.finishLoad();
                break;

            case IntegerUtil.WEB_API_MyMessage:
                mineView.setErrorDatas();
                break;

            case IntegerUtil.WEB_API_MessageNum:
                mineView.setPointDatas(View.GONE);
                break;

            case IntegerUtil.WEB_API_WalletList:
                //我的钱包账户记录信息
                mineWallView.loadFinishDatas();
                break;

            default:
                ToastUtil.showShort(mesg);
                break;

        }
    }

    /****
     * 设置用户信息
     * @param userDatas
     */
    public void setUserDatas(Personal userDatas) {
        userView.setDatas(userDatas);
    }

    private Personal personal;
    private int sexInt;

    /****
     * 保存用户性别
     * @param personal
     * @param sexInt
     */
    public void savePersonalSex(Personal personal, int sexInt) {
        this.personal = personal;
        this.sexInt = sexInt;
        Map<String, Object> map = new HashMap<>();
        map.put("studentSex", sexInt);
        userModel.upDateUser(map);
    }

    /****
     * 更新头像
     * @param photoUrl
     */
    public void upDateUserPhoto(String photoUrl) {
        userModel.upDateUserPhoto(photoUrl);
    }

    /****
     * 设置初始值
     * @param title
     * @param hintText
     * @param text
     */
    public void setModifyDatas(String title, String hintText, String text) {
        modifyView.setDatas(title, hintText, text);
    }

    /*****
     * 保存数据
     * @param titleText
     * @param text
     */
    public void savePersonalData(String titleText, String text) {
        Map<String, Object> map = new HashMap<>();
        switch (titleText) {
            case "昵称":
                map.put("nickName", text);
                break;
            case "学生姓名":
                map.put("studentName", text);
                break;
            case "目前就读学校":
                map.put("school", text);
                break;
        }
        userModel.upDateUser(map);
    }

    public void loadMineWalletDatas() {
        try {
            userModel.getWalletList(mineWallView.getSelectData());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadMyBalance() {
        userModel.getMyBalance();
    }

    public MyBalance getMyBlance(){
        return mineWallView.getMyBalance();
    }

    public void applyUserWithdrawals() {
        userModel.applyExtractCash(getMyBlance().getAvailAmount());
    }

    public void selectMyWalletDate() {
        mineWallView.selectDate();
    }

    public void setBankDatas(String bankNum) {
        bankCardView.setDatas(bankNum);
    }

    public void loadMyBankCard() {
        userModel.getMyBankCard();
    }

    public MyBankCardBean getBankCard() {
        return bankCardView.getBankCard();
    }

    public void callPhone(String mobile) {
        bankCardView.callPhone(mobile);
    }

    public void setAddBankCardDatas(MyBankCardBean addBankCardDatas) {
        addBankCardView.setDatas(addBankCardDatas);
    }

    public void selectBank() {
        addBankCardView.selectBank();
    }

    public void selectCity() {
        addBankCardView.selectCity();
    }

    public void bindingBank() {
        if(addBankCardView.buindingBank()){
            userModel.bindingBank(addBankCardView.getMyBankCard());
        }
    }

    public void setMinesSistantDatas() {
        minesSistantView.setDatas();
    }

    public void loadMinesSistant() {
        userModel.getMinesSitant();
    }

    public void callMinesSistantPhone() {
        minesSistantView.callPhone();
    }

    public void initMineTeacherDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        mineTeacherView.initDatas(listener);
    }

    public void loadMineTeacher() {
        userModel.getMyTeacherList();
    }

    public void setLoginDatas() {
        loginView.initDatas();
    }

    public boolean isShouldHideInput(View v, MotionEvent ev) {
        return loginView.isShouldHideInput(v,ev);
    }

    public void sendVerifyLogin() {
        if (loginView.isCheckVerify()) {
            commonModel.getSendVerifyLogin(loginView.getPhoneNum());
        }
    }

    public void sendUserLogin() {
        if (loginView.isCheck()) {
            ConstantUrl.TOKN = "";
            commonModel.sendLogin(loginView.getPhoneNum(),loginView.getSecurity());
        }
    }

}
