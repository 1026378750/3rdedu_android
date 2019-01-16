package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonEditView;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.shengzhe.disan.xuetangparent.utils.UmengEventUtils;

/**
 * Created by 登录 on 2018/4/25.
 */

public class LoginView extends BaseView implements CommonEditView.CommonEditInPutListener{
    private EditText tvLoginMobile;
    private String mSecurity;
    private String mPhone;
    //定时秒数
    private TimeCount time;

    public LoginView(Context context) {
        super(context);
    }

    private ILoginView iView;
    public void setILoginView(ILoginView iView){
        this.iView = iView;
    }

    @Override
    public void inputChange(int ViewId, String imput) {
        if (StringUtils.textIsEmpty(iView.getLoginMobileView().getContentText().trim()) || TextUtils.isEmpty(iView.getLoginPswView().getContentText().trim()))
            iView.getLoginSubmitView().setEnabled(false);
        else
            iView.getLoginSubmitView().setEnabled(true);
    }

    @Override
    public void cursorVanish(boolean islose) {

    }

    @Override
    public void clearAll() {
        iView.getLoginPswView().setContentText("");
        String text = iView.getLoginPswView().getRightText();
        if (text.equals("获取验证码")||(text.contains("s")&&Long.parseLong(text.substring(0,text.length()-1))<=0))
            return;
        time.cancel();
        iView.getLoginPswView().setRightClickable(true);// 倒数一秒的时候 获得点击
        iView.getLoginPswView().setRightText("获取验证码");
    }

    public void initDatas() {
        ConstantUrl.TOKN = "";
        SharedPreferencesManager.clearDatas();
        iView.getLoginMobileView().setInputType(InputType.TYPE_CLASS_PHONE);
        iView.getLoginPswView().setInputType(InputType.TYPE_CLASS_NUMBER);
        tvLoginMobile = (EditText) iView.getLoginMobileView().getContentView();
        iView.getLoginMobileView().setOnInputListener(this);
        iView.getLoginPswView().setOnInputListener(this);
        // 隐藏软键盘 //
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(iView.getLoginPswView().getWindowToken(), 0);
        imm.hideSoftInputFromWindow(iView.getLoginMobileView().getWindowToken(), 0);

        time = new TimeCount(60000, 1000);
        iView.getLoginSubmitView().setEnabled(false);
    }

    public boolean isCheckVerify() {
        mPhone = iView.getLoginMobileView().getContentText().trim();
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtil.topToast("请输入手机号！");
            setEtLoginMobile();
            return false;
        }
        if (!RegUtil.checkPhone(mPhone)) {
            ToastUtil.topToast("手机号有误，请重新输入");
            setEtLoginMobile();
            return false;
        }
        return true;
    }

    /**
     * 聚焦
     */
    public void setEtLoginMobile() {
        tvLoginMobile.setFocusable(true);
        tvLoginMobile.setFocusableInTouchMode(true);
        tvLoginMobile.requestFocus();
    }

    /**
     * 检查是不是正确
     *
     * @return
     */
    public boolean isCheck() {
        mPhone = iView.getLoginMobileView().getContentText().trim();
        mSecurity = iView.getLoginPswView().getContentText().trim();
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtil.topToast("请输入手机号！");
            setEtLoginMobile();
            return false;
        }
        if (!RegUtil.checkPhone(mPhone)) {
            ToastUtil.topToast("手机号有误，请重新输入");
            setEtLoginMobile();
            return false;
        }
        if (TextUtils.isEmpty(mSecurity)) {
            ToastUtil.topToast("请您输入验证码！");
            return false;
        }
        if (!RegUtil.isSecurity(mSecurity)) {
            ToastUtil.topToast("验证码输入有误");
            return false;
        }
        return true;
    }

    public String getPhoneNum() {
        return mPhone;
    }

    public void setVerifyResult() {
        //倒计时
        time.start();
    }

    public String getSecurity() {
        return mSecurity;
    }

    public void setLoginResult(User user) {
        ConstantUrl.TOKN = user.getToken();
        SharedPreferencesManager.setUserInfo(user);
        ToastUtil.showToast("登录成功！");
        LoginOpentionUtil.getInstance().LoginResult(mPhone);
        UmengEventUtils.toLoginClick(mContext, mPhone);
        AppManager.getAppManager().currentActivity().onBackPressed();
    }

    /**
     * 倒计时的内部类
     *
     * @author Administrator 只要继承CountDownTimer 自会重写继承构造，方法
     */
    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            iView.getLoginPswView().setRightClickable(false);// 设置不可点击
            iView.getLoginPswView().setRightText(millisUntilFinished / 1000 + "s");
            if (millisUntilFinished / 1000 <= 1) {
                iView.getLoginPswView().setRightClickable(true);// 倒数一秒的时候 获得点击
                iView.getLoginPswView().setRightText("获取验证码");
            }
        }

        @Override
        public void onFinish() {
            iView.getLoginPswView().setRightEnabled(true);
        }
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    public interface ILoginView{
        CommonEditView getLoginMobileView();
        CommonEditView getLoginPswView();
        Button getLoginSubmitView();
    }

}
